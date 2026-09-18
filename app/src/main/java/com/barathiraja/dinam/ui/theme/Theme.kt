package com.barathiraja.dinam.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = DinamColors.Primary,
    secondary = DinamColors.Primary,
    tertiary = DinamColors.Primary,

    background = DinamColors.Surface,
    surface = DinamColors.Surface,

    onPrimary = DinamColors.Surface,
    onSecondary = DinamColors.Surface,
    onTertiary = DinamColors.Surface,

    onBackground = DinamColors.TextPrimary,
    onSurface = DinamColors.TextPrimary
)

private val DarkColorScheme = darkColorScheme(
    primary = DinamColors.Primary,
    secondary = DinamColors.Primary,
    tertiary = DinamColors.Primary,

    background = DinamColors.TextPrimary,
    surface = DinamColors.TextPrimary,

    onPrimary = DinamColors.Surface,
    onSecondary = DinamColors.Surface,
    onTertiary = DinamColors.Surface,

    onBackground = DinamColors.Surface,
    onSurface = DinamColors.Surface
)

@Composable
fun DinamTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {

    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}