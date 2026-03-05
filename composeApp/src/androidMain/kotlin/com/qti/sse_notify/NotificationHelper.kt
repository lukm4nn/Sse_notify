package com.qti.sse_notify

import android.content.Context
import androidx.core.app.NotificationCompat

import android.app.*
import android.os.Build
import androidx.annotation.RequiresApi

object NotificationHelper {

    private const val CHANNEL_ID = "sse_channel"
    private const val CHANNEL_NAME = "SSE Notification"

    @RequiresApi(Build.VERSION_CODES.O)
    fun foreground(context: Context): Notification {
        createChannel(context)

        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("Service Aktif")
            .setContentText("Sedang memantau notifikasi baru...")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            // Penting agar service tidak dianggap "silent" oleh sistem
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .build()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun show(context: Context, message: String) {
        createChannel(context)

        val notif = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("Pesan Baru 🚀")
            .setContentText(message)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            // Memastikan notifikasi muncul di atas (heads-up)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(Notification.DEFAULT_ALL)
            .setAutoCancel(true)
            .build()

        val manager = context.getSystemService(NotificationManager::class.java)
        manager.notify(message.hashCode(), notif)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel(context: Context) {
        val manager = context.getSystemService(NotificationManager::class.java)
        if (manager.getNotificationChannel(CHANNEL_ID) == null) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                // Ubah ke HIGH agar diizinkan running saat app terminated
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Saluran untuk notifikasi SSE real-time"
                enableLights(true)
            }
            manager.createNotificationChannel(channel)
        }
    }
}

