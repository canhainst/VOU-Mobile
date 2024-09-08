package com.example.vou_mobile.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vou_mobile.R
import com.example.vou_mobile.model.PaymentMethod
import com.squareup.picasso.Picasso

class HorizontalPaymentMethodsAdapter(
    private var itemList: List<PaymentMethod>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<HorizontalPaymentMethodsAdapter.MyViewHolder>() {

    private var selectedPosition = RecyclerView.NO_POSITION

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var paymentMethodImg: ImageView = itemView.findViewById(R.id.paymentMethodImg)
        var paymentMethod: TextView = itemView.findViewById(R.id.paymentMethod)
        var itemLayout: View = itemView.findViewById(R.id.itemLayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.all_payment_method_layout, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount() = itemList.size

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Picasso.get()
            .load(itemList[position].methodImg)
            .into(holder.paymentMethodImg)
        holder.paymentMethod.text = itemList[position].method

        val backgroundResource = if (holder.adapterPosition == selectedPosition) {
            R.drawable.selected_background
        } else {
            R.drawable.unselected_background
        }

        holder.itemLayout.setBackgroundResource(backgroundResource)

        // Set click listener
        holder.itemView.setOnClickListener {
            val previousPosition = selectedPosition
            selectedPosition = holder.adapterPosition
            notifyItemChanged(previousPosition) // Update previously selected item
            notifyItemChanged(selectedPosition) // Update currently selected item
            listener.onItemClick(selectedPosition) // Pass updated position to listener
        }
    }
}