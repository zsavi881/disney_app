package fr.isen.savi.disney_app.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import fr.isen.savi.disney_app.ui.screens.UniverseScreen
import fr.isen.savi.disney_app.viewmodel.UniverseViewModel

class UniverseDetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val universeViewModel: UniverseViewModel = viewModel()

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
        }
    }
}