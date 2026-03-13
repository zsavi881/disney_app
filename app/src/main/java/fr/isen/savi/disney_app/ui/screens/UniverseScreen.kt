package fr.isen.savi.disney_app.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import fr.isen.savi.disney_app.R
import fr.isen.savi.disney_app.viewmodel.UniverseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UniverseScreen(
    universeViewModel: UniverseViewModel,
    onFilmClick: (String) -> Unit,
    onProfileClick: () -> Unit
) {
    val categories by universeViewModel.categories.collectAsState()
    val expandedItems = universeViewModel.expandedItems

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.app_name)) },
                actions = {
                    IconButton(onClick = onProfileClick) {
                        Icon(Icons.Default.AccountCircle, contentDescription = "Profil")
                    }
                }
            )
        }
    ) { padding ->
        if (categories.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(modifier = Modifier.padding(padding).fillMaxSize()) {
                categories.forEach { categorie ->

                    // catégorie
                    item(key = "cat_${categorie.categorie}") {
                        val isExpanded = expandedItems.contains(categorie.categorie)
                        ListItem(
                            headlineContent = { Text(categorie.categorie, style = MaterialTheme.typography.titleLarge) },
                            trailingContent = {
                                Icon(if (isExpanded) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowRight, null)
                            },
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f))
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
                                    headlineContent = { Text(franchise.nom, style = MaterialTheme.typography.titleMedium) },
                                    leadingContent = { Text("📁") },
                                    trailingContent = {
                                        Icon(if (isExpanded) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowRight, null)
                                    },
                                    modifier = Modifier
                                        .padding(start = 16.dp)
                                        .clickable {
                                            if (isExpanded) expandedItems.remove(franchise.nom)
                                            else expandedItems.add(franchise.nom)
                                        }
                                )
                            }

                            if (expandedItems.contains(franchise.nom)) {

                                // Ffilm
                                items(franchise.films) { film ->
                                    ListItem(
                                        headlineContent = { Text(film.title) },
                                        supportingContent = { Text("${film.genre} - ${film.releaseDate}") },
                                        modifier = Modifier
                                            .padding(start = 32.dp)
                                            .clickable { onFilmClick(film.getStableId()) }
                                    )
                                }

                                // sous saga
                                franchise.sous_sagas.forEach { saga ->
                                    item(key = "saga_${saga.id}") {
                                        val isExpanded = expandedItems.contains(saga.id)
                                        ListItem(
                                            headlineContent = { Text(saga.nom, style = MaterialTheme.typography.bodyLarge) },
                                            leadingContent = { Text("  ↳") },
                                            modifier = Modifier
                                                .padding(start = 32.dp)
                                                .clickable {
                                                    if (isExpanded) expandedItems.remove(saga.id)
                                                    else expandedItems.add(saga.id)
                                                }
                                        )
                                    }

                                    // Films de la sous-saga
                                    if (expandedItems.contains(saga.id)) {
                                        items(saga.films) { film ->
                                            ListItem(
                                                headlineContent = { Text(film.title) },
                                                modifier = Modifier
                                                    .padding(start = 64.dp)
                                                    .clickable { onFilmClick(film.getStableId()) }
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