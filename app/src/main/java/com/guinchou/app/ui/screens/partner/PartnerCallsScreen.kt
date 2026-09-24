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
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.FormatListBulleted
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import android.content.Intent
import android.net.Uri
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.guinchou.app.ui.theme.*

private val CallsCard = Color(0xFF0E1A2A)
private val CallsCardSoft = Color(0xFF112033)
private val CallsMuted = Color(0xFF8998AD)
private val CallsBorder = Color(0xFF1B2B3F)
private val CallsWarning = Color(0xFFFFB547)
private val CallsDanger = Color(0xFFFF6B6B)

private enum class CallFlowStage {
    AVAILABLE,
    GOING_TO_CUSTOMER,
    AT_CUSTOMER,
    VEHICLE_COLLECTED,
    TRANSPORTING,
    COMPLETED
}

private data class PartnerCall(
    val id: String,
    val problem: String,
    val vehicle: String,
    val pickup: String,
    val destination: String,
    val distance: String,
    val price: String,
    val time: String,
    val totalDistance: String,
    val payment: String,
    val observation: String,
    val customerName: String,
    val customerPhone: String
)

@Composable
fun PartnerCallsScreen(
    onBackClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    onCallClick: (String) -> Unit = {},
    onHomeClick: () -> Unit = {},
    onEarningsClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    var selectedCall by remember { mutableStateOf<PartnerCall?>(null) }
    var activeCall by remember { mutableStateOf<PartnerCall?>(null) }
    var flowStage by remember { mutableStateOf(CallFlowStage.AVAILABLE) }
    var showCompletionDialog by remember { mutableStateOf(false) }

    val calls = remember {
        listOf(
            PartnerCall(
                id = "CH-1048",
                problem = "Pane mecânica",
                vehicle = "Honda Civic • Prata",
                pickup = "Asa Norte, Brasília - DF",
                destination = "SIA, Brasília - DF",
                distance = "3,2 km",
                price = "R$ 150,00",
                time = "Agora",
                totalDistance = "9,6 km",
                payment = "Cartão pelo aplicativo",
                observation = "Veículo não liga. Cliente informou que está estacionado em local seguro.",
                customerName = "Carlos Eduardo",
                customerPhone = "(61) 99999-0000"
            ),
            PartnerCall(
                id = "CH-1047",
                problem = "Veículo não liga",
                vehicle = "Volkswagen Polo • Branco",
                pickup = "Sudoeste, Brasília - DF",
                destination = "Taguatinga, Brasília - DF",
                distance = "6,8 km",
                price = "R$ 150,00",
                time = "2 min",
                totalDistance = "18,4 km",
                payment = "PIX pelo aplicativo",
                observation = "Cliente relata falha na partida. Veículo está em estacionamento aberto.",
                customerName = "Mariana Souza",
                customerPhone = "(61) 98888-1111"
            ),
            PartnerCall(
                id = "CH-1046",
                problem = "Acidente",
                vehicle = "Chevrolet Onix • Preto",
                pickup = "Águas Claras, Brasília - DF",
                destination = "Guará, Brasília - DF",
                distance = "11,4 km",
                price = "R$ 200,00",
                time = "5 min",
                totalDistance = "16,7 km",
                payment = "Cartão pelo aplicativo",
                observation = "Solicitação relacionada a acidente. Verifique as condições do veículo antes do transporte.",
                customerName = "Lucas Almeida",
                customerPhone = "(61) 97777-2222"
            )
        )
    }

    if (activeCall == null) {
        AvailableCallsContent(
            calls = calls,
            onBackClick = onBackClick,
            onNotificationsClick = onNotificationsClick,
            onCallSelected = { selectedCall = it },
            onHomeClick = onHomeClick,
            onEarningsClick = onEarningsClick,
            onProfileClick = onProfileClick
        )
    } else {
        ActiveCallContent(
            call = activeCall!!,
            stage = flowStage,
            onBackClick = onBackClick,
            onAdvanceStage = {
                flowStage = when (flowStage) {
                    CallFlowStage.AVAILABLE -> CallFlowStage.GOING_TO_CUSTOMER
                    CallFlowStage.GOING_TO_CUSTOMER -> CallFlowStage.AT_CUSTOMER
                    CallFlowStage.AT_CUSTOMER -> CallFlowStage.VEHICLE_COLLECTED
                    CallFlowStage.VEHICLE_COLLECTED -> CallFlowStage.TRANSPORTING
                    CallFlowStage.TRANSPORTING -> {
                        showCompletionDialog = true
                        CallFlowStage.COMPLETED
                    }
                    CallFlowStage.COMPLETED -> CallFlowStage.COMPLETED
                }
            }
        )
    }

    selectedCall?.let { call ->
        CallDetailsDialog(
            call = call,
            onDismiss = { selectedCall = null },
            onReject = { selectedCall = null },
            onAccept = {
                activeCall = call
                flowStage = CallFlowStage.GOING_TO_CUSTOMER
                selectedCall = null
                onCallClick(call.id)
            }
        )
    }

    if (showCompletionDialog && activeCall != null) {
        CompletionDialog(
            call = activeCall!!,
            onFinish = {
                showCompletionDialog = false
                activeCall = null
                flowStage = CallFlowStage.AVAILABLE
            }
        )
    }
}

@Composable
private fun AvailableCallsContent(
    calls: List<PartnerCall>,
    onBackClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onCallSelected: (PartnerCall) -> Unit,
    onHomeClick: () -> Unit,
    onEarningsClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    Column(
        Modifier
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
            Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(12.dp))
            CallsHeader(onBackClick, onNotificationsClick)
            Spacer(Modifier.height(23.dp))

            Text(
                "Chamados próximos",
                color = GuinchouWhite,
                fontSize = 25.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Spacer(Modifier.height(5.dp))
            Text(
                "Escolha um chamado para visualizar os detalhes do atendimento.",
                color = CallsMuted,
                fontSize = 12.sp,
                lineHeight = 17.sp
            )

            Spacer(Modifier.height(18.dp))
            AvailabilityCard()
            Spacer(Modifier.height(22.dp))

            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Disponíveis agora",
                    color = GuinchouWhite,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Box(
                    Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(GuinchouGreen.copy(alpha = .11f))
                        .border(1.dp, GuinchouGreen.copy(alpha = .25f), RoundedCornerShape(20.dp))
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(
                        "${calls.size} chamados",
                        color = GuinchouGreen,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            calls.forEachIndexed { index, call ->
                CallCard(call) { onCallSelected(call) }
                if (index != calls.lastIndex) Spacer(Modifier.height(12.dp))
            }

            Spacer(Modifier.height(25.dp))
        }

        CallsBottomBar(onHomeClick, onEarningsClick, onProfileClick)
    }
}

@Composable
private fun ActiveCallContent(
    call: PartnerCall,
    stage: CallFlowStage,
    onBackClick: () -> Unit,
    onAdvanceStage: () -> Unit
) {
    Column(
        Modifier
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
            Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(12.dp))

            Row(
                Modifier.fillMaxWidth().height(44.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HeaderButton(Icons.AutoMirrored.Filled.ArrowBack, "Voltar", onBackClick)
                Text(
                    "Atendimento",
                    color = GuinchouWhite,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f).padding(start = 14.dp)
                )
                Box(
                    Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(GuinchouGreen.copy(alpha = .11f))
                        .border(1.dp, GuinchouGreen.copy(alpha = .25f), RoundedCornerShape(20.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(call.id, color = GuinchouGreen, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(Modifier.height(22.dp))
            ActiveStatusCard(stage)
            Spacer(Modifier.height(18.dp))
            ActiveRouteCard(call, stage)
            Spacer(Modifier.height(18.dp))

            SectionTitle("Cliente")
            Spacer(Modifier.height(10.dp))
            CustomerCard(call.customerName, call.customerPhone)

            Spacer(Modifier.height(18.dp))
            SectionTitle("Detalhes do atendimento")
            Spacer(Modifier.height(10.dp))
            ActiveServiceDetails(call)

            Spacer(Modifier.height(18.dp))
            SectionTitle("Progresso do chamado")
            Spacer(Modifier.height(10.dp))
            ProgressCard(stage)

            Spacer(Modifier.height(22.dp))
        }

        ActiveActionBar(stage, onAdvanceStage)
    }
}

@Composable
private fun ActiveStatusCard(stage: CallFlowStage) {
    val title = when (stage) {
        CallFlowStage.GOING_TO_CUSTOMER -> "A caminho do cliente"
        CallFlowStage.AT_CUSTOMER -> "Você chegou ao cliente"
        CallFlowStage.VEHICLE_COLLECTED -> "Veículo recolhido"
        CallFlowStage.TRANSPORTING -> "Transportando veículo"
        CallFlowStage.COMPLETED -> "Serviço finalizado"
        CallFlowStage.AVAILABLE -> "Chamado aceito"
    }

    val subtitle = when (stage) {
        CallFlowStage.GOING_TO_CUSTOMER -> "Siga até o local de origem informado pelo cliente."
        CallFlowStage.AT_CUSTOMER -> "Confira o veículo e prepare-o para o recolhimento."
        CallFlowStage.VEHICLE_COLLECTED -> "Confirme a segurança do veículo antes de iniciar o transporte."
        CallFlowStage.TRANSPORTING -> "Siga até o destino definido no chamado."
        CallFlowStage.COMPLETED -> "Atendimento concluído com sucesso."
        CallFlowStage.AVAILABLE -> "Prepare-se para iniciar o atendimento."
    }

    val icon = when (stage) {
        CallFlowStage.GOING_TO_CUSTOMER -> Icons.Default.Navigation
        CallFlowStage.AT_CUSTOMER -> Icons.Default.LocationOn
        CallFlowStage.VEHICLE_COLLECTED -> Icons.Default.LocalShipping
        CallFlowStage.TRANSPORTING -> Icons.Default.Route
        CallFlowStage.COMPLETED -> Icons.Default.CheckCircle
        CallFlowStage.AVAILABLE -> Icons.Default.Check
    }

    Row(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.horizontalGradient(
                    listOf(GuinchouGreen.copy(alpha = .15f), CallsCardSoft, CallsCard)
                )
            )
            .border(1.dp, GuinchouGreen.copy(alpha = .35f), RoundedCornerShape(20.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier.size(48.dp).clip(CircleShape).background(GuinchouGreen.copy(alpha = .13f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = GuinchouGreen, modifier = Modifier.size(25.dp))
        }
        Spacer(Modifier.width(13.dp))
        Column(Modifier.weight(1f)) {
            Text(title, color = GuinchouWhite, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold)
            Spacer(Modifier.height(3.dp))
            Text(subtitle, color = CallsMuted, fontSize = 10.sp, lineHeight = 14.sp)
        }
        Box(Modifier.size(9.dp).background(GuinchouGreen, CircleShape))
    }
}

@Composable
private fun ActiveRouteCard(call: PartnerCall, stage: CallFlowStage) {
    val context = LocalContext.current

    val routeDestination = when (stage) {
        CallFlowStage.GOING_TO_CUSTOMER,
        CallFlowStage.AT_CUSTOMER -> call.pickup

        CallFlowStage.VEHICLE_COLLECTED,
        CallFlowStage.TRANSPORTING,
        CallFlowStage.COMPLETED -> call.destination

        CallFlowStage.AVAILABLE -> call.pickup
    }

    fun openRouteInMaps() {
        val encodedDestination = Uri.encode(routeDestination)
        val navigationUri = Uri.parse(
            "https://www.google.com/maps/dir/?api=1&destination=$encodedDestination&travelmode=driving"
        )

        val mapIntent = Intent(Intent.ACTION_VIEW, navigationUri)

        try {
            context.startActivity(Intent.createChooser(mapIntent, "Abrir rota com"))
        } catch (_: Exception) {
            val searchUri = Uri.parse("geo:0,0?q=$encodedDestination")
            context.startActivity(
                Intent.createChooser(
                    Intent(Intent.ACTION_VIEW, searchUri),
                    "Abrir mapa com"
                )
            )
        }
    }

    Column(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(CallsCard)
            .border(1.dp, CallsBorder, RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                if (stage == CallFlowStage.TRANSPORTING) "Rota até o destino" else "Rota do atendimento",
                color = GuinchouWhite,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            Icon(Icons.Default.Map, null, tint = GuinchouGreen, modifier = Modifier.size(20.dp))
        }

        Spacer(Modifier.height(16.dp))
        DetailRouteItem(GuinchouGreen, Icons.Default.MyLocation, "Origem", call.pickup)
        Box(Modifier.padding(start = 18.dp).height(20.dp).width(1.dp).background(CallsBorder))
        DetailRouteItem(CallsWarning, Icons.Default.LocationOn, "Destino", call.destination)

        Spacer(Modifier.height(15.dp))
        HorizontalDivider(color = CallsBorder)
        Spacer(Modifier.height(13.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(9.dp)) {
            SmallInfoCard(Modifier.weight(1f), Icons.Default.NearMe, "Até o cliente", call.distance)
            SmallInfoCard(Modifier.weight(1f), Icons.Default.Route, "Percurso", call.totalDistance)
        }

        Spacer(Modifier.height(12.dp))

        Button(
            onClick = { openRouteInMaps() },
            modifier = Modifier.fillMaxWidth().height(44.dp),
            shape = RoundedCornerShape(13.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = GuinchouGreen.copy(alpha = .12f),
                contentColor = GuinchouGreen
            )
        ) {
            Icon(Icons.Default.Navigation, null, modifier = Modifier.size(17.dp))
            Spacer(Modifier.width(7.dp))
            Text("Abrir rota no mapa", fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun CustomerCard(name: String, phone: String) {
    Column(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(CallsCard)
            .border(1.dp, CallsBorder, RoundedCornerShape(20.dp))
            .padding(15.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier.size(44.dp).clip(CircleShape).background(GuinchouGreen.copy(alpha = .12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Person, null, tint = GuinchouGreen, modifier = Modifier.size(23.dp))
            }
            Spacer(Modifier.width(11.dp))
            Column(Modifier.weight(1f)) {
                Text(name, color = GuinchouWhite, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Text(phone, color = CallsMuted, fontSize = 10.sp)
            }
            Box(
                Modifier.size(37.dp).clip(RoundedCornerShape(11.dp))
                    .background(GuinchouGreen.copy(alpha = .10f)).clickable {},
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Phone, "Ligar", tint = GuinchouGreen, modifier = Modifier.size(18.dp))
            }
            Spacer(Modifier.width(7.dp))
            Box(
                Modifier.size(37.dp).clip(RoundedCornerShape(11.dp))
                    .background(GuinchouGreen.copy(alpha = .10f)).clickable {},
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.AutoMirrored.Filled.Chat, "Mensagem", tint = GuinchouGreen, modifier = Modifier.size(18.dp))
            }
        }
    }
}

@Composable
private fun ActiveServiceDetails(call: PartnerCall) {
    Column(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(CallsCard)
            .border(1.dp, CallsBorder, RoundedCornerShape(20.dp))
            .padding(15.dp)
    ) {
        ActiveDetailRow(Icons.Default.Build, "Problema", call.problem)
        HorizontalDivider(Modifier.padding(vertical = 11.dp), color = CallsBorder)
        ActiveDetailRow(Icons.Default.DirectionsCar, "Veículo", call.vehicle)
        HorizontalDivider(Modifier.padding(vertical = 11.dp), color = CallsBorder)
        ActiveDetailRow(Icons.Default.AccountBalanceWallet, "Pagamento", call.payment)
        HorizontalDivider(Modifier.padding(vertical = 11.dp), color = CallsBorder)
        ActiveDetailRow(Icons.Default.Payments, "Valor", call.price, GuinchouGreen)

        Spacer(Modifier.height(14.dp))

        Row(
            Modifier.fillMaxWidth().clip(RoundedCornerShape(13.dp))
                .background(Color(0xFF091421)).padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(Icons.Default.Info, null, tint = GuinchouGreen, modifier = Modifier.size(17.dp))
            Spacer(Modifier.width(8.dp))
            Text(call.observation, color = CallsMuted, fontSize = 9.sp, lineHeight = 13.sp)
        }
    }
}

@Composable
private fun ActiveDetailRow(
    icon: ImageVector,
    label: String,
    value: String,
    valueColor: Color = GuinchouWhite
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = GuinchouGreen, modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(10.dp))
        Text(label, color = CallsMuted, fontSize = 10.sp, modifier = Modifier.weight(1f))
        Text(value, color = valueColor, fontSize = 10.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun ProgressCard(stage: CallFlowStage) {
    val steps = listOf(
        Triple("Chamado aceito", "Solicitação confirmada", Icons.Default.CheckCircle),
        Triple("Chegada ao cliente", "Deslocamento até a origem", Icons.Default.LocationOn),
        Triple("Veículo recolhido", "Veículo carregado no guincho", Icons.Default.LocalShipping),
        Triple("Transporte", "Deslocamento até o destino", Icons.Default.Route),
        Triple("Finalização", "Entrega e conclusão", Icons.Default.Flag)
    )

    val currentIndex = when (stage) {
        CallFlowStage.GOING_TO_CUSTOMER -> 0
        CallFlowStage.AT_CUSTOMER -> 1
        CallFlowStage.VEHICLE_COLLECTED -> 2
        CallFlowStage.TRANSPORTING -> 3
        CallFlowStage.COMPLETED -> 4
        CallFlowStage.AVAILABLE -> 0
    }

    Column(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(CallsCard)
            .border(1.dp, CallsBorder, RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {
        steps.forEachIndexed { index, step ->
            val completed = index <= currentIndex
            Row(verticalAlignment = Alignment.Top) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        Modifier.size(34.dp).clip(CircleShape)
                            .background(if (completed) GuinchouGreen.copy(alpha = .14f) else Color(0xFF142235))
                            .border(
                                1.dp,
                                if (completed) GuinchouGreen.copy(alpha = .45f) else CallsBorder,
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            step.third,
                            null,
                            tint = if (completed) GuinchouGreen else CallsMuted,
                            modifier = Modifier.size(17.dp)
                        )
                    }
                    if (index != steps.lastIndex) {
                        Box(
                            Modifier.width(1.dp).height(25.dp)
                                .background(if (index < currentIndex) GuinchouGreen.copy(alpha = .5f) else CallsBorder)
                        )
                    }
                }

                Spacer(Modifier.width(11.dp))
                Column(Modifier.padding(top = 2.dp)) {
                    Text(
                        step.first,
                        color = if (completed) GuinchouWhite else CallsMuted,
                        fontSize = 11.sp,
                        fontWeight = if (completed) FontWeight.Bold else FontWeight.Medium
                    )
                    Text(step.second, color = CallsMuted, fontSize = 8.sp)
                }
            }
        }
    }
}

@Composable
private fun ActiveActionBar(stage: CallFlowStage, onAdvance: () -> Unit) {
    val (text, icon) = when (stage) {
        CallFlowStage.GOING_TO_CUSTOMER -> "Cheguei ao cliente" to Icons.Default.LocationOn
        CallFlowStage.AT_CUSTOMER -> "Confirmar veículo recolhido" to Icons.Default.LocalShipping
        CallFlowStage.VEHICLE_COLLECTED -> "Iniciar transporte" to Icons.Default.Navigation
        CallFlowStage.TRANSPORTING -> "Finalizar serviço" to Icons.Default.CheckCircle
        CallFlowStage.COMPLETED -> "Serviço concluído" to Icons.Default.CheckCircle
        CallFlowStage.AVAILABLE -> "Iniciar atendimento" to Icons.Default.PlayArrow
    }

    Box(
        Modifier.fillMaxWidth().background(Color(0xFF08121E))
            .border(1.dp, CallsBorder.copy(alpha = .85f))
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        Button(
            onClick = onAdvance,
            enabled = stage != CallFlowStage.COMPLETED,
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(15.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = GuinchouGreen,
                contentColor = Color(0xFF06101A),
                disabledContainerColor = GuinchouGreen.copy(alpha = .35f),
                disabledContentColor = Color(0xFF06101A).copy(alpha = .6f)
            )
        ) {
            Icon(icon, null, modifier = Modifier.size(19.dp))
            Spacer(Modifier.width(8.dp))
            Text(text, fontSize = 12.sp, fontWeight = FontWeight.ExtraBold)
        }
    }
}

@Composable
private fun CompletionDialog(call: PartnerCall, onFinish: () -> Unit) {
    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            Modifier.fillMaxSize().background(Color.Black.copy(alpha = .72f)).padding(22.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                Modifier.fillMaxWidth().clip(RoundedCornerShape(25.dp))
                    .background(
                        Brush.verticalGradient(listOf(Color(0xFF122033), Color(0xFF091522)))
                    )
                    .border(1.dp, GuinchouGreen.copy(alpha = .35f), RoundedCornerShape(25.dp))
                    .padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    Modifier.size(70.dp).clip(CircleShape)
                        .background(GuinchouGreen.copy(alpha = .13f))
                        .border(1.dp, GuinchouGreen.copy(alpha = .4f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.CheckCircle, null, tint = GuinchouGreen, modifier = Modifier.size(38.dp))
                }

                Spacer(Modifier.height(17.dp))
                Text(
                    "Serviço concluído!",
                    color = GuinchouWhite,
                    fontSize = 21.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    "O atendimento ${call.id} foi finalizado com sucesso.",
                    color = CallsMuted,
                    fontSize = 10.sp
                )

                Spacer(Modifier.height(20.dp))

                Row(
                    Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp))
                        .background(GuinchouGreen.copy(alpha = .08f))
                        .border(1.dp, GuinchouGreen.copy(alpha = .18f), RoundedCornerShape(16.dp))
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(Modifier.weight(1f)) {
                        Text("Valor do serviço", color = CallsMuted, fontSize = 9.sp)
                        Text(call.price, color = GuinchouGreen, fontSize = 19.sp, fontWeight = FontWeight.ExtraBold)
                    }
                    Icon(Icons.Default.Payments, null, tint = GuinchouGreen, modifier = Modifier.size(26.dp))
                }

                Spacer(Modifier.height(11.dp))

                InformationRow(Icons.Default.DirectionsCar, "Veículo", call.vehicle)
                Spacer(Modifier.height(8.dp))
                InformationRow(Icons.Default.LocationOn, "Destino final", call.destination)

                Spacer(Modifier.height(20.dp))

                Button(
                    onClick = onFinish,
                    modifier = Modifier.fillMaxWidth().height(49.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GuinchouGreen,
                        contentColor = Color(0xFF06101A)
                    )
                ) {
                    Icon(Icons.Default.DoneAll, null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(7.dp))
                    Text("Concluir atendimento", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold)
                }
            }
        }
    }
}

/* ---------- COMPONENTES DA TELA DE CHAMADOS ---------- */

@Composable
private fun CallsHeader(onBackClick: () -> Unit, onNotificationsClick: () -> Unit) {
    Row(Modifier.fillMaxWidth().height(44.dp), verticalAlignment = Alignment.CenterVertically) {
        HeaderButton(Icons.AutoMirrored.Filled.ArrowBack, "Voltar", onBackClick)
        Text(
            "Chamados",
            color = GuinchouWhite,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f).padding(start = 14.dp)
        )
        Box(
            Modifier.size(42.dp).clip(RoundedCornerShape(13.dp)).background(CallsCard)
                .border(1.dp, CallsBorder, RoundedCornerShape(13.dp))
                .clickable(onClick = onNotificationsClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Notifications, "com/guinchou/app/ui/screens/Notificações", tint = GuinchouWhite, modifier = Modifier.size(20.dp))
            Box(
                Modifier.align(Alignment.TopEnd).offset(x = (-5).dp, y = 5.dp).size(8.dp)
                    .background(GuinchouGreen, CircleShape).border(2.dp, CallsCard, CircleShape)
            )
        }
    }
}

@Composable
private fun HeaderButton(icon: ImageVector, desc: String, onClick: () -> Unit) =
    Box(
        Modifier.size(42.dp).clip(RoundedCornerShape(13.dp)).background(CallsCard)
            .border(1.dp, CallsBorder, RoundedCornerShape(13.dp)).clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, desc, tint = GuinchouWhite, modifier = Modifier.size(20.dp))
    }

@Composable
private fun AvailabilityCard() {
    Row(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(18.dp))
            .background(
                Brush.horizontalGradient(
                    listOf(GuinchouGreen.copy(alpha = .13f), CallsCardSoft, CallsCard)
                )
            )
            .border(1.dp, GuinchouGreen.copy(alpha = .32f), RoundedCornerShape(18.dp))
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier.size(42.dp).clip(CircleShape).background(GuinchouGreen.copy(alpha = .13f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Radar, null, tint = GuinchouGreen, modifier = Modifier.size(23.dp))
        }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text("Você está disponível", color = GuinchouWhite, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(2.dp))
            Text("Buscando solicitações próximas à sua região", color = CallsMuted, fontSize = 10.sp)
        }
        Box(Modifier.size(9.dp).background(GuinchouGreen, CircleShape))
    }
}

@Composable
private fun CallCard(call: PartnerCall, onClick: () -> Unit) {
    Column(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp))
            .background(
                Brush.verticalGradient(listOf(Color(0xFF111E2F), Color(0xFF0C1725)))
            )
            .border(1.dp, CallsBorder, RoundedCornerShape(20.dp))
            .clickable(onClick = onClick).padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier.size(43.dp).clip(RoundedCornerShape(13.dp))
                    .background(GuinchouGreen.copy(alpha = .11f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.LocalShipping, null, tint = GuinchouGreen, modifier = Modifier.size(23.dp))
            }
            Spacer(Modifier.width(11.dp))
            Column(Modifier.weight(1f)) {
                Text(call.problem, color = GuinchouWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(2.dp))
                Text(call.vehicle, color = CallsMuted, fontSize = 10.sp)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(call.price, color = GuinchouGreen, fontSize = 14.sp, fontWeight = FontWeight.ExtraBold)
                Text(call.time, color = CallsMuted, fontSize = 9.sp)
            }
        }

        Spacer(Modifier.height(15.dp))
        HorizontalDivider(color = CallsBorder.copy(alpha = .8f))
        Spacer(Modifier.height(14.dp))

        RouteLine(GuinchouGreen, "Origem", call.pickup)
        Box(Modifier.padding(start = 5.dp).height(13.dp).width(1.dp).background(CallsBorder))
        RouteLine(CallsWarning, "Destino", call.destination)

        Spacer(Modifier.height(15.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.NearMe, null, tint = CallsMuted, modifier = Modifier.size(15.dp))
            Spacer(Modifier.width(5.dp))
            Text("${call.distance} de você", color = CallsMuted, fontSize = 10.sp, modifier = Modifier.weight(1f))
            Text("Ver detalhes", color = GuinchouGreen, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.width(3.dp))
            Icon(Icons.Default.ChevronRight, null, tint = GuinchouGreen, modifier = Modifier.size(17.dp))
        }
    }
}

@Composable
private fun CallDetailsDialog(
    call: PartnerCall,
    onDismiss: () -> Unit,
    onReject: () -> Unit,
    onAccept: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            Modifier.fillMaxSize().background(Color.Black.copy(alpha = .60f))
                .padding(horizontal = 18.dp, vertical = 28.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                Modifier.fillMaxWidth().heightIn(max = 700.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.verticalGradient(listOf(Color(0xFF122033), Color(0xFF0A1523)))
                    )
                    .border(1.dp, GuinchouGreen.copy(alpha = .30f), RoundedCornerShape(24.dp))
                    .verticalScroll(rememberScrollState()).padding(18.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        Modifier.size(46.dp).clip(RoundedCornerShape(14.dp))
                            .background(GuinchouGreen.copy(alpha = .12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.LocalShipping, null, tint = GuinchouGreen, modifier = Modifier.size(25.dp))
                    }
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text("Detalhes do chamado", color = GuinchouWhite, fontSize = 17.sp, fontWeight = FontWeight.ExtraBold)
                        Spacer(Modifier.height(2.dp))
                        Text(call.id, color = CallsMuted, fontSize = 10.sp)
                    }
                    Box(
                        Modifier.size(35.dp).clip(CircleShape).background(Color(0xFF172638))
                            .clickable(onClick = onDismiss),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Close, "Fechar", tint = GuinchouWhite, modifier = Modifier.size(18.dp))
                    }
                }

                Spacer(Modifier.height(17.dp))

                Row(
                    Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp))
                        .background(GuinchouGreen.copy(alpha = .08f))
                        .border(1.dp, GuinchouGreen.copy(alpha = .18f), RoundedCornerShape(16.dp))
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(Modifier.weight(1f)) {
                        Text(call.problem, color = GuinchouWhite, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(3.dp))
                        Text(call.vehicle, color = CallsMuted, fontSize = 10.sp)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(call.price, color = GuinchouGreen, fontSize = 17.sp, fontWeight = FontWeight.ExtraBold)
                        Text("valor estimado", color = CallsMuted, fontSize = 8.sp)
                    }
                }

                Spacer(Modifier.height(17.dp))
                SectionTitle("Rota")
                Spacer(Modifier.height(11.dp))

                DetailRouteItem(GuinchouGreen, Icons.Default.MyLocation, "Origem", call.pickup)
                Box(Modifier.padding(start = 18.dp).height(17.dp).width(1.dp).background(CallsBorder))
                DetailRouteItem(CallsWarning, Icons.Default.LocationOn, "Destino", call.destination)

                Spacer(Modifier.height(17.dp))

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(9.dp)) {
                    SmallInfoCard(Modifier.weight(1f), Icons.Default.NearMe, "Até o cliente", call.distance)
                    SmallInfoCard(Modifier.weight(1f), Icons.Default.Route, "Percurso", call.totalDistance)
                }

                Spacer(Modifier.height(17.dp))
                SectionTitle("Pagamento")
                Spacer(Modifier.height(9.dp))
                InformationRow(Icons.Default.AccountBalanceWallet, "Forma de pagamento", call.payment)

                Spacer(Modifier.height(17.dp))
                SectionTitle("Observações")
                Spacer(Modifier.height(9.dp))

                Row(
                    Modifier.fillMaxWidth().clip(RoundedCornerShape(15.dp))
                        .background(Color(0xFF0A1522)).border(1.dp, CallsBorder, RoundedCornerShape(15.dp))
                        .padding(13.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(Icons.Default.Info, null, tint = GuinchouGreen, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(9.dp))
                    Text(call.observation, color = GuinchouWhite.copy(alpha = .82f), fontSize = 10.sp, lineHeight = 15.sp)
                }

                Spacer(Modifier.height(13.dp))

                Row(
                    Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp))
                        .background(Color(0xFF0A1522)).padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Lock, null, tint = CallsMuted, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "Os dados de contato do cliente serão liberados após aceitar o chamado.",
                        color = CallsMuted,
                        fontSize = 9.sp,
                        lineHeight = 13.sp
                    )
                }

                Spacer(Modifier.height(19.dp))

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedButton(
                        onClick = onReject,
                        modifier = Modifier.weight(1f).height(48.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = CallsDanger)
                    ) {
                        Text("Recusar", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = onAccept,
                        modifier = Modifier.weight(1.45f).height(48.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GuinchouGreen,
                            contentColor = Color(0xFF06101A)
                        )
                    ) {
                        Icon(Icons.Default.CheckCircle, null, modifier = Modifier.size(17.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("Aceitar chamado", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold)
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(title, color = GuinchouWhite, fontSize = 12.sp, fontWeight = FontWeight.Bold)
}

@Composable
private fun DetailRouteItem(color: Color, icon: ImageVector, title: String, value: String) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Box(
            Modifier.size(37.dp).clip(CircleShape).background(color.copy(alpha = .10f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = color, modifier = Modifier.size(18.dp))
        }
        Spacer(Modifier.width(10.dp))
        Column(Modifier.weight(1f)) {
            Text(title, color = CallsMuted, fontSize = 9.sp)
            Spacer(Modifier.height(1.dp))
            Text(value, color = GuinchouWhite, fontSize = 11.sp, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
private fun SmallInfoCard(
    modifier: Modifier,
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier.clip(RoundedCornerShape(14.dp)).background(Color(0xFF0A1522))
            .border(1.dp, CallsBorder, RoundedCornerShape(14.dp)).padding(11.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, tint = GuinchouGreen, modifier = Modifier.size(17.dp))
        Spacer(Modifier.width(8.dp))
        Column {
            Text(label, color = CallsMuted, fontSize = 8.sp)
            Text(value, color = GuinchouWhite, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun InformationRow(icon: ImageVector, title: String, value: String) {
    Row(
        Modifier.fillMaxWidth().clip(RoundedCornerShape(15.dp)).background(Color(0xFF0A1522))
            .border(1.dp, CallsBorder, RoundedCornerShape(15.dp)).padding(13.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier.size(35.dp).clip(RoundedCornerShape(10.dp))
                .background(GuinchouGreen.copy(alpha = .10f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = GuinchouGreen, modifier = Modifier.size(18.dp))
        }
        Spacer(Modifier.width(10.dp))
        Column {
            Text(title, color = CallsMuted, fontSize = 8.sp)
            Text(value, color = GuinchouWhite, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun RouteLine(color: Color, title: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(11.dp).border(2.dp, color, CircleShape))
        Spacer(Modifier.width(10.dp))
        Column(Modifier.weight(1f)) {
            Text(title, color = CallsMuted, fontSize = 9.sp)
            Text(
                value,
                color = GuinchouWhite,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun CallsBottomBar(
    onHomeClick: () -> Unit,
    onEarningsClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    Row(
        Modifier.fillMaxWidth().background(Color(0xFF08121E))
            .border(1.dp, CallsBorder.copy(alpha = .85f))
            .padding(horizontal = 9.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CallsBottomItem(Icons.Default.Home, "Início", false, onHomeClick)
        CallsBottomItem(Icons.AutoMirrored.Filled.FormatListBulleted, "Chamados", true) {}
        CallsBottomItem(Icons.Default.AccountBalanceWallet, "Ganhos", false, onEarningsClick)
        CallsBottomItem(Icons.Default.Person, "Perfil", false, onProfileClick)
    }
}

@Composable
private fun CallsBottomItem(
    icon: ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Column(
        Modifier.clip(RoundedCornerShape(11.dp)).clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            icon,
            label,
            tint = if (selected) GuinchouGreen else GuinchouWhite.copy(alpha = .88f),
            modifier = Modifier.size(21.dp)
        )
        Spacer(Modifier.height(3.dp))
        Text(
            label,
            color = if (selected) GuinchouGreen else GuinchouWhite.copy(alpha = .70f),
            fontSize = 9.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
        )
    }
}
