package com.example.vou_mobile.model

data class Event(
    var id: String,
    var id_brand: String,
    var name: String?,
    var image: String?,
    var start_time: String?, //hh:mm dd/mm/yyyy/
    var end_time: String?,
    var time_update: String?,
    var type: String?, //Quiz, Lac xi - 0/
    var id_game: String?
)
