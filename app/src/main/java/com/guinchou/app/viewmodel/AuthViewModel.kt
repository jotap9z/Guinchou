package com.guinchou.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guinchou.app.data.remote.SupabaseProvider
import com.guinchou.app.data.repository.AuthenticatedProfile
import com.guinchou.app.data.repository.AuthRepository
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AuthUiState(
    val isLoading: Boolean = false,
    val isAuthenticated: Boolean = false,
    val errorMessage: String? = null,
    val profile: AuthenticatedProfile? = null,
    val email: String = ""
)

class AuthViewModel : ViewModel() {

    private val repository = AuthRepository()
    private var profileJob: Job? = null

    private val _uiState = MutableStateFlow(
        AuthUiState(isLoading = true)
    )

    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            SupabaseProvider.client.auth.sessionStatus.collect { status ->
                when (status) {
                    is SessionStatus.Authenticated -> {
                        resolveProfile()
                    }

                    is SessionStatus.NotAuthenticated -> {
                        profileJob?.cancel()

                        _uiState.value = AuthUiState(
                            errorMessage = _uiState.value.errorMessage
                        )
                    }

                    is SessionStatus.RefreshFailure -> {
                        profileJob?.cancel()

                        _uiState.value = AuthUiState(
                            errorMessage =
                                "Sessão expirada. Faça login novamente."
                        )
                    }

                    SessionStatus.Initializing -> Unit
                }
            }
        }
    }

    private fun resolveProfile() {
        profileJob?.cancel()

        _uiState.value = AuthUiState(isLoading = true)

        profileJob = viewModelScope.launch {
            try {
                val profile = repository.loadProfile()

                _uiState.value = AuthUiState(
                    isAuthenticated = true,
                    profile = profile,
                    email = repository.currentEmail()
                )
            } catch (error: CancellationException) {
                throw error
            } catch (error: IllegalStateException) {
                _uiState.value = AuthUiState(
                    errorMessage = error.message
                        ?: "Não foi possível validar seu perfil."
                )
            } catch (_: Exception) {
                _uiState.value = AuthUiState(
                    errorMessage =
                        "Não foi possível consultar seu perfil. " +
                                "Confira a conexão e tente entrar novamente."
                )
            }
        }
    }

    fun signIn(email: String, password: String) {
        if (_uiState.value.isLoading) {
            return
        }

        if (email.isBlank() || password.isBlank()) {
            _uiState.value = AuthUiState(
                errorMessage = "Informe e-mail e senha."
            )
            return
        }

        profileJob?.cancel()

        _uiState.value = AuthUiState(isLoading = true)

        viewModelScope.launch {
            try {
                repository.signIn(email.trim(), password)
                resolveProfile()
            } catch (error: CancellationException) {
                throw error
            } catch (_: Exception) {
                profileJob?.cancel()

                _uiState.value = AuthUiState(
                    errorMessage =
                        "Não foi possível entrar. Confira e-mail, " +
                                "senha e confirmação da conta."
                )
            }
        }
    }

    fun signOut(onFinished: () -> Unit = {}) {
        profileJob?.cancel()

        _uiState.value = _uiState.value.copy(
            isLoading = true,
            errorMessage = null
        )

        viewModelScope.launch {
            try {
                repository.signOut()

                _uiState.value = AuthUiState()

                onFinished()
            } catch (error: CancellationException) {
                throw error
            } catch (_: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage =
                        "Não foi possível encerrar a sessão. Tente novamente."
                )
            }
        }
    }
}