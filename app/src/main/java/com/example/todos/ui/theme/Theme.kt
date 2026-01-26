package com.example.todos.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = LightBlue,
    secondary = Mint,
    tertiary = LightCoral,
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    surfaceVariant = Color(0xFF2C2C2C),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = CreamWhite,
    onSurface = CreamWhite,
    primaryContainer = DarkTeal,
    onPrimaryContainer = CreamWhite,
    secondaryContainer = Color(0xFF2D5F5D),
    onSecondaryContainer = LightMint,
    error = Coral,
    errorContainer = Color(0xFF5C1A1A),
    onErrorContainer = LightCoral
)

private val LightColorScheme = lightColorScheme(
    primary = OceanBlue,
    secondary = Mint,
    tertiary = Coral,
    background = CreamWhite,
    surface = Color.White,
    surfaceVariant = PaleBlue,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1A1A1A),
    onSurface = Color(0xFF1A1A1A),
    primaryContainer = SkyBlue,
    onPrimaryContainer = DarkBlue,
    secondaryContainer = LightMint,
    onSecondaryContainer = Color(0xFF004D47),
    tertiaryContainer = PeachPink,
    onTertiaryContainer = Color(0xFF8B0000),
    error = Coral,
    errorContainer = PeachPink,
    onErrorContainer = Color(0xFF8B0000)
)

@Composable
fun TodosTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false, // Disabled to use our custom colors
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}