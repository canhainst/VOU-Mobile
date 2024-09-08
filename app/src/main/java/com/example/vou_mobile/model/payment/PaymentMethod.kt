package com.example.vou_mobile.model.payment

interface PaymentMethod {
    fun payment(callback: (Boolean, String?) -> Unit)
}