package com.kamal.sahl_notes.models

import kotlinx.serialization.Serializable

@Serializable
data class User(val id : String, val name : String,  val email: String
                , val mobile: String, var token: String?, var password: String?) {
    fun response(): UserResponse? {
        return UserResponse(id,name,email,mobile,token)
    }
}

@Serializable
data class UserResponse(val id : String, val name : String,  val email: String
                , val mobile: String, var token: String?)

@Serializable
data class LoginRequest(val email: String
                ,var password: String?)


@Serializable
data class RegisterRequest( val name : String,  val email: String
                , val mobile: String, var password: String?)