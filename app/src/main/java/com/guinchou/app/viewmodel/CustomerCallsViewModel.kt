package com.guinchou.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guinchou.app.data.repository.CustomerCallRecord
import com.guinchou.app.data.repository.CustomerCallsRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CustomerCallsUiState(
    val loading: Boolean = true,
    val activeCalls: List<CustomerCallRecord> = emptyList(),
    val historyCalls: List<CustomerCallRecord> = emptyList(),
    val errorMessage: String? = null,
    val cancellingRequestId: String? = null,
    val startingRequestId: String? = null,
    val actionErrorMessage: String? = null,
    val successMessage: String? = null
) {
    val cancelling: Boolean
        get() = cancellingRequestId != null

    val startingSearch: Boolean
        get() = startingRequestId != null

    val busy: Boolean
        get() = cancelling || startingSearch

    val canRequestTow: Boolean
        get() =
            !loading &&
                    !busy &&
                    errorMessage == null &&
                    activeCalls.isEmpty()
}

class CustomerCallsViewModel : ViewModel() {

    private val repository = CustomerCallsRepository()

    private var operationJob: Job? = null

    private val _uiState = MutableStateFlow(
        CustomerCallsUiState()
    )

    val uiState = _uiState.asStateFlow()

    fun load() {
        if (_uiState.value.busy) {
            return
        }

        operationJob?.cancel()

        _uiState.value = _uiState.value.copy(
            loading = true,
            errorMessage = null,
            actionErrorMessage = null,
            successMessage = null
        )

        operationJob = viewModelScope.launch {
            refresh()
        }
    }

    fun startSearch(requestId: String) {
        performAction(
            requestId = requestId,
            startSearch = true
        )
    }

    fun cancel(requestId: String) {
        performAction(
            requestId = requestId,
            startSearch = false
        )
    }

    private fun performAction(
        requestId: String,
        startSearch: Boolean
    ) {
        val currentState = _uiState.value

        if (
            currentState.loading ||
            currentState.busy ||
            currentState.errorMessage != null
        ) {
            return
        }

        val call = currentState.activeCalls.firstOrNull {
            it.id == requestId
        } ?: return

        if (startSearch && !call.canStartSearch) {
            return
        }

        if (!startSearch && !call.canCancel) {
            return
        }

        operationJob?.cancel()

        _uiState.value = currentState.copy(
            startingRequestId = if (startSearch) requestId else null,
            cancellingRequestId = if (startSearch) null else requestId,
            actionErrorMessage = null,
            successMessage = null
        )

        operationJob = viewModelScope.launch {
            var successMessage: String? = null
            var actionErrorMessage: String? = null

            try {
                if (startSearch) {
                    repository.startSearch(requestId)

                    successMessage =
                        "Busca iniciada. Seu chamado está aguardando um guincheiro."
                } else {
                    repository.cancel(requestId)

                    successMessage = "Chamado cancelado com sucesso."
                }
            } catch (error: CancellationException) {
                throw error
            } catch (_: Exception) {
                actionErrorMessage = if (startSearch) {
                    "Não foi possível confirmar o início da busca. " +
                            "Confira a situação atual do chamado antes de tentar novamente."
                } else {
                    "Não foi possível confirmar o cancelamento. " +
                            "Confira a situação atual do chamado antes de tentar novamente."
                }
            }

            // Reconsulta mesmo após uma falha de conexão:
            // o banco pode ter concluído a operação.
            refresh(
                successMessage = successMessage,
                actionErrorMessage = actionErrorMessage
            )
        }
    }

    private suspend fun refresh(
        successMessage: String? = null,
        actionErrorMessage: String? = null
    ) {
        _uiState.value = _uiState.value.copy(
            loading = true,
            errorMessage = null
        )

        try {
            val data = repository.load()

            _uiState.value = CustomerCallsUiState(
                loading = false,
                activeCalls = data.active,
                historyCalls = data.history,
                successMessage = successMessage,
                actionErrorMessage = actionErrorMessage
            )
        } catch (error: CancellationException) {
            throw error
        } catch (_: Exception) {
            _uiState.value = _uiState.value.copy(
                loading = false,
                startingRequestId = null,
                cancellingRequestId = null,
                errorMessage =
                    "Não foi possível atualizar os chamados. " +
                            "Confira sua conexão e toque em Atualizar.",
                successMessage = successMessage,
                actionErrorMessage = actionErrorMessage
            )
        }
    }
}