package com.example.meet_app.api.user

import androidx.compose.ui.graphics.painter.Painter
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson

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

class PainterTypeConverter{
    private val gson = Gson()

    @TypeConverter
    fun from(painter: Painter): String{
        return gson.toJson(painter, String::class.java)
    }

    @TypeConverter
    fun to(jsonString: String): Painter{
        return gson.fromJson(jsonString, Painter::class.java)
    }
}