package com.vitoravelar.pokedex.service.remote

import com.vitoravelar.pokedex.feature.model.AbilityResponse
import com.vitoravelar.pokedex.feature.model.PokemonDetail
import com.vitoravelar.pokedex.feature.model.PokemonResponse
import com.vitoravelar.pokedex.feature.model.TypeResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeApiService {

    @GET("pokemon")
    @Headers("Content-type: application/json")
    suspend fun getAllPokemons(
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0
    ): Response<PokemonResponse>

    @GET("pokemon/{idOrName}")
    @Headers("Content-type: application/json")
    suspend fun getPokemonByIdOrName(
        @Path(value = "idOrName") idOrName: String
    ): Response<PokemonDetail>

    @GET("type}")
    @Headers("Content-type: application/json")
    suspend fun getTypePokemon(): Response<TypeResponse>

    @GET("ability}")
    @Headers("Content-type: application/json")
    suspend fun getAbility(): Response<AbilityResponse>


}