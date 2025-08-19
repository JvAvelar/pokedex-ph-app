package com.vitoravelar.pokedex.service.remote

import com.vitoravelar.pokedex.feature.model.PokemonDetail
import com.vitoravelar.pokedex.feature.model.PokemonResponse
import com.vitoravelar.pokedex.feature.model.TypeDetailResponse
import com.vitoravelar.pokedex.feature.model.TypeResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeApiService {

    @GET("pokemon")
    suspend fun getAllPokemons(
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0
    ): Response<PokemonResponse>

    @GET("pokemon/{idOrName}")
    suspend fun getPokemonByIdOrName(
        @Path(value = "idOrName") idOrName: String
    ): Response<PokemonDetail>

    @GET("type")
    suspend fun getPokemonTypes(@Query("limit") limit: Int = 40): Response<TypeResponse>

    @GET("type/{name}")
    suspend fun getPokemonTypeDetails(@Path("name") typeName: String): Response<TypeDetailResponse>

}