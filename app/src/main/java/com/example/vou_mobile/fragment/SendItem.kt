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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vou_mobile.R
import com.example.vou_mobile.adapter.GridItemsAdapter
import com.example.vou_mobile.model.Brand
import com.example.vou_mobile.model.Event
import com.example.vou_mobile.model.Item
import com.example.vou_mobile.services.BrandService
import com.example.vou_mobile.services.EventService
import com.example.vou_mobile.services.RetrofitClient
import com.example.vou_mobile.services.WarehouseService
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SendItem.newInstance] factory method to
 * create an instance of this fragment.
 */
class SendItem : Fragment() {
    // TODO: Rename and change types of parameters
    private var brandId: String? = null
    private var eventId: String? = null
    private lateinit var sharedPreferences: SharedPreferences
    private var uuid: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            brandId = it.getString(ARG_PARAM1)
            eventId = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_send_item, container, false)
        val brandService = RetrofitClient.instance.create(BrandService::class.java)
        val callBrand = brandService.getBrandByUuid(brandId!!)

        callBrand.enqueue(object : Callback<Brand> {
            override fun onResponse(call: Call<Brand>, response: Response<Brand>) {
                if (response.isSuccessful) {
                    val brand = response.body()
                    if (brand != null) {
                        view.findViewById<TextView>(R.id.brand_name).text = brand.brand_name
                    } else {
                        println("Error: ${response.code()}")
                    }
                } else {
                    println("Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Brand>, t: Throwable) {
                println("Failed: ${t.message}")
            }
        })

        val eventService = RetrofitClient.instance.create(EventService::class.java)
        val callEvent = eventService.getEventByID(eventId!!)

        callEvent.enqueue(object : Callback<Event> {
            override fun onResponse(call: Call<Event>, response: Response<Event>) {
                if (response.isSuccessful) {
                    val event = response.body()
                    if (event != null) {
                        view.findViewById<TextView>(R.id.event_name).text = event.name
                    } else {
                        println("Error: ${response.code()}")
                    }
                } else {
                    println("Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<Event>, t: Throwable) {
                println("Failed: ${t.message}")
            }
        })

        // Khởi tạo RecyclerView
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)

        // Cài đặt GridLayoutManager với số cột là 3
        val layoutManager = GridLayoutManager(requireContext(), 3)
        recyclerView.layoutManager = layoutManager

        sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        uuid = sharedPreferences.getString("uuid", null)

        val warehouseService = RetrofitClient.instance.create(WarehouseService::class.java)
        val callItems = warehouseService.getItemsOfEventByUser(uuid!!, eventId!!)
        callItems.enqueue(object : Callback<List<Item>> {
            override fun onResponse(call: Call<List<Item>>, response: Response<List<Item>>) {
                if (response.isSuccessful) {
                    val items = response.body()
                    if (items != null) {
                        // Gán adapter cho RecyclerView
                        recyclerView.adapter = GridItemsAdapter(items)
                    }
                } else {
                    println("Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Item>>, t: Throwable) {
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
         * @return A new instance of fragment SendItem.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SendItem().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}