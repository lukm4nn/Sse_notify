package com.qti.sse_notify

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.logging.Logger

object PushApi {

    private val client = HttpClient(CIO){
        install(ContentNegotiation) {
            json()
        }
    }

    suspend fun sendFcmToken(token: String) {
        try {
            client.post("http://10.0.2.2:8080/device/fcm") {
                parameter("userId", "123")
                parameter("token", token)
            }
        } catch (e: Exception) {
            println("Failed to send FCM token ${e.message}")
        }
    }
}
