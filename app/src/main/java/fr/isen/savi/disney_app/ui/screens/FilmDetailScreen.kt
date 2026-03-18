package fr.isen.savi.disney_app.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import fr.isen.savi.disney_app.model.UserProfile
import fr.isen.savi.disney_app.viewmodel.FilmDetailViewModel

@Composable
fun FilmDetailScreen(
    filmId: String,
    filmDetailViewModel: FilmDetailViewModel,
    onBack: () -> Unit
){
    val film by filmDetailViewModel.film.collectAsState()
    val userStatus by filmDetailViewModel.userStatusMap.collectAsState()
    val owners by filmDetailViewModel.ownersProfiles.collectAsState()

    val backgroundLight = Color(0xFFE3F2FD)
    val disneyDuckBlue = Color(0xFF0077B6)
    val darkCardBlue = Color(0xFF003049)

    var detailImageUrl by remember(film?.imageUrl) { mutableStateOf(film?.imageUrl ?: "") }

    LaunchedEffect(filmId) {
        filmDetailViewModel.loadFilm(filmId)
    }

    LaunchedEffect(film?.title) {
        if (film != null && detailImageUrl.isEmpty()) {
            detailImageUrl = filmDetailViewModel.fetchImageUrl(film!!.title)
        }
    }

    Scaffold(
        containerColor = backgroundLight
    ) { innerPadding ->
        if (film == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = disneyDuckBlue)
            }
        } else {
            Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {

                Box(Modifier.height(450.dp).fillMaxWidth()) {
                    AsyncImage(
                        model = detailImageUrl.ifEmpty { "https://via.placeholder.com/500x750" },
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    Box(Modifier.fillMaxSize().background(Brush.verticalGradient(
                        0f to Color.Black.copy(0.3f),
                        0.5f to Color.Transparent,
                        1f to backgroundLight
                    )))
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.statusBarsPadding().padding(16.dp).background(Color.White.copy(0.8f), CircleShape)
                    ) {
                        Icon(Icons.Default.ArrowBack, null, tint = disneyDuckBlue)
                    }
                }

                Column(Modifier.padding(horizontal = 24.dp)) {
                    Text(
                        film!!.title.uppercase(),
                        style = MaterialTheme.typography.headlineMedium.copy(letterSpacing = 2.sp),
                        fontWeight = FontWeight.Black,
                        color = darkCardBlue
                    )

                    Row(Modifier.padding(vertical = 12.dp), Arrangement.spacedBy(8.dp)) {
                        InfoBadge(film!!.releaseDate.toString(), disneyDuckBlue)
                        InfoBadge(film!!.genre, Color.Gray)
                    }

                    SectionTitle("MA COLLECTION", disneyDuckBlue)
                    Card(
                        Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(Color.White.copy(0.7f)),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Box(Modifier.padding(16.dp)) {
                            StatusGrid(userStatus, { key ->
                                filmDetailViewModel.updateStatus(filmId, key, !(userStatus[key] ?: false))
                            }, disneyDuckBlue)
                        }
                    }

                    Spacer(Modifier.height(24.dp))

                    SectionTitle("PROPRIÉTAIRES", disneyDuckBlue)
                    if (owners.isEmpty()) {
                        Text("Personne ne possède encore ce film.", color = Color.Gray, fontSize = 14.sp)
                    } else {
                        owners.forEach { OwnerCard(it, disneyDuckBlue, darkCardBlue) }
                    }
                    Spacer(Modifier.height(50.dp))
                }
            }
        }
    }
}

@Composable
fun SectionTitle(text: String, color: Color) {
    Text(text, fontWeight = FontWeight.Black, color = color, letterSpacing = 1.5.sp, modifier = Modifier.padding(bottom = 8.dp))
}

@Composable
fun InfoBadge(text: String, color: Color) {
    Surface(color = color.copy(0.1f), shape = RoundedCornerShape(8.dp)) {
        Text(text, Modifier.padding(horizontal = 10.dp, vertical = 4.dp), color, fontWeight = FontWeight.Bold, fontSize = 12.sp)
    }
}

@Composable
fun OwnerCard(profile: UserProfile, color: Color, darkCardBlue: Color) {
    Card(
        Modifier.fillMaxWidth().padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        ListItem(
            headlineContent = { Text(profile.displayName, fontWeight = FontWeight.Bold, color = darkCardBlue) },
            supportingContent = { Text(profile.email, fontSize = 12.sp, color = Color.Gray) },
            leadingContent = {
                Surface(Modifier.size(40.dp), CircleShape, color.copy(0.1f)) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = color)
                    }
                }
            }
        )
    }
}

@Composable
fun StatusGrid(userStatus: Map<String, Boolean>, onToggle: (String) -> Unit, color: Color) {
    val items = listOf(
        Triple("watched", "Déjà vu", Icons.Default.CheckCircle),
        Triple("wantToWatch", "À voir", Icons.Default.Favorite),
        Triple("ownPhysical", "Possède DVD", Icons.Default.Album),
        Triple("wantedPhysical", "Liste d'achat", Icons.Default.ShoppingCart)
    )

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        items.chunked(2).forEach { row ->
            Row(Modifier.fillMaxWidth(), Arrangement.spacedBy(10.dp)) {
                row.forEach { (key, label, icon) ->
                    val active = userStatus[key] ?: false
                    Surface(
                        Modifier.weight(1f).height(54.dp).clickable { onToggle(key) },
                        color = if (active) color else Color.White,
                        shape = RoundedCornerShape(14.dp),
                        border = if (!active) BorderStroke(1.dp, Color.LightGray.copy(0.5f)) else null,
                        shadowElevation = if (active) 4.dp else 0.dp
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(4.dp)
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                                tint = if (active) Color.White else (if (key == "ownPhysical") Color(0xFFE91E63) else Color.Gray)
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                text = label,
                                color = if (active) Color.White else Color.DarkGray,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }
                }
            }
        }
    }
}