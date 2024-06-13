package com.example.rapidrescue.ui.WeatherSafety

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rapidrescue.R
import com.example.rapidrescue.ui.WeatherSafety.WeatherViewModel
import com.example.rapidrescue.ui.WeatherSafety.WeatherAdapter

class WeatherSafety : AppCompatActivity() {
    private lateinit var viewModel: WeatherViewModel
    private lateinit var weatherAdapter: WeatherAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_weather_safety)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize ViewModel and RecyclerView
        viewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)

        val rvForecast: RecyclerView = findViewById(R.id.rvForecast)
        rvForecast.layoutManager = LinearLayoutManager(this)

        viewModel.weatherData.observe(this, { weatherData ->
            findViewById<TextView>(R.id.tvCurrentWeather).text = weatherData.firstOrNull()?.condition ?: "No data"

            weatherAdapter = WeatherAdapter(weatherData)
            rvForecast.adapter = weatherAdapter

            Log.d("WeatherSafety", "Adapter set with data: $weatherData")
        })

        // Fetch weather forecast data for a specific location
        viewModel.fetchWeatherForecast("Guwahati")  // Replace with your desired location
    }
}

