package com.example.vou_mobile.model.games

import android.content.Context

object GameFactory {
    fun createGame(gameType: String, context: Context): Game {
        return when (gameType) {
            "Lắc xì" -> ShakingGame(context)
            "Quiz" -> QuizGame(context)
            else -> throw IllegalArgumentException("Unknown game type")
        }
    }
}