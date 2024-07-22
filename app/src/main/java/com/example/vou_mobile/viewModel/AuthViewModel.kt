package com.example.vou_mobile.viewModel

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vou_mobile.activity.HomePageActivity
import com.example.vou_mobile.activity.MainActivity
import com.example.vou_mobile.classData.User
import com.example.vou_mobile.model.loginMethod.LoginMethod
import com.example.vou_mobile.model.registerMethod.RegisterMethod
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AuthViewModel : ViewModel() {
    private val _loginResult = MutableLiveData<Pair<Boolean, String?>>()
    val loginResult: LiveData<Pair<Boolean, String?>> = _loginResult
    fun loginWithMethod(loginMethod: LoginMethod) {
        loginMethod.login { success, message ->
            _loginResult.postValue(Pair(success, message))
        }
    }

    private val _registerResult = MutableLiveData<Pair<Boolean, String?>>()
    val registerResult: LiveData<Pair<Boolean, String?>> = _registerResult
    fun registerWithMethod(registerMethod: RegisterMethod) {
        registerMethod.register { success, message ->
            _registerResult.postValue(Pair(success, message))
        }
    }
}