package fr.isen.savi.disney_app

import android.content.Intent
import android.os.Bundle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import fr.isen.savi.disney_app.ui.activities.UniverseDetailActivity
import fr.isen.savi.disney_app.ui.screens.LoginScreen
import fr.isen.savi.disney_app.ui.screens.RegisterScreen
import fr.isen.savi.disney_app.viewmodel.AuthViewModel

class MainActivity : BaseActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setAppContent {
            val authViewModel: AuthViewModel = viewModel()
            val showRegister = remember { mutableStateOf(false) }
            val currentUser by authViewModel.user.collectAsState()

            LaunchedEffect(currentUser) {
                if (currentUser != null) {
                    startActivity(Intent(this@MainActivity, UniverseDetailActivity::class.java))
                    finish()
                }
            }
            if (!showRegister.value) {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("Connexion") }
                        )
                    }
                ) { innerPadding ->
                    LoginScreen(
                        authViewModel = authViewModel,
                        innerPadding = innerPadding,
                        onLoginSuccess = {
                            val intent = Intent(this@MainActivity, UniverseDetailActivity::class.java)
                            startActivity(intent)
                            finish()
                        },
                        onRegisterClick = {
                            showRegister.value = true
                        }
                    )
                }
            } else {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("Inscription") }
                        )
                    }
                ) { innerPadding ->
                    RegisterScreen(
                        authViewModel = authViewModel,
                        innerPadding = innerPadding,
                        onRegisterSuccess = {
                            val intent = Intent(this@MainActivity, UniverseDetailActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    )
                }
            }
        }
    }
}