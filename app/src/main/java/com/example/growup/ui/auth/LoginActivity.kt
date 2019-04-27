package com.example.growup.ui.auth

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.widget.*
import com.example.growup.GrowUpApplication
import com.example.growup.R
import com.example.growup.models.CountryCodes
import com.google.firebase.database.*

class LoginActivity : AppCompatActivity() {

    private var loginBtn: Button? = null
    private var number: EditText? = null
    private var spinner: Spinner? = null

    private var backButton: FloatingActionButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        init()
    }

    private fun init() {
        spinner = findViewById(R.id.spinner_countries)
        number = findViewById(R.id.number)
        loginBtn = findViewById(R.id.login_btn)
        backButton = findViewById(R.id.back_button)

        backButton?.setOnClickListener {
            onBackPressed()
        }

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

        checkUserExist("+${code + mNumber}")
    }

    private fun checkUserExist(phoneNumber: String) {
        val intent = Intent(this, VerifyPhoneActivity::class.java)

        GrowUpApplication.mUserRef.orderByChild("phoneNumber").equalTo(phoneNumber)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.value != null) {
                        // user with this number already exist
                        intent.putExtra("phonenumber", phoneNumber)
                        intent.putExtra("fromActivity", "login")
                        startActivity(intent)
                    } else {
                        // user with this number doesn't exist ->

                        Toast.makeText(this@LoginActivity,"User with this phone number doesn't exist , please sign up",Toast.LENGTH_LONG).show()

                        Toast.makeText(this@LoginActivity,"Пользователь с таким номером не зарегистрирован, чтобы войти пожалуйста зарегистрируйтесь.",Toast.LENGTH_LONG).show()


                    }
                }

            })
    }

}



