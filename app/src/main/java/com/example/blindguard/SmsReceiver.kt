package com.example.blindguard

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SmsReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        if (intent.action != "android.provider.Telephony.SMS_RECEIVED") return

        val bundle = intent.extras ?: return
        val pdus = bundle.get("pdus") as Array<*>
        val format = bundle.getString("format")

        for (pdu in pdus) {

            val sms = android.telephony.SmsMessage.createFromPdu(pdu as ByteArray, format)
            val message = sms.messageBody

            // Expected SMS format:
            // BG_ALERT,FALL,20.2961,85.8245

            if (message.startsWith("BG_ALERT")) {

                val parts = message.split(",")

                if (parts.size >= 4) {

                    val type = parts[1]
                    val lat = parts[2]
                    val lon = parts[3]

                    val record = AlertRecord(type = type, latitude = lat, longitude = lon, timestamp = System.currentTimeMillis())
                    val repo = AlertRepository(context)

                    kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.IO).launch {
                        repo.addAlert(record)
                    }

                    showNotification(context, type, lat, lon)
                }
            }
        }
    }

    private fun showNotification(context: Context, type: String, lat: String, lon: String) {

        // 👉 Open AlertActivity (NOT MainActivity)
        val openAppIntent = Intent(context, AlertActivity::class.java).apply {
            putExtra("TYPE", type)
            putExtra("LAT", lat)
            putExtra("LON", lon)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            openAppIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val message =
            if (type == "SOS")
                "SOS triggered. Tap to view location."
            else
                "Fall detected. Tap to view location."

        val notification = NotificationCompat.Builder(context, "blindguard")
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentTitle("BlindGuard Alert")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val manager = NotificationManagerCompat.from(context)

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            if (context.checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS)
                == android.content.pm.PackageManager.PERMISSION_GRANTED
            ) {
                manager.notify(System.currentTimeMillis().toInt(), notification)
            }
        } else {
            manager.notify(System.currentTimeMillis().toInt(), notification)
        }
    }
}