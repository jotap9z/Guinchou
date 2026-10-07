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
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.guinchou.app.ui.screens.admin.AdminApprovalsScreen
import com.guinchou.app.ui.screens.admin.AdminCallsScreen
import com.guinchou.app.ui.screens.admin.AdminFinanceScreen
import com.guinchou.app.ui.screens.admin.AdminHomeScreen
import com.guinchou.app.ui.screens.admin.AdminManagementScreen
import com.guinchou.app.ui.screens.admin.AdminPaymentsScreen
import com.guinchou.app.ui.screens.admin.AdminPayoutsScreen
import com.guinchou.app.ui.screens.admin.AdminPricingScreen
import com.guinchou.app.ui.screens.admin.AdminSettingsScreen
import com.guinchou.app.ui.screens.admin.AdminUsersScreen
import com.guinchou.app.ui.screens.partner.CompanyRegistrationScreen
import com.guinchou.app.ui.screens.partner.IndependentDriverRegistrationScreen
import com.guinchou.app.ui.screens.partner.PartnerCallsScreen
import com.guinchou.app.ui.screens.partner.PartnerEarningsScreen
import com.guinchou.app.ui.screens.partner.PartnerHomeScreen
import com.guinchou.app.ui.screens.partner.PartnerNotificationsScreen
import com.guinchou.app.ui.screens.partner.PartnerProfileScreen
import com.guinchou.app.ui.screens.partner.PartnerTypeScreen
import com.guinchou.app.ui.screens.partner.company.CompanyCallsScreen
import com.guinchou.app.ui.screens.partner.company.CompanyDriversScreen
import com.guinchou.app.ui.screens.partner.company.CompanyFinanceScreen
import com.guinchou.app.ui.screens.partner.company.CompanyFleetScreen
import com.guinchou.app.ui.screens.partner.company.CompanyHomeScreen
import com.guinchou.app.ui.screens.partner.company.CompanyManagementScreen
import com.guinchou.app.ui.screens.partner.company.CompanyNotificationsScreen
import com.guinchou.app.ui.screens.partner.company.CompanyProfileScreen
import com.guinchou.app.ui.screens.partner.company.DriverInviteQrScreen
import com.guinchou.app.ui.screens.partner.company.TowTruckInviteQrScreen
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

private const val FRONTEND_CUSTOMER = "CUSTOMER"
private const val FRONTEND_PARTNER_DRIVER = "PARTNER_DRIVER"
private const val FRONTEND_PARTNER_COMPANY = "PARTNER_COMPANY"
private const val FRONTEND_ADMIN = "ADMIN"

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

    var frontendAccessType by rememberSaveable {
        mutableStateOf(FRONTEND_CUSTOMER)
    }

    val authState by authViewModel.uiState.collectAsStateWithLifecycle()
    val entry by navController.currentBackStackEntryAsState()
    val currentRoute = entry?.destination?.route

    val partnerDriverRoutes = setOf(
        Routes.PARTNER_HOME,
        PARTNER_CALLS_ROUTE,
        PARTNER_EARNINGS_ROUTE,
        PARTNER_EARNINGS_HISTORY_ROUTE,
        PARTNER_NOTIFICATIONS_ROUTE,
        PARTNER_PROFILE_ROUTE
    )

    val companyRoutes = setOf(
        Routes.COMPANY_HOME,
        Routes.COMPANY_NOTIFICATIONS,
        Routes.COMPANY_CALLS,
        Routes.COMPANY_MANAGEMENT,
        Routes.COMPANY_FINANCE,
        Routes.COMPANY_PROFILE,
        Routes.COMPANY_DRIVERS,
        Routes.COMPANY_FLEET,
        Routes.COMPANY_DRIVER_INVITE,
        Routes.COMPANY_TOW_TRUCK_INVITE
    )

    val adminRoutes = setOf(
        Routes.ADMIN_HOME,
        Routes.ADMIN_APPROVALS,
        Routes.ADMIN_USERS,
        Routes.ADMIN_CALLS,
        Routes.ADMIN_PRICING,
        Routes.ADMIN_PAYMENTS,
        Routes.ADMIN_PAYOUTS,
        Routes.ADMIN_FINANCE,
        Routes.ADMIN_MANAGEMENT,
        Routes.ADMIN_SETTINGS
    )

    val publicRoutes = setOf(
        Routes.SPLASH,
        Routes.LOGIN,
        Routes.CREATE_ACCOUNT,
        Routes.FORGOT_PASSWORD,
        Routes.RECOVERY_EMAIL_SENT,
        Routes.PARTNER,
        Routes.DRIVER_REGISTER,
        Routes.COMPANY_REGISTER
    )

    val requiredRole = when {
        currentRoute == null -> null
        currentRoute in publicRoutes -> null

        /*
         * Enquanto a área empresarial está em Front-End,
         * não exigimos role do Supabase nessas rotas.
         */
        currentRoute in companyRoutes -> null

        /*
         * ADMIN também está em modo Front-End nesta fase.
         */
        currentRoute in adminRoutes -> null

        currentRoute in partnerDriverRoutes -> "PARTNER_DRIVER"
        else -> "CUSTOMER"
    }

    val accessGranted =
        requiredRole == null ||
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

    /*
     * Navegação principal da área empresarial.
     * Mantém CompanyHome como raiz para evitar empilhar
     * várias telas do footer.
     */
    val navigateCompany: (String) -> Unit = { route ->
        navController.navigate(route) {
            popUpTo(Routes.COMPANY_HOME) {
                inclusive = false
            }

            launchSingleTop = true
        }
    }

    /*
     * Navegação principal da área ADMIN.
     * Mantém AdminHome como raiz do painel administrativo.
     */
    val navigateAdmin: (String) -> Unit = { route ->
        navController.navigate(route) {
            popUpTo(Routes.ADMIN_HOME) {
                inclusive = false
            }

            launchSingleTop = true
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        NavHost(
            navController = navController,
            startDestination = Routes.SPLASH
        ) {

            /* =====================================================
             * SPLASH
             * ===================================================== */

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

            /* =====================================================
             * LOGIN
             * ===================================================== */

            composable(
                route = Routes.LOGIN
            ) {
                val authUiState by authViewModel.uiState
                    .collectAsStateWithLifecycle()

                LaunchedEffect(
                    authUiState.isAuthenticated,
                    authUiState.profile?.role,
                    frontendAccessType
                ) {
                    /*
                     * Empresa continua em modo Front-End.
                     * Cliente e motorista usam o fluxo real atual.
                     */
                    if (
                        authUiState.isAuthenticated &&
                        frontendAccessType != FRONTEND_PARTNER_COMPANY &&
                        frontendAccessType != FRONTEND_ADMIN
                    ) {
                        openHome(
                            navController = navController,
                            role = authUiState.profile?.role
                        )
                    }
                }

                LoginScreen(
                    isLoading =
                        if (
                            frontendAccessType == FRONTEND_PARTNER_COMPANY
                        ) {
                            false
                        } else {
                            authUiState.isLoading
                        },

                    externalErrorMessage =
                        if (
                            frontendAccessType == FRONTEND_PARTNER_COMPANY
                        ) {
                            null
                        } else {
                            authUiState.errorMessage
                        },

                    onLoginClick = { identifier, password ->
                        val cleanIdentifier = identifier.trim()

                        when {
                            frontendAccessType == FRONTEND_PARTNER_COMPANY -> {
                                if (
                                    cleanIdentifier.isNotBlank() &&
                                    password.isNotBlank()
                                ) {
                                    openFrontendCompanyHome(
                                        navController
                                    )
                                }
                            }

                            cleanIdentifier.equals(
                                "admin@guinchou.com",
                                ignoreCase = true
                            ) && password.isNotBlank() -> {
                                frontendAccessType = FRONTEND_ADMIN

                                openFrontendAdminHome(
                                    navController
                                )
                            }

                            cleanIdentifier.contains("@") -> {
                                authViewModel.signIn(
                                    email = cleanIdentifier,
                                    password = password
                                )
                            }
                        }
                    },

                    onGoogleClick = {},

                    onCreateAccountClick = {
                        frontendAccessType = FRONTEND_CUSTOMER

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

            /* =====================================================
             * CRIAR CONTA / RECUPERAÇÃO
             * ===================================================== */

            composable(
                route = Routes.CREATE_ACCOUNT
            ) {
                CreateAccountScreen(
                    onCreateAccountClick = { _, _, _, _, _ ->
                        frontendAccessType = FRONTEND_CUSTOMER

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

            composable(
                route = Routes.FORGOT_PASSWORD
            ) {
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

            composable(
                route = Routes.RECOVERY_EMAIL_SENT
            ) {
                RecoveryEmailSentScreen(
                    email = recoveryEmail,

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

                    onResendClick = {}
                )
            }

            /* =====================================================
             * ESCOLHA DE PARCEIRO
             * ===================================================== */

            composable(
                route = Routes.PARTNER
            ) {
                PartnerTypeScreen(
                    onIndependentDriverClick = {
                        frontendAccessType =
                            FRONTEND_PARTNER_DRIVER

                        navController.navigate(
                            Routes.DRIVER_REGISTER
                        )
                    },

                    onCompanyClick = {
                        frontendAccessType =
                            FRONTEND_PARTNER_COMPANY

                        navController.navigate(
                            Routes.COMPANY_REGISTER
                        )
                    },

                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }

            /* =====================================================
             * CADASTRO MOTORISTA
             * ===================================================== */

            composable(
                route = Routes.DRIVER_REGISTER
            ) {
                IndependentDriverRegistrationScreen(
                    onBackClick = {
                        navController.popBackStack()
                    },

                    onRegistrationFinished = {
                        frontendAccessType =
                            FRONTEND_PARTNER_DRIVER

                        navController.navigate(
                            Routes.LOGIN
                        ) {
                            popUpTo(
                                Routes.PARTNER
                            ) {
                                inclusive = true
                            }

                            launchSingleTop = true
                        }
                    }
                )
            }

            /* =====================================================
             * CADASTRO EMPRESA
             * ===================================================== */

            composable(
                route = Routes.COMPANY_REGISTER
            ) {
                CompanyRegistrationScreen(
                    onBackClick = {
                        navController.popBackStack()
                    },

                    onRegistrationFinished = {
                        frontendAccessType =
                            FRONTEND_PARTNER_COMPANY

                        navController.navigate(
                            Routes.LOGIN
                        ) {
                            popUpTo(
                                Routes.PARTNER
                            ) {
                                inclusive = true
                            }

                            launchSingleTop = true
                        }
                    }
                )
            }

            /* =====================================================
             * ÁREA EMPRESARIAL
             * ===================================================== */

            composable(
                route = Routes.COMPANY_HOME
            ) {
                CompanyHomeScreen(
                    ownerName =
                        authState.profile?.fullName
                            ?: "Gestor",

                    companyName =
                        "Empresa de Guinchos",

                    driversCount = 3,
                    towTrucksCount = 3,
                    activeCalls = 1,
                    monthlyRevenue = 3250.00,

                    onNotificationsClick = {
                        navController.navigate(
                            Routes.COMPANY_NOTIFICATIONS
                        ) {
                            launchSingleTop = true
                        }
                    },

                    onDriversClick = {
                        navigateCompany(
                            Routes.COMPANY_DRIVERS
                        )
                    },

                    onFleetClick = {
                        navigateCompany(
                            Routes.COMPANY_FLEET
                        )
                    },

                    onCallsClick = {
                        navigateCompany(
                            Routes.COMPANY_CALLS
                        )
                    },

                    onFinanceClick = {
                        navigateCompany(
                            Routes.COMPANY_FINANCE
                        )
                    },

                    onDriverInviteClick = {
                        navController.navigate(
                            Routes.COMPANY_DRIVER_INVITE
                        ) {
                            launchSingleTop = true
                        }
                    },

                    onTowTruckInviteClick = {
                        navController.navigate(
                            Routes.COMPANY_TOW_TRUCK_INVITE
                        ) {
                            launchSingleTop = true
                        }
                    },

                    onManagementClick = {
                        navigateCompany(
                            Routes.COMPANY_MANAGEMENT
                        )
                    },

                    onProfileClick = {
                        navigateCompany(
                            Routes.COMPANY_PROFILE
                        )
                    }
                )
            }

            composable(
                route = Routes.COMPANY_NOTIFICATIONS
            ) {
                CompanyNotificationsScreen(
                    onBackClick = {
                        navController.popBackStack()
                    },

                    onDriverClick = {
                        navigateCompany(
                            Routes.COMPANY_DRIVERS
                        )
                    },

                    onFleetClick = {
                        navigateCompany(
                            Routes.COMPANY_FLEET
                        )
                    },

                    onCallClick = {
                        navigateCompany(
                            Routes.COMPANY_CALLS
                        )
                    },

                    onFinanceClick = {
                        navigateCompany(
                            Routes.COMPANY_FINANCE
                        )
                    }
                )
            }

            composable(
                route = Routes.COMPANY_CALLS
            ) {
                CompanyCallsScreen(
                    onBackClick = {
                        navController.popBackStack()
                    },

                    onHomeClick = {
                        navigateCompany(
                            Routes.COMPANY_HOME
                        )
                    },

                    onManagementClick = {
                        navigateCompany(
                            Routes.COMPANY_MANAGEMENT
                        )
                    },

                    onProfileClick = {
                        navigateCompany(
                            Routes.COMPANY_PROFILE
                        )
                    }
                )
            }

            composable(
                route = Routes.COMPANY_MANAGEMENT
            ) {
                CompanyManagementScreen(
                    onHomeClick = {
                        navigateCompany(
                            Routes.COMPANY_HOME
                        )
                    },

                    onCallsClick = {
                        navigateCompany(
                            Routes.COMPANY_CALLS
                        )
                    },

                    onProfileClick = {
                        navigateCompany(
                            Routes.COMPANY_PROFILE
                        )
                    },

                    onDriversClick = {
                        navigateCompany(
                            Routes.COMPANY_DRIVERS
                        )
                    },

                    onFleetClick = {
                        navigateCompany(
                            Routes.COMPANY_FLEET
                        )
                    },

                    onFinanceClick = {
                        navigateCompany(
                            Routes.COMPANY_FINANCE
                        )
                    },

                    onDriverInviteClick = {
                        navController.navigate(
                            Routes.COMPANY_DRIVER_INVITE
                        ) {
                            launchSingleTop = true
                        }
                    },

                    onTowTruckInviteClick = {
                        navController.navigate(
                            Routes.COMPANY_TOW_TRUCK_INVITE
                        ) {
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable(
                route = Routes.COMPANY_FINANCE
            ) {
                CompanyFinanceScreen(
                    onBackClick = {
                        navController.popBackStack()
                    },

                    onHomeClick = {
                        navigateCompany(
                            Routes.COMPANY_HOME
                        )
                    },

                    onCallsClick = {
                        navigateCompany(
                            Routes.COMPANY_CALLS
                        )
                    },

                    onManagementClick = {
                        navigateCompany(
                            Routes.COMPANY_MANAGEMENT
                        )
                    },

                    onProfileClick = {
                        navigateCompany(
                            Routes.COMPANY_PROFILE
                        )
                    }
                )
            }

            composable(
                route = Routes.COMPANY_DRIVERS
            ) {
                CompanyDriversScreen(
                    onBackClick = {
                        navController.popBackStack()
                    },

                    onInviteClick = {
                        navController.navigate(
                            Routes.COMPANY_DRIVER_INVITE
                        ) {
                            launchSingleTop = true
                        }
                    },

                    onHomeClick = {
                        navigateCompany(
                            Routes.COMPANY_HOME
                        )
                    },

                    onCallsClick = {
                        navigateCompany(
                            Routes.COMPANY_CALLS
                        )
                    },

                    onManagementClick = {
                        navigateCompany(
                            Routes.COMPANY_MANAGEMENT
                        )
                    },

                    onProfileClick = {
                        navigateCompany(
                            Routes.COMPANY_PROFILE
                        )
                    }
                )
            }

            composable(
                route = Routes.COMPANY_FLEET
            ) {
                CompanyFleetScreen(
                    onBackClick = {
                        navController.popBackStack()
                    },

                    onInviteClick = {
                        navController.navigate(
                            Routes.COMPANY_TOW_TRUCK_INVITE
                        ) {
                            launchSingleTop = true
                        }
                    },

                    onHomeClick = {
                        navigateCompany(
                            Routes.COMPANY_HOME
                        )
                    },

                    onCallsClick = {
                        navigateCompany(
                            Routes.COMPANY_CALLS
                        )
                    },

                    onManagementClick = {
                        navigateCompany(
                            Routes.COMPANY_MANAGEMENT
                        )
                    },

                    onProfileClick = {
                        navigateCompany(
                            Routes.COMPANY_PROFILE
                        )
                    }
                )
            }

            composable(
                route = Routes.COMPANY_PROFILE
            ) {
                CompanyProfileScreen(
                    ownerName =
                        authState.profile?.fullName
                            ?: "Gestor",

                    companyName =
                        "Empresa de Guinchos",

                    onHomeClick = {
                        navigateCompany(
                            Routes.COMPANY_HOME
                        )
                    },

                    onCallsClick = {
                        navigateCompany(
                            Routes.COMPANY_CALLS
                        )
                    },

                    onManagementClick = {
                        navigateCompany(
                            Routes.COMPANY_MANAGEMENT
                        )
                    },

                    onNotificationsClick = {
                        navController.navigate(
                            Routes.COMPANY_NOTIFICATIONS
                        ) {
                            launchSingleTop = true
                        }
                    },

                    onLogoutClick = {
                        frontendAccessType =
                            FRONTEND_CUSTOMER

                        navController.navigate(
                            Routes.LOGIN
                        ) {
                            popUpTo(
                                navController.graph.id
                            ) {
                                inclusive = false
                            }

                            launchSingleTop = true
                        }
                    }
                )
            }

            composable(
                route = Routes.COMPANY_DRIVER_INVITE
            ) {
                DriverInviteQrScreen(
                    companyName =
                        "Empresa de Guinchos",

                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }

            composable(
                route = Routes.COMPANY_TOW_TRUCK_INVITE
            ) {
                TowTruckInviteQrScreen(
                    companyName =
                        "Empresa de Guinchos",

                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }

            /* =====================================================
             * ÁREA ADMINISTRATIVA
             * ===================================================== */

            composable(
                route = Routes.ADMIN_HOME
            ) {
                AdminHomeScreen(
                    adminName = "Administrador",

                    onApprovalsClick = {
                        navigateAdmin(
                            Routes.ADMIN_APPROVALS
                        )
                    },

                    onFinanceClick = {
                        navigateAdmin(
                            Routes.ADMIN_FINANCE
                        )
                    },

                    onManagementClick = {
                        navigateAdmin(
                            Routes.ADMIN_MANAGEMENT
                        )
                    },

                    onUsersClick = {
                        navigateAdmin(
                            Routes.ADMIN_USERS
                        )
                    },

                    onCallsClick = {
                        navigateAdmin(
                            Routes.ADMIN_CALLS
                        )
                    },

                    onPaymentsClick = {
                        navigateAdmin(
                            Routes.ADMIN_PAYMENTS
                        )
                    },

                    onPayoutsClick = {
                        navigateAdmin(
                            Routes.ADMIN_PAYOUTS
                        )
                    },

                    /*
                     * Enquanto não existe uma tela ADMIN exclusiva
                     * de notificações, o sino abre a central de
                     * aprovações/atenção necessária.
                     */
                    onNotificationsClick = {
                        navigateAdmin(
                            Routes.ADMIN_APPROVALS
                        )
                    }
                )
            }

            composable(
                route = Routes.ADMIN_APPROVALS
            ) {
                AdminApprovalsScreen(
                    onBackClick = {
                        navController.popBackStack()
                    },

                    onHomeClick = {
                        navigateAdmin(
                            Routes.ADMIN_HOME
                        )
                    },

                    onFinanceClick = {
                        navigateAdmin(
                            Routes.ADMIN_FINANCE
                        )
                    },

                    onManagementClick = {
                        navigateAdmin(
                            Routes.ADMIN_MANAGEMENT
                        )
                    }
                )
            }

            composable(
                route = Routes.ADMIN_USERS
            ) {
                AdminUsersScreen(
                    onBackClick = {
                        navController.popBackStack()
                    },

                    onHomeClick = {
                        navigateAdmin(
                            Routes.ADMIN_HOME
                        )
                    },

                    onApprovalsClick = {
                        navigateAdmin(
                            Routes.ADMIN_APPROVALS
                        )
                    },

                    onFinanceClick = {
                        navigateAdmin(
                            Routes.ADMIN_FINANCE
                        )
                    },

                    onManagementClick = {
                        navigateAdmin(
                            Routes.ADMIN_MANAGEMENT
                        )
                    }
                )
            }

            composable(
                route = Routes.ADMIN_CALLS
            ) {
                AdminCallsScreen(
                    onBackClick = {
                        navController.popBackStack()
                    },

                    onHomeClick = {
                        navigateAdmin(
                            Routes.ADMIN_HOME
                        )
                    },

                    onApprovalsClick = {
                        navigateAdmin(
                            Routes.ADMIN_APPROVALS
                        )
                    },

                    onFinanceClick = {
                        navigateAdmin(
                            Routes.ADMIN_FINANCE
                        )
                    },

                    onManagementClick = {
                        navigateAdmin(
                            Routes.ADMIN_MANAGEMENT
                        )
                    }
                )
            }

            composable(
                route = Routes.ADMIN_PRICING
            ) {
                AdminPricingScreen(
                    onBackClick = {
                        navController.popBackStack()
                    },

                    onHomeClick = {
                        navigateAdmin(
                            Routes.ADMIN_HOME
                        )
                    },

                    onApprovalsClick = {
                        navigateAdmin(
                            Routes.ADMIN_APPROVALS
                        )
                    },

                    onFinanceClick = {
                        navigateAdmin(
                            Routes.ADMIN_FINANCE
                        )
                    },

                    onManagementClick = {
                        navigateAdmin(
                            Routes.ADMIN_MANAGEMENT
                        )
                    }
                )
            }

            composable(
                route = Routes.ADMIN_PAYMENTS
            ) {
                AdminPaymentsScreen(
                    onBackClick = {
                        navController.popBackStack()
                    },

                    onHomeClick = {
                        navigateAdmin(
                            Routes.ADMIN_HOME
                        )
                    },

                    onApprovalsClick = {
                        navigateAdmin(
                            Routes.ADMIN_APPROVALS
                        )
                    },

                    onFinanceClick = {
                        navigateAdmin(
                            Routes.ADMIN_FINANCE
                        )
                    },

                    onManagementClick = {
                        navigateAdmin(
                            Routes.ADMIN_MANAGEMENT
                        )
                    }
                )
            }

            composable(
                route = Routes.ADMIN_PAYOUTS
            ) {
                AdminPayoutsScreen(
                    onBackClick = {
                        navController.popBackStack()
                    },

                    onHomeClick = {
                        navigateAdmin(
                            Routes.ADMIN_HOME
                        )
                    },

                    onApprovalsClick = {
                        navigateAdmin(
                            Routes.ADMIN_APPROVALS
                        )
                    },

                    onFinanceClick = {
                        navigateAdmin(
                            Routes.ADMIN_FINANCE
                        )
                    },

                    onManagementClick = {
                        navigateAdmin(
                            Routes.ADMIN_MANAGEMENT
                        )
                    }
                )
            }

            composable(
                route = Routes.ADMIN_FINANCE
            ) {
                AdminFinanceScreen(
                    onHomeClick = {
                        navigateAdmin(
                            Routes.ADMIN_HOME
                        )
                    },

                    onApprovalsClick = {
                        navigateAdmin(
                            Routes.ADMIN_APPROVALS
                        )
                    },

                    onManagementClick = {
                        navigateAdmin(
                            Routes.ADMIN_MANAGEMENT
                        )
                    },

                    onPaymentsClick = {
                        navigateAdmin(
                            Routes.ADMIN_PAYMENTS
                        )
                    },

                    onPayoutsClick = {
                        navigateAdmin(
                            Routes.ADMIN_PAYOUTS
                        )
                    }
                )
            }

            composable(
                route = Routes.ADMIN_MANAGEMENT
            ) {
                AdminManagementScreen(
                    onHomeClick = {
                        navigateAdmin(
                            Routes.ADMIN_HOME
                        )
                    },

                    onApprovalsClick = {
                        navigateAdmin(
                            Routes.ADMIN_APPROVALS
                        )
                    },

                    onFinanceClick = {
                        navigateAdmin(
                            Routes.ADMIN_FINANCE
                        )
                    },

                    onUsersClick = {
                        navigateAdmin(
                            Routes.ADMIN_USERS
                        )
                    },

                    onCallsClick = {
                        navigateAdmin(
                            Routes.ADMIN_CALLS
                        )
                    },

                    onPricingClick = {
                        navigateAdmin(
                            Routes.ADMIN_PRICING
                        )
                    },

                    onPaymentsClick = {
                        navigateAdmin(
                            Routes.ADMIN_PAYMENTS
                        )
                    },

                    onPayoutsClick = {
                        navigateAdmin(
                            Routes.ADMIN_PAYOUTS
                        )
                    },

                    onSettingsClick = {
                        navigateAdmin(
                            Routes.ADMIN_SETTINGS
                        )
                    }
                )
            }

            composable(
                route = Routes.ADMIN_SETTINGS
            ) {
                AdminSettingsScreen(
                    onBackClick = {
                        navController.popBackStack()
                    },

                    onHomeClick = {
                        navigateAdmin(
                            Routes.ADMIN_HOME
                        )
                    },

                    onApprovalsClick = {
                        navigateAdmin(
                            Routes.ADMIN_APPROVALS
                        )
                    },

                    onFinanceClick = {
                        navigateAdmin(
                            Routes.ADMIN_FINANCE
                        )
                    },

                    onManagementClick = {
                        navigateAdmin(
                            Routes.ADMIN_MANAGEMENT
                        )
                    },

                    onLogoutClick = {
                        frontendAccessType = FRONTEND_CUSTOMER

                        navController.navigate(
                            Routes.LOGIN
                        ) {
                            popUpTo(
                                navController.graph.id
                            ) {
                                inclusive = false
                            }

                            launchSingleTop = true
                        }
                    }
                )
            }

            /* =====================================================
             * MOTORISTA INDEPENDENTE
             * ===================================================== */

            composable(
                route = Routes.PARTNER_HOME
            ) {
                PartnerHomeScreen(
                    driverName =
                        authState.profile?.fullName
                            ?: "Guincheiro",

                    todayTrips = 3,
                    todayEarnings = 382.50,
                    driverRating = 4.9,
                    documentWarningCount = 1,

                    onNotificationsClick = {
                        navigate(
                            PARTNER_NOTIFICATIONS_ROUTE
                        )
                    },

                    onHistoryClick = {
                        navigate(
                            PARTNER_EARNINGS_HISTORY_ROUTE
                        )
                    },

                    onEarningsClick = {
                        navigate(
                            PARTNER_EARNINGS_ROUTE
                        )
                    },

                    onDocumentsClick = {},

                    onProfileClick = {
                        navigate(
                            PARTNER_PROFILE_ROUTE
                        )
                    },

                    onTestRequestClick = {
                        navigate(
                            PARTNER_CALLS_ROUTE
                        )
                    }
                )
            }

            composable(
                route = PARTNER_CALLS_ROUTE
            ) {
                PartnerCallsScreen(
                    onBackClick = {
                        navController.popBackStack()
                    },

                    onNotificationsClick = {
                        navigate(
                            PARTNER_NOTIFICATIONS_ROUTE
                        )
                    },

                    onCallClick = {},

                    onHomeClick = {
                        navigate(
                            Routes.PARTNER_HOME
                        )
                    },

                    onEarningsClick = {
                        navigate(
                            PARTNER_EARNINGS_ROUTE
                        )
                    },

                    onProfileClick = {
                        navigate(
                            PARTNER_PROFILE_ROUTE
                        )
                    }
                )
            }

            composable(
                route = PARTNER_EARNINGS_ROUTE
            ) {
                PartnerEarningsScreen(
                    driverName =
                        authState.profile?.fullName
                            ?: "Guincheiro",

                    openHistory = false,

                    onBackClick = {
                        navController.popBackStack()
                    },

                    onNotificationsClick = {
                        navigate(
                            PARTNER_NOTIFICATIONS_ROUTE
                        )
                    },

                    onHomeClick = {
                        navigate(
                            Routes.PARTNER_HOME
                        )
                    },

                    onCallsClick = {
                        navigate(
                            PARTNER_CALLS_ROUTE
                        )
                    },

                    onProfileClick = {
                        navigate(
                            PARTNER_PROFILE_ROUTE
                        )
                    }
                )
            }

            composable(
                route = PARTNER_EARNINGS_HISTORY_ROUTE
            ) {
                PartnerEarningsScreen(
                    driverName =
                        authState.profile?.fullName
                            ?: "Guincheiro",

                    openHistory = true,

                    onBackClick = {
                        navController.popBackStack()
                    },

                    onNotificationsClick = {
                        navigate(
                            PARTNER_NOTIFICATIONS_ROUTE
                        )
                    },

                    onHomeClick = {
                        navigate(
                            Routes.PARTNER_HOME
                        )
                    },

                    onCallsClick = {
                        navigate(
                            PARTNER_CALLS_ROUTE
                        )
                    },

                    onProfileClick = {
                        navigate(
                            PARTNER_PROFILE_ROUTE
                        )
                    }
                )
            }

            composable(
                route = PARTNER_NOTIFICATIONS_ROUTE
            ) {
                PartnerNotificationsScreen(
                    onBackClick = {
                        navController.popBackStack()
                    },

                    onCallClick = {
                        navigate(
                            PARTNER_CALLS_ROUTE
                        )
                    },

                    onEarningsClick = {
                        navigate(
                            PARTNER_EARNINGS_ROUTE
                        )
                    }
                )
            }

            composable(
                route = PARTNER_PROFILE_ROUTE
            ) {
                PartnerProfileScreen(
                    driverName =
                        authState.profile?.fullName
                            ?: "Guincheiro",

                    driverEmail =
                        authState.email,

                    driverPhone =
                        authState.profile?.phone.orEmpty(),

                    driverRating = 4.9,

                    onNotificationsClick = {
                        navigate(
                            PARTNER_NOTIFICATIONS_ROUTE
                        )
                    },

                    onPersonalDataClick = {},
                    onProfessionalDataClick = {},
                    onTowTruckClick = {},
                    onDocumentsClick = {},
                    onSettingsClick = {},
                    onSupportClick = {},

                    onLogoutClick = {
                        frontendAccessType =
                            FRONTEND_CUSTOMER

                        authViewModel.signOut {
                            customerHomeViewModel.clear()
                            towRequestViewModel.clearRequest()

                            navController.navigate(
                                Routes.LOGIN
                            ) {
                                popUpTo(
                                    navController.graph.id
                                ) {
                                    inclusive = false
                                }

                                launchSingleTop = true
                            }
                        }
                    },

                    onHomeClick = {
                        navigate(
                            Routes.PARTNER_HOME
                        )
                    },

                    onCallsClick = {
                        navigate(
                            PARTNER_CALLS_ROUTE
                        )
                    },

                    onEarningsClick = {
                        navigate(
                            PARTNER_EARNINGS_ROUTE
                        )
                    }
                )
            }

            /* =====================================================
             * CLIENTE
             * ===================================================== */

            composable(
                route = Routes.HOME
            ) {
                val homeState by customerHomeViewModel.uiState
                    .collectAsStateWithLifecycle()

                val authUiState by authViewModel.uiState
                    .collectAsStateWithLifecycle()

                LaunchedEffect(
                    authUiState.isAuthenticated,
                    authUiState.isLoading,
                    authUiState.profile?.role
                ) {
                    if (
                        authUiState.isLoading
                    ) {
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
                        if (
                            !homeState.canRequestTow
                        ) {
                            return@HomeScreen
                        }

                        towRequestViewModel.clearRequest()

                        navController.navigate(
                            Routes.PICKUP
                        )
                    },

                    onNotificationClick = {
                        navigate(
                            CUSTOMER_NOTIFICATIONS_ROUTE
                        )
                    },

                    onCallsClick = {
                        navigate(
                            CUSTOMER_CALLS_ROUTE
                        )
                    },

                    onVehiclesClick = {
                        navigate(
                            CUSTOMER_VEHICLES_ROUTE
                        )
                    },

                    onHistoryClick = {
                        navigate(
                            CUSTOMER_HISTORY_ROUTE
                        )
                    },

                    onPaymentsClick = {
                        navigate(
                            CUSTOMER_PAYMENTS_ROUTE
                        )
                    },

                    onProfileClick = {
                        navController.navigate(
                            Routes.PROFILE
                        )
                    }
                )
            }

            composable(
                route = CUSTOMER_NOTIFICATIONS_ROUTE
            ) {
                CustomerNotificationsScreen(
                    onBackClick = {
                        navController.popBackStack()
                    },

                    onCallClick = {
                        navigate(
                            CUSTOMER_CALLS_ROUTE
                        )
                    },

                    onPaymentClick = {
                        navigate(
                            CUSTOMER_PAYMENTS_ROUTE
                        )
                    }
                )
            }

            composable(
                route = CUSTOMER_CALLS_ROUTE
            ) {
                CustomerCallsScreen(
                    onBackClick = {
                        navController.popBackStack()
                    },

                    onRequestTowClick = {
                        towRequestViewModel.clearRequest()

                        navController.navigate(
                            Routes.PICKUP
                        )
                    },

                    onTrackCallClick = {
                        navigate(
                            Routes.TRACKING
                        )
                    },

                    onHomeClick = {
                        navigate(
                            Routes.HOME
                        )
                    },

                    onPaymentsClick = {
                        navigate(
                            CUSTOMER_PAYMENTS_ROUTE
                        )
                    },

                    onProfileClick = {
                        navigate(
                            Routes.PROFILE
                        )
                    }
                )
            }

            composable(
                route = CUSTOMER_PAYMENTS_ROUTE
            ) {
                PaymentsScreen(
                    onBackClick = {
                        navController.popBackStack()
                    },

                    onHomeClick = {
                        navigate(
                            Routes.HOME
                        )
                    },

                    onCallsClick = {
                        navigate(
                            CUSTOMER_CALLS_ROUTE
                        )
                    },

                    onProfileClick = {
                        navigate(
                            Routes.PROFILE
                        )
                    }
                )
            }

            composable(
                route = CUSTOMER_VEHICLES_ROUTE
            ) {
                val homeState by customerHomeViewModel.uiState
                    .collectAsStateWithLifecycle()

                ProfileScreen(
                    userName =
                        homeState.home?.name
                            ?: "Cliente",

                    userEmail =
                        homeState.home?.email.orEmpty(),

                    openVehicles = true,

                    onBackClick = {
                        navController.popBackStack()
                    },

                    onLogoutClick = {
                        frontendAccessType =
                            FRONTEND_CUSTOMER

                        authViewModel.signOut {
                            customerHomeViewModel.clear()
                            towRequestViewModel.clearRequest()

                            navController.navigate(
                                Routes.LOGIN
                            ) {
                                popUpTo(
                                    navController.graph.id
                                ) {
                                    inclusive = false
                                }

                                launchSingleTop = true
                            }
                        }
                    }
                )
            }

            composable(
                route = CUSTOMER_HISTORY_ROUTE
            ) {
                val homeState by customerHomeViewModel.uiState
                    .collectAsStateWithLifecycle()

                ProfileScreen(
                    userName =
                        homeState.home?.name
                            ?: "Cliente",

                    userEmail =
                        homeState.home?.email.orEmpty(),

                    openHistory = true,

                    onBackClick = {
                        navController.popBackStack()
                    },

                    onLogoutClick = {
                        frontendAccessType =
                            FRONTEND_CUSTOMER

                        authViewModel.signOut {
                            customerHomeViewModel.clear()
                            towRequestViewModel.clearRequest()

                            navController.navigate(
                                Routes.LOGIN
                            ) {
                                popUpTo(
                                    navController.graph.id
                                ) {
                                    inclusive = false
                                }

                                launchSingleTop = true
                            }
                        }
                    }
                )
            }

            composable(
                route = Routes.PROFILE
            ) {
                val homeState by customerHomeViewModel.uiState
                    .collectAsStateWithLifecycle()

                ProfileScreen(
                    userName =
                        homeState.home?.name
                            ?: "Cliente",

                    userEmail =
                        homeState.home?.email.orEmpty(),

                    onBackClick = {
                        navController.popBackStack()
                    },

                    onLogoutClick = {
                        frontendAccessType =
                            FRONTEND_CUSTOMER

                        authViewModel.signOut {
                            customerHomeViewModel.clear()
                            towRequestViewModel.clearRequest()

                            navController.navigate(
                                Routes.LOGIN
                            ) {
                                popUpTo(
                                    navController.graph.id
                                ) {
                                    inclusive = false
                                }

                                launchSingleTop = true
                            }
                        }
                    }
                )
            }

            /* =====================================================
             * SOLICITAÇÃO DE GUINCHO
             * ===================================================== */

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
                            towRequestViewModel.updatePickupFromGps(
                                address = address,
                                latitude = latitude,
                                longitude = longitude
                            )
                        } else {
                            towRequestViewModel.updatePickupAddress(
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

            composable(
                route = Routes.DESTINATION
            ) {
                DestinationScreen(
                    onContinueClick = {
                            address,
                            latitude,
                            longitude ->

                        towRequestViewModel.updateDestinationLocation(
                            address = address,
                            latitude = latitude,
                            longitude = longitude
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

            composable(
                route = Routes.VEHICLE
            ) {
                VehicleScreen(
                    onContinueClick = {
                            type,
                            brand,
                            model,
                            year,
                            plate ->

                        towRequestViewModel.updateVehicle(
                            type = type,
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
                            towRequestViewModel.updateVehiclePhotoOne(
                                uri
                            )
                        } else {
                            towRequestViewModel.removeVehiclePhotoOne()
                        }
                    },

                    onPhotoTwoChanged = { uri ->
                        if (
                            uri != null
                        ) {
                            towRequestViewModel.updateVehiclePhotoTwo(
                                uri
                            )
                        } else {
                            towRequestViewModel.removeVehiclePhotoTwo()
                        }
                    },

                    onContinueClick = {
                            type,
                            detail,
                            description ->

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

                        navigate(
                            REQUEST_CONFIRM_ROUTE
                        )
                    },

                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }

            composable(
                route = REQUEST_CONFIRM_ROUTE
            ) {
                CustomerRequestConfirmationRoute(
                    towRequestViewModel =
                        towRequestViewModel,

                    onRegistered = {
                        customerHomeViewModel.load()
                    },

                    onBackClick = {
                        navController.popBackStack()
                    },

                    onHomeClick = {
                        towRequestViewModel.clearRequest()

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

            composable(
                route = Routes.PAYMENT
            ) {
                PaymentScreen(
                    servicePrice =
                        towRequestViewModel.servicePrice,

                    onConfirmPaymentClick = { _ ->
                        towRequestViewModel.startSearching()

                        navController.navigate(
                            Routes.SEARCHING
                        )
                    },

                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }

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
                        towRequestViewModel.acceptTowRequest(
                            driverName =
                                "Carlos Henrique",

                            towTruckDescription =
                                "Mercedes-Benz Accelo Plataforma",

                            towTruckPlate =
                                "ABC1D23",

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
                        towRequestViewModel.cancelRequest()
                        towRequestViewModel.clearRequest()

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
                        towRequestViewModel.acceptedTowTruckDescription,

                    towTruckPlate =
                        towRequestViewModel.acceptedTowTruckPlate,

                    estimatedArrivalMinutes =
                        towRequestViewModel.estimatedArrivalMinutes,

                    driverRating =
                        towRequestViewModel.acceptedDriverRating,

                    requestStatus =
                        towRequestViewModel.requestStatus,

                    onCallClick = {},
                    onMessageClick = {},

                    onAdvanceTestClick = {
                        when (
                            towRequestViewModel.requestStatus
                        ) {
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

                            else -> Unit
                        }
                    },

                    onCancelClick = {
                        towRequestViewModel.cancelRequest()
                        towRequestViewModel.clearRequest()

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

            composable(
                route = Routes.COMPLETED
            ) {
                CompletedScreen(
                    driverName =
                        towRequestViewModel.acceptedDriverName,

                    pickupAddress =
                        towRequestViewModel.pickupAddress,

                    destinationAddress =
                        towRequestViewModel.destinationAddress,

                    servicePrice =
                        towRequestViewModel.servicePrice,

                    onFinishClick = { _ ->
                        towRequestViewModel.clearRequest()

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

        if (
            !accessGranted
        ) {
            AccountAccessOverlay(
                loading =
                    authState.isLoading
            )
        }
    }
}

private fun openHome(
    navController: NavHostController,
    role: String?
) {
    val destination =
        when (role) {
            "CUSTOMER" ->
                Routes.HOME

            "PARTNER_DRIVER" ->
                Routes.PARTNER_HOME

            "PARTNER_COMPANY" ->
                Routes.COMPANY_HOME

            "ADMIN" ->
                Routes.ADMIN_HOME

            else ->
                Routes.LOGIN
        }

    navController.navigate(
        destination
    ) {
        popUpTo(
            navController.graph.id
        ) {
            inclusive = false
        }

        launchSingleTop = true
    }
}

private fun openFrontendCompanyHome(
    navController: NavHostController
) {
    navController.navigate(
        Routes.COMPANY_HOME
    ) {
        popUpTo(
            Routes.LOGIN
        ) {
            inclusive = true
        }

        launchSingleTop = true
    }
}

private fun openFrontendAdminHome(
    navController: NavHostController
) {
    navController.navigate(
        Routes.ADMIN_HOME
    ) {
        popUpTo(
            Routes.LOGIN
        ) {
            inclusive = true
        }

        launchSingleTop = true
    }
}

@Composable
private fun AccountAccessOverlay(
    loading: Boolean
) {
    BackHandler {}

    val interactionSource =
        remember {
            MutableInteractionSource()
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Color(0xFF0B0F0D)
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {},

        horizontalAlignment =
            Alignment.CenterHorizontally,

        verticalArrangement =
            Arrangement.spacedBy(
                12.dp,
                Alignment.CenterVertically
            )
    ) {
        if (
            loading
        ) {
            CircularProgressIndicator(
                color =
                    Color(0xFF8CE563)
            )

            Text(
                text =
                    "Validando sessão...",

                color =
                    Color.White
            )
        }
    }
}
