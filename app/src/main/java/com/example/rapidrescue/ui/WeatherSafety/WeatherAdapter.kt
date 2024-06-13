package com.example.rapidrescue.ui.WeatherSafety

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rapidrescue.R

class WeatherAdapter(private val weatherList: List<WeatherData>) : RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {

    class WeatherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        val tvTemperature: TextView = itemView.findViewById(R.id.tvTemperature)
        val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_weather, parent, false)
        return WeatherViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val weather = weatherList[position]
        holder.tvDate.text = weather.date
        holder.tvTemperature.text = "${weather.maxTemp} / ${weather.minTemp} / ${weather.avgTemp} °C"
        holder.tvDescription.text = weather.condition
        Log.d("WeatherAdapter", "Binding weather data at position $position: $weather")
    }

    override fun getItemCount() = weatherList.size
}
