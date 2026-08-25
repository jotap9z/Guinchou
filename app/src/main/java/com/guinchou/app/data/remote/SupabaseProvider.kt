package com.guinchou.app.data.remote

import com.guinchou.app.config.SupabaseConfig
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.storage.Storage

/**
 * =============================================
 * SUPABASE PROVIDER
 * =============================================
 *
 * Cliente central utilizado pelo Guinchou
 * para comunicação com o Supabase.
 */
object SupabaseProvider {

    val client: SupabaseClient by lazy {

        /*
         * =========================================
         * VALIDAR URL
         * =========================================
         */

        require(
            SupabaseConfig.URL.isNotBlank() &&
                    SupabaseConfig.URL !=
                    "COLE_AQUI_SUA_PROJECT_URL"
        ) {

            "Configure a Project URL em SupabaseConfig.kt."
        }


        /*
         * =========================================
         * VALIDAR PUBLISHABLE KEY
         * =========================================
         */

        require(
            SupabaseConfig
                .PUBLISHABLE_KEY
                .isNotBlank() &&
                    SupabaseConfig
                        .PUBLISHABLE_KEY !=
                    "COLE_AQUI_SUA_PUBLISHABLE_KEY"
        ) {

            "Configure a Publishable Key em SupabaseConfig.kt."
        }


        /*
         * =========================================
         * CRIAR CLIENTE
         * =========================================
         */

        createSupabaseClient(

            supabaseUrl =
                SupabaseConfig.URL,

            supabaseKey =
                SupabaseConfig
                    .PUBLISHABLE_KEY

        ) {

            /*
             * Login / cadastro.
             */
            install(
                Auth
            )


            /*
             * PostgreSQL / Data API.
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