package com.example.vou_mobile.utilities

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.Locale

class TextToSpeechUtils(private val context: Context, private val onReady: () -> Unit) {
    private var tts: TextToSpeech? = null

    init {
        initializeTTS()
    }

    private fun initializeTTS() {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = tts?.setLanguage(Locale("vi"))
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TTS", "Language is not supported")
                } else {
                    Log.d("TTS", "Text-to-Speech is ready")
                    onReady() // Gọi callback khi TTS sẵn sàng
                }
            } else {
                Log.e("TTS", "Text-to-Speech initialization failed")
            }
        }
    }

    fun speak(text: String) {
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
    }
}
