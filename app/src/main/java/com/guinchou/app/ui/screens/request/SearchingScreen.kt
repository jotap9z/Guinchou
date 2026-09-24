package com.guinchou.app.ui.screens.request

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
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
import java.text.NumberFormat
import java.util.Locale
import kotlinx.coroutines.delay

private data class SearchStage(
    val title: String,
    val subtitle: String
)

@Composable
fun SearchingScreen(
    pickupAddress: String,
    destinationAddress: String,
    vehicleBrand: String,
    vehicleModel: String,
    problemDetail: String,
    servicePrice: Double,
    onTowFound: () -> Unit,
    onCancelClick: () -> Unit
) {
    val currencyFormatter = remember {
        NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
    }

    val stages = remember {
        listOf(
            SearchStage(
                title = "Procurando guinchos próximos",
                subtitle = "Localizando parceiros disponíveis na sua região"
            ),
            SearchStage(
                title = "Enviando sua solicitação",
                subtitle = "Consultando os parceiros mais próximos"
            ),
            SearchStage(
                title = "Aguardando aceite",
                subtitle = "Um parceiro está analisando o seu chamado"
            )
        )
    }

    var currentStage by remember { mutableIntStateOf(0) }
    var elapsedSeconds by remember { mutableIntStateOf(0) }
    var showCancelDialog by remember { mutableStateOf(false) }

    /*
     * Simulação temporária do front-end.
     * O NavGraph continua recebendo onTowFound da mesma forma.
     * Quando o backend em tempo real estiver pronto, esta simulação
     * poderá ser substituída pelo status real do chamado.
     */
    LaunchedEffect(Unit) {
        repeat(6) { second ->
            delay(1000)
            elapsedSeconds = second + 1
            currentStage = when (elapsedSeconds) {
                in 0..1 -> 0
                in 2..3 -> 1
                else -> 2
            }
        }
        onTowFound()
    }

    if (showCancelDialog) {
        AlertDialog(
            onDismissRequest = { showCancelDialog = false },
            containerColor = GuinchouSurface,
            title = {
                Text(
                    text = "Cancelar solicitação?",
                    color = GuinchouWhite,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "A busca por um guincheiro será encerrada e você voltará para a tela inicial.",
                    color = GuinchouGray,
                    fontSize = 13.sp
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showCancelDialog = false
                        onCancelClick()
                    }
                ) {
                    Text(
                        text = "Sim, cancelar",
                        color = Color(0xFFFF6B6B),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showCancelDialog = false }
                ) {
                    Text(
                        text = "Continuar buscando",
                        color = GuinchouGreen,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GuinchouBackground)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        SearchMapArea(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            stage = stages[currentStage],
            elapsedSeconds = elapsedSeconds
        )

        SearchBottomPanel(
            pickupAddress = pickupAddress,
            destinationAddress = destinationAddress,
            vehicleBrand = vehicleBrand,
            vehicleModel = vehicleModel,
            problemDetail = problemDetail,
            servicePrice = currencyFormatter.format(servicePrice),
            onCancelClick = { showCancelDialog = true }
        )
    }
}

@Composable
private fun SearchMapArea(
    modifier: Modifier,
    stage: SearchStage,
    elapsedSeconds: Int
) {
    val transition = rememberInfiniteTransition(label = "searchPulse")

    val pulseScale by transition.animateFloat(
        initialValue = 0.88f,
        targetValue = 1.18f,
        animationSpec = infiniteRepeatable(
            animation = tween(1100),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    val pulseAlpha by transition.animateFloat(
        initialValue = 0.22f,
        targetValue = 0.06f,
        animationSpec = infiniteRepeatable(
            animation = tween(1100),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    Box(
        modifier = modifier
            .background(GuinchouBackground)
    ) {
        /*
         * Área preparada para receber o mapa real.
         * Mantemos o mock visual nesta etapa para não interferir
         * na integração de mapas já existente em outras telas.
         */
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 22.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .background(
                        GuinchouGreen.copy(alpha = 0.08f),
                        RoundedCornerShape(50)
                    )
                    .border(
                        1.dp,
                        GuinchouGreen.copy(alpha = 0.22f),
                        RoundedCornerShape(50)
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "GUINCHOU • BUSCA EM TEMPO REAL",
                    color = GuinchouGreen,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Box(
                modifier = Modifier.size(150.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(126.dp)
                        .scale(pulseScale)
                        .alpha(pulseAlpha)
                        .background(
                            color = GuinchouGreen,
                            shape = CircleShape
                        )
                )

                Box(
                    modifier = Modifier
                        .size(82.dp)
                        .background(
                            color = GuinchouGreen.copy(alpha = 0.10f),
                            shape = CircleShape
                        )
                        .border(
                            width = 1.dp,
                            color = GuinchouGreen.copy(alpha = 0.35f),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = GuinchouGreen,
                        modifier = Modifier.size(34.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Buscando o melhor guincho para você",
                color = GuinchouWhite,
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(7.dp))

            Text(
                text = "Estamos consultando parceiros próximos da sua localização.",
                color = GuinchouGray,
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(22.dp))

            RoutePreview()
        }

        SearchStatusCard(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            stage = stage,
            elapsedSeconds = elapsedSeconds
        )
    }
}

@Composable
private fun RoutePreview() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                GuinchouBackground.copy(alpha = 0.92f),
                RoundedCornerShape(16.dp)
            )
            .border(
                1.dp,
                GuinchouBorder,
                RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 16.dp, vertical = 13.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(
                    GuinchouGreen.copy(alpha = 0.10f),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                tint = GuinchouGreen,
                modifier = Modifier.size(18.dp)
            )
        }

        HorizontalDivider(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 10.dp),
            color = GuinchouBorder
        )

        Box(
            modifier = Modifier
                .size(32.dp)
                .background(
                    GuinchouWhite.copy(alpha = 0.08f),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Build,
                contentDescription = null,
                tint = GuinchouWhite,
                modifier = Modifier.size(17.dp)
            )
        }
    }
}

@Composable
private fun SearchStatusCard(
    modifier: Modifier,
    stage: SearchStage,
    elapsedSeconds: Int
) {
    Row(
        modifier = modifier
            .background(
                color = GuinchouBackground,
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                width = 1.dp,
                color = GuinchouBorder,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 15.dp, vertical = 13.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(30.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = GuinchouGreen,
                strokeWidth = 3.dp
            )
        }

        Spacer(modifier = Modifier.size(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            AnimatedContent(
                targetState = stage,
                label = "searchStage"
            ) { current ->
                Column {
                    Text(
                        text = current.title,
                        color = GuinchouWhite,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = current.subtitle,
                        color = GuinchouGray,
                        fontSize = 10.sp
                    )
                }
            }
        }

        Text(
            text = "${elapsedSeconds}s",
            color = GuinchouGreen,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun SearchBottomPanel(
    pickupAddress: String,
    destinationAddress: String,
    vehicleBrand: String,
    vehicleModel: String,
    problemDetail: String,
    servicePrice: String,
    onCancelClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = GuinchouBackground,
                shape = RoundedCornerShape(
                    topStart = 24.dp,
                    topEnd = 24.dp
                )
            )
            .border(
                width = 1.dp,
                color = GuinchouBorder,
                shape = RoundedCornerShape(
                    topStart = 24.dp,
                    topEnd = 24.dp
                )
            )
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 15.dp)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(0.13f)
                .height(4.dp)
                .background(
                    GuinchouBorder,
                    RoundedCornerShape(50)
                )
        )

        Spacer(modifier = Modifier.height(14.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Seu chamado",
                    color = GuinchouWhite,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Aguardando um parceiro aceitar",
                    color = GuinchouGray,
                    fontSize = 11.sp
                )
            }

            Box(
                modifier = Modifier
                    .background(
                        GuinchouGreen.copy(alpha = 0.10f),
                        RoundedCornerShape(50)
                    )
                    .border(
                        1.dp,
                        GuinchouGreen.copy(alpha = 0.30f),
                        RoundedCornerShape(50)
                    )
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "BUSCANDO",
                    color = GuinchouGreen,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(15.dp))

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
                .padding(14.dp)
        ) {
            SearchInformation(
                label = "Partida",
                value = pickupAddress
            )

            Spacer(modifier = Modifier.height(9.dp))

            SearchInformation(
                label = "Destino",
                value = destinationAddress
            )

            Spacer(modifier = Modifier.height(9.dp))

            SearchInformation(
                label = "Veículo",
                value = listOf(vehicleBrand, vehicleModel)
                    .filter { it.isNotBlank() }
                    .joinToString(" ")
                    .ifBlank { "Não informado" }
            )

            Spacer(modifier = Modifier.height(9.dp))

            SearchInformation(
                label = "Problema",
                value = problemDetail.ifBlank { "Não informado" }
            )

            Spacer(modifier = Modifier.height(9.dp))

            SearchInformation(
                label = "Valor estimado",
                value = servicePrice,
                highlight = true
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onCancelClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = GuinchouSurface,
                contentColor = GuinchouWhite
            )
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = null,
                tint = GuinchouGray,
                modifier = Modifier.size(18.dp)
            )

            Spacer(modifier = Modifier.size(8.dp))

            Text(
                text = "Cancelar solicitação",
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun SearchInformation(
    label: String,
    value: String,
    highlight: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = label,
            color = GuinchouGray,
            fontSize = 11.sp,
            modifier = Modifier.weight(0.34f)
        )

        Text(
            text = value,
            color = if (highlight) GuinchouGreen else GuinchouWhite,
            fontSize = 12.sp,
            fontWeight = if (highlight) FontWeight.Bold else FontWeight.Medium,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(0.66f)
        )
    }
}
