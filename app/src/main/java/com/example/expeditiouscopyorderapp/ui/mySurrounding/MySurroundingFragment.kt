package com.example.expeditiouscopyorderapp.ui.mySurrounding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.expeditiouscopyorderapp.R

class MySurroundingFragment : Fragment() {

    private lateinit var mySurroundingViewModel: MySurroundingViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mySurroundingViewModel =
            ViewModelProviders.of(this).get(MySurroundingViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_my_surrounding, container, false)
        val textView: TextView = root.findViewById(R.id.text_surrounding)
        mySurroundingViewModel.text.observe(this, Observer {
            textView.text = it
        })
        return root
    }
}