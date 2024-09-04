package com.example.vou_mobile.services

import com.example.vou_mobile.model.Brand
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface BrandService {
    @GET("brand/getAll")
    fun getAllBrand(): Call<List<Brand>>
    @GET("brand/get/{uuid}")
    fun getBrandByUuid(@Path("uuid") uuid: String): Call<Brand>
}