package fr.isen.savi.disney_app.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.isen.savi.disney_app.viewmodel.ProfileViewModel
import fr.isen.savi.disney_app.ui.theme.ThemeState
import fr.isen.savi.disney_app.ui.theme.DisneyLightGray

@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel,
    onLogout: () -> Unit,
    onNavigateToCatalog: () -> Unit,
    onNavigateToMyFilms: (String) -> Unit,
    innerPadding: PaddingValues
) {
    val userProfile by profileViewModel.userProfile.collectAsState()
    val ownedFilms by profileViewModel.ownedFilms.collectAsState()
    val watchedFilms by profileViewModel.watchedFilms.collectAsState()
    val isDarkMode by ThemeState.isDarkMode.collectAsState()

    val duckBlue = Color(0xFF0077B6)
    val darkBlue = Color(0xFF003049)
    val deleteRed = Color(0xFFD32F2F)

    LaunchedEffect(Unit) {
        profileViewModel.loadProfile()
    }

    if (userProfile == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = duckBlue)
        }
    } else {
        Box(modifier = Modifier.fillMaxSize().background(Color(0xFFF8FBFF))) {

            // 1. LE FOND DÉGRADÉ
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .background(brush = Brush.verticalGradient(listOf(duckBlue, duckBlue.copy(alpha = 0.7f))))
            )

            // 2. LE CONTENU PRINCIPAL
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Espace pour descendre le contenu
                item { Spacer(Modifier.height(160.dp)) }

                item {
                    // Box pour superposer l'avatar sur la Card
                    Box(contentAlignment = Alignment.TopCenter) {

                        // --- LA CARTE BLANCHE ---
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp)
                                .padding(top = 70.dp) // Décalage pour l'avatar
                                .shadow(15.dp, RoundedCornerShape(32.dp)),
                            shape = RoundedCornerShape(32.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 85.dp, bottom = 30.dp), // Padding important pour voir le texte
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                // BLOC IDENTITÉ
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = userProfile?.displayName ?: "Loudimanche",
                                        fontSize = 30.sp,
                                        fontWeight = FontWeight.Black,
                                        color = darkBlue,
                                        letterSpacing = (-0.5).sp
                                    )
                                    Text(
                                        text = userProfile?.email ?: "",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color.Gray
                                    )
                                }

                                Spacer(Modifier.height(30.dp))

                                // STATS
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    PremiumStatCard(
                                        modifier = Modifier.weight(1f),
                                        value = "${watchedFilms.size}",
                                        label = "VUS",
                                        color = duckBlue,
                                        onClick = { onNavigateToMyFilms("watched") }
                                    )
                                    PremiumStatCard(
                                        modifier = Modifier.weight(1f),
                                        value = "${ownedFilms.size}",
                                        label = "DVD",
                                        color = Color(0xFFE91E63),
                                        onClick = { onNavigateToMyFilms("owned") }
                                    )
                                }

                                Spacer(Modifier.height(24.dp))

                                // MODE SOMBRE
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 24.dp)
                                        .background(Color(0xFFF1F5F9), RoundedCornerShape(16.dp))
                                        .padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.Brightness4, null, tint = darkBlue)
                                    Spacer(Modifier.width(12.dp))
                                    Text("Mode Sombre", fontWeight = FontWeight.Bold, color = darkBlue)
                                    Spacer(Modifier.weight(1f))
                                    Switch(
                                        checked = isDarkMode,
                                        onCheckedChange = { profileViewModel.toggleDarkMode() },
                                        colors = SwitchDefaults.colors(checkedTrackColor = duckBlue)
                                    )
                                }
                            }
                        }

                        // --- L'AVATAR (Placé en dernier dans la Box = AU DESSUS) ---
                        Surface(
                            modifier = Modifier
                                .size(140.dp)
                                .shadow(16.dp, CircleShape),
                            shape = CircleShape,
                            color = Color.White,
                            border = BorderStroke(6.dp, Color.White)
                        ) {
                            Box(modifier = Modifier.background(duckBlue), contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.Default.Person,
                                    contentDescription = null,
                                    modifier = Modifier.size(85.dp),
                                    tint = Color.White
                                )
                            }
                        }
                    }
                }

                // BOUTON DÉCONNEXION
                item {
                    Spacer(Modifier.height(30.dp))
                    OutlinedButton(
                        onClick = { profileViewModel.logout { onLogout() } },
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 40.dp).height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(2.dp, deleteRed),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = deleteRed)
                    ) {
                        Icon(Icons.Default.PowerSettingsNew, null)
                        Spacer(Modifier.width(10.dp))
                        Text("DÉCONNEXION", fontWeight = FontWeight.Black)
                    }
                    Spacer(Modifier.height(50.dp))
                }
            }

            // 3. BOUTON RETOUR (Toujours au premier plan)
            IconButton(
                onClick = onNavigateToCatalog,
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(start = 16.dp, top = 8.dp)
                    .background(Color.White.copy(alpha = 0.2f), CircleShape)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Retour", tint = Color.White)
            }
        }
    }
}

@Composable
fun PremiumStatCard(
    modifier: Modifier,
    value: String,
    label: String,
    color: Color,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .height(110.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        color = color.copy(alpha = 0.08f),
        border = BorderStroke(1.dp, color.copy(alpha = 0.2f))
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(value, fontSize = 28.sp, fontWeight = FontWeight.Black, color = color)
            Text(label, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = DisneyLightGray)
            Spacer(Modifier.height(4.dp))
            Icon(
                Icons.Default.KeyboardArrowRight,
                contentDescription = null,
                tint = color.copy(alpha = 0.4f),
                modifier = Modifier.size(16.dp)
            )
        }
    }
}