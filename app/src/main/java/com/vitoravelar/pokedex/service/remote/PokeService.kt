package com.vitoravelar.pokedex.service.remote

import retrofit2.http.GET
import retrofit2.http.Headers

interface PokeService {
    @GET("")
    @Headers("Content-type: application/json")
    suspend fun getAllPokemons()

}