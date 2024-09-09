package com.example.vou_mobile.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
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
import com.example.vou_mobile.model.Brand
import com.example.vou_mobile.model.Event
import com.example.vou_mobile.model.FavEvents
import com.example.vou_mobile.services.api.BrandService
import com.example.vou_mobile.services.api.RetrofitClient
import com.example.vou_mobile.viewModel.EventViewModelProviderSingleton
import com.example.vou_mobile.viewModel.GameViewModel
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar
import java.util.Date

class HorizontalEventsAdapter(private var events: List<Event>, private var favEvents: List<Event>, private val gameViewModel: GameViewModel): RecyclerView.Adapter<HorizontalEventsAdapter.MyViewHolder>(){
    private lateinit var context: Context
    private var listener: OnItemClickListener? = null
    private val viewModel =  EventViewModelProviderSingleton.getEventViewModel()
    private lateinit var sharedPreferences: SharedPreferences
    private var uuid: String? = null

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

    override fun getItemCount() = events.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        uuid = sharedPreferences.getString("uuid", "")
        val item = events[position]
        Picasso.get()
            .load(item.image)
            .into(holder.eventPicture)

        holder.itemView.setOnClickListener{
            listener?.onItemClick(position)
            showCustomDialog(holder.itemView.context, events[position])
        }

        var isFavorite = false
        if (favEvents.isNotEmpty()){
            isFavorite = favEvents.any { it.id == events[position].id}
        }
        when {
            isFavorite -> {
                holder.markEvent.setImageResource(R.drawable.baseline_notifications_active_24)
                holder.markEvent.setOnClickListener {
                    viewModel.removeFavoriteEvent(context, events[position], uuid!!){
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
                    val favEvent = FavEvents(null, events[position].id, uuid!!, null)
                    viewModel.addFavoriteEvent(context, favEvent){
                        if (it){
                            holder.markEvent.setImageResource(R.drawable.baseline_notifications_active_24)
                            Toast.makeText(context, "Event will be notified when it's about to occur", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("InflateParams", "SetTextI18n", "CutPasteId")
    private fun showCustomDialog(context: Context, event: Event) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.detail_dialog, null)
        val dialogBuilder = AlertDialog.Builder(context)
            .setView(dialogView)
            .create()

        val brandService = RetrofitClient.instance.create(BrandService::class.java)
        val callBrand = brandService.getBrandByUuid(event.id_brand)
        callBrand.enqueue(object : Callback<Brand> {
            override fun onResponse(call: Call<Brand>, response: Response<Brand>) {
                if (response.isSuccessful) {
                    val brand = response.body()
                    if (brand != null) {
                        dialogView.findViewById<TextView>(R.id.brand_name).text = brand.brand_name
                        Picasso.get()
                            .load(brand.avatar)
                            .into(dialogView.findViewById<ImageView>(R.id.brandAvt))
                    }
                } else {
                    Log.e("Error", "${response.code()}")
                }
            }

            override fun onFailure(call: Call<Brand>, t: Throwable) {
                Log.e("Failed", "${t.message}")
            }
        })

        dialogView.findViewById<TextView>(R.id.script).text = event.name
        Picasso.get()
            .load(event.image)
            .into(dialogView.findViewById<ImageView>(R.id.picture))
        dialogView.findViewById<TextView>(R.id.Time).text = Helper.getTimeRangeString(event)
        dialogView.findViewById<TextView>(R.id.script2).text = event.name
        dialogView.findViewById<Button>(R.id.btnDirection).text = "Play"
        dialogView.findViewById<Button>(R.id.btnBack).text = "Back"
        dialogView.findViewById<Button>(R.id.btnBack).setOnClickListener {
            dialogBuilder.dismiss()
        }

        val eventUpdate = Helper.fixEventTime(event)
        //lay ngay hien tai
        val curTime = Helper.dateToString(Date())

        //lay thoi diem cach thoi gian bat dau Quiz 20p
        val calendar = Calendar.getInstance()
        calendar.time = Helper.stringToDate(eventUpdate.start_time!!)!!
        calendar.add(Calendar.MINUTE, 20)
        val time2 = Helper.dateToString(calendar.time)

        if (event.type?.lowercase() == "lắc xì" && Helper.isTimeAfter(curTime, event.end_time)) {
            dialogView.findViewById<Button>(R.id.btnDirection).visibility = View.GONE
            Toast.makeText(context, "The event has ended!", Toast.LENGTH_SHORT).show()
        } else if (event.type?.lowercase() == "quiz" && Helper.isTimeAfter(curTime, time2)){
            dialogView.findViewById<Button>(R.id.btnDirection).visibility = View.GONE
            Toast.makeText(context, "The event has started!", Toast.LENGTH_SHORT).show()
        }

        dialogView.findViewById<Button>(R.id.btnDirection).setOnClickListener {
            if (event.type?.lowercase() == "lắc xì"  && Helper.isTimeBefore(curTime, event.start_time)){
                Toast.makeText(context, "The event has not started yet!", Toast.LENGTH_SHORT).show()
            }  else{
                viewModel.chooseEvent(eventUpdate)
                gameViewModel.setGame(eventUpdate.type!!)
                gameViewModel.currentGame.value?.startGame(context)
            }
        }


        dialogBuilder.show()
    }
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(type: String, newEvents: List<Event>) {
        if (type == "events"){
            events = newEvents
        } else{
            favEvents = newEvents
        }
        notifyDataSetChanged()
    }
}