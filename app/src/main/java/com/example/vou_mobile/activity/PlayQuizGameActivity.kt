package com.example.vou_mobile.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.vou_mobile.databinding.ActivityPlayQuizGameBinding
import com.example.vou_mobile.utilities.TextToSpeechUtils
import com.example.vou_mobile.viewModel.EventViewModel
import com.example.vou_mobile.viewModel.GameViewModel

class PlayQuizGameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayQuizGameBinding
    private val gameViewModel = GameViewModel()
    private val eventViewModel = EventViewModel()
    private val typeOfEvent = "Quiz"

    private lateinit var ttsUtil: TextToSpeechUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayQuizGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        binding.btnClose.setOnClickListener {
//            gameViewModel.setGame(typeOfEvent, this)
//            gameViewModel.stopGame()
//        }

        ttsUtil = TextToSpeechUtils(this) {
            // Callback được gọi khi TTS đã sẵn sàng
            ttsUtil.speak("${binding.tvQuestion.text}. ${binding.tvAnswer1.text}, ${binding.tvAnswer2.text}, ${binding.tvAnswer3.text}, ${binding.tvAnswer4.text}")
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        ttsUtil.shutdown()
    }

}