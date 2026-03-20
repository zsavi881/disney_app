package fr.isen.savi.disney_app.ui.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue // IMPORT MANQUANT : Indispensable pour le "by"
import androidx.lifecycle.viewmodel.compose.viewModel
import fr.isen.savi.disney_app.BaseActivity
import fr.isen.savi.disney_app.ui.screens.MyFilmsScreen
import fr.isen.savi.disney_app.viewmodel.ProfileViewModel

class MyFilmsActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        // On récupère le type envoyé depuis ProfileActivity
        val type = intent.getStringExtra("FILM_TYPE") ?: "watched"

        setAppContent {
            val profileViewModel: ProfileViewModel = viewModel()

            // Grâce à l'import "androidx.compose.runtime.getValue", ces lignes fonctionnent :
            val watchedFilms by profileViewModel.watchedFilms.collectAsState()
            val ownedFilms by profileViewModel.ownedFilms.collectAsState()

            // Choix de la liste selon le type
            val (title, list) = if (type == "watched") {
                "Mes Films Vus" to watchedFilms
            } else {
                "Ma Collection DVD" to ownedFilms
            }

            MyFilmsScreen(
                title = title,
                films = list,
                onBack = { finish() }
            )
        }
    }
}