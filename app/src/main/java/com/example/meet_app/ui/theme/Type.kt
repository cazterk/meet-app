package com.example.meet_app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.meet_app.R

// Set of Material typography styles to start with
val Typography = Typography(
    bodyMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    )
    /* Other default text styles to override
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
    */
)

// custom font styles
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