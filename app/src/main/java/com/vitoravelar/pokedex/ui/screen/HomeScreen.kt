package com.vitoravelar.pokedex.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.vitoravelar.pokedex.R
import com.vitoravelar.pokedex.feature.model.PokemonItem
import com.vitoravelar.pokedex.ui.component.BaseTopAppBar
import com.vitoravelar.pokedex.ui.component.CardError
import com.vitoravelar.pokedex.ui.component.LoadingBar
import com.vitoravelar.pokedex.ui.viewmodel.PokeApiViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: PokeApiViewModel = viewModel(), navController: NavHostController) {

    LaunchedEffect(Unit) {
        viewModel.getPokemonList(2000)
    }

    val pokemonListState by viewModel.pokemonList.observeAsState(PokeApiViewModel.UiState.Loading)

    when (pokemonListState) {
        is PokeApiViewModel.UiState.Loading -> {
            LoadingBar()
        }

        is PokeApiViewModel.UiState.Success -> {
            val listPokemons = (pokemonListState as PokeApiViewModel.UiState.Success).data
            Scaffold(
                topBar = {
                    BaseTopAppBar(
                        title = "Pokemon",
                        leftIcon = null,
                        rightIcon = Icons.Default.FavoriteBorder,
                        onRightIconClick = { navController.navigate("favorite") })
                }
            ) { paddings ->
                ContentHomeScreen(
                    paddings,
                    listPokemons, viewModel, navController
                ) { navController.navigate("cardFilter") }
            }
        }

        is PokeApiViewModel.UiState.Error -> {
            val message = (pokemonListState as PokeApiViewModel.UiState.Error).message
            CardError(message)
        }
    }
}

@Composable
private fun ContentHomeScreen(
    padding: PaddingValues,
    pokemonList: List<PokemonItem>,
    viewModel: PokeApiViewModel,
    navController: NavController,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(horizontal = 8.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SearchBarAndFilter(viewModel) { onClick() }

        LazyVerticalGrid(columns = GridCells.Adaptive(120.dp)) {
            items(pokemonList) { pokemon ->
                PokemonCard(navController, pokemon)
            }
        }
    }
}

fun getPokemonIdFromUrl(url: String): Int {
    return url.trimEnd('/').split("/").last().toInt()
}

fun getPokemonImageUrl(pokemon: PokemonItem): String {
    val id = getPokemonIdFromUrl(pokemon.url)
    return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png"
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun PokemonCard(navController: NavController, pokemon: PokemonItem) {
    val imageUrl = getPokemonImageUrl(pokemon)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navController.navigate("detail/${pokemon.name}") }
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            GlideImage(
                model = imageUrl,
                contentDescription = pokemon.name,
                modifier = Modifier.size(120.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text(text = pokemon.name, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
private fun SearchBarAndFilter(viewModel: PokeApiViewModel, onFilterClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        val search by viewModel.searchText.observeAsState("")

        OutlinedTextField(
            value = search,
            onValueChange = { viewModel.onSearchTextChange(it) },
            placeholder = { Text("Insira o nome do Pokemon") },
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

        IconButton(onClick = { onFilterClick() }) {
            Icon(
                painter = painterResource(R.drawable.ic_filter),
                contentDescription = "Icon filter", Modifier.size(32.dp)
            )
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
}