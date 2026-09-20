package com.barathiraja.dinam.ui.theme

import androidx.compose.material3.MaterialTheme
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

@Composable
fun DinamTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}