package com.example.vou_mobile.services.api

import com.example.vou_mobile.model.Voucher
import com.example.vou_mobile.model.VoucherUser
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

data class addVoucherRequest(
    val id_voucher: String,
    val id_user: String
)
interface VoucherService {
    @GET("voucher/getAll_active")
    fun getAllVoucherActive(): Call<List<Voucher>>
    @GET("warehouse/get/{uuid}")
    fun getAllVoucherByUserUuid(@Path("uuid") uuid: String): Call<List<Voucher>>
    @POST("warehouse/addVoucher")
    fun addVoucherIntoWarehouse(@Body request: addVoucherRequest): Call<VoucherUser>
}