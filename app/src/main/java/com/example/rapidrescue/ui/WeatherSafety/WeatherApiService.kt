package com.example.rapidrescue.ui.WeatherSafety

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// WeatherApiService.kt
interface WeatherApiService {
    @GET("/v1/forecast.json")  // Adjust the endpoint according to your API documentation
    suspend fun getWeatherForecast(
        @Query("q") location: String,
        @Query("key") apiKey: String,
        @Query("days") days: Int = 7  // Specify the number of forecast days (default to 7)
    ): WeatherResponse

    companion object {
        private const val BASE_URL = "https://api.weatherapi.com"

        fun create(): WeatherApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(WeatherApiService::class.java)
        }
    }
}


