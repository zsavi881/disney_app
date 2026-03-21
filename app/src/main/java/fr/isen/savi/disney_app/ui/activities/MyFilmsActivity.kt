package fr.isen.savi.disney_app.ui.activities

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import fr.isen.savi.disney_app.BaseActivity
import fr.isen.savi.disney_app.ui.screens.MyFilmsScreen
import fr.isen.savi.disney_app.viewmodel.ProfileViewModel
import fr.isen.savi.disney_app.viewmodel.UniverseViewModel

class MyFilmsActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val filmType = intent.getStringExtra("FILM_TYPE") ?: "watched"

        setContent {
            val profileViewModel: ProfileViewModel = viewModel()
            val universeViewModel: UniverseViewModel = viewModel()

            val categoryTitle = if (filmType == "watched") "Mes films vus" else "Ma collection DVD"

            LaunchedEffect(Unit) {
                profileViewModel.loadProfile()
            }

            val filmsList by (if (filmType == "watched")
                profileViewModel.watchedFilms
            else
                profileViewModel.ownedFilms).collectAsState()

            MyFilmsScreen(
                title = categoryTitle,
                films = filmsList,
                universeViewModel = universeViewModel,
                onBack = { finish() },
                onDeleteClick = { film ->
                    profileViewModel.userProfile.value?.uid?.let { userId ->
                        val field = if (filmType == "watched") "watched" else "ownPhysical"
                        profileViewModel.removeFilmFromSection(userId, film.getStableId(), field)
                    }
                }
            )
        }
    }
}