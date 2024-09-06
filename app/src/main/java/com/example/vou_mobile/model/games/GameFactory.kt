package com.example.vou_mobile.model.games

import android.content.Context

object GameFactory {
    fun createGame(gameType: String, context: Context): Game {
        return when (gameType.lowercase()) {
            "lắc xì" -> ShakingGame(context)
            "quiz" -> QuizGame(context)
            else -> throw IllegalArgumentException("Unknown game type")
        }
    }
}