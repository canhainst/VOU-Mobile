package com.example.vou_mobile.model.games

import android.content.Context
import android.content.Intent
import com.example.vou_mobile.activity.ShakingGameActivity

class ShakingGame(private val context: Context): Game {
    override fun startGame() {
        val intent = Intent(context, ShakingGameActivity::class.java)
        context.startActivity(intent)
    }

    override fun endGame() {
    }
}