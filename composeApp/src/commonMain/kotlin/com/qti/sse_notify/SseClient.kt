package com.qti.sse_notify

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.utils.io.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

class SseClient(
    private val client: HttpClient = HttpClient(io.ktor.client.engine.cio.CIO) {
        install(HttpTimeout) {
            requestTimeoutMillis = Long.MAX_VALUE
            connectTimeoutMillis = 30_000
            socketTimeoutMillis = Long.MAX_VALUE
        }
    }
) {
    fun connectStable(url: String, userId: String): Flow<String> = channelFlow {
        try {
            client.prepareGet(url) {
                header("X-USER-ID", userId)
            }.execute { response ->
                val channel = response.bodyAsChannel()
                while (!channel.isClosedForRead) {
                    val line = channel.readUTF8Line() ?: break
                    if (line.startsWith("data:")) {
                        val data = line.substringAfter("data:").trim()
                        if (data.isNotBlank() && data != "ping") {
                            // channelFlow menggunakan send(), ini Thread-Safe dan aman untuk context yang berbeda
                            send(data)
                        }
                    }
                }
            }
        } catch (e: Exception) {
            // Biarkan retryWhen yang menangani
            throw e
        }
    }
        .retryWhen { cause, attempt ->
            println("SSE Connection lost ($cause), retrying attempt: $attempt")
            delay(3000)
            true
        }
        .flowOn(Dispatchers.IO)
}