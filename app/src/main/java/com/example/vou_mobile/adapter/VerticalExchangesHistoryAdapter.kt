package com.example.vou_mobile.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vou_mobile.R
import com.example.vou_mobile.model.GiftExchangesHistory
import com.example.vou_mobile.model.Items

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
        holder.exchangeTime.text = itemList[position].exchangeTime
        holder.receiver.text = itemList[position].receiverID
        holder.eventName.text = itemList[position].eventID
        holder.listItemsSent.text = itemList[position].listItems.toString()
    }

    private fun listItemsSent(items: List<Items>): String {
        var listItems = ""
        for (item in items){
            if (item.quantity != 0){
                listItems += "${item.quantity} ${item.itemID}"
            }
        }
        return listItems
    }
}