package com.example.meet_app.api.user

import androidx.room.Dao
import androidx.room.Upsert
import retrofit2.http.GET

@Dao
interface UserApi {

    @GET("secret")
    @Upsert
    suspend fun getSecretInfo(): UserResponse

    @GET("users")
    suspend fun getUsers(): List<UserResponse>


}