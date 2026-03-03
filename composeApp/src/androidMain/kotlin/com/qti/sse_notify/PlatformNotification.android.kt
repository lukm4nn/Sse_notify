package com.qti.sse_notify

import android.annotation.SuppressLint
import android.content.Context

actual object PlatformNotification {

    private lateinit var appContext: Context

    fun init(context: Context) {
        appContext = context.applicationContext
    }

    @SuppressLint("NewApi")
    actual fun show(message: String) {
        NotificationHelper.show(appContext, message)
    }
}