package com.google.rapidrescue.ui.WeatherSafety

data class WeatherResponse(
    val location: Location,
    val current: CurrentWeather,
    val forecast: Forecast
)

data class Location(
    val name: String,
    val region: String,
    val country: String,
    val lat: Double,
    val lon: Double,
    val tz_id: String,
    val localtime_epoch: Long,
    val localtime: String
)

data class CurrentWeather(
    val temp_c: Double,
    val condition: Condition
)

data class Condition(
    val text: String,
    val icon: String,
    val code: Int
)

data class Forecast(
    val forecastday: List<ForecastDay>
)

data class ForecastDay(
    val date: String,
    val day: Day
)

data class Day(
    val maxtemp_c: Double,
    val mintemp_c: Double,
    val avgtemp_c: Double,
    val condition: Condition
)

data class WeatherData(
    val date: String,
    val maxTemp: Double,
    val minTemp: Double,
    val condition: String,
    val iconUrl: String
)



