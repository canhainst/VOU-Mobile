package com.example.vou_mobile.fragment

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
        val voucherTest = listOf(
            Voucher("0", "3","https://image.tienphong.vn/600x315/Uploaded/2024/pcgycivo/2023_09_22/thumb-anh-bia-3156.png", "Shopee Food", "Voucher Giảm Giá", "Giảm 10k trên giá món khao bạn đến 70k tổng đơn hàng", "01/01/2000", "ONLINE"),
            Voucher("1", "2","https://quanlydoitac.viettel.vn/files/qldt/public/voucher/image/2024/1/25/ed8daadf-0504-446d-b7c8-8074f3df13a4.jpg", "KFC", "Voucher Khuyến Mại","Combo Mùa hè sôi động chỉ 80k", "01/01/2000", "ONLINE"),
            Voucher("2", "6","https://magiamgiadienmayxanh.com/wp-content/uploads/2022/01/Phieu-mua-hang-Dien-May-Xanh-voucher-magiamgiadienmayxanh.jpg", "Điện máy xanh", "Voucher Khuyến Mại","Phiếu mua hàng trị giá 500k", "01/01/2000", "ONLINE")
        )

        val allUsedVouchers = view.findViewById<RecyclerView>(R.id.usedVoucherRecyclerView)
        allUsedVouchers.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        val adapter = VerticalUsedVoucherAdapter(voucherTest)
        adapter.setOnItemClickListener(this)
        allUsedVouchers.adapter = adapter

        return view
    }
    override fun onItemClick(position: Int) {
        replaceFragment(VoucherDetail())
    }
    private fun replaceFragment(fragment: Fragment){
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