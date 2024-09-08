package com.example.vou_mobile.model

data class VoucherUser (
    var id_voucher: String,
    var id_user: String,
    var quantity: Int,
    var used: Int?,
    var time_update: String
)
data class VoucherScanner (
    var voucherId: String,
    var discountPercent: Int?,
    var maxDiscount: Int?
)