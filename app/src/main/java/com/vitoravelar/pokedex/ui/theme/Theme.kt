package com.vitoravelar.pokedex.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


val PokemonBlue = Color(0xFF3B4CCA)
val PokemonRed = Color(0xFFFF0000)
val PokemonYellow = Color(0xFFFFDE00)
val PokemonLightBlue = Color(0xFF6C79DB)

private val LightColorScheme = lightColorScheme(
    primary = PokemonBlue,
    secondary = PokemonRed,
    tertiary = PokemonYellow,
    background = Color(0xFFF5F5F5),
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color(0xFF1A1A1A),
    onSurface = Color(0xFF1A1A1A),
)

// Cores para modo escuro
private val DarkColorScheme = darkColorScheme(
    primary = PokemonLightBlue,
    secondary = Color(0xFFFF6B6B),
    tertiary = Color(0xFFFFE066),
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White,
)


@Composable
fun PokedexphappTheme(
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