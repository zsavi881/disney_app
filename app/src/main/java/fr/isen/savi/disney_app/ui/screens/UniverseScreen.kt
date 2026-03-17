package fr.isen.savi.disney_app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import fr.isen.savi.disney_app.R
import fr.isen.savi.disney_app.model.Film
import fr.isen.savi.disney_app.viewmodel.UniverseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UniverseScreen(
    universeViewModel: UniverseViewModel,
    onFilmClick: (String) -> Unit,
    onProfileClick: () -> Unit,
    innerPadding: PaddingValues
) {
    val categories by universeViewModel.categories.collectAsState()
    val expandedItems = universeViewModel.expandedItems

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(id = R.string.app_name),
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(onClick = onProfileClick) {
                        Icon(Icons.Default.AccountCircle, contentDescription = "Profil")
                    }
                }
            )
        }
    ) { padding ->
        if (categories.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                categories.forEach { categorie ->

                    item(key = "cat_${categorie.categorie}") {
                        val isExpanded = expandedItems.contains(categorie.categorie)
                        ListItem(
                            headlineContent = {
                                Text(
                                    categorie.categorie,
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            },
                            trailingContent = {
                                Icon(if (isExpanded) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowRight, null)
                            },
                            modifier = Modifier
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f))
                                .clickable {
                                    if (isExpanded) expandedItems.remove(categorie.categorie)
                                    else expandedItems.add(categorie.categorie)
                                }
                        )
                    }

                    if (expandedItems.contains(categorie.categorie)) {
                        categorie.franchises.forEach { franchise ->

                            item(key = "fran_${franchise.nom}") {
                                val isExpanded = expandedItems.contains(franchise.nom)
                                ListItem(
                                    headlineContent = {
                                        Text(
                                            franchise.nom,
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold
                                        )
                                    },
                                    leadingContent = { Text("🎬", fontSize = 20.sp) },
                                    trailingContent = {
                                        Icon(if (isExpanded) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowRight, null)
                                    },
                                    modifier = Modifier
                                        .padding(start = 12.dp, top = 2.dp)
                                        .clickable {
                                            if (isExpanded) expandedItems.remove(franchise.nom)
                                            else expandedItems.add(franchise.nom)
                                        }
                                )
                            }

                            if (expandedItems.contains(franchise.nom)) {

                                items(franchise.films) { film ->
                                    FilmRow(
                                        film = film,
                                        universeViewModel = universeViewModel, // Passé ici
                                        onClick = { onFilmClick(film.getStableId()) }
                                    )
                                }


                                franchise.sous_sagas.forEach { saga ->
                                    item(key = "saga_${saga.id}") {
                                        val isExpanded = expandedItems.contains(saga.id)
                                        ListItem(
                                            headlineContent = {
                                                Text(
                                                    saga.nom,
                                                    style = MaterialTheme.typography.bodyLarge,
                                                    fontWeight = FontWeight.Medium
                                                )
                                            },
                                            leadingContent = { Text("   ↳", color = MaterialTheme.colorScheme.outline) },
                                            modifier = Modifier
                                                .padding(start = 24.dp)
                                                .clickable {
                                                    if (isExpanded) expandedItems.remove(saga.id)
                                                    else expandedItems.add(saga.id)
                                                }
                                        )
                                    }

                                    // Films de la sous-saga
                                    if (expandedItems.contains(saga.id)) {
                                        items(saga.films) { film ->
                                            FilmRow(
                                                film = film,
                                                universeViewModel = universeViewModel, // Passé ici
                                                indent = 48,
                                                onClick = { onFilmClick(film.getStableId()) }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FilmRow(
    film: Film,
    universeViewModel: UniverseViewModel,
    indent: Int = 24,
    onClick: () -> Unit
) {
    var displayImageUrl by remember { mutableStateOf(film.imageUrl) }

    // Recherche automatique si l'URL est vide
    LaunchedEffect(film.title) {
        if (displayImageUrl.isEmpty()) {
            displayImageUrl = universeViewModel.fetchImageUrl(film.title)
        }
    }

    ListItem(
        modifier = Modifier
            .padding(start = indent.dp, top = 4.dp, bottom = 4.dp, end = 8.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() },
        leadingContent = {
            AsyncImage(
                model = displayImageUrl.ifEmpty { "https://via.placeholder.com/150x225?text=..." },
                contentDescription = "Affiche de ${film.title}",
                modifier = Modifier
                    .width(50.dp)
                    .height(75.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentScale = ContentScale.Crop
            )
        },
        headlineContent = {
            Text(film.title, fontWeight = FontWeight.SemiBold)
        },
        supportingContent = {
            Text(
                text = "${film.genre} • ${film.releaseDate}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    )
}