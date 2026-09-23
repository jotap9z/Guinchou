package com.guinchou.app.data.repository

import com.guinchou.app.data.remote.SupabaseProvider
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Count
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class CustomerHome(
    val name: String,
    val email: String,
    val activeRequestId: String?,
    val activeRequestStatus: String?,
    val completedServices: Int
)

@Serializable
private data class ProfileRow(
    val role: String,
    val status: String,
    @SerialName("full_name")
    val fullName: String? = null
)

@Serializable
private data class CustomerRow(
    val id: String
)

@Serializable
private data class RequestRow(
    val id: String,
    val status: String
)

class CustomerHomeRepository {
    private val client = SupabaseProvider.client

    suspend fun load(): CustomerHome {
        val user = client.auth.currentUserOrNull()
            ?: throw IllegalStateException(
                "Faça login para acessar a Home."
            )

        val userId = user.id

        val profile = client.from("profiles")
            .select(
                columns = Columns.list("role, status, full_name")
            ) {
                filter {
                    eq("id", userId)
                }
            }
            .decodeList<ProfileRow>()
            .singleOrNull()
            ?: throw IllegalStateException(
                "Perfil do usuário não encontrado ou sem acesso."
            )

        if (
            profile.role != "CUSTOMER" ||
            profile.status != "ACTIVE"
        ) {
            throw IllegalStateException(
                "Esta área requer uma conta de cliente ativa."
            )
        }

        val customer = client.from("customers")
            .select(
                columns = Columns.list("id")
            ) {
                filter {
                    eq("id", userId)
                }
            }
            .decodeList<CustomerRow>()
            .singleOrNull()
            ?: throw IllegalStateException(
                "Cadastro de cliente não encontrado ou sem acesso."
            )

        val activeRequest = client.from("tow_requests")
            .select(
                columns = Columns.list("id, status")
            ) {
                filter {
                    eq("customer_id", customer.id)
                    neq("status", "COMPLETED")
                    neq("status", "CANCELLED")
                }

                limit(1)
            }
            .decodeList<RequestRow>()
            .firstOrNull()

        val completedCount = client.from("tow_requests")
            .select(
                columns = Columns.list("id")
            ) {
                head = true

                filter {
                    eq("customer_id", customer.id)
                    eq("status", "COMPLETED")
                }

                count(Count.EXACT)
            }
            .countOrNull() ?: 0

        return CustomerHome(
            name = profile.fullName
                ?.takeIf { it.isNotBlank() }
                ?: "Cliente",
            email = user.email ?: "",
            activeRequestId = activeRequest?.id,
            activeRequestStatus = activeRequest?.status,
            completedServices = completedCount.toInt()
        )
    }
}