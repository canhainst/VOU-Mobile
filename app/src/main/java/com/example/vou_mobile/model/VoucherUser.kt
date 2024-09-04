package com.example.vou_mobile.model

data class VoucherUser (
    var id_voucher: String,
    var id_user: String,
    var quantity: Int,
    var used: Int?,
    var quantityUsed: Int?,
    var time_update: String
)