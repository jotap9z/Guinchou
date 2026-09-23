package com.guinchou.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
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
import com.guinchou.app.ui.screens.partner.IndependentDriverRegistrationScreen
import com.guinchou.app.ui.screens.payment.PaymentScreen
import com.guinchou.app.ui.screens.profile.ProfileScreen
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
import com.guinchou.app.viewmodel.CustomerHomeViewModel
import com.guinchou.app.viewmodel.TowRequestViewModel


@Composable
fun GuinchouNavGraph(
    navController: NavHostController,
    towRequestViewModel: TowRequestViewModel,
    authViewModel: AuthViewModel
) {
    val customerHomeViewModel: CustomerHomeViewModel = viewModel()

    /*
     * E-mail utilizado temporariamente
     * no fluxo de recuperação de senha.
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
                     * Nesta etapa estamos trabalhando
                     * apenas o Front.
                     *
                     * Após a validação visual do formulário,
                     * o usuário entra na Home.
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

                    recoveryEmail =
                        email.trim()

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
                     * Funcionalidade real será
                     * implementada com o backend.
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

                    navController.navigate(
                        Routes.DRIVER_REGISTER
                    )
                },

                onCompanyClick = {

                    /*
                     * Fluxo da empresa parceira
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
         * CADASTRO DO MOTORISTA INDEPENDENTE
         * =========================================
         */

        composable(
            route = Routes.DRIVER_REGISTER
        ) {

            IndependentDriverRegistrationScreen(

                onBackClick = {

                    navController.popBackStack()
                },

                onRegistrationFinished = {

                    /*
                     * Front-end:
                     * após concluir o cadastro, retorna
                     * para a tela de seleção de parceiro.
                     *
                     * Futuramente poderá navegar para
                     * uma área de acompanhamento.
                     */
                    navController.popBackStack(
                        route = Routes.PARTNER,
                        inclusive = false
                    )
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
            val homeState by customerHomeViewModel.uiState.collectAsStateWithLifecycle()
            LaunchedEffect(authViewModel.uiState.value.isAuthenticated) {
                if (authViewModel.uiState.value.isAuthenticated) {
                    customerHomeViewModel.load()
                } else {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.HOME) { inclusive = true }
                    }
                }
            }

            HomeScreen(
                homeState = homeState,

                onRequestTowClick = {
                    if (!homeState.canRequestTow) return@HomeScreen

                    towRequestViewModel
                        .clearRequest()

                    navController.navigate(
                        Routes.PICKUP
                    )
                },

                onNotificationClick = {

                    /*
                     * Tela de notificações
                     * será criada posteriormente.
                     */
                },

                onProfileClick = {

                    navController.navigate(
                        Routes.PROFILE
                    )
                }
            )
        }


        /*
         * =========================================
         * PERFIL DO CLIENTE
         * =========================================
         *
         * ESTA ERA A ROTA QUE ESTAVA FALTANDO.
         *
         * A Home tentava abrir Routes.PROFILE,
         * porém não havia nenhum composable
         * registrado para essa rota.
         */

        composable(
            route = Routes.PROFILE
        ) {
            val homeState by customerHomeViewModel.uiState.collectAsStateWithLifecycle()

            ProfileScreen(

                userName =
                    homeState.home?.name ?: "Cliente",

                userEmail =
                    homeState.home?.email ?: "",

                onBackClick = {

                    navController.popBackStack()
                },

                onLogoutClick = {

                    authViewModel.signOut {
                        customerHomeViewModel.clear()
                        navController.navigate(Routes.LOGIN) {
                            popUpTo(Routes.HOME) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                }
            )
        }


        /*
         * =========================================
         * LOCAL DO VEÍCULO / ORIGEM
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
         * PROBLEMA DO VEÍCULO
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


                    /*
                     * Acidente exige as duas fotos.
                     */
                    if (
                        problemType == "ACCIDENT" &&
                        !towRequestViewModel
                            .hasRequiredAccidentPhotos()
                    ) {

                        return@ProblemScreen
                    }


                    /*
                     * Distância temporária utilizada
                     * enquanto estamos fazendo
                     * somente o Front-end.
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
                     * Motorista fictício utilizado
                     * apenas para simulação do Front.
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

                        launchSingleTop = true
                    }
                }
            )
        }


        /*
         * =========================================
         * ACOMPANHAMENTO DO GUINCHO
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
                     * Chat será implementado
                     * posteriormente.
                     */
                },


                /*
                 * Botão temporário utilizado
                 * para testar as mudanças
                 * de status do atendimento.
                 */
                onAdvanceTestClick = {

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
                             * Não realiza nenhuma ação
                             * para os demais estados.
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

                        launchSingleTop = true
                    }
                }
            )
        }


        /*
         * =========================================
         * SERVIÇO CONCLUÍDO / AVALIAÇÃO
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

                        launchSingleTop = true
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

        launchSingleTop = true
    }
}
