package com.example.expeditiouscopyorderapp.ui.myPrint

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyPrintViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is my print Fragment"
    }
    val text: LiveData<String> = _text
}