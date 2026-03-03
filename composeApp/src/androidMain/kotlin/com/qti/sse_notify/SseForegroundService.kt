package com.qti.sse_notify.sse

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.qti.sse_notify.NotificationHelper
import com.qti.sse_notify.NotificationRepository
import com.qti.sse_notify.SseClient
import kotlinx.coroutines.*

class SseForegroundService : Service() {

    private val scope = CoroutineScope(
        SupervisorJob() + Dispatchers.IO
    )

    @SuppressLint("MissingPermission", "NewApi")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        startForeground(
            1,
            NotificationHelper.foreground(this)
        )

        val repo = NotificationRepository(SseClient())

        scope.launch {
            withTimeoutOrNull(15 * 60 * 1000L) { // 🔥 auto stop 15 menit
                repo.listen("123").collect { event ->
                    NotificationHelper.show(this@SseForegroundService, event)
                }
            }
            stopSelf()
        }

        return START_NOT_STICKY
    }

    override fun onDestroy() {
        scope.cancel()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
