package com.guinchou.app.data.repository

import android.location.Location
import com.guinchou.app.data.remote.SupabaseProvider
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.long
import kotlinx.serialization.json.put

@Serializable
data class PartnerDriverRecord(
    val id: String,

    @SerialName("company_id")
    val companyId: String? = null,

    @SerialName("full_name")
    val fullName: String,

    @SerialName("approval_status")
    val approvalStatus: String,

    @SerialName("is_available")
    val isAvailable: Boolean
)

@Serializable
data class PartnerTowTruck(
    val id: String,
    val plate: String,
    val brand: String,
    val model: String,

    @SerialName("company_id")
    val companyId: String? = null,

    @SerialName("independent_driver_id")
    val independentDriverId: String? = null
)

@Serializable
private data class PartnerAssignment(
    @SerialName("tow_truck_id")
    val towTruckId: String,

    @SerialName("unassigned_at")
    val unassignedAt: String? = null
)

@Serializable
data class PartnerActiveCall(
    val id: String,
    val status: String,

    @SerialName("accepted_tow_truck_id")
    val towTruckId: String? = null
)

@Serializable
data class AvailableTowRequest(
    @SerialName("request_id")
    val id: String,

    @SerialName("searching_at")
    val searchingAt: String,

    @SerialName("pickup_distance_km")
    val pickupDistanceKm: Double,

    @SerialName("route_distance_km")
    val routeDistanceKm: Double? = null,

    @SerialName("estimated_duration_minutes")
    val estimatedDurationMinutes: Int? = null,

    @SerialName("problem_type")
    val problemType: String,

    @SerialName("problem_detail")
    val problemDetail: String? = null,

    @SerialName("vehicle_type")
    val vehicleType: String,

    @SerialName("vehicle_brand")
    val vehicleBrand: String,

    @SerialName("vehicle_model")
    val vehicleModel: String,

    @SerialName("vehicle_model_year")
    val vehicleModelYear: Int? = null
)

data class PartnerCallsContext(
    val driver: PartnerDriverRecord,
    val trucks: List<PartnerTowTruck>,
    val activeCalls: List<PartnerActiveCall>
)

class PartnerCallsRepository {

    private val client = SupabaseProvider.client

    suspend fun loadContext(): PartnerCallsContext {
        val userId = checkNotNull(
            client.auth.currentUserOrNull()?.id
        ) {
            "Entre novamente na sua conta."
        }

        val profiles = client.from("profiles")
            .select(
                Columns.list("id,role,status,full_name,phone")
            ) {
                filter {
                    eq("id", userId)
                    eq("role", "PARTNER_DRIVER")
                    eq("status", "ACTIVE")
                }
            }
            .decodeList<AuthenticatedProfile>()

        check(profiles.size == 1) {
            "Esta conta não possui um perfil de guincheiro ativo."
        }

        val drivers = client.from("drivers")
            .select(
                Columns.list(
                    "id,company_id,full_name,approval_status,is_available"
                )
            ) {
                filter {
                    eq("user_id", userId)
                }
                limit(2)
            }
            .decodeList<PartnerDriverRecord>()

        check(drivers.size == 1) {
            "O cadastro de motorista precisa ser conferido pela empresa."
        }

        val driver = drivers.single()

        val active = client.from("tow_requests")
            .select(
                Columns.list("id,status,accepted_tow_truck_id")
            ) {
                filter {
                    eq("accepted_driver_id", driver.id)
                    neq("status", "COMPLETED")
                    neq("status", "CANCELLED")
                }
            }
            .decodeList<PartnerActiveCall>()

        if (driver.approvalStatus != "APPROVED") {
            return PartnerCallsContext(
                driver = driver,
                trucks = emptyList(),
                activeCalls = active
            )
        }

        val companyId = driver.companyId

        val trucks = if (companyId == null) {
            client.from("tow_trucks")
                .select(
                    Columns.list(
                        "id,plate,brand,model,company_id,independent_driver_id"
                    )
                ) {
                    filter {
                        eq("independent_driver_id", driver.id)
                        eq("approval_status", "APPROVED")
                        eq("is_active", true)
                    }
                }
                .decodeList<PartnerTowTruck>()
                .filter {
                    it.companyId == null
                }
        } else {
            val assignedIds = client.from("driver_vehicle_assignments")
                .select(
                    Columns.list("tow_truck_id,unassigned_at")
                ) {
                    filter {
                        eq("driver_id", driver.id)
                        eq("is_active", true)
                    }
                }
                .decodeList<PartnerAssignment>()
                .filter {
                    it.unassignedAt == null
                }
                .map {
                    it.towTruckId
                }
                .toSet()

            client.from("tow_trucks")
                .select(
                    Columns.list(
                        "id,plate,brand,model,company_id,independent_driver_id"
                    )
                ) {
                    filter {
                        eq("company_id", companyId)
                        eq("approval_status", "APPROVED")
                        eq("is_active", true)
                    }
                }
                .decodeList<PartnerTowTruck>()
                .filter {
                    it.id in assignedIds &&
                            it.independentDriverId == null
                }
        }

        check(client.auth.currentUserOrNull()?.id == userId) {
            "A sessão mudou. Entre novamente."
        }

        return PartnerCallsContext(
            driver = driver,
            trucks = trucks,
            activeCalls = active
        )
    }

    suspend fun setAvailability(available: Boolean) {
        val result = client.postgrest.rpc(
            function = "driver_set_availability",
            parameters = buildJsonObject {
                put("p_is_available", available)
            }
        )

        check(
            Json.parseToJsonElement(result.data)
                .jsonPrimitive.boolean == available
        ) {
            "Não foi possível confirmar sua disponibilidade."
        }
    }

    suspend fun recordLocation(location: Location) {
        val accuracy = if (
            location.hasAccuracy() &&
            location.accuracy.isFinite()
        ) {
            JsonPrimitive(
                location.accuracy
                    .coerceAtLeast(0f)
                    .toDouble()
            )
        } else {
            JsonNull
        }

        val result = client.postgrest.rpc(
            function = "driver_record_location",
            parameters = buildJsonObject {
                put("p_latitude", location.latitude)
                put("p_longitude", location.longitude)
                put("p_accuracy_m", accuracy)
                put("p_request_id", JsonNull)
            }
        )

        check(
            Json.parseToJsonElement(result.data)
                .jsonPrimitive.long > 0L
        ) {
            "Não foi possível confirmar sua localização."
        }
    }

    suspend fun listAvailable(): List<AvailableTowRequest> {
        return client.postgrest.rpc(
            function = "list_available_tow_requests",
            parameters = buildJsonObject {
                put("p_radius_km", 50)
                put("p_limit", 20)
            }
        ).decodeList<AvailableTowRequest>()
    }

    suspend fun accept(
        requestId: String,
        truckId: String
    ) {
        val result = client.postgrest.rpc(
            function = "driver_accept_tow_request",
            parameters = buildJsonObject {
                put("p_request_id", requestId)
                put("p_tow_truck_id", truckId)
            }
        )

        check(
            Json.parseToJsonElement(result.data)
                .jsonPrimitive.content == "ACCEPTED"
        ) {
            "Não foi possível confirmar a aceitação do chamado."
        }
    }
}