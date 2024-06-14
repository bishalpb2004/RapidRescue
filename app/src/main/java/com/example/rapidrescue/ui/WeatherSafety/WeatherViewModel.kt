package com.example.rapidrescue.ui.WeatherSafety

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {
    private val weatherApiService = WeatherApiService.create()
    private val _weatherData = MutableLiveData<List<WeatherData>>()
    val weatherData: LiveData<List<WeatherData>> = _weatherData
    private val _currentCity = MutableLiveData<String>()
    val currentCity: LiveData<String> = _currentCity

    fun fetchWeatherForecast(location: String) {
        viewModelScope.launch {
            try {
                val response = weatherApiService.getWeatherForecast(location, "1009c909fda24ec6b0e174401241306", 7)
                val forecastData = response.forecast.forecastday.map {
                    WeatherData(
                        date = it.date,
                        maxTemp = it.day.maxtemp_c,
                        minTemp = it.day.mintemp_c,
                        condition = it.day.condition.text,
                        iconUrl = it.day.condition.icon
                    )
                }
                _weatherData.value = forecastData
                _currentCity.value = response.location.name // Set the current city
            } catch (e: Exception) {
                Log.e("WeatherViewModel", "Error fetching weather data", e)
            }
        }
    }
}


