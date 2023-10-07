package com.example.meet_app.api.user

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson

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
    var profileImage: String?,
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


class UserConverter{
    private val  gson = Gson()

    @TypeConverter
    fun userEntityToJson(userEntity: UserEntity): String{
        return gson.toJson(userEntity)
    }

    @TypeConverter
    fun jsonToUserEntity(json:String): UserEntity{
        return gson.fromJson(json, UserEntity::class.java)
    }
}