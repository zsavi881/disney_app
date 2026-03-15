package fr.isen.savi.disney_app.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.lifecycle.viewmodel.compose.viewModel
import fr.isen.savi.disney_app.MainActivity
import fr.isen.savi.disney_app.ui.screens.ProfileScreen
import fr.isen.savi.disney_app.viewmodel.ProfileViewModel
import fr.isen.savi.disney_app.BaseActivity

class ProfileActivity : BaseActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        //val profileViewModel = ProfileViewModel()//instencie dans onCreate
        setAppContent {
            val profileViewModel: ProfileViewModel = viewModel()// instencie dans Set
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Film") }
                    )
                }
            ) { innerPadding ->
                    ProfileScreen(
                        profileViewModel = profileViewModel,
                        onLogout = {
                            val intent = Intent(this, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            finish()
                        },
                        innerPadding = innerPadding
                    )
            }
        }
    }
}