package com.guinchou.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.compose.rememberNavController
import com.guinchou.app.navigation.GuinchouNavGraph
import com.guinchou.app.ui.theme.GuinchouTheme
import com.guinchou.app.viewmodel.AuthViewModel
import com.guinchou.app.viewmodel.TowRequestViewModel

class MainActivity : ComponentActivity() {

    private val towRequestViewModel:
            TowRequestViewModel by viewModels()

    override fun onCreate(
        savedInstanceState: Bundle?,
    ) {
        super.onCreate(savedInstanceState)

        setContent {
            GuinchouTheme {

                val navController =
                    rememberNavController()

                val authViewModel: AuthViewModel by viewModels()

                GuinchouNavGraph(
                    navController = navController,
                    towRequestViewModel = towRequestViewModel,
                    authViewModel = authViewModel
                )
            }
        }
    }
}