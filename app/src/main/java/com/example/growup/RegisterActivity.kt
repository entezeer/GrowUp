package com.example.growup

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import com.example.growup.models.CountryCodes

class RegisterActivity : AppCompatActivity() {


    private var registerBtn: Button? = null
    private var spinner: Spinner? = null

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
        spinner = findViewById(R.id.spinner_countries)
        spinner?.adapter = ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,CountryCodes.countryNames)


    }
}
