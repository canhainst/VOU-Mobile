package com.example.vou_mobile.model.games

import android.content.Context

class QuizGame(private val context: Context) : Game {
    override fun startGame() {
        println("Starting Quiz Game...")
        // Logic để bắt đầu trò chơi quiz
    }

    override fun endGame() {
        println("Ending Quiz Game...")
        // Logic để kết thúc trò chơi quiz
    }
}