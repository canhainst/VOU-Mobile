package com.example.vou_mobile.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.vou_mobile.R
import com.example.vou_mobile.activity.HomePageActivity
import com.example.vou_mobile.fragment.SendItem
import com.example.vou_mobile.model.Brand
import com.example.vou_mobile.model.Event
import com.example.vou_mobile.model.Item
import com.example.vou_mobile.model.ItemBelong
import com.example.vou_mobile.model.ItemsOfEvent
import com.example.vou_mobile.services.BrandService
import com.example.vou_mobile.services.EventService
import com.example.vou_mobile.services.RetrofitClient
import com.example.vou_mobile.services.WarehouseService
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VerticalItemWarehouseAdapter(private val itemList: List<ItemBelong>) : RecyclerView.Adapter<VerticalItemWarehouseAdapter.MyViewHolder>() {
    private var listener: OnItemClickListener? = null
    private lateinit var sharedPreferences: SharedPreferences
    private var uuid: String? = null

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val brandImg: ImageView = itemView.findViewById(R.id.brandImg)
        val eventTime: TextView = itemView.findViewById(R.id.eventTime)
        val brandName: TextView = itemView.findViewById(R.id.brand_name)
        val eventName: TextView = itemView.findViewById(R.id.event_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.all_item_warehouse_layout, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount() = itemList.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val brandService = RetrofitClient.instance.create(BrandService::class.java)
        val callBrand = brandService.getBrandByUuid(itemList[position].id_brand)

        callBrand.enqueue(object : Callback<Brand> {
            override fun onResponse(call: Call<Brand>, response: Response<Brand>) {
                if (response.isSuccessful) {
                    val brand = response.body()
                    if (brand != null) {
                        holder.brandName.text = brand.brand_name
                        Picasso.get()
                            .load(brand.avatar)
                            .into(holder.brandImg)
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
        val callEvent = eventService.getEventByID(itemList[position].id_event)

        callEvent.enqueue(object : Callback<Event> {
            override fun onResponse(call: Call<Event>, response: Response<Event>) {
                if (response.isSuccessful) {
                    val event = response.body()
                    if (event != null) {
                        holder.eventName.text = event.name
                        holder.eventTime.text = event.start_time + " - " + event.end_time
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

        holder.itemView.setOnClickListener {
            listener?.onItemClick(position)
            showCustomDialog(holder.itemView.context, position)
        }
    }

    @SuppressLint("InflateParams", "SetTextI18n")
    private fun showCustomDialog(context: Context, position: Int) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.detail_dialog, null)
        val dialogBuilder = AlertDialog.Builder(context)
            .setView(dialogView)
            .create()

        val brandService = RetrofitClient.instance.create(BrandService::class.java)
        val callBrand = brandService.getBrandByUuid(itemList[position].id_brand)

        callBrand.enqueue(object : Callback<Brand> {
            override fun onResponse(call: Call<Brand>, response: Response<Brand>) {
                if (response.isSuccessful) {
                    val brand = response.body()
                    if (brand != null) {
                        dialogView.findViewById<TextView>(R.id.brand_name).text = brand.brand_name
                        Picasso.get()
                            .load(brand.avatar)
                            .into(dialogView.findViewById<ImageView>(R.id.brandAvt))
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
        val callEvent = eventService.getEventByID(itemList[position].id_event)

        callEvent.enqueue(object : Callback<Event> {
            override fun onResponse(call: Call<Event>, response: Response<Event>) {
                if (response.isSuccessful) {
                    val event = response.body()
                    if (event != null) {
                        dialogView.findViewById<TextView>(R.id.script).text = event.name
                        Picasso.get()
                            .load(event.image)
                            .into(dialogView.findViewById<ImageView>(R.id.picture))
                        dialogView.findViewById<TextView>(R.id.Time).text = "Expiration: ${event.start_time} - ${event.end_time}"
                        dialogView.findViewById<TextView>(R.id.script2).text = event.name

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

        sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        uuid = sharedPreferences.getString("uuid", null)

        val warehouseService = RetrofitClient.instance.create(WarehouseService::class.java)
        val callItems = warehouseService.getItemsOfEventByUser(uuid!!, itemList[position].id_event)
        callItems.enqueue(object : Callback<List<Item>> {
            override fun onResponse(call: Call<List<Item>>, response: Response<List<Item>>) {
                if (response.isSuccessful) {
                    val items = response.body()
                    if (items != null) {
                        dialogView.findViewById<TextView>(R.id.detail).text = itemsListToString(items)
                    }
                } else {
                    println("Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Item>>, t: Throwable) {
                println("Failed: ${t.message}")
            }
        })

        dialogView.findViewById<Button>(R.id.btnDirection).text = "Send Item"
        dialogView.findViewById<Button>(R.id.btnDirection).setOnClickListener {
            dialogBuilder.dismiss()

            if (context is FragmentActivity) {
                val mainActivity = context as HomePageActivity
                val brandId = itemList[position].id_brand  // Replace with your actual first string
                val eventId = itemList[position].id_event // Replace with your actual second string
                val sendItemFragment = SendItem.newInstance(brandId, eventId)
                mainActivity.replaceFragment(sendItemFragment)
            }
        }

        dialogView.findViewById<Button>(R.id.btnBack).text = "Done"
        dialogView.findViewById<Button>(R.id.btnBack).setOnClickListener {
            dialogBuilder.dismiss()
        }

        dialogBuilder.show()
    }
    private fun itemsListToString(items: List<Item>): String {
        var result = ""
        items.forEach {
            result += "Item: ${it.name}\t\t Quantity: ${it.quantity}\n"
        }
        return result
    }
}
