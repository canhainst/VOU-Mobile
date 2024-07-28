package com.example.vou_mobile.model

data class Event(
    var id: String?,
    var brandID: String,
    var eventName: String?,
    var brandName: String?,
    var eventPictureUrl: String?,
    var voucherCount: Int?,
    var startTime: String?, //hh:mm dd/mm/yyyy
    var endTime: String?,
    var typeOfEvent: Int, //0: lac_xi, 1: HQ_trivia
    var eventDetail: String?
)
