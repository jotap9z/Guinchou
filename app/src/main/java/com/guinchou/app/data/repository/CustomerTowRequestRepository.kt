package com.guinchou.app.data.repository

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import com.guinchou.app.data.remote.SupabaseProvider
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.postgrest
import java.io.IOException
import java.util.Locale
import kotlin.coroutines.resume
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
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

class CustomerTowRequestRepository(
    private val context: Context
) {
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

        require(draft.problemType == "MECHANICAL" || draft.problemType == "ACCIDENT") {
            "Tipo de problema inválido."
        }

        val origin = resolveCoordinates(
            address = draft.originAddress,
            latitude = draft.originLatitude,
            longitude = draft.originLongitude,
            locationName = "origem"
        )

        val destination = resolveCoordinates(
            address = draft.destinationAddress,
            latitude = draft.destinationLatitude,
            longitude = draft.destinationLongitude,
            locationName = "destino"
        )

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
            put("p_origin_latitude", origin.first)
            put("p_origin_longitude", origin.second)
            put("p_destination_address", draft.destinationAddress.trim())
            put("p_destination_latitude", destination.first)
            put("p_destination_longitude", destination.second)
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

    private suspend fun resolveCoordinates(
        address: String,
        latitude: Double?,
        longitude: Double?,
        locationName: String
    ): Pair<Double, Double> {
        if (latitude != null && longitude != null) {
            require(
                latitude.isFinite() &&
                        longitude.isFinite() &&
                        latitude in -90.0..90.0 &&
                        longitude in -180.0..180.0
            ) {
                "As coordenadas de $locationName são inválidas."
            }

            return latitude to longitude
        }

        require(address.isNotBlank()) {
            "Informe o endereço de $locationName."
        }

        check(Geocoder.isPresent()) {
            "Não foi possível localizar o endereço de $locationName neste aparelho."
        }

        val geocoder = Geocoder(
            context.applicationContext,
            Locale("pt", "BR")
        )

        val result: Address? = try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                withTimeoutOrNull(10_000L) {
                    suspendCancellableCoroutine { continuation ->
                        geocoder.getFromLocationName(
                            address.trim(),
                            1,
                            object : Geocoder.GeocodeListener {
                                override fun onGeocode(addresses: MutableList<Address>) {
                                    if (continuation.isActive) {
                                        continuation.resume(addresses.firstOrNull())
                                    }
                                }

                                override fun onError(errorMessage: String?) {
                                    if (continuation.isActive) {
                                        continuation.resume(null)
                                    }
                                }
                            }
                        )
                    }
                }
            } else {
                withContext(Dispatchers.IO) {
                    @Suppress("DEPRECATION")
                    geocoder.getFromLocationName(address.trim(), 1)
                        ?.firstOrNull()
                }
            }
        } catch (_: IOException) {
            null
        }

        requireNotNull(result) {
            "Não foi possível localizar o endereço de $locationName. Confira o endereço e tente novamente."
        }

        require(result.hasLatitude() && result.hasLongitude()) {
            "O endereço de $locationName não possui coordenadas válidas."
        }

        return result.latitude to result.longitude
    }
}