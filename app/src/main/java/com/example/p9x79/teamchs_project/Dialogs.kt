package com.example.p9x79.teamchs_project

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import org.jetbrains.anko.toast
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