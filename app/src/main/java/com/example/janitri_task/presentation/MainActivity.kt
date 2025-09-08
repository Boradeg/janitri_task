package com.example.janitri_task.presentation

import com.example.janitri_task.presentation.service.TimerService
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.janitri_task.presentation.home_screen.MainScreen
import com.example.janitri_task.presentation.home_screen.TimerViewModel
import com.example.janitri_task.ui.theme.Janitri_taskTheme
import com.example.janitri_task.utils.isServiceRunning
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: TimerViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Let Compose draw behind the system bars

        //WindowCompat.setDecorFitsSystemWindows(window, false)
        val running = isServiceRunning(TimerService::class.java)
        viewModel.setServiceRunning(running)
        //  Request notification permission (Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    100
                )
            }
        }

        setContent {
            Janitri_taskTheme {
                MainScreen(viewModel)
            }
        }
    }
}


