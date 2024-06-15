package com.example.rapidrescue.ui.News

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class NewsViewModel : ViewModel() {
    private val repository = NewsRepository()
    private val _news = MutableLiveData<List<News>>()
    val news: LiveData<List<News>> get() = _news

    fun getTopHeadlines(country: String, apiKey: String) {
        viewModelScope.launch {
            try {
                val response = repository.getTopHeadlines(country, apiKey)
                // Limit the number of articles to 100
                _news.value = response.articles.take(100)
                Log.d("NewsViewModel", "Number of articles: ${_news.value?.size}")
            } catch (e: Exception) {
                Log.e("NewsViewModel", "Error fetching news", e)
            }
        }
    }
}


