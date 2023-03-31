package com.example.meet_app.api.user

import androidx.compose.ui.graphics.painter.Painter

data class UserResponse(
    val username: String,
    val firstName: String,
    val lastName: String,
    val id: String,
    val deviceName: String? = null,
    val deviceId: String? = null,
    val profileImage: Painter? = null
)
