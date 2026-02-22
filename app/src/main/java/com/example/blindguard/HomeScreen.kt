package com.example.blindguard

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun HomeScreen() {
    var lastShownAlertId by remember { mutableStateOf<Int?>(null) }
    val context = LocalContext.current
    val repository = remember { AlertRepository(context) }
    val alerts by repository.getAlerts().collectAsStateWithLifecycle(initialValue = emptyList())

    var popupAlert by remember { mutableStateOf<AlertRecord?>(null) }
    var lastShownId by remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(alerts) {
        val newest = alerts.firstOrNull()

        if (newest != null && newest.id != lastShownAlertId) {
            popupAlert = newest
            lastShownAlertId = newest.id
        }
    }

    popupAlert?.let { alert ->

        val bgColor =
            if (alert.type == "SOS") Color(0xFFD7263D)
            else Color(0xFFFF8C42)

        AlertDialog(
            onDismissRequest = { popupAlert = null },

            confirmButton = {
                TextButton(onClick = {
                    val uri = Uri.parse("geo:${alert.latitude},${alert.longitude}?q=${alert.latitude},${alert.longitude}")
                    context.startActivity(Intent(Intent.ACTION_VIEW, uri))
                    popupAlert = null
                }) {
                    Text("OPEN MAP", color = Color.White)
                }
            },

            title = { Text("BlindGuard Alert", color = Color.White) },

            text = {
                Text(
                    if (alert.type == "SOS")
                        "SOS triggered by the user."
                    else
                        "Fall detected.",
                    color = Color.White
                )
            },

            containerColor = bgColor
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        Text("Hello, Manan Patel", fontSize = 26.sp, color = Color(0xFFFFD60A))
        Text("Not all Heroes wear capes.", color = Color.LightGray)

        Spacer(modifier = Modifier.height(20.dp))

        StatusCard()

        Spacer(modifier = Modifier.height(20.dp))

        Text("Recent Safety Records", fontSize = 20.sp, color = Color.White)

        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn {
            items(alerts) { alert ->
                AlertItem(alert)
            }
        }
    }
}

@Composable
fun StatusCard() {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1C1C1E)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text("System Status: Stable", color = Color.White)
            Text("No emergency activity detected.", color = Color.Gray)
        }
    }
}

@Composable
fun AlertItem(alert: AlertRecord) {

    val color =
        if (alert.type == "SOS") Color(0xFFD7263D)
        else Color(0xFFFF8C42)

    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1C1C1E)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text(
                if (alert.type == "SOS") "SOS Triggered" else "Fall Detected",
                color = color,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(formatTime(alert.timestamp), color = Color.LightGray)

            Spacer(modifier = Modifier.height(6.dp))

            Text("${alert.latitude}, ${alert.longitude}", color = Color.Gray)
        }
    }
}
fun formatTime(time: Long): String {
    val sdf = java.text.SimpleDateFormat(
        "EEE • hh:mm a",
        java.util.Locale.US
    )
    return sdf.format(java.util.Date(time))
}
