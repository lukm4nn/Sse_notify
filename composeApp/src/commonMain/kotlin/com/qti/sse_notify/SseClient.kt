package com.qti.sse_notify

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.headers
import io.ktor.client.request.prepareGet
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.HttpHeaders
import io.ktor.utils.io.readUTF8Line
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.retryWhen

class SseClient(
    val client: HttpClient = HttpClient(CIO) {

        install(HttpTimeout) {
            requestTimeoutMillis = Long.MAX_VALUE
            connectTimeoutMillis = 30_000
            socketTimeoutMillis = Long.MAX_VALUE
        }
    }
) {
    fun connect(url: String, userId: String): Flow<String> =
        flow {
            val response = client.get(url) {
                header("X-USER-ID", userId)
            }

            val channel = response.bodyAsChannel()
            var eventBuffer = ""

            while (!channel.isClosedForRead) {
                val line = channel.readUTF8Line() ?: break

                println("RAW LINE: $line")

                if (line.isBlank()) {
                    if (eventBuffer.isNotBlank()) {
                        emit(eventBuffer.trim())
                        eventBuffer = ""
                    }
                } else if (line.startsWith("data:")) {
                    eventBuffer += line.removePrefix("data:").trim() + "\n"
                }
            }
        }
            .flowOn(Dispatchers.IO)
            .retryWhen { cause, _ ->
                println("SSE retry: ${cause.message}")
                kotlinx.coroutines.delay(3000)
                true
            }
            .catch { e ->
                println("SSE error handled: ${e.message}")
            }
}