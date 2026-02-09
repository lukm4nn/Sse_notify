package com.qti.sse_notify

import kotlinx.coroutines.flow.Flow

class NotificationRepository(
    private val sseClient: SseClient
) {

    fun listen(token: String): Flow<String> =
        sseClient.connect(
            url = "https://api.example.com/sse",
            token = token
        )
}
