package com.example.vou_mobile.services
import com.example.vou_mobile.model.Event
import com.example.vou_mobile.model.FavEvents
import com.example.vou_mobile.model.Item
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

data class PlaythroughResponse(
    val playthrough: Int
)

data class PlayLacXiResponse(
    val code: Int,
    val item: Item?, // Thay đổi kiểu dữ liệu cho phù hợp với phản hồi từ server
    val message: String? = null
)

data class ItemListResponse(
    val items: List<ItemResponse> // Danh sách các mục
)

data class ItemResponse(
    val itemName: String,
    val quantity: Int
)

interface EventService {
    //event
    @GET("event/getEvent/{id}")
    fun getEventByID(@Path("id") id: String): Call<Event>
    @GET("event/getOngoingEvents")
    fun getOngoingEvents(): Call<List<Event>>

    //ShakingGame - lắc xì
    @GET("event/lacxi/getOrCreatePlaythrough/{userId}/{eventId}")
    fun getOrCreatePlaythrough(@Path("userId") userId: String, @Path("eventId") eventId: String): Call<PlaythroughResponse>
    @GET("event/lacxi/{uuid}/{userId}")
    fun playLacXiEvent(@Path("uuid") uuid: String, @Path("userId") userId: String): Call<PlayLacXiResponse>
    @GET("event/lacxi/getUserItemsForEvent/{userId}/{eventId}")
    fun getUserItemsForEvent(@Path("userId") userId: String, @Path("eventId") eventId: String): Call<ItemListResponse>

    //fav event
    @POST("event/addFavEvent")
    fun addFavorite(@Body event: FavEvents): Call<Void>
    @DELETE("event/deleteFavEvent")
    fun deleteFavorite(@Query("eventId") eventId: String, @Query("userId") userId: String): Call<Void>
    @GET("event/getAllFavEvent")
    fun getAllFavorite(@Query("userId") userId: String): Call<List<Event>>
}