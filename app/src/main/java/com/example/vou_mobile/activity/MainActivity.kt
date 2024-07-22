package com.example.vou_mobile.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.vou_mobile.viewModel.AuthViewModel
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var authViewModel: AuthViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize AuthViewModel
        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]

        // Check login status
        if (isUserLoggedIn()) {
            navigateToHomePage()
        } else {
            navigateToSignInPage()
        }

        // Observe the register result
        authViewModel.registerResult.observe(this) { result ->
            if (result.first) {
                // Registration successful
                navigateToHomePage()
            } else {
                // Registration failed, handle the error
                showToast(result.second ?: "Registration failed")
            }
        }
    }
    private fun isUserLoggedIn(): Boolean {
        val currentUser = FirebaseAuth.getInstance().currentUser
        return currentUser != null
    }
    private fun navigateToHomePage() {
        val homePageIntent = Intent(this, HomePageActivity::class.java)
        startActivity(homePageIntent)
        finish()
    }
    private fun navigateToSignInPage() {
        val signInIntent = Intent(this, SignInActivity::class.java)
        startActivity(signInIntent)
        finish()
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}