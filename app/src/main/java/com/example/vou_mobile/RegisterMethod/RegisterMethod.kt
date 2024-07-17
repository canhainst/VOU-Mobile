package com.example.vou_mobile.RegisterMethod

interface RegisterMethod {
    fun register(callback: (Boolean, String?) -> Unit)
}
