package com.example.unmute

import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.ByteArrayOutputStream

const val modelServerUrl = "https://c917-193-205-210-76.ngrok-free.app/process_frame"

@Serializable
data class LLMResponse(
    val status: String,
    val result: String? = null
)

suspend fun sendImageToLLMServer(bitmap: Bitmap): String? {
    val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    val byteArrayOutputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream)
    val base64Image = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT)

    val response: LLMResponse = client.post(modelServerUrl) {
        contentType(ContentType.Application.Json)
        setBody(mapOf("frame" to base64Image))
    }.body()

    Log.e("API Call", "Response: $response")

    return if (response.status == "processed") response.result else null
}
