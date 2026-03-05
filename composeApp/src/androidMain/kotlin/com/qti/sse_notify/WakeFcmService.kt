package com.qti.sse_notify


import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WakeFcmService : FirebaseMessagingService() {

    private val scope = CoroutineScope(Dispatchers.IO)

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM Token", token)
        scope.launch {
            PushApi.sendFcmToken(token)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMessageReceived(message: RemoteMessage) {
        val type = message.data["type"]
        val title = message.data["title"] ?: "Notifikasi Baru"
        val content = message.data["content"] ?: ""

        if (type == "WAKE_SSE") {
            // 1. Munculkan notifikasi tray secara manual agar user tahu ada info baru
            NotificationHelper.show(applicationContext, content)

            // 2. Jalankan Foreground Service untuk koneksi SSE
            val intent = Intent(applicationContext, SseForegroundService::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                applicationContext.startForegroundService(intent)
            } else {
                applicationContext.startService(intent)
            }
        }
    }
}

