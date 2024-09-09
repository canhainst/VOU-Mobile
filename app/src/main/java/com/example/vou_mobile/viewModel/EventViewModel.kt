package com.example.vou_mobile.viewModel

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vou_mobile.activity.RequestPermissionsActivity
import com.example.vou_mobile.services.broadcastReceiver.cancelEventReminder
import com.example.vou_mobile.services.broadcastReceiver.scheduleEventReminder
import com.example.vou_mobile.helper.Helper
import com.example.vou_mobile.model.Event
import com.example.vou_mobile.model.FavEvents
import com.example.vou_mobile.services.api.EventService
import com.example.vou_mobile.services.api.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Locale

object EventViewModelProviderSingleton {
    private var eventViewModel: EventViewModel? = null

    fun getEventViewModel(): EventViewModel {
        if (eventViewModel == null) {
            eventViewModel = EventViewModel()
        }
        return eventViewModel!!
    }
}

class EventViewModel() : ViewModel() {
    private val _favoriteEvents = MutableLiveData<List<Event>>()
    val favoriteEvents : LiveData<List<Event>> = _favoriteEvents
    private val _events = MutableLiveData<List<Event>>()
    val events : LiveData<List<Event>> = _events
    private val _curEvent = MutableLiveData<Event>()
    val curEvent: LiveData<Event> = _curEvent

    fun loadAllEvents(){
        _events.value = emptyList()
        val api = RetrofitClient.instance.create(EventService::class.java)
        api.getOngoingEvents().enqueue(object : Callback<List<Event>> {
            override fun onResponse(call: Call<List<Event>>, response: Response<List<Event>>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    _events.value = response.body()!!
                    Log.d("EventViewModel", response.body().toString())
                } else{
                    _events.value = emptyList()
                    Log.e("EventViewModel", "Empty or failed response.")
                }
            }

            override fun onFailure(call: Call<List<Event>>, t: Throwable) {
                _events.value = emptyList()
                Log.e("EventViewModel", "Failed to fetch events: ${t.message}")
            }
        })
    }
    fun loadFavoriteEvents(userId: String, context: Context) {
        _favoriteEvents.value = emptyList()
        // Tạo Retrofit instance và service
        val api = RetrofitClient.instance.create(EventService::class.java)

        // Gọi API để lấy danh sách sự kiện yêu thích
        api.getAllFavorite(userId).enqueue(object : Callback<List<Event>> {
            override fun onResponse(call: Call<List<Event>>, response: Response<List<Event>>) {
                if (response.isSuccessful && response.body() != null) {

                    _favoriteEvents.value = response.body()!!
                    Log.d("EventViewModel", response.body().toString())

                    // Kiểm tra quyền trước khi lên lịch thông báo
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                        ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
                    ) {
                        // Nếu chưa có quyền, yêu cầu quyền từ Activity
                        if (context is Activity) {
                            ActivityCompat.requestPermissions(
                                context,
                                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                                RequestPermissionsActivity.REQUEST_CODE_POST_NOTIFICATIONS
                            )
                        } else {
                            Log.e("EventViewModel", "Context is not an Activity, cannot request permissions.")
                        }
                    } else {
                        // Nếu quyền đã được cấp, tiếp tục lên lịch thông báo cho các sự kiện
                        response.body()!!.forEachIndexed { index, event ->
                            val eventTimeMillis = parseEventTime(Helper.fixTime(event.start_time!!))

                            // Check if the event time is in the future
                            if (eventTimeMillis > System.currentTimeMillis()) {
                                // Schedule notification only if not already shown
                                scheduleEventReminder(context, event.id, event.name!!, eventTimeMillis)
                            }
                        }
                    }
                } else{
                    _favoriteEvents.value = emptyList()
                    Log.e("EventViewModel", "Empty or failed response.")
                }
            }

            override fun onFailure(call: Call<List<Event>>, t: Throwable) {
                Log.d("API", "Error: ${t.message}")
                _favoriteEvents.value = emptyList()
            }
        })
    }

    fun removeFavoriteEvent(context: Context, event: Event, userID: String,  callback: (Boolean) -> Unit) {
        val currentList = _favoriteEvents.value?.toMutableList() ?: mutableListOf()
        currentList.remove(event)

        //update postgre
        val api = RetrofitClient.instance.create(EventService::class.java)
        api.deleteFavorite(event.id, userID).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    _favoriteEvents.value = currentList
                    cancelEventReminder(context, event.id)
                    callback(true)
                } else {
                    Log.d("API", "Failed to delete favorite event.")
                    callback(false)
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.d("API", "Error: ${t.message}")
                callback(false)
            }
        })

    }

    fun addFavoriteEvent(context: Context, FavEvent: FavEvents, callback: (Boolean) -> Unit) {
        //update postgre
        val api = RetrofitClient.instance.create(EventService::class.java)
        api.addFavorite(FavEvent).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    loadFavoriteEvents(FavEvent.id_user, context)
                    callback(true)
                } else {
                    Log.d("Err addFavoriteEvent", "Error: ${response.errorBody()?.string()}")
                    callback(false)
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                t.printStackTrace()
                callback(false)
            }
        })
    }

    fun chooseEvent(event: Event){
        _curEvent.value = event
    }
}

fun parseEventTime(eventStartTime: String): Long {
    // Định dạng ngày giờ của chuỗi đầu vào
    val format = SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault())
    // Phân tích chuỗi ngày giờ thành đối tượng Date
    val date = format.parse(eventStartTime)
    // Trả về thời gian tính bằng milliseconds
    return date?.time ?: 0
}

private fun isEventNotified(context: Context, eventId: String): Boolean {
    val prefs = context.getSharedPreferences("event_notifications", Context.MODE_PRIVATE)
    return prefs.getBoolean(eventId, false)
}

private fun markEventAsNotified(context: Context, eventId: String) {
    val prefs = context.getSharedPreferences("event_notifications", Context.MODE_PRIVATE)
    prefs.edit().putBoolean(eventId, true).apply()
}
