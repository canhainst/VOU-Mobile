package com.example.vou_mobile.ViewModels

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.vou_mobile.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Kiểm tra trạng thái đăng nhập ở đây
        if (!checkLoginStatus()) {
            val loginIntent = Intent(this, SignInActivity::class.java)
            startActivity(loginIntent)
            finish()
            return
        } else {
            val homePageActivity = Intent(this, HomePageActivity::class.java)
            startActivity(homePageActivity)
            finish()
            return
        }
    }

    private fun checkLoginStatus(): Boolean {
        // check dang nhap cac thu


        return false
    }
}