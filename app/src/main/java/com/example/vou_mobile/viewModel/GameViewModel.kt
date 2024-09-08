package com.example.vou_mobile.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vou_mobile.model.games.Game
import com.example.vou_mobile.model.games.GameFactory
import com.example.vou_mobile.services.EventService
import com.example.vou_mobile.services.PlaythroughResponse
import com.example.vou_mobile.services.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object GameViewModelProviderSingleton {
    private var gameViewModel: GameViewModel? = null

    fun getGameViewModel(): GameViewModel {
        if (gameViewModel == null) {
            gameViewModel = GameViewModel()
        }
        return gameViewModel!!
    }
}

class GameViewModel() : ViewModel() {
    private val _currentGame = MutableLiveData<Game>()
    val currentGame: LiveData<Game> get() = _currentGame

    private val _playthrough = MutableLiveData<Int>()
    val playthrough: LiveData<Int> get() = _playthrough

    fun setGame(gameType: String, eventId: String) {
        val game = GameFactory.createGame(gameType, eventId)
        _currentGame.value = game
    }

    fun loadPlaythrough(eventId: String, userId: String){
        val api = RetrofitClient.instance.create(EventService::class.java)
        api.getOrCreatePlaythrough(userId, eventId).enqueue(object :
            Callback<PlaythroughResponse> {
            override fun onResponse(call: Call<PlaythroughResponse>, response: Response<PlaythroughResponse>) {
                if (response.isSuccessful) {
                    val playthroughResponse = response.body()
                    if (playthroughResponse != null) {
                        // Xử lý dữ liệu ở đây
                        _playthrough.value = playthroughResponse.playthrough
                    }
                } else {
                    Log.e("API Error", "Response code: ${response.code()}, message: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<PlaythroughResponse>, t: Throwable) {
                Log.e("API Failure", "Error occurred: ${t.message}", t)
            }
        })
    }
}