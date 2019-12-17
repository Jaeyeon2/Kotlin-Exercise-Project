package com.jaeyeon.expeditiouscopyorderapp.ui.mySurrounding

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MySurroundingViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is my surrounding Fragment"
    }
    val text: LiveData<String> = _text
}