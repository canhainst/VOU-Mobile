package com.example.vou_mobile.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vou_mobile.model.Event
import com.example.vou_mobile.model.games.Game
import com.example.vou_mobile.model.games.GameFactory

class GameViewModel() : ViewModel() {
    private val _currentGame = MutableLiveData<Game>()
    val currentGame: LiveData<Game> get() = _currentGame

    fun setGame(gameType: String, context: Context) {
        val game = GameFactory.createGame(gameType, context)
        _currentGame.value = game
    }

    fun startGame() {
        _currentGame.value?.startGame()
    }

    fun stopGame() {
        _currentGame.value?.endGame()
    }
}