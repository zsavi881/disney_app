package fr.isen.savi.disney_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import fr.isen.savi.disney_app.navigation.AppNavGraph
import fr.isen.savi.disney_app.ui.theme.Disney_AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Disney_AppTheme {
                AppNavGraph()
            }
        }
    }
}