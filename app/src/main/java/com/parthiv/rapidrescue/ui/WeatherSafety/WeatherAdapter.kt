package com.parthiv.rapidrescue.ui.WeatherSafety

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.parthiv.rapidrescue.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WeatherAdapter(private val weatherData: List<WeatherData>) : RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {

    private val inputDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val outputDateFormat = SimpleDateFormat("EEEE", Locale.getDefault())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_weather, parent, false)
        return WeatherViewHolder(view)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val weather = weatherData[position]

        val dayLabel = when (position) {
            0 -> "Today"
            1 -> "Tomorrow"
            else -> {
                val date = inputDateFormat.parse(weather.date)
                outputDateFormat.format(date ?: Date())
            }
        }

        holder.tvDay.text = dayLabel
        holder.tvMaxTemp.text = "${weather.maxTemp}°C"
        holder.tvMinTemp.text = "${weather.minTemp}°C"

        // Load the weather icon
        Glide.with(holder.itemView.context)
            .load("https:${weather.iconUrl}")
            .into(holder.ivWeatherIcon)
    }

    override fun getItemCount(): Int {
        return weatherData.size
    }

    inner class WeatherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDay: TextView = itemView.findViewById(R.id.tvDay)
        val tvMaxTemp: TextView = itemView.findViewById(R.id.tvMaxTemp)
        val tvMinTemp: TextView = itemView.findViewById(R.id.tvMinTemp)
        val ivWeatherIcon: ImageView = itemView.findViewById(R.id.ivWeatherIcon)
    }
}

