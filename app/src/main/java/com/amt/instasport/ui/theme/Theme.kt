package com.amt.instasport.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val lightColorScheme = lightColorScheme(
    primary = Lavender,
    onPrimary = White,
    background = White,
    secondary = SecondPurple,
    onSecondary = Black
)

@Composable
fun InstaSportTheme(content: @Composable () -> Unit) {
    val colorScheme = lightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}