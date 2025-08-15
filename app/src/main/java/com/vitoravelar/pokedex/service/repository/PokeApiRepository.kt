package com.vitoravelar.pokedex.service.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.vitoravelar.pokedex.feature.model.AbilityItem
import com.vitoravelar.pokedex.feature.model.PokemonDetail
import com.vitoravelar.pokedex.feature.model.PokemonDetailEntity
import com.vitoravelar.pokedex.feature.model.PokemonItem
import com.vitoravelar.pokedex.feature.model.TypeItem
import com.vitoravelar.pokedex.service.database.PokeDatabase
import com.vitoravelar.pokedex.service.remote.PokeApiService
import com.vitoravelar.pokedex.service.remote.RetrofitConfig

class PokeApiRepository private constructor(context: Context) {

    private val service: PokeApiService = RetrofitConfig.getService(PokeApiService::class.java)
    private val database = PokeDatabase.getDatabase(context).pokeDao()

    companion object {
        @Volatile
        private var INSTANCE: PokeApiRepository? = null

        fun getInstance(context: Context): PokeApiRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: PokeApiRepository(context.applicationContext).also {
                    INSTANCE = it
                }
            }
        }
    }

    suspend fun addFavorite(pokemon: PokemonDetailEntity) = database.insert(pokemon)

    suspend fun removeFavorite(pokemon: PokemonDetailEntity) = database.delete(pokemon)

    fun getFavoriteList(): LiveData<List<PokemonDetailEntity>> = database.getAllPokemons()

    suspend fun getPokemonList(limit: Int = 20, offset: Int = 0): List<PokemonItem> {
        return try {
            val result = service.getAllPokemons(limit, offset)
            if (result.isSuccessful) result.body()?.results ?: emptyList()
            else emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun getPokemonByIdOrName(idOrName: String): PokemonDetail? {
        return try {
            val result = service.getPokemonByIdOrName(idOrName)
            if (result.isSuccessful) result.body()
            else null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getTypePokemon(): List<TypeItem> {
        return try {
            val result = service.getTypePokemon()
            if (result.isSuccessful) result.body()?.results ?: emptyList()
            else emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun getAbility(): List<AbilityItem> {
        return try {
            val result = service.getAbility()
            if (result.isSuccessful) result.body()?.results ?: emptyList()
            else emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}