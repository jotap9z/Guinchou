package com.guinchou.app.ui.screens.partner.company

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.guinchou.app.ui.theme.GuinchouBackground
import com.guinchou.app.ui.theme.GuinchouBorder
import com.guinchou.app.ui.theme.GuinchouGray
import com.guinchou.app.ui.theme.GuinchouGreen
import com.guinchou.app.ui.theme.GuinchouSurface
import com.guinchou.app.ui.theme.GuinchouWhite

/*
 * =============================================================
 * COMPANY AREA
 * =============================================================
 *
 * Este arquivo concentra temporariamente todo o Front-End da
 * área empresarial do Guinchou:
 *
 * - CompanyHomeScreen
 * - CompanyCallsScreen
 * - CompanyManagementScreen
 * - CompanyFinanceScreen
 * - CompanyProfileScreen
 * - CompanyDriversScreen
 * - CompanyFleetScreen
 * - CompanyBottomBar
 *
 * Os dados são locais/mockados enquanto o backend empresarial
 * ainda não está sendo integrado.
 */

/* =============================================================
 * MODELOS LOCAIS
 * ============================================================= */

enum class CompanyBottomDestination {
    HOME,
    CALLS,
    MANAGEMENT,
    PROFILE
}

private enum class DriverStatus {
    PENDING,
    ACTIVE,
    SUSPENDED
}

private enum class FleetStatus {
    REVIEW,
    ACTIVE,
    MAINTENANCE
}

private enum class CompanyCallStatus {
    AVAILABLE,
    IN_PROGRESS,
    COMPLETED
}

private enum class CompanyCallFilter {
    ALL,
    AVAILABLE,
    IN_PROGRESS,
    COMPLETED
}

private enum class FinanceFilter {
    ALL,
    RECEIVED,
    PENDING
}

private data class CompanyDriver(
    val id: Int,
    val name: String,
    val phone: String,
    val cnh: String,
    val category: String,
    val towTruck: String?,
    val status: DriverStatus
)

private data class CompanyVehicle(
    val id: Int,
    val plate: String,
    val model: String,
    val year: String,
    val type: String,
    val capacity: String,
    val assignedDriver: String?,
    val status: FleetStatus
)

private data class CompanyCall(
    val id: Int,
    val code: String,
    val customerName: String,
    val serviceType: String,
    val vehicle: String,
    val pickupAddress: String,
    val destinationAddress: String,
    val distance: String,
    val value: Double,
    val requestedAt: String,
    val status: CompanyCallStatus,
    val assignedDriver: String? = null
)

private data class FinanceTransaction(
    val id: Int,
    val title: String,
    val subtitle: String,
    val date: String,
    val value: Double,
    val received: Boolean
)

/* =============================================================
 * COMPANY HOME
 * ============================================================= */

@Composable
fun CompanyHomeScreen(
    ownerName: String = "João",
    companyName: String = "Empresa de Guinchos",
    driversCount: Int = 3,
    towTrucksCount: Int = 3,
    activeCalls: Int = 1,
    monthlyRevenue: Double = 3250.00,
    onNotificationsClick: () -> Unit = {},
    onDriversClick: () -> Unit = {},
    onFleetClick: () -> Unit = {},
    onCallsClick: () -> Unit = {},
    onFinanceClick: () -> Unit = {},
    onDriverInviteClick: () -> Unit = {},
    onTowTruckInviteClick: () -> Unit = {},
    onManagementClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    Scaffold(
        containerColor = GuinchouBackground,
        bottomBar = {
            CompanyBottomBar(
                selectedDestination = CompanyBottomDestination.HOME,
                onHomeClick = {},
                onCallsClick = onCallsClick,
                onManagementClick = onManagementClick,
                onProfileClick = onProfileClick
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "GUINCHOU EMPRESAS",
                        color = GuinchouGreen,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "Olá, $ownerName",
                        color = GuinchouWhite,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        companyName,
                        color = GuinchouGray,
                        fontSize = 14.sp
                    )
                }

                Surface(
                    color = GuinchouSurface,
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, GuinchouBorder)
                ) {
                    IconButton(onClick = onNotificationsClick) {
                        Icon(
                            Icons.Default.Notifications,
                            contentDescription = "Notificações",
                            tint = GuinchouWhite
                        )
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            StatusBanner(
                title = "Conta empresarial ativa",
                subtitle = "Sua central de operação está pronta."
            )

            Spacer(Modifier.height(26.dp))
            SectionTitle("Visão geral")
            Spacer(Modifier.height(12.dp))

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MetricCard(
                    Modifier.weight(1f),
                    Icons.Default.Groups,
                    driversCount.toString(),
                    "Motoristas"
                )
                MetricCard(
                    Modifier.weight(1f),
                    Icons.Default.LocalShipping,
                    towTrucksCount.toString(),
                    "Guinchos"
                )
            }

            Spacer(Modifier.height(12.dp))

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MetricCard(
                    Modifier.weight(1f),
                    Icons.Default.SupportAgent,
                    activeCalls.toString(),
                    "Chamados ativos"
                )
                MetricCard(
                    Modifier.weight(1f),
                    Icons.Default.Payments,
                    money(monthlyRevenue),
                    "Este mês"
                )
            }

            Spacer(Modifier.height(28.dp))
            SectionTitle("Cadastro rápido")
            Spacer(Modifier.height(5.dp))

            Text(
                "Cadastre equipe e frota através dos convites vinculados à empresa.",
                color = GuinchouGray,
                fontSize = 13.sp,
                lineHeight = 18.sp
            )

            Spacer(Modifier.height(14.dp))

            InviteCard(
                icon = Icons.Default.PersonAdd,
                title = "Convidar motorista",
                description = "Gere um QR Code para o funcionário preencher os próprios dados.",
                buttonText = "Gerar QR do motorista",
                onClick = onDriverInviteClick
            )

            Spacer(Modifier.height(12.dp))

            InviteCard(
                icon = Icons.Default.QrCode2,
                title = "Cadastrar guincho",
                description = "Gere um QR Code para cadastrar dados e documentos do veículo.",
                buttonText = "Gerar QR do guincho",
                onClick = onTowTruckInviteClick
            )

            Spacer(Modifier.height(28.dp))
            SectionTitle("Gestão da empresa")
            Spacer(Modifier.height(12.dp))

            ManagementRow(
                icon = Icons.Default.Groups,
                title = "Motoristas",
                subtitle = "Equipe, cadastros e situação dos funcionários",
                onClick = onDriversClick
            )

            ManagementRow(
                icon = Icons.Default.LocalShipping,
                title = "Frota",
                subtitle = "Guinchos, documentos e situação dos veículos",
                onClick = onFleetClick
            )

            ManagementRow(
                icon = Icons.Default.SupportAgent,
                title = "Chamados",
                subtitle = "Acompanhe os atendimentos da empresa",
                onClick = onCallsClick
            )

            ManagementRow(
                icon = Icons.Default.AccountBalanceWallet,
                title = "Financeiro",
                subtitle = "Ganhos, repasses e histórico financeiro",
                onClick = onFinanceClick
            )

            Spacer(Modifier.height(24.dp))
        }
    }
}

/* =============================================================
 * COMPANY CALLS
 * ============================================================= */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompanyCallsScreen(
    onBackClick: () -> Unit = {},
    onHomeClick: () -> Unit = {},
    onManagementClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    val calls = remember {
        mutableStateListOf(
            CompanyCall(
                1, "#GCH-1042", "Ricardo Almeida", "Pane mecânica",
                "Honda Civic 2022", "Asa Norte, Brasília - DF",
                "SIA, Brasília - DF", "12,4 km", 200.0, "Há 4 min",
                CompanyCallStatus.AVAILABLE
            ),
            CompanyCall(
                2, "#GCH-1041", "Amanda Ribeiro", "Transporte de veículo",
                "Toyota Corolla 2021", "Águas Claras - DF",
                "Taguatinga - DF", "9,8 km", 150.0, "Há 12 min",
                CompanyCallStatus.AVAILABLE
            ),
            CompanyCall(
                3, "#GCH-1039", "Lucas Ferreira", "Pane elétrica",
                "Volkswagen T-Cross 2023", "Guará II - DF",
                "SIA - DF", "14,1 km", 200.0, "Hoje, 13:22",
                CompanyCallStatus.IN_PROGRESS, "Carlos Henrique"
            ),
            CompanyCall(
                4, "#GCH-1034", "Mariana Souza", "Acidente / colisão",
                "Hyundai HB20 2020", "Lago Sul - DF",
                "Asa Sul - DF", "17,3 km", 200.0, "Hoje, 09:35",
                CompanyCallStatus.COMPLETED, "Marcos Silva"
            )
        )
    }

    var filter by remember { mutableStateOf(CompanyCallFilter.ALL) }
    var selectedCall by remember { mutableStateOf<CompanyCall?>(null) }

    val visible = when (filter) {
        CompanyCallFilter.ALL -> calls
        CompanyCallFilter.AVAILABLE -> calls.filter { it.status == CompanyCallStatus.AVAILABLE }
        CompanyCallFilter.IN_PROGRESS -> calls.filter { it.status == CompanyCallStatus.IN_PROGRESS }
        CompanyCallFilter.COMPLETED -> calls.filter { it.status == CompanyCallStatus.COMPLETED }
    }

    val newCount = calls.count { it.status == CompanyCallStatus.AVAILABLE }
    val progressCount = calls.count { it.status == CompanyCallStatus.IN_PROGRESS }
    val completedCount = calls.count { it.status == CompanyCallStatus.COMPLETED }

    fun acceptCall(call: CompanyCall) {
        val index = calls.indexOfFirst { it.id == call.id }
        if (index >= 0) {
            calls[index] = call.copy(
                status = CompanyCallStatus.IN_PROGRESS,
                assignedDriver = "Aguardando motorista"
            )
        }
        selectedCall = null
    }

    CompanyScaffold(
        title = "Chamados",
        subtitle = "Operação da empresa",
        selected = CompanyBottomDestination.CALLS,
        onBackClick = onBackClick,
        onHomeClick = onHomeClick,
        onCallsClick = {},
        onManagementClick = onManagementClick,
        onProfileClick = onProfileClick
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Spacer(Modifier.height(8.dp))
                Text(
                    "Visão da operação",
                    color = GuinchouWhite,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "Acompanhe os atendimentos recebidos e a situação de cada serviço.",
                    color = GuinchouGray,
                    fontSize = 13.sp
                )
            }

            item {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    SummaryCounter(
                        Modifier.weight(1f),
                        newCount,
                        "Novos",
                        true
                    )
                    SummaryCounter(
                        Modifier.weight(1f),
                        progressCount,
                        "Em andamento"
                    )
                    SummaryCounter(
                        Modifier.weight(1f),
                        completedCount,
                        "Concluídos"
                    )
                }
            }

            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    item {
                        CallFilterChip("Todos", filter == CompanyCallFilter.ALL) {
                            filter = CompanyCallFilter.ALL
                        }
                    }
                    item {
                        CallFilterChip("Novos", filter == CompanyCallFilter.AVAILABLE) {
                            filter = CompanyCallFilter.AVAILABLE
                        }
                    }
                    item {
                        CallFilterChip(
                            "Em andamento",
                            filter == CompanyCallFilter.IN_PROGRESS
                        ) {
                            filter = CompanyCallFilter.IN_PROGRESS
                        }
                    }
                    item {
                        CallFilterChip(
                            "Concluídos",
                            filter == CompanyCallFilter.COMPLETED
                        ) {
                            filter = CompanyCallFilter.COMPLETED
                        }
                    }
                }
            }

            if (visible.isEmpty()) {
                item {
                    EmptyStateCard(
                        Icons.Default.CheckCircle,
                        "Nenhum chamado",
                        "Não há atendimentos nesta categoria."
                    )
                }
            } else {
                items(visible, key = { it.id }) { call ->
                    CompanyCallCard(call) {
                        selectedCall = call
                    }
                }
            }

            item { Spacer(Modifier.height(24.dp)) }
        }
    }

    selectedCall?.let { call ->
        AlertDialog(
            onDismissRequest = { selectedCall = null },
            containerColor = GuinchouSurface,
            shape = RoundedCornerShape(22.dp),
            title = {
                Column {
                    Text(
                        call.serviceType,
                        color = GuinchouWhite,
                        fontWeight = FontWeight.Bold
                    )
                    Text(call.code, color = GuinchouGray, fontSize = 12.sp)
                }
            },
            text = {
                Column {
                    DialogInfo("Cliente", call.customerName)
                    DialogInfo("Veículo", call.vehicle)
                    DialogInfo("Origem", call.pickupAddress)
                    DialogInfo("Destino", call.destinationAddress)
                    DialogInfo("Distância", call.distance)
                    DialogInfo("Valor", money(call.value))
                    call.assignedDriver?.let {
                        DialogInfo("Motorista", it)
                    }
                }
            },
            confirmButton = {
                if (call.status == CompanyCallStatus.AVAILABLE) {
                    Button(
                        onClick = { acceptCall(call) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GuinchouGreen,
                            contentColor = Color.Black
                        )
                    ) {
                        Text("Aceitar chamado", fontWeight = FontWeight.Bold)
                    }
                } else {
                    Button(
                        onClick = { selectedCall = null },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GuinchouGreen,
                            contentColor = Color.Black
                        )
                    ) {
                        Text("Fechar", fontWeight = FontWeight.Bold)
                    }
                }
            },
            dismissButton = {
                if (call.status == CompanyCallStatus.AVAILABLE) {
                    TextButton(onClick = { selectedCall = null }) {
                        Text("Agora não", color = GuinchouWhite)
                    }
                }
            }
        )
    }
}

/* =============================================================
 * COMPANY MANAGEMENT
 * ============================================================= */

@Composable
fun CompanyManagementScreen(
    onHomeClick: () -> Unit = {},
    onCallsClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onDriversClick: () -> Unit = {},
    onFleetClick: () -> Unit = {},
    onFinanceClick: () -> Unit = {},
    onDriverInviteClick: () -> Unit = {},
    onTowTruckInviteClick: () -> Unit = {}
) {
    CompanySimpleScaffold(
        selected = CompanyBottomDestination.MANAGEMENT,
        onHomeClick = onHomeClick,
        onCallsClick = onCallsClick,
        onManagementClick = {},
        onProfileClick = onProfileClick
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            Text(
                "Gestão",
                color = GuinchouWhite,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                "Administre os principais recursos da empresa.",
                color = GuinchouGray,
                fontSize = 13.sp
            )

            Spacer(Modifier.height(22.dp))
            SectionTitle("Operação")

            Spacer(Modifier.height(12.dp))

            ManagementLargeCard(
                Icons.Default.Groups,
                "Motoristas",
                "Visualize a equipe, aprove cadastros e acompanhe a situação dos funcionários.",
                "Gerenciar motoristas",
                onDriversClick
            )

            Spacer(Modifier.height(12.dp))

            ManagementLargeCard(
                Icons.Default.LocalShipping,
                "Frota",
                "Acompanhe veículos, documentos, manutenção e responsáveis.",
                "Gerenciar frota",
                onFleetClick
            )

            Spacer(Modifier.height(12.dp))

            ManagementLargeCard(
                Icons.Default.SupportAgent,
                "Chamados",
                "Acesse rapidamente a operação e os atendimentos em andamento.",
                "Abrir chamados",
                onCallsClick
            )

            Spacer(Modifier.height(12.dp))

            ManagementLargeCard(
                Icons.Default.AccountBalanceWallet,
                "Financeiro",
                "Consulte faturamento, repasses e movimentações da empresa.",
                "Abrir financeiro",
                onFinanceClick
            )

            Spacer(Modifier.height(26.dp))
            SectionTitle("Cadastros rápidos")
            Spacer(Modifier.height(12.dp))

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                QuickActionCard(
                    Modifier.weight(1f),
                    Icons.Default.PersonAdd,
                    "Novo motorista",
                    onDriverInviteClick
                )
                QuickActionCard(
                    Modifier.weight(1f),
                    Icons.Default.QrCode2,
                    "Novo guincho",
                    onTowTruckInviteClick
                )
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

/* =============================================================
 * COMPANY FINANCE
 * ============================================================= */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompanyFinanceScreen(
    onBackClick: () -> Unit = {},
    onHomeClick: () -> Unit = {},
    onCallsClick: () -> Unit = {},
    onManagementClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    val transactions = remember {
        mutableStateListOf(
            FinanceTransaction(1, "Chamado #GCH-1034", "Acidente / colisão", "Hoje, 10:42", 200.0, true),
            FinanceTransaction(2, "Chamado #GCH-1031", "Transporte de veículo", "Ontem, 18:10", 180.0, true),
            FinanceTransaction(3, "Chamado #GCH-1028", "Pane mecânica", "02/10/2026", 160.0, false),
            FinanceTransaction(4, "Chamado #GCH-1025", "Pane elétrica", "01/10/2026", 145.0, true)
        )
    }

    var filter by remember { mutableStateOf(FinanceFilter.ALL) }
    var showPayoutInfo by remember { mutableStateOf(false) }

    val visible = when (filter) {
        FinanceFilter.ALL -> transactions
        FinanceFilter.RECEIVED -> transactions.filter { it.received }
        FinanceFilter.PENDING -> transactions.filter { !it.received }
    }

    val received = transactions.filter { it.received }.sumOf { it.value }
    val pending = transactions.filter { !it.received }.sumOf { it.value }
    val total = transactions.sumOf { it.value }

    CompanyScaffold(
        title = "Financeiro",
        subtitle = "Receitas e repasses",
        selected = CompanyBottomDestination.MANAGEMENT,
        onBackClick = onBackClick,
        onHomeClick = onHomeClick,
        onCallsClick = onCallsClick,
        onManagementClick = onManagementClick,
        onProfileClick = onProfileClick
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Spacer(Modifier.height(8.dp))

                Surface(
                    Modifier.fillMaxWidth(),
                    color = GuinchouGreen.copy(alpha = 0.07f),
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(1.dp, GuinchouGreen.copy(alpha = 0.3f))
                ) {
                    Column(Modifier.padding(18.dp)) {
                        Text("Faturamento do período", color = GuinchouGray, fontSize = 12.sp)
                        Spacer(Modifier.height(5.dp))
                        Text(
                            money(total),
                            color = GuinchouWhite,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(16.dp))
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            FinanceMiniCard(
                                Modifier.weight(1f),
                                "Recebido",
                                money(received),
                                true
                            )
                            FinanceMiniCard(
                                Modifier.weight(1f),
                                "Pendente",
                                money(pending),
                                false
                            )
                        }
                        Spacer(Modifier.height(14.dp))
                        Button(
                            onClick = { showPayoutInfo = true },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = GuinchouGreen,
                                contentColor = Color.Black
                            )
                        ) {
                            Icon(Icons.Default.Payments, null)
                            Spacer(Modifier.width(8.dp))
                            Text("Informações de repasse", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    item {
                        SimpleFilterChip("Todos", filter == FinanceFilter.ALL) {
                            filter = FinanceFilter.ALL
                        }
                    }
                    item {
                        SimpleFilterChip("Recebidos", filter == FinanceFilter.RECEIVED) {
                            filter = FinanceFilter.RECEIVED
                        }
                    }
                    item {
                        SimpleFilterChip("Pendentes", filter == FinanceFilter.PENDING) {
                            filter = FinanceFilter.PENDING
                        }
                    }
                }
            }

            item {
                SectionTitle("Movimentações")
            }

            items(visible, key = { it.id }) { transaction ->
                FinanceTransactionCard(transaction)
            }

            item {
                InfoBanner(
                    "Front-End em construção",
                    "Os valores desta tela são demonstrativos. Os repasses reais serão integrados posteriormente ao backend."
                )
            }

            item { Spacer(Modifier.height(24.dp)) }
        }
    }

    if (showPayoutInfo) {
        AlertDialog(
            onDismissRequest = { showPayoutInfo = false },
            containerColor = GuinchouSurface,
            title = {
                Text(
                    "Repasses",
                    color = GuinchouWhite,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    "Nesta fase do Front-End, a tela apenas demonstra o fluxo. Quando o financeiro for integrado, aqui serão exibidos conta de recebimento, calendário de repasses e comprovantes.",
                    color = GuinchouGray,
                    lineHeight = 19.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = { showPayoutInfo = false },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GuinchouGreen,
                        contentColor = Color.Black
                    )
                ) {
                    Text("Entendi", fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}

/* =============================================================
 * COMPANY DRIVERS
 * ============================================================= */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompanyDriversScreen(
    onBackClick: () -> Unit = {},
    onInviteClick: () -> Unit = {},
    onHomeClick: () -> Unit = {},
    onCallsClick: () -> Unit = {},
    onManagementClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    val drivers = remember {
        mutableStateListOf(
            CompanyDriver(1, "Carlos Henrique", "(61) 99999-1001", "01234567890", "D", "ABC1D23", DriverStatus.ACTIVE),
            CompanyDriver(2, "Marcos Silva", "(61) 99999-1002", "11234567890", "D", "DEF4G56", DriverStatus.ACTIVE),
            CompanyDriver(3, "João Pedro", "(61) 99999-1003", "21234567890", "E", null, DriverStatus.PENDING),
            CompanyDriver(4, "Paulo Mendes", "(61) 99999-1004", "31234567890", "D", null, DriverStatus.SUSPENDED)
        )
    }

    var query by remember { mutableStateOf("") }
    var selected by remember { mutableStateOf<CompanyDriver?>(null) }

    val visible = drivers.filter {
        query.isBlank() ||
                it.name.contains(query, ignoreCase = true) ||
                it.phone.contains(query, ignoreCase = true)
    }

    fun updateStatus(driver: CompanyDriver, status: DriverStatus) {
        val index = drivers.indexOfFirst { it.id == driver.id }
        if (index >= 0) {
            drivers[index] = driver.copy(status = status)
        }
        selected = null
    }

    CompanyScaffold(
        title = "Motoristas",
        subtitle = "Equipe da empresa",
        selected = CompanyBottomDestination.MANAGEMENT,
        onBackClick = onBackClick,
        onHomeClick = onHomeClick,
        onCallsClick = onCallsClick,
        onManagementClick = onManagementClick,
        onProfileClick = onProfileClick
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Spacer(Modifier.height(8.dp))

                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    SummaryCounter(
                        Modifier.weight(1f),
                        drivers.count { it.status == DriverStatus.ACTIVE },
                        "Ativos",
                        true
                    )
                    SummaryCounter(
                        Modifier.weight(1f),
                        drivers.count { it.status == DriverStatus.PENDING },
                        "Pendentes"
                    )
                    SummaryCounter(
                        Modifier.weight(1f),
                        drivers.count { it.status == DriverStatus.SUSPENDED },
                        "Suspensos"
                    )
                }
            }

            item {
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    leadingIcon = {
                        Icon(Icons.Default.Search, null)
                    },
                    placeholder = {
                        Text("Buscar motorista")
                    },
                    shape = RoundedCornerShape(14.dp),
                    colors = companyTextFieldColors()
                )
            }

            item {
                Button(
                    onClick = onInviteClick,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GuinchouGreen,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(13.dp)
                ) {
                    Icon(Icons.Default.PersonAdd, null)
                    Spacer(Modifier.width(8.dp))
                    Text("Convidar novo motorista", fontWeight = FontWeight.Bold)
                }
            }

            item {
                SectionTitle("Equipe")
            }

            if (visible.isEmpty()) {
                item {
                    EmptyStateCard(
                        Icons.Default.Groups,
                        "Nenhum motorista encontrado",
                        "Tente outro nome ou gere um novo convite."
                    )
                }
            } else {
                items(visible, key = { it.id }) { driver ->
                    DriverCard(driver) {
                        selected = driver
                    }
                }
            }

            item { Spacer(Modifier.height(24.dp)) }
        }
    }

    selected?.let { driver ->
        AlertDialog(
            onDismissRequest = { selected = null },
            containerColor = GuinchouSurface,
            title = {
                Text(
                    driver.name,
                    color = GuinchouWhite,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    DialogInfo("Telefone", driver.phone)
                    DialogInfo("CNH", driver.cnh)
                    DialogInfo("Categoria", driver.category)
                    DialogInfo("Guincho", driver.towTruck ?: "Ainda não vinculado")
                    DialogInfo("Situação", driverStatusText(driver.status))
                }
            },
            confirmButton = {
                when (driver.status) {
                    DriverStatus.PENDING -> {
                        Button(
                            onClick = { updateStatus(driver, DriverStatus.ACTIVE) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = GuinchouGreen,
                                contentColor = Color.Black
                            )
                        ) {
                            Text("Aprovar", fontWeight = FontWeight.Bold)
                        }
                    }

                    DriverStatus.ACTIVE -> {
                        OutlinedButton(
                            onClick = { updateStatus(driver, DriverStatus.SUSPENDED) },
                            border = BorderStroke(1.dp, GuinchouBorder)
                        ) {
                            Text("Suspender", color = GuinchouWhite)
                        }
                    }

                    DriverStatus.SUSPENDED -> {
                        Button(
                            onClick = { updateStatus(driver, DriverStatus.ACTIVE) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = GuinchouGreen,
                                contentColor = Color.Black
                            )
                        ) {
                            Text("Reativar", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = { selected = null }) {
                    Text("Fechar", color = GuinchouGray)
                }
            }
        )
    }
}

/* =============================================================
 * COMPANY FLEET
 * ============================================================= */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompanyFleetScreen(
    onBackClick: () -> Unit = {},
    onInviteClick: () -> Unit = {},
    onHomeClick: () -> Unit = {},
    onCallsClick: () -> Unit = {},
    onManagementClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    val vehicles = remember {
        mutableStateListOf(
            CompanyVehicle(1, "ABC1D23", "Mercedes-Benz Accelo", "2022", "Plataforma", "4.500 kg", "Carlos Henrique", FleetStatus.ACTIVE),
            CompanyVehicle(2, "DEF4G56", "Iveco Daily", "2021", "Plataforma", "3.500 kg", "Marcos Silva", FleetStatus.ACTIVE),
            CompanyVehicle(3, "GHI7J89", "Volkswagen Delivery", "2020", "Guincho", "5.000 kg", null, FleetStatus.REVIEW),
            CompanyVehicle(4, "JKL1M23", "Ford Cargo", "2019", "Pesado", "8.000 kg", null, FleetStatus.MAINTENANCE)
        )
    }

    var query by remember { mutableStateOf("") }
    var selected by remember { mutableStateOf<CompanyVehicle?>(null) }

    val visible = vehicles.filter {
        query.isBlank() ||
                it.plate.contains(query, ignoreCase = true) ||
                it.model.contains(query, ignoreCase = true)
    }

    fun updateStatus(vehicle: CompanyVehicle, status: FleetStatus) {
        val index = vehicles.indexOfFirst { it.id == vehicle.id }
        if (index >= 0) {
            vehicles[index] = vehicle.copy(status = status)
        }
        selected = null
    }

    CompanyScaffold(
        title = "Frota",
        subtitle = "Veículos da empresa",
        selected = CompanyBottomDestination.MANAGEMENT,
        onBackClick = onBackClick,
        onHomeClick = onHomeClick,
        onCallsClick = onCallsClick,
        onManagementClick = onManagementClick,
        onProfileClick = onProfileClick
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Spacer(Modifier.height(8.dp))
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    SummaryCounter(
                        Modifier.weight(1f),
                        vehicles.count { it.status == FleetStatus.ACTIVE },
                        "Ativos",
                        true
                    )
                    SummaryCounter(
                        Modifier.weight(1f),
                        vehicles.count { it.status == FleetStatus.REVIEW },
                        "Em análise"
                    )
                    SummaryCounter(
                        Modifier.weight(1f),
                        vehicles.count { it.status == FleetStatus.MAINTENANCE },
                        "Manutenção"
                    )
                }
            }

            item {
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    leadingIcon = { Icon(Icons.Default.Search, null) },
                    placeholder = { Text("Buscar placa ou modelo") },
                    shape = RoundedCornerShape(14.dp),
                    colors = companyTextFieldColors()
                )
            }

            item {
                Button(
                    onClick = onInviteClick,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GuinchouGreen,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(13.dp)
                ) {
                    Icon(Icons.Default.QrCode2, null)
                    Spacer(Modifier.width(8.dp))
                    Text("Cadastrar novo guincho", fontWeight = FontWeight.Bold)
                }
            }

            item {
                SectionTitle("Veículos")
            }

            if (visible.isEmpty()) {
                item {
                    EmptyStateCard(
                        Icons.Default.LocalShipping,
                        "Nenhum veículo encontrado",
                        "Tente outra placa ou gere um novo cadastro."
                    )
                }
            } else {
                items(visible, key = { it.id }) { vehicle ->
                    FleetCard(vehicle) {
                        selected = vehicle
                    }
                }
            }

            item { Spacer(Modifier.height(24.dp)) }
        }
    }

    selected?.let { vehicle ->
        AlertDialog(
            onDismissRequest = { selected = null },
            containerColor = GuinchouSurface,
            title = {
                Column {
                    Text(
                        vehicle.model,
                        color = GuinchouWhite,
                        fontWeight = FontWeight.Bold
                    )
                    Text(vehicle.plate, color = GuinchouGreen)
                }
            },
            text = {
                Column {
                    DialogInfo("Ano", vehicle.year)
                    DialogInfo("Tipo", vehicle.type)
                    DialogInfo("Capacidade", vehicle.capacity)
                    DialogInfo("Motorista", vehicle.assignedDriver ?: "Não vinculado")
                    DialogInfo("Situação", fleetStatusText(vehicle.status))
                }
            },
            confirmButton = {
                when (vehicle.status) {
                    FleetStatus.REVIEW -> {
                        Button(
                            onClick = { updateStatus(vehicle, FleetStatus.ACTIVE) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = GuinchouGreen,
                                contentColor = Color.Black
                            )
                        ) {
                            Text("Aprovar veículo", fontWeight = FontWeight.Bold)
                        }
                    }

                    FleetStatus.ACTIVE -> {
                        OutlinedButton(
                            onClick = { updateStatus(vehicle, FleetStatus.MAINTENANCE) },
                            border = BorderStroke(1.dp, GuinchouBorder)
                        ) {
                            Text("Enviar à manutenção", color = GuinchouWhite)
                        }
                    }

                    FleetStatus.MAINTENANCE -> {
                        Button(
                            onClick = { updateStatus(vehicle, FleetStatus.ACTIVE) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = GuinchouGreen,
                                contentColor = Color.Black
                            )
                        ) {
                            Text("Marcar como ativo", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = { selected = null }) {
                    Text("Fechar", color = GuinchouGray)
                }
            }
        )
    }
}

/* =============================================================
 * COMPANY PROFILE
 * ============================================================= */

@Composable
fun CompanyProfileScreen(
    ownerName: String = "João da Silva",
    companyName: String = "Empresa de Guinchos",
    companyCnpj: String = "12.345.678/0001-90",
    companyPhone: String = "(61) 99999-9999",
    onHomeClick: () -> Unit = {},
    onCallsClick: () -> Unit = {},
    onManagementClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {}
) {
    var callsNotifications by remember { mutableStateOf(true) }
    var teamNotifications by remember { mutableStateOf(true) }
    var financialNotifications by remember { mutableStateOf(true) }
    var showLogoutDialog by remember { mutableStateOf(false) }
    var profileDialog by remember { mutableStateOf<String?>(null) }

    CompanySimpleScaffold(
        selected = CompanyBottomDestination.PROFILE,
        onHomeClick = onHomeClick,
        onCallsClick = onCallsClick,
        onManagementClick = onManagementClick,
        onProfileClick = {}
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier.size(58.dp),
                    shape = RoundedCornerShape(18.dp),
                    color = GuinchouGreen.copy(alpha = 0.12f)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Default.Business,
                            contentDescription = null,
                            tint = GuinchouGreen,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }

                Spacer(Modifier.width(14.dp))

                Column(Modifier.weight(1f)) {
                    Text(
                        companyName,
                        color = GuinchouWhite,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Responsável: $ownerName",
                        color = GuinchouGray,
                        fontSize = 12.sp
                    )
                }

                IconButton(onClick = onNotificationsClick) {
                    Icon(
                        Icons.Default.Notifications,
                        contentDescription = "Notificações",
                        tint = GuinchouWhite
                    )
                }
            }

            Spacer(Modifier.height(24.dp))
            SectionTitle("Dados da empresa")
            Spacer(Modifier.height(10.dp))

            ProfileInfoCard(Icons.Default.Business, "CNPJ", companyCnpj)
            ProfileInfoCard(Icons.Default.Phone, "Telefone", companyPhone)
            ProfileInfoCard(Icons.Default.Person, "Responsável", ownerName)
            ProfileInfoCard(Icons.Default.VerifiedUser, "Situação", "Conta ativa e verificada")

            Spacer(Modifier.height(22.dp))
            SectionTitle("Preferências")
            Spacer(Modifier.height(10.dp))

            SettingSwitchRow(
                "Chamados",
                "Receber alertas sobre novos atendimentos.",
                callsNotifications
            ) { callsNotifications = it }

            SettingSwitchRow(
                "Equipe",
                "Receber atualizações de cadastros de motoristas.",
                teamNotifications
            ) { teamNotifications = it }

            SettingSwitchRow(
                "Financeiro",
                "Receber atualizações sobre repasses.",
                financialNotifications
            ) { financialNotifications = it }

            Spacer(Modifier.height(22.dp))
            SectionTitle("Conta")
            Spacer(Modifier.height(10.dp))

            ProfileActionRow(
                Icons.Default.Description,
                "Documentos",
                "Documentos enviados no cadastro",
                { profileDialog = "DOCUMENTS" }
            )

            ProfileActionRow(
                Icons.Default.Settings,
                "Configurações",
                "Preferências gerais da conta",
                { profileDialog = "SETTINGS" }
            )

            ProfileActionRow(
                Icons.Default.SupportAgent,
                "Suporte",
                "Ajuda e atendimento Guinchou",
                { profileDialog = "SUPPORT" }
            )

            Spacer(Modifier.height(12.dp))

            OutlinedButton(
                onClick = { showLogoutDialog = true },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(13.dp),
                border = BorderStroke(1.dp, GuinchouBorder)
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.Logout,
                    contentDescription = null,
                    tint = GuinchouWhite
                )
                Spacer(Modifier.width(8.dp))
                Text("Sair da conta", color = GuinchouWhite)
            }

            Spacer(Modifier.height(24.dp))
        }
    }

    profileDialog?.let { type ->
        val title = when (type) {
            "DOCUMENTS" -> "Documentos da empresa"
            "SETTINGS" -> "Configurações"
            else -> "Suporte Guinchou"
        }

        val message = when (type) {
            "DOCUMENTS" ->
                "Nesta área serão exibidos CNPJ, documento do responsável e comprovantes enviados durante o cadastro. A integração com os arquivos reais será feita no backend."

            "SETTINGS" ->
                "As preferências rápidas de notificação já podem ser alteradas nesta tela. Configurações avançadas serão conectadas quando a persistência estiver ativa."

            else ->
                "A central de suporte será ligada aos canais oficiais na etapa de integração. Por enquanto, este modal valida a navegação e o comportamento da interface."
        }

        AlertDialog(
            onDismissRequest = { profileDialog = null },
            containerColor = GuinchouSurface,
            title = {
                Text(
                    title,
                    color = GuinchouWhite,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    message,
                    color = GuinchouGray,
                    lineHeight = 19.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = { profileDialog = null },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GuinchouGreen,
                        contentColor = Color.Black
                    )
                ) {
                    Text("Fechar", fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            containerColor = GuinchouSurface,
            title = {
                Text(
                    "Sair da conta?",
                    color = GuinchouWhite,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    "Você precisará entrar novamente para acessar o painel empresarial.",
                    color = GuinchouGray
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutDialog = false
                        onLogoutClick()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GuinchouGreen,
                        contentColor = Color.Black
                    )
                ) {
                    Text("Sair", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancelar", color = GuinchouGray)
                }
            }
        )
    }
}

/* =============================================================
 * COMPANY BOTTOM BAR
 * ============================================================= */

@Composable
fun CompanyBottomBar(
    selectedDestination: CompanyBottomDestination,
    onHomeClick: () -> Unit,
    onCallsClick: () -> Unit,
    onManagementClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    NavigationBar(
        containerColor = GuinchouSurface,
        tonalElevation = 0.dp
    ) {
        CompanyNavItem(
            selectedDestination == CompanyBottomDestination.HOME,
            Icons.Default.Home,
            "Início",
            onHomeClick
        )

        CompanyNavItem(
            selectedDestination == CompanyBottomDestination.CALLS,
            Icons.Default.SupportAgent,
            "Chamados",
            onCallsClick
        )

        CompanyNavItem(
            selectedDestination == CompanyBottomDestination.MANAGEMENT,
            Icons.Default.BusinessCenter,
            "Gestão",
            onManagementClick
        )

        CompanyNavItem(
            selectedDestination == CompanyBottomDestination.PROFILE,
            Icons.Default.Person,
            "Perfil",
            onProfileClick
        )
    }
}

/* =============================================================
 * SCAFFOLDS
 * ============================================================= */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CompanyScaffold(
    title: String,
    subtitle: String,
    selected: CompanyBottomDestination,
    onBackClick: () -> Unit,
    onHomeClick: () -> Unit,
    onCallsClick: () -> Unit,
    onManagementClick: () -> Unit,
    onProfileClick: () -> Unit,
    content: @Composable (androidx.compose.foundation.layout.PaddingValues) -> Unit
) {
    Scaffold(
        containerColor = GuinchouBackground,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = GuinchouBackground
                ),
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = GuinchouWhite
                        )
                    }
                },
                title = {
                    Column {
                        Text(
                            title,
                            color = GuinchouWhite,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            subtitle,
                            color = GuinchouGray,
                            fontSize = 12.sp
                        )
                    }
                }
            )
        },
        bottomBar = {
            CompanyBottomBar(
                selected,
                onHomeClick,
                onCallsClick,
                onManagementClick,
                onProfileClick
            )
        },
        content = content
    )
}

@Composable
private fun CompanySimpleScaffold(
    selected: CompanyBottomDestination,
    onHomeClick: () -> Unit,
    onCallsClick: () -> Unit,
    onManagementClick: () -> Unit,
    onProfileClick: () -> Unit,
    content: @Composable (androidx.compose.foundation.layout.PaddingValues) -> Unit
) {
    Scaffold(
        containerColor = GuinchouBackground,
        bottomBar = {
            CompanyBottomBar(
                selected,
                onHomeClick,
                onCallsClick,
                onManagementClick,
                onProfileClick
            )
        },
        content = content
    )
}

/* =============================================================
 * COMPONENTES
 * ============================================================= */

@Composable
private fun RowScope.CompanyNavItem(
    selected: Boolean,
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    NavigationBarItem(
        selected = selected,
        onClick = onClick,
        icon = {
            Icon(icon, contentDescription = label)
        },
        label = {
            Text(label)
        },
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = GuinchouGreen,
            selectedTextColor = GuinchouGreen,
            indicatorColor = GuinchouGreen.copy(alpha = 0.10f),
            unselectedIconColor = GuinchouGray,
            unselectedTextColor = GuinchouGray
        )
    )
}

@Composable
private fun StatusBanner(
    title: String,
    subtitle: String
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = GuinchouGreen.copy(alpha = 0.07f),
        shape = RoundedCornerShape(17.dp),
        border = BorderStroke(1.dp, GuinchouGreen.copy(alpha = 0.30f))
    ) {
        Row(
            Modifier.padding(15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(9.dp),
                shape = CircleShape,
                color = GuinchouGreen
            ) {}
            Spacer(Modifier.width(10.dp))
            Column {
                Text(
                    title,
                    color = GuinchouWhite,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    subtitle,
                    color = GuinchouGray,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text,
        color = GuinchouWhite,
        fontSize = 17.sp,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun MetricCard(
    modifier: Modifier,
    icon: ImageVector,
    value: String,
    label: String
) {
    Surface(
        modifier = modifier,
        color = GuinchouSurface,
        shape = RoundedCornerShape(17.dp),
        border = BorderStroke(1.dp, GuinchouBorder)
    ) {
        Column(Modifier.padding(16.dp)) {
            Icon(icon, null, tint = GuinchouGreen, modifier = Modifier.size(24.dp))
            Spacer(Modifier.height(12.dp))
            Text(
                value,
                color = GuinchouWhite,
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold
            )
            Text(label, color = GuinchouGray, fontSize = 12.sp)
        }
    }
}

@Composable
private fun InviteCard(
    icon: ImageVector,
    title: String,
    description: String,
    buttonText: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = GuinchouSurface,
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.dp, GuinchouBorder)
    ) {
        Column(Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    color = GuinchouGreen.copy(alpha = 0.11f),
                    shape = RoundedCornerShape(13.dp)
                ) {
                    Icon(
                        icon,
                        null,
                        tint = GuinchouGreen,
                        modifier = Modifier.padding(12.dp).size(25.dp)
                    )
                }
                Spacer(Modifier.width(13.dp))
                Column(Modifier.weight(1f)) {
                    Text(
                        title,
                        color = GuinchouWhite,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(3.dp))
                    Text(
                        description,
                        color = GuinchouGray,
                        fontSize = 12.sp,
                        lineHeight = 17.sp
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = onClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(13.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = GuinchouGreen,
                    contentColor = Color.Black
                )
            ) {
                Icon(Icons.Default.QrCode2, null)
                Spacer(Modifier.width(8.dp))
                Text(buttonText, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun ManagementRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
            .clickable(onClick = onClick),
        color = GuinchouSurface,
        shape = RoundedCornerShape(15.dp),
        border = BorderStroke(1.dp, GuinchouBorder)
    ) {
        Row(
            Modifier.fillMaxWidth().padding(15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                color = GuinchouGreen.copy(alpha = 0.08f),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(
                    icon,
                    null,
                    tint = GuinchouGreen,
                    modifier = Modifier.padding(9.dp).size(21.dp)
                )
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    title,
                    color = GuinchouWhite,
                    fontWeight = FontWeight.SemiBold
                )
                Text(subtitle, color = GuinchouGray, fontSize = 12.sp)
            }
            Icon(Icons.Default.ChevronRight, null, tint = GuinchouGray)
        }
    }
}

@Composable
private fun ManagementLargeCard(
    icon: ImageVector,
    title: String,
    description: String,
    buttonText: String,
    onClick: () -> Unit
) {
    Surface(
        Modifier.fillMaxWidth(),
        color = GuinchouSurface,
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.dp, GuinchouBorder)
    ) {
        Column(Modifier.padding(17.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    color = GuinchouGreen.copy(alpha = 0.10f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        icon,
                        null,
                        tint = GuinchouGreen,
                        modifier = Modifier.padding(10.dp).size(23.dp)
                    )
                }
                Spacer(Modifier.width(12.dp))
                Text(
                    title,
                    color = GuinchouWhite,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(Modifier.height(10.dp))
            Text(
                description,
                color = GuinchouGray,
                fontSize = 12.sp,
                lineHeight = 18.sp
            )
            Spacer(Modifier.height(12.dp))
            OutlinedButton(
                onClick = onClick,
                modifier = Modifier.fillMaxWidth(),
                border = BorderStroke(1.dp, GuinchouBorder)
            ) {
                Text(buttonText, color = GuinchouGreen)
                Spacer(Modifier.weight(1f))
                Icon(Icons.Default.ChevronRight, null, tint = GuinchouGreen)
            }
        }
    }
}

@Composable
private fun QuickActionCard(
    modifier: Modifier,
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier.clickable(onClick = onClick),
        color = GuinchouSurface,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, GuinchouBorder)
    ) {
        Column(Modifier.padding(16.dp)) {
            Icon(icon, null, tint = GuinchouGreen, modifier = Modifier.size(25.dp))
            Spacer(Modifier.height(11.dp))
            Text(
                title,
                color = GuinchouWhite,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun SummaryCounter(
    modifier: Modifier,
    value: Int,
    label: String,
    highlighted: Boolean = false
) {
    Surface(
        modifier,
        color = if (highlighted) GuinchouGreen.copy(alpha = 0.07f) else GuinchouSurface,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(
            1.dp,
            if (highlighted) GuinchouGreen.copy(alpha = 0.35f) else GuinchouBorder
        )
    ) {
        Column(Modifier.padding(14.dp)) {
            Text(
                value.toString(),
                color = if (highlighted) GuinchouGreen else GuinchouWhite,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Text(label, color = GuinchouGray, fontSize = 11.sp, lineHeight = 14.sp)
        }
    }
}

@Composable
private fun CallFilterChip(
    title: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    SimpleFilterChip(title, selected, onClick)
}

@Composable
private fun SimpleFilterChip(
    title: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(title) },
        colors = FilterChipDefaults.filterChipColors(
            containerColor = GuinchouSurface,
            labelColor = GuinchouGray,
            selectedContainerColor = GuinchouGreen.copy(alpha = 0.13f),
            selectedLabelColor = GuinchouGreen
        )
    )
}

@Composable
private fun CompanyCallCard(
    call: CompanyCall,
    onClick: () -> Unit
) {
    val isNew = call.status == CompanyCallStatus.AVAILABLE

    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        color = if (isNew) GuinchouGreen.copy(alpha = 0.045f) else GuinchouSurface,
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(
            1.dp,
            if (isNew) GuinchouGreen.copy(alpha = 0.30f) else GuinchouBorder
        )
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = GuinchouGreen.copy(alpha = 0.11f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        Icons.Default.SupportAgent,
                        null,
                        tint = GuinchouGreen,
                        modifier = Modifier.padding(10.dp).size(22.dp)
                    )
                }
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(
                        call.serviceType,
                        color = GuinchouWhite,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(call.code, color = GuinchouGray, fontSize = 11.sp)
                }
                CallStatusBadge(call.status)
            }

            Spacer(Modifier.height(14.dp))
            Text(call.customerName, color = GuinchouWhite, fontSize = 13.sp)
            Text(call.vehicle, color = GuinchouGray, fontSize = 12.sp)

            Spacer(Modifier.height(12.dp))
            HorizontalDivider(color = GuinchouBorder.copy(alpha = 0.7f))
            Spacer(Modifier.height(12.dp))

            AddressLine(Icons.Default.LocationOn, call.pickupAddress, true)
            Spacer(Modifier.height(8.dp))
            AddressLine(Icons.Default.Navigation, call.destinationAddress)

            Spacer(Modifier.height(14.dp))

            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Schedule,
                    null,
                    tint = GuinchouGray,
                    modifier = Modifier.size(15.dp)
                )
                Spacer(Modifier.width(5.dp))
                Text(call.requestedAt, color = GuinchouGray, fontSize = 11.sp)
                Spacer(Modifier.weight(1f))
                Text(call.distance, color = GuinchouGray, fontSize = 12.sp)
                Spacer(Modifier.width(12.dp))
                Text(
                    money(call.value),
                    color = GuinchouGreen,
                    fontWeight = FontWeight.Bold
                )
            }

            call.assignedDriver?.let {
                Spacer(Modifier.height(12.dp))
                InfoBanner("Motorista", it)
            }
        }
    }
}

@Composable
private fun CallStatusBadge(status: CompanyCallStatus) {
    val text = when (status) {
        CompanyCallStatus.AVAILABLE -> "Novo"
        CompanyCallStatus.IN_PROGRESS -> "Em andamento"
        CompanyCallStatus.COMPLETED -> "Concluído"
    }

    val icon = when (status) {
        CompanyCallStatus.AVAILABLE -> Icons.Default.WarningAmber
        CompanyCallStatus.IN_PROGRESS -> Icons.Default.SupportAgent
        CompanyCallStatus.COMPLETED -> Icons.Default.CheckCircle
    }

    AssistChip(
        onClick = {},
        label = { Text(text, fontSize = 10.sp) },
        leadingIcon = {
            Icon(icon, null, modifier = Modifier.size(14.dp))
        },
        colors = AssistChipDefaults.assistChipColors(
            containerColor = if (status == CompanyCallStatus.AVAILABLE) {
                GuinchouGreen.copy(alpha = 0.12f)
            } else {
                Color.White.copy(alpha = 0.05f)
            },
            labelColor = if (status == CompanyCallStatus.AVAILABLE) {
                GuinchouGreen
            } else {
                GuinchouWhite
            },
            leadingIconContentColor = if (status == CompanyCallStatus.AVAILABLE) {
                GuinchouGreen
            } else {
                GuinchouWhite
            }
        ),
        border = null
    )
}

@Composable
private fun AddressLine(
    icon: ImageVector,
    text: String,
    highlighted: Boolean = false
) {
    Row(verticalAlignment = Alignment.Top) {
        Icon(
            icon,
            null,
            tint = if (highlighted) GuinchouGreen else GuinchouGray,
            modifier = Modifier.size(17.dp)
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text,
            color = GuinchouWhite,
            fontSize = 12.sp,
            lineHeight = 17.sp
        )
    }
}

@Composable
private fun FinanceMiniCard(
    modifier: Modifier,
    title: String,
    value: String,
    positive: Boolean
) {
    Surface(
        modifier,
        color = Color.Black.copy(alpha = 0.12f),
        shape = RoundedCornerShape(13.dp)
    ) {
        Column(Modifier.padding(12.dp)) {
            Text(title, color = GuinchouGray, fontSize = 11.sp)
            Spacer(Modifier.height(3.dp))
            Text(
                value,
                color = if (positive) GuinchouGreen else GuinchouWhite,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun FinanceTransactionCard(transaction: FinanceTransaction) {
    Surface(
        Modifier.fillMaxWidth(),
        color = GuinchouSurface,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, GuinchouBorder)
    ) {
        Row(
            Modifier.padding(15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                color = GuinchouGreen.copy(alpha = 0.09f),
                shape = RoundedCornerShape(11.dp)
            ) {
                Icon(
                    Icons.Default.Payments,
                    null,
                    tint = GuinchouGreen,
                    modifier = Modifier.padding(9.dp).size(21.dp)
                )
            }

            Spacer(Modifier.width(12.dp))

            Column(Modifier.weight(1f)) {
                Text(
                    transaction.title,
                    color = GuinchouWhite,
                    fontWeight = FontWeight.SemiBold
                )
                Text(transaction.subtitle, color = GuinchouGray, fontSize = 11.sp)
                Text(transaction.date, color = GuinchouGray, fontSize = 10.sp)
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    money(transaction.value),
                    color = GuinchouGreen,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    if (transaction.received) "Recebido" else "Pendente",
                    color = if (transaction.received) GuinchouGreen else GuinchouGray,
                    fontSize = 10.sp
                )
            }
        }
    }
}

@Composable
private fun DriverCard(
    driver: CompanyDriver,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        color = GuinchouSurface,
        shape = RoundedCornerShape(17.dp),
        border = BorderStroke(1.dp, GuinchouBorder)
    ) {
        Row(
            Modifier.padding(15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                color = GuinchouGreen.copy(alpha = 0.10f),
                shape = RoundedCornerShape(13.dp)
            ) {
                Icon(
                    Icons.Default.Person,
                    null,
                    tint = GuinchouGreen,
                    modifier = Modifier.padding(11.dp).size(23.dp)
                )
            }

            Spacer(Modifier.width(12.dp))

            Column(Modifier.weight(1f)) {
                Text(
                    driver.name,
                    color = GuinchouWhite,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "CNH ${driver.category} • ${driver.phone}",
                    color = GuinchouGray,
                    fontSize = 11.sp
                )
                driver.towTruck?.let {
                    Text(
                        "Guincho $it",
                        color = GuinchouGray,
                        fontSize = 11.sp
                    )
                }
            }

            StatusPill(
                driverStatusText(driver.status),
                driver.status == DriverStatus.ACTIVE
            )
        }
    }
}

@Composable
private fun FleetCard(
    vehicle: CompanyVehicle,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        color = GuinchouSurface,
        shape = RoundedCornerShape(17.dp),
        border = BorderStroke(1.dp, GuinchouBorder)
    ) {
        Row(
            Modifier.padding(15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                color = GuinchouGreen.copy(alpha = 0.10f),
                shape = RoundedCornerShape(13.dp)
            ) {
                Icon(
                    Icons.Default.LocalShipping,
                    null,
                    tint = GuinchouGreen,
                    modifier = Modifier.padding(11.dp).size(23.dp)
                )
            }

            Spacer(Modifier.width(12.dp))

            Column(Modifier.weight(1f)) {
                Text(
                    vehicle.model,
                    color = GuinchouWhite,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "${vehicle.plate} • ${vehicle.year} • ${vehicle.type}",
                    color = GuinchouGray,
                    fontSize = 11.sp
                )
                Text(
                    vehicle.assignedDriver ?: "Sem motorista vinculado",
                    color = GuinchouGray,
                    fontSize = 11.sp
                )
            }

            StatusPill(
                fleetStatusText(vehicle.status),
                vehicle.status == FleetStatus.ACTIVE
            )
        }
    }
}

@Composable
private fun StatusPill(
    text: String,
    highlighted: Boolean
) {
    Surface(
        color = if (highlighted) {
            GuinchouGreen.copy(alpha = 0.12f)
        } else {
            Color.White.copy(alpha = 0.05f)
        },
        shape = RoundedCornerShape(100.dp)
    ) {
        Text(
            text,
            color = if (highlighted) GuinchouGreen else GuinchouGray,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 9.dp, vertical = 6.dp)
        )
    }
}

@Composable
private fun ProfileInfoCard(
    icon: ImageVector,
    title: String,
    value: String
) {
    Surface(
        Modifier
            .fillMaxWidth()
            .padding(bottom = 9.dp),
        color = GuinchouSurface,
        shape = RoundedCornerShape(15.dp),
        border = BorderStroke(1.dp, GuinchouBorder)
    ) {
        Row(
            Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, null, tint = GuinchouGreen, modifier = Modifier.size(22.dp))
            Spacer(Modifier.width(12.dp))
            Column {
                Text(title, color = GuinchouGray, fontSize = 10.sp)
                Text(
                    value,
                    color = GuinchouWhite,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun SettingSwitchRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Surface(
        Modifier
            .fillMaxWidth()
            .padding(bottom = 9.dp),
        color = GuinchouSurface,
        shape = RoundedCornerShape(15.dp),
        border = BorderStroke(1.dp, GuinchouBorder)
    ) {
        Row(
            Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text(
                    title,
                    color = GuinchouWhite,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    subtitle,
                    color = GuinchouGray,
                    fontSize = 11.sp
                )
            }
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.Black,
                    checkedTrackColor = GuinchouGreen,
                    uncheckedThumbColor = GuinchouGray,
                    uncheckedTrackColor = GuinchouBorder
                )
            )
        }
    }
}

@Composable
private fun ProfileActionRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    ManagementRow(icon, title, subtitle, onClick)
}

@Composable
private fun InfoBanner(
    title: String,
    subtitle: String
) {
    Surface(
        Modifier.fillMaxWidth(),
        color = Color.White.copy(alpha = 0.035f),
        shape = RoundedCornerShape(13.dp),
        border = BorderStroke(1.dp, GuinchouBorder)
    ) {
        Row(
            Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Info,
                null,
                tint = GuinchouGreen,
                modifier = Modifier.size(18.dp)
            )
            Spacer(Modifier.width(9.dp))
            Column {
                Text(
                    title,
                    color = GuinchouWhite,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    subtitle,
                    color = GuinchouGray,
                    fontSize = 11.sp,
                    lineHeight = 16.sp
                )
            }
        }
    }
}

@Composable
private fun EmptyStateCard(
    icon: ImageVector,
    title: String,
    subtitle: String
) {
    Surface(
        Modifier.fillMaxWidth(),
        color = GuinchouSurface,
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.dp, GuinchouBorder)
    ) {
        Column(
            Modifier.fillMaxWidth().padding(vertical = 32.dp, horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                color = GuinchouGreen.copy(alpha = 0.10f),
                shape = CircleShape
            ) {
                Icon(
                    icon,
                    null,
                    tint = GuinchouGreen,
                    modifier = Modifier.padding(14.dp).size(28.dp)
                )
            }
            Spacer(Modifier.height(13.dp))
            Text(
                title,
                color = GuinchouWhite,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(3.dp))
            Text(
                subtitle,
                color = GuinchouGray,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun DialogInfo(
    title: String,
    value: String
) {
    Text(title, color = GuinchouGray, fontSize = 11.sp)
    Text(
        value,
        color = GuinchouWhite,
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 18.sp
    )
    Spacer(Modifier.height(10.dp))
}

@Composable
private fun companyTextFieldColors() =
    OutlinedTextFieldDefaults.colors(
        focusedTextColor = GuinchouWhite,
        unfocusedTextColor = GuinchouWhite,
        cursorColor = GuinchouGreen,
        focusedBorderColor = GuinchouGreen,
        unfocusedBorderColor = GuinchouBorder,
        focusedLeadingIconColor = GuinchouGreen,
        unfocusedLeadingIconColor = GuinchouGray,
        focusedPlaceholderColor = GuinchouGray,
        unfocusedPlaceholderColor = GuinchouGray
    )

private fun driverStatusText(status: DriverStatus): String =
    when (status) {
        DriverStatus.PENDING -> "Pendente"
        DriverStatus.ACTIVE -> "Ativo"
        DriverStatus.SUSPENDED -> "Suspenso"
    }

private fun fleetStatusText(status: FleetStatus): String =
    when (status) {
        FleetStatus.REVIEW -> "Em análise"
        FleetStatus.ACTIVE -> "Ativo"
        FleetStatus.MAINTENANCE -> "Manutenção"
    }

private fun money(value: Double): String =
    "R$ ${"%.2f".format(value)}"
