package fr.isen.savi.disney_app

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import fr.isen.savi.disney_app.ui.activities.UniverseDetailActivity
import fr.isen.savi.disney_app.ui.screens.LoginScreen
import fr.isen.savi.disney_app.ui.theme.Disney_AppTheme
import fr.isen.savi.disney_app.viewmodel.AuthViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val authViewModel = AuthViewModel()
        if (authViewModel.user.value != null) {
            startActivity(Intent(this, UniverseDetailActivity::class.java))
            finish()
            return
        }

        enableEdgeToEdge()
        setContent {
            Disney_AppTheme {
                val authViewModel: AuthViewModel = viewModel()

                LoginScreen(
                    authViewModel = authViewModel,
                    onLoginSuccess = {
                        // On lance le catalogue
                        val intent = Intent(this, UniverseDetailActivity::class.java)
                        startActivity(intent)
                        finish()
                    },
                    onRegisterClick = {
                    }
                )
            }
        }
    }
}