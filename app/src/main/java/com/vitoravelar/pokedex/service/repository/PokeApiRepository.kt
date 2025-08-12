package com.vitoravelar.pokedex.service.repository

import com.vitoravelar.pokedex.feature.model.AbilityItem
import com.vitoravelar.pokedex.feature.model.PokemonDetail
import com.vitoravelar.pokedex.feature.model.PokemonItem
import com.vitoravelar.pokedex.feature.model.TypeItem
import com.vitoravelar.pokedex.service.remote.PokeApiService

class PokeApiRepository(private val service: PokeApiService) {

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