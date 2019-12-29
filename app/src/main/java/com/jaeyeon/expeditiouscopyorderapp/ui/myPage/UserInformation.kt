package com.jaeyeon.expeditiouscopyorderapp.ui.myPage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.jaeyeon.expeditiouscopyorderapp.MainActivity
import com.jaeyeon.expeditiouscopyorderapp.R

class UserInformation : AppCompatActivity(), View.OnClickListener {

    private lateinit var tv_logout: TextView
    private lateinit var tv_withdrawal: TextView
    val user = FirebaseAuth.getInstance().currentUser
    lateinit var name: String
    lateinit var email: String
    lateinit var photoUrl: String
    lateinit var emailVerified: String
    private val TAG = "UserInformation"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_information)

        tv_logout = findViewById<TextView>(R.id.userInfor_logout)
        tv_withdrawal = findViewById<TextView>(R.id.userInfor_withdrawal)
        tv_logout.setOnClickListener(this)
        tv_withdrawal.setOnClickListener(this)

        if(user != null) {
            user?.let{
                name = user.displayName.toString()
                email = user.email.toString()
                photoUrl = user.photoUrl.toString()
                emailVerified = user.isEmailVerified.toString()
                Log.d("emailVerified", emailVerified)
            }
        } else {
            // No user is signed in
        }

    }

    override fun onClick(view: View?) {
        when(view?.id) {
            R.id.userInfor_logout -> {
                FirebaseAuth.getInstance().signOut()
                //MainActivity.accUserEmail = "noLogin"
                val mainIntent = Intent(this, MainActivity::class.java)
                mainIntent.putExtra("request_page", "UserInformation_logout")
                startActivity(mainIntent)
                finish()
            }
            R.id.userInfor_withdrawal -> {
                user?.delete()
                    ?.addOnCompleteListener { task ->
                        if(task.isSuccessful) {
                            Log.d(TAG, "User account deleted.")
                            val mainIntent = Intent(this, MainActivity::class.java)
                            startActivity(mainIntent)
                            finish()
                        }
                    }
            }
        }
    }
}
