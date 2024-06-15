package com.example.rapidrescue.ui.WeatherSafety

class WeatherRepository {
    private val weatherApiService = WeatherApiService.create()

    suspend fun getWeatherForecast(location: String, apiKey: String): WeatherResponse {
        return weatherApiService.getWeatherForecast(location, apiKey)
    }
}

