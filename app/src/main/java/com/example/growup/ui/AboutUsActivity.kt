package com.example.growup.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import com.example.growup.R

class AboutUsActivity : AppCompatActivity() {
    private var btnBack: FloatingActionButton? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_us)
        init()
    }

    private fun init(){
        btnBack = findViewById(R.id.back)
        btnBack?.setOnClickListener {
            onBackPressed()
        }
    }
}
