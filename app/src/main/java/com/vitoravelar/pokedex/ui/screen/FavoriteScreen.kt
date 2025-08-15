package com.vitoravelar.pokedex.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.vitoravelar.pokedex.R
import com.vitoravelar.pokedex.feature.model.PokemonDetailEntity
import com.vitoravelar.pokedex.ui.component.BaseTopAppBar
import com.vitoravelar.pokedex.ui.component.PokemonCard
import com.vitoravelar.pokedex.ui.viewmodel.PokeApiViewModel
import com.vitoravelar.pokedex.utils.isNetworkAvailable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun FavoriteScreen(viewModel: PokeApiViewModel, navController: NavHostController) {

    val favorites by viewModel.listAllFavorite.observeAsState(emptyList())
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            BaseTopAppBar(
                title = stringResource(R.string.title_favorite),
                onLeftIconClick = { navController.popBackStack() })
        },
        snackbarHost = { SnackbarHost(snackbarHostState) })
    { paddings ->

        if (favorites.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddings),
                contentAlignment = Alignment.Center
            ) {
                Text(stringResource(R.string.no_favorite_yet))
            }
        } else
            ContentFavoriteScreen(
                paddings,
                favorites, snackbarHostState,
                onClickCard = { pokemonName ->
                    navController.navigate("detail/${pokemonName}")
                })
    }
}

@Composable
private fun ContentFavoriteScreen(
    paddings: PaddingValues,
    favorites: List<PokemonDetailEntity>,
    snackbarHostState: SnackbarHostState,
    onClickCard: (String) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(120.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(paddings),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(favorites) { pokemon ->
            val context = LocalContext.current
            val messageNoConnection = stringResource(R.string.no_connection)

            PokemonCard(
                name = pokemon.name,
                imageUrl = pokemon.imageUrl,
                onClick = {
                    if (isNetworkAvailable(context)) {
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