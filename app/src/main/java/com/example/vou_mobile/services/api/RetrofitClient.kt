package com.example.vou_mobile.services.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://10.100.77.62:5001/"

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val requestBuilder = chain.request().newBuilder()
            requestBuilder.addHeader("authorization", "01724dc6-775a-4f52-95fd-245c615f2e77")
            requestBuilder.addHeader("Content-Type", "application/json")
            chain.proceed(requestBuilder.build())
        }
        .build()

    val instance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}