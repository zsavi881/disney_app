package fr.isen.savi.disney_app.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import fr.isen.savi.disney_app.MainActivity
import fr.isen.savi.disney_app.ui.screens.ProfileScreen
import fr.isen.savi.disney_app.ui.theme.Disney_AppTheme
import fr.isen.savi.disney_app.viewmodel.ProfileViewModel

class ProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Permet l'affichage bord à bord (optionnel mais recommandé)
        enableEdgeToEdge()

        setContent {
            // 1. On récupère le ViewModel du Profil
            val profileViewModel: ProfileViewModel = viewModel()

            // 2. On observe l'état du Dark Mode en temps réel
            val isDarkMode by profileViewModel.isDarkMode.collectAsState()

            // 3. On applique ton Thème personnalisé
            Disney_AppTheme(darkTheme = isDarkMode) {
                ProfileScreen(
                    profileViewModel = profileViewModel,
                    onLogout = {
                        // Action de déconnexion :
                        // On retourne à l'écran de Login (ou Main) et on vide la pile d'activités
                        val intent = Intent(this, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    }
                )
            }
        }
    }
}