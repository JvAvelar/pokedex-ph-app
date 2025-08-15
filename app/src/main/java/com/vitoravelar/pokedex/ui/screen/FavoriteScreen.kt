package com.vitoravelar.pokedex.ui.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.vitoravelar.pokedex.ui.component.BaseTopAppBar
import com.vitoravelar.pokedex.ui.viewmodel.PokeApiViewModel

@Composable
fun FavoriteScreen(viewModel: PokeApiViewModel, navController: NavHostController) {

    MaterialTheme {
        Scaffold(topBar = { BaseTopAppBar("Favorite pokemons") })
        { paddings ->
            ContentFavoriteScreen(paddings)
        }
    }

}

@Composable
private fun ContentFavoriteScreen(paddings: PaddingValues) {

}


@Composable
@Preview
private fun FavoriteScreenPreview() {
    MaterialTheme {
        Scaffold(
            topBar = { BaseTopAppBar("Favorite pokemons") })
        { paddings ->
            ContentFavoriteScreen(paddings)

        }
    }
}