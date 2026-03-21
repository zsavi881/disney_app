package fr.isen.savi.disney_app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.isen.savi.disney_app.R
import fr.isen.savi.disney_app.viewmodel.AuthViewModel

@Composable
fun RegisterScreen(
    authViewModel: AuthViewModel,
    onRegisterSuccess: () -> Unit,
    onBackToLogin: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val user by authViewModel.user.collectAsState()
    val error by authViewModel.error.collectAsState()

    val backgroundLight = Color(0xFFE3F2FD)
    val disneyDuckBlue = Color(0xFF0077B6)

    LaunchedEffect(user) {
        if (user != null) onRegisterSuccess()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundLight)
            .statusBarsPadding()
            .padding(horizontal = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackToLogin) {
                Icon(Icons.Default.ArrowBack, null, tint = disneyDuckBlue)
            }
        }

        Spacer(Modifier.height(10.dp))

        Surface(
            modifier = Modifier
                .size(200.dp)
                .shadow(
                    elevation = 25.dp,
                    shape = RoundedCornerShape(35.dp),
                    ambientColor = Color.Black.copy(alpha = 0.3f),
                    spotColor = disneyDuckBlue.copy(alpha = 0.4f)
                ),
            shape = RoundedCornerShape(35.dp),
            color = Color.White
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_disney_app),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(18.dp)
                    .clip(RoundedCornerShape(22.dp)),
                contentScale = ContentScale.Fit
            )
        }

        Spacer(Modifier.height(30.dp))

        Text(
            text = "INSCRIPTION",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = disneyDuckBlue,
            letterSpacing = 2.sp
        )

        Spacer(Modifier.height(25.dp))

        DisneyInput(value = name, onValueChange = { name = it }, label = "Nom d'utilisateur")
        Spacer(Modifier.height(12.dp))
        DisneyInput(value = email, onValueChange = { email = it }, label = "Email")
        Spacer(Modifier.height(12.dp))
        DisneyInput(value = password, onValueChange = { password = it }, label = "Mot de passe", isPassword = true)

        Spacer(Modifier.height(35.dp))

        Button(
            onClick = { authViewModel.register(name, email, password) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .shadow(12.dp, RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = disneyDuckBlue)
        ) {
            Text("CRÉER MON COMPTE", fontWeight = FontWeight.Black, fontSize = 14.sp)
        }

        error?.let {
            Text(
                text = it,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 15.dp),
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun DisneyInput(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isPassword: Boolean = false
) {
    val disneyDuckBlue = Color(0xFF0077B6)
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = disneyDuckBlue.copy(alpha = 0.6f)) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        visualTransformation = if (isPassword) PasswordVisualTransformation() else androidx.compose.ui.text.input.VisualTransformation.None,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedBorderColor = disneyDuckBlue,
            unfocusedBorderColor = Color.Transparent,
        ),
        singleLine = true
    )
}