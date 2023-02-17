package com.example.meet_app.navigation

sealed class Screen(val route: String){
    object Login : Screen("login")
    object Register : Screen("register")
    object ForgotPassword : Screen("forgot_password")
    object Home : Screen("home")
    object Profile : Screen("profile")
    object Message : Screen("message")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            append("/$args")

        }
    }
}
