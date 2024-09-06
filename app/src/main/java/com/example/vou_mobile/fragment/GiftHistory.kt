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
import com.example.vou_mobile.adapter.GridItemsAdapter
import com.example.vou_mobile.adapter.VerticalExchangesHistoryAdapter
import com.example.vou_mobile.model.GiftExchangesHistory
import com.example.vou_mobile.model.Item
import com.example.vou_mobile.services.RetrofitClient
import com.example.vou_mobile.services.WarehouseService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [GiftHistory.newInstance] factory method to
 * create an instance of this fragment.
 */
class GiftHistory : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
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
        val view = inflater.inflate(R.layout.fragment_gift_history, container, false)

        sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        uuid = sharedPreferences.getString("uuid", null)

        val allExchangesHistoryRecyclerView = view.findViewById<RecyclerView>(R.id.all_exchange_history_recyclerview)
        allExchangesHistoryRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        val warehouseService = RetrofitClient.instance.create(WarehouseService::class.java)
        val callGiftItemsHistory = warehouseService.getGiftItemsHistoryByGiver(uuid!!)
        callGiftItemsHistory.enqueue(object : Callback<List<GiftExchangesHistory>> {
            override fun onResponse(call: Call<List<GiftExchangesHistory>>, response: Response<List<GiftExchangesHistory>>) {
                if (response.isSuccessful) {
                    val giftedItems = response.body()
                    if (giftedItems != null) {
                        allExchangesHistoryRecyclerView.adapter = VerticalExchangesHistoryAdapter(giftedItems)
                    }
                } else {
                    println("Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<GiftExchangesHistory>>, t: Throwable) {
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
         * @return A new instance of fragment GiftHistory.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            GiftHistory().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}