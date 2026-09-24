package com.guinchou.app.ui.screens.notifications

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Notifications
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

private enum class CustomerNotificationType {
    CALL,
    PAYMENT,
    GENERAL
}

private data class CustomerNotificationItem(
    val id: Int,
    val title: String,
    val message: String,
    val date: String,
    val time: String,
    val type: CustomerNotificationType,
    val actionLabel: String? = null,
    val unread: Boolean = true
)

@Composable
fun CustomerNotificationsScreen(
    onBackClick: () -> Unit = {},
    onCallClick: () -> Unit = {},
    onPaymentClick: () -> Unit = {}
) {
    var notifications by remember {
        mutableStateOf(
            listOf(
                CustomerNotificationItem(
                    id = 1,
                    title = "Guincheiro encontrado",
                    message = "Carlos Henrique aceitou seu chamado e já está a caminho.",
                    date = "Hoje",
                    time = "18:44",
                    type = CustomerNotificationType.CALL,
                    actionLabel = "Acompanhar chamado"
                ),
                CustomerNotificationItem(
                    id = 2,
                    title = "Guincheiro a caminho",
                    message = "Seu guincheiro iniciou o deslocamento até o local informado.",
                    date = "Hoje",
                    time = "18:45",
                    type = CustomerNotificationType.CALL,
                    actionLabel = "Ver chamado"
                ),
                CustomerNotificationItem(
                    id = 3,
                    title = "Pagamento confirmado",
                    message = "O pagamento de R$ 200,00 via PIX foi confirmado.",
                    date = "Hoje",
                    time = "18:43",
                    type = CustomerNotificationType.PAYMENT,
                    actionLabel = "Ver pagamento"
                ),
                CustomerNotificationItem(
                    id = 4,
                    title = "Atendimento concluído",
                    message = "Seu atendimento foi finalizado com sucesso. O chamado já está disponível no histórico.",
                    date = "14/09/2026",
                    time = "17:52",
                    type = CustomerNotificationType.CALL,
                    actionLabel = "Ver chamado",
                    unread = false
                ),
                CustomerNotificationItem(
                    id = 5,
                    title = "Bem-vindo ao Guinchou",
                    message = "Sua conta está pronta. Cadastre seus veículos para agilizar futuras solicitações.",
                    date = "03/09/2026",
                    time = "08:10",
                    type = CustomerNotificationType.GENERAL,
                    unread = false
                )
            )
        )
    }

    val unreadCount = notifications.count { it.unread }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GuinchouBackground)
    ) {
        NotificationsHeader(
            unreadCount = unreadCount,
            onBackClick = onBackClick,
            onMarkAllRead = {
                notifications = notifications.map {
                    it.copy(unread = false)
                }
            }
        )

        HorizontalDivider(color = GuinchouBorder)

        if (notifications.isEmpty()) {
            EmptyNotifications(
                modifier = Modifier.weight(1f)
            )
        } else {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                if (unreadCount > 0) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                GuinchouGreen.copy(alpha = 0.08f),
                                RoundedCornerShape(14.dp)
                            )
                            .border(
                                1.dp,
                                GuinchouGreen.copy(alpha = 0.25f),
                                RoundedCornerShape(14.dp)
                            )
                            .padding(13.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(9.dp)
                                .background(GuinchouGreen, CircleShape)
                        )

                        Spacer(Modifier.size(9.dp))

                        Text(
                            text = if (unreadCount == 1) {
                                "Você tem 1 nova notificação"
                            } else {
                                "Você tem $unreadCount novas notificações"
                            },
                            color = GuinchouWhite,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(Modifier.height(16.dp))
                }

                notifications.forEach { notification ->
                    NotificationCard(
                        notification = notification,
                        onClick = {
                            notifications = notifications.map {
                                if (it.id == notification.id) {
                                    it.copy(unread = false)
                                } else {
                                    it
                                }
                            }
                        },
                        onActionClick = {
                            notifications = notifications.map {
                                if (it.id == notification.id) {
                                    it.copy(unread = false)
                                } else {
                                    it
                                }
                            }

                            when (notification.type) {
                                CustomerNotificationType.CALL -> onCallClick()
                                CustomerNotificationType.PAYMENT -> onPaymentClick()
                                CustomerNotificationType.GENERAL -> Unit
                            }
                        }
                    )

                    Spacer(Modifier.height(11.dp))
                }

                Spacer(Modifier.height(12.dp))

                Text(
                    text = "As notificações desta versão são demonstrativas. Os eventos reais serão conectados ao backend posteriormente.",
                    color = GuinchouGray,
                    fontSize = 9.sp,
                    lineHeight = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp)
                )

                Spacer(Modifier.height(20.dp))
            }
        }
    }
}

@Composable
private fun NotificationsHeader(
    unreadCount: Int,
    onBackClick: () -> Unit,
    onMarkAllRead: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Voltar",
                    tint = GuinchouWhite
                )
            }

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Notificações",
                    color = GuinchouWhite,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = if (unreadCount == 0) {
                        "Tudo em dia"
                    } else {
                        "$unreadCount não ${if (unreadCount == 1) "lida" else "lidas"}"
                    },
                    color = GuinchouGray,
                    fontSize = 10.sp
                )
            }

            if (unreadCount > 0) {
                TextButton(onClick = onMarkAllRead) {
                    Text(
                        text = "Marcar todas como lidas",
                        color = GuinchouGreen,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
private fun NotificationCard(
    notification: CustomerNotificationItem,
    onClick: () -> Unit,
    onActionClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                if (notification.unread) {
                    GuinchouGreen.copy(alpha = 0.055f)
                } else {
                    GuinchouSurface
                },
                RoundedCornerShape(17.dp)
            )
            .border(
                1.dp,
                if (notification.unread) {
                    GuinchouGreen.copy(alpha = 0.35f)
                } else {
                    GuinchouBorder
                },
                RoundedCornerShape(17.dp)
            )
            .clickable(onClick = onClick)
            .padding(15.dp)
    ) {
        Row(
            verticalAlignment = Alignment.Top
        ) {
            NotificationIcon(
                type = notification.type
            )

            Spacer(Modifier.size(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = notification.title,
                        color = GuinchouWhite,
                        fontSize = 13.sp,
                        fontWeight = if (notification.unread) {
                            FontWeight.Bold
                        } else {
                            FontWeight.SemiBold
                        },
                        modifier = Modifier.weight(1f)
                    )

                    if (notification.unread) {
                        Spacer(Modifier.size(8.dp))
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(GuinchouGreen, CircleShape)
                        )
                    }
                }

                Spacer(Modifier.height(5.dp))

                Text(
                    text = notification.message,
                    color = GuinchouGray,
                    fontSize = 11.sp,
                    lineHeight = 16.sp
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = "${notification.date} • ${notification.time}",
                    color = GuinchouGray.copy(alpha = 0.75f),
                    fontSize = 9.sp
                )
            }
        }

        notification.actionLabel?.let { label ->
            Spacer(Modifier.height(13.dp))
            HorizontalDivider(color = GuinchouBorder)
            Spacer(Modifier.height(8.dp))

            TextButton(
                onClick = onActionClick,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(
                    text = label,
                    color = GuinchouGreen,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun NotificationIcon(
    type: CustomerNotificationType
) {
    val icon: ImageVector = when (type) {
        CustomerNotificationType.CALL -> Icons.Default.Build
        CustomerNotificationType.PAYMENT -> Icons.Default.CreditCard
        CustomerNotificationType.GENERAL -> Icons.Default.Notifications
    }

    Box(
        modifier = Modifier
            .size(42.dp)
            .background(
                GuinchouGreen.copy(alpha = 0.10f),
                RoundedCornerShape(12.dp)
            )
            .border(
                1.dp,
                GuinchouGreen.copy(alpha = 0.22f),
                RoundedCornerShape(12.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = GuinchouGreen,
            modifier = Modifier.size(21.dp)
        )
    }
}

@Composable
private fun EmptyNotifications(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 28.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .background(
                    GuinchouSurface,
                    CircleShape
                )
                .border(
                    1.dp,
                    GuinchouBorder,
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = GuinchouGreen,
                modifier = Modifier.size(32.dp)
            )
        }

        Spacer(Modifier.height(18.dp))

        Text(
            text = "Nenhuma notificação",
            color = GuinchouWhite,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(6.dp))

        Text(
            text = "Avisos sobre seus chamados, pagamentos e atualizações aparecerão aqui.",
            color = GuinchouGray,
            fontSize = 11.sp,
            lineHeight = 16.sp,
            textAlign = TextAlign.Center
        )
    }
}
