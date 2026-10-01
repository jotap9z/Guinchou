package com.guinchou.app.ui.screens.partner

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.guinchou.app.data.repository.AvailableTowRequest
import com.guinchou.app.ui.theme.GuinchouBackground
import com.guinchou.app.ui.theme.GuinchouGray
import com.guinchou.app.ui.theme.GuinchouGreen
import com.guinchou.app.ui.theme.GuinchouSurface
import com.guinchou.app.ui.theme.GuinchouWhite
import com.guinchou.app.viewmodel.PartnerCallsViewModel
import java.util.Locale

@Suppress("UNUSED_PARAMETER")
@Composable
fun PartnerCallsScreen(
    onBackClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    onCallClick: (String) -> Unit = {},
    onHomeClick: () -> Unit = {},
    onEarningsClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    val callsViewModel: PartnerCallsViewModel = viewModel()

    val state by callsViewModel.uiState.collectAsStateWithLifecycle()

    val context = LocalContext.current

    var permissionMessage by rememberSaveable {
        mutableStateOf<String?>(null)
    }

    var confirmationId by rememberSaveable {
        mutableStateOf<String?>(null)
    }

    val permissions = remember {
        arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        if (result.values.any { it }) {
            permissionMessage = null
            callsViewModel.search()
        } else {
            permissionMessage =
                "Permita a localização para buscar chamados. Você pode habilitá-la nas configurações do aplicativo."
        }
    }

    BackHandler(enabled = state.busy) {
        // Aguarda a operação atual.
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = GuinchouBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                TextButton(
                    onClick = onBackClick,
                    enabled = !state.busy
                ) {
                    Text("Voltar")
                }

                Spacer(Modifier.weight(1f))

                TextButton(
                    onClick = onNotificationsClick,
                    enabled = !state.busy
                ) {
                    Text("Avisos")
                }
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = "Chamados",
                    style = MaterialTheme.typography.headlineMedium,
                    color = GuinchouWhite
                )

                Text(
                    text = "Busca em um raio de até 50 km",
                    color = GuinchouGray
                )

                OutlinedButton(
                    onClick = callsViewModel::reload,
                    enabled = !state.busy,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Atualizar dados")
                }

                if (state.busy) {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth(),
                        color = GuinchouGreen
                    )

                    Text(
                        text = state.operation
                            ?: "Carregando cadastro...",
                        color = GuinchouWhite
                    )
                }

                state.error?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                state.message?.let {
                    Text(
                        text = it,
                        color = GuinchouGreen
                    )
                }

                permissionMessage?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                state.driver?.let { driver ->
                    CallsPanel {
                        Text(
                            text = driver.fullName,
                            color = GuinchouWhite
                        )

                        Text(
                            text = "Cadastro: ${
                                approvalLabel(driver.approvalStatus)
                            }",
                            color = GuinchouGray
                        )

                        Text(
                            text = if (driver.isAvailable) {
                                "Disponível para chamados"
                            } else {
                                "Indisponível"
                            },
                            color = GuinchouWhite
                        )

                        Button(
                            onClick = {
                                callsViewModel.setAvailability(
                                    !driver.isAvailable
                                )
                            },
                            enabled = !state.busy &&
                                    state.approved &&
                                    state.activeCalls.isEmpty(),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                if (driver.isAvailable) {
                                    "Ficar indisponível"
                                } else {
                                    "Ficar disponível"
                                }
                            )
                        }
                    }

                    if (!state.approved) {
                        Text(
                            text = "Seu cadastro precisa estar aprovado para buscar e aceitar chamados.",
                            color = GuinchouWhite
                        )
                    }
                }

                state.activeCalls.forEach { call ->
                    CallsPanel {
                        Text(
                            text = "Chamado em andamento",
                            color = GuinchouGreen
                        )

                        Text(
                            text = "ID: ${call.id}",
                            color = GuinchouWhite
                        )

                        Text(
                            text = "Status: ${statusLabel(call.status)}",
                            color = GuinchouWhite
                        )

                        Text(
                            text = "A aceitação está registrada. A busca fica bloqueada enquanto houver um chamado ativo.",
                            color = GuinchouGray
                        )
                    }
                }

                if (
                    state.approved &&
                    state.activeCalls.isEmpty()
                ) {
                    if (state.trucks.isEmpty()) {
                        Text(
                            text = "Nenhum guincho aprovado e ativo está vinculado a você. Confira o cadastro ou a atribuição feita pela empresa.",
                            color = GuinchouWhite
                        )
                    } else {
                        CallsPanel {
                            Text(
                                text = "Guincho para atender",
                                color = GuinchouWhite
                            )

                            state.trucks.forEach { truck ->
                                Row(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    RadioButton(
                                        selected =
                                            state.selectedTruckId == truck.id,
                                        onClick = {
                                            callsViewModel.selectTruck(
                                                truck.id
                                            )
                                        },
                                        enabled = !state.busy
                                    )

                                    Text(
                                        text = "${truck.plate} • ${truck.brand} ${truck.model}",
                                        modifier = Modifier
                                            .padding(top = 12.dp)
                                            .weight(1f),
                                        color = GuinchouWhite
                                    )
                                }
                            }
                        }

                        Button(
                            onClick = {
                                permissionMessage = null

                                val granted = permissions.any {
                                    ContextCompat.checkSelfPermission(
                                        context,
                                        it
                                    ) == PackageManager.PERMISSION_GRANTED
                                }

                                if (granted) {
                                    callsViewModel.search()
                                } else {
                                    launcher.launch(permissions)
                                }
                            },
                            enabled = state.canSearch,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Buscar chamados com GPS")
                        }

                        if (state.driver?.isAvailable != true) {
                            Text(
                                text = "Ative sua disponibilidade para buscar chamados.",
                                color = GuinchouGray
                            )
                        }
                    }
                }

                if (
                    state.searched &&
                    state.availableCalls.isEmpty()
                ) {
                    Text(
                        text = "Nenhum chamado disponível foi encontrado nesse raio. Tente novamente em alguns instantes.",
                        color = GuinchouWhite
                    )
                }

                state.availableCalls.forEach { call ->
                    CallsPanel {
                        Text(
                            text = vehicleLabel(call),
                            color = GuinchouWhite
                        )

                        Text(
                            text = if (call.problemType == "ACCIDENT") {
                                "Acidente"
                            } else {
                                "Pane mecânica"
                            },
                            color = GuinchouGreen
                        )

                        call.problemDetail
                            ?.takeIf { it.isNotBlank() }
                            ?.let {
                                Text(
                                    text = it,
                                    color = GuinchouGray
                                )
                            }

                        Text(
                            text = "Distância aproximada até a origem: ${
                                km(call.pickupDistanceKm)
                            }",
                            color = GuinchouWhite
                        )

                        call.routeDistanceKm?.let {
                            Text(
                                text = "Percurso informado: ${km(it)}",
                                color = GuinchouGray
                            )
                        }

                        call.estimatedDurationMinutes?.let {
                            Text(
                                text = "Duração estimada: $it min",
                                color = GuinchouGray
                            )
                        }

                        Button(
                            onClick = {
                                confirmationId = call.id
                            },
                            enabled = state.canSearch,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Aceitar chamado")
                        }
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                TextButton(
                    onClick = onHomeClick,
                    enabled = !state.busy
                ) {
                    Text("Início")
                }

                TextButton(
                    onClick = {},
                    enabled = false
                ) {
                    Text("Chamados")
                }

                TextButton(
                    onClick = onEarningsClick,
                    enabled = !state.busy
                ) {
                    Text("Ganhos")
                }

                TextButton(
                    onClick = onProfileClick,
                    enabled = !state.busy
                ) {
                    Text("Perfil")
                }
            }
        }
    }

    val call = state.availableCalls.firstOrNull {
        it.id == confirmationId
    }

    val truck = state.trucks.firstOrNull {
        it.id == state.selectedTruckId
    }

    if (
        call != null &&
        truck != null &&
        !state.busy
    ) {
        AlertDialog(
            onDismissRequest = {
                confirmationId = null
            },
            title = {
                Text("Confirmar aceitação")
            },
            text = {
                Text(
                    "Atender ${vehicleLabel(call)} com o guincho ${truck.plate}?"
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        confirmationId = null
                        callsViewModel.accept(call.id)
                    },
                    enabled = state.canSearch
                ) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        confirmationId = null
                    }
                ) {
                    Text("Voltar")
                }
            }
        )
    }
}

@Composable
private fun CallsPanel(
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = GuinchouSurface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            content = content
        )
    }
}

private fun vehicleLabel(
    call: AvailableTowRequest
): String {
    return listOfNotNull(
        call.vehicleBrand,
        call.vehicleModel,
        call.vehicleModelYear?.toString()
    ).joinToString(" ")
}

private fun km(value: Double): String {
    return String.format(
        Locale.forLanguageTag("pt-BR"),
        "%.1f km",
        value
    )
}

private fun approvalLabel(value: String): String {
    return when (value) {
        "APPROVED" -> "Aprovado"
        "PENDING_REVIEW" -> "Em análise"
        "REJECTED" -> "Rejeitado"
        "SUSPENDED" -> "Suspenso"
        else -> "Ainda não enviado para análise"
    }
}

private fun statusLabel(value: String): String {
    return when (value) {
        "ACCEPTED" -> "Aceito"
        "DRIVER_ON_THE_WAY" -> "A caminho do cliente"
        "ARRIVED" -> "Chegou ao cliente"
        "VEHICLE_LOADED" -> "Veículo carregado"
        "IN_TRANSIT" -> "Em transporte"
        "COMPLETED" -> "Concluído"
        "CANCELLED" -> "Cancelado"
        else -> value
    }
}