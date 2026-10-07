package com.guinchou.app.ui.screens.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.guinchou.app.ui.theme.GuinchouBackground
import com.guinchou.app.ui.theme.GuinchouBorder
import com.guinchou.app.ui.theme.GuinchouError
import com.guinchou.app.ui.theme.GuinchouGray
import com.guinchou.app.ui.theme.GuinchouGreen
import com.guinchou.app.ui.theme.GuinchouSurface
import com.guinchou.app.ui.theme.GuinchouWhite

/*
 * =============================================================
 * GUINCHOU ADMIN - FRONT-END
 * =============================================================
 * Um único arquivo contendo:
 * Dashboard, Aprovações, Usuários, Chamados, Preços/Taxas,
 * Pagamentos, Repasses, Financeiro, Gestão, Configurações e Footer.
 * Todos os dados e ações são locais/mockados nesta fase.
 */

private enum class AdminBottomDestination { HOME, APPROVALS, FINANCE, MANAGEMENT }
private enum class ApprovalStatus { PENDING, REVIEWING, APPROVED, REJECTED, CORRECTION }
private enum class ApprovalType { INDEPENDENT_DRIVER, COMPANY, COMPANY_DRIVER, TOW_TRUCK }
private enum class ApprovalFilter { ALL, PENDING, REVIEWING, APPROVED, REJECTED }
private enum class UserType { CUSTOMER, DRIVER, COMPANY, COMPANY_DRIVER }
private enum class UserStatus { ACTIVE, SUSPENDED, BLOCKED }
private enum class PaymentStatus { PAID, PROCESSING, PENDING, FAILED, CANCELLED, REFUNDED, ANALYSIS }
private enum class PayoutStatus { WAITING, PROCESSING, PAID, FAILED, BLOCKED }
private enum class CallStatus { NEW, SEARCHING, ACCEPTED, DRIVER_ON_THE_WAY, IN_TRANSIT, COMPLETED, CANCELLED, DISPUTE }
private enum class FinancePeriod { TODAY, SEVEN_DAYS, THIRTY_DAYS, MONTH }

private data class AdminApproval(
    val id: Int,
    val name: String,
    val type: ApprovalType,
    val documentLabel: String,
    val documentNumber: String,
    val sentAt: String,
    val status: ApprovalStatus,
    val details: List<Pair<String, String>>,
    val documents: List<Pair<String, Boolean>>,
    val history: List<String>
)

private data class AdminUser(
    val id: Int,
    val name: String,
    val type: UserType,
    val document: String,
    val phone: String,
    val email: String,
    val status: UserStatus,
    val callsCount: Int,
    val totalPaid: Double
)

private data class AdminCall(
    val id: Int,
    val code: String,
    val customer: String,
    val partner: String,
    val company: String?,
    val vehicle: String,
    val service: String,
    val origin: String,
    val destination: String,
    val grossValue: Double,
    val platformFee: Double,
    val status: CallStatus,
    val updatedAt: String
)

private data class AdminPayment(
    val id: Int,
    val code: String,
    val customer: String,
    val partner: String,
    val method: String,
    val grossValue: Double,
    val platformFee: Double,
    val status: PaymentStatus,
    val date: String
)

private data class AdminPayout(
    val id: Int,
    val partnerName: String,
    val partnerType: String,
    val grossValue: Double,
    val platformFee: Double,
    val netValue: Double,
    val status: PayoutStatus,
    val expectedDate: String,
    val paidDate: String? = null
)

private data class ServicePrice(
    val id: Int,
    val title: String,
    val basePrice: Double,
    val pricePerKm: Double,
    val minimumPrice: Double
)

@Composable
fun AdminHomeScreen(
    adminName: String = "Administrador",
    onApprovalsClick: () -> Unit = {},
    onFinanceClick: () -> Unit = {},
    onManagementClick: () -> Unit = {},
    onUsersClick: () -> Unit = {},
    onCallsClick: () -> Unit = {},
    onPaymentsClick: () -> Unit = {},
    onPayoutsClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {}
) {
    Scaffold(
        containerColor = GuinchouBackground,
        bottomBar = {
            AdminBottomBar(
                selectedDestination = AdminBottomDestination.HOME,
                onHomeClick = {},
                onApprovalsClick = onApprovalsClick,
                onFinanceClick = onFinanceClick,
                onManagementClick = onManagementClick
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Spacer(Modifier.height(10.dp))
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text("GUINCHOU ADMIN", color = GuinchouGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(3.dp))
                        Text("Olá, $adminName", color = GuinchouWhite, fontSize = 25.sp, fontWeight = FontWeight.Bold)
                        Text("Visão geral da plataforma", color = GuinchouGray, fontSize = 13.sp)
                    }
                    Surface(color = GuinchouSurface, shape = RoundedCornerShape(14.dp), border = BorderStroke(1.dp, GuinchouBorder)) {
                        IconButton(onClick = onNotificationsClick) {
                            Icon(Icons.Default.Notifications, contentDescription = "Notificações", tint = GuinchouWhite)
                        }
                    }
                }
            }

            item { SectionTitle("Atenção necessária") }
            item { AttentionCard(Icons.Default.Description, 8, "Documentos pendentes", "Cadastros aguardando análise", onApprovalsClick) }
            item { AttentionCard(Icons.Default.ReportProblem, 2, "Pagamentos com falha", "Transações que precisam de revisão", onPaymentsClick, true) }
            item { AttentionCard(Icons.Default.SyncAlt, 3, "Repasses aguardando", "Parceiros com valores pendentes", onPayoutsClick) }
            item { AttentionCard(Icons.Default.WarningAmber, 1, "Chamado em disputa", "Atendimento aguardando análise", onCallsClick, true) }

            item { SectionTitle("Visão geral") }
            item {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    MetricCard(Modifier.weight(1f), Icons.Default.Groups, "1.248", "Usuários")
                    MetricCard(Modifier.weight(1f), Icons.Default.Badge, "84", "Motoristas")
                }
            }
            item {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    MetricCard(Modifier.weight(1f), Icons.Default.Business, "32", "Empresas")
                    MetricCard(Modifier.weight(1f), Icons.Default.LocalShipping, "113", "Guinchos")
                }
            }
            item {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    MetricCard(Modifier.weight(1f), Icons.Default.SupportAgent, "48", "Chamados hoje")
                    MetricCard(Modifier.weight(1f), Icons.Default.Refresh, "7", "Em andamento")
                }
            }

            item { SectionTitle("Financeiro do mês") }
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = GuinchouGreen.copy(alpha = 0.06f),
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(1.dp, GuinchouGreen.copy(alpha = 0.28f))
                ) {
                    Column(Modifier.padding(18.dp)) {
                        Text("Volume movimentado", color = GuinchouGray, fontSize = 12.sp)
                        Text("R$ 84.350,00", color = GuinchouWhite, fontSize = 27.sp, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(16.dp))
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            FinanceMiniCard(Modifier.weight(1f), "Receita Guinchou", "R$ 12.652,50", true)
                            FinanceMiniCard(Modifier.weight(1f), "Repasses", "R$ 71.697,50", false)
                        }
                        Spacer(Modifier.height(14.dp))
                        OutlinedButton(onClick = onFinanceClick, modifier = Modifier.fillMaxWidth(), border = BorderStroke(1.dp, GuinchouBorder)) {
                            Text("Ver financeiro completo", color = GuinchouGreen)
                            Spacer(Modifier.weight(1f))
                            Icon(Icons.Default.ChevronRight, null, tint = GuinchouGreen)
                        }
                    }
                }
            }

            item { SectionTitle("Acesso rápido") }
            item {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    QuickActionCard(Modifier.weight(1f), Icons.Default.ManageAccounts, "Usuários", onUsersClick)
                    QuickActionCard(Modifier.weight(1f), Icons.Default.SupportAgent, "Chamados", onCallsClick)
                }
            }
            item {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    QuickActionCard(Modifier.weight(1f), Icons.Default.CreditCard, "Pagamentos", onPaymentsClick)
                    QuickActionCard(Modifier.weight(1f), Icons.Default.Paid, "Repasses", onPayoutsClick)
                }
            }

            item { SectionTitle("Atividade recente") }
            item { ActivityCard("Empresa enviada para análise", "Auto Socorro Brasília Ltda.", "Há 8 min", Icons.Default.Business) }
            item { ActivityCard("Motorista aprovado", "Carlos Henrique", "Há 32 min", Icons.Default.Verified) }
            item { ActivityCard("Pagamento confirmado", "#GCH-1042 • R$ 200,00", "Há 54 min", Icons.Default.Payments) }
            item { ActivityCard("Chamado concluído", "#GCH-1040", "Há 1h", Icons.Default.CheckCircle) }
            item { Spacer(Modifier.height(24.dp)) }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminApprovalsScreen(
    onBackClick: () -> Unit = {},
    onHomeClick: () -> Unit = {},
    onFinanceClick: () -> Unit = {},
    onManagementClick: () -> Unit = {}
) {
    val approvals = remember {
        mutableStateListOf(
            AdminApproval(
                1, "João Pedro Martins", ApprovalType.INDEPENDENT_DRIVER, "CPF", "123.456.789-00", "Hoje, 14:20", ApprovalStatus.PENDING,
                listOf("Telefone" to "(61) 99999-1234", "E-mail" to "joao@email.com", "CNH" to "01234567890", "Categoria" to "D", "Validade" to "10/09/2031"),
                listOf("Documento pessoal" to true, "CNH" to true, "Comprovante de endereço" to true, "Foto do veículo" to false),
                listOf("Cadastro iniciado", "Documentos enviados", "Aguardando análise administrativa")
            ),
            AdminApproval(
                2, "Auto Socorro Brasília Ltda.", ApprovalType.COMPANY, "CNPJ", "12.345.678/0001-90", "Hoje, 13:05", ApprovalStatus.REVIEWING,
                listOf("Responsável" to "Rafael Oliveira", "Telefone" to "(61) 98888-3333", "E-mail" to "contato@autosocorro.com", "Região" to "Brasília e entorno"),
                listOf("Cartão CNPJ" to true, "Documento do responsável" to true, "Comprovante de endereço" to true),
                listOf("Cadastro empresarial enviado", "Análise iniciada pelo administrador")
            ),
            AdminApproval(
                3, "Marcos Silva", ApprovalType.COMPANY_DRIVER, "CPF", "987.654.321-00", "Ontem, 19:32", ApprovalStatus.CORRECTION,
                listOf("Empresa" to "Auto Socorro Brasília Ltda.", "Telefone" to "(61) 97777-2222", "CNH" to "98765432100", "Categoria" to "D"),
                listOf("Documento pessoal" to true, "CNH" to false),
                listOf("Cadastro enviado", "Correção solicitada na CNH")
            ),
            AdminApproval(
                4, "Mercedes-Benz Accelo", ApprovalType.TOW_TRUCK, "Placa", "ABC1D23", "Ontem, 16:18", ApprovalStatus.PENDING,
                listOf("Empresa" to "Auto Socorro Brasília Ltda.", "Ano" to "2022", "Tipo" to "Plataforma", "Capacidade" to "4.500 kg"),
                listOf("CRLV" to true, "Foto frontal" to true, "Foto traseira" to true),
                listOf("Veículo cadastrado", "Documentos enviados")
            )
        )
    }

    var filter by remember { mutableStateOf(ApprovalFilter.ALL) }
    var typeFilter by remember { mutableStateOf<ApprovalType?>(null) }
    var search by remember { mutableStateOf("") }
    var selectedApproval by remember { mutableStateOf<AdminApproval?>(null) }

    val visible = approvals.filter { approval ->
        val statusMatch = when (filter) {
            ApprovalFilter.ALL -> true
            ApprovalFilter.PENDING -> approval.status == ApprovalStatus.PENDING || approval.status == ApprovalStatus.CORRECTION
            ApprovalFilter.REVIEWING -> approval.status == ApprovalStatus.REVIEWING
            ApprovalFilter.APPROVED -> approval.status == ApprovalStatus.APPROVED
            ApprovalFilter.REJECTED -> approval.status == ApprovalStatus.REJECTED
        }
        val typeMatch = typeFilter == null || approval.type == typeFilter
        val searchMatch = search.isBlank() || approval.name.contains(search, true) || approval.documentNumber.contains(search, true)
        statusMatch && typeMatch && searchMatch
    }

    fun updateApproval(approval: AdminApproval, status: ApprovalStatus) {
        val index = approvals.indexOfFirst { it.id == approval.id }
        if (index >= 0) {
            approvals[index] = approval.copy(
                status = status,
                history = approval.history + when (status) {
                    ApprovalStatus.APPROVED -> "Cadastro aprovado pelo administrador"
                    ApprovalStatus.REJECTED -> "Cadastro rejeitado pelo administrador"
                    ApprovalStatus.CORRECTION -> "Correção solicitada pelo administrador"
                    ApprovalStatus.REVIEWING -> "Análise iniciada"
                    ApprovalStatus.PENDING -> "Cadastro retornou para pendente"
                }
            )
        }
        selectedApproval = null
    }

    AdminScaffold(
        title = "Aprovações",
        subtitle = "Cadastros e documentos",
        selectedDestination = AdminBottomDestination.APPROVALS,
        onBackClick = onBackClick,
        onHomeClick = onHomeClick,
        onApprovalsClick = {},
        onFinanceClick = onFinanceClick,
        onManagementClick = onManagementClick
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Spacer(Modifier.height(8.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    SummaryCounter(Modifier.weight(1f), approvals.count { it.status == ApprovalStatus.PENDING || it.status == ApprovalStatus.CORRECTION }, "Pendentes", true)
                    SummaryCounter(Modifier.weight(1f), approvals.count { it.status == ApprovalStatus.REVIEWING }, "Em análise")
                    SummaryCounter(Modifier.weight(1f), approvals.count { it.status == ApprovalStatus.APPROVED }, "Aprovados")
                }
            }
            item {
                OutlinedTextField(
                    value = search,
                    onValueChange = { search = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    leadingIcon = { Icon(Icons.Default.Search, null) },
                    placeholder = { Text("Buscar nome, CPF, CNPJ ou placa") },
                    shape = RoundedCornerShape(14.dp),
                    colors = adminTextFieldColors()
                )
            }
            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    item { AdminFilterChip("Todos", filter == ApprovalFilter.ALL) { filter = ApprovalFilter.ALL } }
                    item { AdminFilterChip("Pendentes", filter == ApprovalFilter.PENDING) { filter = ApprovalFilter.PENDING } }
                    item { AdminFilterChip("Em análise", filter == ApprovalFilter.REVIEWING) { filter = ApprovalFilter.REVIEWING } }
                    item { AdminFilterChip("Aprovados", filter == ApprovalFilter.APPROVED) { filter = ApprovalFilter.APPROVED } }
                    item { AdminFilterChip("Rejeitados", filter == ApprovalFilter.REJECTED) { filter = ApprovalFilter.REJECTED } }
                }
            }
            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    item { AdminFilterChip("Todos os tipos", typeFilter == null) { typeFilter = null } }
                    item { AdminFilterChip("Motoristas", typeFilter == ApprovalType.INDEPENDENT_DRIVER) { typeFilter = ApprovalType.INDEPENDENT_DRIVER } }
                    item { AdminFilterChip("Empresas", typeFilter == ApprovalType.COMPANY) { typeFilter = ApprovalType.COMPANY } }
                    item { AdminFilterChip("Funcionários", typeFilter == ApprovalType.COMPANY_DRIVER) { typeFilter = ApprovalType.COMPANY_DRIVER } }
                    item { AdminFilterChip("Guinchos", typeFilter == ApprovalType.TOW_TRUCK) { typeFilter = ApprovalType.TOW_TRUCK } }
                }
            }
            item { SectionTitle("Solicitações") }
            if (visible.isEmpty()) {
                item { EmptyStateCard(Icons.Default.CheckCircle, "Nenhuma aprovação encontrada", "Não há registros para os filtros selecionados.") }
            } else {
                items(visible, key = { it.id }) { approval -> ApprovalCard(approval) { selectedApproval = approval } }
            }
            item { Spacer(Modifier.height(24.dp)) }
        }
    }

    selectedApproval?.let { approval ->
        AdminApprovalDetailsScreen(
            approval = approval,
            onDismiss = { selectedApproval = null },
            onStartReview = { updateApproval(approval, ApprovalStatus.REVIEWING) },
            onApprove = { updateApproval(approval, ApprovalStatus.APPROVED) },
            onReject = { updateApproval(approval, ApprovalStatus.REJECTED) },
            onRequestCorrection = { updateApproval(approval, ApprovalStatus.CORRECTION) }
        )
    }
}

@Composable
private fun AdminApprovalDetailsScreen(
    approval: AdminApproval,
    onDismiss: () -> Unit,
    onStartReview: () -> Unit,
    onApprove: () -> Unit,
    onReject: () -> Unit,
    onRequestCorrection: () -> Unit
) {
    var confirmAction by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = GuinchouSurface,
        shape = RoundedCornerShape(22.dp),
        title = {
            Column {
                Text(approval.name, color = GuinchouWhite, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(approvalTypeText(approval.type), color = GuinchouGreen, fontSize = 12.sp)
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState())) {
                StatusPill(approvalStatusText(approval.status), approval.status == ApprovalStatus.APPROVED, approval.status == ApprovalStatus.REJECTED)
                Spacer(Modifier.height(14.dp))
                DialogInfo(approval.documentLabel, approval.documentNumber)
                DialogInfo("Enviado em", approval.sentAt)
                Text("Dados", color = GuinchouWhite, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                approval.details.forEach { DialogInfo(it.first, it.second) }
                Text("Documentos", color = GuinchouWhite, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                approval.documents.forEach { DocumentRow(it.first, it.second) }
                Spacer(Modifier.height(12.dp))
                Text("Histórico", color = GuinchouWhite, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                approval.history.forEach { HistoryRow(it) }
            }
        },
        confirmButton = {
            if (approval.status != ApprovalStatus.APPROVED) {
                Button(onClick = { confirmAction = "APPROVE" }, colors = ButtonDefaults.buttonColors(containerColor = GuinchouGreen, contentColor = Color.Black)) {
                    Text("Aprovar", fontWeight = FontWeight.Bold)
                }
            } else {
                Button(onClick = onDismiss, colors = ButtonDefaults.buttonColors(containerColor = GuinchouGreen, contentColor = Color.Black)) { Text("Fechar") }
            }
        },
        dismissButton = {
            Row {
                if (approval.status == ApprovalStatus.PENDING) {
                    TextButton(onClick = onStartReview) { Text("Iniciar análise", color = GuinchouGreen) }
                }
                if (approval.status != ApprovalStatus.APPROVED) {
                    TextButton(onClick = { confirmAction = "CORRECTION" }) { Text("Correção", color = GuinchouWhite) }
                    TextButton(onClick = { confirmAction = "REJECT" }) { Text("Rejeitar", color = GuinchouError) }
                }
            }
        }
    )

    when (confirmAction) {
        "APPROVE" -> ConfirmationDialog("Aprovar cadastro?", "O cadastro será marcado como aprovado no Front-End.", "Aprovar", { confirmAction = null }) { confirmAction = null; onApprove() }
        "REJECT" -> ReasonDialog("Rejeitar cadastro", "Informe o motivo da rejeição", "Rejeitar", true, { confirmAction = null }) { confirmAction = null; onReject() }
        "CORRECTION" -> ReasonDialog("Solicitar correção", "Explique o que precisa ser corrigido", "Enviar solicitação", false, { confirmAction = null }) { confirmAction = null; onRequestCorrection() }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminUsersScreen(
    onBackClick: () -> Unit = {},
    onHomeClick: () -> Unit = {},
    onApprovalsClick: () -> Unit = {},
    onFinanceClick: () -> Unit = {},
    onManagementClick: () -> Unit = {}
) {
    val users = remember {
        mutableStateListOf(
            AdminUser(1, "Ana Souza", UserType.CUSTOMER, "123.456.789-00", "(61) 99999-0001", "ana@email.com", UserStatus.ACTIVE, 8, 1240.0),
            AdminUser(2, "Carlos Henrique", UserType.DRIVER, "987.654.321-00", "(61) 99999-0002", "carlos@email.com", UserStatus.ACTIVE, 124, 0.0),
            AdminUser(3, "Auto Socorro Brasília", UserType.COMPANY, "12.345.678/0001-90", "(61) 99999-0003", "contato@autosocorro.com", UserStatus.ACTIVE, 310, 0.0),
            AdminUser(4, "Paulo Mendes", UserType.COMPANY_DRIVER, "456.789.123-00", "(61) 99999-0004", "paulo@email.com", UserStatus.SUSPENDED, 56, 0.0)
        )
    }
    var search by remember { mutableStateOf("") }
    var typeFilter by remember { mutableStateOf<UserType?>(null) }
    var selectedUser by remember { mutableStateOf<AdminUser?>(null) }

    val visible = users.filter {
        val typeMatch = typeFilter == null || it.type == typeFilter
        val searchMatch = search.isBlank() || it.name.contains(search, true) || it.document.contains(search, true) || it.phone.contains(search, true)
        typeMatch && searchMatch
    }

    fun updateUserStatus(user: AdminUser, status: UserStatus) {
        val index = users.indexOfFirst { it.id == user.id }
        if (index >= 0) users[index] = user.copy(status = status)
        selectedUser = null
    }

    AdminScaffold(
        title = "Usuários",
        subtitle = "Contas e acessos",
        selectedDestination = AdminBottomDestination.MANAGEMENT,
        onBackClick = onBackClick,
        onHomeClick = onHomeClick,
        onApprovalsClick = onApprovalsClick,
        onFinanceClick = onFinanceClick,
        onManagementClick = onManagementClick
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = search,
                    onValueChange = { search = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    leadingIcon = { Icon(Icons.Default.Search, null) },
                    placeholder = { Text("Buscar nome, CPF/CNPJ ou telefone") },
                    shape = RoundedCornerShape(14.dp),
                    colors = adminTextFieldColors()
                )
            }
            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    item { AdminFilterChip("Todos", typeFilter == null) { typeFilter = null } }
                    item { AdminFilterChip("Clientes", typeFilter == UserType.CUSTOMER) { typeFilter = UserType.CUSTOMER } }
                    item { AdminFilterChip("Motoristas", typeFilter == UserType.DRIVER) { typeFilter = UserType.DRIVER } }
                    item { AdminFilterChip("Empresas", typeFilter == UserType.COMPANY) { typeFilter = UserType.COMPANY } }
                    item { AdminFilterChip("Funcionários", typeFilter == UserType.COMPANY_DRIVER) { typeFilter = UserType.COMPANY_DRIVER } }
                }
            }
            item {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    SummaryCounter(Modifier.weight(1f), users.count { it.status == UserStatus.ACTIVE }, "Ativos", true)
                    SummaryCounter(Modifier.weight(1f), users.count { it.status == UserStatus.SUSPENDED }, "Suspensos")
                    SummaryCounter(Modifier.weight(1f), users.count { it.status == UserStatus.BLOCKED }, "Bloqueados")
                }
            }
            item { SectionTitle("Contas") }
            if (visible.isEmpty()) {
                item { EmptyStateCard(Icons.Default.PersonSearch, "Nenhum usuário encontrado", "Tente alterar os filtros de busca.") }
            } else {
                items(visible, key = { it.id }) { user -> UserCard(user) { selectedUser = user } }
            }
            item { Spacer(Modifier.height(24.dp)) }
        }
    }

    selectedUser?.let { user ->
        AlertDialog(
            onDismissRequest = { selectedUser = null },
            containerColor = GuinchouSurface,
            title = {
                Column {
                    Text(user.name, color = GuinchouWhite, fontWeight = FontWeight.Bold)
                    Text(userTypeText(user.type), color = GuinchouGreen, fontSize = 12.sp)
                }
            },
            text = {
                Column {
                    DialogInfo("Documento", user.document)
                    DialogInfo("Telefone", user.phone)
                    DialogInfo("E-mail", user.email)
                    DialogInfo("Chamados", user.callsCount.toString())
                    if (user.type == UserType.CUSTOMER) DialogInfo("Total pago", money(user.totalPaid))
                    DialogInfo("Status", userStatusText(user.status))
                }
            },
            confirmButton = {
                when (user.status) {
                    UserStatus.ACTIVE -> OutlinedButton(onClick = { updateUserStatus(user, UserStatus.SUSPENDED) }, border = BorderStroke(1.dp, GuinchouBorder)) { Text("Suspender", color = GuinchouWhite) }
                    UserStatus.SUSPENDED -> Button(onClick = { updateUserStatus(user, UserStatus.ACTIVE) }, colors = ButtonDefaults.buttonColors(containerColor = GuinchouGreen, contentColor = Color.Black)) { Text("Reativar", fontWeight = FontWeight.Bold) }
                    UserStatus.BLOCKED -> Button(onClick = { updateUserStatus(user, UserStatus.ACTIVE) }, colors = ButtonDefaults.buttonColors(containerColor = GuinchouGreen, contentColor = Color.Black)) { Text("Desbloquear", fontWeight = FontWeight.Bold) }
                }
            },
            dismissButton = {
                Row {
                    if (user.status != UserStatus.BLOCKED) {
                        TextButton(onClick = { updateUserStatus(user, UserStatus.BLOCKED) }) { Text("Bloquear", color = GuinchouError) }
                    }
                    TextButton(onClick = { selectedUser = null }) { Text("Fechar", color = GuinchouGray) }
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminCallsScreen(
    onBackClick: () -> Unit = {},
    onHomeClick: () -> Unit = {},
    onApprovalsClick: () -> Unit = {},
    onFinanceClick: () -> Unit = {},
    onManagementClick: () -> Unit = {}
) {
    val calls = remember {
        mutableStateListOf(
            AdminCall(1, "#GCH-1042", "Ricardo Almeida", "Carlos Henrique", null, "Honda Civic 2022", "Pane mecânica", "Asa Norte - DF", "SIA - DF", 200.0, 30.0, CallStatus.IN_TRANSIT, "Há 4 min"),
            AdminCall(2, "#GCH-1041", "Amanda Ribeiro", "Marcos Silva", "Auto Socorro Brasília", "Toyota Corolla 2021", "Transporte de veículo", "Águas Claras - DF", "Taguatinga - DF", 180.0, 27.0, CallStatus.COMPLETED, "Há 35 min"),
            AdminCall(3, "#GCH-1040", "Lucas Ferreira", "Aguardando parceiro", null, "VW T-Cross 2023", "Pane elétrica", "Guará - DF", "SIA - DF", 160.0, 24.0, CallStatus.SEARCHING, "Há 8 min"),
            AdminCall(4, "#GCH-1038", "Mariana Souza", "Paulo Mendes", "Guinchos DF", "Hyundai HB20 2020", "Acidente / colisão", "Lago Sul - DF", "Asa Sul - DF", 260.0, 39.0, CallStatus.DISPUTE, "Há 1h")
        )
    }
    var search by remember { mutableStateOf("") }
    var statusFilter by remember { mutableStateOf<CallStatus?>(null) }
    var selectedCall by remember { mutableStateOf<AdminCall?>(null) }

    val visible = calls.filter {
        val statusMatch = statusFilter == null || it.status == statusFilter
        val searchMatch = search.isBlank() || it.code.contains(search, true) || it.customer.contains(search, true) || it.partner.contains(search, true)
        statusMatch && searchMatch
    }

    AdminScaffold(
        title = "Chamados",
        subtitle = "Operação global",
        selectedDestination = AdminBottomDestination.MANAGEMENT,
        onBackClick = onBackClick,
        onHomeClick = onHomeClick,
        onApprovalsClick = onApprovalsClick,
        onFinanceClick = onFinanceClick,
        onManagementClick = onManagementClick
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = search,
                    onValueChange = { search = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    leadingIcon = { Icon(Icons.Default.Search, null) },
                    placeholder = { Text("Buscar chamado, cliente ou parceiro") },
                    shape = RoundedCornerShape(14.dp),
                    colors = adminTextFieldColors()
                )
            }
            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    item { AdminFilterChip("Todos", statusFilter == null) { statusFilter = null } }
                    item { AdminFilterChip("Buscando", statusFilter == CallStatus.SEARCHING) { statusFilter = CallStatus.SEARCHING } }
                    item { AdminFilterChip("Em transporte", statusFilter == CallStatus.IN_TRANSIT) { statusFilter = CallStatus.IN_TRANSIT } }
                    item { AdminFilterChip("Concluídos", statusFilter == CallStatus.COMPLETED) { statusFilter = CallStatus.COMPLETED } }
                    item { AdminFilterChip("Disputa", statusFilter == CallStatus.DISPUTE) { statusFilter = CallStatus.DISPUTE } }
                }
            }
            item {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    SummaryCounter(Modifier.weight(1f), calls.count { it.status == CallStatus.SEARCHING || it.status == CallStatus.NEW }, "Abertos", true)
                    SummaryCounter(Modifier.weight(1f), calls.count { it.status == CallStatus.IN_TRANSIT || it.status == CallStatus.ACCEPTED || it.status == CallStatus.DRIVER_ON_THE_WAY }, "Em andamento")
                    SummaryCounter(Modifier.weight(1f), calls.count { it.status == CallStatus.DISPUTE }, "Disputas")
                }
            }
            item { SectionTitle("Atendimentos") }
            items(visible, key = { it.id }) { call -> AdminCallCard(call) { selectedCall = call } }
            item { Spacer(Modifier.height(24.dp)) }
        }
    }

    selectedCall?.let { call ->
        AlertDialog(
            onDismissRequest = { selectedCall = null },
            containerColor = GuinchouSurface,
            title = {
                Column {
                    Text(call.code, color = GuinchouWhite, fontWeight = FontWeight.Bold)
                    Text(callStatusText(call.status), color = if (call.status == CallStatus.DISPUTE) GuinchouError else GuinchouGreen, fontSize = 12.sp)
                }
            },
            text = {
                Column {
                    DialogInfo("Cliente", call.customer)
                    DialogInfo("Parceiro", call.partner)
                    call.company?.let { DialogInfo("Empresa", it) }
                    DialogInfo("Veículo", call.vehicle)
                    DialogInfo("Serviço", call.service)
                    DialogInfo("Origem", call.origin)
                    DialogInfo("Destino", call.destination)
                    DialogInfo("Valor bruto", money(call.grossValue))
                    DialogInfo("Taxa Guinchou", money(call.platformFee))
                    DialogInfo("Parceiro recebe", money(call.grossValue - call.platformFee))
                }
            },
            confirmButton = {
                Button(onClick = { selectedCall = null }, colors = ButtonDefaults.buttonColors(containerColor = GuinchouGreen, contentColor = Color.Black)) { Text("Fechar") }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminPricingScreen(
    onBackClick: () -> Unit = {},
    onHomeClick: () -> Unit = {},
    onApprovalsClick: () -> Unit = {},
    onFinanceClick: () -> Unit = {},
    onManagementClick: () -> Unit = {}
) {
    val services = remember {
        mutableStateListOf(
            ServicePrice(1, "Pane mecânica", 80.0, 4.50, 120.0),
            ServicePrice(2, "Pane elétrica", 75.0, 4.25, 115.0),
            ServicePrice(3, "Acidente / colisão", 120.0, 5.50, 180.0),
            ServicePrice(4, "Transporte de veículo", 90.0, 4.75, 140.0),
            ServicePrice(5, "Veículo pesado", 180.0, 8.50, 280.0)
        )
    }
    var platformRate by remember { mutableStateOf(15.0) }
    var fixedFee by remember { mutableStateOf(2.0) }
    var cancellationFee by remember { mutableStateOf(20.0) }
    var waitingPerMinute by remember { mutableStateOf(1.50) }
    var nightSurcharge by remember { mutableStateOf(20.0) }
    var selectedService by remember { mutableStateOf<ServicePrice?>(null) }
    var showGeneralRules by remember { mutableStateOf(false) }

    AdminScaffold(
        title = "Preços e tarifas",
        subtitle = "Regras comerciais",
        selectedDestination = AdminBottomDestination.MANAGEMENT,
        onBackClick = onBackClick,
        onHomeClick = onHomeClick,
        onApprovalsClick = onApprovalsClick,
        onFinanceClick = onFinanceClick,
        onManagementClick = onManagementClick
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item { Spacer(Modifier.height(8.dp)); InfoBanner("Configuração demonstrativa", "As alterações abaixo afetam apenas o estado local do Front-End.") }
            item { SectionTitle("Serviços") }
            items(services, key = { it.id }) { service -> ServicePricingCard(service) { selectedService = service } }
            item { SectionTitle("Regras gerais") }
            item { GeneralRulesCard(platformRate, fixedFee, cancellationFee, waitingPerMinute, nightSurcharge) { showGeneralRules = true } }
            item { SectionTitle("Simulação") }
            item { PriceSimulationCard(services.first().basePrice, services.first().pricePerKm, services.first().minimumPrice, platformRate, fixedFee) }
            item { Spacer(Modifier.height(24.dp)) }
        }
    }

    selectedService?.let { service ->
        ServicePriceEditDialog(service, { selectedService = null }) { basePrice, pricePerKm, minimumPrice ->
            val index = services.indexOfFirst { it.id == service.id }
            if (index >= 0) services[index] = service.copy(basePrice = basePrice, pricePerKm = pricePerKm, minimumPrice = minimumPrice)
            selectedService = null
        }
    }

    if (showGeneralRules) {
        GeneralRulesEditDialog(platformRate, fixedFee, cancellationFee, waitingPerMinute, nightSurcharge, { showGeneralRules = false }) { rate, fixed, cancellation, waiting, night ->
            platformRate = rate
            fixedFee = fixed
            cancellationFee = cancellation
            waitingPerMinute = waiting
            nightSurcharge = night
            showGeneralRules = false
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminPaymentsScreen(
    onBackClick: () -> Unit = {},
    onHomeClick: () -> Unit = {},
    onApprovalsClick: () -> Unit = {},
    onFinanceClick: () -> Unit = {},
    onManagementClick: () -> Unit = {}
) {
    val payments = remember {
        mutableStateListOf(
            AdminPayment(1, "#GCH-1042", "Ricardo Almeida", "Carlos Henrique", "Cartão", 200.0, 30.0, PaymentStatus.PAID, "Hoje, 14:32"),
            AdminPayment(2, "#GCH-1041", "Amanda Ribeiro", "Auto Socorro Brasília", "PIX", 180.0, 27.0, PaymentStatus.PROCESSING, "Hoje, 13:18"),
            AdminPayment(3, "#GCH-1038", "Mariana Souza", "Guinchos DF", "Cartão", 260.0, 39.0, PaymentStatus.FAILED, "Hoje, 11:42"),
            AdminPayment(4, "#GCH-1030", "Lucas Ferreira", "Carlos Henrique", "PIX", 150.0, 22.50, PaymentStatus.REFUNDED, "Ontem, 18:30")
        )
    }
    var search by remember { mutableStateOf("") }
    var statusFilter by remember { mutableStateOf<PaymentStatus?>(null) }
    var selectedPayment by remember { mutableStateOf<AdminPayment?>(null) }

    val visible = payments.filter {
        val statusMatch = statusFilter == null || it.status == statusFilter
        val searchMatch = search.isBlank() || it.code.contains(search, true) || it.customer.contains(search, true) || it.partner.contains(search, true)
        statusMatch && searchMatch
    }

    fun updateStatus(payment: AdminPayment, status: PaymentStatus) {
        val index = payments.indexOfFirst { it.id == payment.id }
        if (index >= 0) payments[index] = payment.copy(status = status)
        selectedPayment = null
    }

    AdminScaffold(
        title = "Pagamentos",
        subtitle = "Transações dos usuários",
        selectedDestination = AdminBottomDestination.FINANCE,
        onBackClick = onBackClick,
        onHomeClick = onHomeClick,
        onApprovalsClick = onApprovalsClick,
        onFinanceClick = onFinanceClick,
        onManagementClick = onManagementClick
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = search,
                    onValueChange = { search = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    leadingIcon = { Icon(Icons.Default.Search, null) },
                    placeholder = { Text("Buscar chamado, cliente ou parceiro") },
                    shape = RoundedCornerShape(14.dp),
                    colors = adminTextFieldColors()
                )
            }
            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    item { AdminFilterChip("Todos", statusFilter == null) { statusFilter = null } }
                    item { AdminFilterChip("Pagos", statusFilter == PaymentStatus.PAID) { statusFilter = PaymentStatus.PAID } }
                    item { AdminFilterChip("Processando", statusFilter == PaymentStatus.PROCESSING) { statusFilter = PaymentStatus.PROCESSING } }
                    item { AdminFilterChip("Falhas", statusFilter == PaymentStatus.FAILED) { statusFilter = PaymentStatus.FAILED } }
                    item { AdminFilterChip("Reembolsados", statusFilter == PaymentStatus.REFUNDED) { statusFilter = PaymentStatus.REFUNDED } }
                }
            }
            item {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    SummaryCounter(Modifier.weight(1f), payments.count { it.status == PaymentStatus.PAID }, "Pagos", true)
                    SummaryCounter(Modifier.weight(1f), payments.count { it.status == PaymentStatus.PROCESSING || it.status == PaymentStatus.PENDING }, "Pendentes")
                    SummaryCounter(Modifier.weight(1f), payments.count { it.status == PaymentStatus.FAILED }, "Falhas")
                }
            }
            item { SectionTitle("Transações") }
            items(visible, key = { it.id }) { payment -> PaymentCard(payment) { selectedPayment = payment } }
            item { Spacer(Modifier.height(24.dp)) }
        }
    }

    selectedPayment?.let { payment ->
        AlertDialog(
            onDismissRequest = { selectedPayment = null },
            containerColor = GuinchouSurface,
            title = {
                Column {
                    Text(payment.code, color = GuinchouWhite, fontWeight = FontWeight.Bold)
                    Text(paymentStatusText(payment.status), color = paymentStatusColor(payment.status), fontSize = 12.sp)
                }
            },
            text = {
                Column {
                    DialogInfo("Cliente", payment.customer)
                    DialogInfo("Parceiro", payment.partner)
                    DialogInfo("Método", payment.method)
                    DialogInfo("Valor", money(payment.grossValue))
                    DialogInfo("Taxa Guinchou", money(payment.platformFee))
                    DialogInfo("Data", payment.date)
                }
            },
            confirmButton = {
                when (payment.status) {
                    PaymentStatus.FAILED -> Button(onClick = { updateStatus(payment, PaymentStatus.PROCESSING) }, colors = ButtonDefaults.buttonColors(containerColor = GuinchouGreen, contentColor = Color.Black)) { Icon(Icons.Default.Replay, null); Spacer(Modifier.width(6.dp)); Text("Reprocessar", fontWeight = FontWeight.Bold) }
                    PaymentStatus.PAID -> OutlinedButton(onClick = { updateStatus(payment, PaymentStatus.REFUNDED) }, border = BorderStroke(1.dp, GuinchouBorder)) { Icon(Icons.Default.MoneyOff, null, tint = GuinchouWhite); Spacer(Modifier.width(6.dp)); Text("Simular reembolso", color = GuinchouWhite) }
                    else -> Button(onClick = { selectedPayment = null }, colors = ButtonDefaults.buttonColors(containerColor = GuinchouGreen, contentColor = Color.Black)) { Text("Fechar") }
                }
            },
            dismissButton = {
                if (payment.status != PaymentStatus.ANALYSIS) {
                    TextButton(onClick = { updateStatus(payment, PaymentStatus.ANALYSIS) }) { Text("Marcar em análise", color = GuinchouGray) }
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminPayoutsScreen(
    onBackClick: () -> Unit = {},
    onHomeClick: () -> Unit = {},
    onApprovalsClick: () -> Unit = {},
    onFinanceClick: () -> Unit = {},
    onManagementClick: () -> Unit = {}
) {
    val payouts = remember {
        mutableStateListOf(
            AdminPayout(1, "Carlos Henrique", "Motorista independente", 1250.0, 187.50, 1062.50, PayoutStatus.WAITING, "10/10/2026"),
            AdminPayout(2, "Auto Socorro Brasília", "Empresa", 3400.0, 510.0, 2890.0, PayoutStatus.PROCESSING, "10/10/2026"),
            AdminPayout(3, "Guinchos DF", "Empresa", 2180.0, 327.0, 1853.0, PayoutStatus.PAID, "05/10/2026", "05/10/2026"),
            AdminPayout(4, "Marcos Silva", "Motorista independente", 840.0, 126.0, 714.0, PayoutStatus.FAILED, "06/10/2026")
        )
    }
    var selectedPayout by remember { mutableStateOf<AdminPayout?>(null) }
    var statusFilter by remember { mutableStateOf<PayoutStatus?>(null) }
    val visible = payouts.filter { statusFilter == null || it.status == statusFilter }

    fun updateStatus(payout: AdminPayout, status: PayoutStatus) {
        val index = payouts.indexOfFirst { it.id == payout.id }
        if (index >= 0) payouts[index] = payout.copy(status = status)
        selectedPayout = null
    }

    AdminScaffold(
        title = "Repasses",
        subtitle = "Valores dos parceiros",
        selectedDestination = AdminBottomDestination.FINANCE,
        onBackClick = onBackClick,
        onHomeClick = onHomeClick,
        onApprovalsClick = onApprovalsClick,
        onFinanceClick = onFinanceClick,
        onManagementClick = onManagementClick
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Spacer(Modifier.height(8.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    SummaryCounter(Modifier.weight(1f), payouts.count { it.status == PayoutStatus.WAITING }, "Aguardando", true)
                    SummaryCounter(Modifier.weight(1f), payouts.count { it.status == PayoutStatus.PROCESSING }, "Processando")
                    SummaryCounter(Modifier.weight(1f), payouts.count { it.status == PayoutStatus.FAILED }, "Falhas")
                }
            }
            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    item { AdminFilterChip("Todos", statusFilter == null) { statusFilter = null } }
                    item { AdminFilterChip("Aguardando", statusFilter == PayoutStatus.WAITING) { statusFilter = PayoutStatus.WAITING } }
                    item { AdminFilterChip("Processando", statusFilter == PayoutStatus.PROCESSING) { statusFilter = PayoutStatus.PROCESSING } }
                    item { AdminFilterChip("Pagos", statusFilter == PayoutStatus.PAID) { statusFilter = PayoutStatus.PAID } }
                    item { AdminFilterChip("Falhas", statusFilter == PayoutStatus.FAILED) { statusFilter = PayoutStatus.FAILED } }
                }
            }
            item { SectionTitle("Parceiros") }
            items(visible, key = { it.id }) { payout -> PayoutCard(payout) { selectedPayout = payout } }
            item { Spacer(Modifier.height(24.dp)) }
        }
    }

    selectedPayout?.let { payout ->
        AlertDialog(
            onDismissRequest = { selectedPayout = null },
            containerColor = GuinchouSurface,
            title = {
                Column {
                    Text(payout.partnerName, color = GuinchouWhite, fontWeight = FontWeight.Bold)
                    Text(payout.partnerType, color = GuinchouGray, fontSize = 12.sp)
                }
            },
            text = {
                Column {
                    DialogInfo("Valor bruto", money(payout.grossValue))
                    DialogInfo("Taxa Guinchou", money(payout.platformFee))
                    DialogInfo("Valor líquido", money(payout.netValue))
                    DialogInfo("Data prevista", payout.expectedDate)
                    payout.paidDate?.let { DialogInfo("Pago em", it) }
                    DialogInfo("Status", payoutStatusText(payout.status))
                }
            },
            confirmButton = {
                when (payout.status) {
                    PayoutStatus.WAITING, PayoutStatus.FAILED -> Button(onClick = { updateStatus(payout, PayoutStatus.PROCESSING) }, colors = ButtonDefaults.buttonColors(containerColor = GuinchouGreen, contentColor = Color.Black)) { Text(if (payout.status == PayoutStatus.FAILED) "Reprocessar" else "Processar", fontWeight = FontWeight.Bold) }
                    else -> Button(onClick = { selectedPayout = null }, colors = ButtonDefaults.buttonColors(containerColor = GuinchouGreen, contentColor = Color.Black)) { Text("Fechar") }
                }
            },
            dismissButton = {
                if (payout.status != PayoutStatus.PAID && payout.status != PayoutStatus.BLOCKED) {
                    TextButton(onClick = { updateStatus(payout, PayoutStatus.BLOCKED) }) { Text("Bloquear", color = GuinchouError) }
                }
            }
        )
    }
}

@Composable
fun AdminFinanceScreen(
    onHomeClick: () -> Unit = {},
    onApprovalsClick: () -> Unit = {},
    onManagementClick: () -> Unit = {},
    onPaymentsClick: () -> Unit = {},
    onPayoutsClick: () -> Unit = {}
) {
    var period by remember { mutableStateOf(FinancePeriod.MONTH) }

    AdminSimpleScaffold(
        selectedDestination = AdminBottomDestination.FINANCE,
        onHomeClick = onHomeClick,
        onApprovalsClick = onApprovalsClick,
        onFinanceClick = {},
        onManagementClick = onManagementClick
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Spacer(Modifier.height(10.dp))
                Text("Financeiro", color = GuinchouWhite, fontSize = 26.sp, fontWeight = FontWeight.Bold)
                Text("Visão financeira global da plataforma.", color = GuinchouGray, fontSize = 13.sp)
            }
            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    item { AdminFilterChip("Hoje", period == FinancePeriod.TODAY) { period = FinancePeriod.TODAY } }
                    item { AdminFilterChip("7 dias", period == FinancePeriod.SEVEN_DAYS) { period = FinancePeriod.SEVEN_DAYS } }
                    item { AdminFilterChip("30 dias", period == FinancePeriod.THIRTY_DAYS) { period = FinancePeriod.THIRTY_DAYS } }
                    item { AdminFilterChip("Este mês", period == FinancePeriod.MONTH) { period = FinancePeriod.MONTH } }
                }
            }
            item { FinanceHeroCard(84350.0, 12652.50, 71697.50) }
            item {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    FinancialKpiCard(Modifier.weight(1f), "Repasses pendentes", "R$ 5.420,00", Icons.Default.SyncAlt)
                    FinancialKpiCard(Modifier.weight(1f), "Reembolsos", "R$ 780,00", Icons.Default.MoneyOff)
                }
            }
            item { SectionTitle("Desempenho") }
            item { FinanceChartMock(listOf(0.45f, 0.62f, 0.55f, 0.78f, 0.68f, 0.86f, 0.74f)) }
            item { SectionTitle("Ações") }
            item { ManagementRow(Icons.Default.CreditCard, "Pagamentos", "Transações, falhas e reembolsos", onPaymentsClick) }
            item { ManagementRow(Icons.Default.Paid, "Repasses", "Valores destinados aos parceiros", onPayoutsClick) }
            item { InfoBanner("Somente Front-End", "Os números apresentados são mockados e não representam movimentações financeiras reais.") }
            item { Spacer(Modifier.height(24.dp)) }
        }
    }
}

@Composable
fun AdminManagementScreen(
    onHomeClick: () -> Unit = {},
    onApprovalsClick: () -> Unit = {},
    onFinanceClick: () -> Unit = {},
    onUsersClick: () -> Unit = {},
    onCallsClick: () -> Unit = {},
    onPricingClick: () -> Unit = {},
    onPaymentsClick: () -> Unit = {},
    onPayoutsClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
    AdminSimpleScaffold(
        selectedDestination = AdminBottomDestination.MANAGEMENT,
        onHomeClick = onHomeClick,
        onApprovalsClick = onApprovalsClick,
        onFinanceClick = onFinanceClick,
        onManagementClick = {}
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(20.dp)
        ) {
            Text("Gestão", color = GuinchouWhite, fontSize = 26.sp, fontWeight = FontWeight.Bold)
            Text("Central administrativa da plataforma.", color = GuinchouGray, fontSize = 13.sp)
            Spacer(Modifier.height(22.dp))
            ManagementLargeCard(Icons.Default.ManageAccounts, "Usuários", "Gerencie clientes, motoristas, empresas e funcionários.", "Abrir usuários", onUsersClick)
            Spacer(Modifier.height(12.dp))
            ManagementLargeCard(Icons.Default.SupportAgent, "Chamados", "Acompanhe todos os atendimentos e disputas da plataforma.", "Abrir chamados", onCallsClick)
            Spacer(Modifier.height(12.dp))
            ManagementLargeCard(Icons.Default.PriceChange, "Preços e tarifas", "Configure preços base, valor por km, mínimos e taxas.", "Configurar preços", onPricingClick)
            Spacer(Modifier.height(12.dp))
            ManagementLargeCard(Icons.Default.CreditCard, "Pagamentos", "Acompanhe pagamentos, falhas, análises e reembolsos.", "Abrir pagamentos", onPaymentsClick)
            Spacer(Modifier.height(12.dp))
            ManagementLargeCard(Icons.Default.Paid, "Repasses", "Acompanhe valores destinados a motoristas e empresas.", "Abrir repasses", onPayoutsClick)
            Spacer(Modifier.height(12.dp))
            ManagementLargeCard(Icons.Default.Settings, "Configurações", "Preferências operacionais e regras gerais do ADMIN.", "Abrir configurações", onSettingsClick)
            Spacer(Modifier.height(24.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminSettingsScreen(
    onBackClick: () -> Unit = {},
    onHomeClick: () -> Unit = {},
    onApprovalsClick: () -> Unit = {},
    onFinanceClick: () -> Unit = {},
    onManagementClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {}
) {
    var autoReview by remember { mutableStateOf(false) }
    var notifyFailedPayments by remember { mutableStateOf(true) }
    var notifyPendingApprovals by remember { mutableStateOf(true) }
    var notifyDisputes by remember { mutableStateOf(true) }
    var maintenanceMode by remember { mutableStateOf(false) }
    var showLogout by remember { mutableStateOf(false) }

    AdminScaffold(
        title = "Configurações",
        subtitle = "Preferências administrativas",
        selectedDestination = AdminBottomDestination.MANAGEMENT,
        onBackClick = onBackClick,
        onHomeClick = onHomeClick,
        onApprovalsClick = onApprovalsClick,
        onFinanceClick = onFinanceClick,
        onManagementClick = onManagementClick
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(20.dp)) {
            SectionTitle("Aprovações")
            Spacer(Modifier.height(10.dp))
            SettingSwitchRow("Análise automática", "Demonstração visual de um futuro fluxo automatizado.", autoReview) { autoReview = it }
            SettingSwitchRow("Alertar aprovações pendentes", "Exibir avisos quando houver cadastros aguardando análise.", notifyPendingApprovals) { notifyPendingApprovals = it }
            Spacer(Modifier.height(20.dp))
            SectionTitle("Financeiro")
            Spacer(Modifier.height(10.dp))
            SettingSwitchRow("Alertar falhas de pagamento", "Mostrar alerta administrativo em transações com erro.", notifyFailedPayments) { notifyFailedPayments = it }
            Spacer(Modifier.height(20.dp))
            SectionTitle("Operação")
            Spacer(Modifier.height(10.dp))
            SettingSwitchRow("Alertar disputas", "Priorizar chamados em disputa no painel ADMIN.", notifyDisputes) { notifyDisputes = it }
            SettingSwitchRow("Modo manutenção", "Apenas demonstração visual nesta fase do Front-End.", maintenanceMode) { maintenanceMode = it }
            Spacer(Modifier.height(20.dp))
            InfoBanner("Configurações locais", "Nenhuma opção desta tela altera o Supabase ou o funcionamento real da plataforma neste momento.")
            Spacer(Modifier.height(20.dp))
            OutlinedButton(onClick = { showLogout = true }, modifier = Modifier.fillMaxWidth(), border = BorderStroke(1.dp, GuinchouBorder)) {
                Icon(Icons.AutoMirrored.Filled.Logout, null, tint = GuinchouWhite)
                Spacer(Modifier.width(8.dp))
                Text("Sair do ADMIN", color = GuinchouWhite)
            }
            Spacer(Modifier.height(24.dp))
        }
    }

    if (showLogout) {
        ConfirmationDialog("Sair do ADMIN?", "Você será direcionado para a tela de login.", "Sair", { showLogout = false }) {
            showLogout = false
            onLogoutClick()
        }
    }
}

@Composable
private fun AdminBottomBar(
    selectedDestination: AdminBottomDestination,
    onHomeClick: () -> Unit,
    onApprovalsClick: () -> Unit,
    onFinanceClick: () -> Unit,
    onManagementClick: () -> Unit
) {
    NavigationBar(containerColor = GuinchouSurface, tonalElevation = 0.dp) {
        AdminNavItem(selectedDestination == AdminBottomDestination.HOME, Icons.Default.Home, "Dashboard", onHomeClick)
        AdminNavItem(selectedDestination == AdminBottomDestination.APPROVALS, Icons.Default.Verified, "Aprovações", onApprovalsClick)
        AdminNavItem(selectedDestination == AdminBottomDestination.FINANCE, Icons.Default.AccountBalanceWallet, "Financeiro", onFinanceClick)
        AdminNavItem(selectedDestination == AdminBottomDestination.MANAGEMENT, Icons.Default.BusinessCenter, "Gestão", onManagementClick)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AdminScaffold(
    title: String,
    subtitle: String,
    selectedDestination: AdminBottomDestination,
    onBackClick: () -> Unit,
    onHomeClick: () -> Unit,
    onApprovalsClick: () -> Unit,
    onFinanceClick: () -> Unit,
    onManagementClick: () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        containerColor = GuinchouBackground,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = GuinchouBackground),
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar", tint = GuinchouWhite)
                    }
                },
                title = {
                    Column {
                        Text(title, color = GuinchouWhite, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Text(subtitle, color = GuinchouGray, fontSize = 12.sp)
                    }
                }
            )
        },
        bottomBar = {
            AdminBottomBar(selectedDestination, onHomeClick, onApprovalsClick, onFinanceClick, onManagementClick)
        },
        content = content
    )
}

@Composable
private fun AdminSimpleScaffold(
    selectedDestination: AdminBottomDestination,
    onHomeClick: () -> Unit,
    onApprovalsClick: () -> Unit,
    onFinanceClick: () -> Unit,
    onManagementClick: () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        containerColor = GuinchouBackground,
        bottomBar = { AdminBottomBar(selectedDestination, onHomeClick, onApprovalsClick, onFinanceClick, onManagementClick) },
        content = content
    )
}

@Composable
private fun RowScope.AdminNavItem(selected: Boolean, icon: ImageVector, label: String, onClick: () -> Unit) {
    NavigationBarItem(
        selected = selected,
        onClick = onClick,
        icon = { Icon(icon, contentDescription = label) },
        label = { Text(label) },
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
private fun SectionTitle(text: String) {
    Text(text, color = GuinchouWhite, fontSize = 17.sp, fontWeight = FontWeight.Bold)
}

@Composable
private fun MetricCard(modifier: Modifier, icon: ImageVector, value: String, label: String) {
    Surface(modifier = modifier, color = GuinchouSurface, shape = RoundedCornerShape(17.dp), border = BorderStroke(1.dp, GuinchouBorder)) {
        Column(Modifier.padding(16.dp)) {
            Icon(icon, null, tint = GuinchouGreen, modifier = Modifier.size(24.dp))
            Spacer(Modifier.height(12.dp))
            Text(value, color = GuinchouWhite, fontSize = 19.sp, fontWeight = FontWeight.Bold)
            Text(label, color = GuinchouGray, fontSize = 12.sp)
        }
    }
}

@Composable
private fun AttentionCard(icon: ImageVector, value: Int, title: String, subtitle: String, onClick: () -> Unit, alert: Boolean = false) {
    Surface(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        color = if (alert) GuinchouError.copy(alpha = 0.055f) else GuinchouSurface,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, if (alert) GuinchouError.copy(alpha = 0.30f) else GuinchouBorder)
    ) {
        Row(Modifier.padding(15.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(color = if (alert) GuinchouError.copy(alpha = 0.12f) else GuinchouGreen.copy(alpha = 0.10f), shape = RoundedCornerShape(12.dp)) {
                Icon(icon, null, tint = if (alert) GuinchouError else GuinchouGreen, modifier = Modifier.padding(10.dp).size(22.dp))
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(title, color = GuinchouWhite, fontWeight = FontWeight.SemiBold)
                Text(subtitle, color = GuinchouGray, fontSize = 11.sp)
            }
            Surface(color = if (alert) GuinchouError.copy(alpha = 0.12f) else GuinchouGreen.copy(alpha = 0.11f), shape = CircleShape) {
                Text(value.toString(), color = if (alert) GuinchouError else GuinchouGreen, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp))
            }
            Spacer(Modifier.width(8.dp))
            Icon(Icons.Default.ChevronRight, null, tint = GuinchouGray)
        }
    }
}

@Composable
private fun QuickActionCard(modifier: Modifier, icon: ImageVector, title: String, onClick: () -> Unit) {
    Surface(modifier = modifier.clickable(onClick = onClick), color = GuinchouSurface, shape = RoundedCornerShape(16.dp), border = BorderStroke(1.dp, GuinchouBorder)) {
        Column(Modifier.padding(16.dp)) {
            Icon(icon, null, tint = GuinchouGreen, modifier = Modifier.size(25.dp))
            Spacer(Modifier.height(11.dp))
            Text(title, color = GuinchouWhite, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun ActivityCard(title: String, subtitle: String, time: String, icon: ImageVector) {
    Surface(Modifier.fillMaxWidth(), color = GuinchouSurface, shape = RoundedCornerShape(15.dp), border = BorderStroke(1.dp, GuinchouBorder)) {
        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = GuinchouGreen, modifier = Modifier.size(22.dp))
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(title, color = GuinchouWhite, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                Text(subtitle, color = GuinchouGray, fontSize = 11.sp)
            }
            Text(time, color = GuinchouGray, fontSize = 10.sp)
        }
    }
}

@Composable
private fun FinanceMiniCard(modifier: Modifier, title: String, value: String, highlighted: Boolean) {
    Surface(modifier = modifier, color = Color.Black.copy(alpha = 0.13f), shape = RoundedCornerShape(13.dp)) {
        Column(Modifier.padding(12.dp)) {
            Text(title, color = GuinchouGray, fontSize = 11.sp)
            Spacer(Modifier.height(3.dp))
            Text(value, color = if (highlighted) GuinchouGreen else GuinchouWhite, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun ApprovalCard(approval: AdminApproval, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        color = GuinchouSurface,
        shape = RoundedCornerShape(17.dp),
        border = BorderStroke(1.dp, if (approval.status == ApprovalStatus.PENDING || approval.status == ApprovalStatus.CORRECTION) GuinchouGreen.copy(alpha = 0.28f) else GuinchouBorder)
    ) {
        Row(Modifier.padding(15.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(color = GuinchouGreen.copy(alpha = 0.10f), shape = RoundedCornerShape(13.dp)) {
                Icon(approvalTypeIcon(approval.type), null, tint = GuinchouGreen, modifier = Modifier.padding(11.dp).size(23.dp))
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(approval.name, color = GuinchouWhite, fontWeight = FontWeight.Bold)
                Text(approvalTypeText(approval.type), color = GuinchouGray, fontSize = 11.sp)
                Text("${approval.documentLabel}: ${approval.documentNumber}", color = GuinchouGray, fontSize = 11.sp)
                Text(approval.sentAt, color = GuinchouGray, fontSize = 10.sp)
            }
            StatusPill(approvalStatusText(approval.status), approval.status == ApprovalStatus.APPROVED, approval.status == ApprovalStatus.REJECTED)
        }
    }
}

@Composable
private fun DocumentRow(title: String, valid: Boolean) {
    Row(Modifier.fillMaxWidth().padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(if (valid) Icons.Default.CheckCircle else Icons.Default.WarningAmber, null, tint = if (valid) GuinchouGreen else GuinchouError, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(8.dp))
        Text(title, color = GuinchouWhite, fontSize = 12.sp)
    }
}

@Composable
private fun HistoryRow(text: String) {
    Row(Modifier.padding(vertical = 3.dp), verticalAlignment = Alignment.Top) {
        Surface(modifier = Modifier.padding(top = 5.dp).size(6.dp), shape = CircleShape, color = GuinchouGreen) {}
        Spacer(Modifier.width(8.dp))
        Text(text, color = GuinchouGray, fontSize = 11.sp, lineHeight = 16.sp)
    }
}

@Composable
private fun UserCard(user: AdminUser, onClick: () -> Unit) {
    Surface(modifier = Modifier.fillMaxWidth(), onClick = onClick, color = GuinchouSurface, shape = RoundedCornerShape(17.dp), border = BorderStroke(1.dp, GuinchouBorder)) {
        Row(Modifier.padding(15.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(color = GuinchouGreen.copy(alpha = 0.10f), shape = RoundedCornerShape(13.dp)) {
                Icon(userTypeIcon(user.type), null, tint = GuinchouGreen, modifier = Modifier.padding(11.dp).size(23.dp))
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(user.name, color = GuinchouWhite, fontWeight = FontWeight.Bold)
                Text(userTypeText(user.type), color = GuinchouGray, fontSize = 11.sp)
                Text(user.document, color = GuinchouGray, fontSize = 11.sp)
            }
            StatusPill(userStatusText(user.status), user.status == UserStatus.ACTIVE, user.status == UserStatus.BLOCKED)
        }
    }
}

@Composable
private fun AdminCallCard(call: AdminCall, onClick: () -> Unit) {
    Surface(modifier = Modifier.fillMaxWidth(), onClick = onClick, color = GuinchouSurface, shape = RoundedCornerShape(17.dp), border = BorderStroke(1.dp, if (call.status == CallStatus.DISPUTE) GuinchouError.copy(alpha = 0.35f) else GuinchouBorder)) {
        Column(Modifier.padding(15.dp)) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text(call.code, color = GuinchouWhite, fontWeight = FontWeight.Bold)
                    Text(call.service, color = GuinchouGray, fontSize = 11.sp)
                }
                StatusPill(callStatusText(call.status), call.status == CallStatus.COMPLETED, call.status == CallStatus.DISPUTE || call.status == CallStatus.CANCELLED)
            }
            Spacer(Modifier.height(10.dp))
            DialogInfo("Cliente", call.customer)
            DialogInfo("Parceiro", call.partner)
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(money(call.grossValue), color = GuinchouGreen, fontWeight = FontWeight.Bold)
                Spacer(Modifier.weight(1f))
                Text(call.updatedAt, color = GuinchouGray, fontSize = 10.sp)
            }
        }
    }
}

@Composable
private fun ServicePricingCard(service: ServicePrice, onClick: () -> Unit) {
    Surface(Modifier.fillMaxWidth(), color = GuinchouSurface, shape = RoundedCornerShape(18.dp), border = BorderStroke(1.dp, GuinchouBorder)) {
        Column(Modifier.padding(16.dp)) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Surface(color = GuinchouGreen.copy(alpha = 0.10f), shape = RoundedCornerShape(12.dp)) {
                    Icon(Icons.Default.PriceChange, null, tint = GuinchouGreen, modifier = Modifier.padding(10.dp).size(22.dp))
                }
                Spacer(Modifier.width(12.dp))
                Text(service.title, color = GuinchouWhite, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                TextButton(onClick = onClick) { Text("Editar", color = GuinchouGreen) }
            }
            Spacer(Modifier.height(12.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                SmallValueCard(Modifier.weight(1f), "Base", money(service.basePrice))
                SmallValueCard(Modifier.weight(1f), "Por km", money(service.pricePerKm))
                SmallValueCard(Modifier.weight(1f), "Mínimo", money(service.minimumPrice))
            }
        }
    }
}

@Composable
private fun SmallValueCard(modifier: Modifier, title: String, value: String) {
    Surface(modifier = modifier, color = Color.Black.copy(alpha = 0.12f), shape = RoundedCornerShape(11.dp)) {
        Column(Modifier.padding(10.dp)) {
            Text(title, color = GuinchouGray, fontSize = 10.sp)
            Text(value, color = GuinchouWhite, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun GeneralRulesCard(
    platformRate: Double,
    fixedFee: Double,
    cancellationFee: Double,
    waitingPerMinute: Double,
    nightSurcharge: Double,
    onEdit: () -> Unit
) {
    Surface(Modifier.fillMaxWidth(), color = GuinchouSurface, shape = RoundedCornerShape(18.dp), border = BorderStroke(1.dp, GuinchouBorder)) {
        Column(Modifier.padding(16.dp)) {
            RuleRow("Taxa Guinchou", "${formatNumber(platformRate)}%")
            RuleRow("Taxa fixa", money(fixedFee))
            RuleRow("Cancelamento", money(cancellationFee))
            RuleRow("Espera por minuto", money(waitingPerMinute))
            RuleRow("Adicional noturno", "${formatNumber(nightSurcharge)}%")
            Spacer(Modifier.height(10.dp))
            OutlinedButton(onClick = onEdit, modifier = Modifier.fillMaxWidth(), border = BorderStroke(1.dp, GuinchouBorder)) {
                Icon(Icons.Default.Rule, null, tint = GuinchouGreen)
                Spacer(Modifier.width(8.dp))
                Text("Editar regras", color = GuinchouGreen)
            }
        }
    }
}

@Composable
private fun PriceSimulationCard(basePrice: Double, pricePerKm: Double, minimumPrice: Double, platformRate: Double, fixedFee: Double) {
    val distance = 20.0
    val calculated = maxOf(minimumPrice, basePrice + (pricePerKm * distance))
    val platform = (calculated * (platformRate / 100.0)) + fixedFee
    val partner = calculated - platform

    Surface(Modifier.fillMaxWidth(), color = GuinchouGreen.copy(alpha = 0.06f), shape = RoundedCornerShape(18.dp), border = BorderStroke(1.dp, GuinchouGreen.copy(alpha = 0.30f))) {
        Column(Modifier.padding(17.dp)) {
            Text("Pane mecânica • 20 km", color = GuinchouGray, fontSize = 12.sp)
            Spacer(Modifier.height(12.dp))
            RuleRow("Cliente paga", money(calculated))
            RuleRow("Guinchou recebe", money(platform))
            RuleRow("Parceiro recebe", money(partner))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ServicePriceEditDialog(service: ServicePrice, onDismiss: () -> Unit, onSave: (Double, Double, Double) -> Unit) {
    var basePrice by remember { mutableStateOf(service.basePrice.toString()) }
    var perKm by remember { mutableStateOf(service.pricePerKm.toString()) }
    var minimum by remember { mutableStateOf(service.minimumPrice.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = GuinchouSurface,
        title = { Text("Editar ${service.title}", color = GuinchouWhite, fontWeight = FontWeight.Bold) },
        text = {
            Column {
                AdminNumberField("Preço base", basePrice) { basePrice = it }
                Spacer(Modifier.height(10.dp))
                AdminNumberField("Preço por km", perKm) { perKm = it }
                Spacer(Modifier.height(10.dp))
                AdminNumberField("Preço mínimo", minimum) { minimum = it }
            }
        },
        confirmButton = {
            Button(onClick = {
                onSave(basePrice.toDoubleOrNull() ?: service.basePrice, perKm.toDoubleOrNull() ?: service.pricePerKm, minimum.toDoubleOrNull() ?: service.minimumPrice)
            }, colors = ButtonDefaults.buttonColors(containerColor = GuinchouGreen, contentColor = Color.Black)) {
                Text("Salvar", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar", color = GuinchouGray) } }
    )
}

@Composable
private fun GeneralRulesEditDialog(
    platformRate: Double,
    fixedFee: Double,
    cancellationFee: Double,
    waitingPerMinute: Double,
    nightSurcharge: Double,
    onDismiss: () -> Unit,
    onSave: (Double, Double, Double, Double, Double) -> Unit
) {
    var rate by remember { mutableStateOf(platformRate.toString()) }
    var fixed by remember { mutableStateOf(fixedFee.toString()) }
    var cancellation by remember { mutableStateOf(cancellationFee.toString()) }
    var waiting by remember { mutableStateOf(waitingPerMinute.toString()) }
    var night by remember { mutableStateOf(nightSurcharge.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = GuinchouSurface,
        title = { Text("Regras gerais", color = GuinchouWhite, fontWeight = FontWeight.Bold) },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                AdminNumberField("Taxa Guinchou (%)", rate) { rate = it }
                Spacer(Modifier.height(8.dp))
                AdminNumberField("Taxa fixa", fixed) { fixed = it }
                Spacer(Modifier.height(8.dp))
                AdminNumberField("Cancelamento", cancellation) { cancellation = it }
                Spacer(Modifier.height(8.dp))
                AdminNumberField("Espera por minuto", waiting) { waiting = it }
                Spacer(Modifier.height(8.dp))
                AdminNumberField("Adicional noturno (%)", night) { night = it }
            }
        },
        confirmButton = {
            Button(onClick = {
                onSave(rate.toDoubleOrNull() ?: platformRate, fixed.toDoubleOrNull() ?: fixedFee, cancellation.toDoubleOrNull() ?: cancellationFee, waiting.toDoubleOrNull() ?: waitingPerMinute, night.toDoubleOrNull() ?: nightSurcharge)
            }, colors = ButtonDefaults.buttonColors(containerColor = GuinchouGreen, contentColor = Color.Black)) {
                Text("Salvar", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar", color = GuinchouGray) } }
    )
}

@Composable
private fun AdminNumberField(label: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = { input -> onValueChange(input.filter { it.isDigit() || it == '.' || it == ',' }.replace(",", ".")) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        label = { Text(label) },
        colors = adminTextFieldColors(),
        shape = RoundedCornerShape(13.dp)
    )
}

@Composable
private fun PaymentCard(payment: AdminPayment, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        color = GuinchouSurface,
        shape = RoundedCornerShape(17.dp),
        border = BorderStroke(1.dp, if (payment.status == PaymentStatus.FAILED) GuinchouError.copy(alpha = 0.35f) else GuinchouBorder)
    ) {
        Row(Modifier.padding(15.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(color = paymentStatusColor(payment.status).copy(alpha = 0.10f), shape = RoundedCornerShape(12.dp)) {
                Icon(Icons.Default.CreditCard, null, tint = paymentStatusColor(payment.status), modifier = Modifier.padding(10.dp).size(22.dp))
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(payment.code, color = GuinchouWhite, fontWeight = FontWeight.Bold)
                Text(payment.customer, color = GuinchouGray, fontSize = 11.sp)
                Text(payment.date, color = GuinchouGray, fontSize = 10.sp)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(money(payment.grossValue), color = GuinchouWhite, fontWeight = FontWeight.Bold)
                Text(paymentStatusText(payment.status), color = paymentStatusColor(payment.status), fontSize = 10.sp)
            }
        }
    }
}

@Composable
private fun PayoutCard(payout: AdminPayout, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        color = GuinchouSurface,
        shape = RoundedCornerShape(17.dp),
        border = BorderStroke(1.dp, if (payout.status == PayoutStatus.FAILED) GuinchouError.copy(alpha = 0.35f) else GuinchouBorder)
    ) {
        Row(Modifier.padding(15.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(color = GuinchouGreen.copy(alpha = 0.09f), shape = RoundedCornerShape(12.dp)) {
                Icon(Icons.Default.Paid, null, tint = GuinchouGreen, modifier = Modifier.padding(10.dp).size(22.dp))
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(payout.partnerName, color = GuinchouWhite, fontWeight = FontWeight.Bold)
                Text(payout.partnerType, color = GuinchouGray, fontSize = 11.sp)
                Text("Previsto: ${payout.expectedDate}", color = GuinchouGray, fontSize = 10.sp)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(money(payout.netValue), color = GuinchouGreen, fontWeight = FontWeight.Bold)
                Text(payoutStatusText(payout.status), color = payoutStatusColor(payout.status), fontSize = 10.sp)
            }
        }
    }
}

@Composable
private fun FinanceHeroCard(grossVolume: Double, revenue: Double, partnerAmount: Double) {
    Surface(Modifier.fillMaxWidth(), color = GuinchouGreen.copy(alpha = 0.06f), shape = RoundedCornerShape(20.dp), border = BorderStroke(1.dp, GuinchouGreen.copy(alpha = 0.28f))) {
        Column(Modifier.padding(18.dp)) {
            Text("Volume bruto movimentado", color = GuinchouGray, fontSize = 12.sp)
            Text(money(grossVolume), color = GuinchouWhite, fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(16.dp))
            RuleRow("Receita Guinchou", money(revenue))
            RuleRow("Destinado aos parceiros", money(partnerAmount))
        }
    }
}

@Composable
private fun FinancialKpiCard(modifier: Modifier, title: String, value: String, icon: ImageVector) {
    Surface(modifier = modifier, color = GuinchouSurface, shape = RoundedCornerShape(16.dp), border = BorderStroke(1.dp, GuinchouBorder)) {
        Column(Modifier.padding(14.dp)) {
            Icon(icon, null, tint = GuinchouGreen, modifier = Modifier.size(22.dp))
            Spacer(Modifier.height(10.dp))
            Text(value, color = GuinchouWhite, fontWeight = FontWeight.Bold)
            Text(title, color = GuinchouGray, fontSize = 10.sp)
        }
    }
}

@Composable
private fun FinanceChartMock(values: List<Float>) {
    Surface(Modifier.fillMaxWidth(), color = GuinchouSurface, shape = RoundedCornerShape(18.dp), border = BorderStroke(1.dp, GuinchouBorder)) {
        Column(Modifier.padding(16.dp)) {
            Text("Movimentação recente", color = GuinchouWhite, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth().height(120.dp), horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.Bottom) {
                values.forEachIndexed { index, value ->
                    Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(modifier = Modifier.fillMaxWidth().fillMaxHeight(value.coerceIn(0.08f, 1f)).background(GuinchouGreen.copy(alpha = 0.75f), RoundedCornerShape(topStart = 5.dp, topEnd = 5.dp)))
                        Spacer(Modifier.height(4.dp))
                        Text("${index + 1}", color = GuinchouGray, fontSize = 9.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun ManagementRow(icon: ImageVector, title: String, subtitle: String, onClick: () -> Unit) {
    Surface(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick), color = GuinchouSurface, shape = RoundedCornerShape(15.dp), border = BorderStroke(1.dp, GuinchouBorder)) {
        Row(Modifier.fillMaxWidth().padding(15.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = GuinchouGreen, modifier = Modifier.size(23.dp))
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(title, color = GuinchouWhite, fontWeight = FontWeight.SemiBold)
                Text(subtitle, color = GuinchouGray, fontSize = 11.sp)
            }
            Icon(Icons.Default.ChevronRight, null, tint = GuinchouGray)
        }
    }
}

@Composable
private fun ManagementLargeCard(icon: ImageVector, title: String, description: String, buttonText: String, onClick: () -> Unit) {
    Surface(Modifier.fillMaxWidth(), color = GuinchouSurface, shape = RoundedCornerShape(18.dp), border = BorderStroke(1.dp, GuinchouBorder)) {
        Column(Modifier.padding(17.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(color = GuinchouGreen.copy(alpha = 0.10f), shape = RoundedCornerShape(12.dp)) {
                    Icon(icon, null, tint = GuinchouGreen, modifier = Modifier.padding(10.dp).size(23.dp))
                }
                Spacer(Modifier.width(12.dp))
                Text(title, color = GuinchouWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(10.dp))
            Text(description, color = GuinchouGray, fontSize = 12.sp, lineHeight = 18.sp)
            Spacer(Modifier.height(12.dp))
            OutlinedButton(onClick = onClick, modifier = Modifier.fillMaxWidth(), border = BorderStroke(1.dp, GuinchouBorder)) {
                Text(buttonText, color = GuinchouGreen)
                Spacer(Modifier.weight(1f))
                Icon(Icons.Default.ChevronRight, null, tint = GuinchouGreen)
            }
        }
    }
}

@Composable
private fun SettingSwitchRow(title: String, subtitle: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Surface(Modifier.fillMaxWidth().padding(bottom = 9.dp), color = GuinchouSurface, shape = RoundedCornerShape(15.dp), border = BorderStroke(1.dp, GuinchouBorder)) {
        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(title, color = GuinchouWhite, fontWeight = FontWeight.SemiBold)
                Text(subtitle, color = GuinchouGray, fontSize = 11.sp)
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
private fun SummaryCounter(modifier: Modifier, value: Int, label: String, highlighted: Boolean = false) {
    Surface(
        modifier = modifier,
        color = if (highlighted) GuinchouGreen.copy(alpha = 0.07f) else GuinchouSurface,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, if (highlighted) GuinchouGreen.copy(alpha = 0.35f) else GuinchouBorder)
    ) {
        Column(Modifier.padding(14.dp)) {
            Text(value.toString(), color = if (highlighted) GuinchouGreen else GuinchouWhite, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Text(label, color = GuinchouGray, fontSize = 11.sp, lineHeight = 14.sp)
        }
    }
}

@Composable
private fun AdminFilterChip(title: String, selected: Boolean, onClick: () -> Unit) {
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
private fun StatusPill(text: String, positive: Boolean = false, negative: Boolean = false) {
    val color = when {
        negative -> GuinchouError
        positive -> GuinchouGreen
        else -> GuinchouGray
    }
    Surface(color = color.copy(alpha = 0.10f), shape = RoundedCornerShape(100.dp)) {
        Text(text, color = color, fontSize = 10.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(horizontal = 9.dp, vertical = 6.dp))
    }
}

@Composable
private fun EmptyStateCard(icon: ImageVector, title: String, subtitle: String) {
    Surface(Modifier.fillMaxWidth(), color = GuinchouSurface, shape = RoundedCornerShape(18.dp), border = BorderStroke(1.dp, GuinchouBorder)) {
        Column(Modifier.fillMaxWidth().padding(vertical = 32.dp, horizontal = 20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Surface(color = GuinchouGreen.copy(alpha = 0.10f), shape = CircleShape) {
                Icon(icon, null, tint = GuinchouGreen, modifier = Modifier.padding(14.dp).size(28.dp))
            }
            Spacer(Modifier.height(13.dp))
            Text(title, color = GuinchouWhite, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(3.dp))
            Text(subtitle, color = GuinchouGray, fontSize = 12.sp)
        }
    }
}

@Composable
private fun InfoBanner(title: String, subtitle: String) {
    Surface(Modifier.fillMaxWidth(), color = Color.White.copy(alpha = 0.035f), shape = RoundedCornerShape(13.dp), border = BorderStroke(1.dp, GuinchouBorder)) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Info, null, tint = GuinchouGreen, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(9.dp))
            Column {
                Text(title, color = GuinchouWhite, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                Text(subtitle, color = GuinchouGray, fontSize = 11.sp, lineHeight = 16.sp)
            }
        }
    }
}

@Composable
private fun DialogInfo(title: String, value: String) {
    Text(title, color = GuinchouGray, fontSize = 11.sp)
    Text(value, color = GuinchouWhite, fontSize = 13.sp, fontWeight = FontWeight.Medium, lineHeight = 18.sp)
    Spacer(Modifier.height(10.dp))
}

@Composable
private fun RuleRow(title: String, value: String) {
    Row(Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
        Text(title, color = GuinchouGray, modifier = Modifier.weight(1f))
        Text(value, color = GuinchouWhite, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun ConfirmationDialog(title: String, message: String, confirmText: String, onDismiss: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = GuinchouSurface,
        title = { Text(title, color = GuinchouWhite, fontWeight = FontWeight.Bold) },
        text = { Text(message, color = GuinchouGray) },
        confirmButton = {
            Button(onClick = onConfirm, colors = ButtonDefaults.buttonColors(containerColor = GuinchouGreen, contentColor = Color.Black)) {
                Text(confirmText, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar", color = GuinchouGray) } }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ReasonDialog(
    title: String,
    hint: String,
    confirmText: String,
    destructive: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var reason by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = GuinchouSurface,
        title = { Text(title, color = GuinchouWhite, fontWeight = FontWeight.Bold) },
        text = {
            OutlinedTextField(
                value = reason,
                onValueChange = { reason = it },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                placeholder = { Text(hint) },
                colors = adminTextFieldColors(),
                shape = RoundedCornerShape(13.dp)
            )
        },
        confirmButton = {
            Button(
                onClick = { if (reason.isNotBlank()) onConfirm(reason) },
                enabled = reason.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (destructive) GuinchouError else GuinchouGreen,
                    contentColor = if (destructive) Color.White else Color.Black
                )
            ) { Text(confirmText, fontWeight = FontWeight.Bold) }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar", color = GuinchouGray) } }
    )
}

@Composable
private fun adminTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = GuinchouWhite,
    unfocusedTextColor = GuinchouWhite,
    cursorColor = GuinchouGreen,
    focusedBorderColor = GuinchouGreen,
    unfocusedBorderColor = GuinchouBorder,
    focusedLeadingIconColor = GuinchouGreen,
    unfocusedLeadingIconColor = GuinchouGray,
    focusedPlaceholderColor = GuinchouGray,
    unfocusedPlaceholderColor = GuinchouGray,
    focusedLabelColor = GuinchouGreen,
    unfocusedLabelColor = GuinchouGray
)

private fun approvalTypeText(type: ApprovalType): String = when (type) {
    ApprovalType.INDEPENDENT_DRIVER -> "Motorista independente"
    ApprovalType.COMPANY -> "Empresa"
    ApprovalType.COMPANY_DRIVER -> "Motorista de empresa"
    ApprovalType.TOW_TRUCK -> "Guincho"
}

private fun approvalTypeIcon(type: ApprovalType): ImageVector = when (type) {
    ApprovalType.INDEPENDENT_DRIVER -> Icons.Default.Badge
    ApprovalType.COMPANY -> Icons.Default.Business
    ApprovalType.COMPANY_DRIVER -> Icons.Default.Person
    ApprovalType.TOW_TRUCK -> Icons.Default.LocalShipping
}

private fun approvalStatusText(status: ApprovalStatus): String = when (status) {
    ApprovalStatus.PENDING -> "Pendente"
    ApprovalStatus.REVIEWING -> "Em análise"
    ApprovalStatus.APPROVED -> "Aprovado"
    ApprovalStatus.REJECTED -> "Rejeitado"
    ApprovalStatus.CORRECTION -> "Correção"
}

private fun userTypeText(type: UserType): String = when (type) {
    UserType.CUSTOMER -> "Cliente"
    UserType.DRIVER -> "Motorista independente"
    UserType.COMPANY -> "Empresa"
    UserType.COMPANY_DRIVER -> "Motorista de empresa"
}

private fun userTypeIcon(type: UserType): ImageVector = when (type) {
    UserType.CUSTOMER -> Icons.Default.Person
    UserType.DRIVER -> Icons.Default.Badge
    UserType.COMPANY -> Icons.Default.Business
    UserType.COMPANY_DRIVER -> Icons.Default.ManageAccounts
}

private fun userStatusText(status: UserStatus): String = when (status) {
    UserStatus.ACTIVE -> "Ativo"
    UserStatus.SUSPENDED -> "Suspenso"
    UserStatus.BLOCKED -> "Bloqueado"
}

private fun callStatusText(status: CallStatus): String = when (status) {
    CallStatus.NEW -> "Novo"
    CallStatus.SEARCHING -> "Buscando parceiro"
    CallStatus.ACCEPTED -> "Aceito"
    CallStatus.DRIVER_ON_THE_WAY -> "A caminho"
    CallStatus.IN_TRANSIT -> "Em transporte"
    CallStatus.COMPLETED -> "Concluído"
    CallStatus.CANCELLED -> "Cancelado"
    CallStatus.DISPUTE -> "Em disputa"
}

private fun paymentStatusText(status: PaymentStatus): String = when (status) {
    PaymentStatus.PAID -> "Pago"
    PaymentStatus.PROCESSING -> "Processando"
    PaymentStatus.PENDING -> "Pendente"
    PaymentStatus.FAILED -> "Falhou"
    PaymentStatus.CANCELLED -> "Cancelado"
    PaymentStatus.REFUNDED -> "Reembolsado"
    PaymentStatus.ANALYSIS -> "Em análise"
}

private fun paymentStatusColor(status: PaymentStatus): Color = when (status) {
    PaymentStatus.PAID -> GuinchouGreen
    PaymentStatus.PROCESSING, PaymentStatus.PENDING, PaymentStatus.ANALYSIS -> GuinchouWhite
    PaymentStatus.FAILED, PaymentStatus.CANCELLED -> GuinchouError
    PaymentStatus.REFUNDED -> GuinchouGray
}

private fun payoutStatusText(status: PayoutStatus): String = when (status) {
    PayoutStatus.WAITING -> "Aguardando"
    PayoutStatus.PROCESSING -> "Processando"
    PayoutStatus.PAID -> "Pago"
    PayoutStatus.FAILED -> "Falhou"
    PayoutStatus.BLOCKED -> "Bloqueado"
}

private fun payoutStatusColor(status: PayoutStatus): Color = when (status) {
    PayoutStatus.PAID -> GuinchouGreen
    PayoutStatus.WAITING, PayoutStatus.PROCESSING -> GuinchouWhite
    PayoutStatus.FAILED, PayoutStatus.BLOCKED -> GuinchouError
}

private fun money(value: Double): String = "R$ ${"%.2f".format(value)}"

private fun formatNumber(value: Double): String = if (value % 1.0 == 0.0) value.toInt().toString() else "%.2f".format(value)
