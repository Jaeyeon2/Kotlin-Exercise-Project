package com.jaeyeon.expeditiouscopyorderapp.ui.myPage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.jaeyeon.expeditiouscopyorderapp.MainActivity
import com.jaeyeon.expeditiouscopyorderapp.R

class SignupActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var et_inputEmail: EditText
    private lateinit var et_inputNickname: EditText
    private lateinit var et_inputPassword: EditText
    var database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("message")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        auth = FirebaseAuth.getInstance()

        val toolbar = findViewById<Toolbar>(R.id.signup_toolbar)
        setSupportActionBar(toolbar)
        getSupportActionBar()?.setDisplayShowTitleEnabled(false)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setHomeAsUpIndicator(R.drawable.ic_left_arrow)

        et_inputEmail = findViewById<EditText>(R.id.signup_email)
        et_inputNickname = findViewById<EditText>(R.id.signup_nickname)
        et_inputPassword = findViewById<EditText>(R.id.signup_password)
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
    //    updateUI(currentUser)
    }

    /*
    private fun createAccount(email: String, password: String) {
        Log.d("createAccount:$email", "")
        if (!validateForm()) {
            return
        }


    }

     */

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.signup_toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home-> {
                finish()
                return true
            }
            R.id.action_signupFinish -> {

                auth.createUserWithEmailAndPassword(
                    et_inputEmail.text.toString(),
                    et_inputPassword.text.toString()
                )
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            val user = auth.currentUser
                            var userEmail = user?.email
                            Log.d("회원가입 성공", user?.email)
                            MainActivity.accUserEmail = userEmail.toString()

                            /*
                            val myPageFragment = MyPageFragment()
                            val transaction = supportFragmentManager.beginTransaction()
                            transaction.replace(R.id.signup_container, myPageFragment)
                            transaction.commit()
                             */
                            val mainIntent = Intent(this, MainActivity::class.java)
                            mainIntent.putExtra("request_page", "SignupActivity")
                            startActivity(mainIntent)
                            finish()

                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.exception)
                            Toast.makeText(
                                baseContext, "Authentication failed.",
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.d("회원가입 실패", et_inputEmail.text.toString())
                        }
                    }
                myRef.setValue("Hello, World!")

                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    companion object {
        private const val TAG = "EmailPassword"
    }
}
