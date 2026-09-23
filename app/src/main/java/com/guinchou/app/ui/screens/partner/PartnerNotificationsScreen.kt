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

private val NotificationCard = Color(0xFF0E1A2A)
private val NotificationBorder = Color(0xFF1B2B3F)
private val NotificationMuted = Color(0xFF8998AD)
private val NotificationWarning = Color(0xFFFFB547)
private val NotificationBlue = Color(0xFF4CA6FF)

private enum class PartnerNotificationType {
    NEW_CALL,
    PAYMENT,
    DOCUMENT,
    APPROVAL,
    PLATFORM
}

private data class PartnerNotificationItem(
    val id: Int,
    val type: PartnerNotificationType,
    val title: String,
    val description: String,
    val time: String,
    val isRead: Boolean
)

@Composable
fun PartnerNotificationsScreen(
    onBackClick: () -> Unit = {},
    onCallClick: () -> Unit = {},
    onEarningsClick: () -> Unit = {}
) {
    var notifications by remember {
        mutableStateOf(
            listOf(
                PartnerNotificationItem(
                    1,
                    PartnerNotificationType.NEW_CALL,
                    "Novo chamado disponível",
                    "Há uma nova solicitação de guincho a 3,2 km da sua localização.",
                    "Agora",
                    false
                ),
                PartnerNotificationItem(
                    2,
                    PartnerNotificationType.PAYMENT,
                    "Pagamento recebido",
                    "O atendimento CH-1045 foi concluído e R$ 150,00 foram registrados nos seus ganhos.",
                    "Há 18 min",
                    false
                ),
                PartnerNotificationItem(
                    3,
                    PartnerNotificationType.DOCUMENT,
                    "Documento próximo do vencimento",
                    "A documentação do veículo precisa ser atualizada em breve para manter sua conta regular.",
                    "Há 2 h",
                    false
                ),
                PartnerNotificationItem(
                    4,
                    PartnerNotificationType.APPROVAL,
                    "Documento aprovado",
                    "A análise do documento enviado foi concluída com sucesso.",
                    "Ontem",
                    true
                ),
                PartnerNotificationItem(
                    5,
                    PartnerNotificationType.PLATFORM,
                    "Atualização da Guinchou",
                    "Novas melhorias foram disponibilizadas para tornar seus atendimentos mais rápidos.",
                    "22/09",
                    true
                )
            )
        )
    }

    val unreadCount = notifications.count { !it.isRead }

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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            NotificationIconButton(
                icon = Icons.AutoMirrored.Filled.ArrowBack,
                description = "Voltar",
                onClick = onBackClick
            )

            Text(
                text = "Notificações",
                color = GuinchouWhite,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 14.dp)
            )

            if (unreadCount > 0) {
                Text(
                    text = "Marcar lidas",
                    color = GuinchouGreen,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .clickable {
                            notifications = notifications.map { it.copy(isRead = true) }
                        }
                        .padding(horizontal = 8.dp, vertical = 7.dp)
                )
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(12.dp))

            Text(
                text = "Central de notificações",
                color = GuinchouWhite,
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(Modifier.height(5.dp))

            Text(
                text = if (unreadCount > 0)
                    "Você possui $unreadCount ${if (unreadCount == 1) "notificação não lida" else "notificações não lidas"}."
                else
                    "Todas as notificações foram visualizadas.",
                color = NotificationMuted,
                fontSize = 11.sp
            )

            Spacer(Modifier.height(20.dp))

            notifications.forEachIndexed { index, item ->
                NotificationItemCard(
                    item = item,
                    onClick = {
                        notifications = notifications.map {
                            if (it.id == item.id) it.copy(isRead = true) else it
                        }

                        when (item.type) {
                            PartnerNotificationType.NEW_CALL -> onCallClick()
                            PartnerNotificationType.PAYMENT -> onEarningsClick()
                            else -> Unit
                        }
                    }
                )

                if (index != notifications.lastIndex) {
                    Spacer(Modifier.height(10.dp))
                }
            }

            Spacer(Modifier.height(28.dp))
        }
    }
}

@Composable
private fun NotificationItemCard(
    item: PartnerNotificationItem,
    onClick: () -> Unit
) {
    val icon = when (item.type) {
        PartnerNotificationType.NEW_CALL -> Icons.Default.LocalShipping
        PartnerNotificationType.PAYMENT -> Icons.Default.Payments
        PartnerNotificationType.DOCUMENT -> Icons.Default.Description
        PartnerNotificationType.APPROVAL -> Icons.Default.Verified
        PartnerNotificationType.PLATFORM -> Icons.Default.Campaign
    }

    val accent = when (item.type) {
        PartnerNotificationType.NEW_CALL -> GuinchouGreen
        PartnerNotificationType.PAYMENT -> GuinchouGreen
        PartnerNotificationType.DOCUMENT -> NotificationWarning
        PartnerNotificationType.APPROVAL -> NotificationBlue
        PartnerNotificationType.PLATFORM -> NotificationMuted
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(
                if (item.isRead) NotificationCard
                else GuinchouGreen.copy(alpha = .065f)
            )
            .border(
                1.dp,
                if (item.isRead) NotificationBorder
                else GuinchouGreen.copy(alpha = .27f),
                RoundedCornerShape(18.dp)
            )
            .clickable(onClick = onClick)
            .padding(14.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(43.dp)
                .clip(RoundedCornerShape(13.dp))
                .background(accent.copy(alpha = .12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = accent,
                modifier = Modifier.size(21.dp)
            )
        }

        Spacer(Modifier.width(12.dp))

        Column(Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = item.title,
                    color = GuinchouWhite,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )

                if (!item.isRead) {
                    Spacer(Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(GuinchouGreen, CircleShape)
                    )
                }
            }

            Spacer(Modifier.height(5.dp))

            Text(
                text = item.description,
                color = NotificationMuted,
                fontSize = 9.sp,
                lineHeight = 14.sp
            )

            Spacer(Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Schedule,
                    contentDescription = null,
                    tint = NotificationMuted,
                    modifier = Modifier.size(12.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = item.time,
                    color = NotificationMuted,
                    fontSize = 8.sp
                )

                Spacer(Modifier.weight(1f))

                when (item.type) {
                    PartnerNotificationType.NEW_CALL -> NotificationActionText("Ver chamado")
                    PartnerNotificationType.PAYMENT -> NotificationActionText("Ver ganhos")
                    else -> Unit
                }
            }
        }
    }
}

@Composable
private fun NotificationActionText(text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = text,
            color = GuinchouGreen,
            fontSize = 8.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.width(2.dp))
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = GuinchouGreen,
            modifier = Modifier.size(14.dp)
        )
    }
}

@Composable
private fun NotificationIconButton(
    icon: ImageVector,
    description: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(42.dp)
            .clip(RoundedCornerShape(13.dp))
            .background(NotificationCard)
            .border(1.dp, NotificationBorder, RoundedCornerShape(13.dp))
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
