package com.example.vou_mobile.model.payment

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.example.vou_mobile.model.Billing
import com.paypal.android.sdk.payments.PayPalConfiguration
import com.paypal.android.sdk.payments.PayPalPayment
import com.paypal.android.sdk.payments.PayPalService
import com.paypal.android.sdk.payments.PaymentActivity
import java.math.BigDecimal

class Paypal(private var billing: Billing, private val context: Context) : PaymentMethod {

    var PAYPAL_REQUEST_CODE: Int = 123
    var configuration: PayPalConfiguration = PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
        .clientId("AVh_Fsp1sPKVTH7cRu-Di7PL3Y-PmSqqr8oaR-U2vqlBIr_x0-uuAa0FONBXqnofhtsGt8bG_OnyMDRC")

    override fun payment(callback: (Boolean, String?) -> Unit) {
        val payment = PayPalPayment(
            BigDecimal(billing.price),
            "USD",
            "Description of the Payment",
            PayPalPayment.PAYMENT_INTENT_SALE
        )

        val paypalIntent = Intent(context, PaymentActivity::class.java).apply {
            putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configuration)
            putExtra(PaymentActivity.EXTRA_PAYMENT, payment)
        }
        println(configuration)
        println(payment)
        (context as Activity).startActivityForResult(paypalIntent, PAYPAL_REQUEST_CODE)
    }
}
