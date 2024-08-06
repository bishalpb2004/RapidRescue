package com.google.rapidrescue.ui.News

data class NewsResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<News>
)

