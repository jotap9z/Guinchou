package com.guinchou.app.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.guinchou.app.domain.PricingCalculator

/**
 * Guarda todo o estado temporário
 * de uma solicitação de guincho.
 */
class TowRequestViewModel : ViewModel() {

    /*
     * =========================================
     * PARTIDA
     * =========================================
     */

    var pickupAddress by mutableStateOf("")
        private set


    /*
     * =========================================
     * DESTINO
     * =========================================
     */

    var destinationAddress by mutableStateOf("")
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
     * DISTÂNCIA
     * =========================================
     */

    var distanceKm by mutableStateOf(0.0)
        private set


    /*
     * =========================================
     * PREÇO DO SERVIÇO
     * =========================================
     */

    var servicePrice by mutableStateOf(0.0)
        private set


    /*
     * =========================================
     * TAXA DA PLATAFORMA
     * =========================================
     */

    var platformFee by mutableStateOf(0.0)
        private set


    /*
     * Porcentagem aplicada.
     *
     * Exemplo:
     *
     * 0.10 = 10%
     * 0.06 = 6%
     */
    var platformFeePercentage by mutableStateOf(0.0)
        private set


    /*
     * =========================================
     * VALOR DO PARCEIRO
     * =========================================
     */

    var partnerAmount by mutableStateOf(0.0)
        private set


    /*
     * =========================================
     * PARTIDA
     * =========================================
     */

    fun updatePickupAddress(
        address: String
    ) {

        pickupAddress =
            address
    }


    /*
     * =========================================
     * DESTINO
     * =========================================
     */

    fun updateDestinationAddress(
        address: String
    ) {

        destinationAddress =
            address
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

        vehicleType =
            type

        vehicleBrand =
            brand

        vehicleModel =
            model

        vehicleYear =
            year

        vehiclePlate =
            plate
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

        problemType =
            type

        problemDetail =
            detail

        problemDescription =
            description
    }


    /*
     * =========================================
     * DISTÂNCIA + CÁLCULO FINANCEIRO
     * =========================================
     *
     * Quando recebemos a distância,
     * calculamos automaticamente:
     *
     * - preço;
     * - porcentagem;
     * - taxa da plataforma;
     * - valor do parceiro.
     */
    fun calculateEstimate(
        distance: Double
    ) {

        /*
         * Guarda distância.
         */
        distanceKm =
            distance


        /*
         * Calcula preço.
         */
        servicePrice =
            PricingCalculator
                .calculateServicePrice(
                    distance
                )


        /*
         * Descobre se usamos
         * 10% ou 6%.
         */
        platformFeePercentage =
            PricingCalculator
                .calculatePlatformFeePercentage(
                    servicePrice
                )


        /*
         * Valor da plataforma.
         */
        platformFee =
            PricingCalculator
                .calculatePlatformFee(
                    servicePrice
                )


        /*
         * Valor do parceiro.
         */
        partnerAmount =
            PricingCalculator
                .calculatePartnerAmount(
                    servicePrice
                )
    }


    /*
     * =========================================
     * LIMPAR PEDIDO
     * =========================================
     */

    fun clearRequest() {

        pickupAddress = ""

        destinationAddress = ""

        vehicleType = ""

        vehicleBrand = ""

        vehicleModel = ""

        vehicleYear = ""

        vehiclePlate = ""

        problemType = ""

        problemDetail = ""

        problemDescription = ""

        distanceKm = 0.0

        servicePrice = 0.0

        platformFee = 0.0

        platformFeePercentage = 0.0

        partnerAmount = 0.0
    }
}