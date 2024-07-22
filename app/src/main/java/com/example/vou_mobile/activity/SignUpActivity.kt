package com.example.vou_mobile.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.vou_mobile.R
import com.example.vou_mobile.model.registerMethod.PhoneRegister
import com.example.vou_mobile.viewModel.AuthViewModel
import com.google.android.material.textfield.TextInputEditText

class SignUpActivity : AppCompatActivity() {
    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]

        val inputName = findViewById<TextInputEditText>(R.id.inputName)
        val inputNumberPhone = findViewById<TextInputEditText>(R.id.inputNumberPhone)
        val editTextPassword = findViewById<EditText>(R.id.editTextTextPassword)
        val termCheckbox = findViewById<RadioButton>(R.id.term)
        val signUpButton = findViewById<Button>(R.id.button)
        val backToLoginTextView = findViewById<TextView>(R.id.backToLogin)

        signUpButton.setOnClickListener {
            val username = inputName.text.toString()
            val phoneNumber = inputNumberPhone.text.toString()
            val password = editTextPassword.text.toString()

            if (termCheckbox.isChecked && username.isNotEmpty() && phoneNumber.isNotEmpty() && password.isNotEmpty()) {
                saveCredentialsToLocal(username, password)
                val phoneRegister = PhoneRegister(this, phoneNumber)
                authViewModel.registerWithMethod(phoneRegister)
                authViewModel.registerResult.observe(this) { result ->
                    result?.let {
                        val (isSuccessful, message) = it
                        if (isSuccessful) {
                            // Handle successful registration
                            showToast("Registration successful")
                            // Proceed to next activity or update UI
                        } else {
                            // Handle registration failure
                            showToast(message ?: "Registration failed")
                        }
                    }
                }
            } else {
                showToast("Please fill in all fields and agree to the terms.")
            }
        }

        backToLoginTextView.setOnClickListener {
            val backToLoginIntent = Intent(this, SignInActivity::class.java)
            startActivity(backToLoginIntent)
            finish()
        }
    }
    private fun saveCredentialsToLocal(username: String, password: String) {
        val sharedPreferences = getSharedPreferences("UserCredentials", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("username", username)
        editor.putString("password", password)
        editor.apply()
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}