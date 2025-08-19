package com.vitoravelar.pokedex.ui.screen

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
// Imports adicionados para o filtro e Material 3
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
// Remover imports não utilizados se houver
// import androidx.compose.foundation.layout.wrapContentSize
// import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
// import androidx.compose.foundation.lazy.grid.items // Removido pois items(pokemonList.size) é usado
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme // Adicionado para cores do tema
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar // Adicionado se for usar o TopAppBar padrão para o título do filtro
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
// import androidx.compose.ui.focus.focusModifier // Removido se não usado
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign // Adicionado para alinhar texto
import androidx.compose.ui.unit.dp
// import androidx.compose.ui.unit.sp // Removido se não usado diretamente aqui
import androidx.navigation.NavHostController
import com.vitoravelar.pokedex.R
import com.vitoravelar.pokedex.feature.model.PokemonItem
import com.vitoravelar.pokedex.feature.navigation.Screen
import com.vitoravelar.pokedex.ui.component.BaseTopAppBar
import com.vitoravelar.pokedex.ui.component.CardError
import com.vitoravelar.pokedex.ui.component.LoadingBar
import com.vitoravelar.pokedex.ui.component.PokemonCard
// Importe seu FilterScreen.kt
import com.vitoravelar.pokedex.ui.viewmodel.PokeApiViewModel
import com.vitoravelar.pokedex.utils.isNetworkAvailable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: PokeApiViewModel, navController: NavHostController) {

    val pokemonListUiState by viewModel.pokemonListState.observeAsState(PokeApiViewModel.UiState.Loading)
    var showFilterSheet by remember { mutableStateOf(false) }
    val currentActiveFilter by viewModel.selectedTypeFilter.observeAsState()


    val snackbarHostState = remember { SnackbarHostState() }
    val gridState = rememberLazyGridState()
    val context = LocalContext.current
    val messageNoConnection = stringResource(R.string.no_connection)

    LaunchedEffect(pokemonListUiState) {
        if (pokemonListUiState is PokeApiViewModel.UiState.Success) {

            if (currentActiveFilter == null) {
                val index = viewModel.lastScrollState
                if (index > 0 && gridState.firstVisibleItemIndex != index) {
                    gridState.scrollToItem(index)
                }
            } else {
                gridState.scrollToItem(0)
            }
        }
    }

    val shouldLoadMore by remember(pokemonListUiState, currentActiveFilter) {
        derivedStateOf {

            if (currentActiveFilter != null || !viewModel.canLoadMore) {
                return@derivedStateOf false
            }

            val layoutInfo = gridState.layoutInfo
            val totalItemsCount = layoutInfo.totalItemsCount
            if (totalItemsCount == 0) return@derivedStateOf false

            val lastVisibleItemIndex = (layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0)
            lastVisibleItemIndex >= totalItemsCount - 5
        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) {
            if (isNetworkAvailable(context)) {
                viewModel.loadMorePokemon()
            } else {
                CoroutineScope(Dispatchers.Default).launch {
                    snackbarHostState.showSnackbar(messageNoConnection)
                }
            }
        }
    }

    BackHandler {
        if (showFilterSheet) {
            showFilterSheet = false
        } else {
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            BaseTopAppBar(
                stringResource(R.string.title_pokemon),
                leftIcon = null,
                rightIcon = Icons.Default.FavoriteBorder,
                onRightIconClick = { navController.navigate(Screen.Favorite.route) }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddings ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(paddings)) {
            currentActiveFilter?.let { activeFilter ->
                Text(
                    text = "Filtrando por: ${activeFilter.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f))
                        .padding(vertical = 4.dp, horizontal = 16.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            when (val state = pokemonListUiState) {
                is PokeApiViewModel.UiState.Loading -> {
                    LoadingBar(PaddingValues(0.dp))
                }

                is PokeApiViewModel.UiState.Success -> {
                    ContentHomeScreen(
                        padding = PaddingValues(0.dp),
                        pokemonList = state.data,
                        viewModel = viewModel,
                        snackbarHostState = snackbarHostState,
                        context = context,
                        gridState = gridState,
                        onClickFilter = { showFilterSheet = true },
                        onClickNavigateToDetailScreen = { pokemonName ->
                            if (currentActiveFilter == null) {
                                viewModel.lastScrollState = gridState.firstVisibleItemIndex
                            }
                            navController.navigate(
                                Screen.Detail.createRoute(pokemonName)
                            )
                        },
                        activeFilterName = currentActiveFilter?.name
                    )
                }

                is PokeApiViewModel.UiState.Error -> {
                    CardError(state.message)
                }
            }
        }
    }

    if (showFilterSheet) {
        FilterScreen(
            viewModel = viewModel,
            onDismiss = { showFilterSheet = false }
        )
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
    onClickFilter: () -> Unit,
    onClickNavigateToDetailScreen: (String) -> Unit,
    activeFilterName: String?
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(horizontal = 8.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SearchBarAndFilter(
            viewModel, context,
            onClickFilter = { onClickFilter() },
            onSearchClick = { pokemonName ->
                if (pokemonName != null)
                    onClickNavigateToDetailScreen(pokemonName)
            },
        )

        val messageNoConnection = stringResource(R.string.no_connection)

        if (pokemonList.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Nenhum Pokémon para exibir.")
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(120.dp),
                state = gridState,
                modifier = Modifier.fillMaxSize()
            ) {
                items(count = pokemonList.size, key = { index -> pokemonList[index].url }) { index ->
                    val pokemon = pokemonList[index]
                    val image = getPokemonImageUrl(pokemon)
                    PokemonCard(
                        name = pokemon.name,
                        imageUrl = image,
                        onClick = {
                            if (isNetworkAvailable(context)) {
                                onClickNavigateToDetailScreen(pokemon.name)
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
}

@Composable
private fun SearchBarAndFilter(
    viewModel: PokeApiViewModel,
    context: Context,
    onClickFilter: () -> Unit,
    onSearchClick: (String?) -> Unit
) {
    var search by remember { mutableStateOf("") }
    val messageEnterPokemonName = stringResource(R.string.enter_pokemon_name)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {

        OutlinedTextField(
            value = search,
            onValueChange = { search = it },
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
                    if (search.isNotBlank()) {
                        viewModel.getPokemonByIdOrName(search.lowercase())
                        onSearchClick(search.lowercase())
                    }
                     else
                        Toast.makeText(
                            context, messageEnterPokemonName,
                            Toast.LENGTH_SHORT
                        ).show()
                }
            ),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Search
            ),
            modifier = Modifier.weight(1f)
        )
        IconButton(onClick = { onClickFilter() }) {
            Icon(
                painter = painterResource(R.drawable.ic_filter),
                contentDescription = "Icon Filter",
                modifier = Modifier.size(40.dp)
            )
        }
    }
}

private fun getPokemonIdFromUrl(url: String): Int {
    return url.trimEnd('/').split("/").last().toInt()
}

private fun getPokemonImageUrl(pokemon: PokemonItem): String {
    val id = getPokemonIdFromUrl(pokemon.url)
      val url = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png"
    return url
}
