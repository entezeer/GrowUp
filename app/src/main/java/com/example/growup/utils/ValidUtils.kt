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
        password: String,
        passwordEditText: EditText
    ): Boolean {
        val validPassword = password.length >= 8
        if (!validPassword) {
            passwordEditText.error = "Invalid Password"
            passwordEditText.requestFocus()
        }

        return validPassword
    }

    fun checkRegisterInputData(
        name: EditText,
        surname: EditText,
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

        if (phone.text.trim().isEmpty() && phone.text.trim().length < 9){
            phone.error =  "Input Phone Correct"
            valid = false
        }
        if (password.text.trim().isEmpty() && password.text.trim().length < 9){
            password.error =  "Input Password Correct"
            valid = false
        }
        if (radioGroup.checkedRadioButtonId == -1){
            valid = false
            Toast.makeText(context,"Выбирете тип пользователя",Toast.LENGTH_SHORT).show()
        }
        return valid
    }

    fun checkEditProfileChanges(
        editProfileName: EditText?,
        editProfileSurname: EditText?
    ): Boolean {
        var valid = true
        if(editProfileName?.text?.trim()!!.isEmpty() && editProfileName.text.trim().length < 2){
            editProfileName.error =  "Input Name Correct"
            valid = false
        }
        if(editProfileSurname?.text?.trim()!!.isEmpty() && editProfileSurname.text.trim().length < 2){
            editProfileSurname.error =  "Input Surname Correct"
            valid = false
        }
        return valid
    }

    fun checkAddProductData(
        name: EditText,
        unitPrice: EditText,
        size: EditText,
        totalPrice: EditText,
        message: EditText
    ): Boolean {
        var valid = true
        if (name.text.trim().isEmpty() && name.text.trim().length < 4){
            name.error =  "Input Product Name Correct"
            valid = false
        }
        if (message.text.trim().isEmpty() && message.text.trim().length < 4){
            message.error =  "Input Message Correct"
            valid = false
        }
        if(unitPrice.text.trim().isEmpty() ){
            unitPrice.error =  "Input Unit Price Correct"
            valid = false
        }
        if(size.text.trim().isEmpty() ){
            size.error =  "Input Size Correct"
            valid = false
        }
        if(totalPrice.text.trim().isEmpty() ){
            totalPrice.error =  "Input Total Price Correct"
            valid = false
        }

        return valid
    }

}