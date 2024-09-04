package com.example.vou_mobile.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vou_mobile.R
import com.example.vou_mobile.model.Brand
import com.squareup.picasso.Picasso

class HorizontalBrandsAdapter(private val itemList: List<Brand>): RecyclerView.Adapter<HorizontalBrandsAdapter.MyViewHolder>(){
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val brandAvtUrl: ImageView = itemView.findViewById(R.id.brand_picture)
        val brandName: TextView = itemView.findViewById(R.id.brand_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.all_brands_layout, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = itemList[position]
        Picasso.get()
            .load(item.avatar)
            .into(holder.brandAvtUrl)
        holder.brandName.text = item.brand_name
    }

    override fun getItemCount() = itemList.size
}