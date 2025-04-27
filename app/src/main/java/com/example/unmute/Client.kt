package com.example.unmute

import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import androidx.core.graphics.scale
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.ByteArrayOutputStream

// Call local for server in same host machine
// If calling to other machine, check for its IP address
const val SERVER_URL = "http://10.0.2.2:5000/process_frame"

@Serializable
data class LLMResponse(
    val status: String,
    val result: String? = null,
)

suspend fun sendImageToLLMServer(base64Image: String): String? {
    val client =
        HttpClient(Android) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }

    val response: LLMResponse =
        client.post(SERVER_URL) {
            contentType(ContentType.Application.Json)
            setBody(mapOf("frame" to base64Image))
        }.body()

    Log.e("API Call", "Response: $response")

    return if (response.status == "processed") response.result else null
}

suspend fun convertBitmapToBase64(bitmap: Bitmap): String {
    return withContext(Dispatchers.Default) {
        val resizedBitmap = bitmap.scale(640, 480, false)
        val byteArrayOutputStream = ByteArrayOutputStream()
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream)
        Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT)
    }
}
