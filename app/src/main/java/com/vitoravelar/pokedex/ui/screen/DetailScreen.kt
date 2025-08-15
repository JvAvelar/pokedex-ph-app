package com.vitoravelar.pokedex.ui.screen

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.vitoravelar.pokedex.R
import com.vitoravelar.pokedex.feature.model.PokemonDetail
import com.vitoravelar.pokedex.feature.model.PokemonDetailEntity
import com.vitoravelar.pokedex.ui.component.BaseTopAppBar
import com.vitoravelar.pokedex.ui.component.CardError
import com.vitoravelar.pokedex.ui.component.LoadingBar
import com.vitoravelar.pokedex.ui.viewmodel.PokeApiViewModel

@Composable
fun DetailScreen(
    viewModel: PokeApiViewModel,
    navController: NavHostController,
    pokemonName: String
) {
    LaunchedEffect(Unit) {
        viewModel.getPokemonByIdOrName(pokemonName)
    }

    val pokemonState by viewModel.getPokemonByIdOrName.observeAsState(PokeApiViewModel.UiState.Loading)

    when (pokemonState) {
        is PokeApiViewModel.UiState.Loading -> {
            LoadingBar()
        }

        is PokeApiViewModel.UiState.Success -> {
            val pokemon = (pokemonState as PokeApiViewModel.UiState.Success).data!!
            Scaffold(
                topBar = {
                    BaseTopAppBar(
                        title = "Pokemon details",
                        onLeftIconClick = { navController.popBackStack() })
                }
            ) { paddings ->
                DetailCard(paddings, pokemon, viewModel)
            }
        }

        is PokeApiViewModel.UiState.Error -> {
            val message = (pokemonState as PokeApiViewModel.UiState.Error).message
            CardError(message)
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun DetailCard(
    padding: PaddingValues,
    pokemon: PokemonDetail,
    viewModel: PokeApiViewModel
) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.background))
            .padding(padding)
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                var clickable by remember { mutableStateOf(false) }
                IconButton(onClick = {
                    clickable = !clickable
                    val pokemonEntity = PokemonDetailEntity(
                        pokemon.id,
                        pokemon.name,
                        pokemon.sprites.other.officialArtwork.imageUrl!!,
                        pokemon.types[0].type.name
                    )
                    if (clickable)
                        viewModel.addFavorite(pokemonEntity)
                    else
                        viewModel.removeFavorite(pokemonEntity)
                }) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Left Icon",
                        modifier = Modifier.size(24.dp),
                        tint = if (clickable) Color.Red else Color.LightGray
                    )
                }
            }

            GlideImage(
                model = pokemon.sprites.other.officialArtwork.imageUrl,
                contentDescription = pokemon.name,
                modifier = Modifier.size(120.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "#${pokemon.id} ${pokemon.name.replaceFirstChar { it.uppercase() }}",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))


            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                pokemon.types.forEach { typeSlot ->
                    AssistChip(
                        onClick = {},
                        label = { Text(typeSlot.type.name.uppercase()) },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = when (typeSlot.type.name.lowercase()) {
                                "water" -> colorResource(R.color.water)
                                "fire" -> colorResource(R.color.fire)
                                "grass" -> colorResource(R.color.grass)
                                "electric" -> colorResource(R.color.eletric)
                                "ground" -> colorResource(R.color.ground)
                                "poison" -> colorResource(R.color.poison)
                                "normal" -> colorResource(R.color.normal)
                                "flying" -> colorResource(R.color.flying)
                                "fighting" -> colorResource(R.color.fighting)
                                "ice" -> colorResource(R.color.ice)
                                "ghost" -> colorResource(R.color.ghost)
                                "rock" -> colorResource(R.color.rock)
                                "bug" -> colorResource(R.color.bug)
                                "dragon" -> colorResource(R.color.dragon)
                                "psychic" -> colorResource(R.color.psychic)
                                else -> Color.White
                            }
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color.Black)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Statistics",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                pokemon.stats.forEach { stat ->
                    Text(
                        text = "${stat.stat.name.replaceFirstChar { it.uppercase() }}: ${stat.baseStat}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color.Black)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Skills",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                pokemon.abilities.forEach { abilitySlot ->
                    Text(
                        text = abilitySlot.ability.name.replaceFirstChar { it.uppercase() },
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}