package com.example.vou_mobile.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.vou_mobile.R
import com.example.vou_mobile.databinding.ActivityQuizGameBinding
import com.example.vou_mobile.helper.Helper
import com.example.vou_mobile.services.websocket.SocketManager
import com.example.vou_mobile.utilities.TextToSpeechUtils
import com.example.vou_mobile.viewModel.EventViewModelProviderSingleton
import com.example.vou_mobile.viewModel.GameViewModelProviderSingleton
import java.util.Calendar
import java.util.Date

class QuizGameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQuizGameBinding
    private val eventViewModel = EventViewModelProviderSingleton.getEventViewModel()
    private val gameViewModel = GameViewModelProviderSingleton.getGameViewModel()
    private var time2: String? = null

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var userId: String
    private lateinit var eventId: String

    private lateinit var socketManager: SocketManager
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

        sharedPreferences = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        userId = sharedPreferences.getString("uuid", null)!!
        eventId = intent.getStringExtra("idEvent") ?: ""

        binding.btnClose.setOnClickListener {
            gameViewModel.currentGame.value?.endGame(this)
        }

        // Bắt đầu cập nhật giao diện người dùng
        handler.post(runnable)
        binding.tvTime.text = "The game will start at\n${eventViewModel.curEvent.value?.start_time}"
        val calendar = Calendar.getInstance()
        calendar.time = Helper.stringToDate(eventViewModel.curEvent.value?.start_time!!)!!
        calendar.add(Calendar.MINUTE, 10)
        time2 = Helper.dateToString(calendar.time)

        // Khởi tạo SocketManager nhưng chưa kết nối
        socketManager = SocketManager.getInstance()

        // Đăng ký callback cho sự kiện eventStart từ server
        socketManager.onEventStart { countDown ->
            runOnUiThread {
                binding.tvTime.text = "Game starts in: $countDown seconds"
            }
        }

        // Đăng ký callback cho sự kiện nhận câu hỏi
        socketManager.onQuestionReceived { questionData ->
            runOnUiThread {
                val intent = Intent(this, PlayQuizGameActivity::class.java)
                questionData.let {
                    intent.putExtra("question", it.ques)
                    intent.putExtra("choice_1", it.choice_1)
                    intent.putExtra("choice_2", it.choice_2)
                    intent.putExtra("choice_3", it.choice_3)
                    intent.putExtra("choice_4", it.choice_4)
                    intent.putExtra("timer", it.timer)
                }
                finish()
                startActivity(intent)
            }
        }

        // Xử lý lỗi kết nối nếu có
        socketManager.onError { error ->
            runOnUiThread {
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_LONG).show()
            }
        }

        // Text-to-speech setup
        ttsUtil = TextToSpeechUtils(this) {
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
                binding.btnPlay.text = "You must join within 10 minutes of start."
            } else {
                binding.btnPlay.isClickable = true
                binding.btnPlay.backgroundTintList = ContextCompat.getColorStateList(this, R.color.green_correct)
                binding.btnPlay.text = "Start"
                binding.btnPlay.setOnClickListener {
                    // Kết nối WebSocket khi nhấn nút Start
                    socketManager.connect(eventId, userId)
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
