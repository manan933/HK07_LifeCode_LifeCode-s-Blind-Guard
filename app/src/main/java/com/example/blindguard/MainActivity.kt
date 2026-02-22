package com.example.blindguard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Color
import com.example.blindguard.ui.theme.BlindguardTheme
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.systemBars
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface

import androidx.compose.material3.MaterialTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Notification channel (required Android 8+)
        val channel = android.app.NotificationChannel(
            "blindguard",
            "BlindGuard Alerts",
            android.app.NotificationManager.IMPORTANCE_HIGH
        )
        val manager = getSystemService(android.app.NotificationManager::class.java)
        manager.createNotificationChannel(channel)

        setContent {
            BlindguardTheme {
                Surface(

                    color = Color(0xFF0F0F0F),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(WindowInsets.systemBars.asPaddingValues())
                ) {
                    HomeScreen()
                }
            }
        }
    }
}