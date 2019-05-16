package com.example.growup.data.user.model

import java.io.Serializable

class User(
    var name: String = "",
    var lastName: String = "",
    var phoneNumber: String = "",
    var email: String = "",
    var password: String = "",
    var userType: String = "",
    var region: String = "",
    var profileImage: String = "null"

):Serializable