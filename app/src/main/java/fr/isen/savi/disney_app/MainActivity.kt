package fr.isen.savi.disney_app

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import fr.isen.savi.disney_app.repository.FirebaseRepository
import fr.isen.savi.disney_app.navigation.AppNavGraph
import fr.isen.savi.disney_app.ui.theme.Disney_AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // --- PARTIE FIREBASE (TON TRAVAIL) ---
        val repo = FirebaseRepository()

        // On importe les données une seule fois (tu pourras commenter après vérification, c'est fait )
        //repo.initialPopulate()
        Log.d("MainActivity", "Tentative d'importation des films lancée...")
        // -------------------------------------

        setContent {
            Disney_AppTheme {
                // On appelle le NavGraph qui va gérer l'affichage :
                // Il commencera par l'écran de Login par défaut.
                AppNavGraph()
            }
        }
    }
}