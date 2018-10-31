package com.example.p9x79.teamchs_project

import android.annotation.TargetApi
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.view.WindowManager.LayoutParams
import android.view.WindowManager.LayoutParams.*
import android.widget.TextView
import com.kwabenaberko.openweathermaplib.Lang
import com.kwabenaberko.openweathermaplib.Units
import com.kwabenaberko.openweathermaplib.implementation.OpenWeatherMapHelper
import com.kwabenaberko.openweathermaplib.models.currentweather.CurrentWeather
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast
import java.lang.IllegalArgumentException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity()
        , SimpleAlertDialog.OnClickListener
        , DatePickerFragment.OnDataSelectedListener
        , TimePickerFragment.OnTimeSelectedListener{

    var test_data = ""

    override fun onSelected(year: Int, month: Int, date: Int) {
        val c = Calendar.getInstance()
        c.set(year, month, date)
        dateText.text = DateFormat.format("yyyy/MM/dd", c)
    }

    override fun onSelected(hourOfDay: Int, minute: Int) {
        timeText.text = "%1$02d:%2$02d".format(hourOfDay, minute)
    }

    override fun onPositiveClick() {
        finish()
    }

    override fun onNegativeClick() {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.add(Calendar.MINUTE, 5)
        setAlarmManager(calendar)
        finish()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //val helper = OpenWeatherMapHelper()
        //helper.setApiKey("5e6b7a32304944d415210fb613d55478")
        //helper.setUnits(Units.IMPERIAL)
        //helper.setLang(Lang.ENGLISH)
        //val textSetting = findViewById<TextView>(R.id.textSetting)
        //helper.getCurrentWeatherByCityName("Tokyo", object : OpenWeatherMapHelper.CurrentWeatherCallback {

            //override fun onSuccess(currentWeather: CurrentWeather) {
                //var test = ("Coordinates: " + currentWeather.coord.lat + ", " + currentWeather.coord.lon + "\n"
                        //+ "Weather Description: " + currentWeather.weatherArray[0].description + "\n"
                        //+ "Max Temperature: " + currentWeather.main.tempMax + "\n"
                        //+ "Wind Speed: " + currentWeather.wind.speed + "\n"
                        //+ "City, Country: " + currentWeather.name + ", " + currentWeather.sys.country)
                //Log.v("Test_1", test)
                //test_data = test
                //textSetting.text = test_data
            //}

            //override fun onFailure(throwable: Throwable) {
                //Log.v("Test_1", throwable.message)
            //}
        //})

        if (intent?.getBooleanExtra("onReceive", false) == true) {
            when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ->
                    window.addFlags(FLAG_TURN_SCREEN_ON or
                            FLAG_SHOW_WHEN_LOCKED)
                else ->
                    window.addFlags(FLAG_TURN_SCREEN_ON or
                            FLAG_SHOW_WHEN_LOCKED or FLAG_DISMISS_KEYGUARD)
            }
            val dialog = SimpleAlertDialog()
            dialog.show(supportFragmentManager, "alert_dialog")
        }

        setContentView(R.layout.activity_main)


        setAlarm.setOnClickListener {
            val date = "${dateText.text} ${timeText.text}".toDate()
            when {
                date != null -> {
                    val calendar = Calendar.getInstance()
                    calendar.time = date
                    setAlarmManager(calendar)
                    toast("アラームをセットしました")
                }
                else -> {
                    toast("日付の形式が正しくありません")
                }
            }
        }

        cancelAlarm.setOnClickListener {
            cancelAlarmManager()
        }

        dateText.setOnClickListener {
            val dialog = DatePickerFragment()
            dialog.show(supportFragmentManager, "date_dialog")
        }

        timeText.setOnClickListener {
            val dialog = TimePickerFragment()
            dialog.show(supportFragmentManager, "time_dialog")
        }

        val helper = OpenWeatherMapHelper()
        helper.setApiKey("5e6b7a32304944d415210fb613d55478")
        helper.setUnits(Units.IMPERIAL)
        helper.setLang(Lang.ENGLISH)
        val textSetting = findViewById<TextView>(R.id.textSetting)
        helper.getCurrentWeatherByCityName("Tokyo", object : OpenWeatherMapHelper.CurrentWeatherCallback {

            override fun onSuccess(currentWeather: CurrentWeather) {
                var test = ("Coordinates: " + currentWeather.coord.lat + ", " + currentWeather.coord.lon + "\n"
                        + "Weather Description: " + currentWeather.weatherArray[0].description + "\n"
                        + "Max Temperature: " + currentWeather.main.tempMax + "\n"
                        + "Wind Speed: " + currentWeather.wind.speed + "\n"
                        + "City, Country: " + currentWeather.name + ", " + currentWeather.sys.country)
                Log.v("Test_1", test)
                test_data = test
                textSetting.text = test_data
            }

            override fun onFailure(throwable: Throwable) {
                Log.v("Test_1", throwable.message)
            }
        })

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setAlarmManager(calendar: Calendar) {
        val am = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmBroadcastReceiver::class.java)
        val pending = PendingIntent.getBroadcast(this, 0, intent, 0)
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> {
                val info = AlarmManager.AlarmClockInfo(
                        calendar.timeInMillis, null)
                am.setAlarmClock(info, pending)
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT -> {
                am.setExact(AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis, pending)
            }
            else -> {
                am.set(AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis, pending)
            }
        }

    }
    private  fun cancelAlarmManager(){
        val am =getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmBroadcastReceiver::class.java)
        val pending = PendingIntent.getBroadcast(this, 0, intent, 0)
        am.cancel(pending)
    }

    fun String.toDate(pattern: String = "yyyy/MM/dd HH:mm"): Date? {
        val sdFormat = try {
            SimpleDateFormat(pattern)
        } catch (e: IllegalArgumentException) {
            null

        }
        val date = sdFormat?.let {
            try {
                it.parse(this)
            } catch (e: ParseException) {
                null

            }
        }
        return date
    }
}

