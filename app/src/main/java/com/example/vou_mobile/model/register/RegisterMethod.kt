package com.example.vou_mobile.model.registerMethod

interface RegisterMethod {
    fun register(callback: (Boolean, String?) -> Unit)
}
