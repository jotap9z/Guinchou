package com.guinchou.app.ui.screens.calls

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.guinchou.app.data.repository.CustomerCallRecord
import com.guinchou.app.ui.theme.GuinchouBackground
import com.guinchou.app.ui.theme.GuinchouBorder
import com.guinchou.app.ui.theme.GuinchouGray
import com.guinchou.app.ui.theme.GuinchouGreen
import com.guinchou.app.ui.theme.GuinchouSurface
import com.guinchou.app.ui.theme.GuinchouWhite
import com.guinchou.app.viewmodel.CustomerCallsViewModel

@Suppress("UNUSED_PARAMETER")
@Composable
fun CustomerCallsScreen(
    onBackClick: () -> Unit = {},
    onRequestTowClick: () -> Unit = {},
    onTrackCallClick: () -> Unit = {},
    onHomeClick: () -> Unit = {},
    onPaymentsClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    // A assinatura é mantida para compatibilidade com a navegação existente.
    // O acompanhamento simulado não é aberto para os registros reais.

    val callsViewModel: CustomerCallsViewModel = viewModel()

    val state by callsViewModel.uiState.collectAsStateWithLifecycle()

    var showHistory by rememberSaveable {
        mutableStateOf(false)
    }

    var selectedCallId by rememberSaveable {
        mutableStateOf<String?>(null)
    }

    LaunchedEffect(Unit) {
        callsViewModel.load()
    }

    val selectedCall = (
            state.activeCalls + state.historyCalls
            ).firstOrNull {
            it.id == selectedCallId
        }

    if (selectedCall != null) {
        AlertDialog(
            onDismissRequest = {
                selectedCallId = null
            },
            containerColor = GuinchouSurface,
            title = {
                Text(
                    text = "Detalhes do chamado",
                    color = GuinchouWhite
                )
            },
            text = {
                SelectionContainer {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = statusLabel(selectedCall.status),
                            color = GuinchouGreen,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "Identificador\n${selectedCall.id}",
                            color = GuinchouWhite
                        )

                        Text(
                            text = "Status registrado: ${selectedCall.status}",
                            color = GuinchouGray
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        selectedCallId = null
                    }
                ) {
                    Text(
                        text = "Fechar",
                        color = GuinchouGreen
                    )
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GuinchouBackground)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackClick
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Voltar",
                        tint = GuinchouWhite
                    )
                }

                Text(
                    text = "Chamados",
                    color = GuinchouWhite,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )

                TextButton(
                    onClick = {
                        callsViewModel.load()
                    },
                    enabled = !state.loading
                ) {
                    Text(
                        text = "Atualizar",
                        color = GuinchouGreen
                    )
                }
            }

            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Seus chamados",
                    color = GuinchouWhite,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Consulte as solicitações registradas na sua conta.",
                    color = GuinchouGray,
                    fontSize = 13.sp
                )

                CallsTabs(
                    showHistory = showHistory,
                    onSelected = {
                        showHistory = it
                    }
                )

                when {
                    state.loading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color = GuinchouGreen
                            )
                        }
                    }

                    state.errorMessage != null -> {
                        Text(
                            text = state.errorMessage ?: "",
                            color = Color(0xFFFF8A80)
                        )

                        TextButton(
                            onClick = {
                                callsViewModel.load()
                            }
                        ) {
                            Text(
                                text = "Tentar novamente",
                                color = GuinchouGreen
                            )
                        }
                    }

                    else -> {
                        val calls = if (showHistory) {
                            state.historyCalls
                        } else {
                            state.activeCalls
                        }

                        if (calls.isEmpty()) {
                            Text(
                                text = if (showHistory) {
                                    "Você ainda não possui chamados concluídos ou cancelados."
                                } else {
                                    "Nenhum chamado em andamento."
                                },
                                color = GuinchouGray,
                                modifier = Modifier.padding(
                                    vertical = 20.dp
                                )
                            )
                        } else {
                            calls.forEach { call ->
                                CallCard(
                                    call = call,
                                    onClick = {
                                        selectedCallId = call.id
                                    }
                                )
                            }

                            if (
                                state.activeCalls.size >= 100 ||
                                state.historyCalls.count {
                                    it.status == "COMPLETED"
                                } >= 100 ||
                                state.historyCalls.count {
                                    it.status == "CANCELLED"
                                } >= 100
                            ) {
                                Text(
                                    text = "A consulta exibe até 100 registros por situação.",
                                    color = GuinchouGray,
                                    fontSize = 12.sp
                                )
                            }
                        }

                        if (state.activeCalls.isNotEmpty()) {
                            Text(
                                text = "Você já possui um chamado em andamento.",
                                color = GuinchouGray,
                                fontSize = 13.sp
                            )
                        }
                    }
                }

                Button(
                    onClick = {
                        if (state.canRequestTow) {
                            onRequestTowClick()
                        }
                    },
                    enabled = state.canRequestTow,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GuinchouGreen,
                        contentColor = GuinchouBackground
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Build,
                        contentDescription = null
                    )

                    Spacer(Modifier.size(8.dp))

                    Text(
                        text = "Solicitar novo guincho",
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(Modifier.height(8.dp))
            }
        }

        HorizontalDivider(
            color = GuinchouBorder
        )

        CustomerCallsBottomBar(
            onHomeClick = onHomeClick,
            onPaymentsClick = onPaymentsClick,
            onProfileClick = onProfileClick
        )
    }
}

@Composable
private fun CallsTabs(
    showHistory: Boolean,
    onSelected: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                GuinchouSurface,
                RoundedCornerShape(14.dp)
            )
            .border(
                1.dp,
                GuinchouBorder,
                RoundedCornerShape(14.dp)
            )
            .padding(4.dp)
    ) {
        CallsTab(
            text = "Em andamento",
            selected = !showHistory,
            modifier = Modifier.weight(1f),
            onClick = {
                onSelected(false)
            }
        )

        CallsTab(
            text = "Histórico",
            selected = showHistory,
            modifier = Modifier.weight(1f),
            onClick = {
                onSelected(true)
            }
        )
    }
}

@Composable
private fun CallsTab(
    text: String,
    selected: Boolean,
    modifier: Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .background(
                if (selected) {
                    GuinchouGreen
                } else {
                    Color.Transparent
                },
                RoundedCornerShape(10.dp)
            )
            .clickable(onClick = onClick)
            .padding(vertical = 11.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (selected) {
                GuinchouBackground
            } else {
                GuinchouGray
            },
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun CallCard(
    call: CustomerCallRecord,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                GuinchouSurface,
                RoundedCornerShape(16.dp)
            )
            .border(
                1.dp,
                GuinchouBorder,
                RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = statusLabel(call.status),
            color = if (call.status == "CANCELLED") {
                GuinchouGray
            } else {
                GuinchouGreen
            },
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Chamado ${call.id.take(8)}",
            color = GuinchouWhite,
            fontSize = 16.sp
        )

        Text(
            text = "Toque para consultar o identificador completo.",
            color = GuinchouGray,
            fontSize = 12.sp
        )
    }
}

private fun statusLabel(
    status: String
): String = when (status) {
    "CREATED" -> "Solicitação registrada"
    "SEARCHING" -> "Buscando guincheiro"
    "ACCEPTED" -> "Chamado aceito"
    "DRIVER_ON_THE_WAY" -> "Guincheiro a caminho"
    "ARRIVED" -> "Guincheiro no local"
    "VEHICLE_LOADED" -> "Veículo carregado"
    "IN_TRANSIT" -> "Transporte em andamento"
    "COMPLETED" -> "Atendimento concluído"
    "CANCELLED" -> "Chamado cancelado"
    else -> "Status: $status"
}

@Composable
private fun CustomerCallsBottomBar(
    onHomeClick: () -> Unit,
    onPaymentsClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(GuinchouBackground)
            .navigationBarsPadding()
            .padding(
                horizontal = 8.dp,
                vertical = 8.dp
            ),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BottomItem(
            icon = Icons.Default.Home,
            text = "Início",
            onClick = onHomeClick
        )

        BottomItem(
            icon = Icons.Default.Build,
            text = "Chamados",
            selected = true
        )

        BottomItem(
            icon = Icons.Default.CreditCard,
            text = "Pagamentos",
            onClick = onPaymentsClick
        )

        BottomItem(
            icon = Icons.Default.Person,
            text = "Perfil",
            onClick = onProfileClick
        )
    }
}

@Composable
private fun BottomItem(
    icon: ImageVector,
    text: String,
    selected: Boolean = false,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(
                horizontal = 10.dp,
                vertical = 5.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = if (selected) {
                GuinchouGreen
            } else {
                GuinchouGray
            },
            modifier = Modifier.size(22.dp)
        )

        Spacer(Modifier.height(4.dp))

        Text(
            text = text,
            color = if (selected) {
                GuinchouGreen
            } else {
                GuinchouGray
            },
            fontSize = 11.sp
        )

        Spacer(Modifier.height(3.dp))

        Box(
            modifier = Modifier
                .size(
                    width = 18.dp,
                    height = 2.dp
                )
                .background(
                    if (selected) {
                        GuinchouGreen
                    } else {
                        Color.Transparent
                    },
                    RoundedCornerShape(50)
                )
        )
    }
}