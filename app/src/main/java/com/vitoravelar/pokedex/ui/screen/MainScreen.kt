package com.vitoravelar.pokedex.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vitoravelar.pokedex.ui.viewmodel.PokeApiViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {

    val viewModel: PokeApiViewModel = viewModel()

    MaterialTheme {
        Scaffold(topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "All pokemons",
                        color = Color.White,
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                        textAlign = TextAlign.End
                    )
                },
            )
        }) { paddings ->
            ContentFavoriteScreen(paddings, viewModel)

        }
    }

}

@Composable
private fun ContentFavoriteScreen(paddings: PaddingValues, viewModel: PokeApiViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddings)
            .padding(top = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn {
            items(10) {
                Card {
                    Text("Bulbasaur")
                    Text("charizard")
                    Text("Cartepie")
                }
                Spacer(modifier = Modifier.height(8.dp))

            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
private fun MainScreenPreview() {
    MaterialTheme {
        Scaffold(topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "All pokemons",
                        color = Color.Black,
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.bodyLarge,
                        fontSize = 24.sp,
                        textAlign = TextAlign.Center
                    )
                },
            )
        }) { paddings ->
            ContentFavoriteScreen(paddings, viewModel())

        }
    }
}