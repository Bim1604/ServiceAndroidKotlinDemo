package com.example.service.service

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.os.Process
import android.util.Log

class MyService : Service() {

    private var serviceLooper: Looper? = null
    private var serviceHandler: ServiceHandler? = null

    override fun onBind(intent: Intent): IBinder? {
        // phân biệt đâu là bound đâu là unbound
        // background service bthuong hay unbound thì se trả về null
        return null
    }

    override fun onCreate() {
        Log.d("MyService", "onCreate")
        // khởi tạo 1 thread mới
        HandlerThread("ServiceStartArguments", Process.THREAD_PRIORITY_BACKGROUND).apply {
            start()
            serviceLooper = looper
            serviceHandler = ServiceHandler(looper)
        }
        super.onCreate()
    }

    private inner class ServiceHandler (looper: Looper) : Handler(looper) {
        override fun handleMessage(msg: Message) {
            try {
                Thread.sleep(5000)
            } catch (e : InterruptedException) {
                Thread.currentThread().interrupt()
            }
            super.handleMessage(msg)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // nơi hứng message
        serviceHandler?.obtainMessage()?.also { msg ->
            msg.arg1 = startId
            serviceHandler?.sendMessage(msg)
        }

        return START_STICKY
    }

    override fun onDestroy() {
        Log.d("MyService", "onDestroy")
        super.onDestroy()
    }
}