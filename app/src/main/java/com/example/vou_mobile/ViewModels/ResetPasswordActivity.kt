package com.example.vou_mobile.ViewModels

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.vou_mobile.R

class ResetPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        findViewById<Button>(R.id.button).setOnClickListener {
            val verifyCode = Intent(this, VerifyCodeActivity::class.java)
            startActivity(verifyCode)
            finish()
        }
    }
}