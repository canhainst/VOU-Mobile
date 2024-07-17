package com.example.vou_mobile.LoginMethod

interface LoginMethod {
    fun login(callback: (Boolean, String?) -> Unit)
}
