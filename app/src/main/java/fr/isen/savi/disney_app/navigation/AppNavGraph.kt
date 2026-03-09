package fr.isen.savi.disney_app.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import fr.isen.savi.disney_app.ui.screens.LoginScreen
import fr.isen.savi.disney_app.ui.screens.RegisterScreen
import fr.isen.savi.disney_app.viewmodel.AuthViewModel
import fr.isen.savi.disney_app.ui.screens.UniverseScreen
import fr.isen.savi.disney_app.viewmodel.UniverseViewModel
import fr.isen.savi.disney_app.ui.screens.FilmListScreen
import androidx.navigation.NavType
import androidx.navigation.navArgument
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
    val filmListViewModel: FilmListViewModel = viewModel()
    val universeViewModel: UniverseViewModel = viewModel()
    val authViewModel: AuthViewModel = viewModel()
    val filmDetailViewModel: FilmDetailViewModel = viewModel()
    val profileViewModel: ProfileViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN
    ) {

        composable(Routes.LOGIN) {
            LoginScreen(
                authViewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate(Routes.HOME)
                },
                onRegisterClick = {
                    navController.navigate(Routes.REGISTER)
                }
            )
        }

        composable(Routes.REGISTER) {
            RegisterScreen(
                authViewModel = authViewModel,
                onRegisterSuccess = {
                    navController.navigate(Routes.HOME)
                }
            )
        }
        composable(Routes.HOME) {
            UniverseScreen(
                universeViewModel = universeViewModel,
                onUniverseClick = { universeId ->
                    navController.navigate("${Routes.FILMS}/$universeId")
                },
                onProfileClick = {
                    navController.navigate(Routes.PROFILE)
                }
            )
        }
        composable(
            route = "${Routes.FILMS}/{universeId}",
            arguments = listOf(
                navArgument("universeId") { type = NavType.StringType }
            )
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
        composable(
            route = "${Routes.FILM_DETAIL}/{filmId}",
            arguments = listOf(
                navArgument("filmId") { type = NavType.StringType }
            )
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