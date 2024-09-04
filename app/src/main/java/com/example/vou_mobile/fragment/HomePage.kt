package com.example.vou_mobile.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vou_mobile.R
import com.example.vou_mobile.adapter.FavoriteEventAdapter
import com.example.vou_mobile.adapter.HorizontalBrandsAdapter
import com.example.vou_mobile.adapter.HorizontalEventsAdapter
import com.example.vou_mobile.adapter.HorizontalVouchersAdapter
import com.example.vou_mobile.model.Brand
import com.example.vou_mobile.model.Event
import com.example.vou_mobile.model.Voucher
import com.example.vou_mobile.viewModel.EventViewModelProviderSingleton
import com.example.vou_mobile.viewModel.GameViewModel
import com.squareup.picasso.Picasso

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomePage.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomePage : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val gameViewModel = GameViewModel()
    private val viewModel = EventViewModelProviderSingleton.getEventViewModel()


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
        val view = inflater.inflate(R.layout.fragment_home_page, container, false)

        val userPictureUrl = "https://scontent.fsgn2-8.fna.fbcdn.net/v/t39.30808-6/446651424_1681220119288938_4828402852445544478_n.jpg?_nc_cat=102&ccb=1-7&_nc_sid=6ee11a&_nc_ohc=MHCPT2zDHoEQ7kNvgG2yjD-&_nc_ht=scontent.fsgn2-8.fna&oh=00_AYCz7QsbOTWFy-oLUAowm7ba85crAps7UHZfvK4xn-ewPA&oe=66A4CB11"
        val username = "Nguyen Thanh"

        Picasso.get()
            .load(userPictureUrl)
            .into(view.findViewById<ImageView>(R.id.imageView))
        view.findViewById<TextView>(R.id.username).text = username

        val allBrandRecyclerView = view.findViewById<RecyclerView>(R.id.brand)
        allBrandRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        val brandTest = listOf(
            Brand("0", "https://downloadlogomienphi.com/sites/default/files/logos/download-logo-phuclong-mien-phi.jpg", "Phúc Long"),
            Brand("1","https://play-lh.googleusercontent.com/KBMEAtNbnht-M9jqeJqiFCDqazutWY_OQk7UyfJfcO6QO1PI6EWWm0G6j1D60dgNN12-", "Shopee Food"),
            Brand("2","https://seeklogo.com/images/K/kfc-logo-A232F2E6D1-seeklogo.com.png", "KFC"),
            Brand("3","https://logodix.com/logo/2015053.png", "Shopee"),
            Brand("4","https://upload.wikimedia.org/wikipedia/vi/b/b1/Logo_GSM_xanh_SM.png", "Xanh SM"),
            Brand("5","https://downloadlogomienphi.com/sites/default/files/logos/download-logo-phuclong-mien-phi.jpg", "Phúc Long"),
            Brand("6","https://upload.wikimedia.org/wikipedia/vi/b/b1/Logo_GSM_xanh_SM.png", "Xanh SM")
            )

        allBrandRecyclerView.adapter = HorizontalBrandsAdapter(brandTest)

        val allEventRecyclerView = view.findViewById<RecyclerView>(R.id.event)
        viewModel.loadAllEvents()
        viewModel.events.observe(viewLifecycleOwner, Observer { items ->
            allEventRecyclerView?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            allEventRecyclerView?.adapter = HorizontalEventsAdapter(items, gameViewModel)
        })


        val allVouchersRecyclerView = view.findViewById<RecyclerView>(R.id.voucher)
        allVouchersRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        val voucherTest = listOf(
            Voucher("0", "3","https://image.tienphong.vn/600x315/Uploaded/2024/pcgycivo/2023_09_22/thumb-anh-bia-3156.png", "Shopee Food", "Voucher Giảm Giá", "Giảm 10k trên giá món khao bạn đến 70k tổng đơn hàng", "01/01/2000", "ONLINE"),
            Voucher("1", "2","https://quanlydoitac.viettel.vn/files/qldt/public/voucher/image/2024/1/25/ed8daadf-0504-446d-b7c8-8074f3df13a4.jpg", "KFC", "Voucher Khuyến Mại","Combo Mùa hè sôi động chỉ 80k", "01/01/2000", "ONLINE"),
            Voucher("2", "6","https://magiamgiadienmayxanh.com/wp-content/uploads/2022/01/Phieu-mua-hang-Dien-May-Xanh-voucher-magiamgiadienmayxanh.jpg", "Điện máy xanh", "Voucher Khuyến Mại","Phiếu mua hàng trị giá 500k", "01/01/2000", "ONLINE")
        )

        allVouchersRecyclerView.adapter = HorizontalVouchersAdapter(voucherTest)

        return view
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomePage.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomePage().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}