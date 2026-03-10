package fr.isen.savi.disney_app.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

// On importe tout ce qui est dans screens et viewmodel
import fr.isen.savi.disney_app.ui.screens.*
import fr.isen.savi.disney_app.viewmodel.*
import fr.isen.savi.disney_app.viewmodel.FilmDetailViewModel
import fr.isen.savi.disney_app.viewmodel.FilmListViewModel
import fr.isen.savi.disney_app.ui.screens.FilmDetailScreen
import fr.isen.savi.disney_app.ui.screens.ProfileScreen
import fr.isen.savi.disney_app.viewmodel.ProfileViewModel

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
    const val FILMS = "films"
    const val FILM_DETAIL = "film"
    const val PROFILE = "profile"
}

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    // Initialisation des ViewModels
    val authViewModel: AuthViewModel = viewModel()
    val filmListViewModel: FilmListViewModel = viewModel()
    val universeViewModel: UniverseViewModel = viewModel()
    val filmDetailViewModel: FilmDetailViewModel = viewModel()
    val profileViewModel: ProfileViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN
    ) {
        // Écran Login
        composable(Routes.LOGIN) {
            LoginScreen(
                authViewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onRegisterClick = {
                    navController.navigate(Routes.REGISTER)
                }
            )
        }

        // Écran Inscription
        composable(Routes.REGISTER) {
            RegisterScreen(
                authViewModel = authViewModel,
                onRegisterSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        // Écran des Univers
        composable(Routes.HOME) {
            UniverseScreen(
                universeViewModel = universeViewModel,
                onFilmClick = { filmId ->
                    // On navigue directement vers le détail du film avec son ID stable
                    navController.navigate("${Routes.FILM_DETAIL}/$filmId")
                },
                onProfileClick = {
                    navController.navigate(Routes.PROFILE)
                }
            )
        }

        // Liste des Films
        composable(
            route = "${Routes.FILMS}/{universeId}",
            arguments = listOf(navArgument("universeId") { type = NavType.StringType })
        ) { backStackEntry ->
            val universeId = backStackEntry.arguments?.getString("universeId").orEmpty()
            FilmListScreen(
                universeId = universeId,
                filmListViewModel = filmListViewModel,
                onFilmClick = { filmId ->
                    navController.navigate("${Routes.FILM_DETAIL}/$filmId")
                }
            )
        }

        // Détail d'un Film
        composable(
            route = "${Routes.FILM_DETAIL}/{filmId}",
            arguments = listOf(navArgument("filmId") { type = NavType.StringType })
        ) { backStackEntry ->
            val filmId = backStackEntry.arguments?.getString("filmId").orEmpty()
            FilmDetailScreen(
                filmId = filmId,
                filmDetailViewModel = filmDetailViewModel
            )
        }
        composable(Routes.PROFILE) {
            ProfileScreen(
                profileViewModel = profileViewModel,
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(0)
                    }
                }
            )
        }
    }
}