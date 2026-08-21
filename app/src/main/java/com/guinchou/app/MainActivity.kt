package com.guinchou.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.guinchou.app.ui.theme.GuinchouTheme

/**
 * Activity principal do Guinchou.
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(
            savedInstanceState
        )

        /*
         * Habilita Edge-to-Edge.
         *
         * O conteúdo pode utilizar toda
         * a tela do aparelho.
         *
         * Cada tela controla os espaços
         * seguros através do WindowInsets.
         */
        enableEdgeToEdge(

            /*
             * Ícones claros na barra superior,
             * porque o Guinchou possui fundo escuro.
             */
            statusBarStyle =
                SystemBarStyle.dark(
                    android.graphics.Color.TRANSPARENT
                ),

            /*
             * Ícones claros na barra inferior.
             */
            navigationBarStyle =
                SystemBarStyle.dark(
                    android.graphics.Color.TRANSPARENT
                )
        )

        /*
         * Inicia Jetpack Compose.
         */
        setContent {

            GuinchouTheme {

                GuinchouApp()
            }
        }
    }
}