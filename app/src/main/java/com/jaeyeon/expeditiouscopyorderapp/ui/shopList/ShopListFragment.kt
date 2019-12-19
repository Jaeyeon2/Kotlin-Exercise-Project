package com.jaeyeon.expeditiouscopyorderapp.ui.shopList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.jaeyeon.expeditiouscopyorderapp.R

class ShopListFragment : Fragment() {

    private lateinit var shopListViewModel: ShopListViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        shopListViewModel =
            ViewModelProviders.of(this).get(ShopListViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_shop_list, container, false)
        return root
    }
}