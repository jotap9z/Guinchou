package com.guinchou.app.data.repository

import com.guinchou.app.data.remote.SupabaseProvider
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.Serializable

@Serializable
data class CustomerCallRecord(
    val id: String,
    val status: String
)

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