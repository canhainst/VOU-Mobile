package com.example.vou_mobile.model

data class IsUsedVouchers(val voucherID: String, val isUsed: Boolean)
data class Voucher (
    var id: String,
    var brandID: String,
    var voucherPictureUrl: String?,
    var brandName: String?,
    var script: String?,
    var voucherDetail: String?,
    var expiration: String?,
    var type: String? //ONLINE , OFFLINE
)