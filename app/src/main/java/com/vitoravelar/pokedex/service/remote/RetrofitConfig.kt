package com.vitoravelar.pokedex.service.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitConfig {

    private const val URL = "https://pokeapi.co/api/v2/"

    private fun getInstance() : Retrofit{
        return Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun <T> getService(serviceClass: Class<T>): T {
        return RetrofitConfig.getInstance().create(serviceClass)
    }
}