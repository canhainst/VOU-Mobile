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
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.vou_mobile.R
import com.example.vou_mobile.fragment.ItemWarehouse
import com.example.vou_mobile.fragment.SendItem
import com.example.vou_mobile.model.Brand
import com.example.vou_mobile.model.Event
import com.example.vou_mobile.model.Item
import com.example.vou_mobile.model.ItemsOfEvent
import com.example.vou_mobile.model.Warehouse
import com.squareup.picasso.Picasso

class VerticalItemWarehouseAdapter(private val itemList: List<ItemsOfEvent>) : RecyclerView.Adapter<VerticalItemWarehouseAdapter.MyViewHolder>() {
    private var listener: OnItemClickListener? = null

    val eventTest = listOf(
        Event("0", "1", "Lắc xì may mắn", "Shopee Food", "https://thanhnien.mediacdn.vn/Uploaded/nthanhluan/2022_03_01/shopee-15-3-sieu-hoi-tieu-dung-4607.jpg", 100, "01/01/2000", "01/02/2000", 0, "Thu thập đủ 5 loại ngọc bằng cách lắc xì để đổi lấy phần thưởng. Tham gia ngay!"),
        Event("1", "2", "Lắc xì may mắn", "KFC", "https://down-vn.img.susercontent.com/file/40d21efdf195faccb7710ae93fb5d0ea", 100, "01/01/2000", "01/02/2000", 0, "Thu thập đủ 5 loại ngọc bằng cách lắc xì để đổi lấy phần thưởng. Tham gia ngay!"),
        Event("2", "3", "Lắc xì may mắn", "Shopee", "https://images.bloggiamgia.vn/full/07-02-2023/Shopee-sale-99-1-1675759490515.png", 100, "01/01/2000", "01/02/2000", 0, "Thu thập đủ 5 loại ngọc bằng cách lắc xì để đổi lấy phần thưởng. Tham gia ngay!"),
    )

    val itemTest = listOf(
        Item("0", "Ngọc Đỏ"),
        Item("1", "Ngọc Xanh"),
        Item("2", "Ngọc Vàng"),
        Item("3", "Ngọc Trắng"),
        Item("4", "Ngọc Cam"),
        Item("5", "Mảnh Tranh"),
        Item("6", "Mảnh Gốm"),
        Item("7", "Mảnh Đá Quý"),
        Item("8", "Mảnh Gương Vỡ"),
        Item("9", "Ngọc Giáp Sắt"),
        Item("10", "Đá Cẩm Thạch"),
        Item("11", "Đá Nhân Tạo"),
        Item("12", "Đá Khoáng Thạch"),
        Item("13", "Đá Huyết Tinh")
    )

    val brandTest = listOf(
        Brand("0", "https://downloadlogomienphi.com/sites/default/files/logos/download-logo-phuclong-mien-phi.jpg", "Phúc Long"),
        Brand("1", "https://play-lh.googleusercontent.com/KBMEAtNbnht-M9jqeJqiFCDqazutWY_OQk7UyfJfcO6QO1PI6EWWm0G6j1D60dgNN12-", "Shopee Food"),
        Brand("2", "https://seeklogo.com/images/K/kfc-logo-A232F2E6D1-seeklogo.com.png", "KFC"),
        Brand("3", "https://logodix.com/logo/2015053.png", "Shopee"),
        Brand("4", "https://upload.wikimedia.org/wikipedia/vi/b/b1/Logo_GSM_xanh_SM.png", "Xanh SM"),
        Brand("5", "https://downloadlogomienphi.com/sites/default/files/logos/download-logo-phuclong-mien-phi.jpg", "Phúc Long"),
        Brand("6", "https://upload.wikimedia.org/wikipedia/vi/b/b1/Logo_GSM_xanh_SM.png", "Xanh SM")
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

        holder.brandName.text = brand!!.brandName
        holder.eventTime.text = event!!.startTime + " - " + event.endTime
        holder.eventName.text = event.eventName
        Picasso.get()
            .load(brand.brandAvtUrl)
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

        dialogView.findViewById<TextView>(R.id.brand_name).text = brand!!.brandName
        dialogView.findViewById<TextView>(R.id.script).text = event!!.eventName
        Picasso.get()
            .load(brand.brandAvtUrl)
            .into(dialogView.findViewById<ImageView>(R.id.brandAvt))
        Picasso.get()
            .load(event.eventPictureUrl)
            .into(dialogView.findViewById<ImageView>(R.id.picture))
        dialogView.findViewById<TextView>(R.id.Time).text = "Expiration: ${event.startTime} - ${event.endTime}"
        dialogView.findViewById<TextView>(R.id.script2).text = event.eventName
        dialogView.findViewById<TextView>(R.id.detail).text = getItemsListByEventID(event.id!!)
        dialogView.findViewById<Button>(R.id.btnDirection).text = "Send Item"
        dialogView.findViewById<Button>(R.id.btnDirection).setOnClickListener {
            dialogBuilder.dismiss()

            if (context is FragmentActivity) {
                val fragmentManager = context.supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.setCustomAnimations(
                    R.anim.enter_from_right,
                    R.anim.exit_to_left,
                    R.anim.enter_from_left,
                    R.anim.exit_to_right
                )
                fragmentTransaction.replace(R.id.frameLayout, SendItem())
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
            }
        }

        dialogView.findViewById<Button>(R.id.btnDirection2).text = "Done"
        dialogView.findViewById<Button>(R.id.btnDirection2).setOnClickListener {
            dialogBuilder.dismiss()
        }

        dialogBuilder.show()
    }

    private fun getItemsListByEventID(id: String): String {
        val itemsList = itemList.find { it.eventID == id }!!.items
        var list = "Items List:\n"
        for (item in itemsList) {
            list += "${itemTest.find { it.ID == item.itemID }!!.itemName}: ${item.quantity} pcs\n"
        }
        return list
    }

    private fun getBrandByEventID(id: String): Brand? {
        val event = eventTest.find { it.id == id }
        return brandTest.find { it.id == event!!.brandID }
    }

    private fun getEventByID(id: String): Event? {
        return eventTest.find { it.id == id }
    }
}
