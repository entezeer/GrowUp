package com.example.growup

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.entezeer.tracking.utils.ValidUtils
import com.example.growup.models.CountryCodes

class RegisterActivity : AppCompatActivity() {

    private var radioGroup: RadioGroup? = null
    private var radioButton: RadioButton? = null

    private var name: EditText? = null
    private var last_name: EditText? = null
    private var email: EditText? = null
    private var number: EditText? = null
    private var password: EditText? = null

    private var userType = ""

    private var registerBtn: Button? = null
    private var spinner: Spinner? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        init()

    }

    private fun init() {
        registerBtn = findViewById(R.id.register_btn)
        spinner = findViewById(R.id.spinner_countries)
        radioGroup = findViewById(R.id.type_radio_group)

        name = findViewById(R.id.name)
        last_name = findViewById(R.id.last_name)
        email = findViewById(R.id.email)
        number = findViewById(R.id.number)
        password = findViewById(R.id.password)

        spinner?.adapter = ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,CountryCodes.countryNames)

        registerBtn?.setOnClickListener {
            register()
//            startActivity(Intent(this, FarmerActivity::class.java))
        }
    }

    private fun register(){
        checkUserType()
        Toast.makeText(this,userType, Toast.LENGTH_LONG).show()
        val code = CountryCodes.countryAreaCodes[spinner?.selectedItemPosition!!]
        val mNumber = number?.text.toString().trim()
        val mEmail = email?.text.toString()
        val mPassword = password?.text.toString()

        if (mNumber.isEmpty() || mNumber.length < 9){
            number?.error = "Valid number is required"
            number?.requestFocus()
            return
        }

        if (ValidUtils.validCredentials(mEmail,mPassword,email!!,password!!)){
            val intent = Intent(this,VerifyPhoneActivity::class.java)
            intent.putExtra("phonenumber","+${code+mNumber}")
            startActivity(intent)
        }
    }

    private fun checkUserType(){
        val selectedId = radioGroup?.checkedRadioButtonId
        radioButton = selectedId?.let { findViewById(it) }

        userType = radioButton?.text.toString()
    }
}
