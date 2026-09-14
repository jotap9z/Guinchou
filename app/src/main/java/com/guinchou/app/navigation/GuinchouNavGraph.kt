package com.guinchou.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.guinchou.app.model.TowRequestStatus
import com.guinchou.app.ui.screens.auth.CreateAccountScreen
import com.guinchou.app.ui.screens.auth.LoginScreen
import com.guinchou.app.ui.screens.home.HomeScreen
import com.guinchou.app.ui.screens.partner.PartnerTypeScreen
import com.guinchou.app.ui.screens.payment.PaymentScreen
import com.guinchou.app.ui.screens.request.CompletedScreen
import com.guinchou.app.ui.screens.request.DestinationScreen
import com.guinchou.app.ui.screens.request.EstimateScreen
import com.guinchou.app.ui.screens.request.PickupScreen
import com.guinchou.app.ui.screens.request.ProblemScreen
import com.guinchou.app.ui.screens.request.SearchingScreen
import com.guinchou.app.ui.screens.request.TrackingScreen
import com.guinchou.app.ui.screens.request.VehicleScreen
import com.guinchou.app.ui.screens.splash.SplashScreen
import com.guinchou.app.viewmodel.AuthViewModel
import com.guinchou.app.viewmodel.TowRequestViewModel

@Composable
fun GuinchouNavGraph(
    navController: NavHostController,
    towRequestViewModel: TowRequestViewModel,
    authViewModel: AuthViewModel
) {

    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH
    ) {

        /*
         * =========================================
         * SPLASH
         * =========================================
         */

        composable(
            route = Routes.SPLASH
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
            route = Routes.LOGIN
        ) {

            val authUiState by
            authViewModel
                .uiState
                .collectAsStateWithLifecycle()

            /*
             * Caso o Supabase confirme
             * a autenticação, abre a Home.
             */
            LaunchedEffect(
                authUiState.isAuthenticated
            ) {

                if (
                    authUiState.isAuthenticated
                ) {

                    openHome(
                        navController
                    )
                }
            }

            LoginScreen(

                isLoading =
                    authUiState.isLoading,

                externalErrorMessage =
                    authUiState.errorMessage,

                onLoginClick = {
                        identifier,
                        password ->

                    if (
                        identifier.contains("@")
                    ) {

                        authViewModel.signIn(
                            email =
                                identifier.trim(),
                            password =
                                password
                        )
                    }
                },

                onGoogleClick = {

                    /*
                     * Google Auth será
                     * implementado posteriormente.
                     */
                },

                onCreateAccountClick = {

                    /*
                     * Abre a nova tela
                     * de cadastro.
                     */
                    navController.navigate(
                        Routes.CREATE_ACCOUNT
                    )
                },

                onForgotPasswordClick = {

                    /*
                     * Recuperação de senha será
                     * implementada posteriormente.
                     */
                },

                onPartnerClick = {

                    navController.navigate(
                        Routes.PARTNER
                    )
                }
            )
        }


        /*
         * =========================================
         * CRIAR CONTA
         * =========================================
         */

        composable(
            route = Routes.CREATE_ACCOUNT
        ) {

            CreateAccountScreen(

                onCreateAccountClick = { _, _, _, _, _ ->

                    /*
                     * Neste momento estamos
                     * testando apenas a interface
                     * e a navegação.
                     *
                     * No próximo passo estes dados
                     * serão enviados ao Supabase.
                     */
                },

                onLoginClick = {

                    /*
                     * Volta para a tela anterior,
                     * que neste fluxo é o Login.
                     */
                    navController.popBackStack()
                }
            )
        }


        /*
         * =========================================
         * PARCEIRO
         * =========================================
         */

        composable(
            route = Routes.PARTNER
        ) {

            PartnerTypeScreen(

                onIndependentDriverClick = {

                    /*
                     * Cadastro do motorista
                     * será criado posteriormente.
                     */
                },

                onCompanyClick = {

                    /*
                     * Cadastro empresarial
                     * será criado posteriormente.
                     */
                },

                onBackClick = {

                    navController.popBackStack()
                }
            )
        }


        /*
         * =========================================
         * HOME
         * =========================================
         */

        composable(
            route = Routes.HOME
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

                    /*
                     * Tela de notificações
                     * será adicionada posteriormente.
                     */
                },

                onProfileClick = {

                    /*
                     * Tela de perfil será
                     * adicionada posteriormente.
                     */
                }
            )
        }


        /*
         * =========================================
         * LOCAL DO VEÍCULO
         * =========================================
         */

        composable(
            route = Routes.PICKUP
        ) {

            PickupScreen(

                onContinueClick = {
                        address,
                        latitude,
                        longitude,
                        source ->

                    if (
                        source == "GPS" &&
                        latitude != null &&
                        longitude != null
                    ) {

                        towRequestViewModel
                            .updatePickupFromGps(
                                address = address,
                                latitude = latitude,
                                longitude = longitude
                            )

                    } else {

                        towRequestViewModel
                            .updatePickupAddress(
                                address
                            )
                    }

                    navController.navigate(
                        Routes.DESTINATION
                    )
                },

                onBackClick = {

                    navController.popBackStack()
                }
            )
        }


        /*
         * =========================================
         * DESTINO
         * =========================================
         */

        composable(
            route = Routes.DESTINATION
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

                    navController.popBackStack()
                }
            )
        }


        /*
         * =========================================
         * VEÍCULO
         * =========================================
         */

        composable(
            route = Routes.VEHICLE
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
                            type = vehicleType,
                            brand = brand,
                            model = model,
                            year = year,
                            plate = plate
                        )

                    navController.navigate(
                        Routes.PROBLEM
                    )
                },

                onBackClick = {

                    navController.popBackStack()
                }
            )
        }


        /*
         * =========================================
         * PROBLEMA
         * =========================================
         */

        composable(
            route = Routes.PROBLEM
        ) {

            ProblemScreen(

                initialProblemType =
                    towRequestViewModel.problemType,

                initialProblemDetail =
                    towRequestViewModel.problemDetail,

                initialDescription =
                    towRequestViewModel.problemDescription,

                initialPhotoOneUri =
                    towRequestViewModel.vehiclePhotoOneUri,

                initialPhotoTwoUri =
                    towRequestViewModel.vehiclePhotoTwoUri,

                onPhotoOneChanged = { uri ->

                    if (
                        uri != null
                    ) {

                        towRequestViewModel
                            .updateVehiclePhotoOne(
                                uri
                            )

                    } else {

                        towRequestViewModel
                            .removeVehiclePhotoOne()
                    }
                },

                onPhotoTwoChanged = { uri ->

                    if (
                        uri != null
                    ) {

                        towRequestViewModel
                            .updateVehiclePhotoTwo(
                                uri
                            )

                    } else {

                        towRequestViewModel
                            .removeVehiclePhotoTwo()
                    }
                },

                onContinueClick = {
                        problemType,
                        problemDetail,
                        description ->

                    towRequestViewModel
                        .updateProblem(
                            type = problemType,
                            detail = problemDetail,
                            description = description
                        )

                    if (
                        problemType == "ACCIDENT" &&
                        !towRequestViewModel
                            .hasRequiredAccidentPhotos()
                    ) {

                        return@ProblemScreen
                    }

                    /*
                     * Distância de teste.
                     *
                     * Posteriormente será calculada
                     * utilizando origem e destino reais.
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

                    navController.popBackStack()
                }
            )
        }


        /*
         * =========================================
         * ESTIMATIVA
         * =========================================
         */

        composable(
            route = Routes.ESTIMATE
        ) {

            EstimateScreen(

                pickupAddress =
                    towRequestViewModel.pickupAddress,

                destinationAddress =
                    towRequestViewModel.destinationAddress,

                vehicleType =
                    towRequestViewModel.vehicleType,

                vehicleBrand =
                    towRequestViewModel.vehicleBrand,

                vehicleModel =
                    towRequestViewModel.vehicleModel,

                problemDetail =
                    towRequestViewModel.problemDetail,

                distanceKm =
                    towRequestViewModel.distanceKm,

                servicePrice =
                    towRequestViewModel.servicePrice,

                onContinueClick = {

                    navController.navigate(
                        Routes.PAYMENT
                    )
                },

                onBackClick = {

                    navController.popBackStack()
                }
            )
        }


        /*
         * =========================================
         * PAGAMENTO
         * =========================================
         */

        composable(
            route = Routes.PAYMENT
        ) {

            PaymentScreen(

                servicePrice =
                    towRequestViewModel.servicePrice,

                onConfirmPaymentClick = { _ ->

                    towRequestViewModel
                        .startSearching()

                    navController.navigate(
                        Routes.SEARCHING
                    )
                },

                onBackClick = {

                    navController.popBackStack()
                }
            )
        }


        /*
         * =========================================
         * PROCURANDO GUINCHO
         * =========================================
         */

        composable(
            route = Routes.SEARCHING
        ) {

            SearchingScreen(

                pickupAddress =
                    towRequestViewModel.pickupAddress,

                destinationAddress =
                    towRequestViewModel.destinationAddress,

                vehicleBrand =
                    towRequestViewModel.vehicleBrand,

                vehicleModel =
                    towRequestViewModel.vehicleModel,

                problemDetail =
                    towRequestViewModel.problemDetail,

                servicePrice =
                    towRequestViewModel.servicePrice,

                onTowFound = {

                    /*
                     * Dados temporários enquanto
                     * ainda não temos o aplicativo
                     * do motorista conectado.
                     */
                    towRequestViewModel
                        .acceptTowRequest(
                            driverName =
                                "Carlos Henrique",
                            towTruckDescription =
                                "Mercedes-Benz Accelo Plataforma",
                            towTruckPlate =
                                "ABC1D23",
                            driverRating =
                                4.9,
                            arrivalMinutes =
                                12
                        )

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
                        .cancelRequest()

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
            route = Routes.TRACKING
        ) {

            TrackingScreen(

                pickupAddress =
                    towRequestViewModel.pickupAddress,

                destinationAddress =
                    towRequestViewModel.destinationAddress,

                driverName =
                    towRequestViewModel.acceptedDriverName,

                towTruckDescription =
                    towRequestViewModel
                        .acceptedTowTruckDescription,

                towTruckPlate =
                    towRequestViewModel
                        .acceptedTowTruckPlate,

                estimatedArrivalMinutes =
                    towRequestViewModel
                        .estimatedArrivalMinutes,

                driverRating =
                    towRequestViewModel
                        .acceptedDriverRating,

                requestStatus =
                    towRequestViewModel
                        .requestStatus,

                onCallClick = {

                    /*
                     * Ligação será implementada
                     * posteriormente.
                     */
                },

                onMessageClick = {

                    /*
                     * Chat interno será implementado
                     * posteriormente.
                     */
                },

                onAdvanceTestClick = {

                    /*
                     * Controle temporário para
                     * testar os estados da corrida
                     * sem um motorista real.
                     */
                    when (
                        towRequestViewModel
                            .requestStatus
                    ) {

                        TowRequestStatus
                            .DRIVER_ON_THE_WAY -> {

                            towRequestViewModel
                                .markDriverArrived()
                        }

                        TowRequestStatus
                            .ARRIVED -> {

                            towRequestViewModel
                                .markVehicleLoaded()
                        }

                        TowRequestStatus
                            .VEHICLE_LOADED -> {

                            towRequestViewModel
                                .startTransport()
                        }

                        TowRequestStatus
                            .IN_TRANSIT -> {

                            towRequestViewModel
                                .completeRequest()

                            navController.navigate(
                                Routes.COMPLETED
                            ) {

                                popUpTo(
                                    Routes.TRACKING
                                ) {
                                    inclusive = true
                                }
                            }
                        }

                        else -> {

                            /*
                             * Nenhuma ação
                             * para outros estados.
                             */
                        }
                    }
                },

                onCancelClick = {

                    towRequestViewModel
                        .cancelRequest()

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
         * SERVIÇO CONCLUÍDO
         * =========================================
         */

        composable(
            route = Routes.COMPLETED
        ) {

            CompletedScreen(

                driverName =
                    towRequestViewModel
                        .acceptedDriverName,

                pickupAddress =
                    towRequestViewModel
                        .pickupAddress,

                destinationAddress =
                    towRequestViewModel
                        .destinationAddress,

                servicePrice =
                    towRequestViewModel
                        .servicePrice,

                onFinishClick = { _ ->

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


/*
 * =============================================
 * NAVEGAÇÃO PARA HOME
 * =============================================
 *
 * Remove o Login da pilha.
 *
 * Dessa forma, depois que o usuário entra
 * corretamente, pressionar "voltar" no Android
 * não retorna para a tela de autenticação.
 */
private fun openHome(
    navController: NavHostController
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