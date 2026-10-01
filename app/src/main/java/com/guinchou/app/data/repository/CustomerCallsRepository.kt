package com.guinchou.app.data.repository

import com.guinchou.app.data.remote.SupabaseProvider
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put

@Serializable
data class CustomerCallRecord(
    val id: String,
    val status: String
) {
    val canCancel: Boolean
        get() = status == "CREATED" || status == "SEARCHING"
}

data class CustomerCallsData(
    val active: List<CustomerCallRecord>,
    val history: List<CustomerCallRecord>
)

class CustomerCallsRepository {

    private val client = SupabaseProvider.client

    suspend fun load(): CustomerCallsData {
        val user = client.auth.currentUserOrNull()
            ?: throw IllegalStateException(
                "Faça login para consultar seus chamados."
            )

        return coroutineScope {
            val active = async {
                loadByStatus(user.id, null)
            }

            val completed = async {
                loadByStatus(user.id, "COMPLETED")
            }

            val cancelled = async {
                loadByStatus(user.id, "CANCELLED")
            }

            CustomerCallsData(
                active = active.await().sortedBy { it.id },
                history = (
                        completed.await() + cancelled.await()
                        ).sortedBy { it.id }
            )
        }
    }

    suspend fun cancel(requestId: String) {
        check(client.auth.currentUserOrNull() != null) {
            "Faça login para cancelar o chamado."
        }

        require(requestId.isNotBlank()) {
            "Identificador do chamado inválido."
        }

        val parameters = buildJsonObject {
            put("p_request_id", requestId)
        }

        val response = client.postgrest.rpc(
            function = "customer_cancel_tow_request",
            parameters = parameters
        )

        val status = Json.parseToJsonElement(response.data)
            .jsonPrimitive
            .content

        check(status == "CANCELLED") {
            "O servidor não confirmou o cancelamento."
        }
    }

    private suspend fun loadByStatus(
        customerId: String,
        status: String?
    ): List<CustomerCallRecord> {
        return client.from("tow_requests")
            .select(
                columns = Columns.list("id, status")
            ) {
                filter {
                    eq("customer_id", customerId)

                    if (status == null) {
                        neq("status", "COMPLETED")
                        neq("status", "CANCELLED")
                    } else {
                        eq("status", status)
                    }
                }

                limit(100)
            }
            .decodeList<CustomerCallRecord>()
    }
}