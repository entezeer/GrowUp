package com.example.growup

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.tasks.TaskExecutors
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class VerifyPhoneActivity : AppCompatActivity() {

    private var verificationId: String? = null
    private var loginBtn: Button? = null
    private var verifyPhone: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_phone)

        loginBtn = findViewById(R.id.login_btn)
        verifyPhone = findViewById(R.id.verify_phone)
        val phoneNumber = intent.getStringExtra("phonenumber")
        Toast.makeText(this, phoneNumber, Toast.LENGTH_LONG).show()
        sendVerificationCode(phoneNumber)

        loginBtn?.setOnClickListener {
            val code = verifyPhone?.text.toString().trim()
            if (code.isEmpty() || code.length < 6){
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
        GrowUpApplication.mAuth.signInWithCredential(credential)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    GrowUpApplication.mUserRef.child(GrowUpApplication.mAuth.currentUser?.uid!!).setValue(GrowUpApplication.mUserData).addOnCompleteListener { it1 ->
                        if(it1.isSuccessful){
                            val intent = Intent(this@VerifyPhoneActivity, MarketActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                        }
                    }
                } else {
                    Toast.makeText(this@VerifyPhoneActivity, it.exception?.message, Toast.LENGTH_LONG).show()
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
}
