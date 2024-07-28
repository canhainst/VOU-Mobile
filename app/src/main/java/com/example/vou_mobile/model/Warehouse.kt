package com.example.vou_mobile.model

data class Warehouse(
    var userID: String,
    var itemsOfEvents: List<ItemsOfEvent>,
    var voucherList: List<VouchersList>,
    var giftExchangesHistory: List<GiftExchangesHistory>
)
