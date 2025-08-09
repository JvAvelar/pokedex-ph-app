package com.vitoravelar.pokedex.feature.model

data class AbilityResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<AbilityItem>
)

data class AbilityItem(
    val name: String,
    val url: String
)

data class PokemonAbility(
    val ability: AbilityInfo
)

data class AbilityInfo(
    val name: String
)