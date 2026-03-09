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
import fr.isen.savi.disney_app.viewmodel.FilmListViewModel

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
    const val FILMS = "films"
}

@Composable
fun AppNavGraph() {

    val navController = rememberNavController()
    val filmListViewModel: FilmListViewModel = viewModel()
    val universeViewModel: UniverseViewModel = viewModel()
    val authViewModel: AuthViewModel = viewModel()

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

            val universeViewModel: UniverseViewModel = viewModel()

            UniverseScreen(
                universeViewModel = universeViewModel,
                onUniverseClick = { universeId ->
                    navController.navigate("${Routes.FILMS}/$universeId")
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
                }
            )
        }
    }
}