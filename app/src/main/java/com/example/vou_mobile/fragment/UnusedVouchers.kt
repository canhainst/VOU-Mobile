package com.example.vou_mobile.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vou_mobile.R
import com.example.vou_mobile.adapter.VerticalUnusedVoucherAdapter
import com.example.vou_mobile.model.Voucher
import com.example.vou_mobile.services.api.RetrofitClient
import com.example.vou_mobile.services.api.VoucherService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class UnusedVouchers : Fragment(), VerticalUnusedVoucherAdapter.OnItemClickListener {
    private var param1: String? = null
    private var param2: String? = null
    private var voucherList: List<Voucher> = emptyList()
    private lateinit var sharedPreferences: SharedPreferences
    private var uuid: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_unused_vouchers, container, false)

        sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        uuid = sharedPreferences.getString("uuid", null)

        if (uuid != null) {
            val allUnusedVouchers = view.findViewById<RecyclerView>(R.id.unusedVoucherRecyclerView)
            allUnusedVouchers.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

            val voucherService = RetrofitClient.instance.create(VoucherService::class.java)
            val callVoucher = voucherService.getAllVoucherByUserUuid(uuid!!)
            callVoucher.enqueue(object : Callback<List<Voucher>> {
                override fun onResponse(call: Call<List<Voucher>>, response: Response<List<Voucher>>) {
                    if (response.isSuccessful) {
                        val vouchers = response.body()
                        if (vouchers != null) {
                            voucherList = vouchers
                            val adapter = VerticalUnusedVoucherAdapter(vouchers)
                            adapter.setOnItemClickListener(this@UnusedVouchers)
                            allUnusedVouchers.adapter = adapter
                        }
                    } else {
                        println("Error: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<List<Voucher>>, t: Throwable) {
                    println("Failed: ${t.message}")
                }
            })
        } else {
            println("UUID is null.")
        }

        return view
    }

    override fun onItemClick(position: Int) {
        val voucher = voucherList[position]
        val data = Bundle().apply {
            putBoolean("isUsed", false)
            putString("voucherId", voucher.voucher_code)
            putInt("maxDiscount", voucher.max_discount)
            putInt("value", voucher.value)
            putString("voucherImgUrl", voucher.image)
            putString("brandName", voucher.id_brand)
            putString("script", voucher.type)
            putString("exp", "exp")
            putString("detail", voucher.description)
        }
        replaceFragment(VoucherDetail(), data)
    }

    private fun replaceFragment(fragment: Fragment, data: Bundle){
        fragment.arguments = data
        val fragmentManager = parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.setCustomAnimations(
            R.anim.enter_from_right,
            R.anim.exit_to_left,
            R.anim.enter_from_left,
            R.anim.exit_to_right
        )
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UnusedVouchers().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

