package com.example.meet_app.api.user

import com.example.meet_app.roomDb.dao.UserDao
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userDao: UserDao,
    private val userApi: UserApi
) {


    suspend fun getCurrentUser(): UserEntity {
        try {
            val user = userApi.getSecretInfo()
            userDao.insertUser(user)
            return user

        } catch (e: Exception) {
            // handle exception
            throw  e
        }
    }

    suspend fun getUsers(): List<UserEntity> {
        return userApi.getUsers()
    }
}