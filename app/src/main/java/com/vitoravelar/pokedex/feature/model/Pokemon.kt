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

data class PokemonDetail(
    val id: Int,
    val name: String,
    val sprites: Sprites,
    val types: List<PokemonTypeSlot>,
    val stats: List<PokemonStat>,
    val abilities: List<PokemonAbility>
)

data class Sprites(
    val other: OtherSprites
)

data class OtherSprites(
    @SerializedName("official-artwork")
    val officialArtwork: OfficialArtwork
)

data class OfficialArtwork(
    @SerializedName("front_default")
    val imageUrl: String?
)

data class PokemonTypeSlot(
    val type: TypeInfo
)

data class TypeInfo(
    val name: String
)

data class PokemonStat(
    @SerializedName("base_stat")
    val baseStat: Int,
    val stat: StatInfo
)

data class StatInfo(
    val name: String
)

data class PokemonAbility(
    val ability: AbilityInfo
)

data class AbilityInfo(
    val name: String
)