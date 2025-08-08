package com.vitoravelar.pokedex.service.remote

import com.vitoravelar.pokedex.feature.model.Pokemon
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface PokeService {

    @GET("/pokemon")
    @Headers("Content-type: application/json")
    suspend fun getAllPokemons(): Response<List<Pokemon>>

    @GET("/pokemon/{id}")
    @Headers("Content-type: application/json")
    suspend fun getPokemonById(@Path(value = "id") id: Int): Response<Pokemon>

    @GET("/pokemon/{name}")
    @Headers("Content-type: application/json")
    suspend fun getPokemonByName(@Path(value = "name") name: String): Response<Pokemon>

    @GET("/type}")
    @Headers("Content-type: application/json")
    suspend fun getTypePokemon() // : Response<Type>

    @GET("/ability}")
    @Headers("Content-type: application/json")
    suspend fun getAbility() // : Response<Ability>


}