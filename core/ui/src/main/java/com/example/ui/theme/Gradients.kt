package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

object Gradient {

    val GradientBegin = Color(0xffF3F3F6)
    val GradientEnd = Color(0xFF5883b7)

    val PrimaryGradient = Brush.linearGradient(
        colors = listOf(GradientBegin, GradientEnd)
    )

}