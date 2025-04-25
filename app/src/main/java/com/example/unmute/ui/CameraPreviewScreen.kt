package com.example.unmute.ui

import android.Manifest
import android.content.pm.PackageManager
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.example.unmute.R
import com.example.unmute.sendImageToLLMServer
import com.example.unmute.speakText
import kotlinx.coroutines.launch
import java.util.Locale

@Composable
fun CameraPreviewScreen(navController: NavHostController) {
    val context = LocalContext.current
    val cameraPermission = Manifest.permission.CAMERA
    val coroutineScope = rememberCoroutineScope()

    val ttsRef = remember { mutableStateOf<TextToSpeech?>(null) }
    var subtitleText by remember { mutableStateOf("") }
    var isSpeaking by remember { mutableStateOf(false) }

    val cameraPermissionState =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
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
            ttsRef.value =
                TextToSpeech(context) { status ->
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

    val logoPainter = painterResource(id = R.drawable.hope_logo)

    Box(modifier = Modifier.fillMaxSize()) {
        // Camera preview in the back
        CameraPreview(
            modifier = Modifier.fillMaxSize(),
            onFrameCaptured = { bitmap ->
                if (!isSpeaking && ttsRef.value != null) {
                    coroutineScope.launch {
                        try {
                            val resultText = sendImageToLLMServer(bitmap)
                            if (resultText != null) {
                                subtitleText = resultText
                                isSpeaking = true
                                speakText(ttsRef.value!!, resultText) {
                                    isSpeaking = false
                                }
                            }
                        } catch (e: Exception) {
                            // subtitleText = "Error: ${e.message}"
                            Log.e("API Call", "API call failed", e)
                            isSpeaking = false
                        }
                    }
                }
            },
        )

        // UI overlays
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .statusBarsPadding(),
        ) {
            // Logo at the top
            Image(
                painter = logoPainter,
                contentDescription = "Hope Logo",
                modifier =
                    Modifier
                        .align(Alignment.TopCenter)
                        .size(56.dp)
                        .clip(CircleShape),
            )

            // Subtitle box above the red button
            if (subtitleText.isNotEmpty()) {
                Box(
                    modifier =
                        Modifier
                            .align(Alignment.BottomCenter)
                            .padding(start = 24.dp, end = 24.dp, bottom = 96.dp) // <--- Outer padding
                            .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 16.dp, vertical = 12.dp), // <--- Inner padding
                ) {
                    Text(
                        text = subtitleText,
                        color = Color.White,
                        fontSize = 20.sp,
                    )
                }
            }

            // Red "X" button, pushed up from the bottom
            Box(
                modifier =
                    Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 24.dp)
                        .size(64.dp)
                        .background(Color.Red, shape = RoundedCornerShape(50))
                        .clickable {
                            navController.popBackStack()
                        },
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "✕",
                    color = Color.White,
                    fontSize = 28.sp,
                )
            }
        }
    }
}
