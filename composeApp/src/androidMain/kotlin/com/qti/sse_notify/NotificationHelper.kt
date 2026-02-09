package com.qti.sse_notify

import android.content.Context
import androidx.core.app.NotificationCompat

import android.app.*

object NotificationHelper {

    fun foreground(context: Context): Notification {
        return NotificationCompat.Builder(context, "sse")
            .setContentTitle("Listening notification")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .build()
    }

    fun show(context: Context, message: String) {
        val notif = NotificationCompat.Builder(context, "sse")
            .setContentTitle("New Event")
            .setContentText(message)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .build()

        context.getSystemService(NotificationManager::class.java)
            .notify(message.hashCode(), notif)
    }
}

