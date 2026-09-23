package com.guinchou.app.ui.screens.partner

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.FormatListBulleted
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.guinchou.app.ui.theme.*

private val EarningsCard = Color(0xFF0E1A2A)
private val EarningsCardSoft = Color(0xFF112033)
private val EarningsMuted = Color(0xFF8998AD)
private val EarningsBorder = Color(0xFF1B2B3F)
private val EarningsWarning = Color(0xFFFFB547)

private enum class EarningsPeriod(val label: String) {
    TODAY("Hoje"),
    WEEK("Semana"),
    MONTH("Mês")
}

private data class EarningItem(
    val id: String,
    val date: String,
    val time: String,
    val route: String,
    val value: String,
    val status: String
)

@Composable
fun PartnerEarningsScreen(
    driverName: String = "João",
    openHistory: Boolean = false,
    onBackClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    onHomeClick: () -> Unit = {},
    onCallsClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    var selectedPeriod by remember {
        mutableStateOf(EarningsPeriod.WEEK)
    }

    var showFullHistory by remember(openHistory) {
        mutableStateOf(openHistory)
    }

    var selectedHistoryItem by remember {
        mutableStateOf<EarningItem?>(null)
    }

    var historyFilter by remember {
        mutableStateOf("Todos")
    }

    val history = remember {
        listOf(
            EarningItem("101", "Hoje", "14:20", "Asa Sul -> Taguatinga", "R$ 145,00", "Concluído"),
            EarningItem("102", "Hoje", "11:05", "Sudoeste -> Plano Piloto", "R$ 112,50", "Concluído"),
            EarningItem("103", "Hoje", "08:40", "Lago Norte -> Sobradinho", "R$ 125,00", "Concluído"),
            EarningItem("104", "Ontem", "18:15", "Guará -> Águas Claras", "R$ 98,00", "Concluído"),
            EarningItem("105", "Ontem", "15:30", "Ceilândia -> Taguatinga", "R$ 130,00", "Concluído"),
            EarningItem("106", "18/03", "10:10", "Gama -> Plano Piloto", "R$ 180,00", "Pendente")
        )
    }

    val summary = remember(selectedPeriod) {
        when (selectedPeriod) {
            EarningsPeriod.TODAY -> Triple("R$ 382,50", "3", "R$ 127,50")
            EarningsPeriod.WEEK -> Triple("R$ 1.840,00", "14", "R$ 131,42")
            EarningsPeriod.MONTH -> Triple("R$ 6.920,00", "52", "R$ 133,07")
        }
    }

    if (showFullHistory) {
        FullEarningsHistory(
            history = history,
            selectedFilter = historyFilter,
            onFilterSelected = { historyFilter = it },
            onBackClick = { showFullHistory = false },
            onItemClick = { selectedHistoryItem = it }
        )

        selectedHistoryItem?.let { item ->
            EarningDetailsDialog(
                item = item,
                onDismiss = { selectedHistoryItem = null }
            )
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF07101C),
                        Color(0xFF081321),
                        Color(0xFF050D17)
                    )
                )
            )
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(12.dp))
            EarningsHeader(onBackClick, onNotificationsClick)

            Spacer(Modifier.height(23.dp))

            Text(
                text = "Seus ganhos",
                color = GuinchouWhite,
                fontSize = 25.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(Modifier.height(5.dp))

            Text(
                text = "Acompanhe seus recebimentos e o desempenho dos seus atendimentos.",
                color = EarningsMuted,
                fontSize = 12.sp,
                lineHeight = 17.sp
            )

            Spacer(Modifier.height(18.dp))

            BalanceCard()

            Spacer(Modifier.height(18.dp))

            PeriodSelector(
                selected = selectedPeriod,
                onSelected = { selectedPeriod = it }
            )

            Spacer(Modifier.height(18.dp))

            EarningsSummary(
                total = summary.first,
                services = summary.second,
                average = summary.third
            )

            Spacer(Modifier.height(20.dp))

            PerformanceCard(
                period = selectedPeriod,
                driverName = driverName
            )

            Spacer(Modifier.height(22.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Últimos atendimentos",
                    color = GuinchouWhite,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = "Ver histórico",
                    color = GuinchouGreen,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { showFullHistory = true }
                        .padding(horizontal = 8.dp, vertical = 5.dp)
                )
            }

            Spacer(Modifier.height(11.dp))

            history.forEachIndexed { index, item ->
                EarningHistoryCard(item, onClick = { selectedHistoryItem = item })
                if (index != history.lastIndex) Spacer(Modifier.height(10.dp))
            }

            Spacer(Modifier.height(25.dp))
        }

        selectedHistoryItem?.let { item ->
            EarningDetailsDialog(
                item = item,
                onDismiss = { selectedHistoryItem = null }
            )
        }

        EarningsBottomBar(
            onHomeClick = onHomeClick,
            onCallsClick = onCallsClick,
            onProfileClick = onProfileClick
        )
    }
}

@Composable
private fun EarningsHeader(
    onBackClick: () -> Unit,
    onNotificationsClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        EarningsHeaderButton(
            icon = Icons.AutoMirrored.Filled.ArrowBack,
            description = "Voltar",
            onClick = onBackClick
        )

        Text(
            text = "Ganhos",
            color = GuinchouWhite,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .weight(1f)
                .padding(start = 14.dp)
        )

        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(13.dp))
                .background(EarningsCard)
                .border(1.dp, EarningsBorder, RoundedCornerShape(13.dp))
                .clickable(onClick = onNotificationsClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Notificações",
                tint = GuinchouWhite,
                modifier = Modifier.size(20.dp)
            )

            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = (-5).dp, y = 5.dp)
                    .size(8.dp)
                    .background(GuinchouGreen, CircleShape)
                    .border(2.dp, EarningsCard, CircleShape)
            )
        }
    }
}

@Composable
private fun EarningsHeaderButton(
    icon: ImageVector,
    description: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(42.dp)
            .clip(RoundedCornerShape(13.dp))
            .background(EarningsCard)
            .border(1.dp, EarningsBorder, RoundedCornerShape(13.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = description,
            tint = GuinchouWhite,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
private fun BalanceCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(
                Brush.horizontalGradient(
                    listOf(
                        GuinchouGreen.copy(alpha = .16f),
                        EarningsCardSoft,
                        EarningsCard
                    )
                )
            )
            .border(
                1.dp,
                GuinchouGreen.copy(alpha = .32f),
                RoundedCornerShape(22.dp)
            )
            .padding(18.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(13.dp))
                    .background(GuinchouGreen.copy(alpha = .12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AccountBalanceWallet,
                    contentDescription = null,
                    tint = GuinchouGreen,
                    modifier = Modifier.size(23.dp)
                )
            }

            Spacer(Modifier.width(12.dp))

            Column(Modifier.weight(1f)) {
                Text(
                    text = "Saldo disponível",
                    color = EarningsMuted,
                    fontSize = 10.sp
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = "R$ 1.267,50",
                    color = GuinchouWhite,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(GuinchouGreen.copy(alpha = .12f))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "Disponível",
                    color = GuinchouGreen,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(Modifier.height(15.dp))
        HorizontalDivider(color = EarningsBorder.copy(alpha = .8f))
        Spacer(Modifier.height(13.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null,
                tint = EarningsMuted,
                modifier = Modifier.size(15.dp)
            )
            Spacer(Modifier.width(7.dp))
            Text(
                text = "Valores ilustrativos nesta etapa do desenvolvimento.",
                color = EarningsMuted,
                fontSize = 9.sp
            )
        }
    }
}

@Composable
private fun PeriodSelector(
    selected: EarningsPeriod,
    onSelected: (EarningsPeriod) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(15.dp))
            .background(Color(0xFF091421))
            .border(1.dp, EarningsBorder, RoundedCornerShape(15.dp))
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        EarningsPeriod.entries.forEach { period ->
            val isSelected = selected == period

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(11.dp))
                    .background(
                        if (isSelected) GuinchouGreen.copy(alpha = .14f)
                        else Color.Transparent
                    )
                    .clickable { onSelected(period) }
                    .padding(vertical = 9.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = period.label,
                    color = if (isSelected) GuinchouGreen else EarningsMuted,
                    fontSize = 10.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun EarningsSummary(
    total: String,
    services: String,
    average: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(9.dp)
    ) {
        SummaryCard(
            modifier = Modifier.weight(1f),
            icon = Icons.Default.Payments,
            label = "Ganhos",
            value = total
        )

        SummaryCard(
            modifier = Modifier.weight(1f),
            icon = Icons.Default.LocalShipping,
            label = "Corridas",
            value = services
        )

        SummaryCard(
            modifier = Modifier.weight(1f),
            icon = Icons.AutoMirrored.Filled.TrendingUp,
            label = "Média",
            value = average
        )
    }
}

@Composable
private fun SummaryCard(
    modifier: Modifier,
    icon: ImageVector,
    label: String,
    value: String
) {
    Column(
        modifier = modifier
            .height(102.dp)
            .clip(RoundedCornerShape(17.dp))
            .background(EarningsCard)
            .border(1.dp, EarningsBorder, RoundedCornerShape(17.dp))
            .padding(12.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = GuinchouGreen,
            modifier = Modifier.size(19.dp)
        )

        Spacer(Modifier.weight(1f))

        Text(
            text = value,
            color = GuinchouWhite,
            fontSize = 12.sp,
            fontWeight = FontWeight.ExtraBold,
            maxLines = 1
        )

        Spacer(Modifier.height(2.dp))

        Text(
            text = label,
            color = EarningsMuted,
            fontSize = 8.sp
        )
    }
}

@Composable
private fun PerformanceCard(
    period: EarningsPeriod,
    driverName: String
) {
    val progress = when (period) {
        EarningsPeriod.TODAY -> .70f
        EarningsPeriod.WEEK -> .82f
        EarningsPeriod.MONTH -> .76f
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(EarningsCard)
            .border(1.dp, EarningsBorder, RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(GuinchouGreen.copy(alpha = .11f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Insights,
                    contentDescription = null,
                    tint = GuinchouGreen,
                    modifier = Modifier.size(21.dp)
                )
            }

            Spacer(Modifier.width(11.dp))

            Column(Modifier.weight(1f)) {
                Text(
                    text = "Resumo de desempenho",
                    color = GuinchouWhite,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Acompanhamento do período selecionado",
                    color = EarningsMuted,
                    fontSize = 9.sp
                )
            }

            Text(
                text = "4,9 ★",
                color = EarningsWarning,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.height(17.dp))

        Text(
            text = "Taxa de conclusão",
            color = EarningsMuted,
            fontSize = 9.sp
        )

        Spacer(Modifier.height(7.dp))

        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(7.dp)
                .clip(CircleShape),
            color = GuinchouGreen,
            trackColor = Color(0xFF172638)
        )

        Spacer(Modifier.height(7.dp))

        Row {
            Text(
                text = "${(progress * 100).toInt()}% dos atendimentos concluídos",
                color = GuinchouWhite.copy(alpha = .82f),
                fontSize = 9.sp,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = driverName,
                color = EarningsMuted,
                fontSize = 9.sp
            )
        }
    }
}

@Composable
private fun EarningHistoryCard(
    item: EarningItem,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .clip(RoundedCornerShape(17.dp))
            .background(EarningsCard)
            .border(1.dp, EarningsBorder, RoundedCornerShape(17.dp))
            .padding(13.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(13.dp))
                .background(GuinchouGreen.copy(alpha = .10f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.LocalShipping,
                contentDescription = null,
                tint = GuinchouGreen,
                modifier = Modifier.size(21.dp)
            )
        }

        Spacer(Modifier.width(11.dp))

        Column(Modifier.weight(1f)) {
            Text(
                text = item.route,
                color = GuinchouWhite,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(3.dp))

            Text(
                text = "${item.id} • ${item.date} às ${item.time}",
                color = EarningsMuted,
                fontSize = 8.sp
            )
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = item.value,
                color = GuinchouGreen,
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(Modifier.height(3.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .background(GuinchouGreen, CircleShape)
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = item.status,
                    color = EarningsMuted,
                    fontSize = 8.sp
                )
            }
        }
    }
}


@Composable
private fun FullEarningsHistory(
    history: List<EarningItem>,
    selectedFilter: String,
    onFilterSelected: (String) -> Unit,
    onBackClick: () -> Unit,
    onItemClick: (EarningItem) -> Unit
) {
    val filtered = when (selectedFilter) {
        "Pagos" -> history.filter { it.status == "Pago" }
        "Pendentes" -> history.filter { it.status == "Pendente" }
        else -> history
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF07101C), Color(0xFF081321), Color(0xFF050D17))
                )
            )
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                EarningsHeaderButton(
                    icon = Icons.AutoMirrored.Filled.ArrowBack,
                    description = "Voltar",
                    onClick = onBackClick
                )

                Text(
                    text = "Histórico de ganhos",
                    color = GuinchouWhite,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 14.dp)
                )
            }

            Spacer(Modifier.height(25.dp))

            Text(
                text = "Seus atendimentos",
                color = GuinchouWhite,
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(Modifier.height(5.dp))

            Text(
                text = "Consulte os valores e a situação financeira de cada serviço realizado.",
                color = EarningsMuted,
                fontSize = 11.sp,
                lineHeight = 17.sp
            )

            Spacer(Modifier.height(18.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(9.dp)
            ) {
                HistoryInfoCard(
                    modifier = Modifier.weight(1f),
                    label = "Total exibido",
                    value = if (selectedFilter == "Pendentes") "R$ 150,00" else "R$ 1.000,00"
                )
                HistoryInfoCard(
                    modifier = Modifier.weight(1f),
                    label = "Atendimentos",
                    value = filtered.size.toString()
                )
            }

            Spacer(Modifier.height(18.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(7.dp)
            ) {
                listOf("Todos", "Pagos", "Pendentes").forEach { filter ->
                    val selected = selectedFilter == filter
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (selected) GuinchouGreen.copy(alpha = .14f)
                                else EarningsCard
                            )
                            .border(
                                1.dp,
                                if (selected) GuinchouGreen.copy(alpha = .35f) else EarningsBorder,
                                RoundedCornerShape(12.dp)
                            )
                            .clickable { onFilterSelected(filter) }
                            .padding(vertical = 9.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = filter,
                            color = if (selected) GuinchouGreen else EarningsMuted,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            filtered.forEachIndexed { index, item ->
                EarningHistoryCard(
                    item = item,
                    onClick = { onItemClick(item) }
                )
                if (index != filtered.lastIndex) Spacer(Modifier.height(10.dp))
            }

            Spacer(Modifier.height(30.dp))
        }
    }
}

@Composable
private fun HistoryInfoCard(
    modifier: Modifier,
    label: String,
    value: String
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(17.dp))
            .background(EarningsCard)
            .border(1.dp, EarningsBorder, RoundedCornerShape(17.dp))
            .padding(14.dp)
    ) {
        Text(
            text = label,
            color = EarningsMuted,
            fontSize = 9.sp
        )
        Spacer(Modifier.height(5.dp))
        Text(
            text = value,
            color = GuinchouWhite,
            fontSize = 17.sp,
            fontWeight = FontWeight.ExtraBold
        )
    }
}

@Composable
private fun EarningDetailsDialog(
    item: EarningItem,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF0B1725),
        shape = RoundedCornerShape(22.dp),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(GuinchouGreen.copy(alpha = .12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ReceiptLong,
                        contentDescription = null,
                        tint = GuinchouGreen,
                        modifier = Modifier.size(21.dp)
                    )
                }
                Spacer(Modifier.width(11.dp))
                Column {
                    Text(
                        text = "Detalhes financeiros",
                        color = GuinchouWhite,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = item.id,
                        color = EarningsMuted,
                        fontSize = 9.sp
                    )
                }
            }
        },
        text = {
            Column {
                FinancialDetailRow("Rota", item.route)
                FinancialDetailRow("Data", item.date)
                FinancialDetailRow("Horário", item.time)
                FinancialDetailRow("Valor do serviço", item.value)
                FinancialDetailRow("Status", item.status, highlight = true)

                Spacer(Modifier.height(10.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(13.dp))
                        .background(GuinchouGreen.copy(alpha = .08f))
                        .padding(11.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = GuinchouGreen,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "Nesta etapa, os dados financeiros são demonstrativos e serão integrados ao backend posteriormente.",
                        color = EarningsMuted,
                        fontSize = 8.sp,
                        lineHeight = 12.sp
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Fechar",
                    color = GuinchouGreen,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    )
}

@Composable
private fun FinancialDetailRow(
    label: String,
    value: String,
    highlight: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = EarningsMuted,
            fontSize = 9.sp,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            color = if (highlight && value == "Pendente") EarningsWarning
            else if (highlight) GuinchouGreen
            else GuinchouWhite,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun EarningsBottomBar(
    onHomeClick: () -> Unit,
    onCallsClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF08121E))
            .border(1.dp, EarningsBorder.copy(alpha = .85f))
            .padding(horizontal = 9.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        EarningsBottomItem(
            icon = Icons.Default.Home,
            label = "Início",
            selected = false,
            onClick = onHomeClick
        )

        EarningsBottomItem(
            icon = Icons.AutoMirrored.Filled.FormatListBulleted,
            label = "Chamados",
            selected = false,
            onClick = onCallsClick
        )

        EarningsBottomItem(
            icon = Icons.Default.AccountBalanceWallet,
            label = "Ganhos",
            selected = true,
            onClick = {}
        )

        EarningsBottomItem(
            icon = Icons.Default.Person,
            label = "Perfil",
            selected = false,
            onClick = onProfileClick
        )
    }
}

@Composable
private fun EarningsBottomItem(
    icon: ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(11.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (selected) GuinchouGreen else GuinchouWhite.copy(alpha = .88f),
            modifier = Modifier.size(21.dp)
        )

        Spacer(Modifier.height(3.dp))

        Text(
            text = label,
            color = if (selected) GuinchouGreen else GuinchouWhite.copy(alpha = .70f),
            fontSize = 9.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
        )
    }
}
