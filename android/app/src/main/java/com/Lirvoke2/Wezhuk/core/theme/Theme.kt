package com.Lirvoke2.Wezhuk.core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val CoinStrikeScheme = darkColorScheme(
    primary = AccentGold,
    onPrimary = SurfaceDeep,
    primaryContainer = AccentDeep,
    onPrimaryContainer = SurfaceDeep,
    secondary = SecondaryAqua,
    onSecondary = SurfaceDeep,
    secondaryContainer = PrimaryTeal,
    onSecondaryContainer = TextPrimary,
    tertiary = PrimaryTeal,
    onTertiary = TextPrimary,
    background = BackgroundDeep,
    onBackground = TextPrimary,
    surface = SurfaceCard,
    onSurface = TextPrimary,
    surfaceVariant = BackgroundLifted,
    onSurfaceVariant = TextSecondary,
    outline = SecondaryAqua,
    outlineVariant = PrimaryTeal,
    error = WarnMiss,
    onError = SurfaceDeep,
    scrim = BackgroundDarker
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = CoinStrikeScheme,
        typography = AppTypography,
        content = content
    )
}
