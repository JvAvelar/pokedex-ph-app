package com.vitoravelar.pokedex.ui.screen

import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vitoravelar.pokedex.R
import com.vitoravelar.pokedex.feature.model.TypeItem
import com.vitoravelar.pokedex.ui.viewmodel.PokeApiViewModel
import com.vitoravelar.pokedex.utils.isNetworkAvailable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterScreen(
    viewModel: PokeApiViewModel,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val typesUiState by viewModel.pokemonTypesState.observeAsState(PokeApiViewModel.UiState.Loading)

    val context = LocalContext.current
    val messageNoConnection = stringResource(R.string.no_connection)

    LaunchedEffect(Unit) {
        viewModel.fetchPokemonTypes()
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Filtrar por Tipo",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(16.dp))

            when (val state = typesUiState) {
                is PokeApiViewModel.UiState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp), contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is PokeApiViewModel.UiState.Success -> {
                    OptionsTypes(viewModel, state.data, context) { onDismiss() }
                }

                is PokeApiViewModel.UiState.Error -> {
                    Toast.makeText(
                        context,
                        messageNoConnection,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun OptionsTypes(
    viewModel: PokeApiViewModel,
    types: List<TypeItem>,
    context: Context,
    onDismiss: () -> Unit
) {
    val currentSelectedType by viewModel.selectedTypeFilter.observeAsState()
    val messageNoConnection = stringResource(R.string.no_connection)

    if (types.isNotEmpty()) {
        Button(
            onClick = {
                viewModel.setTypeFilter(null)
                onDismiss()
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = currentSelectedType != null
        ) {
            Text("Limpar Filtro / Mostrar Todos")
        }
        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(types, key = { it.name }) { typeItem ->
                TypeFilterItem(
                    typeItem = typeItem,
                    isSelected = typeItem.name == currentSelectedType?.name,
                    onTypeClick = { selected ->
                        if (isNetworkAvailable(context)) {
                            viewModel.setTypeFilter(selected)
                            onDismiss()
                        } else
                            Toast.makeText(
                                context,
                                messageNoConnection,
                                Toast.LENGTH_SHORT
                            ).show()
                    }
                )
                HorizontalDivider(modifier = Modifier.height(1.dp))
            }
        }
    } else {
        Box(Modifier, contentAlignment = Alignment.Center) {
            Text(
                "Nenhum tipo encontrado.", fontSize = 22.sp
            )
        }
    }
}

@Composable
fun TypeFilterItem(
    typeItem: TypeItem,
    isSelected: Boolean,
    onTypeClick: (TypeItem) -> Unit
) {
    ListItem(
        headlineContent = {
            Text(
                text = typeItem.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() },
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                fontSize = 18.sp
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onTypeClick(typeItem) },
        colors = if (isSelected) {
            ListItemDefaults.colors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                headlineColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        } else {
            ListItemDefaults.colors()
        }
    )
}
