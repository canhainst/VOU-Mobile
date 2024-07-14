package com.example.vou_mobile.ViewModels

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.vou_mobile.R

class VerifyCodeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_code)

        findViewById<Button>(R.id.BtnConfirm).setOnClickListener {
            if (verifyCode()) {
                val updateAccount = Intent(this, UpdateAccount::class.java)
                startActivity(updateAccount)
                finish()
            } else {
                // do nothing
            }
        }
    }

    private fun verifyCode(): Boolean {
        // check code cac thu

        return true
    }
}