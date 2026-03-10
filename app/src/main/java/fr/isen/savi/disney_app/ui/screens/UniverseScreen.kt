package fr.isen.savi.disney_app.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.isen.savi.disney_app.model.Categorie
import fr.isen.savi.disney_app.model.Film
import fr.isen.savi.disney_app.model.Franchise
import fr.isen.savi.disney_app.model.SousSaga
import fr.isen.savi.disney_app.viewmodel.UniverseViewModel

@Composable
fun UniverseScreen(
    universeViewModel: UniverseViewModel,
    onFilmClick: (String) -> Unit, // Changé pour aller directement au détail du film
    onProfileClick: () -> Unit
) {
    // On récupère les catégories depuis le ViewModel
    val categories by universeViewModel.categories.collectAsState()

    // On utilise les états dépliables du ViewModel pour que ça survive aux rotations
    val expandedItems = universeViewModel.expandedItems

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Button(
                onClick = onProfileClick,
                modifier = Modifier.padding(16.dp).fillMaxWidth()
            ) {
                Text("Go to profile")
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Text(
                    text = "Disney Catalogue",
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            items(categories) { categorie ->
                CategoryItem(categorie, expandedItems, onFilmClick)
            }
        }
    }
}

@Composable
fun CategoryItem(
    categorie: Categorie,
    expandedItems: MutableList<String>,
    onFilmClick: (String) -> Unit
) {
    val isExpanded = expandedItems.contains(categorie.categorie)

    Column {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    if (isExpanded) expandedItems.remove(categorie.categorie)
                    else expandedItems.add(categorie.categorie)
                }
        ) {
            Text(
                text = categorie.categorie,
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.titleLarge
            )
        }

        if (isExpanded) {
            categorie.franchises.forEach { franchise ->
                FranchiseItem(franchise, expandedItems, onFilmClick)
            }
        }
    }
}

@Composable
fun FranchiseItem(
    franchise: Franchise,
    expandedItems: MutableList<String>,
    onFilmClick: (String) -> Unit
) {
    val isExpanded = expandedItems.contains(franchise.nom)

    Column(modifier = Modifier.padding(start = 16.dp, top = 8.dp)) {
        Text(
            text = "▶ ${franchise.nom}",
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    if (isExpanded) expandedItems.remove(franchise.nom)
                    else expandedItems.add(franchise.nom)
                }
                .padding(8.dp),
            style = MaterialTheme.typography.titleMedium
        )

        if (isExpanded) {
            franchise.sous_sagas?.let { sagas ->
                sagas.forEach { saga ->
                    SagaItem(saga, expandedItems, onFilmClick)
                }
            } ?: run {
                FilmsList(franchise.tousLesFilms(), onFilmClick)
            }
        }
    }
}

@Composable
fun SagaItem(
    saga: SousSaga,
    expandedItems: MutableList<String>,
    onFilmClick: (String) -> Unit
) {
    val isExpanded = expandedItems.contains(saga.nom)

    Column(modifier = Modifier.padding(start = 16.dp)) {
        Text(
            text = "• ${saga.nom}",
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    if (isExpanded) expandedItems.remove(saga.nom)
                    else expandedItems.add(saga.nom)
                }
                .padding(4.dp),
            style = MaterialTheme.typography.bodyLarge
        )

        if (isExpanded) {
            FilmsList(saga.films, onFilmClick)
        }
    }
}

@Composable
fun FilmsList(films: List<Film>, onFilmClick: (String) -> Unit) {
    Column(modifier = Modifier.padding(start = 16.dp)) {
        films.forEach { film ->
            Text(
                text = film.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onFilmClick(film.getStableId()) }
                    .padding(vertical = 4.dp),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}