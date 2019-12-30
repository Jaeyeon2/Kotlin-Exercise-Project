package com.jaeyeon.expeditiouscopyorderapp.ui.myPage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.jaeyeon.expeditiouscopyorderapp.MainActivity
import com.jaeyeon.expeditiouscopyorderapp.R

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var et_inputEmail: EditText
    private lateinit var et_inputPassword: EditText
    private lateinit var btn_loginButton: Button
    val user = FirebaseAuth.getInstance().currentUser
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val tv_goSignup = findViewById<TextView>(R.id.login_goSignup)

        et_inputEmail = findViewById<EditText>(R.id.login_email)
        et_inputPassword = findViewById<EditText>(R.id.login_password)
        btn_loginButton = findViewById<Button>(R.id.login_finishLogin)
        auth = FirebaseAuth.getInstance()

        tv_goSignup.setOnClickListener(this)
        btn_loginButton.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when(view?.id) {
            R.id.login_goSignup -> {
             val signupIntent = Intent(this, SignupActivity::class.java)
             startActivity(signupIntent)
                finish()
            }
            R.id.login_finishLogin -> {
                auth.signInWithEmailAndPassword(et_inputEmail.text.toString(), et_inputPassword.text.toString()).addOnCompleteListener(this) { task ->
                    if(task.isSuccessful) {
                        // Sign in success
                        val user = auth.currentUser
                        val mainIntent = Intent(this, MainActivity::class.java)
                        mainIntent.putExtra("request_page", "LoginActivity_loginSuccess")
                        startActivity(mainIntent)
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(baseContext, "로그인 실패.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
    }
}
