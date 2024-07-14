package com.example.vou_mobile.ViewModels

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.vou_mobile.R

class SignInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        findViewById<Button>(R.id.button).setOnClickListener {
            if (checkLoginStatus()){
                val homePageActivity = Intent(this, HomePageActivity::class.java)
                startActivity(homePageActivity)
                finish()
            } else {
                // do nothing
            }
        }

        findViewById<TextView>(R.id.fgPassword).setOnClickListener {
            val forgotPassword = Intent(this, ResetPasswordActivity::class.java)
            startActivity(forgotPassword)
            finish()
        }

        findViewById<TextView>(R.id.CreateAccount).setOnClickListener {
            val createAccount = Intent(this, SignUpActivity::class.java)
            startActivity(createAccount)
            finish()
        }
    }

    private fun checkLoginStatus(): Boolean {
        // check dang nhap cac thu


        return true
    }
}