package com.example.meet_app.auth


interface AuthRepository {
    suspend fun signUp(
        username: String,
        password: String,
        firstName: String,
        lastName: String
    ): AuthResult<Unit>

    suspend fun signIn(username: String, password: String): AuthResult<Unit>
    suspend fun authenticate(): AuthResult<Unit>
    suspend fun logout(): AuthResult<Unit>
}