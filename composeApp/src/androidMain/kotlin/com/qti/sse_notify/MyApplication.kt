package com.qti.sse_notify

import android.app.Application
import android.content.Intent
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.qti.sse_notify.sse.SseForegroundService

class MyApplication : Application(), LifecycleObserver {

    override fun onCreate() {
        super.onCreate()
        PlatformNotification.init(this)
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onForeground() {
        stopService(Intent(this, SseForegroundService::class.java))
        AppState.isForeground = true
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onBackground() {
        AppState.isForeground = false
    }
}

object AppState {
    var isForeground = false
}
