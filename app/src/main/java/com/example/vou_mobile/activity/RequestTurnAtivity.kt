package com.example.vou_mobile.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.example.vou_mobile.R
import com.example.vou_mobile.databinding.ActivityRequestTurnAtivityBinding
import com.example.vou_mobile.databinding.DialogGetTurnSuccessBinding

class RequestTurnAtivity : AppCompatActivity() {
    private lateinit var binding: ActivityRequestTurnAtivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRequestTurnAtivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnRequest.setOnClickListener {
            showSuccessDialog()
        }

        binding.btnShareFb.setOnClickListener {

        }
    }

    private fun showSuccessDialog() {
        val binding = DialogGetTurnSuccessBinding.inflate(layoutInflater)
        val dialogBuilder = AlertDialog.Builder(this)
            .setView(binding.root)
            .create()

        binding.animationHeart.playAnimation()
        dialogBuilder.show()
    }
}