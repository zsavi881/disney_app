package fr.isen.savi.disney_app.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import fr.isen.savi.disney_app.BaseActivity
import fr.isen.savi.disney_app.ui.screens.UniverseScreen
import fr.isen.savi.disney_app.viewmodel.UniverseViewModel

class UniverseDetailActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setAppContent {
            val universeViewModel: UniverseViewModel = viewModel()

            UniverseScreen(
                universeViewModel = universeViewModel,
                innerPadding = PaddingValues(0.dp),
                onFilmClick = { id ->
                    val intent = Intent(this@UniverseDetailActivity, FilmDetailActivity::class.java).apply {
                        putExtra("film_id", id)
                    }
                    startActivity(intent)
                },
                onProfileClick = {
                    startActivity(Intent(this@UniverseDetailActivity, ProfileActivity::class.java))
                }
            )
        }
    }
}