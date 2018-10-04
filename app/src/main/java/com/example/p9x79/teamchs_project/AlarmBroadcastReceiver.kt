package com.example.p9x79.teamchs_project

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import org.jetbrains.anko.toast

class AlarmBroadcastReceiver:BroadcastReceiver() {
    override  fun onReceive(context: Context?,intent: Intent?){
      val intent = Intent(context, MainActivity::class.java)
              .putExtra("onReceive", true)
              .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context?.startActivity(intent)
    }
}
