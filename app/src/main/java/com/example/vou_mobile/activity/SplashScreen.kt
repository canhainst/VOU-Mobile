package com.example.vou_mobile.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.airbnb.lottie.LottieAnimationView
import com.example.vou_mobile.R

@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val animationView = findViewById<LottieAnimationView>(R.id.logo1)
        val animationView2 = findViewById<LottieAnimationView>(R.id.logo2)
        val animationView3 = findViewById<LottieAnimationView>(R.id.logo3)

        // Bắt đầu animation
        animationView.playAnimation()
        animationView2.playAnimation()
        animationView3.playAnimation()

        Handler().postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 7000)
    }
}