package com.example.meet_app.auth

data class RegisterRequest(
    val username: String,
    val password: String,
    val firstName: String,
    val lastName: String
)

data class LoginRequest(
    val username: String,
    val password: String

)

