package com.example.meet_app.auth


import android.content.SharedPreferences
import com.example.meet_app.api.user.UserApi
import com.example.meet_app.roomDb.dao.UserDao
import retrofit2.HttpException


class AuthRepositoryImpl(
    private val api: AuthApi,
    private val userApi: UserApi,
    private val prefs: SharedPreferences,
    private val userDao: UserDao

//    private val context: Context
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
//                userDao.deleteUser()
                AuthResult.Unauthorized()
            } else {
                AuthResult.UnknownError(e.message)
            }
        } catch (e: Exception) {
            AuthResult.UnknownError(e.message)
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
                AuthResult.UnknownError(e.message)
            }
        } catch (e: Exception) {
            AuthResult.UnknownError(e.message)
        }
    }

    override suspend fun authenticate(): AuthResult<Unit> {
        return try {
            val token = prefs.getString("jwt", null) ?: return AuthResult.Unauthorized()
            api.authenticate("Bearer $token")
            val user = userApi.getSecretInfo()
            userDao.insertUser(user)
            println( "user : $user")
            AuthResult.Authorized()
        } catch (e: HttpException) {
            if (e.code() == 401) {
                AuthResult.Unauthorized()
            } else {
                AuthResult.UnknownError(e.message)
            }
        } catch (e: Exception) {
            AuthResult.UnknownError(e.message)
        }
    }

    override suspend fun logout(): AuthResult<Unit> {
        return try {
            prefs.edit().remove("jwt").apply()
            userDao.deleteUser()
            AuthResult.Unauthorized()
        } catch (e: Exception) {
            AuthResult.UnknownError(e.message)
        }

    }
}