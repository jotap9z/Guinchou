package com.guinchou.app.viewmodel

import android.content.Context
import com.guinchou.app.data.repository.CustomerTowRequestDraft
import com.guinchou.app.data.repository.CustomerTowRequestRepository

suspend fun TowRequestViewModel.createCustomerTowRequest(
    context: Context
): String {
    require(pickupAddress.isNotBlank()) {
        "Informe onde o veículo está."
    }

    require(destinationAddress.isNotBlank()) {
        "Informe o destino."
    }

    require(vehicleType.isNotBlank()) {
        "Selecione o tipo de veículo."
    }

    require(problemType.isNotBlank()) {
        "Informe o tipo de problema."
    }

    val draft = CustomerTowRequestDraft(
        vehicleType = vehicleType,
        vehicleBrand = vehicleBrand,
        vehicleModel = vehicleModel,
        vehicleYear = vehicleYear,
        vehiclePlate = vehiclePlate,
        originAddress = pickupAddress,
        originLatitude = pickupLatitude,
        originLongitude = pickupLongitude,
        destinationAddress = destinationAddress,
        destinationLatitude = destinationLatitude,
        destinationLongitude = destinationLongitude,
        problemType = problemType,
        problemDetail = problemDetail,
        problemDescription = problemDescription
    )

    return CustomerTowRequestRepository(context.applicationContext)
        .createDraft(draft)
}