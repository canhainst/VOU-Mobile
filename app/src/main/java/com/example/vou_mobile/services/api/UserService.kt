package com.example.vou_mobile.services.api

import com.example.vou_mobile.model.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface UserService {
    @GET("user/get/{uuid}")
    fun getUserByUUID(@Path("uuid") uuid: String): Call<User>

}