package fr.isen.savi.disney_app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.isen.savi.disney_app.viewmodel.FilmDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilmDetailScreen(
    filmId: String,
    filmDetailViewModel: FilmDetailViewModel,
    innerPadding: PaddingValues,
    onBack: () -> Unit
) {
    val film by filmDetailViewModel.film.collectAsState()
    val userStatus by filmDetailViewModel.userStatusMap.collectAsState()

    val owners by filmDetailViewModel.ownersProfiles.collectAsState()

    LaunchedEffect(filmId) {
        filmDetailViewModel.loadFilm(filmId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(film?.title ?: "Détails") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Retour")
                    }
                }
            )
        }
    ) { padding ->
        if (film == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            val currentFilm = film!!

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(padding)
                    .padding(horizontal = 24.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

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

                Text(text = "Propriétaires de ce film", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(12.dp))

                if (owners.isEmpty()) {
                    Text(
                        "Personne ne possède ce film pour le moment.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    owners.forEach { profile ->
                        ListItem(
                            // j'affiche le nom à la place de l'id
                            headlineContent = { Text(profile.displayName) },
                            supportingContent = { Text(profile.email) },
                            leadingContent = {
                                Icon(Icons.Default.Person, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(40.dp))
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