package com.example.meet_app.api.user

import androidx.compose.ui.graphics.painter.Painter
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserResponse(
    val username: String,
    val firstName: String,
    val lastName: String,
    var endpointId: String? = null,
    val profileImage: Painter? = null,
    @PrimaryKey(autoGenerate = false)
    val id: String,
)
