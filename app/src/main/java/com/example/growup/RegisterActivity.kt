package com.example.growup

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class RegisterActivity : AppCompatActivity() {

    private var registerBtn: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        init()
    }

    private fun init() {
        registerBtn = findViewById(R.id.register_btn)
        registerBtn?.setOnClickListener {
            startActivity(Intent(this, FarmerActivity::class.java))
        }
    }
}
