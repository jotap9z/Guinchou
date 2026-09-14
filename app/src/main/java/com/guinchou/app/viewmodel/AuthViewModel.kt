package com.guinchou.app.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Controla o estado de autenticação
 * do usuário no Guinchou.
 */
class AuthViewModel : ViewModel() {

    private val _uiState =
        MutableStateFlow(
            AuthUiState()
        )

    val uiState: StateFlow<AuthUiState> =
        _uiState.asStateFlow()

    /**
     * Realiza a autenticação via Supabase.
     */
    fun signIn(
        email: String,
        password: String,
    ) {

        /*
         * Sinaliza que está carregando.
         */
        _uiState.value =
            _uiState.value.copy(
                isLoading = true,
                errorMessage = null
            )

        /*
         * Simulação temporária até
         * a integração total com o Supabase.
         */
        if (
            email == "admin@guinchou.com" &&
            password == "123456"
        ) {

            _uiState.value =
                _uiState.value.copy(
                    isLoading = false,
                    isAuthenticated = true
                )

        } else {

            _uiState.value =
                _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "E-mail ou senha incorretos."
                )
        }
    }
}

/**
 * Representa o estado visual
 * do fluxo de login.
 */
data class AuthUiState(
    val isLoading: Boolean = false,
    val isAuthenticated: Boolean = false,
    val errorMessage: String? = null,
)