package fr.isen.savi.disney_app.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel // Indispensable pour récupérer le ViewModel
import fr.isen.savi.disney_app.BaseActivity
import fr.isen.savi.disney_app.MainActivity
import fr.isen.savi.disney_app.ui.screens.ProfileScreen
import fr.isen.savi.disney_app.viewmodel.ProfileViewModel

// ATTENTION : Vérifie dans ton dossier "activities" si ton fichier de login
// s'appelle bien "LoginActivity". Si c'est "MainActivity", change le nom ici :
// import fr.isen.savi.disney_app.ui.activities.LoginActivity

class ProfileActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setAppContent {
            // FIX de l'erreur ViewModel : On crée la variable locale "mViewModel"
            val mViewModel: ProfileViewModel = viewModel()

            ProfileScreen(
                profileViewModel = mViewModel, // On passe la variable qu'on vient de créer
                onLogout = {
                    // FIX de l'erreur LoginActivity :
                    // Si ton écran de connexion s'appelle autrement, change le nom ici.
                    // Par exemple : Intent(this, MainActivity::class.java)
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finishAffinity()
                },
                onNavigateToCatalog = {
                    finish()
                },
                onNavigateToMyFilms = { type ->
                    // Navigation vers ton écran de liste
                    val intent = Intent(this, MyFilmsActivity::class.java)
                    intent.putExtra("FILM_TYPE", type)
                    startActivity(intent)
                },
                innerPadding = PaddingValues(0.dp)
            )
        }
    }
}