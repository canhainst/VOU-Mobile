package com.example.vou_mobile.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vou_mobile.R
import com.example.vou_mobile.model.GiftDetail
import com.example.vou_mobile.model.GiftExchangesHistory
import com.example.vou_mobile.model.Item
import com.example.vou_mobile.model.User
import com.example.vou_mobile.services.RetrofitClient
import com.example.vou_mobile.services.UserService
import com.example.vou_mobile.services.WarehouseService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VerticalExchangesHistoryAdapter(private val itemList: List<GiftExchangesHistory>) : RecyclerView.Adapter<VerticalExchangesHistoryAdapter.MyViewHolder>() {
    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val exchangeTime: TextView = itemView.findViewById(R.id.exchangeTime)
        val receiver: TextView = itemView.findViewById(R.id.receiverName)
        val eventName: TextView = itemView.findViewById(R.id.event_name)
        val listItemsSent: TextView = itemView.findViewById(R.id.list_items_sent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.all_exchange_history_layout, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount() = itemList.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.exchangeTime.text = itemList[position].gift_time

        val userService = RetrofitClient.instance.create(UserService::class.java)
        val callUser = userService.getUserByUUID(itemList[position].id_recipient)
        callUser.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful) {
                    val user = response.body()
                    if (user != null) {
                        holder.receiver.text = user.user_name
                    }
                } else {
                    println("Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                println("Failed: ${t.message}")
            }
        })
        holder.eventName.text = "Unknown"
        holder.listItemsSent.text = itemListToString(itemList[position].id_item)
    }
    private fun itemListToString(items: List<GiftDetail>): String {
        var result = ""
        items.forEach {
            result += "x${it.quantity} ${it.name}\n"
        }
        return result
    }
}