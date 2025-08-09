package com.vitoravelar.pokedex.feature.model

import com.google.gson.annotations.SerializedName


data class PokemonResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<PokemonItem>
)

data class PokemonItem(
    val name: String,
    val url: String
)

data class PokemonStat(
    @SerializedName("base_stat")
    val baseStat: Int,
    val stat: StatInfo
)

data class StatInfo(
    val name: String
)