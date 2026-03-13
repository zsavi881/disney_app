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
import fr.isen.savi.disney_app.ui.screens.RegisterScreen
import fr.isen.savi.disney_app.ui.theme.DisneyAppTheme
import fr.isen.savi.disney_app.viewmodel.AuthViewModel
import fr.isen.savi.disney_app.viewmodel.ProfileViewModel
import fr.isen.savi.disney_app.BaseActivity



class RegisterActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Active l'affichage plein écran (Edge-to-Edge)
        enableEdgeToEdge()

        setAppContent {
            // 1. Initialisation du ViewModel
            val profileViewModel: ProfileViewModel = viewModel()
            val authViewModel: AuthViewModel = viewModel()
            // 2. Observation de l'état du thème (Dark Mode)
            // On utilise "by" pour déléguer l'état et simplifier l'usage de isDarkMode
            //val isDarkMode by profileViewModel.isDarkMode.collectAsState()

            // 3. Application du thème avec le paramètre correct
            //DisneyAppTheme(darkTheme = isDarkMode) {
                ProfileScreen(
                    profileViewModel = profileViewModel,
                    onLogout = {
                        // Action de déconnexion avec nettoyage de la pile d'activités
                        val intent = Intent(this, MainActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        startActivity(intent)
                        finish()
                    }
                )

                RegisterScreen(
                    authViewModel
                ) {
                    // Action de déconnexion avec nettoyage de la pile d'activités
                    val intent = Intent(this, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    startActivity(intent)
                    finish()
                }
           // }
        }
    }
}