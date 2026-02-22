package com.example.blindguard
import androidx.compose.ui.platform.LocalContext
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class AlertActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val type = intent.getStringExtra("TYPE") ?: "ALERT"
        val lat = intent.getStringExtra("LAT") ?: "0.0"
        val lon = intent.getStringExtra("LON") ?: "0.0"

        setContent {
            EmergencyScreen(type, lat, lon)
        }
    }
}

@Composable
fun EmergencyScreen(type: String, lat: String, lon: String) {

    val context = LocalContext.current
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF121212)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "🚨 $type DETECTED",
                fontSize = 28.sp,
                color = Color.Red
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Location:",
                color = Color.White,
                fontSize = 18.sp
            )

            Text(
                text = "$lat , $lon",
                color = Color.LightGray
            )

            Spacer(modifier = Modifier.height(30.dp))

            Button(onClick = {
                val uri = Uri.parse("geo:$lat,$lon?q=$lat,$lon(Emergency)")
                val intent = Intent(Intent.ACTION_VIEW, uri)
                context.startActivity(intent)
            }) {
                Text("Open in Google Maps")
            }
        }
    }
}