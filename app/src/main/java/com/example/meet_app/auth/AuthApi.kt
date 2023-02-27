package com.example.meet_app.auth

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApi {

    @POST("signup")
    suspend fun signUp(
        @Body request: RegisterRequest
    )

    @POST("signin")
    suspend fun signIn(
        @Body request: LoginRequest
    ):TokenResponse

    @GET("authenticate")
    suspend fun authenticate(
        @Header("Authorization") token: String
    )
}


