package com.example.vou_mobile.model.payment

import android.app.Activity
import android.content.Context
import android.widget.Toast
import com.example.vou_mobile.Api.CreateOrder
import com.example.vou_mobile.model.Billing
import vn.zalopay.sdk.ZaloPayError
import vn.zalopay.sdk.ZaloPaySDK
import vn.zalopay.sdk.listeners.PayOrderListener

class ZaloPay(private var billing: Billing, private val context: Context): PaymentMethod {
    override fun payment(callback: (Boolean, String?) -> Unit) {
        val orderApi = CreateOrder()
        try {
            val data = orderApi.createOrder(billing.price.toString())
            val code = data.getString("return_code")
            Toast.makeText(context, "return_code: $code", Toast.LENGTH_LONG)
                .show()
            if (code == "1") {
                val token = data.getString("zp_trans_token")
                ZaloPaySDK.getInstance().payOrder(context as Activity, token, "demozpdk://app", object :
                    PayOrderListener {
                    override fun onPaymentSucceeded(p0: String?, p1: String?, p2: String?) {
                        println("thanh cong")
                    }

                    override fun onPaymentCanceled(p0: String?, p1: String?) {
                        println("huy")
                    }

                    override fun onPaymentError(
                        p0: ZaloPayError?,
                        p1: String?,
                        p2: String?
                    ) {
                        println("that bai")
                    }
                })
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}