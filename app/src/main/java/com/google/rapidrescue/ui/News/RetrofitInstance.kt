package com.google.rapidrescue.ui.News

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://newsapi.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: NewsAPI by lazy {
        retrofit.create(NewsAPI::class.java)
    }
}
