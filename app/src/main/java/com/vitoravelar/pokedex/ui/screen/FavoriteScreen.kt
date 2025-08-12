package com.vitoravelar.pokedex.ui.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.vitoravelar.pokedex.ui.component.BaseTopAppBar

@Composable
fun FavoriteScreen(){

    MaterialTheme{
        Scaffold { paddings ->
            ContentFavoriteScreen(paddings)

        }
    }

}

@Composable
private fun ContentFavoriteScreen(paddings: PaddingValues) {

}


@Composable
@Preview
private fun FavoriteScreenPreview(){
    MaterialTheme{
        Scaffold(
            topBar = { BaseTopAppBar("Favorite") })
        { paddings ->
            ContentFavoriteScreen(paddings)

        }
    }
}