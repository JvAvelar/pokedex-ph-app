package com.vitoravelar.pokedex.feature.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon_detail")
data class DetailFavoritesEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("pokemonId")
    val pokemonId: Int,

    @ColumnInfo("pokemonName")
    val pokemonName: String,

    @ColumnInfo("hp")
    val hp: Int,

    @ColumnInfo("attack")
    val attack: Int,

    @ColumnInfo("defense")
    val defense: Int,

    @ColumnInfo("specialAttack")
    val specialAttack: Int,

    @ColumnInfo("specialDefense")
    val specialDefense: Int,

    @ColumnInfo("speed")
    val speed: Int,

    @ColumnInfo("skills")
    val skills: String
)
