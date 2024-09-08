package com.example.vou_mobile.model.games

import android.content.Context

object GameFactory {
    fun createGame(gameType: String, eventId: String): Game {
        return when (gameType.lowercase()) {
            "lắc xì" -> ShakingGame(eventId)
            "quiz" -> QuizGame(eventId)
            else -> throw IllegalArgumentException("Unknown game type")
        }
    }
}