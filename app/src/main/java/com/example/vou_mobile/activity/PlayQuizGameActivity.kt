package com.example.vou_mobile.activity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.hardware.SensorManager
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.airbnb.lottie.LottieAnimationView
import com.example.vou_mobile.R
import com.example.vou_mobile.databinding.ActivityPlayQuizGameBinding
import com.example.vou_mobile.model.User
import com.example.vou_mobile.services.api.EventService
import com.example.vou_mobile.services.api.PlayLacXiResponse
import com.example.vou_mobile.services.api.QuestionCountResponse
import com.example.vou_mobile.services.api.QuizService
import com.example.vou_mobile.services.api.RetrofitClient
import com.example.vou_mobile.services.websocket.SocketManager
import com.example.vou_mobile.utilities.TextToSpeechUtils
import com.example.vou_mobile.viewModel.EventViewModel
import com.example.vou_mobile.viewModel.EventViewModelProviderSingleton
import com.example.vou_mobile.viewModel.GameViewModelProviderSingleton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlayQuizGameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayQuizGameBinding
    private val gameViewModel = GameViewModelProviderSingleton.getGameViewModel()
    private val eventViewModel = EventViewModelProviderSingleton.getEventViewModel()
    private lateinit var socketManager: SocketManager
    private lateinit var sharedPreferences: SharedPreferences
    private val gson = Gson()
    private lateinit var userId: String
    private lateinit var currentUser: User
    private var answer: TextView? = null
    private lateinit var ttsUtil: TextToSpeechUtils
    private var timer: CountDownTimer? = null
    private lateinit var eventId: String
    private var curQuestion = 0
    private var numQuestion = 0
    private var correct = 0
    private var wrong = 0


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayQuizGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize SocketManager
        socketManager = SocketManager.getInstance()

        // Get userId from Intent or SharedPreferences if necessary
        sharedPreferences = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        userId = sharedPreferences.getString("uuid", null)!!
        eventId = eventViewModel.curEvent.value!!.id
        val userJson = sharedPreferences.getString("currentUser", null)
        if (userJson != null) {
            val userType = object : TypeToken<User>() {}.type
            currentUser = gson.fromJson(userJson, userType)
        }

        if (userId.isEmpty()) {
            Toast.makeText(this, "User ID is missing", Toast.LENGTH_SHORT).show()
            finish() // Exit the activity if userId is missing
            return
        }

        binding.tvPlayerName.text = currentUser.full_name
        // Gọi API để lấy số lượng câu hỏi
        val quizService = RetrofitClient.instance.create(QuizService::class.java)
        quizService.getQuestionCountByEvent(eventId).enqueue(object : Callback<QuestionCountResponse> {
            override fun onResponse(call: Call<QuestionCountResponse>, response: Response<QuestionCountResponse>) {
                if (response.isSuccessful) {
                    val questionCountResponse = response.body()
                    val questionCount = questionCountResponse?.questionCount ?: 0
                    numQuestion = questionCount
                    binding.tvQuestionNum.text = "Question $curQuestion/$numQuestion"
                } else {
                    Log.e("MainActivity", "Request failed with status code ${response.code()}")
                }
            }

            override fun onFailure(call: Call<QuestionCountResponse>, t: Throwable) {
                Log.e("MainActivity", "Network error", t)
            }
        })


        binding.btnClose.setOnClickListener {
            gameViewModel.currentGame.value?.endGame(this)
        }

        // Receive question data from Intent and display
        displayQuestion(
            question = intent.getStringExtra("question") ?: "No question received",
            choice1 = intent.getStringExtra("choice_1") ?: "N/A",
            choice2 = intent.getStringExtra("choice_2") ?: "N/A",
            choice3 = intent.getStringExtra("choice_3") ?: "N/A",
            choice4 = intent.getStringExtra("choice_4") ?: "N/A",
            timer = intent.getIntExtra("timer", 0)
        )

        binding.tvQuestionNum.text = "Question ${++curQuestion}/$numQuestion"

        // Register to receive new questions from the server
        socketManager.onQuestionReceived { questionData ->
            runOnUiThread {
                binding.tvQuestionNum.text = "Question ${++curQuestion}/$numQuestion"
                displayQuestion(
                    question = questionData.ques,
                    choice1 = questionData.choice_1,
                    choice2 = questionData.choice_2,
                    choice3 = questionData.choice_3,
                    choice4 = questionData.choice_4,
                    timer = questionData.timer
                )
            }
        }

        // Register callback to receive answer data
        socketManager.setOnAnswerOfQuestionListener { answerData ->
            runOnUiThread {
                // Handle received data from server
                if (answerData.correctAnswer == answer?.text.toString()){
                    answer?.setBackgroundColor(Color.GREEN)
                    binding.tvCorrectNum.text = "Correct: ${++correct}"
                    val userScore = answerData.allScores.find { it.userId == userId }?.score ?: 0
                    binding.tvPoint.text = "Score: $userScore"
                } else{
                    binding.tvWrongNum.text = "Wrong: ${++wrong}"
                    when (answerData.correctAnswer) {
                        binding.tvAnswer1.text.toString() -> {
                            binding.tvAnswer1.setBackgroundColor(Color.GREEN)
                        }
                        binding.tvAnswer2.text.toString() -> {
                            binding.tvAnswer2.setBackgroundColor(Color.GREEN)
                        }
                        binding.tvAnswer3.text.toString() -> {
                            binding.tvAnswer3.setBackgroundColor(Color.GREEN)
                        }
                        binding.tvAnswer4.text.toString() -> {
                            binding.tvAnswer4.setBackgroundColor(Color.GREEN)
                        }
                    }
                    answer?.setBackgroundColor(Color.RED)
                }
            }
        }

        // Set click events for answer options
        setAnswerClickListeners()
    }

    // Start the countdown timer
    private fun startTimer(duration: Int) {
        timer?.cancel() // Cancel previous timer if any
        timer = object : CountDownTimer(duration * 1000L, 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                val secondsLeft = millisUntilFinished / 1000
                binding.tvTimer.text = "Time left: $secondsLeft seconds"
            }

            @SuppressLint("SetTextI18n")
            override fun onFinish() {
                binding.tvTimer.text = "Time's up!"
                if (curQuestion == numQuestion){
                    socketManager.onEventEnd { allPlayerScore, voucher ->
                        runOnUiThread {
                            Log.d("SocketManager", "$allPlayerScore - $voucher")
                            // Display the player scores and voucher received
                            val sortedScores = allPlayerScore.toList().sortedByDescending { (_, score) -> score }
                            val rank = sortedScores.indexOfFirst { (id, _) -> id == userId } + 1
                            showRewardDialog(binding.tvPoint.text.toString(), "$rank/${sortedScores.size}", voucher)
                            allPlayerScore.forEach { (userId, score) ->
                                Log.d("QuizGameActivity", "Player: $userId, Score: $score")
                            }
                            // Implement further actions, such as showing a dialog or navigating to another activity
                        }
                    }
                }
            }
        }.start()
    }

    // Display the question and answers, initialize TTS and timer
    private fun displayQuestion(question: String, choice1: String, choice2: String, choice3: String, choice4: String, timer: Int) {
        binding.tvQuestion.text = question
        binding.tvAnswer1.text = choice1
        binding.tvAnswer2.text = choice2
        binding.tvAnswer3.text = choice3
        binding.tvAnswer4.text = choice4
        binding.tvAnswer1.setBackgroundColor(Color.WHITE)
        binding.tvAnswer2.setBackgroundColor(Color.WHITE)
        binding.tvAnswer3.setBackgroundColor(Color.WHITE)
        binding.tvAnswer4.setBackgroundColor(Color.WHITE)
        startTimer(timer)


        // Initialize TTS and speak the question and answers
        ttsUtil = TextToSpeechUtils(this) {
            ttsUtil.speak("${binding.tvQuestion.text}. ${binding.tvAnswer1.text}, ${binding.tvAnswer2.text}, ${binding.tvAnswer3.text}, ${binding.tvAnswer4.text}")
        }
    }

    // Set click events for answer options
    private fun setAnswerClickListeners() {
        binding.tvAnswer1.setOnClickListener { submitAnswer(binding.tvAnswer1) }
        binding.tvAnswer2.setOnClickListener { submitAnswer(binding.tvAnswer2) }
        binding.tvAnswer3.setOnClickListener { submitAnswer(binding.tvAnswer3) }
        binding.tvAnswer4.setOnClickListener { submitAnswer(binding.tvAnswer4) }
    }

    // Submit user's answer via WebSocket
    private fun submitAnswer(tvAnswer: TextView) {
        tvAnswer.setBackgroundColor(Color.YELLOW)
        socketManager.submitAnswer(userId, tvAnswer.text.toString())
        answer = tvAnswer
    }

    @SuppressLint("SetTextI18n", "CutPasteId")
    private fun showRewardDialog(score: String, ranking: String, voucher: String) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_open_gift, null)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        // Xử lý logic khi thành công
        Picasso.get()
            .load("https://cdn-icons-png.flaticon.com/512/3837/3837136.png")
            .into(dialogView.findViewById<LottieAnimationView>(R.id.animation_gift))

         dialogView.findViewById<TextView>(R.id.textWithGift).text =
            "Your ${score} \n " +
            "Ranking: $ranking\n" +
            "You have received a ${voucher}"

        dialogView.findViewById<TextView>(R.id.textWithGift).textSize = 15f // thay đổi kích thước chữ
        dialogView.findViewById<TextView>(R.id.textWithGift).setTextColor(ContextCompat.getColor(this, R.color.black)) // thay đổi màu chữ
        dialogView.findViewById<TextView>(R.id.textWithGift).setLineSpacing(10f, 1.2f) // điều chỉnh khoảng cách giữa các dòng


        val btnClaim = dialogView.findViewById<Button>(R.id.btn_Claim)
        btnClaim.setOnClickListener {
            eventViewModel.loadAllEvents()
            Toast.makeText(this, "Event is over", Toast.LENGTH_SHORT).show()
            gameViewModel.currentGame.value?.endGame(this)
            dialog.dismiss()
        }
        dialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        ttsUtil.shutdown() // Cleanup TTS resources
        timer?.cancel() // Cancel timer when Activity is destroyed
    }
}
