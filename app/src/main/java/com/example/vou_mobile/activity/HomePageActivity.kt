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
    }

    fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.setCustomAnimations(
            R.anim.enter_from_right,
            R.anim.exit_to_left,
            R.anim.enter_from_left,
            R.anim.exit_to_right
        )
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
}