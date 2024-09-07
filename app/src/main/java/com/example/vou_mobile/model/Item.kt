package com.example.vou_mobile.model

data class ItemsOfEvent(
    var brand: Brand,
    val event: Event,
    val items: List<Item>
)
data class Item (
    var id_item: String,
    var name: String,
    var image: String,
    var quantity: Int
)
data class ItemBelong (
    var id_brand: String,
    var id_event: String
)
