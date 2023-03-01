package com.example.meet_app.auth

sealed class AuthResult<T>(val data: T? = null, val message: String? = null) {
    class Authorized<T>(data: T? = null) : AuthResult<T>(data)
    class Unauthorized<T> : AuthResult<T>()
    class UnknownError<T>(message: String?, data: T? = null) : AuthResult<T>(data, message)

}
