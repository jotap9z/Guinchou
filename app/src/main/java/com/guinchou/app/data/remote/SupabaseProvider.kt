package com.guinchou.app.data.remote

import com.guinchou.app.config.SupabaseConfig
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.storage.Storage

/**
 * Cliente central do Supabase utilizado pelo Guinchou.
 */
object SupabaseProvider {

    val client: SupabaseClient by lazy {

        /*
         * Remove espaços acidentais.
         */
        val configuredUrl =
            SupabaseConfig.URL.trim()

        /*
         * Proteção contra URL vazia.
         */
        require(
            configuredUrl.isNotBlank()
        ) {
            "A URL do Supabase não foi configurada."
        }

        /*
         * A Publishable Key também precisa existir.
         */
        require(
            SupabaseConfig.PUBLISHABLE_KEY
                .trim()
                .isNotBlank()
        ) {
            "A Publishable Key do Supabase não foi configurada."
        }

        /*
         * =========================================
         * NORMALIZAÇÃO DA URL
         * =========================================
         *
         * O supabase-kt precisa somente da URL base.
         *
         * Exemplo:
         *
         * https://abc.supabase.co
         *
         * Se /rest/v1 tiver sido colocado por engano,
         * removemos.
         */

        val normalizedUrl =
            configuredUrl
                .removeSuffix("/")
                .removeSuffix("/rest/v1")
                .removeSuffix("/rest/v1/")
                .removeSuffix("/auth/v1")
                .removeSuffix("/auth/v1/")
                .removeSuffix("/storage/v1")
                .removeSuffix("/storage/v1/")
                .removeSuffix("/realtime/v1")
                .removeSuffix("/realtime/v1/")


        /*
         * Ainda fazemos uma validação básica.
         */
        require(
            normalizedUrl.startsWith(
                "https://"
            )
        ) {
            "A URL do Supabase precisa começar com https://"
        }


        createSupabaseClient(

            supabaseUrl =
                normalizedUrl,

            supabaseKey =
                SupabaseConfig
                    .PUBLISHABLE_KEY
                    .trim()

        ) {

            /*
             * Cadastro e login.
             */
            install(
                Auth
            )


            /*
             * Banco PostgreSQL.
             */
            install(
                Postgrest
            )


            /*
             * Atualizações em tempo real.
             */
            install(
                Realtime
            )


            /*
             * Fotos e documentos.
             */
            install(
                Storage
            )
        }
    }
}