package com.example.meet_app

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight

val fonts = FontFamily(
    Font(R.font.now_light, weight = FontWeight.Light),
    Font(R.font.now_thin, weight = FontWeight.Thin),
    Font(R.font.now_regular, weight = FontWeight.Normal),
    Font(R.font.now_medium, weight = FontWeight.Medium),
    Font(R.font.now_bold, weight = FontWeight.Bold)

    )


@JvmName("getFonts1")
fun getFonts(): FontFamily {
     return  fonts
}