package com.entezeer.tracking.utils

import android.content.Context
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import com.example.growup.ui.auth.RegisterActivity

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

    fun checkRegisterInputData(
        name: EditText,
        surname: EditText,
        email: EditText,
        phone: EditText,
        password: EditText,
        radioGroup: RadioGroup,
        context: Context
    ): Boolean{
        var valid = true
        if(name.text.trim().isEmpty() && name.text.trim().length < 2){
            name.error =  "Input Name Correct"
            valid = false
        }
        if(surname.text.trim().isEmpty() && surname.text.trim().length < 2){
            surname.error =  "Input Surname Correct"
            valid = false
        }
        if (email.text.trim().isEmpty() && email.text.trim().length < 9){
            email.error =  "Input Email Correct"
            valid = false
        }
        if (phone.text.trim().isEmpty() && phone.text.trim().length < 9){
            phone.error =  "Input Phone Correct"
            valid = false
        }
        if (password.text.trim().isEmpty() && password.text.trim().length < 9){
            password.error =  "Input Password Correct"
            valid = false
        }
        if (radioGroup.checkedRadioButtonId == -1){
            Toast.makeText(context,"Выбирете тип пользователя",Toast.LENGTH_SHORT).show()
        }
        return valid
    }

    fun checkEditProfileChanges(
        editProfileName: EditText,
        editProfileSurname: EditText,
        editProfileRegion: EditText,
        editProfileEmail: EditText
    ): Boolean {
        var valid = true
        if(editProfileName.text.trim().isEmpty() && editProfileName.text.trim().length < 2){
            editProfileName.error =  "Input Name Correct"
            valid = false
        }
        if(editProfileSurname.text.trim().isEmpty() && editProfileSurname.text.trim().length < 2){
            editProfileSurname.error =  "Input Name Correct"
            valid = false
        }
        if(editProfileRegion.text.trim().isEmpty() ){
            editProfileRegion.error =  "Input Name Correct"
            valid = false
        }
        if (editProfileEmail.text.trim().isEmpty() && editProfileEmail.text.trim().length < 9){
            editProfileEmail.error =  "Input Email Correct"
            valid = false
        }


        return valid
    }

}