package com.example.vou_mobile.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.vou_mobile.R
import com.example.vou_mobile.Utilities.UserUtils
import com.example.vou_mobile.classData.User
import com.example.vou_mobile.viewModel.AuthViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.core.utilities.Utilities

class UpdateAccount : AppCompatActivity() {
    private lateinit var email: String
    private lateinit var dob: String
    private lateinit var gender: String
    private var avtUrl: String = ""

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_account)

        val username = loadCredentialsFromLocal("username")
        val password = loadCredentialsFromLocal("password")

        findViewById<TextView>(R.id.textView7).text = "Hi, $username"

        findViewById<TextView>(R.id.updateLater).setOnClickListener {
            // save information without email, dob, gender, avtUrl to Firebase Realtime
            if (username != null && password != null) {
                val user = User(username, password, null, null, null, null)
                UserUtils.addNewUser(user)
                showToast("Account successfully created")
            }
            // move to homepage
            navigateToHomePage()
        }

        findViewById<Button>(R.id.BtnDone).setOnClickListener {
            email = findViewById<TextInputEditText>(R.id.inputEmail).text.toString()
            dob = findViewById<TextInputEditText>(R.id.inputDOB).text.toString()
            gender = findViewById<Spinner>(R.id.spinnerGender).selectedItem.toString()

            // save all information to Firebase Realtime
            if (username != null && password != null) {
                val user = User(username, password, email, dob, gender, avtUrl)
                UserUtils.addNewUser(user)
                showToast("Account successfully created")
            }
            // move to homepage
            navigateToHomePage()
        }

        findViewById<ImageView>(R.id.BtnAvatar).setOnClickListener {
            // add avatar url
        }
    }
    private fun loadCredentialsFromLocal(key: String, defaultValue: String? = null): String? {
        val sharedPreferences = getSharedPreferences("UserCredentials", Context.MODE_PRIVATE)
        return sharedPreferences.getString(key, defaultValue)
    }
    private fun navigateToHomePage(){
        val homePageActivity = Intent(this, HomePageActivity::class.java)
        startActivity(homePageActivity)
        finish()
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}