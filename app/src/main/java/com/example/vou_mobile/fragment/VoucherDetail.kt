package com.example.vou_mobile.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vou_mobile.R
import com.example.vou_mobile.activity.ScanningActivity
import com.example.vou_mobile.adapter.VerticalPaymentMethodAdapter
import com.example.vou_mobile.model.Brand
import com.example.vou_mobile.model.PaymentMethod
import com.example.vou_mobile.services.api.BrandService
import com.example.vou_mobile.services.api.RetrofitClient
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class VoucherDetail : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private val paymentMethod = listOf(
        PaymentMethod("PayPal", "https://rgb.vn/wp-content/uploads/2014/05/rgb_vn_new_branding_paypal_2014_logo_detail.png"),
        PaymentMethod("Zalo Pay", "https://r2.thoainguyentek.com/2021/11/zalopay-logo.png")
    )
    private var id: String? = null
    private var value: Int? = null
    private var maxDiscount: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_voucher_detail, container, false)
        val data = arguments
        val isUsed = data?.getBoolean("isUsed")

        id = data?.getString("voucherId")
        value = data?.getInt("value")
        maxDiscount = data?.getInt("maxDiscount")

        Picasso.get()
            .load(data?.getString("voucherImgUrl"))
            .into(view.findViewById<ImageView>(R.id.brandImg))

        val brandService = RetrofitClient.instance.create(BrandService::class.java)
        val callBrand = brandService.getBrandByUuid(data?.getString("brandName")!!)
        callBrand.enqueue(object : Callback<Brand> {
            override fun onResponse(call: Call<Brand>, response: Response<Brand>) {
                if (response.isSuccessful) {
                    val brand = response.body()
                    if (brand != null) {
                        view.findViewById<TextView>(R.id.brand_name).text = brand.brand_name
                    }
                } else {
                    println("Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Brand>, t: Throwable) {
                println("Failed: ${t.message}")
            }
        })

        view.findViewById<TextView>(R.id.script).text = data.getString("script")
        view.findViewById<TextView>(R.id.expiryDate).text = data.getString("exp")
        view.findViewById<TextView>(R.id.voucher_detail).text = data.getString("detail")

        val btnUseVoucher = view.findViewById<Button>(R.id.btnUse)
        if(isUsed!!) {
            btnUseVoucher.isEnabled = false
            btnUseVoucher.text = "Used"
            btnUseVoucher.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.gray))
        }

        btnUseVoucher.setOnClickListener {
            if (data.getString("script") == "ONLINE"){
                showCustomDialog(requireContext())
            } else {
                val scanActivity = Intent(activity, ScanningActivity::class.java)
                scanActivity.putExtra("voucherId", id)
                scanActivity.putExtra("value", value)
                scanActivity.putExtra("maxDiscount", maxDiscount)
                startActivity(scanActivity)
            }
        }

        return view
    }

    private fun showCustomDialog(context: Context) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.all_method_online_layout, null)
        val dialogBuilder = AlertDialog.Builder(context)
            .setView(dialogView)
            .create()

        val allPaymentMethodRecyclerView = dialogView.findViewById<RecyclerView>(R.id.paymentMethodRecyclerView)
        allPaymentMethodRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        allPaymentMethodRecyclerView.adapter = VerticalPaymentMethodAdapter(paymentMethod)
        dialogBuilder.show()
    }
    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            VoucherDetail().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
