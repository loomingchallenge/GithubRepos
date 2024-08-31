package com.githubrepos.framework.ui.screens.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColors(
    primary = Purple80,
    secondary = PurpleGrey80,
    background = Pink80
)

private val LightColorScheme = lightColors(
    primary = Purple40,
    secondary = Color.White,
    background = Color.White,
    surface = Color.White,
    error = LightRed,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Gray,
    onSurface = Blue,
    onError = Color.White,
)

@Composable
fun MainTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colors = colorScheme,
        typography = Typography,
        content = content
    )
}
