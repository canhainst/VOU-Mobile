package com.example.vou_mobile.model.payment

import android.content.Context
import com.example.vou_mobile.model.Billing
import com.paypal.android.sdk.payments.PayPalConfiguration

class Paypal(private var billing: Billing, private val context: Context): PaymentMethod {
    private var clientID = "AVh_Fsp1sPKVTH7cRu-Di7PL3Y-PmSqqr8oaR-U2vqlBIr_x0-uuAa0FONBXqnofhtsGt8bG_OnyMDRC"
    private var secretID = "ED_Pch8X8XiDXU-uGIuO5UR6g0QXVMJTlTzrwK9-2Q8Dq8cI9j6cVB4YC52un2gUShLeZJbEo-NHEAfN"
    private var returnUrl = "com.devshiv.paypaltest://paypalpay"

    private var PAYPAL_REQUEST_CODE: Int = 123
    private lateinit var configuration: PayPalConfiguration
    override fun payment(callback: (Boolean, String?) -> Unit) {
        configuration = PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(clientID)

    }
}