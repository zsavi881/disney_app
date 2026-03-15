package fr.isen.savi.disney_app.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.isen.savi.disney_app.model.Film
import fr.isen.savi.disney_app.viewmodel.FilmListViewModel

@Composable
fun FilmListScreen(
    universeId: String,
    filmListViewModel: FilmListViewModel,
    innerPadding: PaddingValues,
    onFilmClick: (String) -> Unit
) {
    val films by filmListViewModel.films.collectAsState()

    LaunchedEffect(universeId) {
        filmListViewModel.loadFilmsByUniverse(universeId)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .padding(innerPadding),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = universeId.replace("_", " ").uppercase(),
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Liste des films de cet univers",
                style = MaterialTheme.typography.bodyMedium
            )
        }


        items(films) { film ->
            Spacer(modifier = Modifier.height(8.dp))
            FilmCard(

                film = film,
                onClick = { onFilmClick(film.getStableId()) }
            )
        }
    }
}

@Composable
fun FilmCard(film: Film, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = film.title, style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Année : ${film.releaseDate}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Genre : ${film.genre}", style = MaterialTheme.typography.bodySmall)
        }
    }
}