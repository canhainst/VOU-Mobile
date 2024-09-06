package com.example.vou_mobile.services
import com.example.vou_mobile.model.Event
import com.example.vou_mobile.model.FavEvents
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

data class FavoriteResponse(
    val isFavorite: Boolean
)

interface EventService {
    //event
    @GET("event/getEvent/{id}")
    fun getEventByID(@Path("id") id: String): Call<Event>
    @GET("event/getOngoingEvents")
    fun getOngoingEvents(): Call<List<Event>>

    //fav event
    @POST("event/addFavEvent")
    fun addFavorite(@Body event: FavEvents): Call<Void>
    @DELETE("event/deleteFavEvent")
    fun deleteFavorite(@Query("eventId") eventId: String, @Query("userId") userId: String): Call<Void>
    @GET("event/getAllFavEvent")
    fun getAllFavorite(@Query("userId") userId: String): Call<List<Event>>
}