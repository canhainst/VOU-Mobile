package com.example.vou_mobile.ViewModels

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.vou_mobile.R

class UpdateAccount : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_account)

        findViewById<TextView>(R.id.updateLater).setOnClickListener {
            val homePageActivity = Intent(this, HomePageActivity::class.java)
            startActivity(homePageActivity)
            finish()
        }

        findViewById<Button>(R.id.BtnDone).setOnClickListener {
            // save information

            // move to homepage
            val homePageActivity = Intent(this, HomePageActivity::class.java)
            startActivity(homePageActivity)
            finish()
        }

        findViewById<ImageView>(R.id.BtnAvatar).setOnClickListener {
            // add avatar cac thu

        }
    }
}