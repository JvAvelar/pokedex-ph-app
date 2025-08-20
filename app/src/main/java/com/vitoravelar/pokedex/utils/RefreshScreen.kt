package com.vitoravelar.pokedex.utils

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.vitoravelar.pokedex.ui.viewmodel.PokeApiViewModel


@Composable
fun RefreshScreen(viewModel: PokeApiViewModel) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        IconButton(onClick = { viewModel.loadInitialPokemon() }) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = "Icon refresh"
            )
        }
    }
}

