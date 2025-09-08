package com.example.janitri_task.utils

import android.content.Context

fun Context.isServiceRunning(serviceClass: Class<*>): Boolean {
    val manager = getSystemService(Context.ACTIVITY_SERVICE) as android.app.ActivityManager
    @Suppress("DEPRECATION")
    return manager.getRunningServices(Int.MAX_VALUE)
        .any { it.service.className == serviceClass.name }
}

fun formatTimestamp(timestamp: Long): String {
    val formatter = java.text.SimpleDateFormat("EEE, dd MMM yyyy hh:mm a", java.util.Locale.getDefault())
    return formatter.format(java.util.Date(timestamp))
        .replace("AM", "am")
        .replace("PM", "pm")
}