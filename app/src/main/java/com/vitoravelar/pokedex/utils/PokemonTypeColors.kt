package com.vitoravelar.pokedex.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.vitoravelar.pokedex.R

object PokemonTypeColors {
    private val typeMap = mapOf(
        "water" to R.color.water,
        "fire" to R.color.fire,
        "grass" to R.color.grass,
        "electric" to R.color.eletric,
        "ground" to R.color.ground,
        "poison" to R.color.poison,
        "normal" to R.color.normal,
        "flying" to R.color.flying,
        "fighting" to R.color.fighting,
        "ice" to R.color.ice,
        "ghost" to R.color.ghost,
        "rock" to R.color.rock,
        "bug" to R.color.bug,
        "dragon" to R.color.dragon,
        "psychic" to R.color.psychic
    )

    @Composable
    fun getColor(typeName: String): Color {
        val resId = typeMap[typeName.lowercase()] ?: android.R.color.white
        return colorResource(resId)
    }
}