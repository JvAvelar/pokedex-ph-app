package com.vitoravelar.pokedex.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.vitoravelar.pokedex.R

object PokemonStatsColor {

    private val stats = mapOf(
        "hp" to  R.color.grass,
        "attack" to R.color.fire,
        "defense" to R.color.water,
        "special-attack" to R.color.fire,
        "special-defense" to R.color.water,
        "speed" to R.color.eletric
    )

    @Composable
    fun getColor(name: String): Color {
        val resId = stats[name.lowercase()] ?:  android.R.color.white
        return colorResource(resId)
    }
}