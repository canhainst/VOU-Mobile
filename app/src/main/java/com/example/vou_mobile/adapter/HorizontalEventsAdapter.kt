package com.example.vou_mobile.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.vou_mobile.R
import com.example.vou_mobile.helper.Helper
import com.example.vou_mobile.model.Event
import com.example.vou_mobile.model.FavEvents
import com.example.vou_mobile.services.EventService
import com.example.vou_mobile.services.FavoriteResponse
import com.example.vou_mobile.services.RetrofitClient
import com.example.vou_mobile.viewModel.EventViewModelProviderSingleton
import com.example.vou_mobile.viewModel.GameViewModel
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar
import java.util.Date

class HorizontalEventsAdapter(private var itemList: List<Event>, private val gameViewModel: GameViewModel): RecyclerView.Adapter<HorizontalEventsAdapter.MyViewHolder>(){
    private lateinit var context: Context
    private var listener: OnItemClickListener? = null
    private val viewModel =  EventViewModelProviderSingleton.getEventViewModel()

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        context = parent.context
        val view = LayoutInflater.from(parent.context).inflate(R.layout.all_event_layout, parent, false)
        return MyViewHolder(view)
    }

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val eventPicture: ImageView = itemView.findViewById(R.id.event)
        val markEvent: ImageButton = itemView.findViewById(R.id.markEvent)
    }

    override fun getItemCount() = itemList.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Log.d("EventViewModelAdapter", itemList.toString())
        val item = itemList[position]
        Picasso.get()
            .load(item.image)
            .into(holder.eventPicture)

        holder.itemView.setOnClickListener{
            listener?.onItemClick(position)
            showCustomDialog(holder.itemView.context, position)
        }

        var isFavorite = false
        if (viewModel.favoriteEvents.value != null && viewModel.favoriteEvents.value!!.isNotEmpty()){
            isFavorite = viewModel.favoriteEvents.value!!.any { it.id == itemList[position].id}
        }
        when {
            isFavorite -> {
                holder.markEvent.setImageResource(R.drawable.baseline_notifications_active_24)
                holder.markEvent.setOnClickListener {
                    viewModel.removeFavoriteEvent(itemList[position], "01724dc6-775a-4f52-95fd-245c615f2e77"){
                        if (it){
                            holder.markEvent.setImageResource(R.drawable.baseline_notifications_none_24)
                            Toast.makeText(context, "Event marking has been removed", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            else -> {
                holder.markEvent.setImageResource(R.drawable.baseline_notifications_none_24)
                holder.markEvent.setOnClickListener {
                    val favEvent = FavEvents(null, itemList[position].id, "01724dc6-775a-4f52-95fd-245c615f2e77", null)
                    viewModel.addFavoriteEvent(favEvent){
                        if (it){
                            holder.markEvent.setImageResource(R.drawable.baseline_notifications_active_24)
                            Toast.makeText(context, "Event will be notified when it's about to occur", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("InflateParams", "SetTextI18n")
    private fun showCustomDialog(context: Context, position: Int) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.detail_dialog, null)
        val dialogBuilder = AlertDialog.Builder(context)
            .setView(dialogView)
            .create()

        dialogView.findViewById<TextView>(R.id.brand_name).text = itemList[position].id_brand
        dialogView.findViewById<TextView>(R.id.script).text = itemList[position].name
        Picasso.get()
            .load(itemList[position].image)
            .into(dialogView.findViewById<ImageView>(R.id.picture))
        dialogView.findViewById<TextView>(R.id.Time).text = Helper.getTimeRangeString(itemList[position])
        dialogView.findViewById<TextView>(R.id.script2).text = itemList[position].name
        dialogView.findViewById<Button>(R.id.btnDirection).text = "Play"
        dialogView.findViewById<Button>(R.id.btnBack).text = "Back"
        dialogView.findViewById<Button>(R.id.btnBack).setOnClickListener {
            dialogBuilder.dismiss()
        }

        //lay ngay hien tai
        val curTime = Helper.dateToString(Date())
        val calendar = Calendar.getInstance()
        calendar.time = Helper.stringToDate(itemList[position].start_time!!)!!
        calendar.add(Calendar.MINUTE, 10)
        val time2 = Helper.dateToString(calendar.time)

        if (itemList[position].type == "Lắc xì" && Helper.isTimeAfter(curTime, itemList[position].end_time)) {
            dialogView.findViewById<Button>(R.id.btnDirection).visibility = View.GONE
            Toast.makeText(context, "The event has ended!", Toast.LENGTH_SHORT).show()
        } else if (itemList[position].type == "Quiz" && Helper.isTimeAfter(curTime, time2)){
            dialogView.findViewById<Button>(R.id.btnDirection).visibility = View.GONE
            Toast.makeText(context, "The event has started!", Toast.LENGTH_SHORT).show()
        }

        dialogView.findViewById<Button>(R.id.btnDirection).setOnClickListener {
            if (itemList[position].type == "Lắc xì"  && Helper.isTimeBefore(curTime, itemList[position].start_time)){
                Toast.makeText(context, "The event has not started yet!", Toast.LENGTH_SHORT).show()
            }  else{
                EventViewModelProviderSingleton.getEventViewModel().chooseEvent(itemList[position])
                gameViewModel.setGame(itemList[position].type!!, context)
                gameViewModel.startGame()
            }
        }


        dialogBuilder.show()
    }
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newEvents: List<Event>) {
        itemList = newEvents
        notifyDataSetChanged() // Thông báo cho adapter rằng dữ liệu đã thay đổi
    }
}