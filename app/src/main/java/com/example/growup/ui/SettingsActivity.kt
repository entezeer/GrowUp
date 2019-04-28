package com.example.growup.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import com.example.growup.R

class SettingsActivity : AppCompatActivity() {

    private var backButton: FloatingActionButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        init()
    }

    private fun init(){
        backButton = findViewById(R.id.back_button)

        backButton?.setOnClickListener {
            onBackPressed()
        }
    }
}
