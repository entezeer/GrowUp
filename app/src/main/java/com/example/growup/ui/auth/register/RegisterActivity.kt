package com.example.growup.ui.auth.register

import android.app.ProgressDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.view.View
import android.widget.*
import com.entezeer.tracking.utils.InternetUtil
import com.entezeer.tracking.utils.ValidUtils
import com.example.growup.GrowUpApplication
import com.example.growup.R
import com.example.growup.data.RepositoryProvider
import com.example.growup.models.CountryCodes
import com.example.growup.models.Regions
import com.example.growup.data.user.model.User
import com.example.growup.ui.auth.login.LoginContract
import com.example.growup.ui.auth.verify.VerifyPhoneActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class RegisterActivity : AppCompatActivity(), RegisterContract.View {

    private var mPresenter: RegisterContract.Presenter? = null
    private var progressDialog: ProgressDialog? = null

    private var radioGroup: RadioGroup? = null
    private var radioButton: RadioButton? = null
    private var exist: Boolean = false
    private var name: EditText? = null
    private var lastName: EditText? = null
    private var email: EditText? = null
    private var number: EditText? = null
    private var password: EditText? = null

    private var backButton: FloatingActionButton? = null

    private var userType = ""

    private var registerBtn: Button? = null
    private var spinnerCountries: Spinner? = null
    private var spinnerRegions: Spinner? = null
    private var spinnerDistricts: Spinner? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        mPresenter = RegisterPresenter(RepositoryProvider.getUserDataSource())

        mPresenter?.attachView(this)

        mPresenter?.checkNetwork(this)

        init()
    }

    private fun init() {
        registerBtn = findViewById(R.id.register_btn)
        spinnerCountries = findViewById(R.id.spinner_countries)
        radioGroup = findViewById(R.id.type_radio_group)
        spinnerRegions = findViewById(R.id.spinner_regions)
        spinnerDistricts = findViewById(R.id.spinner_districts)

        name = findViewById(R.id.name)
        lastName = findViewById(R.id.last_name)
        email = findViewById(R.id.email)
        number = findViewById(R.id.number)
        password = findViewById(R.id.password)

        backButton = findViewById(R.id.back_button)

        backButton?.setOnClickListener {
            onBackPressed()
        }

        spinnerCountries?.adapter =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, CountryCodes.countryNames)

        spinnerRegions?.adapter =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, Regions.regions)

        spinnerRegions?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                spinnerDistricts?.adapter =
                    ArrayAdapter<String>(
                        this@RegisterActivity,
                        android.R.layout.simple_spinner_dropdown_item,
                        Regions.regionsList[0]
                    )
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                spinnerDistricts?.adapter =
                    ArrayAdapter<String>(
                        this@RegisterActivity,
                        android.R.layout.simple_spinner_dropdown_item,
                        Regions.regionsList[spinnerRegions?.selectedItemPosition!!]
                    )
            }
        }

        registerBtn?.setOnClickListener {
            if (ValidUtils.checkRegisterInputData(name!!, lastName!!, number!!, password!!, radioGroup!!, this)) {
                if (ValidUtils.validCredentials(password?.text.toString(), password!!)) {
                    val code = CountryCodes.countryAreaCodes[spinnerCountries?.selectedItemPosition!!]
                    val mNumber = number?.text.toString().trim()
                    val mPassword = password?.text.toString()
                    mPresenter?.register(code,
                        mNumber,
                        mPassword,
                        User(
                            name?.text.toString(),
                            lastName?.text.toString(),
                            "+${code + mNumber}",
                            email?.text.toString(),
                            password?.text.toString(),
                            userType,
                            "${spinnerRegions?.selectedItem},${spinnerDistricts?.selectedItem}"
                        ))
                }
            }

        }
    }

    override fun showValidError() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showToast() {
        Toast.makeText(
            this,
            "Пользователь с таким номером уже существует",
            Toast.LENGTH_LONG
        ).show()
    }

    override fun openVerifyActivity(number: String) {
        VerifyPhoneActivity.start(this, number, "login")
    }


    override fun showLoading() {
        Utils.progressShow(progressDialog)
    }

    override fun hideLoading() {
        progressDialog?.cancel()
    }

    override fun showNetworkAlert() {
        InternetUtil.showInternetAlert(this)
    }

    override fun finishView() {
        finish()
    }

    override fun attachPresenter(presenter: RegisterContract.Presenter) {
        mPresenter = presenter
    }


    private fun register() {
        checkUserType()

        val code = CountryCodes.countryAreaCodes[spinnerCountries?.selectedItemPosition!!]
        var mNumber = number?.text.toString().trim()
        val mPassword = password?.text.toString()
        if (mNumber[0].equals('0') && code.equals("996")) {
            mNumber = mNumber.substring(1, mNumber.length)
        }
        if (mNumber.isEmpty() || mNumber.length < 9) {
            number?.error = "Valid number is required"
            number?.requestFocus()
            return
        }

        checkUserExist("+${code + mNumber}") {
            if (it) {
                Toast.makeText(this@RegisterActivity, "Пользователь с таким номером уже существует", Toast.LENGTH_LONG)
                    .show()
            } else {
                if (ValidUtils.validCredentials(mPassword, password!!)) {
                    GrowUpApplication.mUserData = User(
                        name?.text.toString(),
                        lastName?.text.toString(),
                        "+${code + mNumber}",
                        email?.text.toString(),
                        password?.text.toString(),
                        userType,
                        "${spinnerRegions?.selectedItem},${spinnerDistricts?.selectedItem}"
                    )
                    val intent = Intent(this, VerifyPhoneActivity::class.java)
                    intent.putExtra("phoneNumber", "+${code + mNumber}")
                    intent.putExtra("fromActivity", "login")
                    startActivity(intent)
                }
            }
        }
    }


    private fun checkUserExist(phoneNumber: String, callBack: (Boolean) -> Unit) {
        GrowUpApplication.mUserRef.orderByChild("phoneNumber").equalTo(phoneNumber)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(this@RegisterActivity, databaseError.message, Toast.LENGTH_LONG).show()
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.value != null) {
                        exist = true
                        callBack(exist)
                    } else {
                        exist = false
                        callBack(exist)
                    }
                }
            })
    }

    private fun checkUserType() {
        val selectedId = radioGroup?.checkedRadioButtonId
        radioButton = selectedId?.let { findViewById(it) }

        userType = radioButton?.text.toString()
    }
}