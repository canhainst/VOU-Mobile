package com.example.vou_mobile.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vou_mobile.model.login.LoginMethod
import com.example.vou_mobile.model.register.RegisterMethod

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