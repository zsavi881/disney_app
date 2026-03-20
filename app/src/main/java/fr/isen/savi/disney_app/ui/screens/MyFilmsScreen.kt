package fr.isen.savi.disney_app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// N'oublie pas d'importer ton modèle Film ici
// import fr.isen.savi.disney_app.model.Film

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyFilmsScreen(
    title: String,
    films: List<fr.isen.savi.disney_app.model.Film>, // Utilisation du modèle Film
    onBack: () -> Unit
) {
    val duckBlue = Color(0xFF0077B6)
    val backgroundColor = Color(0xFFF8FBFF)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = title,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Retour",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = duckBlue)
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(backgroundColor)
        ) {
            if (films.isEmpty()) {
                // Écran vide si la liste est vide
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Movie,
                        contentDescription = null,
                        modifier = Modifier.size(80.dp),
                        tint = Color.LightGray
                    )
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = "Ta collection est vide pour le moment",
                        color = Color.Gray,
                        fontWeight = FontWeight.Medium
                    )
                }
            } else {
                // Grille de films
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(films) { film ->
                        FilmCard(film)
                    }
                }
            }
        }
    }
}

@Composable
fun FilmCard(film: fr.isen.savi.disney_app.model.Film) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp), // Hauteur fixe pour une grille harmonieuse
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            // Zone Image / Poster
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color(0xFFE2E8F0)),
                contentAlignment = Alignment.Center
            ) {
                // Note : Plus tard, utilise Coil pour charger film.posterUrl
                Icon(
                    imageVector = Icons.Default.Movie,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp),
                    tint = Color.Gray
                )
            }

            // Zone Texte
            Column(
                modifier = Modifier
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = film.title ?: "Titre inconnu",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color(0xFF003049),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis // Coupe le texte proprement s'il est trop long
                )
            }
        }
    }
}