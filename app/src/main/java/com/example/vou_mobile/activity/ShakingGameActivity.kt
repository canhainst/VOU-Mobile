package com.example.vou_mobile.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.vou_mobile.R
import com.example.vou_mobile.databinding.ActivityHomePageBinding
import com.example.vou_mobile.databinding.ActivityShakingGameBinding
import com.example.vou_mobile.viewModel.GameViewModel

class ShakingGameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShakingGameBinding
    private  var viewMode = GameViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShakingGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnClose.setOnClickListener {
            finish()
        }

    }
}