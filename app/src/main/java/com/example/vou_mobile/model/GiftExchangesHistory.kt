package com.example.vou_mobile.model

data class GiftDetail(
    var id: String,
    var quantity: Int,
    var name: String
)

data class GiftExchangesHistory(
    var gift_time: String?,
    var id_recipient: String,
    var id_item: List<GiftDetail>
)