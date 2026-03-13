package fr.isen.savi.disney_app.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import fr.isen.savi.disney_app.ui.screens.UniverseScreen
import fr.isen.savi.disney_app.ui.theme.DisneyAppTheme
import fr.isen.savi.disney_app.viewmodel.UniverseViewModel
import fr.isen.savi.disney_app.viewmodel.ProfileViewModel
import fr.isen.savi.disney_app.BaseActivity

class UniverseDetailActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val profileViewModel = ProfileViewModel()//instencie profil

        setAppContent {
            val universeViewModel: UniverseViewModel = viewModel()
            //val isDarkMode by profileViewModel.isDarkMode.collectAsState()

            //DisneyAppTheme(darkTheme = isDarkMode) {
                UniverseScreen(
                    universeViewModel = universeViewModel,
                    onFilmClick = { id ->
                        val intent = Intent(this, FilmDetailActivity::class.java).apply {
                            putExtra("film_id", id) // On passe l'ID du film
                        }
                    startActivity(intent)
                    },
                    onProfileClick = {
                        startActivity(Intent(this, ProfileActivity::class.java))
                    }
                )
            //}
        }

    }
}