package com.example.vou_mobile.activity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.example.vou_mobile.R
import com.example.vou_mobile.adapter.HorizontalPaymentMethodsAdapter
import com.example.vou_mobile.model.Billing
import com.example.vou_mobile.model.PaymentMethod
import com.example.vou_mobile.model.VoucherScanner
import com.example.vou_mobile.model.payment.Paypal
import com.example.vou_mobile.model.payment.ZaloPay
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import vn.zalopay.sdk.Environment
import vn.zalopay.sdk.ZaloPaySDK

class ScanningActivity : AppCompatActivity(), HorizontalPaymentMethodsAdapter.OnItemClickListener {
    private lateinit var codeScanner: CodeScanner
    private var switchQRCodeState: Boolean = true
    private lateinit var btnSwitchQRCode: ImageButton
    private lateinit var scannerView: CodeScannerView
    private lateinit var ivQRCode: ImageView
    private var selectedMethod: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scaninng)

        val paymentMethod = listOf(
            PaymentMethod("PayPal", "https://rgb.vn/wp-content/uploads/2014/05/rgb_vn_new_branding_paypal_2014_logo_detail.png"),
            PaymentMethod("Zalo Pay", "https://r2.thoainguyentek.com/2021/11/zalopay-logo.png")
        )

        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        // ZaloPay SDK Init
        ZaloPaySDK.init(2553, Environment.SANDBOX)

        val allPaymentMethodRecyclerView = findViewById<RecyclerView>(R.id.paymentMethodRecyclerView)
        allPaymentMethodRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        allPaymentMethodRecyclerView.adapter = HorizontalPaymentMethodsAdapter(paymentMethod, this)

        btnSwitchQRCode = findViewById(R.id.switchBtn)
        btnSwitchQRCode.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.baseline_qr_code_scanner_24))

        ivQRCode = findViewById(R.id.QRCode)
        scannerView = findViewById(R.id.codeScannerView)

        val voucherId = intent.getStringExtra("voucherId")
        val voucherValue = intent.getStringExtra("value")?.toInt()
        val voucherMaxDiscount = intent.getStringExtra("maxDiscount")?.toInt()

        val dataTest = VoucherScanner(voucherId!!, voucherValue, voucherMaxDiscount).toString()

        val data = dataTest.trim()

        runScanner()
        generateQR(data)

        btnSwitchQRCode.setOnClickListener {
            switchQRCodeState = if (switchQRCodeState) {
                btnSwitchQRCode.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.baseline_flip_24))
                scannerView.visibility = View.GONE
                ivQRCode.visibility = View.VISIBLE
                onPause()
                false
            } else {
                btnSwitchQRCode.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.baseline_qr_code_scanner_24))
                scannerView.visibility = View.VISIBLE
                ivQRCode.visibility = View.GONE
                onResume()
                true
            }
        }
    }
    override fun onItemClick(position: Int) {
        // Update selectedMethod based on the clicked item
        selectedMethod = when (position) {
            0 -> "PayPal"
            1 -> "ZaloPay"
            else -> ""
        }
    }
    private fun generateQR(data: String){
        if (data.isEmpty()){
            Toast.makeText(this, "Data is empty", Toast.LENGTH_SHORT).show()
        } else {
            val writer = QRCodeWriter()
            try {
                val bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, 512, 512)
                val width = bitMatrix.width
                val height = bitMatrix.height
                val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
                for (x in 0 until width)
                    for (y in 0 until height)
                        bmp.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)

                ivQRCode.setImageBitmap(bmp)
            } catch (e: WriterException) {
                e.printStackTrace()
            }
        }
    }
    private fun runScanner(){
        val scannerView = findViewById<CodeScannerView>(R.id.codeScannerView)
        codeScanner = CodeScanner(this, scannerView)

        codeScanner.camera = CodeScanner.CAMERA_BACK
        codeScanner.formats = CodeScanner.ALL_FORMATS
        codeScanner.autoFocusMode = AutoFocusMode.SAFE
        codeScanner.scanMode = ScanMode.SINGLE
        codeScanner.isAutoFocusEnabled = true
        codeScanner.isFlashEnabled = true

        codeScanner.decodeCallback = DecodeCallback {
            runOnUiThread{
                when (selectedMethod) {
                    "PayPal" -> {
                        val billing = stringToBilling(it.text)
                        val paypal = Paypal(billing!!, this)
                        paypal.payment { success, message ->
                            if (success) {
                                // Payment succeeded
                                Toast.makeText(this, "Payment successful: $message", Toast.LENGTH_LONG).show()
                            } else {
                                // Payment failed or canceled
                                Toast.makeText(this, "Payment failed: $message", Toast.LENGTH_LONG).show()
                            }
                        }
                        finish()
                    }
                    "ZaloPay" -> {
                        val billing = stringToBilling(it.text)
                        val zaloPay = ZaloPay(billing!!, this)
                        zaloPay.payment { success, message ->
                            if (success) {
                                // Payment succeeded
                                Toast.makeText(this, "Payment successful: $message", Toast.LENGTH_LONG).show()
                            } else {
                                // Payment failed or canceled
                                Toast.makeText(this, "Payment failed: $message", Toast.LENGTH_LONG).show()
                            }
                        }
                        finish()
                    }
                    else -> {
                        showCustomDialog(this)
                        onResume()
                    }
                }
            }
        }
        codeScanner.errorCallback = ErrorCallback {
            runOnUiThread {
                Toast.makeText(this, "Scan error: ${it.message}", Toast.LENGTH_LONG).show()
            }
        }
        scannerView.setOnClickListener{
            codeScanner.startPreview()
        }

        checkPermission(android.Manifest.permission.CAMERA, 200)
    }

    private fun stringToBilling(input: String): Billing? {
        val regex = Regex("""Billing\(brandName=(.*), price=([\d.]+)\)""")
        val matchResult = regex.find(input)

        return if (matchResult != null) {
            val (brandName, price) = matchResult.destructured
            Billing(brandName, price.toInt())
        } else {
            null
        }
    }
    @SuppressLint("SetTextI18n")
    private fun showCustomDialog(context: Context) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_send_items_result, null)
        val dialogBuilder = AlertDialog.Builder(context)
            .setView(dialogView)
            .create()

        val animationView = dialogView.findViewById<LottieAnimationView>(R.id.aniDialog)
        dialogView.findViewById<TextView>(R.id.sendingResult).text = "Are you missing something??"
        dialogView.findViewById<TextView>(R.id.sendingDescription).text = "Please select a Payment method\n(」° ロ °)」"
        animationView.setAnimation(R.raw.sad)
        animationView.playAnimation()

        dialogBuilder.setOnCancelListener {
            onResume()
        }
        dialogBuilder.show()
    }
    private fun checkPermission(permission: String, reqCode: Int){
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(permission), reqCode)
        }
    }
    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }
    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        ZaloPaySDK.getInstance().onResult(intent)
    }
}