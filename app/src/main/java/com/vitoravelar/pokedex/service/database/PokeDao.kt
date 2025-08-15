package com.vitoravelar.pokedex.service.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vitoravelar.pokedex.feature.model.PokemonDetailEntity

@Dao
interface PokeDao {
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insert(pokemon: PokemonDetailEntity)

    @Delete
    suspend fun delete(pokemon: PokemonDetailEntity)

    @Query("SELECT * FROM pokemon_favorite")
    fun getAllPokemons(): LiveData<List<PokemonDetailEntity>>
}