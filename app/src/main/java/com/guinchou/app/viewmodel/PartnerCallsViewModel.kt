package com.guinchou.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.guinchou.app.data.location.DriverLocationProvider
import com.guinchou.app.data.repository.AvailableTowRequest
import com.guinchou.app.data.repository.PartnerActiveCall
import com.guinchou.app.data.repository.PartnerCallsRepository
import com.guinchou.app.data.repository.PartnerDriverRecord
import com.guinchou.app.data.repository.PartnerTowTruck
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PartnerCallsUiState(
    val loading: Boolean = true,
    val driver: PartnerDriverRecord? = null,
    val trucks: List<PartnerTowTruck> = emptyList(),
    val activeCalls: List<PartnerActiveCall> = emptyList(),
    val availableCalls: List<AvailableTowRequest> = emptyList(),
    val selectedTruckId: String? = null,
    val searched: Boolean = false,
    val operation: String? = null,
    val error: String? = null,
    val message: String? = null
) {
    val busy: Boolean
        get() = loading || operation != null

    val approved: Boolean
        get() = driver?.approvalStatus == "APPROVED"

    val canSearch: Boolean
        get() = !busy &&
                approved &&
                driver?.isAvailable == true &&
                trucks.isNotEmpty() &&
                activeCalls.isEmpty()
}

class PartnerCallsViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository = PartnerCallsRepository()

    private val _uiState = MutableStateFlow(
        PartnerCallsUiState()
    )

    val uiState = _uiState.asStateFlow()

    private var job: Job? = null

    init {
        reload()
    }

    fun reload() {
        if (job?.isActive == true) return

        _uiState.value = PartnerCallsUiState()

        job = viewModelScope.launch {
            try {
                refreshContext()
            } catch (error: CancellationException) {
                throw error
            } catch (error: Exception) {
                _uiState.update {
                    PartnerCallsUiState(
                        loading = false,
                        error = contextError(error)
                    )
                }
            }
        }
    }

    private suspend fun refreshContext() {
        val context = repository.loadContext()

        _uiState.update { old ->
            old.copy(
                loading = false,
                driver = context.driver,
                trucks = context.trucks,
                activeCalls = context.activeCalls,
                selectedTruckId = old.selectedTruckId
                    ?.takeIf { selected ->
                        context.trucks.any {
                            it.id == selected
                        }
                    }
                    ?: context.trucks.firstOrNull()?.id,
                availableCalls = emptyList(),
                searched = false
            )
        }
    }

    fun selectTruck(id: String) {
        val state = uiState.value

        if (
            state.busy ||
            state.trucks.none { it.id == id }
        ) {
            return
        }

        _uiState.update {
            it.copy(selectedTruckId = id)
        }
    }

    fun setAvailability(available: Boolean) {
        val state = uiState.value

        if (
            state.busy ||
            !state.approved ||
            state.activeCalls.isNotEmpty()
        ) {
            return
        }

        _uiState.update {
            it.copy(
                operation = "Atualizando disponibilidade...",
                error = null,
                message = null
            )
        }

        job = viewModelScope.launch {
            try {
                repository.setAvailability(available)
                refreshContext()

                _uiState.update {
                    it.copy(
                        message = "Disponibilidade atualizada."
                    )
                }
            } catch (error: CancellationException) {
                throw error
            } catch (_: Exception) {
                _uiState.update {
                    it.copy(
                        error = "Não foi possível confirmar a disponibilidade. Atualize os dados."
                    )
                }
            } finally {
                _uiState.update {
                    it.copy(operation = null)
                }
            }
        }
    }

    fun search() {
        if (!uiState.value.canSearch) return

        _uiState.update {
            it.copy(
                operation = "Obtendo localização...",
                error = null,
                message = null,
                availableCalls = emptyList(),
                searched = false
            )
        }

        job = viewModelScope.launch {
            try {
                val location = DriverLocationProvider.currentLocation(
                    getApplication()
                )

                _uiState.update {
                    it.copy(
                        operation = "Buscando chamados próximos..."
                    )
                }

                repository.recordLocation(location)

                val calls = repository.listAvailable()

                _uiState.update {
                    it.copy(
                        availableCalls = calls,
                        searched = true
                    )
                }
            } catch (error: CancellationException) {
                throw error
            } catch (error: Exception) {
                _uiState.update {
                    it.copy(
                        error = if (error is IllegalStateException) {
                            error.message
                                ?: "Não foi possível obter sua localização."
                        } else {
                            "Não foi possível buscar chamados. Confira o GPS, a conexão e seu cadastro."
                        }
                    )
                }
            } finally {
                _uiState.update {
                    it.copy(operation = null)
                }
            }
        }
    }

    fun accept(id: String) {
        val state = uiState.value
        val truckId = state.selectedTruckId ?: return

        if (
            !state.canSearch ||
            state.availableCalls.none { it.id == id } ||
            state.trucks.none { it.id == truckId }
        ) {
            return
        }

        _uiState.update {
            it.copy(
                operation = "Confirmando chamado...",
                error = null,
                message = null
            )
        }

        job = viewModelScope.launch {
            var accepted = false

            try {
                repository.accept(id, truckId)
                accepted = true

                refreshContext()

                _uiState.update {
                    it.copy(
                        message = "Chamado aceito e confirmado no banco."
                    )
                }
            } catch (error: CancellationException) {
                throw error
            } catch (_: Exception) {
                // A conexão pode falhar depois de o banco aceitar.
                // Confira o servidor antes de permitir outra tentativa.
                try {
                    refreshContext()

                    _uiState.update { current ->
                        if (
                            current.activeCalls.any {
                                it.id == id
                            }
                        ) {
                            current.copy(
                                message = "Chamado aceito e confirmado no banco."
                            )
                        } else {
                            current.copy(
                                error = if (accepted) {
                                    "O banco confirmou a aceitação, mas os dados não foram recarregados. Atualize."
                                } else {
                                    "Não foi possível aceitar. O chamado pode ter sido aceito por outro guincheiro."
                                }
                            )
                        }
                    }
                } catch (error: CancellationException) {
                    throw error
                } catch (_: Exception) {
                    _uiState.value = PartnerCallsUiState(
                        loading = false,
                        error = "Não foi possível consultar o resultado. Atualize os dados antes de tentar novamente."
                    )
                }
            } finally {
                _uiState.update {
                    it.copy(operation = null)
                }
            }
        }
    }

    private fun contextError(error: Exception): String {
        return if (error is IllegalStateException) {
            error.message ?: "Confira seu cadastro."
        } else {
            "Não foi possível carregar seu cadastro. Confira a conexão e tente novamente."
        }
    }
}