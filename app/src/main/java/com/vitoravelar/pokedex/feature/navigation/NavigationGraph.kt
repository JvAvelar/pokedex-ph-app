package com.vitoravelar.pokedex.feature.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.vitoravelar.pokedex.ui.screen.DetailScreen
import com.vitoravelar.pokedex.ui.screen.FavoriteScreen
import com.vitoravelar.pokedex.ui.screen.HomeScreen
import com.vitoravelar.pokedex.ui.viewmodel.PokeApiViewModel

@Composable
fun NavigationGraph(viewModel: PokeApiViewModel) {
    val navController = rememberNavController()

    NavHost(navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            val viewModel: PokeApiViewModel = viewModel()
            HomeScreen(viewModel, navController)
        }
        composable(Screen.Favorite.route) {
            val viewModel: PokeApiViewModel = viewModel()
            FavoriteScreen(viewModel, navController)
        }
        composable(
            route = Screen.Detail.route,
            arguments = listOf(
                navArgument(Routes.POKEMON_NAME) { type = NavType.StringType }
            )
        ) { entry ->
            val name = entry.arguments?.getString(Routes.POKEMON_NAME) ?: ""
            val viewModel: PokeApiViewModel = viewModel()
            DetailScreen(viewModel, navController, name)
        }
    }
}

sealed class Screen(val route: String) {
    object Home : Screen(Routes.HOME)
    object Favorite : Screen(Routes.FAVORITE)
    object Detail : Screen("${Routes.DETAIL}/{${Routes.POKEMON_NAME}}") {
        fun createRoute(pokemonName: String) = "${Routes.DETAIL}/$pokemonName"
    }
}

private object Routes {
    const val HOME = "home"
    const val FAVORITE = "favorite"
    const val DETAIL = "detail"
    const val POKEMON_NAME = "pokemonName"
}