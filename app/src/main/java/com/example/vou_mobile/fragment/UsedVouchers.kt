package com.example.vou_mobile.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vou_mobile.R
import com.example.vou_mobile.adapter.VerticalUnusedVoucherAdapter
import com.example.vou_mobile.adapter.VerticalUsedVoucherAdapter
import com.example.vou_mobile.model.Voucher
import com.example.vou_mobile.services.RetrofitClient
import com.example.vou_mobile.services.VoucherService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [UsedVouchers.newInstance] factory method to
 * create an instance of this fragment.
 */
class UsedVouchers : Fragment(), VerticalUsedVoucherAdapter.OnItemClickListener {
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
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_used_vouchers, container, false)

        sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        uuid = sharedPreferences.getString("uuid", null)

        if (uuid != null) {
            val allUsedVouchers = view.findViewById<RecyclerView>(R.id.usedVoucherRecyclerView)
            allUsedVouchers.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

            val voucherService = RetrofitClient.instance.create(VoucherService::class.java)
            val callVoucherUsed = voucherService.getAllUsedVoucherByUserUuid(uuid!!)
            callVoucherUsed.enqueue(object : Callback<List<Voucher>> {
                override fun onResponse(call: Call<List<Voucher>>, response: Response<List<Voucher>>) {
                    if (response.isSuccessful) {
                        val vouchers = response.body()
                        if (vouchers != null) {
                            voucherList = vouchers
                            val adapter = VerticalUsedVoucherAdapter(vouchers)
                            adapter.setOnItemClickListener(this@UsedVouchers)
                            allUsedVouchers.adapter = adapter
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
        val data = Bundle().apply {
            putBoolean("isUsed", true)
            putString("voucherImgUrl", voucherList[position].image)
            putString("brandName", "Brand Name")
            putString("script", "Voucher")
            putString("exp", "exp")
            putString("detail", voucherList[position].description)
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
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment UsedVouchers.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UsedVouchers().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}