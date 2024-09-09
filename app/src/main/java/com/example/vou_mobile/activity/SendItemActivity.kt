package com.example.vou_mobile.activity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.vou_mobile.R
import com.example.vou_mobile.adapter.GridItemsAdapter
import com.example.vou_mobile.model.Brand
import com.example.vou_mobile.model.Event
import com.example.vou_mobile.model.Item
import com.example.vou_mobile.model.ItemSent
import com.example.vou_mobile.model.SendGift
import com.example.vou_mobile.model.SendItemsResponse
import com.example.vou_mobile.services.api.BrandService
import com.example.vou_mobile.services.api.EventService
import com.example.vou_mobile.services.api.RetrofitClient
import com.example.vou_mobile.services.api.WarehouseService
import com.example.vou_mobile.viewModel.EventViewModelProviderSingleton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SendItemActivity : AppCompatActivity() {
    private val eventViewModel = EventViewModelProviderSingleton.getEventViewModel()
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var uuid: String
    private var selectedItems: MutableList<Item> = mutableListOf()
    private var recipientUsername: String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_item)

        val event = eventViewModel.curEvent.value
        val brandService = RetrofitClient.instance.create(BrandService::class.java)
        val callBrand = brandService.getBrandByUuid(event!!.id_brand)

        callBrand.enqueue(object : Callback<Brand> {
            override fun onResponse(call: Call<Brand>, response: Response<Brand>) {
                if (response.isSuccessful) {
                    val brand = response.body()
                    if (brand != null) {
                        findViewById<TextView>(R.id.brand_name).text = brand.brand_name
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
        val callEvent = eventService.getEventByID(event.id)

        callEvent.enqueue(object : Callback<Event> {
            override fun onResponse(call: Call<Event>, response: Response<Event>) {
                if (response.isSuccessful) {
                    val event = response.body()
                    if (event != null) {
                        findViewById<TextView>(R.id.event_name).text = event.name
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

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 3)

        sharedPreferences = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        uuid = sharedPreferences.getString("uuid", "")!!

        val warehouseService = RetrofitClient.instance.create(WarehouseService::class.java)
        val callItems = warehouseService.getItemsOfEventByUser(uuid, event.id)

        callItems.enqueue(object : Callback<List<Item>> {
            override fun onResponse(call: Call<List<Item>>, response: Response<List<Item>>) {
                if (response.isSuccessful) {
                    val items = response.body()
                    if (items != null) {
                        recyclerView.adapter = GridItemsAdapter(items, selectedItems)
                    }
                } else {
                    println("Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Item>>, t: Throwable) {
                println("Failed: ${t.message}")
            }
        })

        findViewById<Button>(R.id.sendBtn).setOnClickListener {
            recipientUsername = findViewById<EditText>(R.id.inputID).text.toString()
            showCustomDialog(this)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showCustomDialog(context: Context) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_send_items_result, null)
        val dialogBuilder = AlertDialog.Builder(context)
            .setView(dialogView)
            .create()
        val animationView = dialogView.findViewById<LottieAnimationView>(R.id.aniDialog)

        if (selectedItems.isEmpty()){
            dialogView.findViewById<TextView>(R.id.sendingResult).text = "Something went wrong?!"
            dialogView.findViewById<TextView>(R.id.sendingDescription).text = "You can't send a gift with an empty basket like that :(\nTry choosing a gift <3\n(」° ロ °)」"
            animationView.setAnimation(R.raw.sad)
            animationView.playAnimation()
            dialogBuilder.show()
        } else if (recipientUsername.isNullOrBlank()){
            dialogView.findViewById<TextView>(R.id.sendingResult).text = "Something went wrong?!"
            dialogView.findViewById<TextView>(R.id.sendingDescription).text = "Hey hey, you forgot to fill in the recipient's name:D\n(￢_￢)"
            animationView.setAnimation(R.raw.sad)
            animationView.playAnimation()
            dialogBuilder.show()
        } else {
            val gift = createAGift(uuid!!, recipientUsername!!, selectedItems)
            val warehouseService = RetrofitClient.instance.create(WarehouseService::class.java)
            val call = warehouseService.sendItems(gift)

            call.enqueue(object : Callback<SendItemsResponse> {
                override fun onResponse(call: Call<SendItemsResponse>, response: Response<SendItemsResponse>) {
                    if (response.isSuccessful) {
                        animationView.setAnimation(R.raw.send_successfully)
                        dialogView.findViewById<TextView>(R.id.sendingResult).text = "Yay, delivery successfully <3"
                        dialogView.findViewById<TextView>(R.id.sendingDescription).text = "Looks like your gift has been delivered, the couriers are really hard working.\nSurely the recipient will feel very happy :3\n(つ ✧ω✧) つ"
                        animationView.playAnimation()

                        dialogBuilder.setOnCancelListener {
                            finish()
                        }
                        dialogBuilder.show()
                    } else {
                        dialogView.findViewById<TextView>(R.id.sendingResult).text = "Something went wrong?!"
                        dialogView.findViewById<TextView>(R.id.sendingDescription).text = "API error: ${response.code()}\n╮ (￣ ～ ￣) ╭"
                        animationView.setAnimation(R.raw.sad)
                        animationView.playAnimation()
                        dialogBuilder.show()
                    }
                }
                override fun onFailure(call: Call<SendItemsResponse>, t: Throwable) {
                    dialogView.findViewById<TextView>(R.id.sendingResult).text = "Something went wrong?!"
                    dialogView.findViewById<TextView>(R.id.sendingDescription).text = "Request failed: ${t.message}\n╮ (￣ ～ ￣) ╭"
                    animationView.setAnimation(R.raw.sad)
                    animationView.playAnimation()
                    dialogBuilder.show()
                }
            })
        }
    }

    fun createAGift(
        uuid: String,
        recipientUsername: String,
        selectedItems: List<Item>
    ): SendGift {
        val itemsSentList = selectedItems.map { item ->
            ItemSent(
                id_item = item.id_item,
                quantity = 1
            )
        }

        return SendGift(
            id_giver = uuid,
            user_name = recipientUsername,
            itemsList = itemsSentList
        )
    }

}