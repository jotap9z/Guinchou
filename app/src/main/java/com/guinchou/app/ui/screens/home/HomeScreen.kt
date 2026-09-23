package com.guinchou.app.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.guinchou.app.viewmodel.CustomerHomeUiState

@Composable
fun HomeScreen(
    homeState: CustomerHomeUiState,
    onRequestTowClick: () -> Unit,
    onNotificationClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GuinchouBackground)
    ) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val compactHeight = maxHeight < 700.dp
            val mapHeight = if (compactHeight) 200.dp else 260.dp

            Column(modifier = Modifier.fillMaxSize()) {
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
                            .padding(
                                horizontal = 20.dp,
                                vertical = 14.dp
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "GUINCHOU",
                                color = GuinchouWhite,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Seu socorro chegou.",
                                color = GuinchouGray,
                                fontSize = 13.sp
                            )
                            Text(
                                text = when {
                                    homeState.loading ->
                                        "Carregando seus dados..."
                                    homeState.error != null ->
                                        homeState.error
                                    else ->
                                        "Olá, ${homeState.home?.name ?: "Cliente"} · " +
                                                "${homeState.home?.completedServices ?: 0} " +
                                                "serviço(s) concluído(s)"
                                },
                                color = GuinchouGray,
                                fontSize = 12.sp
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .border(
                                    width = 1.dp,
                                    color = GuinchouBorder,
                                    shape = CircleShape
                                )
                                .clickable { onNotificationClick() },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "!",
                                color = GuinchouGreen,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(mapHeight)
                            .heightIn(
                                min = 180.dp,
                                max = 300.dp
                            )
                            .padding(horizontal = 16.dp)
                            .background(
                                color = GuinchouSurface,
                                shape = RoundedCornerShape(20.dp)
                            )
                            .border(
                                width = 1.dp,
                                color = GuinchouBorder,
                                shape = RoundedCornerShape(20.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(22.dp)
                                    .background(
                                        color = GuinchouGreen,
                                        shape = CircleShape
                                    )
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Mapa",
                                color = GuinchouWhite,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Sua localização aparecerá aqui",
                                color = GuinchouGray,
                                fontSize = 13.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }
                    }

                    Spacer(
                        modifier = Modifier.height(
                            if (compactHeight) 12.dp else 18.dp
                        )
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .background(
                                color = GuinchouSurface,
                                shape = RoundedCornerShape(20.dp)
                            )
                            .border(
                                width = 1.dp,
                                color = GuinchouBorder,
                                shape = RoundedCornerShape(20.dp)
                            )
                            .padding(
                                if (compactHeight) 16.dp else 20.dp
                            )
                    ) {
                        Text(
                            text = "Onde está o veículo?",
                            color = GuinchouWhite,
                            fontSize = 21.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Informe o local exato para encontrarmos " +
                                    "um guincho próximo.",
                            color = GuinchouGray,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(18.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    color = GuinchouBackground,
                                    shape = RoundedCornerShape(14.dp)
                                )
                                .border(
                                    width = 1.dp,
                                    color = GuinchouBorder,
                                    shape = RoundedCornerShape(14.dp)
                                )
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .background(
                                        color = GuinchouGreen,
                                        shape = CircleShape
                                    )
                            )
                            Spacer(modifier = Modifier.size(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Localização atual",
                                    color = GuinchouWhite,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Toque para definir o endereço",
                                    color = GuinchouGray,
                                    fontSize = 12.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        if (homeState.home?.activeRequestId != null) {
                            Text(
                                text = "Atendimento em andamento: " +
                                        "${homeState.home.activeRequestStatus}",
                                color = GuinchouGreen,
                                fontSize = 13.sp,
                                modifier = Modifier.padding(bottom = 10.dp)
                            )
                        }

                        Button(
                            onClick = onRequestTowClick,
                            enabled = homeState.canRequestTow,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = GuinchouGreen,
                                contentColor = GuinchouBackground
                            )
                        ) {
                            Text(
                                text = "Solicitar um guincho",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }

                HorizontalDivider(color = GuinchouBorder)

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
                        text = "Início",
                        selected = true
                    )
                    BottomItem(text = "Chamados")
                    BottomItem(text = "Pagamentos")
                    BottomItem(
                        text = "Perfil",
                        onClick = onProfileClick
                    )
                }
            }
        }
    }
}

@Composable
private fun BottomItem(
    text: String,
    selected: Boolean = false,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .clickable { onClick() }
            .padding(
                horizontal = 10.dp,
                vertical = 6.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .background(
                    color = if (selected) {
                        GuinchouGreen
                    } else {
                        Color.Transparent
                    },
                    shape = CircleShape
                )
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = text,
            color = if (selected) GuinchouGreen else GuinchouGray,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}