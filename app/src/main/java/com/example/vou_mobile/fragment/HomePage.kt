package com.example.vou_mobile.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
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
import com.example.vou_mobile.adapter.HorizontalBrandsAdapter
import com.example.vou_mobile.adapter.HorizontalEventsAdapter
import com.example.vou_mobile.adapter.HorizontalVouchersAdapter
import com.example.vou_mobile.model.Brand
import com.example.vou_mobile.model.User
import com.example.vou_mobile.model.Voucher
import com.example.vou_mobile.services.BrandService
import com.example.vou_mobile.services.RetrofitClient
import com.example.vou_mobile.services.VoucherService
import com.example.vou_mobile.viewModel.EventViewModelProviderSingleton
import com.example.vou_mobile.viewModel.GameViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create

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
    private lateinit var sharedPreferences: SharedPreferences
    private val gson = Gson()
    private lateinit var currentUser: User
    private var uuid: String? = null
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

        val brandService = RetrofitClient.instance.create(BrandService::class.java)
        val voucherService = RetrofitClient.instance.create(VoucherService::class.java)

        sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        uuid = sharedPreferences.getString("uuid", null)

        val userJson = sharedPreferences.getString("currentUser", null)
        if (userJson != null) {
            val userType = object : TypeToken<User>() {}.type
            currentUser = gson.fromJson(userJson, userType)
        }

        Picasso.get()
            .load(currentUser.avatar)
            .into(view.findViewById<ImageView>(R.id.imageView))
        view.findViewById<TextView>(R.id.username).text = currentUser.full_name

        val allBrandRecyclerView = view.findViewById<RecyclerView>(R.id.brand)
        allBrandRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        // Get all brand active
        val callBrand = brandService.getAllBrand()
        callBrand.enqueue(object : Callback<List<Brand>> {
            override fun onResponse(call: Call<List<Brand>>, response: Response<List<Brand>>) {
                if (response.isSuccessful) {
                    val brands = response.body()
                    if (brands != null) {
                        allBrandRecyclerView.adapter = HorizontalBrandsAdapter(brands)                    }
                } else {
                    println("Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Brand>>, t: Throwable) {
                println("Failed: ${t.message}")
            }
        })

        val allEventRecyclerView = view.findViewById<RecyclerView>(R.id.event)
        viewModel.loadAllEvents()
        viewModel.events.observe(viewLifecycleOwner, Observer { items ->
            allEventRecyclerView?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            allEventRecyclerView?.adapter = HorizontalEventsAdapter(items, gameViewModel)
        })


        val allVouchersRecyclerView = view.findViewById<RecyclerView>(R.id.voucher)
        allVouchersRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        // Get all voucher active
        val callVoucher = voucherService.getAllVoucherByUserUuid(uuid!!)
        callVoucher.enqueue(object : Callback<List<Voucher>> {
            override fun onResponse(call: Call<List<Voucher>>, response: Response<List<Voucher>>) {
                if (response.isSuccessful) {
                    val vouchers = response.body()
                    if (vouchers != null) {
                        allVouchersRecyclerView.adapter = HorizontalVouchersAdapter(vouchers)
                    }
                } else {
                    println("Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Voucher>>, t: Throwable) {
                println("Failed: ${t.message}")
            }
        })

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