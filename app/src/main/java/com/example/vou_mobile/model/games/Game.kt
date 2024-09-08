package com.example.vou_mobile.model.games

import android.content.Context

interface Game {
    fun startGame(context: Context)
    fun endGame(context: Context)
}