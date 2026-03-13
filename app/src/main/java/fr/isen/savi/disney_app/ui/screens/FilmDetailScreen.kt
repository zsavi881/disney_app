package fr.isen.savi.disney_app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.isen.savi.disney_app.viewmodel.FilmDetailViewModel
import fr.isen.savi.disney_app.ui.theme.DisneyAppTheme
import fr.isen.savi.disney_app.viewmodel.ProfileViewModel

@Composable
fun FilmDetailScreen(
    filmId: String,
    filmDetailViewModel: FilmDetailViewModel
) {
    val profileViewModel = ProfileViewModel()
    val film by filmDetailViewModel.film.collectAsState()
    val userStatus by filmDetailViewModel.userStatusMap.collectAsState()
    val owners by filmDetailViewModel.owners.collectAsState()
    val isDarkMode by profileViewModel.isDarkMode.collectAsState()

    LaunchedEffect(filmId) {
        filmDetailViewModel.loadFilm(filmId)
    }

    if (film == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val currentFilm = film!!

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = currentFilm.title,
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Année de sortie : ${currentFilm.releaseDate}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Genre : ${currentFilm.genre}",
                    style = MaterialTheme.typography.bodyLarge
                )
                if (currentFilm.numero > 0) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Numéro dans la saga : ${currentFilm.numero}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(text = "Ma collection", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(12.dp))

        StatusButton(
            label = "Vu",
            isActive = userStatus["watched"] == true,
            onClick = { filmDetailViewModel.updateStatus(filmId, "watched", !(userStatus["watched"] == true)) }
        )

        StatusButton(
            label = "Dans ma liste à voir",
            isActive = userStatus["wantToWatch"] == true,
            onClick = { filmDetailViewModel.updateStatus(filmId, "wantToWatch", !(userStatus["wantToWatch"] == true)) }
        )

        StatusButton(
            label = "Je possède le DVD/Blu-ray",
            isActive = userStatus["ownPhysical"] == true,
            onClick = { filmDetailViewModel.updateStatus(filmId, "ownPhysical", !(userStatus["ownPhysical"] == true)) }
        )

        StatusButton(
            label = "Je le veux",
            isActive = userStatus["wantedPhysical"] == true,
            onClick = { filmDetailViewModel.updateStatus(filmId, "wantedPhysical", !(userStatus["wantedPhysical"] == true)) }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(text = "Propriétaires à proximité", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(12.dp))

        if (owners.isEmpty()) {
            Text("Personne ne possède ce film pour le moment.", style = MaterialTheme.typography.bodyMedium)
        } else {
            owners.forEach { ownerId ->
                ListItem(
                    headlineContent = { Text("Utilisateur : $ownerId") },
                    supportingContent = { Text("Possède une copie physique") },
                    leadingContent = { Icon(androidx.compose.material.icons.Icons.Default.Person, contentDescription = null) }
                )
            }
        }
    }
}

@Composable
fun StatusButton(label: String, isActive: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
        colors = if (isActive) ButtonDefaults.buttonColors() else ButtonDefaults.filledTonalButtonColors()
    ) {
        Text(if (isActive) "$label ✓" else label)
    }
}