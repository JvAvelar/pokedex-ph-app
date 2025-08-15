package com.vitoravelar.pokedex.feature.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon_favorite")
data class PokemonDetailEntity(

    @PrimaryKey()
    @ColumnInfo("id")
    val id: Int,

    @ColumnInfo("name")
    val name: String,

    @ColumnInfo("imageUrl")
    val imageUrl: String,

    @ColumnInfo("types")
    val types: String
)
