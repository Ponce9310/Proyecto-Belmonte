
package com.example.myapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val BelmonteLightColors = lightColorScheme(
    primary = AzulBelmonte,
    onPrimary = Blanco,

    secondary = Naranjo,
    onSecondary = TextoBelmonte,

    tertiary = AzulBelmonte,
    onTertiary = Blanco,

    background = FondoBelmonte,
    onBackground = TextoBelmonte,

    surface = Blanco,
    onSurface = TextoBelmonte,

    outline = GrisSuave
)

private val BelmonteDarkColors = darkColorScheme(
    primary = AzulBelmonte,
    onPrimary = Blanco,

    secondary = Naranjo,
    onSecondary = TextoBelmonte,

    tertiary = AzulBelmonte,
    onTertiary = Blanco,

    background = TextoBelmonte,
    onBackground = Blanco,

    surface = AzulBelmonte,
    onSurface = Blanco,

    outline = GrisSuave
)

@Composable
fun MyAppTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        BelmonteDarkColors
    } else {
        BelmonteLightColors
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
