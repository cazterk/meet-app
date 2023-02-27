package com.example.meet_app.auth

import android.content.SharedPreferences
import retrofit2.HttpException


class AuthRepositoryImpl(
    private val api: AuthApi,
    private val prefs: SharedPreferences
) : AuthRepository {
    override suspend fun signUp(
        username: String,
        password: String,
        firstName: String,
        lastName: String
    ): AuthResult<Unit> {
        return try {
            api.signUp(
                request = RegisterRequest(
                    username = username,
                    password = password,
                    firstName = firstName,
                    lastName = lastName
                )
            )
            signIn(username, password)
        } catch (e: HttpException) {
            if (e.code() == 401) {
                AuthResult.Unauthorized()
            } else {
                AuthResult.UnknownError()
            }
        } catch (e: Exception) {
            AuthResult.UnknownError()
        }
    }

    override suspend fun signIn(username: String, password: String): AuthResult<Unit> {
        return try {
            val response = api.signIn(
                request = LoginRequest(
                    username = username,
                    password = password
                )
            )
            prefs.edit()
                .putString("jwt", response.token)
                .apply()
            AuthResult.Authorized()
        } catch (e: HttpException) {
            if (e.code() == 401) {
                AuthResult.Unauthorized()
            } else {
                AuthResult.UnknownError()
            }
        } catch (e: Exception) {
            AuthResult.UnknownError()
        }
    }

    override suspend fun authenticate(): AuthResult<Unit> {
        return try {
            val token = prefs.getString("jwt", null) ?: return AuthResult.Unauthorized()
            api.authenticate("Bearer $token")
            AuthResult.Authorized()
        } catch (e: HttpException) {
            if (e.code() == 401) {
                AuthResult.Unauthorized()
            } else {
                AuthResult.UnknownError()
            }
        } catch (e: Exception) {
            AuthResult.UnknownError()
        }
    }
}