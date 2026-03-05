package com.qti.sse_notify

import android.R.attr.data
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        requestIgnoreBatteryOptimizations()
        lifecycleScope.launch {
            FirebaseMessaging.getInstance().token
                .addOnSuccessListener { token ->
                    CoroutineScope(Dispatchers.IO).launch {
                        Log.d("TokenFCM", token)
                        PushApi.sendFcmToken(token)
                    }
                }
        }


        setContent {
            App()
        }
    }

    fun requestIgnoreBatteryOptimizations() {
        val pm = getSystemService(Context.POWER_SERVICE) as PowerManager
        val packageName = packageName

        // Cek apakah sudah diizinkan sebelumnya
        if (!pm.isIgnoringBatteryOptimizations(packageName)) {
            val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
                data = Uri.parse("package:$packageName")
            }
            try {
                startActivity(intent)
            } catch (e: Exception) {
                // Beberapa perangkat (seperti Xiaomi/Oppo) terkadang memblokir intent ini
                println("Gagal membuka setting baterai: ${e.message}")
            }
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}