package com.example.vou_mobile.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.vou_mobile.R
import com.example.vou_mobile.adapter.ShowGridItemsAdapter
import com.example.vou_mobile.databinding.ActivityGetVoucherBinding
import com.example.vou_mobile.model.Brand
import com.example.vou_mobile.model.Item
import com.example.vou_mobile.services.api.BrandService
import com.example.vou_mobile.services.api.EventService
import com.example.vou_mobile.services.api.RedeemGifResponse
import com.example.vou_mobile.services.api.RetrofitClient
import com.example.vou_mobile.services.api.WarehouseService
import com.example.vou_mobile.viewModel.EventViewModelProviderSingleton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetVoucherActivity : AppCompatActivity() {
    private lateinit var binding : ActivityGetVoucherBinding
    private val eventViewModel = EventViewModelProviderSingleton.getEventViewModel()
    private lateinit var userId: String
    private lateinit var eventId: String

    private lateinit var sharedPreferences: SharedPreferences
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGetVoucherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        userId = sharedPreferences.getString("uuid", null)!!
        eventId = eventViewModel.curEvent.value!!.id

        val recyclerView = findViewById<RecyclerView>(R.id.rcv_itemList)
        recyclerView.layoutManager = GridLayoutManager(this, 3)

        val warehouseService = RetrofitClient.instance.create(WarehouseService::class.java)
        val callItems = warehouseService.getItemsOfEventByUser(userId, eventId)

        callItems.enqueue(object : Callback<List<Item>> {
            override fun onResponse(call: Call<List<Item>>, response: Response<List<Item>>) {
                if (response.isSuccessful) {
                    val items = response.body()
                    if (items != null) {
                        recyclerView.adapter = ShowGridItemsAdapter(items)
                    }
                } else {
                    println("Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Item>>, t: Throwable) {
                println("Failed: ${t.message}")
            }
        })

        binding.getVoucherBtn.text = "Get voucher"

        binding.getVoucherBtn.setOnClickListener {
            val api = RetrofitClient.instance.create(EventService::class.java)
            api.redeemGift(userId, eventId).enqueue(
                object: Callback<RedeemGifResponse>{
                    override fun onResponse(
                        call: Call<RedeemGifResponse>,
                        response: Response<RedeemGifResponse>
                    ) {
                        val res = response.body()
                        println("${res}")

                        if (res != null){
                            showCustomDialog(res)
                        }
                    }

                    override fun onFailure(call: Call<RedeemGifResponse>, t: Throwable) {
                        println("${t.message}")
                    }

                }
            )
        }

        binding.btnBack.setOnClickListener {
            finish()
        }



    }

    @SuppressLint("SetTextI18n")
    private fun showCustomDialog(res: RedeemGifResponse) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_send_items_result, null)
        val dialogBuilder = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()
        val animationView = dialogView.findViewById<LottieAnimationView>(R.id.aniDialog)

        if (res.code == 200){
            val brandService = RetrofitClient.instance.create(BrandService::class.java)
            val callBrand = brandService.getBrandByUuid(res.item.id_brand)

            callBrand.enqueue(object : Callback<Brand> {
                override fun onResponse(call: Call<Brand>, response: Response<Brand>) {
                    if (response.isSuccessful) {
                        val brand = response.body()
                        if (brand != null) {
                            dialogView.findViewById<TextView>(R.id.sendingResult).text = "You got a voucher from ${brand.brand_name}"
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
            animationView.setAnimation(R.raw.get_voucher_success)
            dialogView.findViewById<TextView>(R.id.sendingDescription).text = res.item.description
            animationView.playAnimation()
            dialogBuilder.show()
        } else {
            dialogView.findViewById<TextView>(R.id.sendingResult).text = "Something went wrong?!"
            dialogView.findViewById<TextView>(R.id.sendingDescription).text = "${res.message}\n╮ (￣ ～ ￣) ╭"
            animationView.setAnimation(R.raw.sad)
            animationView.playAnimation()
            dialogBuilder.show()
        }
    }
}