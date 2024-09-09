package com.example.vou_mobile.services.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

data class QuestionCountResponse(
    val questionCount: Int
)

interface QuizService {
    @GET("quiz/questionCount/{id_event}")
    fun getQuestionCountByEvent(@Path("id_event") idEvent: String): Call<QuestionCountResponse>
}