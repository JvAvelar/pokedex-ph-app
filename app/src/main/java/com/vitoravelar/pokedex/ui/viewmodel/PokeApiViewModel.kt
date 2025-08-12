package com.vitoravelar.pokedex.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vitoravelar.pokedex.feature.model.AbilityItem
import com.vitoravelar.pokedex.feature.model.PokemonDetail
import com.vitoravelar.pokedex.feature.model.PokemonItem
import com.vitoravelar.pokedex.feature.model.TypeItem
import com.vitoravelar.pokedex.service.remote.PokeApiService
import com.vitoravelar.pokedex.service.remote.RetrofitConfig
import com.vitoravelar.pokedex.service.repository.PokeApiRepository
import kotlinx.coroutines.launch

class PokeApiViewModel : ViewModel() {

    private val repository = PokeApiRepository(
        RetrofitConfig.getService(PokeApiService::class.java)
    )

    sealed class UiState<out T> {
        object Loading : UiState<Nothing>()
        data class Success<T>(val data: T) : UiState<T>()
        data class Error(val message: String) : UiState<Nothing>()
    }

    private val _getPokemonList = MutableLiveData<UiState<List<PokemonItem>>>()
    val getPokemonList: LiveData<UiState<List<PokemonItem>>> = _getPokemonList

    private val _getPokemon = MutableLiveData<UiState<PokemonDetail?>>()
    val getPokemon: LiveData<UiState<PokemonDetail?>> = _getPokemon

    private val _getType = MutableLiveData<UiState<List<TypeItem>>>()
    val getType: LiveData<UiState<List<TypeItem>>> = _getType

    private val _getAbility = MutableLiveData<UiState<List<AbilityItem>>>()
    val getAbility: LiveData<UiState<List<AbilityItem>>> = _getAbility


    fun getPokemonList(limit: Int = 20, offset: Int = 0) {
        viewModelScope.launch {
            _getPokemonList.postValue(UiState.Loading)
            try {
                _getPokemonList.postValue(UiState.Success(repository.getPokemonList(limit, offset)))
            } catch (e: Exception){
                _getPokemonList.postValue(UiState.Error(e.message ?: "Erro desconhecido"))
            }
        }
    }

    fun getPokemonByIdOrName(idOrName: String) {
        viewModelScope.launch {
            _getPokemon.postValue(UiState.Loading)
            try {
                _getPokemon.postValue(UiState.Success(repository.getPokemonByIdOrName(idOrName)))
            } catch (e: Exception){
                _getPokemon.postValue(UiState.Error(e.message ?: "Erro desconhecido"))
            }
        }
    }

    fun getTypePokemon() {
        viewModelScope.launch {
       _getType.postValue(UiState.Loading)
            try {
                _getType.postValue(UiState.Success(repository.getTypePokemon()))
            } catch (e: Exception){
                _getType.postValue(UiState.Error(e.message ?: "Erro desconhecido"))
            }
        }
    }

    fun getAbility() {
        viewModelScope.launch {
            _getAbility.postValue(UiState.Loading)
            try {
                _getAbility.postValue(UiState.Success(repository.getAbility()))
            } catch (e: Exception){
                _getAbility.postValue(UiState.Error(e.message ?: "Erro desconhecido"))
            }
        }
    }
}