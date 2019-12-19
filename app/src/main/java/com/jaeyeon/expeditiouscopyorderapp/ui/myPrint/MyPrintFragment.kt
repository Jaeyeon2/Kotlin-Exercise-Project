package com.jaeyeon.expeditiouscopyorderapp.ui.myPrint

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.jaeyeon.expeditiouscopyorderapp.R

class MyPrintFragment : Fragment() {

    private lateinit var myPrintViewModel: MyPrintViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        myPrintViewModel =
            ViewModelProviders.of(this).get(MyPrintViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_my_print, container, false)
        return root
    }
}