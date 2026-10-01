package com.guinchou.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CustomerTowRequestSubmissionUiState(
    val submitting: Boolean = false,
    val createdRequestId: String? = null,
    val errorMessage: String? = null
)

class CustomerTowRequestSubmissionViewModel(
    application: Application,
    private val savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(
        CustomerTowRequestSubmissionUiState(
            createdRequestId = savedStateHandle[REQUEST_ID_KEY]
        )
    )

    val uiState = _uiState.asStateFlow()

    fun submit(towRequestViewModel: TowRequestViewModel) {
        if (_uiState.value.submitting || _uiState.value.createdRequestId != null) {
            return
        }

        if (towRequestViewModel.problemType == "ACCIDENT") {
            _uiState.value = CustomerTowRequestSubmissionUiState(
                errorMessage = "O envio das fotos do acidente ainda precisa ser integrado."
            )
            return
        }

        _uiState.value = CustomerTowRequestSubmissionUiState(
            submitting = true
        )

        viewModelScope.launch {
            try {
                val requestId = towRequestViewModel.createCustomerTowRequest(
                    getApplication<Application>()
                )

                savedStateHandle[REQUEST_ID_KEY] = requestId

                _uiState.value = CustomerTowRequestSubmissionUiState(
                    createdRequestId = requestId
                )
            } catch (error: CancellationException) {
                throw error
            } catch (error: IllegalArgumentException) {
                _uiState.value = CustomerTowRequestSubmissionUiState(
                    errorMessage = error.message
                        ?: "Confira os dados da solicitação."
                )
            } catch (error: IllegalStateException) {
                _uiState.value = CustomerTowRequestSubmissionUiState(
                    errorMessage = error.message
                        ?: "Não foi possível registrar o chamado."
                )
            } catch (_: Exception) {
                _uiState.value = CustomerTowRequestSubmissionUiState(
                    errorMessage = "Não foi possível confirmar o registro. Confira sua conexão e consulte a Home antes de tentar novamente."
                )
            }
        }
    }

    private companion object {
        const val REQUEST_ID_KEY = "customer_tow_request_id"
    }
}