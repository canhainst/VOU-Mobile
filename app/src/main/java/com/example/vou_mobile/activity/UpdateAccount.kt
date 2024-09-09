package com.example.vou_mobile.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.example.vou_mobile.R
import com.example.vou_mobile.model.NewUser
import com.example.vou_mobile.model.User
import com.example.vou_mobile.services.api.RetrofitClient
import com.example.vou_mobile.services.api.UserService
import com.example.vou_mobile.utilities.UserUtils
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Callback
import retrofit2.Call
import retrofit2.Response

class UpdateAccount : AppCompatActivity() {
    private lateinit var email: TextInputEditText
    private lateinit var dob: TextInputEditText
    private lateinit var gender: Spinner
    private var avtUrl: String = "https://i.imgur.com/ynpbrKV.jpg"
    private lateinit var sharedPreferences: SharedPreferences

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_account)

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        val username = loadCredentialsFromLocal("username")
        val password = loadCredentialsFromLocal("password")
        val sharedPreferences = getSharedPreferences("UserCredentials", Context.MODE_PRIVATE)
        val number = sharedPreferences.getString("number", null)

        dob = findViewById(R.id.inputDOB)
        email = findViewById(R.id.inputEmail)
        gender = findViewById(R.id.spinnerGender)
        val genderString = when (gender.selectedItem) {
            "Male" -> "Nam"
            "Female" -> "Nữ"
            else -> "else"
        }

        val userService = RetrofitClient.instance.create(UserService::class.java)

        findViewById<TextView>(R.id.textView7).text = "Hi, $username"

        findViewById<TextView>(R.id.updateLater).setOnClickListener {
            // save information without email, dob, gender, avtUrl to Firebase Realtime
            if (username != null && password != null) {
                val user = NewUser(username, username, password, avtUrl, dob.text.toString(), gender.selectedItem.toString(), "", email.text.toString(), number!!, "Người chơi", "Active")
//                UserUtils.addNewUser(user)
                showToast("Account successfully created")
            }
            // move to homepage
//            navigateToHomePage()
        }

        findViewById<Button>(R.id.BtnDone).setOnClickListener {
            // save all information to Firebase Realtime
            if (username != null && password != null) {
                val user = NewUser( username, username, password, avtUrl, dob.text.toString(), genderString, "", email.text.toString(), number!!, "Người chơi", "Active")
                userService.createNewUser(user).enqueue(object : Callback<User> {
                    override fun onResponse(call: Call<User>, response: Response<User>) {
                        if (response.isSuccessful) {
                            val createdUserId = response.body()
                            with(sharedPreferences.edit()) {
                                putString("uuid", createdUserId!!.id)
                                apply()
                            }
                            UserUtils.addNewUser(createdUserId!!) { success ->
                                if (success!!) {
                                    // Handle success, e.g., show a toast message
                                    showToast("User added successfully")
                                    navigateToHomePage()
                                } else {
                                    // Handle failure, e.g., show an error message
                                    showToast("Failed to add user")
                                }
                            }
                        } else {
                            showToast("Failed to create user. Error code: ${response.code()}")
                        }
                    }

                    override fun onFailure(call: Call<User>, t: Throwable) {
                        showToast("API call failed: ${t.message}")
                    }
                })
                showToast("Account successfully created")
            }
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