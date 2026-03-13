package fr.isen.savi.disney_app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import fr.isen.savi.disney_app.R
import fr.isen.savi.disney_app.model.Film
import fr.isen.savi.disney_app.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel,
    onLogout: () -> Unit
) {
    val userProfile by profileViewModel.userProfile.collectAsState()
    val ownedFilms by profileViewModel.ownedFilms.collectAsState()
    val watchedFilms by profileViewModel.watchedFilms.collectAsState()
    val wishlistFilms by profileViewModel.wishlistFilms.collectAsState()

    val isDarkMode by profileViewModel.isDarkMode.collectAsState()

    LaunchedEffect(Unit) {
        profileViewModel.loadProfile()
    }

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
            Text(text = profile.displayName, style = MaterialTheme.typography.titleLarge)
            Text(
                text = profile.email,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(text = "Réglages", style = MaterialTheme.typography.titleMedium)
            Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Mode Sombre")
                    Switch(
                        checked = isDarkMode,
                        onCheckedChange = { profileViewModel.toggleDarkMode() }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    profileViewModel.logout { onLogout() }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Se déconnecter")
            }
        }

        item { FilmSection(title = "Ma Collection (DVD/Blu-ray)", films = ownedFilms) }
        item { FilmSection(title = "Films déjà vus", films = watchedFilms) }
        item { FilmSection(title = "Ma liste à voir", films = wishlistFilms) }
    }
}

@Composable
fun FilmSection(title: String, films: List<Film>) {
    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    )) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(8.dp))
            if (films.isEmpty()) {
                Text(text = "Aucun film répertorié", style = MaterialTheme.typography.bodySmall)
            } else {
                films.forEach { film ->
                    Text(text = "• ${film.title}", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}