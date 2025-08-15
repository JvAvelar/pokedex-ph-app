package com.vitoravelar.pokedex.ui.screen

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.vitoravelar.pokedex.R
import com.vitoravelar.pokedex.feature.model.PokemonItem
import com.vitoravelar.pokedex.ui.component.BaseTopAppBar
import com.vitoravelar.pokedex.ui.component.CardError
import com.vitoravelar.pokedex.ui.component.LoadingBar
import com.vitoravelar.pokedex.ui.component.PokemonCard
import com.vitoravelar.pokedex.ui.viewmodel.PokeApiViewModel
import com.vitoravelar.pokedex.utils.isNetworkAvailable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: PokeApiViewModel, navController: NavHostController) {
    val pokemonListState by viewModel.pokemonList.observeAsState(PokeApiViewModel.UiState.Loading)

    val snackbarHostState = remember { SnackbarHostState() }
    val gridState = rememberLazyGridState()

    val context = LocalContext.current
    val messageNoConnection = stringResource(R.string.no_connection)

    LaunchedEffect(Unit) {
        viewModel.getPokemonList(2000)
    }

    LaunchedEffect(Unit) {
        val index = viewModel.lastScrollState
        if (index > 0) gridState.scrollToItem(index)
    }

    Scaffold(
        topBar = {
            BaseTopAppBar(
                title = stringResource(R.string.title_pokemon),
                leftIcon = null,
                rightIcon = Icons.Default.FavoriteBorder,
                onRightIconClick = {
                    viewModel.lastScrollState = gridState.firstVisibleItemIndex
                    navController.navigate("favorite")
                })
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddings ->

        when (pokemonListState) {
            is PokeApiViewModel.UiState.Loading -> {
                LoadingBar(paddings)
            }

            is PokeApiViewModel.UiState.Success -> {
                val listPokemons = (pokemonListState as PokeApiViewModel.UiState.Success).data
                if (isNetworkAvailable(context)) {
                    ContentHomeScreen(
                        paddings, listPokemons, viewModel, snackbarHostState, context, gridState,
                        onClickCard = { pokemonName -> navController.navigate("detail/${pokemonName}") }
                    )
                } else {
                    CoroutineScope(Dispatchers.Default).launch {
                        snackbarHostState.showSnackbar(messageNoConnection)
                    }
                }
            }

            is PokeApiViewModel.UiState.Error -> {
                val message = (pokemonListState as PokeApiViewModel.UiState.Error).message
                CardError(message)
            }
        }
    }
}

@Composable
private fun ContentHomeScreen(
    padding: PaddingValues,
    pokemonList: List<PokemonItem>,
    viewModel: PokeApiViewModel,
    snackbarHostState: SnackbarHostState,
    context: Context,
    gridState: LazyGridState,
    onClickCard: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(horizontal = 8.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SearchBarAndFilter(viewModel)

        val messageNoConnection = stringResource(R.string.no_connection)

        LazyVerticalGrid(
            columns = GridCells.Adaptive(120.dp),
            state = gridState,
            modifier = Modifier.fillMaxSize()
        ) {

            items(pokemonList) { pokemon ->
                val image = getPokemonImageUrl(pokemon)
                PokemonCard(
                    name = pokemon.name,
                    imageUrl = image,
                    onClick = {
                        if (isNetworkAvailable(context)) {
                            viewModel.lastScrollState = gridState.firstVisibleItemIndex
                            onClickCard(pokemon.name)
                        } else {
                            CoroutineScope(Dispatchers.Default).launch {
                                snackbarHostState.showSnackbar(messageNoConnection)
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun SearchBarAndFilter(viewModel: PokeApiViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        var search by remember { mutableStateOf("") }
        val pokemonState by viewModel.getPokemonByIdOrName.observeAsState(PokeApiViewModel.UiState.Loading)

        OutlinedTextField(
            value = search,
            onValueChange = { search = it
                viewModel.getPokemonByIdOrName(search) },
            placeholder = { Text(stringResource(R.string.enter_pokemon_name)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Icon Search"
                )
            },
            shape = RoundedCornerShape(20.dp),
            singleLine = true,
            keyboardActions = KeyboardActions(
                onSearch = {
                    if (search.isNotBlank())
                        viewModel.getPokemonByIdOrName(search)
                }
            ),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Search
            ),
            modifier = Modifier.weight(1f)
        )
    }
    Spacer(modifier = Modifier.height(16.dp))
}

private fun getPokemonIdFromUrl(url: String): Int {
    return url.trimEnd('/').split("/").last().toInt()
}

private fun getPokemonImageUrl(pokemon: PokemonItem): String {
    val id = getPokemonIdFromUrl(pokemon.url)
    return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png"
}
