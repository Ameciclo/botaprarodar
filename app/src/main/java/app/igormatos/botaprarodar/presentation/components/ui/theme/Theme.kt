package app.igormatos.botaprarodar.presentation.components.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import app.igormatos.botaprarodar.presentation.components.ui.theme.ColorPalet.*

private val DarkColorPalette = darkColors(
    primary = ColorPalet.Purple200,
    primaryVariant = ColorPalet.Purple700,
    secondary = ColorPalet.Teal200
)

private val LightColorPalette = lightColors(
    primary = ColorPalet.Purple500,
    primaryVariant = ColorPalet.Purple700,
    secondary = ColorPalet.Teal200

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun BotaprarodarTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}