package com.blueprintcompanion.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Ink = Color(0xFF1C2834)
private val Paper = Color(0xFFF4F1EA)
private val Blueprint = Color(0xFF1F4E79)

private val LightColors = lightColorScheme(
    primary = Blueprint,
    onPrimary = Color.White,
    secondary = Color(0xFF8A6232),
    onSecondary = Color.White,
    background = Paper,
    surface = Color(0xFFFFFCF7),
    onBackground = Ink,
    onSurface = Ink
)

@Composable
fun BlueprintTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        content = content
    )
}
