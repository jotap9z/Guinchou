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
    val errorMessage: String? = null
) {
    val canRequestTow: Boolean
        get() =
            !loading &&
                    errorMessage == null &&
                    activeCalls.isEmpty()
}

class CustomerCallsViewModel : ViewModel() {
    private val repository = CustomerCallsRepository()

    private var loadJob: Job? = null

    private val _uiState = MutableStateFlow(
        CustomerCallsUiState()
    )

    val uiState = _uiState.asStateFlow()

    fun load() {
        loadJob?.cancel()

        _uiState.value = CustomerCallsUiState()

        loadJob = viewModelScope.launch {
            try {
                val data = repository.load()

                _uiState.value = CustomerCallsUiState(
                    loading = false,
                    activeCalls = data.active,
                    historyCalls = data.history
                )
            } catch (error: CancellationException) {
                throw error
            } catch (error: IllegalStateException) {
                _uiState.value = CustomerCallsUiState(
                    loading = false,
                    errorMessage = error.message
                        ?: "Não foi possível consultar seus chamados."
                )
            } catch (_: Exception) {
                _uiState.value = CustomerCallsUiState(
                    loading = false,
                    errorMessage = "Não foi possível carregar os chamados. Confira sua conexão e tente novamente."
                )
            }
        }
    }
}