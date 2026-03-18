package fr.isen.savi.disney_app.ui.activities

import android.os.Bundle
import androidx.lifecycle.viewmodel.compose.viewModel
import fr.isen.savi.disney_app.ui.screens.FilmDetailScreen
import fr.isen.savi.disney_app.viewmodel.FilmDetailViewModel
import fr.isen.savi.disney_app.BaseActivity

class FilmDetailActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val filmId = intent.getStringExtra("film_id") ?: ""

        setAppContent {
            val detailViewModel: FilmDetailViewModel = viewModel()

            FilmDetailScreen(
                filmId = filmId,
                filmDetailViewModel = detailViewModel,
                onBack = { finish() }
            )
        }
    }
}