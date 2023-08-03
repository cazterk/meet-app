package com.example.meet_app.api.user

import android.content.ContentValues.TAG
import android.util.Log
import com.example.meet_app.roomDb.dao.UserDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userDao: UserDao,
    private val userApi: UserApi

) {


    suspend fun getCurrentUser(): UserEntity {
        return withContext(Dispatchers.IO) {
            userDao.getUser()
        } ?: return userApi.getSecretInfo()

    }

    suspend fun insertUser() {
        try {
            // Fetch data from the API
            val user = userApi.getSecretInfo()
            withContext(Dispatchers.IO) {
                userDao.insertUser(user)
                Log.d(TAG, " $user")
            }

        } catch (e: Exception) {
            Log.d(TAG, "${e.message}")
        }


    }

    suspend fun getUsers(): List<UserEntity> {
        return userApi.getUsers()
    }

    suspend fun deleteUser() {
        userDao.deleteUser()
    }
}