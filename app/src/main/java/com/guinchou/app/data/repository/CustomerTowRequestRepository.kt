package com.guinchou.app.data.repository

import com.guinchou.app.data.remote.SupabaseProvider
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put

data class CustomerTowRequestDraft(
    val vehicleType: String,
    val vehicleBrand: String,
    val vehicleModel: String,
    val vehicleYear: String,
    val vehiclePlate: String,
    val originAddress: String,
    val originLatitude: Double?,
    val originLongitude: Double?,
    val destinationAddress: String,
    val destinationLatitude: Double?,
    val destinationLongitude: Double?,
    val problemType: String,
    val problemDetail: String,
    val problemDescription: String
)

class CustomerTowRequestRepository {

    private val client = SupabaseProvider.client

    suspend fun createDraft(draft: CustomerTowRequestDraft): String {
        check(client.auth.currentUserOrNull() != null) {
            "Faça login antes de solicitar um guincho."
        }

        val yearText = draft.vehicleYear.trim()
        val year = if (yearText.isEmpty()) {
            null
        } else {
            yearText.toIntOrNull()
                ?: throw IllegalArgumentException("Ano do veículo inválido.")
        }

        val originLatitude = requireNotNull(draft.originLatitude) {
            "Selecione uma origem com localização válida."
        }
        val originLongitude = requireNotNull(draft.originLongitude) {
            "Selecione uma origem com localização válida."
        }
        val destinationLatitude = requireNotNull(draft.destinationLatitude) {
            "Selecione um destino com localização válida."
        }
        val destinationLongitude = requireNotNull(draft.destinationLongitude) {
            "Selecione um destino com localização válida."
        }

        require(draft.problemType == "MECHANICAL" || draft.problemType == "ACCIDENT") {
            "Tipo de problema inválido."
        }

        val parameters = buildJsonObject {
            put("p_vehicle_type", draft.vehicleType.trim())
            put("p_vehicle_brand", draft.vehicleBrand.trim())
            put("p_vehicle_model", draft.vehicleModel.trim())
            put(
                "p_vehicle_year",
                year?.let(::JsonPrimitive) ?: JsonNull
            )
            put("p_vehicle_plate", draft.vehiclePlate.trim().uppercase())
            put("p_origin_address", draft.originAddress.trim())
            put("p_origin_latitude", originLatitude)
            put("p_origin_longitude", originLongitude)
            put("p_destination_address", draft.destinationAddress.trim())
            put("p_destination_latitude", destinationLatitude)
            put("p_destination_longitude", destinationLongitude)
            put("p_problem_type", draft.problemType)
            put("p_problem_detail", draft.problemDetail.trim())
            put("p_problem_description", draft.problemDescription.trim())
        }

        val response = client.postgrest.rpc(
            function = "create_customer_tow_request",
            parameters = parameters
        )

        return Json.parseToJsonElement(response.data)
            .jsonPrimitive
            .content
    }
}