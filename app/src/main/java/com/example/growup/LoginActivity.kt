package com.example.growup

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.entezeer.tracking.utils.ValidUtils
import com.example.growup.models.CountryCodes

class LoginActivity : AppCompatActivity() {

    private var loginBtn: Button? = null
    private var number: EditText? = null
    private var spinner: Spinner? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        init()
    }

    private fun init() {
        spinner = findViewById(R.id.spinner_countries)
        number = findViewById(R.id.number)
        loginBtn = findViewById(R.id.login_btn)

        spinner?.adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, CountryCodes.countryNames)
        loginBtn?.setOnClickListener {
            register()
        }
    }

    private fun register() {
        val code = CountryCodes.countryAreaCodes[spinner?.selectedItemPosition!!]
        val mNumber = number?.text.toString().trim()


        if (mNumber.isEmpty() || mNumber.length < 9) {
            number?.error = "Valid number is required"
            number?.requestFocus()
            return
        }

        val intent = Intent(this, VerifyPhoneActivity::class.java)
        intent.putExtra("phonenumber", "+${code + mNumber}")
        startActivity(intent)

    }
}
