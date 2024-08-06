package com.google.rapidrescue.ui.News

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewsViewModel : ViewModel() {
    private val repository = NewsRepository()
    private val _news = MutableLiveData<List<News>>()
    private val _isLoading = MutableLiveData<Boolean>()
    val news: LiveData<List<News>> get() = _news
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun getTopHeadlines(country: String, apiKey: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = withContext(Dispatchers.IO) {
                    repository.getTopHeadlines(country, apiKey)
                }
                _news.value = response.articles
            } catch (e: Exception) {
                Log.e("NewsViewModel", "Error fetching news", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun searchNews(query: String, apiKey: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = withContext(Dispatchers.IO) {
                    repository.searchNews(query, apiKey)
                }
                _news.value = response.articles
            } catch (e: Exception) {
                Log.e("NewsViewModel", "Error searching news", e)
            } finally {
                _isLoading.value = false
            }
        }
    }
}
