package com.example.vou_mobile.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
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
import com.example.vou_mobile.model.ItemsOfEvent
import com.squareup.picasso.Picasso

class VerticalItemWarehouseAdapter(private val itemList: List<ItemsOfEvent>) : RecyclerView.Adapter<VerticalItemWarehouseAdapter.MyViewHolder>() {
    private var listener: OnItemClickListener? = null

    val eventTest = listOf(
        Event("0", "1", "Lắc xì may mắn", "https://thanhnien.mediacdn.vn/Uploaded/nthanhluan/2022_03_01/shopee-15-3-sieu-hoi-tieu-dung-4607.jpg", "01/01/2000", "01/02/2000", null, "Lắc xì", null),
        Event("0", "1", "Lắc xì may mắn", "https://thanhnien.mediacdn.vn/Uploaded/nthanhluan/2022_03_01/shopee-15-3-sieu-hoi-tieu-dung-4607.jpg", "01/01/2000", "01/02/2000", null, "Lắc xì", null),
        Event("0", "1", "Lắc xì may mắn", "https://thanhnien.mediacdn.vn/Uploaded/nthanhluan/2022_03_01/shopee-15-3-sieu-hoi-tieu-dung-4607.jpg", "01/01/2000", "01/02/2000", null, "Lắc xì", null),
    )

    val itemTest = listOf(
        Item("0", "33274103-48ec-4167-b450-112a018e6a6d", "Item 2", "https://static.wikia.nocookie.net/party-animals/images/2/2e/HarryAvatar.png/revision/latest?cb=20230127124432"),
        Item("1", "33274103-48ec-4167-b450-112a018e6a6d", "Item 2", "https://static.wikia.nocookie.net/party-animals/images/2/2e/HarryAvatar.png/revision/latest?cb=20230127124432"),
        Item("2", "33274103-48ec-4167-b450-112a018e6a6d", "Item 2", "https://static.wikia.nocookie.net/party-animals/images/2/2e/HarryAvatar.png/revision/latest?cb=20230127124432"),
    )

    val brandTest = listOf(
        Brand("0", "https://downloadlogomienphi.com/sites/default/files/logos/download-logo-phuclong-mien-phi.jpg", "Phúc Long", "", "e", "0", "a", "", "Active", ""),
        Brand("1", "https://play-lh.googleusercontent.com/KBMEAtNbnht-M9jqeJqiFCDqazutWY_OQk7UyfJfcO6QO1PI6EWWm0G6j1D60dgNN12-", "Shopee Food","", "e", "0", "a", "", "Active", ""),
        Brand("2", "https://seeklogo.com/images/K/kfc-logo-A232F2E6D1-seeklogo.com.png", "KFC", "", "e", "0", "a", "", "Active", ""),
        Brand("3", "https://logodix.com/logo/2015053.png", "Shopee", "", "e", "0", "a", "", "Active", ""),
        Brand("4", "https://upload.wikimedia.org/wikipedia/vi/b/b1/Logo_GSM_xanh_SM.png", "Xanh SM", "", "e", "0", "a", "", "Active", ""),
        Brand("5", "https://downloadlogomienphi.com/sites/default/files/logos/download-logo-phuclong-mien-phi.jpg", "Phúc Long", "", "e", "0", "a", "", "Active", ""),
        Brand("6", "https://upload.wikimedia.org/wikipedia/vi/b/b1/Logo_GSM_xanh_SM.png", "Xanh SM", "", "e", "0", "a", "", "Active", "")
    )

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
        val eventID = itemList[position].eventID
        val event = getEventByID(eventID)
        val brand = getBrandByEventID(eventID)

        holder.brandName.text = brand!!.brand_name
        holder.eventTime.text = event!!.start_time + " - " + event.end_time
        holder.eventName.text = event.name
        Picasso.get()
            .load(brand.avatar)
            .into(holder.brandImg)

        holder.itemView.setOnClickListener{
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

        val eventID = itemList[position].eventID
        val event = getEventByID(eventID)
        val brand = getBrandByEventID(eventID)

        dialogView.findViewById<TextView>(R.id.brand_name).text = brand!!.brand_name
        dialogView.findViewById<TextView>(R.id.script).text = event!!.name
        Picasso.get()
            .load(brand.avatar)
            .into(dialogView.findViewById<ImageView>(R.id.brandAvt))
        Picasso.get()
            .load(event.image)
            .into(dialogView.findViewById<ImageView>(R.id.picture))
        dialogView.findViewById<TextView>(R.id.Time).text = "Expiration: ${event.start_time} - ${event.end_time}"
        dialogView.findViewById<TextView>(R.id.script2).text = event.name
        dialogView.findViewById<TextView>(R.id.detail).text = getItemsListByEventID(event.id!!)

        dialogView.findViewById<Button>(R.id.btnDirection).text = "Send Item"
        dialogView.findViewById<Button>(R.id.btnDirection).setOnClickListener {
            dialogBuilder.dismiss()

            if (context is FragmentActivity) {
                val mainActivity = context as HomePageActivity
                mainActivity.replaceFragment(SendItem())
            }
        }

        dialogView.findViewById<Button>(R.id.btnBack).text = "Done"
        dialogView.findViewById<Button>(R.id.btnBack).setOnClickListener {
            dialogBuilder.dismiss()
        }

        dialogBuilder.show()
    }

    private fun getItemsListByEventID(id: String): String {
        val itemsList = itemList.find { it.eventID == id }!!.items
        var list = "Items List:\n"
        for (item in itemsList) {
            list += "${itemTest.find { it.id == item.itemID }!!.name}: ${item.quantity} pcs\n"
        }
        return list
    }

    private fun getBrandByEventID(id: String): Brand? {
        val event = eventTest.find { it.id == id }
        return brandTest.find { it.id == event!!.id_brand }
    }

    private fun getEventByID(id: String): Event? {
        return eventTest.find { it.id == id }
    }
}
