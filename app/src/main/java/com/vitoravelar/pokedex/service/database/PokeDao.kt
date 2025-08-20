package com.vitoravelar.pokedex.service.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vitoravelar.pokedex.feature.model.DetailFavoritesEntity
import com.vitoravelar.pokedex.feature.model.PokemonDetailEntity

@Dao
interface PokeDao {
    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insert(pokemon: PokemonDetailEntity)

    @Delete
    suspend fun delete(pokemon: PokemonDetailEntity)

    @Query("SELECT * FROM pokemon_favorite")
    fun getAllPokemons(): LiveData<List<PokemonDetailEntity>>

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertDetails(details: DetailFavoritesEntity)

    @Delete
    suspend fun deleteDetails(details: DetailFavoritesEntity)

    @Query("SELECT * FROM pokemon_detail")
    fun getAllDetails(): LiveData<List<DetailFavoritesEntity>>
}