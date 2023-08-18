package com.example.meet_app.api.user

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "current_user")
data class UserEntity (
    val username: String,
    val firstName: String,
    val lastName: String,
    var endpointId: String? = null,
    val profileImage: String?,
    @PrimaryKey(autoGenerate = false)
    val id: String,
)

