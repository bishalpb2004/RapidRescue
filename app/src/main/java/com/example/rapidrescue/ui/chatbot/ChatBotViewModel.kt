package com.example.rapidrescue.ui.chatbot

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ChatBotViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Hello"
    }
    val text: LiveData<String> = _text
}