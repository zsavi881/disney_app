package fr.isen.savi.disney_app

import android.content.Intent
import android.os.Bundle
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import fr.isen.savi.disney_app.ui.activities.RegisterActivity
import fr.isen.savi.disney_app.ui.activities.UniverseDetailActivity
import fr.isen.savi.disney_app.ui.screens.LoginScreen
import fr.isen.savi.disney_app.viewmodel.AuthViewModel

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setAppContent {
            val authViewModel: AuthViewModel = viewModel()
            val currentUser by authViewModel.user.collectAsState()

            LaunchedEffect(currentUser) {
                if (currentUser != null) {
                    startActivity(Intent(this@MainActivity, UniverseDetailActivity::class.java))
                    finish()
                }
            }

            LoginScreen(
                onLoginClick = { email, password ->
                    authViewModel.login(email, password)
                },
                onCreateAccountClick = {
                    startActivity(Intent(this@MainActivity, RegisterActivity::class.java))
                }
            )
        }
    }
}