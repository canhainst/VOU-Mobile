package com.example.vou_mobile.model

data class Items(val itemID: String, val quantity: Int)
data class ItemsOfEvent(val eventID: String, val items: List<Items>)
data class Item (
    var id: String,
    var id_event: String,
    var name: String?,
    var image: String?
)
