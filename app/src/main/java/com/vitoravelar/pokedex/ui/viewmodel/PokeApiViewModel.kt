package com.vitoravelar.pokedex.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.vitoravelar.pokedex.feature.model.AbilityItem
import com.vitoravelar.pokedex.feature.model.PokemonDetail
import com.vitoravelar.pokedex.feature.model.PokemonDetailEntity
import com.vitoravelar.pokedex.feature.model.PokemonItem
import com.vitoravelar.pokedex.feature.model.TypeItem
import com.vitoravelar.pokedex.service.repository.PokeApiRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PokeApiViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = PokeApiRepository.getInstance(application.applicationContext)

    sealed class UiState<out T> {
        object Loading : UiState<Nothing>()
        data class Success<T>(val data: T) : UiState<T>()
        data class Error(val message: String) : UiState<Nothing>()
    }

    private val _getPokemonList = MutableLiveData<UiState<List<PokemonItem>>>()

    val pokemonList: LiveData<UiState<List<PokemonItem>>> = _getPokemonList

    private val _getPokemonByIdOrName = MutableLiveData<UiState<PokemonDetail?>>()
    val getPokemonByIdOrName: LiveData<UiState<PokemonDetail?>> = _getPokemonByIdOrName

    private val _getType = MutableLiveData<UiState<List<TypeItem>>>()
    val getType: LiveData<UiState<List<TypeItem>>> = _getType

    private val _getAbility = MutableLiveData<UiState<List<AbilityItem>>>()
    val getAbility: LiveData<UiState<List<AbilityItem>>> = _getAbility

    private val _searchText = MutableLiveData("")
    val searchText: LiveData<String> = _searchText

    fun onSearchTextChange(newText: String) {
        _searchText.value = newText
    }

    fun addFavorite(pokemon: PokemonDetailEntity) = viewModelScope.launch { repository.addFavorite(pokemon) }
    fun removeFavorite(pokemon: PokemonDetailEntity) = viewModelScope.launch { repository.removeFavorite(pokemon) }
    fun getAllFavorite() = repository.getFavoriteList()


    fun getPokemonList(limit: Int = 20, offset: Int = 0) {
        viewModelScope.launch {
            _getPokemonList.postValue(UiState.Loading)
            
            try {
                delay(5000)
                _getPokemonList.postValue(UiState.Success(repository.getPokemonList(limit, offset)))
            } catch (e: Exception) {
                _getPokemonList.postValue(UiState.Error(e.message ?: "Erro desconhecido"))
            }
        }
    }

    fun getPokemonByIdOrName(idOrName: String) {
        viewModelScope.launch {
            _getPokemonByIdOrName.postValue(UiState.Loading)
            try {
                _getPokemonByIdOrName.postValue(
                    UiState.Success(repository.getPokemonByIdOrName(idOrName.lowercase().trim())))
            } catch (e: Exception) {
                _getPokemonByIdOrName.postValue(UiState.Error(e.message ?: "Erro desconhecido"))
            }
        }
    }

    fun getTypePokemon() {
        viewModelScope.launch {
            _getType.postValue(UiState.Loading)
            try {
                _getType.postValue(UiState.Success(repository.getTypePokemon()))
            } catch (e: Exception) {
                _getType.postValue(UiState.Error(e.message ?: "Erro desconhecido"))
            }
        }
    }

    fun getAbility() {
        viewModelScope.launch {
            _getAbility.postValue(UiState.Loading)
            try {
                _getAbility.postValue(UiState.Success(repository.getAbility()))
            } catch (e: Exception) {
                _getAbility.postValue(UiState.Error(e.message ?: "Erro desconhecido"))
            }
        }
    }
}