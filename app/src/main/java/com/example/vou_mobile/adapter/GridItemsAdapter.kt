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

class GridItemsAdapter(
    private val itemList: List<Item>,
    private val selectedItems: MutableList<Item>
) : RecyclerView.Adapter<GridItemsAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemImg: ImageView = itemView.findViewById(R.id.itemImg)
        val itemName: TextView = itemView.findViewById(R.id.itemName)
        val itemQuantity: TextView = itemView.findViewById(R.id.quantity)
        var itemLayout: View = itemView.findViewById(R.id.itemLayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.all_items_list_layout, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount() = itemList.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = itemList[position]
        Picasso.get()
            .load(currentItem.image)
            .into(holder.itemImg)
        holder.itemName.text = currentItem.name
        holder.itemQuantity.text = "x${currentItem.quantity}"

        // Kiểm tra xem item này đã được chọn hay chưa
        val backgroundResource = if (selectedItems.contains(currentItem)) {
            R.drawable.selected_background
        } else {
            R.drawable.unselected_background
        }

        holder.itemLayout.setBackgroundResource(backgroundResource)

        // Check if quantity is 0 to disable item selection
        if (currentItem.quantity == 0) {
            // Disable item click if quantity is 0
            holder.itemLayout.setBackgroundResource(R.drawable.disabled_background)
            holder.itemView.isClickable = false
        } else {
            // Enable item click if quantity is not 0
            holder.itemView.isClickable = true
            holder.itemView.setOnClickListener {
                if (selectedItems.contains(currentItem)) {
                    selectedItems.remove(currentItem) // Bỏ chọn nếu đã được chọn trước đó
                } else {
                    selectedItems.add(currentItem) // Chọn item nếu chưa được chọn
                }
                notifyItemChanged(position) // Cập nhật giao diện cho item được chọn/bỏ chọn
            }
        }
    }
}
