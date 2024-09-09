package com.example.vou_mobile.services.api

import com.example.vou_mobile.model.Item
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ItemService {
    @GET("getByIdEvent/{uuid}")
    fun getItemsByIdEvent(@Path("uuid") idEvent: String): Call<List<Item>>
}