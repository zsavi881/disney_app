package fr.isen.savi.disney_app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
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
    var selectedCategoryName by remember { mutableStateOf<String?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    val backgroundLight = Color(0xFFE3F2FD)
    val disneyDuckBlue = Color(0xFF0077B6)
    val darkCardBlue = Color(0xFF003049)
    val disneyGold = Color(0xFFFFC300)

    val bannerFilms = remember(categories) {
        categories.flatMap { it.franchises }
            .flatMap { it.films }
            .distinctBy { it.title }
            .take(6)
    }

    val pagerState = rememberPagerState(pageCount = { bannerFilms.size })

    Scaffold(
        containerColor = backgroundLight,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "BIENVENUE,",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Black,
                        color = disneyDuckBlue,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "EXPLOREZ LA MAGIE",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = disneyDuckBlue.copy(alpha = 0.6f)
                    )
                }

                Surface(
                    onClick = onProfileClick,
                    shape = CircleShape,
                    color = Color.White,
                    shadowElevation = 4.dp,
                    modifier = Modifier.size(45.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Profil",
                            tint = disneyGold,
                            modifier = Modifier.size(35.dp)
                        )
                    }
                }
            }
        }
    ) { padding ->
        if (categories.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = disneyDuckBlue)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(bottom = 32.dp)
            ) {
                // carrousel
                item {
                    if (bannerFilms.isNotEmpty()) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(vertical = 8.dp)
                        ) {
                            HorizontalPager(
                                state = pagerState,
                                modifier = Modifier.fillMaxWidth().height(220.dp),
                                contentPadding = PaddingValues(horizontal = 40.dp),
                                pageSpacing = 16.dp,
                                userScrollEnabled = true,
                                beyondViewportPageCount = 1,
                                flingBehavior = PagerDefaults.flingBehavior(state = pagerState)
                            ) { page ->
                                val film = bannerFilms[page]
                                var bannerImageUrl by remember(film.title) { mutableStateOf(film.imageUrl) }

                                LaunchedEffect(film.title) {
                                    if (bannerImageUrl.isEmpty()) {
                                        bannerImageUrl = universeViewModel.fetchImageUrl(film.title)
                                    }
                                }

                                Card(
                                    modifier = Modifier.fillMaxSize().clickable { onFilmClick(film.getStableId()) },
                                    shape = RoundedCornerShape(24.dp),
                                    colors = CardDefaults.cardColors(containerColor = darkCardBlue),
                                    elevation = CardDefaults.cardElevation(6.dp)
                                ) {
                                    Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
                                        AsyncImage(
                                            model = bannerImageUrl.ifEmpty { "https://via.placeholder.com/500x750?text=..." },
                                            contentDescription = null,
                                            modifier = Modifier.width(110.dp).fillMaxHeight().clip(RoundedCornerShape(topStart = 24.dp, bottomStart = 24.dp)),
                                            contentScale = ContentScale.Crop
                                        )
                                        Column(modifier = Modifier.weight(1f).padding(16.dp)) {
                                            Text("À NE PAS MANQUER", color = Color(0xFF00B4D8), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                                            Text(film.title, color = Color.White, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, maxLines = 2)
                                            Spacer(modifier = Modifier.height(12.dp))
                                            Button(
                                                onClick = { onFilmClick(film.getStableId()) },
                                                colors = ButtonDefaults.buttonColors(containerColor = disneyDuckBlue),
                                                shape = RoundedCornerShape(12.dp)
                                            ) {
                                                Text("Découvrir", fontSize = 12.sp)
                                            }
                                        }
                                    }
                                }
                            }

                            Row(
                                Modifier.padding(top = 12.dp).fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                repeat(bannerFilms.size) { iteration ->
                                    val color = if (pagerState.currentPage == iteration) disneyDuckBlue else Color.LightGray.copy(alpha = 0.5f)
                                    Box(modifier = Modifier.padding(4.dp).clip(CircleShape).background(color).size(8.dp))
                                }
                            }
                        }
                    }
                }

                // recherche
                item {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                        placeholder = { Text("Rechercher un film...", color = disneyDuckBlue.copy(alpha = 0.5f)) },
                        leadingIcon = { Icon(Icons.Default.Search, null, tint = disneyDuckBlue) },
                        shape = RoundedCornerShape(28.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedIndicatorColor = disneyDuckBlue,
                            unfocusedIndicatorColor = Color.Transparent,
                        ),
                        singleLine = true
                    )
                }

                // filtre
                item {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        val filterOptions = listOf("Tout") + categories.map { it.categorie }
                        items(filterOptions) { option ->
                            val isSelected = (option == "Tout" && selectedCategoryName == null) || (option == selectedCategoryName)
                            Surface(
                                modifier = Modifier.height(38.dp).clickable { selectedCategoryName = if (option == "Tout") null else option },
                                shape = RoundedCornerShape(19.dp),
                                color = if (isSelected) disneyDuckBlue else Color.White,
                                shadowElevation = 2.dp
                            ) {
                                Box(modifier = Modifier.padding(horizontal = 20.dp), contentAlignment = Alignment.Center) {
                                    Text(text = option, color = if (isSelected) Color.White else disneyDuckBlue, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                }
                            }
                        }
                    }
                }

                // sections
                categories.forEach { categorie ->
                    val isVisible = selectedCategoryName == null || selectedCategoryName == categorie.categorie
                    if (isVisible) {
                        val filteredFilms = categorie.franchises.flatMap { it.films }.filter { it.title.contains(searchQuery, ignoreCase = true) }

                        if (filteredFilms.isNotEmpty()) {
                            item(key = "section_${categorie.categorie}") {
                                Column(modifier = Modifier.padding(vertical = 14.dp)) {
                                    Text(text = categorie.categorie.uppercase(), style = MaterialTheme.typography.titleSmall, color = disneyDuckBlue, fontWeight = FontWeight.Black, modifier = Modifier.padding(start = 20.dp, bottom = 10.dp))
                                    LazyRow(contentPadding = PaddingValues(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                        items(filteredFilms) { film ->
                                            FilmPosterCard(film, universeViewModel) { onFilmClick(film.getStableId()) }
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
fun FilmPosterCard(film: Film, universeViewModel: UniverseViewModel, onClick: () -> Unit) {
    var displayImageUrl by remember(film.getStableId()) { mutableStateOf(film.imageUrl) }
    LaunchedEffect(film.title) {
        if (displayImageUrl.isEmpty()) displayImageUrl = universeViewModel.fetchImageUrl(film.title)
    }

    Column(modifier = Modifier.width(115.dp).clickable { onClick() }) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            AsyncImage(
                model = displayImageUrl.ifEmpty { "https://via.placeholder.com/150x225?text=..." },
                contentDescription = film.title,
                modifier = Modifier.fillMaxWidth().height(165.dp),
                contentScale = ContentScale.Crop
            )
        }
        Text(text = film.title, color = Color(0xFF00507A), style = MaterialTheme.typography.labelMedium, maxLines = 1, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 6.dp, start = 4.dp))
    }
}