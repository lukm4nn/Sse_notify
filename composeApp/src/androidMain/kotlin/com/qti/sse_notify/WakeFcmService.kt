package com.qti.sse_notify


import android.content.Intent
import android.os.Build
import android.util.Log
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

    override fun onMessageReceived(message: RemoteMessage) {
        println("FCM Received: ${message.data}")

        val type = message.data["type"] ?: return
        if (type == "WAKE_SSE") {
            if (AppState.isForeground) {
                println("App is in foreground, skipping service start.")
                return
            }

            val intent = Intent(this, SseForegroundService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent)
            } else {
                startService(intent)
            }
        }
    }
}

