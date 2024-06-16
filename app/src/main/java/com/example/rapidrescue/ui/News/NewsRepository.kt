package com.example.rapidrescue.ui.News

class NewsRepository {
    suspend fun getTopHeadlines(country: String, apiKey: String): NewsResponse {
        return RetrofitInstance.api.getTopHeadlines(country, apiKey)
    }

    suspend fun searchNews(query: String, apiKey: String): NewsResponse {
        return RetrofitInstance.api.searchNews(query, apiKey)
    }
}



