package com.example.vou_mobile.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import com.example.vou_mobile.R
import com.example.vou_mobile.databinding.ActivityPlayQuizGameBinding
import com.example.vou_mobile.helper.Helper
import com.example.vou_mobile.viewModel.EventViewModel
import com.example.vou_mobile.viewModel.GameViewModel
import java.util.Date

class PlayQuizGameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayQuizGameBinding
    private val gameViewModel = GameViewModel()
    private val eventViewModel = EventViewModel()
    private val typeOfEvent = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayQuizGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnClose.setOnClickListener {
            gameViewModel.setGame(typeOfEvent, this)
            gameViewModel.stopGame()
        }
    }
}