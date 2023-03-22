package com.example.meet_app.api.user

import retrofit2.http.GET

interface UserApi {

    @GET("secret")
    suspend fun getSecretInfo(): UserResponse


}