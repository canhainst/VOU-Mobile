package com.example.vou_mobile.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vou_mobile.model.loginMethod.LoginMethod
import com.example.vou_mobile.model.registerMethod.RegisterMethod

class AuthViewModel : ViewModel() {
    private val _authStatus = MutableLiveData<Boolean>()
    val authStatus: LiveData<Boolean> get() = _authStatus

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun login(loginMethod: LoginMethod) {
        loginMethod.login { success, message ->
            _authStatus.postValue(success)
            _errorMessage.postValue(message)
        }
    }

    fun register(registerMethod: RegisterMethod) {
        registerMethod.register { success, message ->
            _authStatus.postValue(success)
            _errorMessage.postValue(message)
        }
    }
}
