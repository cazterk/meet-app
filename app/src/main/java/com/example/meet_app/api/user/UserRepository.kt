package com.example.meet_app.api.user

class UserRepository(private val userApi: UserApi) {
    suspend fun getCurrentUser(): UserResponse {
        return userApi.getSecretInfo()
    }

    suspend fun getUsers(): List<UserResponse> {
        return userApi.getUsers()
    }
}