package com.example.service.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.widget.Toast

const val MSG_SAY_HELLO = 1

class BoundService : Service() {

    private lateinit var mMessager : Messenger

    private class IncomingHandler (
        context: Context,
        private val applicationContext: Context = context.applicationContext
    ) : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what){
                MSG_SAY_HELLO -> Toast.makeText(applicationContext, "HELLO", Toast.LENGTH_SHORT).show()
                else -> super.handleMessage(msg)
            }
        }
    }

    override fun onBind(intent: Intent): IBinder {
        mMessager = Messenger(IncomingHandler(this))
        return mMessager.binder
    }

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(applicationContext, "Bound Service Destroy", Toast.LENGTH_SHORT).show()
    }


}