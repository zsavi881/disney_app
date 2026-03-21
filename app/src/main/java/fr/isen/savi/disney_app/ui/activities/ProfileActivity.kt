package fr.isen.savi.disney_app.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import fr.isen.savi.disney_app.BaseActivity
import fr.isen.savi.disney_app.MainActivity
import fr.isen.savi.disney_app.ui.screens.ProfileScreen
import fr.isen.savi.disney_app.viewmodel.ProfileViewModel

class ProfileActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setAppContent {
            val mViewModel: ProfileViewModel = viewModel()
            val lifecycleOwner = LocalLifecycleOwner.current

            DisposableEffect(lifecycleOwner) {
                val observer = LifecycleEventObserver { _, event ->
                    if (event == Lifecycle.Event.ON_RESUME) {
                        mViewModel.loadProfile()
                    }
                }
                lifecycleOwner.lifecycle.addObserver(observer)
                onDispose {
                    lifecycleOwner.lifecycle.removeObserver(observer)
                }
            }

            ProfileScreen(
                profileViewModel = mViewModel,
                onLogout = {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finishAffinity()
                },
                onNavigateToCatalog = {
                    finish()
                },
                onNavigateToMyFilms = { type ->
                    val intent = Intent(this, MyFilmsActivity::class.java).apply {
                        putExtra("FILM_TYPE", type)
                    }
                    startActivity(intent)
                },
                innerPadding = PaddingValues(0.dp)
            )
        }
    }
}