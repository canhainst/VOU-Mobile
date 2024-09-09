package com.example.vou_mobile.model.games

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.example.vou_mobile.activity.QuizGameActivity

class QuizGame() : Game {
    override fun startGame(context: Context) {
        val intent = Intent(context, QuizGameActivity::class.java)
        context.startActivity(intent)
    }

    override fun endGame(context: Context) {
        if (context is AppCompatActivity) {
            context.finish()
        }
    }
}