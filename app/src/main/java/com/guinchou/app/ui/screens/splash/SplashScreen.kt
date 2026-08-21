package com.guinchou.app.ui.screens.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.guinchou.app.ui.theme.GuinchouBackground
import com.guinchou.app.ui.theme.GuinchouGreen
import com.guinchou.app.ui.theme.GuinchouWhite
import kotlinx.coroutines.delay

/**
 * Tela inicial do aplicativo.
 *
 * Ela aparece por 1,8 segundo e depois chama
 * a função onFinished para seguir para o Login.
 */
@Composable
fun SplashScreen(
    onFinished: () -> Unit
) {

    // Executa uma vez quando a Splash entra na tela.
    LaunchedEffect(Unit) {

        // Aguarda 1,8 segundo.
        delay(1800)

        // Informa ao sistema de navegação
        // que a Splash terminou.
        onFinished()
    }

    // Organiza os elementos verticalmente.
    Column(

        modifier = Modifier

            // Ocupa toda a tela.
            .fillMaxSize()

            // Define o fundo escuro.
            .background(GuinchouBackground)

            // Adiciona espaçamento interno.
            .padding(32.dp),

        // Centraliza verticalmente.
        verticalArrangement = Arrangement.Center,

        // Centraliza horizontalmente.
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Logo temporário.
        Text(
            text = "G",
            color = GuinchouGreen,
            fontSize = 76.sp,
            fontWeight = FontWeight.Black
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        // Nome do aplicativo.
        Text(
            text = "GUINCHOU",
            color = GuinchouWhite,
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        // Slogan.
        Text(
            text = "Seu guincho,\nonde você estiver.",
            color = Color.LightGray,
            fontSize = 18.sp,
            textAlign = TextAlign.Center
        )

        Spacer(
            modifier = Modifier.height(48.dp)
        )

        // Indicador de carregamento.
        CircularProgressIndicator(
            color = GuinchouGreen
        )
    }
}