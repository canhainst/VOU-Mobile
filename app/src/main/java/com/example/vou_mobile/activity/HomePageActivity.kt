package com.example.vou_mobile.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.vou_mobile.R
import com.example.vou_mobile.databinding.ActivityHomePageBinding
import com.example.vou_mobile.fragment.Account
import com.example.vou_mobile.fragment.FavoriteEvent
import com.example.vou_mobile.fragment.HomePage
import com.example.vou_mobile.fragment.SendItem
import com.example.vou_mobile.model.User
import com.example.vou_mobile.services.api.RetrofitClient
import com.example.vou_mobile.services.api.UserService
import com.example.vou_mobile.utilities.UserUtils
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomePageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomePageBinding
    private lateinit var sharedPreferences: SharedPreferences
    private val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomePageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Khởi tạo sharedPreferences bên trong onCreate
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        val uuid = sharedPreferences.getString("uuid", null)

//        UserUtils.getUuidById { uuid ->
//            with(sharedPreferences.edit()) {
//                putString("uuid", uuid)
//                apply()
//                getUserByUUID(uuid!!){
//                    replaceFragment(HomePage())
//                }
//            }
//        }

        if (uuid == null) {
            UserUtils.getUuidById { uuid ->
                with(sharedPreferences.edit()) {
                    putString("uuid", uuid)
                    apply()
                    getUserByUUID(uuid!!){
                        replaceFragment(HomePage())
                    }
                }
            }
        } else {
            getUserByUUID(uuid){
                replaceFragment(HomePage())
            }
        }

        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> replaceFragment(HomePage())
                R.id.event -> replaceFragment(FavoriteEvent())
                R.id.account -> replaceFragment(Account())
                else -> {}
            }
            true
        }

        if (intent.getBooleanExtra("sendItem", false)) {
            replaceFragment(SendItem())
        }

        // Add OnBackStackChangedListener
        supportFragmentManager.addOnBackStackChangedListener {
            updateBottomNavigationView()
        }
    }

    private fun getUserByUUID(uuid: String, callback: () -> Unit){
        val userService = RetrofitClient.instance.create(UserService::class.java)
        val call = userService.getUserByUUID(uuid)
        call.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    val user = response.body()
                    if (user != null) {
                        // Chuyển đổi đối tượng User thành JSON và lưu vào SharedPreferences
                        val userJson = gson.toJson(user)
                        with(sharedPreferences.edit()) {
                            putString("currentUser", userJson)
                            apply()
                        }
                    }
                } else {
                    println("Error: ${response.code()}")
                }
                callback()
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                println("Failed: ${t.message}")
                callback()
            }
        })
    }

    private fun updateBottomNavigationView() {
        when (supportFragmentManager.findFragmentById(R.id.frameLayout)) {
            is HomePage -> binding.bottomNavigationView.menu.findItem(R.id.home).isChecked = true
            is FavoriteEvent -> binding.bottomNavigationView.menu.findItem(R.id.event).isChecked = true
            is Account -> binding.bottomNavigationView.menu.findItem(R.id.account).isChecked = true
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

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.frameLayout)
        if (currentFragment is SendItem) {
            if (intent.getBooleanExtra("fromShakingGame", false)) {
                val intent = Intent(this, ShakingGameActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                super.onBackPressed()
            }
        } else {
            super.onBackPressed()
        }
    }

}
