package com.guinchou.app.ui.screens.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.guinchou.app.ui.theme.GuinchouBackground
import com.guinchou.app.ui.theme.GuinchouGreen
import com.guinchou.app.ui.theme.GuinchouWhite
import kotlinx.coroutines.delay

/**
 * Splash inicial do Guinchou.
 *
 * Importante:
 * o atraso acontece dentro de LaunchedEffect,
 * sem bloquear a thread principal.
 */
@Composable
fun SplashScreen(
    onFinished: () -> Unit
) {

    LaunchedEffect(Unit) {

        /*
         * Pequeno tempo de apresentação.
         *
         * Não use Thread.sleep aqui.
         * delay() não bloqueia a interface.
         */
        delay(
            1500L
        )

        onFinished()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                GuinchouBackground
            )
            .statusBarsPadding()
            .navigationBarsPadding(),
        verticalArrangement =
            Arrangement.Center,
        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        Text(
            text = "GUINCHOU",
            color = GuinchouWhite,
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Seu guincho, quando precisar.",
            color = GuinchouGreen,
            fontSize = 14.sp
        )
    }
}