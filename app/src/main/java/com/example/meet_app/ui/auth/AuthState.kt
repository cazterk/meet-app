package com.example.meet_app.ui.auth

data class AuthState(
    val isLoading: Boolean = false,
    val signUpUsername: String = "",
    val signUpPassword: String = "",
    val signUpFirstName: String = "",
    val signUpLastName: String = "",
    val signInUsername: String = "",
    val signInPassword: String = "",
)