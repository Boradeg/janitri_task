package com.example.janitri_task.presentation.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.janitri_task.R
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

class TimerService : Service() {
    private var timerJob: Job? = null

    private val serviceScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    companion object {
        const val ACTION_TIMER_TICK = "com.example.janitri_task.TIMER_TICK"
        const val EXTRA_TIME = "extra_time"
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForegroundNotification()
        startTimer()
        return START_STICKY
    }

    private fun startForegroundNotification() {
        val channelId = "timer_channel"
        val channelName = "Timer Service"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val chan = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW)
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(chan)
        }

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Timer Running")
            .setContentText("Time updates every second")
            .setSmallIcon(R.drawable.ic_heart_rate) // Replace with your icon
            .build()

        startForeground(1, notification)
    }

    private fun startTimer() {
        if (timerJob?.isActive == true) return
        timerJob =  serviceScope.launch {
            while (isActive) {
                val currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
                val intent = Intent(ACTION_TIMER_TICK).apply {
                    putExtra(EXTRA_TIME, currentTime)
                }
                sendBroadcast(intent)
                delay(1000)
            }
        }
    }

    override fun onDestroy() {
        serviceScope.cancel() // Cancel coroutines when service is destroyed
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
