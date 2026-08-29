package com.gemidospremium.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val GemidosColors = darkColorScheme(
    primary = Color(0xFFE7C978),
    onPrimary = Color(0xFF2D2410),
    primaryContainer = Color(0xFF3D3320),
    onPrimaryContainer = Color(0xFFF4E2A7),
    secondary = Color(0xFFB5798D),
    onSecondary = Color(0xFF26171C),
    background = Color(0xFF0B0B0D),
    onBackground = Color(0xFFF2F3F5),
    surface = Color(0xFF181317),
    onSurface = Color(0xFFF2F3F5),
    surfaceVariant = Color(0xFF282126),
    onSurfaceVariant = Color(0xFFC3B8BE),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
)

@Composable
fun GemidosPremiumTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = GemidosColors,
        content = content,
    )
}
