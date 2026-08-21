package com.guinchou.app

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.guinchou.app.navigation.GuinchouNavGraph
import com.guinchou.app.viewmodel.TowRequestViewModel

/**
 * Componente raiz do aplicativo.
 */
@Composable
fun GuinchouApp() {

    /*
     * Controlador das rotas.
     */
    val navController =
        rememberNavController()

    /*
     * Cria uma única instância do ViewModel
     * para o fluxo da solicitação.
     */
    val towRequestViewModel:
            TowRequestViewModel =
        viewModel()

    /*
     * Passamos o ViewModel para
     * o grafo de navegação.
     */
    GuinchouNavGraph(
        navController =
            navController,
        towRequestViewModel =
            towRequestViewModel
    )
}