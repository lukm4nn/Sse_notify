package com.qti.sse_notify

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform