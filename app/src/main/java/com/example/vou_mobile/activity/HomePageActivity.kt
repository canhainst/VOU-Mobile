package com.example.vou_mobile.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.vou_mobile.R
import com.example.vou_mobile.databinding.ActivityHomePageBinding
import com.example.vou_mobile.fragment.Account
import com.example.vou_mobile.fragment.FavoriteEvent
import com.example.vou_mobile.fragment.HomePage
import com.google.firebase.auth.FirebaseAuth

class HomePageActivity : AppCompatActivity() {
    private lateinit var currentUserID: String
    private lateinit var binding: ActivityHomePageBinding
    private var initialX: Float = 0f
    private var initialY: Float = 0f

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser != null){
            currentUserID = currentUser.uid
            println(currentUserID)
        }

        binding = ActivityHomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(HomePage())

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home -> replaceFragment(HomePage())
                R.id.event -> replaceFragment(FavoriteEvent())
                R.id.account -> replaceFragment(Account())
                else -> {
                }
            }
            true
        }

        // Add setOnTouchListener to a view, for example, the root view of the activity
        binding.root.setOnTouchListener(object : View.OnTouchListener {
            @SuppressLint("ClickableViewAccessibility")
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        initialX = event.x
                        initialY = event.y
                        return true
                    }
                    MotionEvent.ACTION_UP -> {
                        val finalX = event.x
                        val finalY = event.y
                        val deltaX = finalX - initialX
                        val deltaY = finalY - initialY

                        if (Math.abs(deltaX) > Math.abs(deltaY)) {
                            if (Math.abs(deltaX) > 100) {
                                if (deltaX > 0) {
                                    onBackPressed()
                                }
                                return true
                            }
                        }
                    }
                }
                return false
            }
        })
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()
    }
}