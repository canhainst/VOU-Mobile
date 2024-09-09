package com.example.vou_mobile.utilities

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import java.util.Locale

class TextToSpeechUtils(private val context: Context, private val onReady: () -> Unit) {
    private var tts: TextToSpeech? = null
    private var onSpeakDone: (() -> Unit)? = null

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
                    tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                        override fun onStart(utteranceId: String?) {
                            // Khi TTS bắt đầu đọc
                        }

                        override fun onDone(utteranceId: String?) {
                            // Khi TTS đọc xong
                            onSpeakDone?.invoke() // Gọi callback khi hoàn tất đọc
                        }

                        override fun onError(utteranceId: String?) {
                            // Xử lý khi có lỗi xảy ra trong quá trình đọc
                            Log.e("TTS", "Error during speech")
                        }
                    })
                    Log.d("TTS", "Text-to-Speech is ready")
                    onReady() // Gọi callback khi TTS sẵn sàng
                }
            } else {
                Log.e("TTS", "Text-to-Speech initialization failed")
            }
        }
    }

    fun speak(text: String, onDone: () -> Unit = {}) {
        onSpeakDone = onDone
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "UTTERANCE_ID") // ID định danh để theo dõi
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
    }
}
