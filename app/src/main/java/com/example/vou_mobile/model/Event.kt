package com.example.vou_mobile.model

data class Event(
    var id: String?,
    var name: String,
    var brand: String,
    var imageUrl: String,
    var voucherCount: Int,
    var startTime: String,
    var endTime: String
)
