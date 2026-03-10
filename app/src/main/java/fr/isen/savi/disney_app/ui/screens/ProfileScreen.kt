package fr.isen.savi.disney_app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.isen.savi.disney_app.model.Film
import fr.isen.savi.disney_app.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel,
    onLogout: () -> Unit
) {
    // Observation des états du ViewModel
    val userProfile by profileViewModel.userProfile.collectAsState()
    val ownedFilms by profileViewModel.ownedFilms.collectAsState()
    val watchedFilms by profileViewModel.watchedFilms.collectAsState()
    val wishlistFilms by profileViewModel.wishlistFilms.collectAsState()
    val toGetRidFilms by profileViewModel.toGetRidFilms.collectAsState()

    // Chargement automatique du profil au lancement de l'écran
    LaunchedEffect(Unit) {
        profileViewModel.loadProfile() // Plus besoin de passer "1", le VM gère l'ID Firebase
    }

    // Gestion de l'état de chargement
    if (userProfile == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val profile = userProfile!!

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Mon Profil Disney",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = profile.displayName,
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = profile.email,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Se déconnecter")
            }
        }

        // Section : Mes films possédés
        item {
            FilmSection(title = "Ma Collection (DVD/Blu-ray)", films = ownedFilms)
        }

        // Section : Films vus
        item {
            FilmSection(title = "Films déjà vus", films = watchedFilms)
        }

        // Section : Liste de souhaits
        item {
            FilmSection(title = "Ma liste à voir", films = wishlistFilms)
        }

        // Section : À se débarrasser
        item {
            FilmSection(title = "Films dont je souhaite me séparer", films = toGetRidFilms)
        }
    }
}

@Composable
fun FilmSection(title: String, films: List<Film>) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (films.isEmpty()) {
                Text(
                    text = "Aucun film dans cette catégorie",
                    style = MaterialTheme.typography.bodySmall
                )
            } else {
                films.forEach { film ->
                    Text(
                        text = "• ${film.title}",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                }
            }
        }
    }
}