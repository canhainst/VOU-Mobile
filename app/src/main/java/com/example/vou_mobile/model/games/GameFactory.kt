package com.example.vou_mobile.model.games

import android.content.Context

object GameFactory {
    fun createGame(gameType: Int, context: Context): Game {
        return when (gameType) {
            0 -> ShakingGame(context)
            1 -> QuizGame(context)
            else -> throw IllegalArgumentException("Unknown game type")
        }
    }
}