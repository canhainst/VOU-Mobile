package com.example.vou_mobile.fragment

import android.annotation.SuppressLint
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
import com.example.vou_mobile.R
import com.example.vou_mobile.activity.ScanningActivity
import com.example.vou_mobile.adapter.HorizontalBrandsAdapter
import com.example.vou_mobile.model.Brand
import com.example.vou_mobile.services.BrandService
import com.example.vou_mobile.services.RetrofitClient
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class VoucherDetail : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

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
            val scanActivity = Intent(activity, ScanningActivity::class.java)
            startActivity(scanActivity)
        }

        return view
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
