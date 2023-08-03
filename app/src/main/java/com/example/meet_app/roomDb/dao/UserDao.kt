package com.example.meet_app.roomDb.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.meet_app.api.user.UserEntity

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Query("SELECT * FROM current_user LIMIT 1")
    fun getUser(): UserEntity?

    @Query("DELETE  FROM current_user")
    suspend fun deleteUser()
}

