package fr.isen.savi.disney_app.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.lifecycle.viewmodel.compose.viewModel
import fr.isen.savi.disney_app.BaseActivity
import fr.isen.savi.disney_app.ui.screens.UniverseScreen
import fr.isen.savi.disney_app.viewmodel.ProfileViewModel
import fr.isen.savi.disney_app.viewmodel.UniverseViewModel

class UniverseDetailActivity : BaseActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val profileViewModel = ProfileViewModel()

        setAppContent {
            val universeViewModel: UniverseViewModel = viewModel()

            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Inscription") },
                        actions = {
                            IconButton(
                                onClick = {
                                    startActivity(
                                        Intent(
                                            this@UniverseDetailActivity,
                                            ProfileActivity::class.java
                                        )
                                    )
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AccountCircle,
                                    contentDescription = "Profil"
                                )
                            }
                        }
                    )
                }
            ) { innerPadding ->
                UniverseScreen(
                    universeViewModel = universeViewModel,
                    innerPadding = innerPadding,
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
}