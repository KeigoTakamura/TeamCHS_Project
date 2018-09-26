package com.example.p9x79.teamchs_project

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import org.jetbrains.anko.toast

class AlarmBroadcastReceiver:BroadcastReceiver() {
    override  fun onReceive(context: Context?,intent: Intent?){
      context?.toast("アラームを受信しました")
    }
}
