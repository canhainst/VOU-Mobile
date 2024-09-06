package com.example.vou_mobile.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vou_mobile.R
import com.example.vou_mobile.model.Item
import com.squareup.picasso.Picasso

class GridItemsAdapter(private val itemList: List<Item>) : RecyclerView.Adapter<GridItemsAdapter.MyViewHolder>() {
    private var listener: HorizontalEventsAdapter.OnItemClickListener? = null
    private var selectedPosition = RecyclerView.NO_POSITION
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemImg = itemView.findViewById<ImageView>(R.id.itemImg)
        val itemName = itemView.findViewById<TextView>(R.id.itemName)
        val itemQuantity = itemView.findViewById<TextView>(R.id.quantity)
        var itemLayout: View = itemView.findViewById(R.id.itemLayout)
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.all_items_list_layout, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount() = itemList.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Picasso.get()
            .load(itemList[position].image)
            .into(holder.itemImg)
        holder.itemName.text = itemList[position].name
        holder.itemQuantity.text = "x${itemList[position].quantity}"

        val backgroundResource = if (holder.adapterPosition == selectedPosition) {
            R.drawable.selected_background
        } else {
            R.drawable.unselected_background
        }

        holder.itemLayout.setBackgroundResource(backgroundResource)

        holder.itemView.setOnClickListener{
            val previousPosition = selectedPosition
            selectedPosition = holder.adapterPosition
            notifyItemChanged(previousPosition) // Update previously selected item
            notifyItemChanged(selectedPosition) // Update currently selected item
            listener?.onItemClick(selectedPosition) // Pass updated position
        }
    }
}