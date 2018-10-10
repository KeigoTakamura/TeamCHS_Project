package com.example.p9x79.teamchs_project

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.location.OnNmeaMessageListener
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.widget.DatePicker
import android.widget.TimePicker
import org.jetbrains.anko.toast
import java.time.Month
import java.time.MonthDay
import java.util.*

class SimpleAlertDialog : DialogFragment() {

    interface OnClickLIstener{
        fun onPositiveClick()
        fun onNegativeClick()
    }

    private lateinit var listener: OnClickLIstener

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is SimpleAlertDialog.OnClickLIstener){
            listener = context
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val context = context
        if (context == null)
            return super.onCreateDialog(savedInstanceState)
        val builder = AlertDialog.Builder(context).apply{
            setMessage("時間になりました！　")
            setPositiveButton("起きる"){ dialog, which ->
                listener.onPositiveClick()
            }
            setNegativeButton("あと５分") { dialog, which ->
                listener.onNegativeClick()
            }
        }
        return builder.create()
    }
}

class DatePickerFragment : DialogFragment(),
            DatePickerDialog.OnDateSetListener{

    interface   onDataSelectedListener {
        fun onSelected(year: Int, month: Int,date:Int)
    }
    private  lateinit var  listener: onDataSelectedListener
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if(context is  onDataSelectedListener){
            listener = context
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?) :Dialog{
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val date = c.get(Calendar.DAY_OF_MONTH)
        return DatePickerDialog(context, this,year,month,date)
    }

    override fun onDateSet(view: DatePicker, year:
            Int,month: Int, date: Int) {
        listener.onSelected(year, month, date)
    }

}


class  TimePickerFragment : DialogFragment(),
        TimePickerDialog.OnTimeSetListener{
            interface  OntimeSelectedListener{
                fun onSelected(hourodDay: Int,minute:Int)
            }
        private  lateinit var  listener: TimePickerDialog.OnTimeSetListener

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if(context is TimePickerFragment.OntimeSelectedListener){
            listener = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute= c.get(Calendar.MINUTE)

        return  TimePickerDialog(context,this,hour,minute,true)
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        listener.onSelected(hourOfDay,minute)
    }

}
