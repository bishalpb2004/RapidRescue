package com.example.rapidrescue.ui.WeatherSafety

// WeatherData.kt
data class WeatherData(
    val date: String,
    val maxTemp: Double,
    val minTemp: Double,
    val avgTemp: Double,
    val condition: String
)
