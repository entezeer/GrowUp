package com.example.growup.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.example.growup.GrowUpApplication
import com.example.growup.R
import com.example.growup.models.Products
import com.example.growup.models.User
import com.example.growup.ui.main.MainActivity
import com.example.growup.ui.start.StartActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class SplashActivity : AppCompatActivity() {

    private val SPLASH_TIME_OUT: Long = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed(
            {
                checkUser()
            }, SPLASH_TIME_OUT
        )
    }

    private fun checkUser() {
        if (GrowUpApplication.mAuth.currentUser != null ) {
            startActivity(Intent(this, MainActivity::class.java))
        } else startActivity(Intent(this, StartActivity::class.java))
        finish()
    }
}
