package com.qti.sse_notify

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.sse.SSE
import io.ktor.client.request.headers
import io.ktor.client.request.prepareGet
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.HttpHeaders
import io.ktor.utils.io.readUTF8Line
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SseClient(
    private val client: HttpClient = HttpClient(CIO) {
        install(SSE)
    }
) {

    fun connect(
        url: String,
        token: String
    ): Flow<String> = flow {

        client.prepareGet(url) {
            headers {
                append(HttpHeaders.Accept, "text/event-stream")
                append(HttpHeaders.Authorization, "Bearer $token")
            }
        }.execute { response ->

            val channel = response.bodyAsChannel()

            while (!channel.isClosedForRead) {
                val line = channel.readUTF8Line()
                if (line?.startsWith("data:") == true) {
                    emit(line.removePrefix("data:").trim())
                }
            }
        }
    }
}
