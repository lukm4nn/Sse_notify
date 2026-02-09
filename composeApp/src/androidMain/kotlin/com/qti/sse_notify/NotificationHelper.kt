package com.qti.sse_notify

import android.content.Context
import androidx.core.app.NotificationCompat

import android.app.*
import android.os.Build
import androidx.annotation.RequiresApi

object NotificationHelper {

    private const val CHANNEL_ID = "sse_channel"

    @RequiresApi(Build.VERSION_CODES.O)
    fun foreground(context: Context): Notification {
        createChannel(context)

        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("Listening notification")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .build()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun show(context: Context, message: String) {
        createChannel(context)

        val notif = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("New Notification")
            .setContentText(message)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .build()

        context.getSystemService(NotificationManager::class.java)
            .notify(message.hashCode(), notif)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel(context: Context) {
        val manager = context.getSystemService(NotificationManager::class.java)
        if (manager.getNotificationChannel(CHANNEL_ID) == null) {
            manager.createNotificationChannel(
                NotificationChannel(
                    CHANNEL_ID,
                    "SSE Notification",
                    NotificationManager.IMPORTANCE_DEFAULT
                )
            )
        }
    }
}


