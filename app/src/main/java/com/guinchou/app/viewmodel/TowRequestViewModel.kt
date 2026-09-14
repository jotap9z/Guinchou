package com.guinchou.app.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.guinchou.app.domain.PricingCalculator
import com.guinchou.app.model.TowRequestStatus

/**
 * Estado central de uma solicitação
 * de guincho.
 */
class TowRequestViewModel : ViewModel() {

    /*
     * =========================================
     * STATUS DO CHAMADO
     * =========================================
     */

    var requestStatus by mutableStateOf(
        TowRequestStatus.CREATED,
    )
        private set


    /*
     * =========================================
     * LOCALIZAÇÃO
     * =========================================
     */

    var pickupAddress by mutableStateOf("")
        private set

    var pickupLatitude by mutableStateOf<Double?>(null)
        private set

    var pickupLongitude by mutableStateOf<Double?>(null)
        private set

    var pickupLocationSource by mutableStateOf("")
        private set


    /*
     * =========================================
     * DESTINO
     * =========================================
     */

    var destinationAddress by mutableStateOf("")
        private set

    var destinationLatitude by mutableStateOf<Double?>(null)
        private set

    var destinationLongitude by mutableStateOf<Double?>(null)
        private set


    /*
     * =========================================
     * VEÍCULO
     * =========================================
     */

    var vehicleType by mutableStateOf("")
        private set

    var vehicleBrand by mutableStateOf("")
        private set

    var vehicleModel by mutableStateOf("")
        private set

    var vehicleYear by mutableStateOf("")
        private set

    var vehiclePlate by mutableStateOf("")
        private set


    /*
     * =========================================
     * PROBLEMA
     * =========================================
     */

    var problemType by mutableStateOf("")
        private set

    var problemDetail by mutableStateOf("")
        private set

    var problemDescription by mutableStateOf("")
        private set


    /*
     * =========================================
     * FOTOS
     * =========================================
     */

    var vehiclePhotoOneUri by mutableStateOf<String?>(null)
        private set

    var vehiclePhotoTwoUri by mutableStateOf<String?>(null)
        private set


    /*
     * =========================================
     * FINANCEIRO
     * =========================================
     */

    var distanceKm by androidx.compose.runtime.mutableDoubleStateOf(0.0)
        private set

    var servicePrice by androidx.compose.runtime.mutableDoubleStateOf(0.0)
        private set

    var platformFee by androidx.compose.runtime.mutableDoubleStateOf(0.0)
        private set

    var platformFeePercentage by androidx.compose.runtime.mutableDoubleStateOf(0.0)
        private set

    var partnerAmount by androidx.compose.runtime.mutableDoubleStateOf(0.0)
        private set


    /*
     * =========================================
     * DADOS DO PARCEIRO QUE ACEITOU
     * =========================================
     *
     * Ainda simulados.
     *
     * Depois virão do backend.
     */

    var acceptedDriverName by mutableStateOf("")
        private set

    var acceptedTowTruckDescription by mutableStateOf("")
        private set

    var acceptedTowTruckPlate by mutableStateOf("")
        private set

    var acceptedDriverRating by mutableStateOf(0.0)
        private set

    var estimatedArrivalMinutes by androidx.compose.runtime.mutableIntStateOf(0)
        private set


    /*
     * =========================================
     * STATUS
     * =========================================
     */

    fun updateRequestStatus(
        status: TowRequestStatus,
    ) {

        requestStatus = status
    }


    /*
     * =========================================
     * LOCALIZAÇÃO MANUAL
     * =========================================
     */

    fun updatePickupAddress(
        address: String,
    ) {

        pickupAddress = address

        pickupLatitude = null

        pickupLongitude = null

        pickupLocationSource = "MANUAL"
    }


    /*
     * =========================================
     * LOCALIZAÇÃO GPS
     * =========================================
     */

    fun updatePickupFromGps(
        address: String,
        latitude: Double,
        longitude: Double
    ) {

        pickupAddress = address

        pickupLatitude = latitude

        pickupLongitude = longitude

        pickupLocationSource = "GPS"
    }


    /*
     * =========================================
     * DESTINO
     * =========================================
     */

    fun updateDestinationAddress(
        address: String
    ) {

        destinationAddress = address

        destinationLatitude = null

        destinationLongitude = null
    }


    fun updateDestinationLocation(
        address: String,
        latitude: Double,
        longitude: Double
    ) {

        destinationAddress = address

        destinationLatitude = latitude

        destinationLongitude = longitude
    }


    /*
     * =========================================
     * VEÍCULO
     * =========================================
     */

    fun updateVehicle(
        type: String,
        brand: String,
        model: String,
        year: String,
        plate: String
    ) {

        vehicleType = type

        vehicleBrand = brand

        vehicleModel = model

        vehicleYear = year

        vehiclePlate = plate
    }


    /*
     * =========================================
     * PROBLEMA
     * =========================================
     */

    fun updateProblem(
        type: String,
        detail: String,
        description: String
    ) {

        problemType = type

        problemDetail = detail

        problemDescription = description

        if (
            type != "ACCIDENT"
        ) {

            clearVehiclePhotos()
        }
    }


    /*
     * =========================================
     * FOTOS
     * =========================================
     */

    fun updateVehiclePhotoOne(
        uri: String
    ) {

        vehiclePhotoOneUri = uri
    }


    fun updateVehiclePhotoTwo(
        uri: String
    ) {

        vehiclePhotoTwoUri = uri
    }


    fun removeVehiclePhotoOne() {

        vehiclePhotoOneUri = null
    }


    fun removeVehiclePhotoTwo() {

        vehiclePhotoTwoUri = null
    }


    fun clearVehiclePhotos() {

        vehiclePhotoOneUri = null

        vehiclePhotoTwoUri = null
    }


    fun hasRequiredAccidentPhotos(): Boolean {

        return (
                (vehiclePhotoOneUri != null) &&
                        (vehiclePhotoTwoUri != null)
                )
    }


    /*
     * =========================================
     * ESTIMATIVA
     * =========================================
     */

    fun calculateEstimate(
        distance: Double
    ) {

        distanceKm = distance

        servicePrice =
            PricingCalculator
                .calculateServicePrice(
                    distance
                )

        platformFeePercentage =
            PricingCalculator
                .calculatePlatformFeePercentage(
                    servicePrice
                )

        platformFee =
            PricingCalculator
                .calculatePlatformFee(
                    servicePrice
                )

        partnerAmount =
            PricingCalculator
                .calculatePartnerAmount(
                    servicePrice
                )
    }


    /*
     * =========================================
     * INICIAR BUSCA
     * =========================================
     */

    fun startSearching() {

        requestStatus =
            TowRequestStatus.SEARCHING
    }


    /*
     * =========================================
     * PARCEIRO ACEITOU
     * =========================================
     */

    fun acceptTowRequest(
        driverName: String,
        towTruckDescription: String,
        towTruckPlate: String,
        driverRating: Double,
        arrivalMinutes: Int
    ) {

        acceptedDriverName =
            driverName

        acceptedTowTruckDescription =
            towTruckDescription

        acceptedTowTruckPlate =
            towTruckPlate

        acceptedDriverRating =
            driverRating

        estimatedArrivalMinutes =
            arrivalMinutes

        /*
         * Primeiro marcamos como aceito.
         */
        requestStatus =
            TowRequestStatus.ACCEPTED

        /*
         * Em seguida o guincheiro
         * passa a estar a caminho.
         */
        requestStatus =
            TowRequestStatus.DRIVER_ON_THE_WAY
    }


    /*
     * =========================================
     * GUINCHEIRO CHEGOU
     * =========================================
     */

    fun markDriverArrived() {

        requestStatus =
            TowRequestStatus.ARRIVED
    }


    /*
     * =========================================
     * VEÍCULO CARREGADO
     * =========================================
     */

    fun markVehicleLoaded() {

        requestStatus =
            TowRequestStatus.VEHICLE_LOADED
    }


    /*
     * =========================================
     * TRANSPORTE INICIADO
     * =========================================
     */

    fun startTransport() {

        requestStatus =
            TowRequestStatus.IN_TRANSIT
    }


    /*
     * =========================================
     * FINALIZAR
     * =========================================
     */

    fun completeRequest() {

        requestStatus =
            TowRequestStatus.COMPLETED
    }


    /*
     * =========================================
     * CANCELAR
     * =========================================
     */

    fun cancelRequest() {

        requestStatus =
            TowRequestStatus.CANCELLED
    }


    /*
     * =========================================
     * LIMPAR SOLICITAÇÃO
     * =========================================
     */

    fun clearRequest() {

        requestStatus =
            TowRequestStatus.CREATED


        pickupAddress = ""

        pickupLatitude = null

        pickupLongitude = null

        pickupLocationSource = ""


        destinationAddress = ""

        destinationLatitude = null

        destinationLongitude = null


        vehicleType = ""

        vehicleBrand = ""

        vehicleModel = ""

        vehicleYear = ""

        vehiclePlate = ""


        problemType = ""

        problemDetail = ""

        problemDescription = ""


        vehiclePhotoOneUri = null

        vehiclePhotoTwoUri = null


        distanceKm = 0.0

        servicePrice = 0.0

        platformFee = 0.0

        platformFeePercentage = 0.0

        partnerAmount = 0.0


        acceptedDriverName = ""

        acceptedTowTruckDescription = ""

        acceptedTowTruckPlate = ""

        acceptedDriverRating = 0.0

        estimatedArrivalMinutes = 0
    }
}