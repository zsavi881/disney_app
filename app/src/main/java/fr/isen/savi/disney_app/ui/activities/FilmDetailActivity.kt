package fr.isen.savi.disney_app.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import fr.isen.savi.disney_app.ui.screens.FilmDetailScreen
import fr.isen.savi.disney_app.viewmodel.FilmDetailViewModel

class FilmDetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val filmId = intent.getStringExtra("film_id") ?: ""

        setContent {
            val detailViewModel: FilmDetailViewModel = viewModel()


            FilmDetailScreen(
                filmId = filmId,
                filmDetailViewModel = detailViewModel
            )
        }
    }
}