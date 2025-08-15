package com.vitoravelar.pokedex.feature.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.vitoravelar.pokedex.ui.component.CardError
import com.vitoravelar.pokedex.ui.component.CardFilter
import com.vitoravelar.pokedex.ui.screen.DetailScreen
import com.vitoravelar.pokedex.ui.screen.FavoriteScreen
import com.vitoravelar.pokedex.ui.screen.HomeScreen
import com.vitoravelar.pokedex.ui.viewmodel.PokeApiViewModel
import kotlinx.serialization.Serializable

@Serializable
object Home
@Serializable
object Detail
@Serializable
object Favorite
@Serializable
object CardFilter

@Serializable
object CardError


@Composable
fun NavigationGraph(viewModel: PokeApiViewModel){
    val navController = rememberNavController()

    NavHost(navController, startDestination = "home"){
        composable("home") { HomeScreen(viewModel, navController) }
        composable("favorite") { FavoriteScreen(viewModel, navController) }
        composable("cardFilter") { CardFilter() }
        composable(
            route = "detail/{pokemonName}",
            arguments = listOf(
                navArgument("pokemonName"){ type = NavType.StringType}
            )
        ) { entry ->
            val name = entry.arguments?.getString("pokemonName") ?: ""
            DetailScreen(viewModel, navController, name)
        }
    }
}