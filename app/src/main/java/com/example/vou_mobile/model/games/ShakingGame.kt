package com.example.vou_mobile.model.games

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.example.vou_mobile.activity.ShakingGameActivity

class ShakingGame(private val context: Context): Game {
    override fun startGame() {
        val intent = Intent(context, ShakingGameActivity::class.java)
        context.startActivity(intent)
    }

    override fun endGame() {
        if (context is AppCompatActivity) {
            context.finish()
        }
    }
}