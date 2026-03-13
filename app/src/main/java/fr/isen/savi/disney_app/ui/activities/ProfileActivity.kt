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
import fr.isen.savi.disney_app.ui.theme.DisneyAppTheme
import fr.isen.savi.disney_app.viewmodel.ProfileViewModel
import fr.isen.savi.disney_app.BaseActivity

class ProfileActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        //val profileViewModel = ProfileViewModel()//instencie dans onCreate
        setAppContent {
            val profileViewModel: ProfileViewModel = viewModel()// instencie dans Set

            //val isDarkMode by profileViewModel.isDarkMode.collectAsState()

           // DisneyAppTheme(darkTheme = isDarkMode) {
                ProfileScreen(
                    profileViewModel = profileViewModel,
                    onLogout = {
                        val intent = Intent(this, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    }
                )
            //}
        }
    }
}