package com.entezeer.tracking.utils

import android.text.InputFilter
import android.text.Spanned
import android.widget.EditText

/**
 * Created by entezeer on 25.03.19
 */
object ValidUtils {
    fun validCredentials(
        email: String,
        password: String,
        emailEditText: EditText,
        passwordEditText: EditText
    ): Boolean {
        val validPassword = password.length >= 8
        if (!validPassword) {
            passwordEditText.error = "Invalid Password"
            passwordEditText.requestFocus()
        }
        val validEmail = email.isNotEmpty()
        if (!validEmail) {
            emailEditText.error = "Invalid Email"
            emailEditText.requestFocus()
        }
        return validPassword && validEmail
    }

}