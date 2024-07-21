package com.example.vou_mobile.model.loginMethod

interface LoginMethod {
    fun login(callback: (Boolean, String?) -> Unit)
}
