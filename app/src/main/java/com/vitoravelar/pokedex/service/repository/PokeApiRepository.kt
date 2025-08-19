package com.vitoravelar.pokedex.service.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.vitoravelar.pokedex.feature.model.PokemonDetail
import com.vitoravelar.pokedex.feature.model.PokemonDetailEntity
import com.vitoravelar.pokedex.feature.model.PokemonItem
import com.vitoravelar.pokedex.feature.model.TypeDetailResponse
import com.vitoravelar.pokedex.feature.model.TypeResponse
import com.vitoravelar.pokedex.service.database.PokeDatabase
import com.vitoravelar.pokedex.service.remote.PokeApiService
import com.vitoravelar.pokedex.service.remote.RetrofitConfig
import okio.IOException

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
            val response = service.getAllPokemons(limit, offset)
            if (response.isSuccessful)
                response.body()?.results
                    ?: throw IOException("Response body is null (successful response)")
            else
                throw IOException("Failed ${response.code()} - ${response.message()}")
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun getPokemonByIdOrName(idOrName: String): PokemonDetail {
        return try {
            val response = service.getPokemonByIdOrName(idOrName)
            if (response.isSuccessful)
                response.body()
                    ?: throw IOException("Response body is null (successful response)")
            else
                throw IOException("Failed ${response.code()} - ${response.message()}")
        } catch (e: Exception) {
            throw e
        }
    }


    suspend fun getPokemonTypes(): TypeResponse {
        return try {
            val response = service.getPokemonTypes()
            if (response.isSuccessful) {
                response.body()
                    ?: throw IOException("Response body is null  (successful response)")
            } else {
                throw IOException("Failed ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun getPokemonTypesDetails(typeName: String): TypeDetailResponse {
        return try {
            val response = service.getPokemonTypeDetails(typeName.lowercase())
            if (response.isSuccessful) {
                response.body()
                    ?: throw IOException("Response body is null for type details request: $typeName (successful response)")
            } else {
                throw IOException("Failed $typeName: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            throw e
        }
    }
}