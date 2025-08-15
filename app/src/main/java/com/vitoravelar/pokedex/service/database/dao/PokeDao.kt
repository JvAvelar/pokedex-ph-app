package com.vitoravelar.pokedex.service.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vitoravelar.pokedex.feature.model.PokemonDetailEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PokeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pokemon: PokemonDetailEntity)

    @Delete
    suspend fun delete(pokemon: PokemonDetailEntity)

    @Query("SELECT * FROM pokemon_favorite")
    fun getAllPokemons(): List<PokemonDetailEntity>
}