package com.vitoravelar.pokedex.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.vitoravelar.pokedex.feature.model.PokemonDetail
import com.vitoravelar.pokedex.feature.model.PokemonDetailEntity
import com.vitoravelar.pokedex.feature.model.PokemonItem
import com.vitoravelar.pokedex.service.repository.PokeApiRepository
import kotlinx.coroutines.launch

class PokeApiViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = PokeApiRepository.getInstance(application.applicationContext)

    val msgUnknownError = "Unknown error"
    val pokemonNotFound = "Pokemon não encontrado"

    sealed class UiState<out T> {
        object Loading : UiState<Nothing>()
        data class Success<T>(val data: T) : UiState<T>()
        data class Error(val message: String) : UiState<Nothing>()
    }

    var lastScrollState: Int = 0

    private val _getPokemonList = MutableLiveData<UiState<List<PokemonItem>>>()
    val pokemonList: LiveData<UiState<List<PokemonItem>>> = _getPokemonList

    private val _getPokemonByIdOrName = MutableLiveData<UiState<PokemonDetail?>>()
    val getPokemonByIdOrName: LiveData<UiState<PokemonDetail?>> = _getPokemonByIdOrName

    val listAllFavorite: LiveData<List<PokemonDetailEntity>> = repository.getFavoriteList()

    fun addFavorite(pokemon: PokemonDetailEntity) = viewModelScope.launch {
        repository.addFavorite(pokemon)
    }

    fun removeFavorite(pokemon: PokemonDetailEntity) = viewModelScope.launch {
        repository.removeFavorite(pokemon)
    }

    fun getPokemonList(limit: Int = 20, offset: Int = 0) = viewModelScope.launch {
        _getPokemonList.postValue(UiState.Loading)
        try {
            _getPokemonList.postValue(UiState.Success(repository.getPokemonList(limit, offset)))
        } catch (e: Exception) {
            _getPokemonList.postValue(UiState.Error(e.message ?: msgUnknownError))
        }
    }

    fun getPokemonByIdOrName(idOrName: String) = viewModelScope.launch {
        _getPokemonByIdOrName.postValue(UiState.Loading)
        try {
            val result = repository.getPokemonByIdOrName(idOrName.lowercase().trim())
            _getPokemonByIdOrName.postValue(UiState.Success(result))

        } catch (e: Exception) {
            _getPokemonByIdOrName.postValue(UiState.Error(pokemonNotFound))
        }
    }
}