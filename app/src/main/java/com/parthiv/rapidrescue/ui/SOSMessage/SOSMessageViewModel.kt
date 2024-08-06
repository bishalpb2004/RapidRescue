package com.parthiv.rapidrescue.ui.SOSMessage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SOSMessageViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "Edit"
    }
    val text: LiveData<String> = _text
}