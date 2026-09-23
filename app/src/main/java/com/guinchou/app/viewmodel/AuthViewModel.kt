package com.guinchou.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guinchou.app.data.remote.SupabaseProvider
import com.guinchou.app.data.repository.AuthRepository
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val repository = AuthRepository()
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            SupabaseProvider.client.auth.sessionStatus.collect { status ->
                when (status) {
                    is SessionStatus.Authenticated -> _uiState.value =
                        _uiState.value.copy(isAuthenticated = true, isLoading = false)
                    is SessionStatus.NotAuthenticated -> _uiState.value =
                        _uiState.value.copy(isAuthenticated = false, isLoading = false)
                    is SessionStatus.RefreshFailure -> _uiState.value =
                        _uiState.value.copy(isAuthenticated = false, isLoading = false,
                            errorMessage = "Sessão expirada. Faça login novamente.")
                    SessionStatus.Initializing -> Unit
                }
            }
        }
    }

    fun signIn(email: String, password: String) {
        if (_uiState.value.isLoading) return
        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            try {
                repository.signIn(email, password)
                _uiState.value = _uiState.value.copy(isLoading = false, isAuthenticated = true)
            } catch (error: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false,
                    isAuthenticated = false,
                    errorMessage = "Não foi possível entrar. Confira e-mail, senha e confirmação da conta.")
            }
        }
    }

    fun signOut(onFinished: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                repository.signOut()
                _uiState.value = AuthUiState()
                onFinished()
            } catch (error: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Não foi possível encerrar a sessão. Tente novamente.")
            }
        }
    }
}

data class AuthUiState(
    val isLoading: Boolean = false,
    val isAuthenticated: Boolean = false,
    val errorMessage: String? = null,
)
