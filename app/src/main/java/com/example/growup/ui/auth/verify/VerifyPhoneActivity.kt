package com.example.growup.ui.auth.verify

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.growup.GrowUpApplication
import com.example.growup.R
import com.example.growup.data.RepositoryProvider
import com.example.growup.data.user.UserDataSource
import com.example.growup.data.user.model.User
import com.example.growup.ui.auth.register.RegisterContract
import com.example.growup.ui.main.MainActivity
import com.google.android.gms.tasks.TaskExecutors
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.gson.Gson
import java.util.concurrent.TimeUnit

class VerifyPhoneActivity : AppCompatActivity() {


    private var mPresenter: RegisterContract.Presenter? = null
    private var progressDialog: ProgressDialog? = null

    private var verificationId: String? = null
    private var fromActivity: String? = null
    private var user: User? = null
    private var loginBtn: Button? = null
    private var verifyPhone: EditText? = null

    private var backButton: FloatingActionButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_phone)

        init()

        val phoneNumber = intent.getStringExtra(EXTRA_PHONENUMBER)
        fromActivity = intent.getStringExtra(EXTRA_ACTIVITY)
        user = GrowUpApplication.mUserData
        Toast.makeText(this, phoneNumber, Toast.LENGTH_LONG).show()
        sendVerificationCode(phoneNumber)
    }

    private fun init() {

        progressDialog = ProgressDialog(this)
        loginBtn = findViewById(R.id.login_btn)
        verifyPhone = findViewById(R.id.verify_phone)
        backButton = findViewById(R.id.back_button)

        backButton?.setOnClickListener {
            onBackPressed()
        }

        loginBtn?.setOnClickListener {
            val code = verifyPhone?.text.toString().trim()
            if (code.isEmpty() || code.length < 6) {
                verifyPhone?.error = "Enter code . . ."
                verifyPhone?.requestFocus()
            }

            verifyCode(code)
        }
    }

    private fun verifyCode(code: String) {

        val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
        signInWithCredential(credential)
    }

    private fun signInWithCredential(credential: PhoneAuthCredential) {
        Utils.progressShow(progressDialog)
        if (fromActivity != "login") {
            user?.let {
                RepositoryProvider.getUserDataSource()
                    .setUser(credential, it, false, object : UserDataSource.UserCallback {
                        override fun onSuccess(result: User) {
                            startActivity(Intent(this@VerifyPhoneActivity, MainActivity::class.java))
                        }

                        override fun onFailure(message: String) {
                        }

                    })
            }
        }
    }

    private fun sendVerificationCode(number: String) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            number,
            60,
            TimeUnit.SECONDS,
            TaskExecutors.MAIN_THREAD,
            mCallback
        )
    }

    private val mCallback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onCodeSent(s: String?, phoneAuthProvider: PhoneAuthProvider.ForceResendingToken?) {
            super.onCodeSent(s, phoneAuthProvider)
            verificationId = s
        }

        override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential?) {
            val code = phoneAuthCredential?.smsCode
            if (code != null) {
                verifyCode(code)
            }
        }

        override fun onVerificationFailed(e: FirebaseException?) {
            Toast.makeText(this@VerifyPhoneActivity, e?.message, Toast.LENGTH_LONG).show()
        }
    }

    companion object {
        private const val EXTRA_ACTIVITY = "fromActivity"
        private const val EXTRA_PHONENUMBER = "phoneNumber"
        private const val EXTRA_USER = "user"
        fun start(context: Context, phoneNumber: String, fragment: String, user: User) {
            val intent = Intent(context, VerifyPhoneActivity::class.java)
            intent.putExtra(EXTRA_ACTIVITY, fragment)
            intent.putExtra(EXTRA_PHONENUMBER, phoneNumber)
            context.startActivity(intent)
        }
    }
}
