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
    val film by filmDetailViewModel.film.collectAsState()
    val status by filmDetailViewModel.status.collectAsState()

    LaunchedEffect(filmId) {
        filmDetailViewModel.loadFilm(
            filmId = filmId,
            userId = "1"
        )
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

    val currentFilm = film ?: return
    val owners by filmDetailViewModel.owners.collectAsState()

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
                    Text(
                        text = "Category: $it",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                currentFilm.saga?.let {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Saga: $it",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Synopsis",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = currentFilm.synopsis,
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Owners",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (owners.isEmpty()) {
            Text(
                text = "No owner found for this film",
                style = MaterialTheme.typography.bodyMedium
            )
        } else {
            owners.forEach { owner ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = owner.displayName,
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = if (owner.ownPhysical) {
                                "Owns this film"
                            } else {
                                "Does not own this film"
                            },
                            style = MaterialTheme.typography.bodyMedium
                        )

                        if (owner.wantToGetRid) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Wants to get rid of it",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }

        Text(
            text = "My status",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                filmDetailViewModel.updateStatus(
                    userId = "1",
                    filmId = filmId,
                    watched = true
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (status?.watched == true) "Watched ✓" else "Watched")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                filmDetailViewModel.updateStatus(
                    userId = "1",
                    filmId = filmId,
                    wantToWatch = true
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (status?.wantToWatch == true) "Want to watch ✓" else "Want to watch")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                filmDetailViewModel.updateStatus(
                    userId = "1",
                    filmId = filmId,
                    own = true
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (status?.ownPhysical == true) "Own ✓" else "Own DVD / Blu-ray")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                filmDetailViewModel.updateStatus(
                    userId = "1",
                    filmId = filmId,
                    getRid = true
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (status?.wantToGetRid == true) "Want to get rid ✓" else "Want to get rid")
        }
    }
}