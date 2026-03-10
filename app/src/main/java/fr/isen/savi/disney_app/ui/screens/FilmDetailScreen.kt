package fr.isen.savi.disney_app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.isen.savi.disney_app.viewmodel.FilmDetailViewModel

@Composable
fun FilmDetailScreen(
    filmId: String,
    filmDetailViewModel: FilmDetailViewModel
) {
    // On observe les données du ViewModel
    val film by filmDetailViewModel.film.collectAsState()
    val userStatus by filmDetailViewModel.userStatusMap.collectAsState()
    val owners by filmDetailViewModel.owners.collectAsState()

    // On charge le film au démarrage
    LaunchedEffect(filmId) {
        filmDetailViewModel.loadFilm(filmId)
    }

    if (film == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Film not found",
                style = MaterialTheme.typography.headlineSmall
            )
        }
        return
    }

    val currentFilm = film!! // On sait qu'il n'est pas nul ici

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

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Universe: ${currentFilm.universeName}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Release date: ${currentFilm.releaseDate}",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Duration: ${currentFilm.durationMinutes} min",
                    style = MaterialTheme.typography.bodyLarge
                )
                currentFilm.category?.let {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Category: $it", style = MaterialTheme.typography.bodyLarge)
                }
                currentFilm.saga?.let {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Saga: $it", style = MaterialTheme.typography.bodyLarge)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Synopsis", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = currentFilm.synopsis, style = MaterialTheme.typography.bodyLarge)

        Spacer(modifier = Modifier.height(24.dp))

        // --- SECTION PROPRIÉTAIRES ---
        Text(text = "Owners", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(12.dp))

        if (owners.isEmpty()) {
            Text(
                text = "No owner found for this film",
                style = MaterialTheme.typography.bodyMedium
            )
        } else {
            owners.forEach { ownerId ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "User ID: $ownerId", style = MaterialTheme.typography.titleMedium)
                        Text(text = "Owns a physical copy", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- SECTION STATUTS (MON COMPTE) ---
        Text(text = "My status", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(12.dp))

        // Bouton Watched
        val isWatched = userStatus["watched"] == true
        Button(
            onClick = { filmDetailViewModel.updateStatus(filmId, "watched", !isWatched) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isWatched) "Watched ✓" else "Mark as Watched")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Bouton Want to Watch
        val wantToWatch = userStatus["wantToWatch"] == true
        Button(
            onClick = { filmDetailViewModel.updateStatus(filmId, "wantToWatch", !wantToWatch) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (wantToWatch) "In Watchlist ✓" else "Add to Watchlist")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Bouton Own Physical
        val ownsPhysical = userStatus["ownPhysical"] == true
        Button(
            onClick = { filmDetailViewModel.updateStatus(filmId, "ownPhysical", !ownsPhysical) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (ownsPhysical) "Own DVD/Blu-ray ✓" else "I own the DVD/Blu-ray")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Bouton Get Rid
        val wantToGetRid = userStatus["wantToGetRid"] == true
        Button(
            onClick = { filmDetailViewModel.updateStatus(filmId, "wantToGetRid", !wantToGetRid) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (wantToGetRid) "Wants to get rid of it ✓" else "I want to get rid of it")
        }
    }
}