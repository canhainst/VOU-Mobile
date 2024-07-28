package com.example.vou_mobile.model.register

interface RegisterMethod {
    fun register(callback: (Boolean, String?) -> Unit)
}
