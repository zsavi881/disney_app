package fr.isen.savi.disney_app

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import fr.isen.savi.disney_app.ui.activities.UniverseDetailActivity
import fr.isen.savi.disney_app.ui.screens.LoginScreen
import fr.isen.savi.disney_app.ui.screens.RegisterScreen
import fr.isen.savi.disney_app.ui.theme.DisneyAppTheme
import fr.isen.savi.disney_app.viewmodel.AuthViewModel
import fr.isen.savi.disney_app.viewmodel.ProfileViewModel

class MainActivity : ComponentActivity() {
    val profileViewModel = ProfileViewModel() // permet d'nstancier la classe en gros ce que je veux faire avec cette classe doi etre ennregistre quelque part
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val authViewModel = AuthViewModel()
        if (authViewModel.user.value != null) {
            startActivity(Intent(this, UniverseDetailActivity::class.java))
            finish()
            return
        }

        enableEdgeToEdge()
        setContent { // collectAsState ne dois pas etre dans oncreate mais ici car onCreate s’exécute une seule fois quand l’Activity est créée
                    //setContent contient l’UI Compose donc être recomposée à chaque changement d’état utile pour un boutton dark mode (a peine)
            val isDarkMode by profileViewModel.isDarkMode.collectAsState()
            DisneyAppTheme(darkTheme = isDarkMode) {
                val authViewModel: AuthViewModel = viewModel()

                val state: MutableState<Boolean> = remember { mutableStateOf(false) }

                if(state.value == false) {
                    LoginScreen(
                        authViewModel = authViewModel,
                        onLoginSuccess = {
                            // On lance le catalogue
                            val intent = Intent(this, UniverseDetailActivity::class.java)
                            startActivity(intent)
                            finish()
                        },
                        onRegisterClick = {
                            state.value = true
                        }
                    )
                } else {
                    RegisterScreen(authViewModel) {
                        val intent = Intent(this, UniverseDetailActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }
    }
}