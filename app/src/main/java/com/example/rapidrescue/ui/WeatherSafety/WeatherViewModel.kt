package com.example.rapidrescue.ui.WeatherSafety

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {
    private val repository = WeatherRepository()
    private val _weatherData = MutableLiveData<List<WeatherData>>()
    val weatherData: LiveData<List<WeatherData>> get() = _weatherData

    fun fetchWeatherForecast(location: String) {
        viewModelScope.launch {
            try {
                val apiKey = "1009c909fda24ec6b0e174401241306"  // Replace with your actual API key
                val response = repository.getWeatherForecast(location, apiKey)

                // Check if API response contains forecast data
                if (response.forecast.forecastday.size >= 7) {
                    val forecastData = response.forecast.forecastday.take(7).map { forecastDay ->
                        WeatherData(
                            date = forecastDay.date,
                            maxTemp = forecastDay.day.maxtemp_c,
                            minTemp = forecastDay.day.mintemp_c,
                            avgTemp = forecastDay.day.avgtemp_c,
                            condition = forecastDay.day.condition.text
                        )
                    }

                    _weatherData.postValue(forecastData)
                    Log.d("WeatherViewModel", "Weather data received: $forecastData")
                } else {
                    Log.e("WeatherViewModel", "Insufficient forecast data in API response")
                }
            } catch (e: Exception) {
                Log.e("WeatherViewModel", "Error fetching weather data", e)
            }
        }
    }
}


