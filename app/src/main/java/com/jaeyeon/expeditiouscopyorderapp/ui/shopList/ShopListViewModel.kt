package com.jaeyeon.expeditiouscopyorderapp.ui.shopList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ShopListViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is shop list Fragment"
    }
    val text: LiveData<String> = _text
}