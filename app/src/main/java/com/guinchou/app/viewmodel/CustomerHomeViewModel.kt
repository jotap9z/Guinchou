package com.guinchou.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guinchou.app.data.repository.CustomerHome
import com.guinchou.app.data.repository.CustomerHomeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CustomerHomeUiState(
    val loading: Boolean = true,
    val home: CustomerHome? = null,
    val error: String? = null,
) {
    val canRequestTow: Boolean
        get() = !loading && error == null && home != null && home.activeRequestId == null
}

class CustomerHomeViewModel : ViewModel() {
    private val repository = CustomerHomeRepository()
    private val _uiState = MutableStateFlow(CustomerHomeUiState())
    val uiState = _uiState.asStateFlow()

    fun load() {
        _uiState.value = CustomerHomeUiState()
        viewModelScope.launch {
            _uiState.value = try {
                CustomerHomeUiState(loading = false, home = repository.load())
            } catch (error: Exception) {
                CustomerHomeUiState(
                    loading = false,
                    error = error.message ?: "Falha ao carregar os dados do cliente.",
                )
            }
        }
    }

    fun clear() {
        _uiState.value = CustomerHomeUiState()
    }
}
