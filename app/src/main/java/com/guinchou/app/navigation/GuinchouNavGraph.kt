package com.guinchou.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.guinchou.app.model.TowRequestStatus
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
import com.guinchou.app.viewmodel.TowRequestViewModel

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
                            inclusive =
                                true
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

                onLoginClick = {
                        _, _ ->

                    openHome(
                        navController
                    )
                },

                onGoogleClick = {

                    openHome(
                        navController
                    )
                },

                onCreateAccountClick = {

                },

                onForgotPasswordClick = {

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
         * PARCEIRO
         * =========================================
         */

        composable(
            route =
                Routes.PARTNER
        ) {

            PartnerTypeScreen(

                onIndependentDriverClick = {

                },

                onCompanyClick = {

                },

                onBackClick = {

                    navController
                        .popBackStack()
                }
            )
        }


        /*
         * =========================================
         * HOME
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
         * PICKUP
         * =========================================
         */

        composable(
            route =
                Routes.PICKUP
        ) {

            PickupScreen(

                onContinueClick = {
                        address,
                        latitude,
                        longitude,
                        source ->

                    if (
                        source ==
                        "GPS" &&
                        latitude !=
                        null &&
                        longitude !=
                        null
                    ) {

                        towRequestViewModel
                            .updatePickupFromGps(
                                address =
                                    address,
                                latitude =
                                    latitude,
                                longitude =
                                    longitude
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

                    navController
                        .popBackStack()
                }
            )
        }


        /*
         * =========================================
         * DESTINO
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
         * VEÍCULO
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
         * PROBLEMA
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

                initialPhotoOneUri =
                    towRequestViewModel
                        .vehiclePhotoOneUri,

                initialPhotoTwoUri =
                    towRequestViewModel
                        .vehiclePhotoTwoUri,

                onPhotoOneChanged = {
                        uri ->

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

                onPhotoTwoChanged = {
                        uri ->

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
                            type =
                                problemType,
                            detail =
                                problemDetail,
                            description =
                                description
                        )

                    if (
                        problemType ==
                        "ACCIDENT" &&
                        !towRequestViewModel
                            .hasRequiredAccidentPhotos()
                    ) {

                        return@ProblemScreen
                    }

                    /*
                     * Temporário.
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
         * ESTIMATIVA
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

                    towRequestViewModel
                        .startSearching()

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
         * BUSCA
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
                            inclusive =
                                true
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
                            inclusive =
                                false
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

                destinationAddress =
                    towRequestViewModel
                        .destinationAddress,

                driverName =
                    towRequestViewModel
                        .acceptedDriverName,

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

                },

                onMessageClick = {

                },


                /*
                 * =================================
                 * SIMULAÇÃO DOS ESTADOS
                 * =================================
                 *
                 * Isso será removido quando
                 * conectarmos o app do parceiro.
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
                                    inclusive =
                                        true
                                }
                            }
                        }


                        else -> {

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
                            inclusive =
                                false
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
            route =
                Routes.COMPLETED
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

                onFinishClick = {
                        rating ->

                    /*
                     * Rating será enviado para
                     * o backend posteriormente.
                     *
                     * Por enquanto apenas
                     * encerramos o fluxo.
                     */

                    towRequestViewModel
                        .clearRequest()

                    navController.navigate(
                        Routes.HOME
                    ) {

                        popUpTo(
                            Routes.HOME
                        ) {
                            inclusive =
                                false
                        }
                    }
                }
            )
        }
    }
}


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
            inclusive =
                true
        }
    }
}