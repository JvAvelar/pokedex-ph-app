package com.vitoravelar.pokedex.feature.model

data class TypeDetailResponse(
    val id: Int,
    val name: String,
    val pokemon: List<TypePokemonEntry>
)

data class TypePokemonEntry(
    val pokemon: PokemonResource,
    val slot: Int
)


data class PokemonResource(
    val name: String,
    val url: String
)