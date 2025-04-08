package com.example.unmute

import android.graphics.Bitmap
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log

fun speakText(tts: TextToSpeech, text: String, onDone: () -> Unit) {
    tts.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
        override fun onStart(utteranceId: String?) {}
        override fun onError(utteranceId: String?) {
            onDone()
        }

        override fun onDone(utteranceId: String?) {
            onDone()
        }
    })

    val params = Bundle()
    params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "utteranceId")
    tts.speak(text, TextToSpeech.QUEUE_FLUSH, params, "utteranceId")
}


fun mockLLM(bitmap: Bitmap): String {
    // In real case, send frame to LLM
    Log.e("CameraFrame", "Current bitmap is $bitmap")
    Log.d("CameraFrame", "Captured bitmap: ${bitmap.width}x${bitmap.height}")
    return listOf(
        "Hi there!",
        "I'm listening to you.",
        "How are you?",
        "Let us have a conversation!",
        "I put this very very long sentence just to test that the app will speak the full sentence and won't be overlapping with the next sentence lmao"
    ).random()
}
