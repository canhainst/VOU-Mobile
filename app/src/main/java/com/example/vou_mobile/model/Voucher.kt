package com.example.vou_mobile.model

data class IsUsedVouchers(val voucherID: String, val isUsed: Boolean)
data class Voucher (
    var voucher_code: String,
    var id_brand: String,
    var image: String,
    var value: Int,
    var max_discount: Int,
    var description: String?,
    var status: String,
    var type: String?,
    var time_update: String?,
    var quantity: Int?
)