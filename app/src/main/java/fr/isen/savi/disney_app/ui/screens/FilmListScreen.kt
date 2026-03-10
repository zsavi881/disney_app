package fr.isen.savi.disney_app.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.isen.savi.disney_app.model.Film
import fr.isen.savi.disney_app.viewmodel.FilmListViewModel

@Composable
fun FilmListScreen(
    universeId: String,
    filmListViewModel: FilmListViewModel,
    onFilmClick: (String) -> Unit
) {
    val films by filmListViewModel.films.collectAsState()
    val universeTitle by filmListViewModel.universeTitle.collectAsState()

    LaunchedEffect(universeId) {
        filmListViewModel.loadFilms(universeId)
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = universeTitle.ifBlank { "Films" },
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Films sorted by release date",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        items(films) { film ->
            FilmCard(
                film = film,
                onClick = { onFilmClick(film.id) }
            )
        }
    }
}

@Composable
fun FilmCard(
    film: Film,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = film.title,
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Release date: ${film.releaseDate}",
                style = MaterialTheme.typography.bodyMedium
            )

            film.category?.let {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Category: $it",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            film.saga?.let {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Saga: $it",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}