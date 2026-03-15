package fr.isen.savi.disney_app.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.lifecycle.viewmodel.compose.viewModel
import fr.isen.savi.disney_app.MainActivity
import fr.isen.savi.disney_app.ui.screens.RegisterScreen
import fr.isen.savi.disney_app.viewmodel.AuthViewModel
import fr.isen.savi.disney_app.BaseActivity



class RegisterActivity : BaseActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setAppContent {
            val authViewModel: AuthViewModel = viewModel()

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
                        val intent = Intent(this, MainActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        startActivity(intent)
                        finish()
                    }
                )
            }
        }
    }
}