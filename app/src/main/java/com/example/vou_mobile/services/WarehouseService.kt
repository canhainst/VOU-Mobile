package com.example.vou_mobile.services

import com.example.vou_mobile.model.GiftExchangesHistory
import com.example.vou_mobile.model.Item
import com.example.vou_mobile.model.ItemBelong
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface WarehouseService {
    @GET("user/items/{uuid}")
    fun getBrandsAndEventsOfItemsByUser(@Path("uuid") uuid: String): Call<List<ItemBelong>>
    @GET("user/items/{uuid}/{id_event}")
    fun getItemsOfEventByUser(@Path("uuid") uuid: String, @Path("id_event") id_event: String): Call<List<Item>>
    @GET("warehouse/getItem/{uuid}")
    fun getItemById(@Path("uuid") uuid: String): Call<Item>
    @GET("user/gift_items_history/{uuid}")
    fun getGiftItemsHistoryByGiver(@Path("uuid") uuid: String): Call<List<GiftExchangesHistory>>
}