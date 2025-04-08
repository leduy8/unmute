package com.example.unmute.ui

import android.Manifest
import android.content.pm.PackageManager
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.unmute.mockLLM
import com.example.unmute.speakText
import java.util.Locale

@Composable
fun CameraPreviewScreen() {
    val context = LocalContext.current
    val cameraPermission = Manifest.permission.CAMERA

    val ttsRef = remember { mutableStateOf<TextToSpeech?>(null) }
    var subtitleText by remember { mutableStateOf("") }
    var isSpeaking by remember { mutableStateOf(false) }

    val cameraPermissionState = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            Toast.makeText(context, "Camera permission denied", Toast.LENGTH_LONG).show()
        }
    }

    // Initialize TTS once
    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(context, cameraPermission)
            != PackageManager.PERMISSION_GRANTED
        ) {
            cameraPermissionState.launch(cameraPermission)
        }

        if (ttsRef.value == null) {
            ttsRef.value = TextToSpeech(context) { status ->
                if (status == TextToSpeech.SUCCESS) {
                    val result = ttsRef.value?.setLanguage(Locale.US)
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Toast.makeText(context, "US English not supported", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "TTS initialization failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        CameraPreview(
            modifier = Modifier.fillMaxSize(),
            onFrameCaptured = { bitmap ->
                if (!isSpeaking && ttsRef.value != null) {

                    val mockedSentence = mockLLM(bitmap)
                    subtitleText = mockedSentence
                    isSpeaking = true
                    speakText(ttsRef.value!!, mockedSentence) {
                        isSpeaking = false
                    }
                }
            }
        )

        // Subtitle overlay
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
                .padding(8.dp)
        ) {
            Text(
                text = subtitleText,
                color = Color.White,
                fontSize = 20.sp
            )
        }
    }
}
