package fr.isen.savi.disney_app

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import fr.isen.savi.disney_app.repository.FirebaseRepository
import fr.isen.savi.disney_app.ui.theme.Disney_AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // --- PARTIE FIREBASE (TON TRAVAIL) ---
        val repo = FirebaseRepository()

        // On lance l'importation des données vers la Realtime Database.
        // Une fois que tu vois les données sur la console Firebase,
        // tu pourras commenter cette ligne.
        repo.initialPopulate()
        Log.d("MainActivity", "Tentative d'importation des films lancée...")
        // -------------------------------------

        setContent {
            Disney_AppTheme {
                // Pour l'instant, on laisse le Greeting ou on appelle l'écran de Zoé
                // Si Zoé t'a donné un écran de départ (ex: LoginScreen), appelle-le ici.
                Greeting(name = "Disney App - Firebase Connectée")
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    androidx.compose.material3.Text(text = "Bienvenue sur $name !")
}