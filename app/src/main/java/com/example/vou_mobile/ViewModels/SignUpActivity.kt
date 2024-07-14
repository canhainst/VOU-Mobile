package com.example.vou_mobile.ViewModels

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.TextView
import com.example.vou_mobile.R

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        findViewById<Button>(R.id.button).setOnClickListener {
            if (findViewById<RadioButton>(R.id.term).isChecked){
                if (checkLoginStatus()){
                    val homePageActivity = Intent(this, HomePageActivity::class.java)
                    startActivity(homePageActivity)
                    finish()
                } else {
                    // do nothing
                }
            } else {
                //do nothing
            }
        }

        findViewById<TextView>(R.id.backToLogin).setOnClickListener {
            val backToLogin = Intent(this, SignInActivity::class.java)
            startActivity(backToLogin)
            finish()
        }
    }

    private fun checkLoginStatus(): Boolean {
        // check dang nhap cac thu


        return true
    }
}