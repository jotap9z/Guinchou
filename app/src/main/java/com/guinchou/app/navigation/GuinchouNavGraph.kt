package com.guinchou.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.guinchou.app.ui.screens.auth.LoginScreen
import com.guinchou.app.ui.screens.home.HomeScreen
import com.guinchou.app.ui.screens.partner.PartnerTypeScreen
import com.guinchou.app.ui.screens.payment.PaymentScreen
import com.guinchou.app.ui.screens.request.DestinationScreen
import com.guinchou.app.ui.screens.request.EstimateScreen
import com.guinchou.app.ui.screens.request.PickupScreen
import com.guinchou.app.ui.screens.request.ProblemScreen
import com.guinchou.app.ui.screens.request.SearchingScreen
import com.guinchou.app.ui.screens.request.TrackingScreen
import com.guinchou.app.ui.screens.request.VehicleScreen
import com.guinchou.app.ui.screens.splash.SplashScreen
import com.guinchou.app.viewmodel.TowRequestViewModel

/**
 * Grafo principal de navegação do Guinchou.
 *
 * Contém atualmente:
 *
 * - fluxo do cliente;
 * - entrada do fluxo parceiro.
 */
@Composable
fun GuinchouNavGraph(

    navController:
    NavHostController,

    towRequestViewModel:
    TowRequestViewModel
) {

    NavHost(

        navController =
            navController,

        startDestination =
            Routes.SPLASH
    ) {


        /*
         * =========================================
         * SPLASH
         * =========================================
         */
        composable(
            route =
                Routes.SPLASH
        ) {

            SplashScreen(

                onFinished = {

                    navController.navigate(
                        Routes.LOGIN
                    ) {

                        popUpTo(
                            Routes.SPLASH
                        ) {

                            inclusive = true
                        }
                    }
                }
            )
        }


        /*
         * =========================================
         * LOGIN
         * =========================================
         */
        composable(
            route =
                Routes.LOGIN
        ) {

            LoginScreen(

                /*
                 * Login do cliente.
                 */
                onLoginClick = {
                        _, _ ->

                    openHome(
                        navController
                    )
                },


                /*
                 * Google.
                 *
                 * Ainda simulado.
                 */
                onGoogleClick = {

                    openHome(
                        navController
                    )
                },


                /*
                 * Cadastro de cliente
                 * será criado posteriormente.
                 */
                onCreateAccountClick = {

                },


                /*
                 * Recuperação de senha.
                 */
                onForgotPasswordClick = {

                },


                /*
                 * =====================================
                 * ENTRADA DO PARCEIRO
                 * =====================================
                 */
                onPartnerClick = {

                    navController.navigate(
                        Routes.PARTNER
                    )
                }
            )
        }


        /*
         * =========================================
         * ESCOLHA DO TIPO DE PARCEIRO
         * =========================================
         */
        composable(
            route =
                Routes.PARTNER
        ) {

            PartnerTypeScreen(

                /*
                 * Motorista independente.
                 */
                onIndependentDriverClick = {

                    /*
                     * Na próxima etapa:
                     *
                     * Routes.DRIVER_REGISTER
                     *
                     * Ainda não navegamos porque
                     * a tela será criada em seguida.
                     */
                },


                /*
                 * Empresa.
                 */
                onCompanyClick = {

                    /*
                     * Na próxima etapa empresarial:
                     *
                     * Routes.COMPANY_REGISTER
                     */
                },


                /*
                 * Volta para Login.
                 */
                onBackClick = {

                    navController
                        .popBackStack()
                }
            )
        }


        /*
         * =========================================
         * HOME DO CLIENTE
         * =========================================
         */
        composable(
            route =
                Routes.HOME
        ) {

            HomeScreen(

                onRequestTowClick = {

                    towRequestViewModel
                        .clearRequest()

                    navController.navigate(
                        Routes.PICKUP
                    )
                },

                onNotificationClick = {

                },

                onProfileClick = {

                }
            )
        }


        /*
         * =========================================
         * 1 DE 5 - PARTIDA
         * =========================================
         */
        composable(
            route =
                Routes.PICKUP
        ) {

            PickupScreen(

                onContinueClick = {
                        pickupAddress ->

                    towRequestViewModel
                        .updatePickupAddress(
                            pickupAddress
                        )

                    navController.navigate(
                        Routes.DESTINATION
                    )
                },

                onBackClick = {

                    navController
                        .popBackStack()
                }
            )
        }


        /*
         * =========================================
         * 2 DE 5 - DESTINO
         * =========================================
         */
        composable(
            route =
                Routes.DESTINATION
        ) {

            DestinationScreen(

                onContinueClick = {
                        destinationAddress ->

                    towRequestViewModel
                        .updateDestinationAddress(
                            destinationAddress
                        )

                    navController.navigate(
                        Routes.VEHICLE
                    )
                },

                onBackClick = {

                    navController
                        .popBackStack()
                }
            )
        }


        /*
         * =========================================
         * 3 DE 5 - VEÍCULO
         * =========================================
         */
        composable(
            route =
                Routes.VEHICLE
        ) {

            VehicleScreen(

                onContinueClick = {
                        vehicleType,
                        brand,
                        model,
                        year,
                        plate ->

                    towRequestViewModel
                        .updateVehicle(

                            type =
                                vehicleType,

                            brand =
                                brand,

                            model =
                                model,

                            year =
                                year,

                            plate =
                                plate
                        )

                    navController.navigate(
                        Routes.PROBLEM
                    )
                },

                onBackClick = {

                    navController
                        .popBackStack()
                }
            )
        }


        /*
         * =========================================
         * 4 DE 5 - PROBLEMA
         * =========================================
         */
        composable(
            route =
                Routes.PROBLEM
        ) {

            ProblemScreen(

                initialProblemType =
                    towRequestViewModel
                        .problemType,

                initialProblemDetail =
                    towRequestViewModel
                        .problemDetail,

                initialDescription =
                    towRequestViewModel
                        .problemDescription,

                onContinueClick = {
                        problemType,
                        problemDetail,
                        description ->

                    towRequestViewModel
                        .updateProblem(

                            type =
                                problemType,

                            detail =
                                problemDetail,

                            description =
                                description
                        )

                    /*
                     * Distância temporária.
                     */
                    towRequestViewModel
                        .calculateEstimate(
                            18.0
                        )

                    navController.navigate(
                        Routes.ESTIMATE
                    )
                },

                onBackClick = {

                    navController
                        .popBackStack()
                }
            )
        }


        /*
         * =========================================
         * 5 DE 5 - ESTIMATIVA
         * =========================================
         */
        composable(
            route =
                Routes.ESTIMATE
        ) {

            EstimateScreen(

                pickupAddress =
                    towRequestViewModel
                        .pickupAddress,

                destinationAddress =
                    towRequestViewModel
                        .destinationAddress,

                vehicleType =
                    towRequestViewModel
                        .vehicleType,

                vehicleBrand =
                    towRequestViewModel
                        .vehicleBrand,

                vehicleModel =
                    towRequestViewModel
                        .vehicleModel,

                problemDetail =
                    towRequestViewModel
                        .problemDetail,

                distanceKm =
                    towRequestViewModel
                        .distanceKm,

                servicePrice =
                    towRequestViewModel
                        .servicePrice,

                onContinueClick = {

                    navController.navigate(
                        Routes.PAYMENT
                    )
                },

                onBackClick = {

                    navController
                        .popBackStack()
                }
            )
        }


        /*
         * =========================================
         * PAGAMENTO
         * =========================================
         */
        composable(
            route =
                Routes.PAYMENT
        ) {

            PaymentScreen(

                servicePrice =
                    towRequestViewModel
                        .servicePrice,

                onConfirmPaymentClick = {
                        _ ->

                    navController.navigate(
                        Routes.SEARCHING
                    )
                },

                onBackClick = {

                    navController
                        .popBackStack()
                }
            )
        }


        /*
         * =========================================
         * PROCURANDO GUINCHO
         * =========================================
         */
        composable(
            route =
                Routes.SEARCHING
        ) {

            SearchingScreen(

                pickupAddress =
                    towRequestViewModel
                        .pickupAddress,

                destinationAddress =
                    towRequestViewModel
                        .destinationAddress,

                vehicleBrand =
                    towRequestViewModel
                        .vehicleBrand,

                vehicleModel =
                    towRequestViewModel
                        .vehicleModel,

                problemDetail =
                    towRequestViewModel
                        .problemDetail,

                servicePrice =
                    towRequestViewModel
                        .servicePrice,

                onTowFound = {

                    navController.navigate(
                        Routes.TRACKING
                    ) {

                        popUpTo(
                            Routes.SEARCHING
                        ) {

                            inclusive = true
                        }
                    }
                },

                onCancelClick = {

                    towRequestViewModel
                        .clearRequest()

                    navController.navigate(
                        Routes.HOME
                    ) {

                        popUpTo(
                            Routes.HOME
                        ) {

                            inclusive = false
                        }
                    }
                }
            )
        }


        /*
         * =========================================
         * ACOMPANHAMENTO
         * =========================================
         */
        composable(
            route =
                Routes.TRACKING
        ) {

            TrackingScreen(

                pickupAddress =
                    towRequestViewModel
                        .pickupAddress,

                /*
                 * Temporários até existir
                 * o backend do parceiro.
                 */
                driverName =
                    "Carlos Henrique",

                towTruckDescription =
                    "Mercedes-Benz Accelo Plataforma",

                towTruckPlate =
                    "ABC1D23",

                estimatedArrivalMinutes =
                    12,

                onCallClick = {

                },

                onMessageClick = {

                },

                onCancelClick = {

                    towRequestViewModel
                        .clearRequest()

                    navController.navigate(
                        Routes.HOME
                    ) {

                        popUpTo(
                            Routes.HOME
                        ) {

                            inclusive = false
                        }
                    }
                }
            )
        }
    }
}


/**
 * Abre a Home do cliente
 * após autenticação.
 */
private fun openHome(
    navController:
    NavHostController
) {

    navController.navigate(
        Routes.HOME
    ) {

        popUpTo(
            Routes.LOGIN
        ) {

            inclusive = true
        }
    }
}