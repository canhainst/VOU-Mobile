package com.example.vou_mobile.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.example.vou_mobile.R
import com.example.vou_mobile.databinding.ActivityQuizGameBinding
import com.example.vou_mobile.helper.Helper
import com.example.vou_mobile.utilities.TextToSpeechUtils
import com.example.vou_mobile.viewModel.EventViewModelProviderSingleton
import com.example.vou_mobile.viewModel.GameViewModel
import java.util.Calendar
import java.util.Date

class QuizGameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQuizGameBinding
    private val gameViewModel = GameViewModel()
    private val eventViewModel = EventViewModelProviderSingleton.getEventViewModel()
    private val typeOfEvent = "Quiz"
    private var time2: String? = null

    private lateinit var ttsUtil: TextToSpeechUtils

    private val handler = android.os.Handler()
    private val runnable = object : Runnable {
        override fun run() {
            updateUI()
            handler.postDelayed(this, 1000) // Cập nhật mỗi giây
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnClose.setOnClickListener {
            gameViewModel.setGame(typeOfEvent, this)
            gameViewModel.stopGame()
        }

        // Bắt đầu cập nhật giao diện người dùng
        handler.post(runnable)
        binding.tvTime.text = "The game will start at\n${eventViewModel.curEvent.value?.start_time}"
        val calendar = Calendar.getInstance()
        calendar.time = Helper.stringToDate(eventViewModel.curEvent.value?.start_time!!)!!
        calendar.add(Calendar.MINUTE, 10)
        time2 = Helper.dateToString(calendar.time)

        //text to speech
        ttsUtil = TextToSpeechUtils(this) {
            // Callback được gọi khi TTS đã sẵn sàng
            ttsUtil.speak(binding.tvWelcome.text.toString())
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateUI() {
        val curTime = Helper.dateToString(Date())
        eventViewModel.curEvent.value?.let { event ->
            if (Helper.isTimeBefore(curTime, event.start_time)) {
                binding.btnPlay.isClickable = false
                binding.btnPlay.backgroundTintList = ContextCompat.getColorStateList(this, R.color.light_grey)
                binding.btnPlay.text = "It's not time to play yet"
            } else if (Helper.isTimeAfter(curTime, time2)) {
                binding.btnPlay.isClickable = false
                binding.btnPlay.backgroundTintList = ContextCompat.getColorStateList(this, R.color.light_grey)
                binding.btnPlay.text = "Oh no! You must join within 10 minutes of start."
            } else{
                binding.btnPlay.isClickable = true
                binding.btnPlay.backgroundTintList = ContextCompat.getColorStateList(this, R.color.green_correct)
                binding.btnPlay.text = "Start"
                binding.btnPlay.setOnClickListener {
                    val intent = Intent(this, PlayQuizGameActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnable) // Dừng cập nhật khi Activity bị hủy
        ttsUtil.shutdown()
    }
}
