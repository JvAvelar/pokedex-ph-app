package com.vitoravelar.pokedex.service.remote

import com.vitoravelar.pokedex.feature.model.PokemonResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeService {

    @GET("pokemon")
    @Headers("Content-type: application/json")
    suspend fun getAllPokemons(
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0
    ): Response<List<PokemonResponse>>

    @GET("pokemon/{idOrName}")
    @Headers("Content-type: application/json")
    suspend fun getPokemonById(@Path(value = "idOrName") idOrName: Int): Response<PokemonResponse> // Response<PokemonDetail>


    @GET("type}")
    @Headers("Content-type: application/json")
    suspend fun getTypePokemon() // : Response<Type>

    @GET("ability}")
    @Headers("Content-type: application/json")
    suspend fun getAbility() // : Response<Ability>


}