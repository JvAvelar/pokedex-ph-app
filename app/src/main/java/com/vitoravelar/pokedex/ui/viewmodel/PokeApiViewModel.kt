package com.vitoravelar.pokedex.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.vitoravelar.pokedex.feature.model.PokemonDetail
import com.vitoravelar.pokedex.feature.model.PokemonDetailEntity
import com.vitoravelar.pokedex.feature.model.PokemonItem
import com.vitoravelar.pokedex.feature.model.TypeDetailResponse
import com.vitoravelar.pokedex.feature.model.TypeItem
import com.vitoravelar.pokedex.service.repository.PokeApiRepository
import kotlinx.coroutines.Job
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

    private val _pokemonListState = MutableLiveData<UiState<List<PokemonItem>>>()
    val pokemonListState: LiveData<UiState<List<PokemonItem>>> = _pokemonListState

    private val currentPokemonList = mutableListOf<PokemonItem>()
    private var currentOffset = 0
    private val pageSize = 20
    private var isLoadingMore = false
    var canLoadMore = true
    private var currentLoadingJob: Job? = null


    private val _pokemonTypesState = MutableLiveData<UiState<List<TypeItem>>>()
    val pokemonTypesState: LiveData<UiState<List<TypeItem>>> = _pokemonTypesState

    private val _selectedTypeFilter = MutableLiveData<TypeItem?>()
    val selectedTypeFilter: LiveData<TypeItem?> = _selectedTypeFilter


    private val _getPokemonByIdOrName = MutableLiveData<UiState<PokemonDetail?>>()
    val getPokemonByIdOrName: LiveData<UiState<PokemonDetail?>> = _getPokemonByIdOrName

    val listAllFavorite: LiveData<List<PokemonDetailEntity>> = repository.getFavoriteList()

    init {
        loadInitialPokemon()
    }

    fun loadInitialPokemon() {
        currentOffset = 0
        currentPokemonList.clear()
        canLoadMore = true
        fetchPokemonList()
    }

    fun loadMorePokemon() {
        if (isLoadingMore || !canLoadMore) {
            return
        }
        fetchPokemonList()
    }

    private var currentFilterTypeJob: Job? = null

    fun fetchPokemonTypes() {
        if (_pokemonTypesState.value is UiState.Success || _pokemonTypesState.value is UiState.Loading) {
            return
        }
        viewModelScope.launch {
            _pokemonTypesState.postValue(UiState.Loading)
            try {
                val typeResponse = repository.getPokemonTypes()
                _pokemonTypesState.postValue(UiState.Success(typeResponse.results))
            } catch (e: Exception) {
                _pokemonTypesState.postValue(UiState.Error(e.message ?: msgUnknownError))
            }
        }
    }

    fun setTypeFilter(type: TypeItem?) {
        _selectedTypeFilter.value = type

        currentLoadingJob?.cancel()
        currentFilterTypeJob?.cancel()

        if (type != null) {
            loadPokemonByType(type.name)
        } else {
            loadInitialPokemon()
        }
    }

    private fun loadPokemonByType(typeName: String) {
        isLoadingMore = true
        _pokemonListState.postValue(UiState.Loading)
        currentFilterTypeJob = viewModelScope.launch {
            try {
                val typeDetails: TypeDetailResponse = repository.getPokemonTypesDetails(typeName)

                val filteredPokemonItems = typeDetails.pokemon.map { typePokemonEntry ->
                    PokemonItem(
                        name = typePokemonEntry.pokemon.name,
                        url = typePokemonEntry.pokemon.url
                    )
                }

                currentPokemonList.clear()
                currentPokemonList.addAll(filteredPokemonItems)
                _pokemonListState.postValue(UiState.Success(ArrayList(currentPokemonList))) // Notifica a HomeScreen

                canLoadMore = false
                currentOffset = 0

            } catch (e: Exception) {
                _pokemonListState.postValue(
                    UiState.Error(
                        e.message ?: "Erro ao carregar Pokémon por tipo: $typeName"
                    )
                )
            } finally {
                isLoadingMore = false
            }
        }
    }

    private fun fetchPokemonList() {
        currentLoadingJob?.cancel()
        currentLoadingJob = viewModelScope.launch {
            isLoadingMore = true

            if (currentOffset == 0 && currentPokemonList.isEmpty()) {
                _pokemonListState.postValue(UiState.Loading)
            }

            try {
                val newPokemon = repository.getPokemonList(limit = pageSize, offset = currentOffset)
                if (newPokemon.isNotEmpty()) {
                    currentPokemonList.addAll(newPokemon)
                    _pokemonListState.postValue(UiState.Success(ArrayList(currentPokemonList)))
                    currentOffset += pageSize
                    canLoadMore = newPokemon.size == pageSize
                } else {
                    canLoadMore = false
                    if (currentPokemonList.isEmpty()) {
                        _pokemonListState.postValue(UiState.Success(emptyList()))
                    }
                }
            } catch (e: Exception) {
                if (currentOffset == 0) {
                    _pokemonListState.postValue(UiState.Error(e.message ?: msgUnknownError))
                }

                canLoadMore = false
            } finally {
                isLoadingMore = false
            }
        }
    }

    fun addFavorite(pokemon: PokemonDetailEntity) = viewModelScope.launch {
        repository.addFavorite(pokemon)
    }

    fun removeFavorite(pokemon: PokemonDetailEntity) = viewModelScope.launch {
        repository.removeFavorite(pokemon)
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
