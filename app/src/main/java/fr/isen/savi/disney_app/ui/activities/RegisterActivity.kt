package fr.isen.savi.disney_app.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.viewmodel.compose.viewModel
import fr.isen.savi.disney_app.BaseActivity
import fr.isen.savi.disney_app.MainActivity
import fr.isen.savi.disney_app.ui.screens.RegisterScreen
import fr.isen.savi.disney_app.viewmodel.AuthViewModel

class RegisterActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setAppContent {
            val authViewModel: AuthViewModel = viewModel()

            RegisterScreen(
                authViewModel = authViewModel,
                onRegisterSuccess = {
                    val intent = Intent(this, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    startActivity(intent)
                    finish()
                },
                onBackToLogin = {
                    finish()
                }
            )
        }
    }
}