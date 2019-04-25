package com.example.growup.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.growup.GrowUpApplication
import com.example.growup.R
import com.example.growup.ui.main.MainActivity
import com.example.growup.ui.start.StartActivity

class SplashActivity : AppCompatActivity() {

    private val SPLASH_TIME_OUT: Long = 3000

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

        if (GrowUpApplication.mAuth.currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
        } else startActivity(Intent(this, StartActivity::class.java))
        finish()
    }
}
