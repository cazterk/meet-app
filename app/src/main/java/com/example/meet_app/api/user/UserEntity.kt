package com.example.meet_app.api.user

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

open class User(
    open val username: String,
    open val firstName: String,
    open val lastName: String,
    open val id: String,
)

@Entity(tableName = "current_user")
data class UserEntity(
    override val username: String,
    override val firstName: String,
    override val lastName: String,
    val profileImage: String?,
    @PrimaryKey(autoGenerate = false)
    override val id: String
) : User(username, firstName, lastName, id)

data class ConnectionEntity(
    override val username: String,
    override val firstName: String,
    override val lastName: String,
    val connectionProfileImage: Bitmap?,
    override val id: String,

    ) : User(username, firstName, lastName, id)

