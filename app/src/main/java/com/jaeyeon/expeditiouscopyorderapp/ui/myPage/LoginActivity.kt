package com.jaeyeon.expeditiouscopyorderapp.ui.myPage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.jaeyeon.expeditiouscopyorderapp.R

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val tv_goSignup = findViewById<TextView>(R.id.login_goSignup)
        tv_goSignup.setOnClickListener(this)


    }

    override fun onClick(view: View?) {
        when(view?.id) {
            R.id.login_goSignup -> {
             val signupIntent = Intent(this, SignupActivity::class.java)
             startActivity(signupIntent)
                finish()
            }
        }
    }
}
