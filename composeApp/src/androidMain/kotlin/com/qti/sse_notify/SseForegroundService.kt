package com.qti.sse_notify

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.PowerManager
import kotlinx.coroutines.*

class SseForegroundService : Service() {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var wakeLock: PowerManager.WakeLock? = null

    @SuppressLint("MissingPermission", "NewApi")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val powerManager = getSystemService(POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "SseApp::WakelockTag")
        wakeLock?.acquire(15 * 60 * 1000L)

        startForeground(1, NotificationHelper.foreground(this))

        val repo = NotificationRepository(SseClient())

        scope.launch {
            try {
                withTimeoutOrNull(15 * 60 * 1000L) {
                    repo.listen("123").collect { event ->
                        NotificationHelper.show(this@SseForegroundService, event)
                    }
                }
            } finally {
                stopForeground(STOP_FOREGROUND_REMOVE)
                stopSelf()
            }
        }

        return START_STICKY
    }

    override fun onDestroy() {
        scope.cancel()
        if (wakeLock?.isHeld == true) wakeLock?.release()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}