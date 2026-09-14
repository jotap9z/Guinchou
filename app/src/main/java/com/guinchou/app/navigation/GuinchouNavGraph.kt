package com.guinchou.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.guinchou.app.model.TowRequestStatus
import com.guinchou.app.ui.screens.auth.CreateAccountScreen
import com.guinchou.app.ui.screens.auth.ForgotPasswordScreen
import com.guinchou.app.ui.screens.auth.LoginScreen
import com.guinchou.app.ui.screens.auth.RecoveryEmailSentScreen
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

    /*
     * Guarda temporariamente o e-mail informado
     * na recuperação de senha.
     *
     * Como ainda estamos trabalhando somente
     * com o Front-end, não precisamos salvar
     * isso no banco ou no Supabase.
     */
    var recoveryEmail by remember {
        mutableStateOf("")
    }

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

                    navController.navigate(
                        Routes.CREATE_ACCOUNT
                    )
                },

                onForgotPasswordClick = {

                    /*
                     * Agora abre a tela real
                     * de recuperação de senha.
                     */
                    navController.navigate(
                        Routes.FORGOT_PASSWORD
                    )
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

                onCreateAccountClick = {
                        _,
                        _,
                        _,
                        _,
                        _ ->

                    /*
                     * Por enquanto estamos trabalhando
                     * somente com o Front-end.
                     *
                     * Depois que todos os campos forem
                     * validados pela CreateAccountScreen,
                     * o usuário é enviado para a Home.
                     *
                     * Posteriormente esta navegação
                     * acontecerá apenas depois do
                     * cadastro real no Supabase.
                     */
                    navController.navigate(
                        Routes.HOME
                    ) {

                        popUpTo(
                            Routes.LOGIN
                        ) {
                            inclusive = true
                        }
                    }
                },

                onLoginClick = {

                    navController.popBackStack()
                }
            )
        }


        /*
         * =========================================
         * RECUPERAR SENHA
         * =========================================
         */

        composable(
            route = Routes.FORGOT_PASSWORD
        ) {

            ForgotPasswordScreen(

                onSendRecoveryClick = { email ->

                    /*
                     * Guarda o e-mail para podermos
                     * exibi-lo na próxima tela.
                     */
                    recoveryEmail =
                        email.trim()

                    /*
                     * Por enquanto não existe envio
                     * de e-mail real.
                     *
                     * Apenas simulamos o sucesso
                     * visual do processo.
                     */
                    navController.navigate(
                        Routes.RECOVERY_EMAIL_SENT
                    )
                },

                onBackToLoginClick = {

                    navController.popBackStack()
                }
            )
        }


        /*
         * =========================================
         * E-MAIL DE RECUPERAÇÃO ENVIADO
         * =========================================
         */

        composable(
            route = Routes.RECOVERY_EMAIL_SENT
        ) {

            RecoveryEmailSentScreen(

                email =
                    recoveryEmail,

                onBackToLoginClick = {

                    /*
                     * Retorna diretamente ao Login
                     * e remove as telas de recuperação
                     * da pilha de navegação.
                     */
                    navController.navigate(
                        Routes.LOGIN
                    ) {

                        popUpTo(
                            Routes.FORGOT_PASSWORD
                        ) {
                            inclusive = true
                        }

                        launchSingleTop = true
                    }
                },

                onResendClick = {

                    /*
                     * Somente visual nesta etapa.
                     *
                     * Quando implementarmos o backend,
                     * aqui faremos um novo pedido
                     * de recuperação ao Supabase.
                     */
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
                     * será desenvolvido posteriormente.
                     */
                },

                onCompanyClick = {

                    /*
                     * Cadastro da empresa
                     * será desenvolvido posteriormente.
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
                     * Tela de notificações será
                     * criada posteriormente.
                     */
                },

                onProfileClick = {

                    /*
                     * Tela de perfil será
                     * criada posteriormente.
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
                     * Distância temporária
                     * utilizada apenas no Front.
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
                     * Dados simulados enquanto ainda
                     * estamos desenvolvendo o Front.
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
                     * Implementado posteriormente.
                     */
                },

                onMessageClick = {

                    /*
                     * Chat será implementado
                     * posteriormente.
                     */
                },

                onAdvanceTestClick = {

                    /*
                     * Simulação dos estados do
                     * atendimento durante o Front.
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
                             * Nenhuma ação.
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
 * ABRIR HOME APÓS LOGIN
 * =============================================
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