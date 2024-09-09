package com.example.vou_mobile.model

data class VoucherInEvent (
    var id: String,
    var id_voucher_code: String?,
    var id_event: String,
    var exp_data: String?,
    var total_quantity: Int,
    val voucher: Voucher
)