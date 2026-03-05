package com.qti.sse_notify

import kotlinx.coroutines.flow.Flow

class NotificationRepository(
    private val sseClient: SseClient
) {

    fun listen(userId: String): Flow<String> =
        sseClient.connectStable(
            url = "http://10.0.2.2:8080/sse/notifications",
            userId = userId
        )
}