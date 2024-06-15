package com.example.rapidrescue.ui.News

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {
    @GET("v2/top-headlines")
    suspend fun getTopHeadlines(
        @Query("country") country: String,
        @Query("apiKey") apiKey: String,
        @Query("pageSize") pageSize: Int = 100
    ): NewsResponse
}


object RetrofitInstance {
    val api: NewsApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://newsapi.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsApi::class.java)
    }
}
