package com.example.service

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.service.databinding.ActivityDemoBinding
import com.example.service.service.*

class DemoActivity : AppCompatActivity() {

    private var mService: Messenger? = null

    private var bound: Boolean = false

    private val mConnection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, service: IBinder?) {
            Toast.makeText(applicationContext, "onServiceConnected", Toast.LENGTH_SHORT).show()
            mService = Messenger(service)
            bound = true
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            mService = null
            bound = false
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // intent service
        val btnStartIntentService = findViewById<Button>(R.id.btnStartIntentService)
        btnStartIntentService.setOnClickListener {
            MyIntentService.startActionFoo(this, "", "")
        }

        // unbound service
        // background service
        val btnStartUnboundService = findViewById<Button>(R.id.btnStartUnBoundService)
        btnStartUnboundService.setOnClickListener {
            Intent(this, MyService::class.java).also {
                startService(it)
            }
        }

        val btnStopUnboundService = findViewById<Button>(R.id.btnStopUnBoundService)
        btnStopUnboundService.setOnClickListener {
            Intent(this, MyService::class.java).also {
                stopService(it)
            }
        }

        // bound service
        // dạng service client connect
        val btnStartBoundService = findViewById<Button>(R.id.btnStartBoundService)
        btnStartBoundService.setOnClickListener {
            Intent(this, BoundService::class.java).also {
                bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
            }
        }

        val btnStopBoundService = findViewById<Button>(R.id.btnStopBoundService)
        btnStopBoundService.setOnClickListener {
            if (bound) {
                unbindService(mConnection)
                bound = false
            }
        }

        val btnSendMessage = findViewById<Button>(R.id.btnSendMessage)
        btnSendMessage.setOnClickListener {
            val msg: Message = Message.obtain(null, MSG_SAY_HELLO, 0,0)
            try {
                mService?.send(msg)
            } catch (e : RemoteException) {
                e.printStackTrace()
            }
        }

        // Foreground
        // trên android 8
        // chạy treo noti
        val btnStartForegroundService = findViewById<Button>(R.id.StartForegroundService)
        btnStartForegroundService.setOnClickListener {
            val intent = Intent(this, ForegroundService::class.java)
            startForegroundService(intent)
        }

    }
}