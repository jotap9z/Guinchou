package com.guinchou.app.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.guinchou.app.model.TowRequestStatus
import com.guinchou.app.ui.screens.auth.CreateAccountScreen
import com.guinchou.app.ui.screens.auth.ForgotPasswordScreen
import com.guinchou.app.ui.screens.auth.LoginScreen
import com.guinchou.app.ui.screens.auth.RecoveryEmailSentScreen
import com.guinchou.app.ui.screens.calls.CustomerCallsScreen
import com.guinchou.app.ui.screens.home.HomeScreen
import com.guinchou.app.ui.screens.notifications.CustomerNotificationsScreen
import com.guinchou.app.ui.screens.partner.IndependentDriverRegistrationScreen
import com.guinchou.app.ui.screens.partner.PartnerCallsScreen
import com.guinchou.app.ui.screens.partner.PartnerEarningsScreen
import com.guinchou.app.ui.screens.partner.PartnerHomeScreen
import com.guinchou.app.ui.screens.partner.PartnerNotificationsScreen
import com.guinchou.app.ui.screens.partner.PartnerProfileScreen
import com.guinchou.app.ui.screens.partner.PartnerTypeScreen
import com.guinchou.app.ui.screens.payment.PaymentScreen
import com.guinchou.app.ui.screens.payment.PaymentsScreen
import com.guinchou.app.ui.screens.profile.ProfileScreen
import com.guinchou.app.ui.screens.request.CompletedScreen
import com.guinchou.app.ui.screens.request.CustomerRequestConfirmationRoute
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

private const val CUSTOMER_PAYMENTS_ROUTE = "customer_payments"
private const val CUSTOMER_CALLS_ROUTE = "customer_calls"
private const val CUSTOMER_NOTIFICATIONS_ROUTE = "customer_notifications"
private const val CUSTOMER_VEHICLES_ROUTE = "customer_vehicles"
private const val CUSTOMER_HISTORY_ROUTE = "customer_history"

private const val REQUEST_CONFIRM_ROUTE = "request_confirm"

@Composable
fun GuinchouNavGraph(
    navController: NavHostController,
    towRequestViewModel: TowRequestViewModel,
    authViewModel: AuthViewModel,
) {
    val customerHomeViewModel: CustomerHomeViewModel = viewModel()

    var recoveryEmail by remember {
        mutableStateOf("")
    }

    val authState by authViewModel.uiState.collectAsStateWithLifecycle()
    val entry by navController.currentBackStackEntryAsState()
    val currentRoute = entry?.destination?.route

    val partnerRoutes = setOf(
        Routes.PARTNER_HOME,
        PARTNER_CALLS_ROUTE,
        PARTNER_EARNINGS_ROUTE,
        PARTNER_EARNINGS_HISTORY_ROUTE,
        PARTNER_NOTIFICATIONS_ROUTE,
        PARTNER_PROFILE_ROUTE
    )

    val publicRoutes = setOf(
        Routes.SPLASH,
        Routes.LOGIN,
        Routes.CREATE_ACCOUNT,
        Routes.FORGOT_PASSWORD,
        Routes.RECOVERY_EMAIL_SENT,
        Routes.PARTNER,
        Routes.DRIVER_REGISTER
    )

    val requiredRole = when {
        currentRoute == null || currentRoute in publicRoutes -> null
        currentRoute in partnerRoutes -> "PARTNER_DRIVER"
        else -> "CUSTOMER"
    }

    val accessGranted = requiredRole == null ||
            (
                    authState.isAuthenticated &&
                            authState.profile?.role == requiredRole
                    )

    LaunchedEffect(
        currentRoute,
        authState.isLoading,
        authState.isAuthenticated,
        authState.profile?.role
    ) {
        if (
            requiredRole != null &&
            !authState.isLoading &&
            !accessGranted
        ) {
            customerHomeViewModel.clear()
            towRequestViewModel.clearRequest()

            openHome(
                navController = navController,
                role = authState.profile?.role
            )
        }
    }

    val navigate: (String) -> Unit = { route ->
        navController.navigate(route) {
            launchSingleTop = true
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
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
                val authUiState by authViewModel.uiState
                    .collectAsStateWithLifecycle()

                LaunchedEffect(
                    authUiState.isAuthenticated,
                    authUiState.profile?.role
                ) {
                    if (authUiState.isAuthenticated) {
                        openHome(
                            navController,
                            authUiState.profile?.role
                        )
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

                        navController.navigate(
                            Routes.RECOVERY_EMAIL_SENT
                        )
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
                        navController.navigate(Routes.LOGIN) {
                            popUpTo(Routes.PARTNER) {
                                inclusive = true
                            }

                            launchSingleTop = true
                        }
                    }
                )
            }

            composable(route = Routes.PARTNER_HOME) {
                PartnerHomeScreen(
                    driverName = authState.profile?.fullName
                        ?: "Guincheiro",

                    todayTrips = 3,
                    todayEarnings = 382.50,
                    driverRating = 4.9,
                    documentWarningCount = 1,

                    onNotificationsClick = {
                        navigate(PARTNER_NOTIFICATIONS_ROUTE)
                    },

                    onHistoryClick = {
                        navigate(PARTNER_EARNINGS_HISTORY_ROUTE)
                    },

                    onEarningsClick = {
                        navigate(PARTNER_EARNINGS_ROUTE)
                    },

                    onDocumentsClick = {},

                    onProfileClick = {
                        navigate(PARTNER_PROFILE_ROUTE)
                    },

                    onTestRequestClick = {
                        navigate(PARTNER_CALLS_ROUTE)
                    }
                )
            }

            composable(route = PARTNER_CALLS_ROUTE) {
                PartnerCallsScreen(
                    onBackClick = {
                        navController.popBackStack()
                    },

                    onNotificationsClick = {
                        navigate(PARTNER_NOTIFICATIONS_ROUTE)
                    },

                    onCallClick = {},

                    onHomeClick = {
                        navigate(Routes.PARTNER_HOME)
                    },

                    onEarningsClick = {
                        navigate(PARTNER_EARNINGS_ROUTE)
                    },

                    onProfileClick = {
                        navigate(PARTNER_PROFILE_ROUTE)
                    }
                )
            }

            composable(route = PARTNER_EARNINGS_ROUTE) {
                PartnerEarningsScreen(
                    driverName = authState.profile?.fullName
                        ?: "Guincheiro",

                    openHistory = false,

                    onBackClick = {
                        navController.popBackStack()
                    },

                    onNotificationsClick = {
                        navigate(PARTNER_NOTIFICATIONS_ROUTE)
                    },

                    onHomeClick = {
                        navigate(Routes.PARTNER_HOME)
                    },

                    onCallsClick = {
                        navigate(PARTNER_CALLS_ROUTE)
                    },

                    onProfileClick = {
                        navigate(PARTNER_PROFILE_ROUTE)
                    }
                )
            }

            composable(route = PARTNER_EARNINGS_HISTORY_ROUTE) {
                PartnerEarningsScreen(
                    driverName = authState.profile?.fullName
                        ?: "Guincheiro",

                    openHistory = true,

                    onBackClick = {
                        navController.popBackStack()
                    },

                    onNotificationsClick = {
                        navigate(PARTNER_NOTIFICATIONS_ROUTE)
                    },

                    onHomeClick = {
                        navigate(Routes.PARTNER_HOME)
                    },

                    onCallsClick = {
                        navigate(PARTNER_CALLS_ROUTE)
                    },

                    onProfileClick = {
                        navigate(PARTNER_PROFILE_ROUTE)
                    }
                )
            }

            composable(route = PARTNER_NOTIFICATIONS_ROUTE) {
                PartnerNotificationsScreen(
                    onBackClick = {
                        navController.popBackStack()
                    },

                    onCallClick = {
                        navigate(PARTNER_CALLS_ROUTE)
                    },

                    onEarningsClick = {
                        navigate(PARTNER_EARNINGS_ROUTE)
                    }
                )
            }

            composable(route = PARTNER_PROFILE_ROUTE) {
                PartnerProfileScreen(
                    driverName = authState.profile?.fullName
                        ?: "Guincheiro",

                    driverEmail = authState.email,
                    driverPhone = authState.profile?.phone.orEmpty(),
                    driverRating = 4.9,

                    onNotificationsClick = {
                        navigate(PARTNER_NOTIFICATIONS_ROUTE)
                    },

                    onPersonalDataClick = {},
                    onProfessionalDataClick = {},
                    onTowTruckClick = {},
                    onDocumentsClick = {},
                    onSettingsClick = {},
                    onSupportClick = {},

                    onLogoutClick = {
                        authViewModel.signOut {
                            customerHomeViewModel.clear()
                            towRequestViewModel.clearRequest()

                            navController.navigate(Routes.LOGIN) {
                                popUpTo(navController.graph.id) {
                                    inclusive = false
                                }

                                launchSingleTop = true
                            }
                        }
                    },

                    onHomeClick = {
                        navigate(Routes.PARTNER_HOME)
                    },

                    onCallsClick = {
                        navigate(PARTNER_CALLS_ROUTE)
                    },

                    onEarningsClick = {
                        navigate(PARTNER_EARNINGS_ROUTE)
                    }
                )
            }

            composable(route = Routes.HOME) {
                val homeState by customerHomeViewModel.uiState
                    .collectAsStateWithLifecycle()

                val authUiState by authViewModel.uiState
                    .collectAsStateWithLifecycle()

                LaunchedEffect(
                    authUiState.isAuthenticated,
                    authUiState.isLoading,
                    authUiState.profile?.role
                ) {
                    if (authUiState.isLoading) {
                        return@LaunchedEffect
                    }

                    if (
                        authUiState.isAuthenticated &&
                        authUiState.profile?.role == "CUSTOMER"
                    ) {
                        customerHomeViewModel.load()
                    }
                }

                HomeScreen(
                    homeState = homeState,

                    onRequestTowClick = {
                        if (!homeState.canRequestTow) {
                            return@HomeScreen
                        }

                        towRequestViewModel.clearRequest()
                        navController.navigate(Routes.PICKUP)
                    },

                    onNotificationClick = {
                        navigate(CUSTOMER_NOTIFICATIONS_ROUTE)
                    },

                    onCallsClick = {
                        navigate(CUSTOMER_CALLS_ROUTE)
                    },

                    onVehiclesClick = {
                        navigate(CUSTOMER_VEHICLES_ROUTE)
                    },

                    onHistoryClick = {
                        navigate(CUSTOMER_HISTORY_ROUTE)
                    },

                    onPaymentsClick = {
                        navigate(CUSTOMER_PAYMENTS_ROUTE)
                    },

                    onProfileClick = {
                        navController.navigate(Routes.PROFILE)
                    }
                )
            }

            composable(route = CUSTOMER_NOTIFICATIONS_ROUTE) {
                CustomerNotificationsScreen(
                    onBackClick = {
                        navController.popBackStack()
                    },

                    onCallClick = {
                        navigate(CUSTOMER_CALLS_ROUTE)
                    },

                    onPaymentClick = {
                        navigate(CUSTOMER_PAYMENTS_ROUTE)
                    }
                )
            }

            composable(route = CUSTOMER_CALLS_ROUTE) {
                CustomerCallsScreen(
                    onBackClick = {
                        navController.popBackStack()
                    },

                    onRequestTowClick = {
                        towRequestViewModel.clearRequest()
                        navController.navigate(Routes.PICKUP)
                    },

                    onTrackCallClick = {
                        navigate(Routes.TRACKING)
                    },

                    onHomeClick = {
                        navigate(Routes.HOME)
                    },

                    onPaymentsClick = {
                        navigate(CUSTOMER_PAYMENTS_ROUTE)
                    },

                    onProfileClick = {
                        navigate(Routes.PROFILE)
                    }
                )
            }

            composable(route = CUSTOMER_PAYMENTS_ROUTE) {
                PaymentsScreen(
                    onBackClick = {
                        navController.popBackStack()
                    },

                    onHomeClick = {
                        navigate(Routes.HOME)
                    },

                    onCallsClick = {
                        navigate(CUSTOMER_CALLS_ROUTE)
                    },

                    onProfileClick = {
                        navigate(Routes.PROFILE)
                    }
                )
            }

            composable(route = CUSTOMER_VEHICLES_ROUTE) {
                val homeState by customerHomeViewModel.uiState
                    .collectAsStateWithLifecycle()

                ProfileScreen(
                    userName = homeState.home?.name ?: "Cliente",
                    userEmail = homeState.home?.email.orEmpty(),
                    openVehicles = true,

                    onBackClick = {
                        navController.popBackStack()
                    },

                    onLogoutClick = {
                        authViewModel.signOut {
                            customerHomeViewModel.clear()
                            towRequestViewModel.clearRequest()

                            navController.navigate(Routes.LOGIN) {
                                popUpTo(navController.graph.id) {
                                    inclusive = false
                                }

                                launchSingleTop = true
                            }
                        }
                    }
                )
            }

            composable(route = CUSTOMER_HISTORY_ROUTE) {
                val homeState by customerHomeViewModel.uiState
                    .collectAsStateWithLifecycle()

                ProfileScreen(
                    userName = homeState.home?.name ?: "Cliente",
                    userEmail = homeState.home?.email.orEmpty(),
                    openHistory = true,

                    onBackClick = {
                        navController.popBackStack()
                    },

                    onLogoutClick = {
                        authViewModel.signOut {
                            customerHomeViewModel.clear()
                            towRequestViewModel.clearRequest()

                            navController.navigate(Routes.LOGIN) {
                                popUpTo(navController.graph.id) {
                                    inclusive = false
                                }

                                launchSingleTop = true
                            }
                        }
                    }
                )
            }

            composable(route = Routes.PROFILE) {
                val homeState by customerHomeViewModel.uiState
                    .collectAsStateWithLifecycle()

                ProfileScreen(
                    userName = homeState.home?.name ?: "Cliente",
                    userEmail = homeState.home?.email.orEmpty(),

                    onBackClick = {
                        navController.popBackStack()
                    },

                    onLogoutClick = {
                        authViewModel.signOut {
                            customerHomeViewModel.clear()
                            towRequestViewModel.clearRequest()

                            navController.navigate(Routes.LOGIN) {
                                popUpTo(navController.graph.id) {
                                    inclusive = false
                                }

                                launchSingleTop = true
                            }
                        }
                    }
                )
            }

            composable(route = Routes.PICKUP) {
                PickupScreen(
                    onContinueClick = { address, latitude, longitude, source ->
                        if (
                            source == "GPS" &&
                            latitude != null &&
                            longitude != null
                        ) {
                            towRequestViewModel.updatePickupFromGps(
                                address = address,
                                latitude = latitude,
                                longitude = longitude
                            )
                        } else {
                            towRequestViewModel.updatePickupAddress(address)
                        }

                        navController.navigate(Routes.DESTINATION)
                    },

                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }

            composable(route = Routes.DESTINATION) {
                DestinationScreen(
                    onContinueClick = { address, latitude, longitude ->
                        towRequestViewModel.updateDestinationLocation(
                            address = address,
                            latitude = latitude,
                            longitude = longitude
                        )

                        navController.navigate(Routes.VEHICLE)
                    },

                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }

            composable(route = Routes.VEHICLE) {
                VehicleScreen(
                    onContinueClick = { type, brand, model, year, plate ->
                        towRequestViewModel.updateVehicle(
                            type = type,
                            brand = brand,
                            model = model,
                            year = year,
                            plate = plate
                        )

                        navController.navigate(Routes.PROBLEM)
                    },

                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }

            composable(route = Routes.PROBLEM) {
                ProblemScreen(
                    initialProblemType = towRequestViewModel.problemType,
                    initialProblemDetail = towRequestViewModel.problemDetail,
                    initialDescription = towRequestViewModel.problemDescription,
                    initialPhotoOneUri = towRequestViewModel.vehiclePhotoOneUri,
                    initialPhotoTwoUri = towRequestViewModel.vehiclePhotoTwoUri,

                    onPhotoOneChanged = { uri ->
                        if (uri != null) {
                            towRequestViewModel.updateVehiclePhotoOne(uri)
                        } else {
                            towRequestViewModel.removeVehiclePhotoOne()
                        }
                    },

                    onPhotoTwoChanged = { uri ->
                        if (uri != null) {
                            towRequestViewModel.updateVehiclePhotoTwo(uri)
                        } else {
                            towRequestViewModel.removeVehiclePhotoTwo()
                        }
                    },

                    onContinueClick = { type, detail, description ->
                        towRequestViewModel.updateProblem(
                            type = type,
                            detail = detail,
                            description = description
                        )

                        if (
                            type == "ACCIDENT" &&
                            !towRequestViewModel.hasRequiredAccidentPhotos()
                        ) {
                            return@ProblemScreen
                        }

                        navigate(REQUEST_CONFIRM_ROUTE)
                    },

                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }

            composable(route = REQUEST_CONFIRM_ROUTE) {
                CustomerRequestConfirmationRoute(
                    towRequestViewModel = towRequestViewModel,

                    onRegistered = {
                        customerHomeViewModel.load()
                    },

                    onBackClick = {
                        navController.popBackStack()
                    },

                    onHomeClick = {
                        towRequestViewModel.clearRequest()

                        navController.navigate(Routes.HOME) {
                            popUpTo(Routes.HOME) {
                                inclusive = false
                            }

                            launchSingleTop = true
                        }
                    }
                )
            }

            composable(route = Routes.ESTIMATE) {
                EstimateScreen(
                    pickupAddress = towRequestViewModel.pickupAddress,
                    destinationAddress = towRequestViewModel.destinationAddress,
                    vehicleType = towRequestViewModel.vehicleType,
                    vehicleBrand = towRequestViewModel.vehicleBrand,
                    vehicleModel = towRequestViewModel.vehicleModel,
                    problemDetail = towRequestViewModel.problemDetail,
                    distanceKm = towRequestViewModel.distanceKm,
                    servicePrice = towRequestViewModel.servicePrice,

                    onContinueClick = {
                        navController.navigate(Routes.PAYMENT)
                    },

                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }

            composable(route = Routes.PAYMENT) {
                PaymentScreen(
                    servicePrice = towRequestViewModel.servicePrice,

                    onConfirmPaymentClick = { _ ->
                        towRequestViewModel.startSearching()
                        navController.navigate(Routes.SEARCHING)
                    },

                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }

            composable(route = Routes.SEARCHING) {
                SearchingScreen(
                    pickupAddress = towRequestViewModel.pickupAddress,
                    destinationAddress = towRequestViewModel.destinationAddress,
                    vehicleBrand = towRequestViewModel.vehicleBrand,
                    vehicleModel = towRequestViewModel.vehicleModel,
                    problemDetail = towRequestViewModel.problemDetail,
                    servicePrice = towRequestViewModel.servicePrice,

                    onTowFound = {
                        towRequestViewModel.acceptTowRequest(
                            driverName = "Carlos Henrique",
                            towTruckDescription =
                                "Mercedes-Benz Accelo Plataforma",
                            towTruckPlate = "ABC1D23",
                            driverRating = 4.9,
                            arrivalMinutes = 12
                        )

                        navController.navigate(Routes.TRACKING) {
                            popUpTo(Routes.SEARCHING) {
                                inclusive = true
                            }
                        }
                    },

                    onCancelClick = {
                        towRequestViewModel.cancelRequest()
                        towRequestViewModel.clearRequest()

                        navController.navigate(Routes.HOME) {
                            popUpTo(Routes.HOME) {
                                inclusive = false
                            }

                            launchSingleTop = true
                        }
                    }
                )
            }

            composable(route = Routes.TRACKING) {
                TrackingScreen(
                    pickupAddress = towRequestViewModel.pickupAddress,
                    destinationAddress = towRequestViewModel.destinationAddress,
                    driverName = towRequestViewModel.acceptedDriverName,
                    towTruckDescription =
                        towRequestViewModel.acceptedTowTruckDescription,
                    towTruckPlate = towRequestViewModel.acceptedTowTruckPlate,
                    estimatedArrivalMinutes =
                        towRequestViewModel.estimatedArrivalMinutes,
                    driverRating = towRequestViewModel.acceptedDriverRating,
                    requestStatus = towRequestViewModel.requestStatus,

                    onCallClick = {},
                    onMessageClick = {},

                    onAdvanceTestClick = {
                        when (towRequestViewModel.requestStatus) {
                            TowRequestStatus.DRIVER_ON_THE_WAY -> {
                                towRequestViewModel.markDriverArrived()
                            }

                            TowRequestStatus.ARRIVED -> {
                                towRequestViewModel.markVehicleLoaded()
                            }

                            TowRequestStatus.VEHICLE_LOADED -> {
                                towRequestViewModel.startTransport()
                            }

                            TowRequestStatus.IN_TRANSIT -> {
                                towRequestViewModel.completeRequest()

                                navController.navigate(Routes.COMPLETED) {
                                    popUpTo(Routes.TRACKING) {
                                        inclusive = true
                                    }
                                }
                            }

                            else -> Unit
                        }
                    },

                    onCancelClick = {
                        towRequestViewModel.cancelRequest()
                        towRequestViewModel.clearRequest()

                        navController.navigate(Routes.HOME) {
                            popUpTo(Routes.HOME) {
                                inclusive = false
                            }

                            launchSingleTop = true
                        }
                    }
                )
            }

            composable(route = Routes.COMPLETED) {
                CompletedScreen(
                    driverName = towRequestViewModel.acceptedDriverName,
                    pickupAddress = towRequestViewModel.pickupAddress,
                    destinationAddress = towRequestViewModel.destinationAddress,
                    servicePrice = towRequestViewModel.servicePrice,

                    onFinishClick = { _ ->
                        towRequestViewModel.clearRequest()

                        navController.navigate(Routes.HOME) {
                            popUpTo(Routes.HOME) {
                                inclusive = false
                            }

                            launchSingleTop = true
                        }
                    }
                )
            }
        }

        if (!accessGranted) {
            AccountAccessOverlay(
                loading = authState.isLoading
            )
        }
    }
}

private fun openHome(
    navController: NavHostController,
    role: String?
) {
    val destination = when (role) {
        "CUSTOMER" -> Routes.HOME
        "PARTNER_DRIVER" -> Routes.PARTNER_HOME
        else -> Routes.LOGIN
    }

    navController.navigate(destination) {
        popUpTo(navController.graph.id) {
            inclusive = false
        }

        launchSingleTop = true
    }
}

@Composable
private fun AccountAccessOverlay(loading: Boolean) {
    BackHandler {}

    val interactionSource = remember {
        MutableInteractionSource()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0B0F0D))
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {},

        horizontalAlignment = Alignment.CenterHorizontally,

        verticalArrangement = Arrangement.spacedBy(
            12.dp,
            Alignment.CenterVertically
        )
    ) {
        if (loading) {
            CircularProgressIndicator(
                color = Color(0xFF8CE563)
            )

            Text(
                text = "Validando sessão...",
                color = Color.White
            )
        }
    }
}