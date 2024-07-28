package com.example.vou_mobile.model.login

interface LoginMethod {
    fun login(callback: (Boolean, String?) -> Unit)
}
