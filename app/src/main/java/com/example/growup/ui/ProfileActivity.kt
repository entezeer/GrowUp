package com.example.growup.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.growup.R

class ProfileActivity : AppCompatActivity() {

    private var backButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        init()
    }

    private fun init(){
        backButton = findViewById(R.id.back_button)

        backButton?.setOnClickListener {
            onBackPressed()
        }
    }
}
