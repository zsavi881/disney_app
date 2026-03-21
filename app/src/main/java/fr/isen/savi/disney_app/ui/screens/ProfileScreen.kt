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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.isen.savi.disney_app.viewmodel.ProfileViewModel
import fr.isen.savi.disney_app.ui.theme.ThemeState

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

    val backgroundLight = Color(0xFFE3F2FD)
    val duckBlue = Color(0xFF0077B6)
    val darkBlue = Color(0xFF003049)
    val deleteRed = Color(0xFFD32F2F)

    if (userProfile == null) {
        Box(Modifier.fillMaxSize().background(backgroundLight), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = duckBlue)
        }
    } else {
        Box(modifier = Modifier.fillMaxSize().background(backgroundLight)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .background(brush = Brush.verticalGradient(listOf(duckBlue, duckBlue.copy(alpha = 0.6f))))
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item { Spacer(Modifier.height(140.dp)) }

                item {
                    Box(contentAlignment = Alignment.TopCenter) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp)
                                .padding(top = 70.dp)
                                .shadow(20.dp, RoundedCornerShape(32.dp)),
                            shape = RoundedCornerShape(32.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 80.dp, bottom = 30.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = userProfile?.displayName ?: "Utilisateur",
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Black,
                                    color = darkBlue
                                )
                                Text(
                                    text = userProfile?.email ?: "",
                                    fontSize = 14.sp,
                                    color = Color.Gray
                                )

                                Spacer(Modifier.height(30.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    PremiumStatCard(
                                        modifier = Modifier.weight(1f),
                                        value = "${watchedFilms.size}",
                                        label = "Films vus",
                                        color = duckBlue,
                                        onClick = { onNavigateToMyFilms("watched") }
                                    )
                                    PremiumStatCard(
                                        modifier = Modifier.weight(1f),
                                        value = "${ownedFilms.size}",
                                        label = "DVD / Blu-Ray",
                                        color = Color(0xFFE91E63),
                                        onClick = { onNavigateToMyFilms("owned") }
                                    )
                                }

                                Spacer(Modifier.height(24.dp))

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 24.dp)
                                        .background(backgroundLight.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
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

                        Surface(
                            modifier = Modifier.size(130.dp).shadow(12.dp, CircleShape),
                            shape = CircleShape,
                            color = Color.White,
                            border = BorderStroke(4.dp, Color.White)
                        ) {
                            Box(modifier = Modifier.background(duckBlue), contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Person, null, Modifier.size(80.dp), tint = Color.White)
                            }
                        }
                    }
                }

                item {
                    Spacer(Modifier.height(30.dp))
                    Button(
                        onClick = { profileViewModel.logout { onLogout() } },
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 40.dp).height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = deleteRed.copy(alpha = 0.1f)),
                        border = BorderStroke(1.dp, deleteRed.copy(alpha = 0.5f))
                    ) {
                        Icon(Icons.Default.PowerSettingsNew, null, tint = deleteRed)
                        Spacer(Modifier.width(10.dp))
                        Text("DÉCONNEXION", fontWeight = FontWeight.Black, color = deleteRed)
                    }
                    Spacer(Modifier.height(40.dp))
                }
            }

            IconButton(
                onClick = onNavigateToCatalog,
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(start = 16.dp, top = 8.dp)
                    .background(Color.White.copy(alpha = 0.3f), CircleShape)
            ) {
                Icon(Icons.Default.ArrowBack, null, tint = Color.White)
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
        modifier = modifier.height(100.dp).clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        color = color.copy(alpha = 0.1f),
        border = BorderStroke(1.dp, color.copy(alpha = 0.2f))
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(value, fontSize = 26.sp, fontWeight = FontWeight.Black, color = color)
            Text(label, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
            Icon(Icons.Default.KeyboardArrowRight, null, Modifier.size(16.dp), tint = color.copy(alpha = 0.4f))
        }
    }
}