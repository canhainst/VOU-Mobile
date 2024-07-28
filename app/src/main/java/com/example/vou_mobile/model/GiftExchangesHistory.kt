package com.example.vou_mobile.model

data class GiftExchangesHistory (
    var exchangeTime: String?,
    var receiverID: String,
    var eventID: String,
    var listItems: List<Items>
)