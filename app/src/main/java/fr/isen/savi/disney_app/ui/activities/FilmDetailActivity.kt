package fr.isen.savi.disney_app.ui.activities

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.lifecycle.viewmodel.compose.viewModel
import fr.isen.savi.disney_app.ui.screens.FilmDetailScreen
import fr.isen.savi.disney_app.viewmodel.FilmDetailViewModel
import fr.isen.savi.disney_app.BaseActivity

class FilmDetailActivity : BaseActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val filmId = intent.getStringExtra("film_id") ?: ""

        setAppContent {
            val detailViewModel: FilmDetailViewModel = viewModel()

            FilmDetailScreen(
                filmId = filmId,
                filmDetailViewModel = detailViewModel,
                innerPadding = PaddingValues(),
                onBack = { finish() }
            )
        }
    }
}