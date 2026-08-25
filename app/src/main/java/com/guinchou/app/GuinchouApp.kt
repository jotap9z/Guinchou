package com.guinchou.app

import android.app.Application
import com.guinchou.app.data.remote.SupabaseProvider

/**
 * Application principal do Guinchou.
 */
class GuinchouApp : Application() {

    override fun onCreate() {

        super.onCreate()

        /*
         * Inicializa o cliente Supabase
         * quando o aplicativo é aberto.
         */
        SupabaseProvider.client
    }
}