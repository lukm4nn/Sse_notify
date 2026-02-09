package com.qti.sse_notify

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.http.*

object PushApi {

    private val client = HttpClient(CIO)

    suspend fun sendFcmToken(token: String) {
        try {
            client.post("https://api.example.com/device/fcm-token") {
                contentType(ContentType.Application.Json)
                setBody(
                    mapOf(
                        "token" to token,
                        "platform" to "ANDROID"
                    )
                )
            }
        } catch (e: Exception) {
            // log aja, jangan crash
        }
    }
}
