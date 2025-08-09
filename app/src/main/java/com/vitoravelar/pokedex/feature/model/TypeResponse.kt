package com.vitoravelar.pokedex.feature.model

data class TypeResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<TypeItem>
)

data class TypeItem(
    val name: String,
    val url: String
)

data class PokemonTypeSlot(
    val type: TypeInfo
)

data class TypeInfo(
    val name: String
)
