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
data class SendGift(
    var id_giver: String,
    var user_name: String,
    var itemsList: List<ItemSent>
)
data class ItemSent(
    var id_item: String,
    var quantity: Int
)
data class SendItemsResponse(
    val success: Boolean,
    val message: String
)