package fr.isen.savi.disney_app.ui.theme


import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color
private val DarkColorScheme = darkColorScheme(
    primary = BlueSecondary,
    secondary = BlueAccent,

    background = DarkBackground,
    surface = SurfaceDark,

    onPrimary = TextOnDark,
    onSecondary = TextOnDark,
    onBackground = TextOnDark,
    onSurface = TextOnDark
)

private val LightColorScheme = lightColorScheme(
    primary = BluePrimary,
    secondary = BlueSecondary,
    background = SurfaceLight,
    surface = BlueLight,
    onPrimary = Color.White,
    onSecondary = TextOnLight,
    onBackground = TextOnLight,
    onSurface = TextOnLight
)

@Composable
fun DisneyAppTheme(
    darkTheme: Boolean,
    content: @Composable () -> Unit
) {

    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography
    ) {

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            content()
        }

    }
}