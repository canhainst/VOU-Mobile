package com.example.vou_mobile.model.games

import android.content.Context

object GameFactory {
    fun createGame(gameType: String): Game {
        return when (gameType.lowercase()) {
            "lắc xì" -> ShakingGame()
            "quiz" -> QuizGame()
            else -> throw IllegalArgumentException("Unknown game type")
        }
    }
}