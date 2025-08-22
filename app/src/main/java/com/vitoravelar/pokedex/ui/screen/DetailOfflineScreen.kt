package com.vitoravelar.pokedex.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.vitoravelar.pokedex.R
import com.vitoravelar.pokedex.feature.model.DetailFavoritesEntity
import com.vitoravelar.pokedex.ui.component.BaseTopAppBar
import com.vitoravelar.pokedex.ui.viewmodel.PokeApiViewModel
import com.vitoravelar.pokedex.utils.PokemonDetailColor

@Composable
fun DetailOfflineScreen(
    viewModel: PokeApiViewModel,
    navController: NavController,
    pokemonName: String
) {
    val detailsOfflineList by viewModel.listAllDetails.observeAsState(emptyList())

    val details = detailsOfflineList.find { it.pokemonName == pokemonName }
    Scaffold(
        topBar = {
            BaseTopAppBar(
                title = stringResource(R.string.title_details),
                onLeftIconClick = { navController.popBackStack() })
        }
    ) { paddings ->

        details?.let { detail -> DetailCardOffline(paddings, detail) }

    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun DetailCardOffline(
    paddings: PaddingValues,
    details: DetailFavoritesEntity
) {

    val mapStatics = mapOf(
        "hp" to details.hp, "attack" to details.attack, "defense" to details.defense,
        "special-attack" to details.specialAttack, "special-defense" to details.specialDefense,
        "speed" to details.speed
    )

    val listSkills = details.skills.split(",")


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(paddings)
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
            Text(
                text = "#${details.pokemonId} ${details.pokemonName.replaceFirstChar { it.uppercase() }}",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

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
                    text = stringResource(R.string.statistics),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.height(4.dp))

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    mapStatics.forEach { stat ->
                        AssistChip(
                            onClick = {},
                            label = {
                                Text(
                                    text = "${stat.key.replaceFirstChar { it.uppercase() }}: ${stat.value}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            },
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = PokemonDetailColor.getColor(stat.key)
                            )
                        )
                    }
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
                    text = stringResource(R.string.skills),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.height(4.dp))

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listSkills.forEach { skill ->
                        AssistChip(
                            onClick = {},
                            label = {
                                Text(
                                    text = skill.replaceFirstChar { it.uppercase() },
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            },
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = colorResource(R.color.fire)
                            )
                        )
                    }
                }
            }
        }
    }
}