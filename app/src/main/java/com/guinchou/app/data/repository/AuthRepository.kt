package com.guinchou.app.data.repository

import com.guinchou.app.data.remote.SupabaseProvider
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthenticatedProfile(
    val id: String,
    val role: String,
    val status: String,
    @SerialName("full_name")
    val fullName: String? = null,
    val phone: String? = null
)

class AuthRepository {

    private val client = SupabaseProvider.client

    suspend fun signIn(email: String, password: String) {
        client.auth.signInWith(Email) {
            this.email = email
            this.password = password
        }
    }

    suspend fun loadProfile(): AuthenticatedProfile {
        val user = client.auth.currentUserOrNull()
            ?: throw IllegalStateException("Faça login novamente.")

        val profile = client.from("profiles")
            .select(
                columns = Columns.list(
                    "id, role, status, full_name, phone"
                )
            ) {
                filter {
                    eq("id", user.id)
                }
                limit(1)
            }
            .decodeList<AuthenticatedProfile>()
            .firstOrNull()
            ?: throw IllegalStateException(
                "Perfil não encontrado para esta conta."
            )

        check(profile.status == "ACTIVE") {
            "Sua conta está inativa, suspensa ou bloqueada. " +
                    "Entre em contato com o suporte."
        }

        when (profile.role) {

            "CUSTOMER",
            "PARTNER_DRIVER",
            "PARTNER_COMPANY" -> Unit

            "ADMIN" -> throw IllegalStateException(
                "A área administrativa ainda não está integrada neste aplicativo."
            )

            else -> throw IllegalStateException(
                "Perfil de acesso não reconhecido."
            )
        }

        check(client.auth.currentUserOrNull()?.id == user.id) {
            "A sessão foi alterada. Faça login novamente."
        }

        return profile
    }

    fun currentEmail(): String {
        return client.auth.currentUserOrNull()?.email.orEmpty()
    }

    suspend fun signOut() = client.auth.signOut()
}