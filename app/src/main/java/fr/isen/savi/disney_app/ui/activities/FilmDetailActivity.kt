package fr.isen.savi.disney_app.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import fr.isen.savi.disney_app.ui.screens.FilmDetailScreen
import fr.isen.savi.disney_app.ui.theme.DisneyAppTheme
import fr.isen.savi.disney_app.viewmodel.FilmDetailViewModel
import fr.isen.savi.disney_app.viewmodel.ProfileViewModel
import fr.isen.savi.disney_app.BaseActivity

class FilmDetailActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val profileViewModel = ProfileViewModel()//instencier

        val filmId = intent.getStringExtra("film_id") ?: ""

        setAppContent {
            //val isDarkMode by profileViewModel.isDarkMode.collectAsState()
            //DisneyAppTheme(darkTheme = isDarkMode) {
                val detailViewModel: FilmDetailViewModel = viewModel()


                FilmDetailScreen(
                    filmId = filmId,
                    filmDetailViewModel = detailViewModel
                )
          //  }
        }
    }
}