package com.guinchou.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

// Define todas as cores utilizadas pelo Material Design
// dentro do Guinchou.
private val GuinchouDarkColorScheme = darkColorScheme(

    // Cor dos principais botões.
    primary = GuinchouGreen,

    // Cor que aparece sobre elementos verdes.
    onPrimary = GuinchouBackground,

    // Cor secundária.
    secondary = GuinchouGreenLight,

    // Fundo principal do aplicativo.
    background = GuinchouBackground,

    // Cor do texto sobre o fundo.
    onBackground = GuinchouWhite,

    // Cards, caixas e componentes.
    surface = GuinchouSurface,

    // Cor dos textos sobre cards.
    onSurface = GuinchouWhite,

    // Cor utilizada para erros.
    error = GuinchouError
)

/**
 * Tema principal de todo o aplicativo.
 *
 * Qualquer tela colocada dentro de GuinchouTheme
 * passa a utilizar automaticamente nossas cores,
 * tipografia e estilos Material.
 */
@Composable
fun GuinchouTheme(
    content: @Composable () -> Unit
) {

    MaterialTheme(

        // Passamos nosso conjunto de cores.
        colorScheme = GuinchouDarkColorScheme,

        // Mantemos a tipografia criada pelo Android Studio.
        typography = Typography,

        // Interface que será desenhada dentro do tema.
        content = content
    )
}