package com.example.vou_mobile.model

data class Turn(
    var idUser: String?,
    var idEvent: String?,
    var quantity: Int = 10,
    var isUsed: Boolean = false
)
