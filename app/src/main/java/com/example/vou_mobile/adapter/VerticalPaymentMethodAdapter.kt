package com.example.vou_mobile.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vou_mobile.R
import com.example.vou_mobile.model.PaymentMethod
import com.squareup.picasso.Picasso

class VerticalPaymentMethodAdapter(private var itemList: List<PaymentMethod>): RecyclerView.Adapter<VerticalPaymentMethodAdapter.MyViewHolder>()  {
    private var listener: AdapterView.OnItemClickListener? = null
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
    class MyViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        var paymentMethodImg: ImageView = itemView.findViewById(R.id.paymentMethodImg)
        var paymentMethod: TextView = itemView.findViewById(R.id.paymentMethod)
        var itemLayout: View = itemView.findViewById(R.id.itemLayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.all_payment_method_layout, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount() = itemList.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Picasso.get()
            .load(itemList[position].methodImg)
            .into(holder.paymentMethodImg)

        holder.paymentMethod.text = itemList[position].method
        holder.itemLayout.setBackgroundResource(R.drawable.unselected_background)
        holder.itemLayout.setOnClickListener {
            println(itemList[position].method)
        }
    }
}