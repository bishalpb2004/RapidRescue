package com.google.rapidrescue.ui.WeatherSafety

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.rapidrescue.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class WeatherSafety : AppCompatActivity() {
    private lateinit var viewModel: WeatherViewModel
    private lateinit var weatherAdapter: WeatherAdapter
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var progressBar: ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_weather_safety)

        val mainView = findViewById<View>(R.id.main)
        ViewCompat.setOnApplyWindowInsetsListener(mainView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize ViewModel and RecyclerView
        viewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)

        val rvForecast: RecyclerView = findViewById(R.id.rvForecast)
        rvForecast.layoutManager = LinearLayoutManager(this)

        viewModel.weatherData.observe(this, { weatherData ->
            findViewById<TextView>(R.id.tvCurrentLocation).text = weatherData.firstOrNull()?.condition ?: ""

            weatherAdapter = WeatherAdapter(weatherData)
            rvForecast.adapter = weatherAdapter

            Log.d("WeatherSafety", "Adapter set with data: $weatherData")
        })

        viewModel.currentCity.observe(this, { currentCity ->
            if (currentCity == "Loading...") {
                // Show progress bar
                progressBar.visibility = View.VISIBLE
            } else if (currentCity == "Error") {
                // Show error message or handle error state
                progressBar.visibility = View.GONE
                Toast.makeText(this, "Failed to fetch weather data", Toast.LENGTH_SHORT).show()
            } else {
                // Data loaded successfully, update UI
                progressBar.visibility = View.GONE
                findViewById<TextView>(R.id.tvCurrentLocation).text = currentCity
            }
        })


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        progressBar = findViewById(R.id.progressBar)

        fetchLocation()
    }

    private fun fetchLocation() {
        progressBar.visibility = View.VISIBLE
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request location permissions
            requestLocationPermissionLauncher.launch(arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ))
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val latitude = location.latitude
                val longitude = location.longitude
                viewModel.fetchWeatherForecast("$latitude,$longitude")
            } else {
                Log.e("WeatherSafety", "Location is null")
            }
        }.addOnFailureListener { e ->
            Log.e("WeatherSafety", "Failed to get location", e)
        }
    }

    private val requestLocationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true || permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
            // Permission granted
            fetchLocation()
        } else {
            // Permission denied
            Log.e("WeatherSafety", "Location permission denied")
        }
    }
}


