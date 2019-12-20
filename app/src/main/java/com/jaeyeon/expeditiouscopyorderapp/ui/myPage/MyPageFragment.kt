package com.jaeyeon.expeditiouscopyorderapp.ui.myPage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.jaeyeon.expeditiouscopyorderapp.MainActivity
import com.jaeyeon.expeditiouscopyorderapp.R
import kotlinx.android.synthetic.main.fragment_my_page.*

class MyPageFragment : Fragment() {

    private lateinit var myPageViewModel: MyPageViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        myPageViewModel =
            ViewModelProviders.of(this).get(MyPageViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_my_page, container, false)
        val tv_userNickname = root.findViewById<TextView>(R.id.my_page_userNickname)
        val tv_userWelcome = root.findViewById<TextView>(R.id.my_page_welcome)
        if(MainActivity.accUserEmail.equals("noLogin")) {
            tv_userNickname.text = "등록된 계정이 없습니다."
            tv_userWelcome.visibility = INVISIBLE
        } else {

        }

        return root
    }


}