package fr.isen.savi.disney_app.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.lifecycle.viewmodel.compose.viewModel
import fr.isen.savi.disney_app.ui.screens.UniverseScreen
import fr.isen.savi.disney_app.viewmodel.UniverseViewModel
import fr.isen.savi.disney_app.viewmodel.ProfileViewModel
import fr.isen.savi.disney_app.BaseActivity

class UniverseDetailActivity : BaseActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val profileViewModel = ProfileViewModel()//instencie profil

        setAppContent {
            val universeViewModel: UniverseViewModel = viewModel()

            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Inscription") }
                    )
                }
            ) { innerPadding ->
                UniverseScreen(
                    universeViewModel = universeViewModel,
                    innerPadding = innerPadding,
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
            }
        }

    }
}