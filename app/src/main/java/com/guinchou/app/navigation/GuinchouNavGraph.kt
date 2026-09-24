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
import com.guinchou.app.ui.screens.partner.IndependentDriverRegistrationScreen
import com.guinchou.app.ui.screens.partner.PartnerCallsScreen
import com.guinchou.app.ui.screens.partner.PartnerEarningsScreen
import com.guinchou.app.ui.screens.partner.PartnerHomeScreen
import com.guinchou.app.ui.screens.partner.PartnerNotificationsScreen
import com.guinchou.app.ui.screens.partner.PartnerProfileScreen
import com.guinchou.app.ui.screens.partner.PartnerTypeScreen
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

private const val PARTNER_CALLS_ROUTE = "partner_calls"
private const val PARTNER_EARNINGS_ROUTE = "partner_earnings"
private const val PARTNER_EARNINGS_HISTORY_ROUTE = "partner_earnings_history"
private const val PARTNER_NOTIFICATIONS_ROUTE = "partner_notifications"
private const val PARTNER_PROFILE_ROUTE = "partner_profile"

@Composable
fun GuinchouNavGraph(
    navController: NavHostController,
    towRequestViewModel: TowRequestViewModel,
    authViewModel: AuthViewModel
) {
    val customerHomeViewModel: CustomerHomeViewModel = viewModel()
    var recoveryEmail by remember { mutableStateOf("") }

    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH
    ) {

        composable(route = Routes.SPLASH) {
            SplashScreen(
                onFinished = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.SPLASH) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(route = Routes.LOGIN) {
            val authUiState by authViewModel.uiState.collectAsStateWithLifecycle()

            LaunchedEffect(authUiState.isAuthenticated) {
                if (authUiState.isAuthenticated) {
                    openHome(navController)
                }
            }

            LoginScreen(
                isLoading = authUiState.isLoading,
                externalErrorMessage = authUiState.errorMessage,
                onLoginClick = { identifier, password ->
                    if (identifier.contains("@")) {
                        authViewModel.signIn(
                            email = identifier.trim(),
                            password = password
                        )
                    }
                },
                onGoogleClick = {},
                onCreateAccountClick = {
                    navController.navigate(Routes.CREATE_ACCOUNT)
                },
                onForgotPasswordClick = {
                    navController.navigate(Routes.FORGOT_PASSWORD)
                },
                onPartnerClick = {
                    navController.navigate(Routes.PARTNER)
                }
            )
        }

        composable(route = Routes.CREATE_ACCOUNT) {
            CreateAccountScreen(
                onCreateAccountClick = { _, _, _, _, _ ->
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) {
                            inclusive = true
                        }
                    }
                },
                onLoginClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(route = Routes.FORGOT_PASSWORD) {
            ForgotPasswordScreen(
                onSendRecoveryClick = { email ->
                    recoveryEmail = email.trim()
                    navController.navigate(Routes.RECOVERY_EMAIL_SENT)
                },
                onBackToLoginClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(route = Routes.RECOVERY_EMAIL_SENT) {
            RecoveryEmailSentScreen(
                email = recoveryEmail,
                onBackToLoginClick = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.FORGOT_PASSWORD) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                onResendClick = {}
            )
        }

        /*
         * =========================================================
         * ÁREA DO PARCEIRO
         * =========================================================
         */

        composable(route = Routes.PARTNER) {
            PartnerTypeScreen(
                onIndependentDriverClick = {
                    navController.navigate(Routes.DRIVER_REGISTER)
                },
                onCompanyClick = {},
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(route = Routes.DRIVER_REGISTER) {
            IndependentDriverRegistrationScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onRegistrationFinished = {
                    navController.navigate(Routes.PARTNER_HOME) {
                        popUpTo(Routes.PARTNER) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }

        /*
         * HOME DO GUINCHEIRO
         */

        composable(route = Routes.PARTNER_HOME) {
            PartnerHomeScreen(
                driverName = "João",
                todayTrips = 3,
                todayEarnings = 382.50,
                driverRating = 4.9,
                documentWarningCount = 1,

                onNotificationsClick = {
                    navController.navigate(
                        PARTNER_NOTIFICATIONS_ROUTE
                    ) {
                        launchSingleTop = true
                    }
                },

                onHistoryClick = {
                    navController.navigate(
                        PARTNER_EARNINGS_HISTORY_ROUTE
                    ) {
                        launchSingleTop = true
                    }
                },

                onEarningsClick = {
                    navController.navigate(
                        PARTNER_EARNINGS_ROUTE
                    ) {
                        launchSingleTop = true
                    }
                },

                onDocumentsClick = {
                    // Tela de documentos será conectada depois.
                },

                onProfileClick = {
                    navController.navigate(
                        PARTNER_PROFILE_ROUTE
                    ) {
                        launchSingleTop = true
                    }
                },

                onTestRequestClick = {
                    navController.navigate(
                        PARTNER_CALLS_ROUTE
                    ) {
                        launchSingleTop = true
                    }
                }
            )
        }

        /*
         * CHAMADOS
         */

        composable(route = PARTNER_CALLS_ROUTE) {
            PartnerCallsScreen(
                onBackClick = {
                    navController.popBackStack()
                },

                onNotificationsClick = {
                    navController.navigate(
                        PARTNER_NOTIFICATIONS_ROUTE
                    ) {
                        launchSingleTop = true
                    }
                },

                onCallClick = {},

                onHomeClick = {
                    navController.navigate(
                        Routes.PARTNER_HOME
                    ) {
                        launchSingleTop = true
                    }
                },

                onEarningsClick = {
                    navController.navigate(
                        PARTNER_EARNINGS_ROUTE
                    ) {
                        launchSingleTop = true
                    }
                },

                onProfileClick = {
                    navController.navigate(
                        PARTNER_PROFILE_ROUTE
                    ) {
                        launchSingleTop = true
                    }
                }
            )
        }

        /*
         * GANHOS
         */

        composable(route = PARTNER_EARNINGS_ROUTE) {
            PartnerEarningsScreen(
                driverName = "João",
                openHistory = false,

                onBackClick = {
                    navController.popBackStack()
                },

                onNotificationsClick = {
                    navController.navigate(
                        PARTNER_NOTIFICATIONS_ROUTE
                    ) {
                        launchSingleTop = true
                    }
                },

                onHomeClick = {
                    navController.navigate(
                        Routes.PARTNER_HOME
                    ) {
                        launchSingleTop = true
                    }
                },

                onCallsClick = {
                    navController.navigate(
                        PARTNER_CALLS_ROUTE
                    ) {
                        launchSingleTop = true
                    }
                },

                onProfileClick = {
                    navController.navigate(
                        PARTNER_PROFILE_ROUTE
                    ) {
                        launchSingleTop = true
                    }
                }
            )
        }

        /*
         * HISTÓRICO DE GANHOS
         */

        composable(
            route = PARTNER_EARNINGS_HISTORY_ROUTE
        ) {
            PartnerEarningsScreen(
                driverName = "João",
                openHistory = true,

                onBackClick = {
                    navController.popBackStack()
                },

                onNotificationsClick = {
                    navController.navigate(
                        PARTNER_NOTIFICATIONS_ROUTE
                    ) {
                        launchSingleTop = true
                    }
                },

                onHomeClick = {
                    navController.navigate(
                        Routes.PARTNER_HOME
                    ) {
                        launchSingleTop = true
                    }
                },

                onCallsClick = {
                    navController.navigate(
                        PARTNER_CALLS_ROUTE
                    ) {
                        launchSingleTop = true
                    }
                },

                onProfileClick = {
                    navController.navigate(
                        PARTNER_PROFILE_ROUTE
                    ) {
                        launchSingleTop = true
                    }
                }
            )
        }

        /*
         * NOTIFICAÇÕES DO GUINCHEIRO
         */

        composable(
            route = PARTNER_NOTIFICATIONS_ROUTE
        ) {
            PartnerNotificationsScreen(
                onBackClick = {
                    navController.popBackStack()
                },

                onCallClick = {
                    navController.navigate(
                        PARTNER_CALLS_ROUTE
                    ) {
                        launchSingleTop = true
                    }
                },

                onEarningsClick = {
                    navController.navigate(
                        PARTNER_EARNINGS_ROUTE
                    ) {
                        launchSingleTop = true
                    }
                }
            )
        }

        /*
         * PERFIL DO GUINCHEIRO
         */

        composable(
            route = PARTNER_PROFILE_ROUTE
        ) {
            PartnerProfileScreen(
                driverName = "João",
                driverEmail = "joao@guinchou.com.br",
                driverPhone = "(61) 99999-9999",
                driverRating = 4.9,

                onNotificationsClick = {
                    navController.navigate(
                        PARTNER_NOTIFICATIONS_ROUTE
                    ) {
                        launchSingleTop = true
                    }
                },

                onPersonalDataClick = {
                    // Próxima etapa.
                },

                onProfessionalDataClick = {
                    // Próxima etapa.
                },

                onTowTruckClick = {
                    // Próxima etapa.
                },

                onDocumentsClick = {
                    // Próxima etapa.
                },

                onSettingsClick = {
                    // Próxima etapa.
                },

                onSupportClick = {
                    // Próxima etapa.
                },

                onLogoutClick = {
                    authViewModel.signOut {
                        customerHomeViewModel.clear()

                        navController.navigate(
                            Routes.LOGIN
                        ) {
                            popUpTo(0) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }
                },

                onHomeClick = {
                    navController.navigate(
                        Routes.PARTNER_HOME
                    ) {
                        launchSingleTop = true
                    }
                },

                onCallsClick = {
                    navController.navigate(
                        PARTNER_CALLS_ROUTE
                    ) {
                        launchSingleTop = true
                    }
                },

                onEarningsClick = {
                    navController.navigate(
                        PARTNER_EARNINGS_ROUTE
                    ) {
                        launchSingleTop = true
                    }
                }
            )
        }

        /*
         * =========================================================
         * ÁREA DO CLIENTE
         * =========================================================
         */

        composable(route = Routes.HOME) {
            val homeState by customerHomeViewModel.uiState.collectAsStateWithLifecycle()
            val authUiState by authViewModel.uiState.collectAsStateWithLifecycle()

            LaunchedEffect(
                authUiState.isAuthenticated
            ) {
                if (authUiState.isAuthenticated) {
                    customerHomeViewModel.load()
                } else {
                    navController.navigate(
                        Routes.LOGIN
                    ) {
                        popUpTo(Routes.HOME) {
                            inclusive = true
                        }
                    }
                }
            }

            HomeScreen(
                homeState = homeState,

                onRequestTowClick = {
                    if (!homeState.canRequestTow) {
                        return@HomeScreen
                    }

                    towRequestViewModel.clearRequest()
                    navController.navigate(
                        Routes.PICKUP
                    )
                },

                onNotificationClick = {},

                onProfileClick = {
                    navController.navigate(
                        Routes.PROFILE
                    )
                }
            )
        }

        composable(route = Routes.PROFILE) {
            val homeState by customerHomeViewModel.uiState.collectAsStateWithLifecycle()

            ProfileScreen(
                userName = homeState.home?.name
                    ?: "Cliente",

                userEmail = homeState.home?.email
                    ?: "",

                onBackClick = {
                    navController.popBackStack()
                },

                onLogoutClick = {
                    authViewModel.signOut {
                        customerHomeViewModel.clear()

                        navController.navigate(
                            Routes.LOGIN
                        ) {
                            popUpTo(Routes.HOME) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }
                }
            )
        }

        composable(route = Routes.PICKUP) {
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

        composable(route = Routes.DESTINATION) {
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

        composable(route = Routes.VEHICLE) {
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

        composable(route = Routes.PROBLEM) {
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
                    if (uri != null) {
                        towRequestViewModel
                            .updateVehiclePhotoOne(uri)
                    } else {
                        towRequestViewModel
                            .removeVehiclePhotoOne()
                    }
                },

                onPhotoTwoChanged = { uri ->
                    if (uri != null) {
                        towRequestViewModel
                            .updateVehiclePhotoTwo(uri)
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

                    towRequestViewModel
                        .calculateEstimate(18.0)

                    navController.navigate(
                        Routes.ESTIMATE
                    )
                },

                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(route = Routes.ESTIMATE) {
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

        composable(route = Routes.PAYMENT) {
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

        composable(route = Routes.SEARCHING) {
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
                    towRequestViewModel
                        .acceptTowRequest(
                            driverName = "Carlos Henrique",
                            towTruckDescription =
                                "Mercedes-Benz Accelo Plataforma",
                            towTruckPlate = "ABC1D23",
                            driverRating = 4.9,
                            arrivalMinutes = 12
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

        composable(route = Routes.TRACKING) {
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
                    towRequestViewModel.requestStatus,

                onCallClick = {},

                onMessageClick = {},

                onAdvanceTestClick = {
                    when (
                        towRequestViewModel.requestStatus
                    ) {

                        TowRequestStatus.DRIVER_ON_THE_WAY -> {
                            towRequestViewModel
                                .markDriverArrived()
                        }

                        TowRequestStatus.ARRIVED -> {
                            towRequestViewModel
                                .markVehicleLoaded()
                        }

                        TowRequestStatus.VEHICLE_LOADED -> {
                            towRequestViewModel
                                .startTransport()
                        }

                        TowRequestStatus.IN_TRANSIT -> {
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

                        else -> {}
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

        composable(route = Routes.COMPLETED) {
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

private fun openHome(
    navController: NavHostController
) {
    navController.navigate(
        Routes.HOME
    ) {
        popUpTo(Routes.LOGIN) {
            inclusive = true
        }

        launchSingleTop = true
    }
}