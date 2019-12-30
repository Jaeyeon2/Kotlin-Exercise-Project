package com.jaeyeon.expeditiouscopyorderapp.ui.myPage

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.jaeyeon.expeditiouscopyorderapp.MainActivity
import com.jaeyeon.expeditiouscopyorderapp.MainActivity.Companion.accUserNickname
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
        val ll_userInfor = root.findViewById<LinearLayout>(R.id.my_page_userInformation)
        if(MainActivity.accUserEmail.equals("noLogin")) {
            tv_userNickname.text = "등록된 계정이 없습니다."
            tv_userWelcome.visibility = INVISIBLE
            ll_userInfor.setOnClickListener {
                val loginIntent = Intent(context, LoginActivity::class.java)
                startActivity(loginIntent)
            }
        } else {
            tv_userNickname.setText(MainActivity.accUserNickname)
            Log.d("accUserNickname22", accUserNickname)
            ll_userInfor.setOnClickListener {
                val userInforIntent = Intent(context, UserInformation::class.java)
                startActivity(userInforIntent)
            }
        }
        return root
    }


}