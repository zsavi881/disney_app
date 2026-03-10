package fr.isen.savi.disney_app

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import fr.isen.savi.disney_app.ui.activities.FilmDetailActivity
import fr.isen.savi.disney_app.ui.activities.ProfileActivity
import fr.isen.savi.disney_app.ui.screens.UniverseScreen
import fr.isen.savi.disney_app.ui.theme.Disney_AppTheme
import fr.isen.savi.disney_app.viewmodel.UniverseViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            // État local pour le thème (en attendant la persistance)
            var isDarkMode by remember { mutableStateOf(false) }

            Disney_AppTheme(darkTheme = isDarkMode) {
                val universeViewModel: UniverseViewModel = viewModel()

                UniverseScreen(
                    universeViewModel = universeViewModel,
                    onFilmClick = { id ->
                        // On lance l'activité de détail
                        val intent = Intent(this, FilmDetailActivity::class.java).apply {
                            putExtra("film_id", id) // Clé corrigée : "film_id"
                        }
                        startActivity(intent)
                    },
                    onProfileClick = {
                        // On lance l'activité de profil
                        val intent = Intent(this, ProfileActivity::class.java)
                        startActivity(intent)
                    }
                )
            }
        }
    }
}