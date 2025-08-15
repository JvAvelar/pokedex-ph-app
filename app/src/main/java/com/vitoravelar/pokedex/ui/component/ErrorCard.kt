package com.vitoravelar.pokedex.ui.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight

@Composable
fun CardError(message: String) {

    Text(message, fontWeight = FontWeight.Bold)
}