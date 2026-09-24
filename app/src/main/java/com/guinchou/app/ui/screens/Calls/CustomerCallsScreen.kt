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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.guinchou.app.ui.theme.GuinchouBackground
import com.guinchou.app.ui.theme.GuinchouBorder
import com.guinchou.app.ui.theme.GuinchouGray
import com.guinchou.app.ui.theme.GuinchouGreen
import com.guinchou.app.ui.theme.GuinchouSurface
import com.guinchou.app.ui.theme.GuinchouWhite

private enum class CustomerCallsTab {
    ACTIVE,
    HISTORY
}

private data class CustomerCallItem(
    val id: Int,
    val date: String,
    val time: String,
    val pickup: String,
    val destination: String,
    val vehicle: String,
    val problem: String,
    val driver: String,
    val towTruck: String,
    val towTruckPlate: String,
    val payment: String,
    val value: String,
    val status: String,
    val active: Boolean,
)

@Composable
fun CustomerCallsScreen(
    onBackClick: () -> Unit = {},
    onRequestTowClick: () -> Unit = {},
    onTrackCallClick: () -> Unit = {},
    onHomeClick: () -> Unit = {},
    onPaymentsClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    var selectedTab by remember { mutableStateOf(CustomerCallsTab.ACTIVE) }
    var selectedCall by remember { mutableStateOf<CustomerCallItem?>(null) }

    val calls = remember {
        listOf(
            CustomerCallItem(
                id = 1,
                date = "24/09/2026",
                time = "18:42",
                pickup = "Asa Norte, Brasília - DF",
                destination = "Águas Claras, Brasília - DF",
                vehicle = "Honda Civic 2020 • ABC1D23",
                problem = "Pane mecânica",
                driver = "Carlos Henrique",
                towTruck = "Mercedes-Benz Accelo Plataforma",
                towTruckPlate = "GUIN2A24",
                payment = "PIX",
                value = "R$ 200,00",
                status = "Guincheiro a caminho",
                active = true
            ),
            CustomerCallItem(
                id = 2,
                date = "14/09/2026",
                time = "16:20",
                pickup = "Asa Norte, Brasília - DF",
                destination = "Águas Claras, Brasília - DF",
                vehicle = "Honda Civic 2020 • ABC1D23",
                problem = "Pane mecânica",
                driver = "Rafael Souza",
                towTruck = "Iveco Daily Plataforma",
                towTruckPlate = "TOW4B21",
                payment = "PIX",
                value = "R$ 200,00",
                status = "Concluído",
                active = false
            ),
            CustomerCallItem(
                id = 3,
                date = "03/09/2026",
                time = "09:15",
                pickup = "Taguatinga, Brasília - DF",
                destination = "SIA, Brasília - DF",
                vehicle = "Honda Civic 2020 • ABC1D23",
                problem = "Pneu danificado",
                driver = "Marcos Lima",
                towTruck = "Volkswagen Delivery Plataforma",
                towTruckPlate = "GRW8C19",
                payment = "Mastercard •••• 4242",
                value = "R$ 150,00",
                status = "Concluído",
                active = false
            )
        )
    }

    selectedCall?.let { call ->
        CustomerCallDetails(
            call = call,
            onBackClick = { selectedCall = null },
            onTrackCallClick = onTrackCallClick
        )
        return
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
            CustomerCallsHeader(onBackClick)

            Column(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "Seus chamados",
                    color = GuinchouWhite,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = "Acompanhe atendimentos em andamento e consulte seu histórico.",
                    color = GuinchouGray,
                    fontSize = 13.sp
                )

                Spacer(Modifier.height(18.dp))

                CallsTabs(
                    selected = selectedTab,
                    onSelected = { selectedTab = it }
                )

                Spacer(Modifier.height(18.dp))

                if (selectedTab == CustomerCallsTab.ACTIVE) {
                    val activeCalls = calls.filter { it.active }

                    if (activeCalls.isEmpty()) {
                        EmptyCallsCard(onRequestTowClick)
                    } else {
                        activeCalls.forEach { call ->
                            ActiveCallCard(
                                call = call,
                                onDetailsClick = { selectedCall = call },
                                onTrackClick = onTrackCallClick
                            )
                            Spacer(Modifier.height(12.dp))
                        }
                    }
                } else {
                    val history = calls.filterNot { it.active }

                    history.forEach { call ->
                        HistoryCallCard(
                            call = call,
                            onClick = { selectedCall = call }
                        )
                        Spacer(Modifier.height(10.dp))
                    }
                }

                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = onRequestTowClick,
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

                Spacer(Modifier.height(22.dp))
            }
        }

        HorizontalDivider(color = GuinchouBorder)

        CustomerCallsBottomBar(
            onHomeClick = onHomeClick,
            onPaymentsClick = onPaymentsClick,
            onProfileClick = onProfileClick
        )
    }
}

@Composable
private fun CustomerCallsHeader(
    onBackClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClick) {
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
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun CallsTabs(
    selected: CustomerCallsTab,
    onSelected: (CustomerCallsTab) -> Unit
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
        CallsTabButton(
            text = "Em andamento",
            selected = selected == CustomerCallsTab.ACTIVE,
            modifier = Modifier.weight(1f),
            onClick = { onSelected(CustomerCallsTab.ACTIVE) }
        )

        CallsTabButton(
            text = "Histórico",
            selected = selected == CustomerCallsTab.HISTORY,
            modifier = Modifier.weight(1f),
            onClick = { onSelected(CustomerCallsTab.HISTORY) }
        )
    }
}

@Composable
private fun CallsTabButton(
    text: String,
    selected: Boolean,
    modifier: Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .background(
                if (selected) GuinchouGreen else Color.Transparent,
                RoundedCornerShape(10.dp)
            )
            .clickable(onClick = onClick)
            .padding(vertical = 11.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (selected) GuinchouBackground else GuinchouGray,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun ActiveCallCard(
    call: CustomerCallItem,
    onDetailsClick: () -> Unit,
    onTrackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                GuinchouSurface,
                RoundedCornerShape(18.dp)
            )
            .border(
                1.dp,
                GuinchouGreen.copy(alpha = 0.40f),
                RoundedCornerShape(18.dp)
            )
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            StatusDot()
            Spacer(Modifier.size(8.dp))
            Text(
                text = call.status,
                color = GuinchouGreen,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = call.value,
                color = GuinchouWhite,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.height(16.dp))

        RouteLine(
            pickup = call.pickup,
            destination = call.destination
        )

        Spacer(Modifier.height(14.dp))
        HorizontalDivider(color = GuinchouBorder)
        Spacer(Modifier.height(14.dp))

        Text(
            text = call.vehicle,
            color = GuinchouWhite,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.height(3.dp))
        Text(
            text = "${call.driver} • ${call.towTruck}",
            color = GuinchouGray,
            fontSize = 10.sp
        )

        Spacer(Modifier.height(14.dp))

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            TextButton(
                onClick = onDetailsClick,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Ver detalhes",
                    color = GuinchouWhite,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(Modifier.size(8.dp))

            Button(
                onClick = onTrackClick,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = GuinchouGreen,
                    contentColor = GuinchouBackground
                )
            ) {
                Text(
                    text = "Acompanhar",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun HistoryCallCard(
    call: CustomerCallItem,
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
            .padding(15.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "${call.date} • ${call.time}",
                    color = GuinchouGray,
                    fontSize = 10.sp
                )
                Spacer(Modifier.height(3.dp))
                Text(
                    text = call.status,
                    color = GuinchouGreen,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = call.value,
                color = GuinchouWhite,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.height(12.dp))

        RouteLine(
            pickup = call.pickup,
            destination = call.destination
        )

        Spacer(Modifier.height(10.dp))

        Text(
            text = call.vehicle,
            color = GuinchouGray,
            fontSize = 10.sp
        )
    }
}

@Composable
private fun RouteLine(
    pickup: String,
    destination: String
) {
    Row {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .background(GuinchouGreen, CircleShape)
            )
            Box(
                modifier = Modifier
                    .size(width = 2.dp, height = 30.dp)
                    .background(GuinchouBorder)
            )
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .border(2.dp, GuinchouGreen, CircleShape)
            )
        }

        Spacer(Modifier.size(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "Origem",
                color = GuinchouGray,
                fontSize = 9.sp
            )
            Text(
                text = pickup,
                color = GuinchouWhite,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(13.dp))

            Text(
                text = "Destino",
                color = GuinchouGray,
                fontSize = 9.sp
            )
            Text(
                text = destination,
                color = GuinchouWhite,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun EmptyCallsCard(
    onRequestTowClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                GuinchouSurface,
                RoundedCornerShape(18.dp)
            )
            .border(
                1.dp,
                GuinchouBorder,
                RoundedCornerShape(18.dp)
            )
            .padding(22.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Build,
            contentDescription = null,
            tint = GuinchouGreen,
            modifier = Modifier.size(38.dp)
        )

        Spacer(Modifier.height(12.dp))

        Text(
            text = "Nenhum chamado em andamento",
            color = GuinchouWhite,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(5.dp))

        Text(
            text = "Quando você solicitar um guincho, poderá acompanhar o atendimento por aqui.",
            color = GuinchouGray,
            fontSize = 11.sp,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(14.dp))

        TextButton(onClick = onRequestTowClick) {
            Text(
                text = "Solicitar guincho",
                color = GuinchouGreen,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun CustomerCallDetails(
    call: CustomerCallItem,
    onBackClick: () -> Unit,
    onTrackCallClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GuinchouBackground)
            .statusBarsPadding()
    ) {
        CustomerCallsHeader(onBackClick)

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Detalhes do chamado",
                color = GuinchouWhite,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(5.dp))

            Text(
                text = "Chamado #${call.id.toString().padStart(4, '0')} • ${call.date} às ${call.time}",
                color = GuinchouGray,
                fontSize = 11.sp
            )

            Spacer(Modifier.height(18.dp))

            DetailSection(
                title = "Status do atendimento"
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StatusDot()
                    Spacer(Modifier.size(8.dp))
                    Text(
                        text = call.status,
                        color = GuinchouGreen,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            DetailSection(title = "Trajeto") {
                RouteLine(
                    pickup = call.pickup,
                    destination = call.destination
                )
            }

            Spacer(Modifier.height(12.dp))

            DetailSection(title = "Veículo e problema") {
                DetailValue("Veículo", call.vehicle)
                Spacer(Modifier.height(10.dp))
                DetailValue("Problema informado", call.problem)
            }

            Spacer(Modifier.height(12.dp))

            DetailSection(title = "Guincheiro") {
                DetailValue("Motorista", call.driver)
                Spacer(Modifier.height(10.dp))
                DetailValue("Guincho", call.towTruck)
                Spacer(Modifier.height(10.dp))
                DetailValue("Placa", call.towTruckPlate)
            }

            Spacer(Modifier.height(12.dp))

            DetailSection(title = "Pagamento") {
                DetailValue("Forma de pagamento", call.payment)
                Spacer(Modifier.height(10.dp))
                DetailValue("Valor total", call.value, highlight = true)
            }

            Spacer(Modifier.height(12.dp))

            DetailSection(title = "Linha do tempo") {
                TimelineItem(
                    title = "Solicitação criada",
                    subtitle = "${call.date} • ${call.time}",
                    completed = true
                )
                TimelineItem(
                    title = "Guincheiro aceitou o chamado",
                    subtitle = call.driver,
                    completed = true
                )
                TimelineItem(
                    title = if (call.active) "Guincheiro a caminho" else "Veículo transportado",
                    subtitle = if (call.active) "Atendimento em andamento" else "Etapa concluída",
                    completed = true
                )
                TimelineItem(
                    title = "Atendimento concluído",
                    subtitle = if (call.active) "Aguardando conclusão" else "Serviço finalizado",
                    completed = !call.active,
                    last = true
                )
            }

            if (call.active) {
                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = onTrackCallClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GuinchouGreen,
                        contentColor = GuinchouBackground
                    )
                ) {
                    Text(
                        text = "Acompanhar em tempo real",
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun DetailSection(
    title: String,
    content: @Composable () -> Unit
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
            .padding(15.dp)
    ) {
        Text(
            text = title,
            color = GuinchouWhite,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(12.dp))

        content()
    }
}

@Composable
private fun DetailValue(
    label: String,
    value: String,
    highlight: Boolean = false
) {
    Column {
        Text(
            text = label,
            color = GuinchouGray,
            fontSize = 9.sp
        )
        Spacer(Modifier.height(2.dp))
        Text(
            text = value,
            color = if (highlight) GuinchouGreen else GuinchouWhite,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun TimelineItem(
    title: String,
    subtitle: String,
    completed: Boolean,
    last: Boolean = false
) {
    Row {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .background(
                        if (completed) GuinchouGreen else GuinchouBackground,
                        CircleShape
                    )
                    .border(
                        1.dp,
                        if (completed) GuinchouGreen else GuinchouBorder,
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (completed) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = GuinchouBackground,
                        modifier = Modifier.size(13.dp)
                    )
                }
            }

            if (!last) {
                Box(
                    modifier = Modifier
                        .size(width = 2.dp, height = 28.dp)
                        .background(
                            if (completed) {
                                GuinchouGreen.copy(alpha = 0.45f)
                            } else {
                                GuinchouBorder
                            }
                        )
                )
            }
        }

        Spacer(Modifier.size(10.dp))

        Column {
            Text(
                text = title,
                color = if (completed) GuinchouWhite else GuinchouGray,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = subtitle,
                color = GuinchouGray,
                fontSize = 9.sp
            )
        }
    }
}

@Composable
private fun StatusDot() {
    Box(
        modifier = Modifier
            .size(9.dp)
            .background(GuinchouGreen, CircleShape)
    )
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
            .padding(horizontal = 8.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CustomerCallBottomItem(
            icon = Icons.Default.Home,
            text = "Início",
            onClick = onHomeClick
        )

        CustomerCallBottomItem(
            icon = Icons.Default.Build,
            text = "Chamados",
            selected = true
        )

        CustomerCallBottomItem(
            icon = Icons.Default.CreditCard,
            text = "Pagamentos",
            onClick = onPaymentsClick
        )

        CustomerCallBottomItem(
            icon = Icons.Default.Person,
            text = "Perfil",
            onClick = onProfileClick
        )
    }
}

@Composable
private fun CustomerCallBottomItem(
    icon: ImageVector,
    text: String,
    selected: Boolean = false,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = if (selected) GuinchouGreen else GuinchouGray,
            modifier = Modifier.size(22.dp)
        )

        Spacer(Modifier.height(4.dp))

        Text(
            text = text,
            color = if (selected) GuinchouGreen else GuinchouGray,
            fontSize = 11.sp,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(3.dp))

        Box(
            modifier = Modifier
                .size(width = 18.dp, height = 2.dp)
                .background(
                    if (selected) GuinchouGreen else Color.Transparent,
                    RoundedCornerShape(50)
                )
        )
    }
}
