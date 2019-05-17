package com.example.growup.ui.auth.login

import android.app.ProgressDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.widget.*
import com.entezeer.tracking.utils.InternetUtil
import com.example.growup.GrowUpApplication
import com.example.growup.R
import com.example.growup.data.RepositoryProvider
import com.example.growup.data.user.model.User
import com.example.growup.models.CountryCodes
import com.example.growup.ui.auth.verify.VerifyPhoneActivity

@Suppress("UNREACHABLE_CODE")
class LoginActivity : AppCompatActivity(), LoginContract.View {

    private var mPresenter: LoginContract.Presenter? = null
    private var progressDialog: ProgressDialog? = null
    private var loginBtn: Button? = null
    private var number: EditText? = null
    private var spinner: Spinner? = null

    private var backButton: FloatingActionButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mPresenter = LoginPresenter(RepositoryProvider.getUserDataSource())
        mPresenter?.attachView(this)

        mPresenter?.checkNetwork(this)

        init()
    }

    private fun init() {
        spinner = findViewById(R.id.spinner_countries)
        number = findViewById(R.id.number)
        loginBtn = findViewById(R.id.login_btn)
        backButton = findViewById(R.id.back_button)
        progressDialog = ProgressDialog(this)

        backButton?.setOnClickListener {
            onBackPressed()
        }

        spinner?.adapter =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, CountryCodes.countryNames)
        loginBtn?.setOnClickListener {
            mPresenter?.login(
                CountryCodes.countryAreaCodes[spinner?.selectedItemPosition!!],
                number?.text.toString().trim()
            )
        }
    }

    override fun showNumberError() {
        number?.error = "Valid number is required"
        number?.requestFocus()
    }

    override fun showToast() {
        Toast.makeText(
            this,
            "Пользователь с таким номером не зарегистрирован, чтобы войти пожалуйста зарегистрируйтесь",
            Toast.LENGTH_LONG
        ).show()
    }

    override fun openVerifyActivity(number: String) {
        GrowUpApplication.mUserData = User()
        VerifyPhoneActivity.start(this, number, "login",null)
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

    override fun attachPresenter(presenter: LoginContract.Presenter) {
        mPresenter = presenter
    }
}