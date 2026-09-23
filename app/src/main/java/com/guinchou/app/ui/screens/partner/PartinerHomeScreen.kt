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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.guinchou.app.ui.theme.*
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.guinchou.app.R

private val PartnerCard = Color(0xFF101B2B)
private val PartnerCardSoft = Color(0xFF132237)
private val PartnerMuted = Color(0xFF8C9AAF)
private val PartnerWarning = Color(0xFFFFB547)

@Composable
fun PartnerHomeScreen(
    driverName: String = "João",
    todayTrips: Int = 3,
    todayEarnings: Double = 382.50,
    driverRating: Double = 4.9,
    documentWarningCount: Int = 1,
    onNotificationsClick: () -> Unit = {},
    onHistoryClick: () -> Unit = {},
    onEarningsClick: () -> Unit = {},
    onDocumentsClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onTestRequestClick: () -> Unit = {}
) {
    var isOnline by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        GuinchouBackground,
                        Color(0xFF07111E),
                        GuinchouBackground
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
                .padding(horizontal = 18.dp)
        ) {
            Spacer(Modifier.height(12.dp))

            BrandHeader(
                onNotificationsClick = onNotificationsClick
            )

            Spacer(Modifier.height(25.dp))

            Text(
                text = "Olá, $driverName!",
                color = GuinchouWhite,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(5.dp))

            Text(
                text = "Pronto para ajudar alguém hoje?",
                color = PartnerMuted,
                fontSize = 13.sp
            )

            Spacer(Modifier.height(20.dp))

            OnlineHeroCard(
                isOnline = isOnline,
                onOnlineChanged = { isOnline = it }
            )

            Spacer(Modifier.height(23.dp))

            SectionTitle("Resumo de hoje")

            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SummaryCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.LocalShipping,
                    value = todayTrips.toString(),
                    label = "Corridas"
                )

                SummaryCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.AccountBalanceWallet,
                    value = formatCurrency(todayEarnings),
                    label = "Ganhos"
                )

                SummaryCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.Star,
                    value = "%.1f".format(driverRating),
                    label = "Avaliação"
                )
            }

            Spacer(Modifier.height(24.dp))

            SectionTitle("Acesso rápido")

            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(11.dp)
            ) {
                QuickAccessCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.History,
                    title = "Histórico",
                    subtitle = "Seus atendimentos",
                    onClick = onHistoryClick
                )

                QuickAccessCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.AccountBalanceWallet,
                    title = "Ganhos",
                    subtitle = "Valores e repasses",
                    onClick = onEarningsClick
                )
            }

            Spacer(Modifier.height(11.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(11.dp)
            ) {
                QuickAccessCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.Description,
                    title = "Documentos",
                    subtitle = "Validade e situação",
                    onClick = onDocumentsClick
                )

                QuickAccessCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.Person,
                    title = "Perfil",
                    subtitle = "Conta e guincho",
                    onClick = onProfileClick
                )
            }

            if (documentWarningCount > 0) {
                Spacer(Modifier.height(20.dp))

                DocumentWarningCard(
                    warningCount = documentWarningCount,
                    onClick = onDocumentsClick
                )
            }

            Spacer(Modifier.height(22.dp))

            WaitingRequestsCard(
                isOnline = isOnline,
                onTestRequestClick = onTestRequestClick
            )

            Spacer(Modifier.height(28.dp))
        }

        PartnerBottomBar(
            onHistoryClick = onHistoryClick,
            onEarningsClick = onEarningsClick,
            onProfileClick = onProfileClick
        )
    }
}

@Composable
private fun BrandHeader(
    onNotificationsClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_guinchou),
                contentDescription = "Guinchou",
                modifier = Modifier
                    .height(60.dp),
                contentScale = ContentScale.Fit
            )
        }

        Box(
            modifier = Modifier
                .size(43.dp)
                .clip(CircleShape)
                .background(PartnerCard)
                .border(1.dp, GuinchouBorder, CircleShape)
                .clickable(onClick = onNotificationsClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Notificações",
                tint = GuinchouWhite,
                modifier = Modifier.size(21.dp)
            )

            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = (-3).dp, y = 3.dp)
                    .size(9.dp)
                    .background(GuinchouGreen, CircleShape)
                    .border(2.dp, PartnerCard, CircleShape)
            )
        }
    }
}

@Composable
private fun OnlineHeroCard(
    isOnline: Boolean,
    onOnlineChanged: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(
                Brush.horizontalGradient(
                    colors = if (isOnline) {
                        listOf(
                            GuinchouGreen.copy(alpha = 0.16f),
                            PartnerCardSoft,
                            PartnerCard
                        )
                    } else {
                        listOf(
                            PartnerCardSoft,
                            PartnerCard
                        )
                    }
                )
            )
            .border(
                width = 1.dp,
                color = if (isOnline) {
                    GuinchouGreen.copy(alpha = 0.55f)
                } else {
                    GuinchouBorder
                },
                shape = RoundedCornerShape(22.dp)
            )
            .padding(18.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(
                        if (isOnline) {
                            GuinchouGreen.copy(alpha = 0.17f)
                        } else {
                            GuinchouBackground.copy(alpha = 0.7f)
                        },
                        RoundedCornerShape(15.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PowerSettingsNew,
                    contentDescription = null,
                    tint = if (isOnline) GuinchouGreen else PartnerMuted,
                    modifier = Modifier.size(25.dp)
                )
            }

            Spacer(Modifier.width(13.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = if (isOnline) "Você está online" else "Ficar online",
                    color = GuinchouWhite,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(3.dp))

                Text(
                    text = if (isOnline) {
                        "Disponível para receber chamados próximos."
                    } else {
                        "Ative para começar a receber chamados."
                    },
                    color = PartnerMuted,
                    fontSize = 12.sp,
                    lineHeight = 17.sp
                )
            }

            Switch(
                checked = isOnline,
                onCheckedChange = onOnlineChanged,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = GuinchouBackground,
                    checkedTrackColor = GuinchouGreen,
                    uncheckedThumbColor = PartnerMuted,
                    uncheckedTrackColor = GuinchouBorder
                )
            )
        }

        if (isOnline) {
            Spacer(Modifier.height(15.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    Modifier
                        .size(8.dp)
                        .background(GuinchouGreen, CircleShape)
                )

                Spacer(Modifier.width(7.dp))

                Text(
                    text = "Disponível para chamados",
                    color = GuinchouGreen,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun SectionTitle(
    title: String
) {
    Text(
        text = title,
        color = GuinchouWhite,
        fontSize = 17.sp,
        fontWeight = FontWeight.Bold
    )
}

@Composable
private fun SummaryCard(
    modifier: Modifier,
    icon: ImageVector,
    value: String,
    label: String
) {
    Column(
        modifier = modifier
            .height(106.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(PartnerCard)
            .border(
                1.dp,
                GuinchouBorder.copy(alpha = 0.85f),
                RoundedCornerShape(18.dp)
            )
            .padding(13.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = GuinchouGreen,
            modifier = Modifier.size(20.dp)
        )

        Column {
            Text(
                text = value,
                color = GuinchouWhite,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )

            Text(
                text = label,
                color = PartnerMuted,
                fontSize = 10.sp
            )
        }
    }
}

@Composable
private fun QuickAccessCard(
    modifier: Modifier,
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .height(115.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(PartnerCard)
            .border(
                1.dp,
                GuinchouBorder.copy(alpha = 0.85f),
                RoundedCornerShape(18.dp)
            )
            .clickable(onClick = onClick)
            .padding(14.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .size(39.dp)
                .background(
                    GuinchouGreen.copy(alpha = 0.10f),
                    RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = GuinchouGreen,
                modifier = Modifier.size(20.dp)
            )
        }

        Column {
            Text(
                text = title,
                color = GuinchouWhite,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(2.dp))

            Text(
                text = subtitle,
                color = PartnerMuted,
                fontSize = 10.sp,
                lineHeight = 13.sp
            )
        }
    }
}

@Composable
private fun DocumentWarningCard(
    warningCount: Int,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(
                Brush.horizontalGradient(
                    listOf(
                        PartnerWarning.copy(alpha = 0.12f),
                        PartnerCard
                    )
                )
            )
            .border(
                1.dp,
                PartnerWarning.copy(alpha = 0.32f),
                RoundedCornerShape(18.dp)
            )
            .clickable(onClick = onClick)
            .padding(15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(39.dp)
                .background(
                    PartnerWarning.copy(alpha = 0.12f),
                    RoundedCornerShape(11.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = null,
                tint = PartnerWarning,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "Atenção aos documentos",
                color = GuinchouWhite,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "$warningCount documento(s) requer(em) atenção.",
                color = PartnerMuted,
                fontSize = 11.sp
            )
        }

        Text(
            text = "Ver",
            color = GuinchouGreen,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun WaitingRequestsCard(
    isOnline: Boolean,
    onTestRequestClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(PartnerCard)
            .border(
                1.dp,
                if (isOnline) {
                    GuinchouGreen.copy(alpha = 0.40f)
                } else {
                    GuinchouBorder
                },
                RoundedCornerShape(22.dp)
            )
            .padding(horizontal = 22.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(58.dp)
                .background(
                    if (isOnline) {
                        GuinchouGreen.copy(alpha = 0.12f)
                    } else {
                        GuinchouBackground
                    },
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.icone_guincho),
                contentDescription = null,
                modifier = Modifier.size(28.dp)
            )
        }

        Spacer(Modifier.height(13.dp))

        Text(
            text = if (isOnline) "Aguardando chamados" else "Você está offline",
            color = GuinchouWhite,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(6.dp))

        Text(
            text = if (isOnline) {
                "Estamos procurando solicitações próximas à sua localização."
            } else {
                "Fique online para começar a receber solicitações de guincho."
            },
            color = PartnerMuted,
            fontSize = 12.sp,
            lineHeight = 17.sp,
            textAlign = TextAlign.Center
        )

        if (isOnline) {
            Spacer(Modifier.height(17.dp))

            Button(
                onClick = onTestRequestClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = GuinchouGreen,
                    contentColor = GuinchouBackground
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Bolt,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )

                Spacer(Modifier.width(7.dp))

                Text(
                    text = "Simular novo chamado",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun PartnerBottomBar(
    onHistoryClick: () -> Unit,
    onEarningsClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF0B1523))
            .border(
                width = 1.dp,
                color = GuinchouBorder.copy(alpha = 0.7f)
            )
            .padding(
                horizontal = 12.dp,
                vertical = 9.dp
            ),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BottomItem(
            icon = Icons.Default.Home,
            label = "Início",
            selected = true,
            onClick = {}
        )

        BottomItem(
            icon = Icons.Default.History,
            label = "Histórico",
            selected = false,
            onClick = onHistoryClick
        )

        BottomItem(
            icon = Icons.Default.AccountBalanceWallet,
            label = "Ganhos",
            selected = false,
            onClick = onEarningsClick
        )

        BottomItem(
            icon = Icons.Default.Person,
            label = "Perfil",
            selected = false,
            onClick = onProfileClick
        )
    }
}

@Composable
private fun BottomItem(
    icon: ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(
                horizontal = 12.dp,
                vertical = 5.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (selected) GuinchouGreen else PartnerMuted,
            modifier = Modifier.size(21.dp)
        )

        Spacer(Modifier.height(3.dp))

        Text(
            text = label,
            color = if (selected) GuinchouGreen else PartnerMuted,
            fontSize = 9.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

private fun formatCurrency(
    value: Double
): String {
    return "R$ %.2f"
        .format(value)
        .replace(".", ",")
}

/*
 * ==========================================================
 * COMO USAR A LOGO GERADA
 * ==========================================================
 *
 * 1. Salve a imagem como:
 *    logo_guinchou.png
 *
 * 2. Coloque em:
 *    app/src/main/res/drawable/logo_guinchou.png
 *
 * 3. Adicione estes imports:
 *
 *    import androidx.compose.foundation.Image
 *    import androidx.compose.ui.layout.ContentScale
 *    import androidx.compose.ui.res.painterResource
 *    import com.guinchou.app.R
 *
 * 4. Dentro de BrandHeader(), substitua o Box verde que contém
 *    Icons.Default.LocalShipping por:
 *
 *    Image(
 *        painter = painterResource(id = R.drawable.logo_guinchou),
 *        contentDescription = "Guinchou",
 *        modifier = Modifier
 *            .width(145.dp)
 *            .height(42.dp),
 *        contentScale = ContentScale.Fit
 *    )
 *
 * 5. Depois remova também o Text("GUINCHOU") que está ao lado,
 *    porque a imagem gerada já contém símbolo + nome.
 *
 * O placeholder atual foi mantido propositalmente para que o
 * projeto compile antes da imagem ser adicionada ao drawable.
 */
