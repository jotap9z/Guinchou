package com.guinchou.app.ui.screens.partner

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FormatListBulleted
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.guinchou.app.R
import com.guinchou.app.ui.theme.*

private val PartnerCard = Color(0xFF0E1A2A)
private val PartnerCard2 = Color(0xFF112033)
private val PartnerMuted = Color(0xFF8998AD)
private val PartnerBorder = Color(0xFF1B2B3F)
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
    onCallsClick: () -> Unit = onHistoryClick,
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
            Spacer(Modifier.height(8.dp))
            BrandHeader(onNotificationsClick)
            Spacer(Modifier.height(22.dp))

            Text(
                text = "Olá, $driverName!",
                color = GuinchouWhite,
                fontSize = 26.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Pronto para ajudar alguém hoje?",
                color = PartnerMuted,
                fontSize = 13.sp
            )

            Spacer(Modifier.height(18.dp))
            OnlineHeroCard(isOnline = isOnline, onOnlineChanged = { isOnline = it })

            Spacer(Modifier.height(23.dp))
            SectionTitle("Resumo de hoje")
            Spacer(Modifier.height(11.dp))

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

            Spacer(Modifier.height(23.dp))
            SectionTitle("Acesso rápido")
            Spacer(Modifier.height(11.dp))

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
                Spacer(Modifier.height(18.dp))
                DocumentWarningCard(documentWarningCount, onDocumentsClick)
            }

            Spacer(Modifier.height(20.dp))
            WaitingRequestsCard(isOnline, onTestRequestClick)
            Spacer(Modifier.height(22.dp))
        }

        PartnerBottomBar(
            onCallsClick = onCallsClick,
            onEarningsClick = onEarningsClick,
            onProfileClick = onProfileClick
        )
    }
}

@Composable
private fun BrandHeader(onNotificationsClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(R.drawable.logo_guinchou),
            contentDescription = "Guinchou",
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            contentScale = ContentScale.Fit,
            alignment = Alignment.CenterStart
        )

        Spacer(Modifier.width(12.dp))

        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(13.dp))
                .background(PartnerCard)
                .border(1.dp, PartnerBorder, RoundedCornerShape(13.dp))
                .clickable(onClick = onNotificationsClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "com/guinchou/app/ui/screens/Notificações",
                tint = GuinchouWhite,
                modifier = Modifier.size(21.dp)
            )
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = (-5).dp, y = 5.dp)
                    .size(8.dp)
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
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.horizontalGradient(
                    if (isOnline) {
                        listOf(Color(0xFF112B27), PartnerCard2, PartnerCard)
                    } else {
                        listOf(Color(0xFF101F31), PartnerCard2, PartnerCard)
                    }
                )
            )
            .border(
                1.dp,
                if (isOnline) GuinchouGreen.copy(alpha = .45f) else PartnerBorder,
                RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 17.dp, vertical = 17.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(
                    if (isOnline) GuinchouGreen.copy(alpha = .16f)
                    else Color(0xFF0A1421)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.PowerSettingsNew,
                contentDescription = null,
                tint = if (isOnline) GuinchouGreen else Color(0xFF718096),
                modifier = Modifier.size(27.dp)
            )
        }

        Spacer(Modifier.width(13.dp))

        Column(Modifier.weight(1f)) {
            Text(
                text = if (isOnline) "Você está online" else "Ficar online",
                color = GuinchouWhite,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(3.dp))
            Text(
                text = if (isOnline)
                    "Disponível para receber chamados."
                else
                    "Ative para começar a receber chamados.",
                color = PartnerMuted,
                fontSize = 11.sp,
                lineHeight = 15.sp
            )
        }

        Switch(
            checked = isOnline,
            onCheckedChange = onOnlineChanged,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color(0xFF07111E),
                checkedTrackColor = GuinchouGreen,
                uncheckedThumbColor = Color(0xFF9AA7B9),
                uncheckedTrackColor = Color(0xFF263548),
                uncheckedBorderColor = Color.Transparent
            )
        )
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        color = GuinchouWhite,
        fontSize = 16.sp,
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
            .height(101.dp)
            .clip(RoundedCornerShape(17.dp))
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF111E2F), Color(0xFF0D1827))
                )
            )
            .border(1.dp, PartnerBorder, RoundedCornerShape(17.dp))
            .padding(13.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = GuinchouGreen,
            modifier = Modifier.size(21.dp)
        )
        Column {
            Text(
                text = value,
                color = GuinchouWhite,
                fontSize = 15.sp,
                fontWeight = FontWeight.ExtraBold,
                maxLines = 1
            )
            Text(text = label, color = PartnerMuted, fontSize = 10.sp)
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
            .height(111.dp)
            .clip(RoundedCornerShape(17.dp))
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF111E2F), Color(0xFF0D1827))
                )
            )
            .border(1.dp, PartnerBorder, RoundedCornerShape(17.dp))
            .clickable(onClick = onClick)
            .padding(13.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .size(37.dp)
                .clip(RoundedCornerShape(11.dp))
                .background(GuinchouGreen.copy(alpha = .10f)),
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
                fontSize = 9.sp,
                lineHeight = 12.sp
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
            .clip(RoundedCornerShape(17.dp))
            .background(
                Brush.horizontalGradient(
                    listOf(
                        PartnerWarning.copy(alpha = .12f),
                        Color(0xFF111B29)
                    )
                )
            )
            .border(
                1.dp,
                PartnerWarning.copy(alpha = .28f),
                RoundedCornerShape(17.dp)
            )
            .clickable(onClick = onClick)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(RoundedCornerShape(11.dp))
                .background(PartnerWarning.copy(alpha = .12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = null,
                tint = PartnerWarning,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(Modifier.width(11.dp))

        Column(Modifier.weight(1f)) {
            Text(
                text = "Atenção aos documentos",
                color = GuinchouWhite,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = "$warningCount documento(s) requer(em) atenção.",
                color = PartnerMuted,
                fontSize = 10.sp
            )
        }

        Text(
            text = "Ver",
            color = GuinchouGreen,
            fontSize = 11.sp,
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
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF101D2D), Color(0xFF0B1624))
                )
            )
            .border(
                1.dp,
                if (isOnline) GuinchouGreen.copy(alpha = .30f) else PartnerBorder,
                RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 22.dp, vertical = 22.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(
                    if (isOnline) GuinchouGreen.copy(alpha = .10f)
                    else Color(0xFF08121F)
                ),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.icone_guincho),
                contentDescription = "Guincho aguardando chamado",
                modifier = Modifier.size(44.dp),
                contentScale = ContentScale.Fit
            )
        }

        Spacer(Modifier.height(12.dp))

        Text(
            text = if (isOnline) "Aguardando chamados" else "Você está offline",
            color = GuinchouWhite,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(5.dp))

        Text(
            text = if (isOnline)
                "Estamos procurando solicitações próximas à sua localização."
            else
                "Fique online para começar a receber solicitações de guincho.",
            color = PartnerMuted,
            fontSize = 11.sp,
            lineHeight = 16.sp,
            textAlign = TextAlign.Center
        )

        if (isOnline) {
            Spacer(Modifier.height(15.dp))
            Button(
                onClick = onTestRequestClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(45.dp),
                shape = RoundedCornerShape(13.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = GuinchouGreen,
                    contentColor = Color(0xFF06101A)
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Bolt,
                    contentDescription = null,
                    modifier = Modifier.size(17.dp)
                )
                Spacer(Modifier.width(7.dp))
                Text(
                    text = "Simular novo chamado",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }
    }
}

@Composable
private fun PartnerBottomBar(
    onCallsClick: () -> Unit,
    onEarningsClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF08121E))
            .border(
                width = 1.dp,
                color = PartnerBorder.copy(alpha = .85f)
            )
            .padding(horizontal = 9.dp, vertical = 8.dp),
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
            icon = Icons.AutoMirrored.Filled.FormatListBulleted,
            label = "Chamados",
            selected = false,
            onClick = onCallsClick
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
            .clip(RoundedCornerShape(11.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 13.dp, vertical = 4.dp),
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

private fun formatCurrency(value: Double): String =
    "R$ %.2f".format(value).replace(".", ",")
