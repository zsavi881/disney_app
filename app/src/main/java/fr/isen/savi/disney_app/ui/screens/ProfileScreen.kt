package fr.isen.savi.disney_app.ui.screens

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
    val toGetRidFilms by profileViewModel.toGetRidFilms.collectAsState()

    LaunchedEffect(Unit) {
        profileViewModel.loadProfile("1")
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Profile",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = userProfile.displayName,
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = userProfile.email,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        item {
            FilmSection(
                title = "Owned films",
                films = ownedFilms
            )
        }

        item {
            FilmSection(
                title = "Watched films",
                films = watchedFilms
            )
        }

        item {
            FilmSection(
                title = "Want to watch",
                films = wishlistFilms
            )
        }

        item {
            FilmSection(
                title = "Want to get rid of",
                films = toGetRidFilms
            )
        }

        item {
            Button(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Logout")
            }
        }
    }
}

@Composable
fun FilmSection(
    title: String,
    films: List<Film>
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (films.isEmpty()) {
                Text(
                    text = "No films",
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                films.forEach { film ->
                    Text(
                        text = "• ${film.title}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                }
            }
        }
    }
}