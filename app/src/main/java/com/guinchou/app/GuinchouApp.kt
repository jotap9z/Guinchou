package com.guinchou.app

import android.app.Application

/**
 * Application principal do Guinchou.
 *
 * Neste momento não inicializamos
 * serviços de rede aqui.
 *
 * O Supabase será criado somente quando
 * algum Repository realmente precisar dele.
 */
class GuinchouApp : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}