package com.example.carrentall.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = LightBlue,
    onPrimary = TextOnPrimary,
    primaryContainer = PrimaryBlue,
    onPrimaryContainer = VeryLightBlue,
    
    secondary = AccentOrange,
    onSecondary = TextOnPrimary,
    secondaryContainer = AccentAmber,
    onSecondaryContainer = TextOnPrimary,
    
    tertiary = LightBlue,
    onTertiary = TextOnPrimary,
    
    background = DarkGray,
    onBackground = Color.White,
    
    surface = MediumGray,
    onSurface = Color.White,
    surfaceVariant = LightGray,
    onSurfaceVariant = TextOnPrimary,
    
    error = ErrorRed,
    onError = TextOnPrimary,
    
    outline = LightGray,
    outlineVariant = MediumGray
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    onPrimary = TextOnPrimary,
    primaryContainer = VeryLightBlue,
    onPrimaryContainer = DarkBlue,
    
    secondary = AccentOrange,
    onSecondary = TextOnPrimary,
    secondaryContainer = AccentAmber,
    onSecondaryContainer = TextPrimary,
    
    tertiary = LightBlue,
    onTertiary = TextOnPrimary,
    
    background = SurfaceGray,
    onBackground = TextPrimary,
    
    surface = Color.White,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceGray,
    onSurfaceVariant = TextSecondary,
    
    error = ErrorRed,
    onError = TextOnPrimary,
    
    outline = LightGray,
    outlineVariant = MediumGray
)

@Composable
fun CarRentallTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false, // Disabled to use custom dark blue theme
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
    
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}