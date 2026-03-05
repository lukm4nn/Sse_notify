package com.qti.sse_notify

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun App() {
    // Inisialisasi client & repo (sebaiknya pakai DI seperti Koin, tapi ini untuk simpelnya)
    val sseClient = remember { SseClient() }
    val repo = remember { NotificationRepository(sseClient) }
    val userId = "123" // Hardcoded untuk testing

    // State untuk memantau status koneksi atau pesan terakhir di UI
    var lastMessage by remember { mutableStateOf("Menunggu notifikasi...") }

    LaunchedEffect(userId) {
        repo.listen(userId).collect { message ->
            lastMessage = message
            println("Notifikasi Masuk: $message")

            // Panggil fungsi native untuk show notification
            PlatformNotification.show(message)
        }
    }

    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .safeContentPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "SSE Status: Connected",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "Pesan Terakhir:",
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = lastMessage,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}