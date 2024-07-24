package com.example.vou_mobile.model

data class ItemWithInt(val item: String, val number: Int)
data class Warehouse(
    var userID: String?,
    var items: List<ItemWithInt>
)
