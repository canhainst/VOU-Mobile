package com.example.vou_mobile.services.websocket

import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import io.socket.engineio.client.transports.WebSocket
import org.json.JSONObject
import java.net.URISyntaxException

data class QuestionData(
    val ques: String,
    val choice_1: String,
    val choice_2: String,
    val choice_3: String,
    val choice_4: String,
    val timer: Int
)

data class PlayerScore(val userId: String, val score: Int)
data class AnswerData(val correctAnswer: String, val allScores: List<PlayerScore>)

class SocketManager private constructor() {
    private var socket: Socket? = null
    private var onEventStartListener: ((Int) -> Unit)? = null
    private var onErrorListener: ((Exception) -> Unit)? = null
    private var onQuestionReceivedListener: ((QuestionData) -> Unit)? = null
    private var onAnswerOfQuestionListener: ((AnswerData) -> Unit)? = null
    private var onEventEndListener: ((Map<String, Int>, String) -> Unit)? = null

    companion object {
        @Volatile
        private var instance: SocketManager? = null

        fun getInstance(): SocketManager {
            return instance ?: synchronized(this) {
                instance ?: SocketManager().also { instance = it }
            }
        }
    }

    fun connect(eventId: String, userId: String) {
        try {

//            socket = IO.socket("http://10.100.77.64:5000")

//            val opts = IO.Options()
//            opts.transports = arrayOf<String>(WebSocket.NAME)

            val opts = IO.Options().apply {
                transports = arrayOf("websocket", "polling") // Chỉ sử dụng WebSocket
                reconnection = true // Tự động kết nối lại nếu kết nối bị mất
                reconnectionAttempts = 5 // Số lần thử kết nối lại
                reconnectionDelay = 1000 // Thời gian chờ giữa các lần thử kết nối lại (ms)
            }


            socket = IO.socket("http://10.100.77.64:80", opts)
            socket?.connect()

            socket?.on(Socket.EVENT_CONNECT, onConnect)
            socket?.on(Socket.EVENT_DISCONNECT, onDisconnect)
            socket?.on(Socket.EVENT_CONNECT_ERROR, onError)

            socket?.emit("playEvent", eventId, userId)

            socket?.on("eventStart", onEventStart)
            socket?.on("question", onQuestionReceived)
            socket?.on("answerOfQuestion", onAnswerOfQuestion)

            socket?.on("eventEnd", onEventEnd) // Registering the "eventEnd" listener

        } catch (e: URISyntaxException) {
            Log.e("SocketManager", "Socket connection error: ${e.message}")
            onErrorListener?.invoke(e)
        }
    }

    fun disconnect() {
        socket?.disconnect()
    }

    fun onEventStart(callback: (Int) -> Unit) {
        onEventStartListener = callback
    }

    fun onEventEnd(callback: (Map<String, Int>, String) -> Unit) {
        onEventEndListener = callback
    }

    fun onQuestionReceived(callback: (QuestionData) -> Unit) {
        onQuestionReceivedListener = callback
    }

    fun onError(callback: (Exception) -> Unit) {
        onErrorListener = callback
    }

    fun emit(event: String, vararg args: Any) {
        socket?.emit(event, *args)
    }

    fun submitAnswer(userId: String, answer: String) {
        socket?.emit("submitAnswer", userId, answer)
        Log.d("SocketManager", "Submitted answer: userId = $userId, answer = $answer")
    }

    fun setOnAnswerOfQuestionListener(listener: (AnswerData) -> Unit) {
        onAnswerOfQuestionListener = listener
    }

    private val onConnect = Emitter.Listener {
        Log.d("SocketManager", "Connected to server")
    }

    private val onDisconnect = Emitter.Listener {
        Log.d("SocketManager", "Disconnected from server")
    }

    private val onError = Emitter.Listener { args ->
        val error = args[0] as Exception
        Log.e("SocketManager", "Connection error: ${error}")
        onErrorListener?.invoke(error)
    }

    private val onEventStart = Emitter.Listener { args ->
        val countDown = args[0] as Int
        Log.d("SocketManager", "Countdown: $countDown seconds")
        onEventStartListener?.invoke(countDown)
    }

    private val onEventEnd = Emitter.Listener { args ->
        val data = args[0] as JSONObject
        val playerScoreJson = data.getJSONObject("allPlayerScore")
        val voucher = data.getString("voucher")

        // Convert playerScoreJson to a Map
        val playerScores = mutableMapOf<String, Int>()
        playerScoreJson.keys().forEach { key ->
            playerScores[key.toString()] = playerScoreJson.getInt(key.toString())
        }

        Log.d("SocketManager", "Event ended. Player scores: $playerScores, Voucher: $voucher")
        onEventEndListener?.invoke(playerScores, voucher)
    }


    private val onQuestionReceived = Emitter.Listener { args ->
        val data = args[0] as JSONObject
        val questionData = QuestionData(
            ques = data.getString("ques"),
            choice_1 = data.getString("choice_1"),
            choice_2 = data.getString("choice_2"),
            choice_3 = data.getString("choice_3"),
            choice_4 = data.getString("choice_4"),
            timer = data.getInt("timer")
        )
        Log.d("SocketManager", "Question received: ${questionData.ques}")
        onQuestionReceivedListener?.invoke(questionData)
    }

    // Listener cho sự kiện "answerOfQuestion"
    private val onAnswerOfQuestion = Emitter.Listener { args ->
        val data = args[0] as JSONObject
        val correctAnswer = data.optString("correctAnswer", "")

        // Phân tích dữ liệu allScores từ JSONObject
        val allScores = mutableListOf<PlayerScore>()
        val scoresJson = data.optJSONObject("AllScores")
        scoresJson?.keys()?.forEach { key ->
            val score = scoresJson.optInt(key.toString(), 0)
            allScores.add(PlayerScore(userId = key.toString(), score = score))
        }

        val answerData = AnswerData(
            correctAnswer = correctAnswer,
            allScores = allScores
        )
        Log.d("SocketManager", "Answer data received: ${answerData.correctAnswer}")
        onAnswerOfQuestionListener?.invoke(answerData)
    }
}
