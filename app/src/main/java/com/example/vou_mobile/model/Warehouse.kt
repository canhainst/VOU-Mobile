package com.example.vou_mobile.model

data class Warehouse(
    var userID: String,
    var itemsOfEvents: List<ItemsOfEvent>,
    var voucherList: List<IsUsedVouchers>,
    var giftExchangesHistory: List<GiftExchangesHistory>
)
