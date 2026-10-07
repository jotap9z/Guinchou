package com.guinchou.app.ui.screens.partner.company

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.guinchou.app.ui.theme.GuinchouBackground
import com.guinchou.app.ui.theme.GuinchouBorder
import com.guinchou.app.ui.theme.GuinchouGray
import com.guinchou.app.ui.theme.GuinchouGreen
import com.guinchou.app.ui.theme.GuinchouSurface
import com.guinchou.app.ui.theme.GuinchouWhite

private enum class CompanyNotificationCategory {
    DRIVER,
    FLEET,
    CALL,
    FINANCE,
    GENERAL
}

private data class CompanyNotification(
    val id: Int,
    val category: CompanyNotificationCategory,
    val title: String,
    val description: String,
    val time: String,
    val actionLabel: String? = null,
    val isRead: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompanyNotificationsScreen(
    onBackClick: () -> Unit = {},
    onDriverClick: () -> Unit = {},
    onFleetClick: () -> Unit = {},
    onCallClick: () -> Unit = {},
    onFinanceClick: () -> Unit = {}
) {

    val notifications =
        remember {
            mutableStateListOf(
                CompanyNotification(
                    id = 1,
                    category = CompanyNotificationCategory.DRIVER,
                    title = "Novo motorista aguardando análise",
                    description =
                        "Carlos Henrique concluiu o cadastro e enviou os documentos.",
                    time = "Há 5 min",
                    actionLabel = "Ver motorista",
                    isRead = false
                ),

                CompanyNotification(
                    id = 2,
                    category = CompanyNotificationCategory.CALL,
                    title = "Novo chamado disponível",
                    description =
                        "Há um novo atendimento próximo à região da empresa.",
                    time = "Há 18 min",
                    actionLabel = "Ver chamado",
                    isRead = false
                ),

                CompanyNotification(
                    id = 3,
                    category = CompanyNotificationCategory.FLEET,
                    title = "Guincho cadastrado",
                    description =
                        "O veículo ABC1D23 foi enviado para análise da frota.",
                    time = "Há 42 min",
                    actionLabel = "Ver frota",
                    isRead = false
                ),

                CompanyNotification(
                    id = 4,
                    category = CompanyNotificationCategory.DRIVER,
                    title = "Motorista aprovado",
                    description =
                        "O cadastro de Marcos Silva foi aprovado e já está disponível para a empresa.",
                    time = "Hoje, 09:14",
                    actionLabel = "Ver motorista",
                    isRead = true
                ),

                CompanyNotification(
                    id = 5,
                    category = CompanyNotificationCategory.FINANCE,
                    title = "Novo repasse disponível",
                    description =
                        "Um novo repasse referente aos atendimentos concluídos foi registrado.",
                    time = "Ontem",
                    actionLabel = "Ver financeiro",
                    isRead = true
                ),

                CompanyNotification(
                    id = 6,
                    category = CompanyNotificationCategory.GENERAL,
                    title = "Bem-vindo ao Guinchou Empresas",
                    description =
                        "Gerencie motoristas, frota, chamados e operação da empresa em um único ambiente.",
                    time = "Ontem",
                    isRead = true
                )
            )
        }

    var unreadOnly by remember {
        mutableStateOf(false)
    }

    val unreadCount =
        notifications.count {
            !it.isRead
        }

    val visibleNotifications =
        if (unreadOnly) {
            notifications.filter {
                !it.isRead
            }
        } else {
            notifications
        }

    fun markAsRead(
        notificationId: Int
    ) {

        val index =
            notifications.indexOfFirst {
                it.id == notificationId
            }

        if (index >= 0) {

            val current =
                notifications[index]

            if (!current.isRead) {

                notifications[index] =
                    current.copy(
                        isRead = true
                    )
            }
        }
    }

    fun markAllAsRead() {

        notifications.indices.forEach { index ->

            val current =
                notifications[index]

            if (!current.isRead) {

                notifications[index] =
                    current.copy(
                        isRead = true
                    )
            }
        }
    }

    fun executeAction(
        notification: CompanyNotification
    ) {

        markAsRead(
            notification.id
        )

        when (
            notification.category
        ) {

            CompanyNotificationCategory.DRIVER -> {
                onDriverClick()
            }

            CompanyNotificationCategory.FLEET -> {
                onFleetClick()
            }

            CompanyNotificationCategory.CALL -> {
                onCallClick()
            }

            CompanyNotificationCategory.FINANCE -> {
                onFinanceClick()
            }

            CompanyNotificationCategory.GENERAL -> {
                // Apenas informativa.
            }
        }
    }

    Scaffold(
        containerColor =
            GuinchouBackground,

        topBar = {

            TopAppBar(
                colors =
                    TopAppBarDefaults
                        .topAppBarColors(
                            containerColor =
                                GuinchouBackground
                        ),

                navigationIcon = {

                    IconButton(
                        onClick =
                            onBackClick
                    ) {

                        Icon(
                            imageVector =
                                Icons.AutoMirrored
                                    .Filled
                                    .ArrowBack,

                            contentDescription =
                                "Voltar",

                            tint =
                                GuinchouWhite
                        )
                    }
                },

                title = {

                    Column {

                        Text(
                            text =
                                "Notificações",

                            color =
                                GuinchouWhite,

                            fontSize =
                                20.sp,

                            fontWeight =
                                FontWeight.Bold
                        )

                        Text(
                            text =
                                "Central da empresa",

                            color =
                                GuinchouGray,

                            fontSize =
                                12.sp
                        )
                    }
                }
            )
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    paddingValues
                )
                .padding(
                    horizontal =
                        20.dp
                ),

            verticalArrangement =
                Arrangement.spacedBy(
                    12.dp
                )
        ) {

            /*
             * =====================================================
             * RESUMO
             * =====================================================
             */

            item {

                Spacer(
                    modifier =
                        Modifier.height(
                            6.dp
                        )
                )

                NotificationSummaryCard(
                    unreadCount =
                        unreadCount,

                    onMarkAllClick = {
                        markAllAsRead()
                    }
                )
            }

            /*
             * =====================================================
             * FILTROS
             * =====================================================
             */

            item {

                Row(
                    modifier =
                        Modifier.fillMaxWidth(),

                    horizontalArrangement =
                        Arrangement.spacedBy(
                            10.dp
                        )
                ) {

                    FilterChip(
                        selected =
                            !unreadOnly,

                        onClick = {
                            unreadOnly =
                                false
                        },

                        label = {
                            Text(
                                "Todas"
                            )
                        },

                        colors =
                            companyFilterColors()
                    )

                    FilterChip(
                        selected =
                            unreadOnly,

                        onClick = {
                            unreadOnly =
                                true
                        },

                        label = {

                            Text(
                                if (
                                    unreadCount > 0
                                ) {
                                    "Não lidas ($unreadCount)"
                                } else {
                                    "Não lidas"
                                }
                            )
                        },

                        colors =
                            companyFilterColors()
                    )
                }
            }

            item {

                Spacer(
                    modifier =
                        Modifier.height(
                            2.dp
                        )
                )

                Text(
                    text =
                        if (unreadOnly) {
                            "Não lidas"
                        } else {
                            "Recentes"
                        },

                    color =
                        GuinchouWhite,

                    fontSize =
                        17.sp,

                    fontWeight =
                        FontWeight.Bold
                )
            }

            /*
             * =====================================================
             * ESTADO VAZIO
             * =====================================================
             */

            if (
                visibleNotifications
                    .isEmpty()
            ) {

                item {

                    EmptyNotificationsCard()
                }

            } else {

                items(
                    items =
                        visibleNotifications,

                    key = {
                        it.id
                    }
                ) { notification ->

                    CompanyNotificationCard(
                        notification =
                            notification,

                        onClick = {

                            markAsRead(
                                notification.id
                            )
                        },

                        onActionClick = {

                            executeAction(
                                notification
                            )
                        }
                    )
                }
            }

            item {

                Spacer(
                    modifier =
                        Modifier.height(
                            26.dp
                        )
                )
            }
        }
    }
}

@Composable
private fun NotificationSummaryCard(
    unreadCount: Int,
    onMarkAllClick: () -> Unit
) {

    Surface(
        modifier =
            Modifier.fillMaxWidth(),

        color =
            GuinchouGreen.copy(
                alpha =
                    0.07f
            ),

        shape =
            RoundedCornerShape(
                20.dp
            ),

        border =
            BorderStroke(
                width =
                    1.dp,

                color =
                    GuinchouGreen.copy(
                        alpha =
                            0.28f
                    )
            )
    ) {

        Column(
            modifier =
                Modifier.padding(
                    18.dp
                )
        ) {

            Row(
                modifier =
                    Modifier.fillMaxWidth(),

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Surface(
                    color =
                        GuinchouGreen.copy(
                            alpha =
                                0.14f
                        ),

                    shape =
                        RoundedCornerShape(
                            14.dp
                        )
                ) {

                    Icon(
                        imageVector =
                            Icons.Default
                                .Notifications,

                        contentDescription =
                            null,

                        tint =
                            GuinchouGreen,

                        modifier = Modifier
                            .padding(
                                12.dp
                            )
                            .size(
                                28.dp
                            )
                    )
                }

                Spacer(
                    modifier =
                        Modifier.width(
                            14.dp
                        )
                )

                Column(
                    modifier =
                        Modifier.weight(
                            1f
                        )
                ) {

                    Text(
                        text =
                            if (
                                unreadCount == 0
                            ) {
                                "Tudo em dia"
                            } else {
                                "$unreadCount nova${if (unreadCount > 1) "s" else ""} notificação${if (unreadCount > 1) "ões" else ""}"
                            },

                        color =
                            GuinchouWhite,

                        fontSize =
                            17.sp,

                        fontWeight =
                            FontWeight.Bold
                    )

                    Spacer(
                        modifier =
                            Modifier.height(
                                3.dp
                            )
                    )

                    Text(
                        text =
                            if (
                                unreadCount == 0
                            ) {
                                "Você já visualizou todas as atualizações."
                            } else {
                                "Há novidades que podem precisar da sua atenção."
                            },

                        color =
                            GuinchouGray,

                        fontSize =
                            12.sp,

                        lineHeight =
                            17.sp
                    )
                }
            }

            if (
                unreadCount > 0
            ) {

                Spacer(
                    modifier =
                        Modifier.height(
                            15.dp
                        )
                )

                Button(
                    onClick =
                        onMarkAllClick,

                    modifier =
                        Modifier.fillMaxWidth(),

                    shape =
                        RoundedCornerShape(
                            13.dp
                        ),

                    colors =
                        ButtonDefaults
                            .buttonColors(
                                containerColor =
                                    GuinchouGreen,

                                contentColor =
                                    Color.Black
                            )
                ) {

                    Icon(
                        imageVector =
                            Icons.Default
                                .CheckCircle,

                        contentDescription =
                            null,

                        modifier =
                            Modifier.size(
                                19.dp
                            )
                    )

                    Spacer(
                        modifier =
                            Modifier.width(
                                7.dp
                            )
                    )

                    Text(
                        text =
                            "Marcar todas como lidas",

                        fontWeight =
                            FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun CompanyNotificationCard(
    notification: CompanyNotification,
    onClick: () -> Unit,
    onActionClick: () -> Unit
) {

    val icon =
        notificationIcon(
            notification.category
        )

    Surface(
        onClick =
            onClick,

        modifier =
            Modifier.fillMaxWidth(),

        color =
            if (
                notification.isRead
            ) {
                GuinchouSurface
            } else {
                GuinchouGreen.copy(
                    alpha =
                        0.055f
                )
            },

        shape =
            RoundedCornerShape(
                18.dp
            ),

        border =
            BorderStroke(
                width =
                    1.dp,

                color =
                    if (
                        notification.isRead
                    ) {
                        GuinchouBorder
                    } else {
                        GuinchouGreen.copy(
                            alpha =
                                0.38f
                        )
                    }
            )
    ) {

        Column(
            modifier =
                Modifier.padding(
                    16.dp
                )
        ) {

            Row(
                modifier =
                    Modifier.fillMaxWidth(),

                verticalAlignment =
                    Alignment.Top
            ) {

                Surface(
                    color =
                        GuinchouGreen.copy(
                            alpha =
                                0.11f
                        ),

                    shape =
                        RoundedCornerShape(
                            13.dp
                        )
                ) {

                    Icon(
                        imageVector =
                            icon,

                        contentDescription =
                            null,

                        tint =
                            GuinchouGreen,

                        modifier = Modifier
                            .padding(
                                11.dp
                            )
                            .size(
                                23.dp
                            )
                    )
                }

                Spacer(
                    modifier =
                        Modifier.width(
                            13.dp
                        )
                )

                Column(
                    modifier =
                        Modifier.weight(
                            1f
                        )
                ) {

                    Row(
                        modifier =
                            Modifier.fillMaxWidth(),

                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        Text(
                            text =
                                notification.title,

                            color =
                                GuinchouWhite,

                            fontSize =
                                15.sp,

                            fontWeight =
                                if (
                                    notification.isRead
                                ) {
                                    FontWeight.SemiBold
                                } else {
                                    FontWeight.Bold
                                },

                            modifier =
                                Modifier.weight(
                                    1f
                                )
                        )

                        if (
                            !notification.isRead
                        ) {

                            Spacer(
                                modifier =
                                    Modifier.width(
                                        8.dp
                                    )
                            )

                            Surface(
                                modifier =
                                    Modifier.size(
                                        8.dp
                                    ),

                                shape =
                                    CircleShape,

                                color =
                                    GuinchouGreen
                            ) {}
                        }
                    }

                    Spacer(
                        modifier =
                            Modifier.height(
                                5.dp
                            )
                    )

                    Text(
                        text =
                            notification.description,

                        color =
                            GuinchouGray,

                        fontSize =
                            13.sp,

                        lineHeight =
                            19.sp
                    )

                    Spacer(
                        modifier =
                            Modifier.height(
                                8.dp
                            )
                    )

                    Text(
                        text =
                            notification.time,

                        color =
                            GuinchouGray,

                        fontSize =
                            11.sp
                    )
                }
            }

            if (
                notification.actionLabel != null
            ) {

                Spacer(
                    modifier =
                        Modifier.height(
                            12.dp
                        )
                )

                HorizontalDivider(
                    color =
                        GuinchouBorder.copy(
                            alpha =
                                0.7f
                        )
                )

                TextButton(
                    onClick =
                        onActionClick,

                    modifier =
                        Modifier.fillMaxWidth()
                ) {

                    Text(
                        text =
                            notification.actionLabel,

                        color =
                            GuinchouGreen,

                        fontWeight =
                            FontWeight.SemiBold,

                        modifier =
                            Modifier.weight(
                                1f
                            )
                    )

                    Icon(
                        imageVector =
                            Icons.Default
                                .ChevronRight,

                        contentDescription =
                            null,

                        tint =
                            GuinchouGreen
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyNotificationsCard() {

    Surface(
        modifier =
            Modifier.fillMaxWidth(),

        color =
            GuinchouSurface,

        shape =
            RoundedCornerShape(
                18.dp
            ),

        border =
            BorderStroke(
                width =
                    1.dp,

                color =
                    GuinchouBorder
            )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical =
                        35.dp,

                    horizontal =
                        20.dp
                ),

            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            Surface(
                color =
                    GuinchouGreen.copy(
                        alpha =
                            0.1f
                    ),

                shape =
                    CircleShape
            ) {

                Icon(
                    imageVector =
                        Icons.Default
                            .CheckCircle,

                    contentDescription =
                        null,

                    tint =
                        GuinchouGreen,

                    modifier = Modifier
                        .padding(
                            15.dp
                        )
                        .size(
                            30.dp
                        )
                )
            }

            Spacer(
                modifier =
                    Modifier.height(
                        15.dp
                    )
            )

            Text(
                text =
                    "Nenhuma notificação pendente",

                color =
                    GuinchouWhite,

                fontSize =
                    16.sp,

                fontWeight =
                    FontWeight.Bold
            )

            Spacer(
                modifier =
                    Modifier.height(
                        5.dp
                    )
            )

            Text(
                text =
                    "As novas atualizações da empresa aparecerão aqui.",

                color =
                    GuinchouGray,

                fontSize =
                    13.sp
            )
        }
    }
}

private fun notificationIcon(
    category: CompanyNotificationCategory
): ImageVector {

    return when (
        category
    ) {

        CompanyNotificationCategory.DRIVER -> {
            Icons.Default.Person
        }

        CompanyNotificationCategory.FLEET -> {
            Icons.Default.LocalShipping
        }

        CompanyNotificationCategory.CALL -> {
            Icons.Default.Build
        }

        CompanyNotificationCategory.FINANCE -> {
            Icons.Default.Payments
        }

        CompanyNotificationCategory.GENERAL -> {
            Icons.Default.Info
        }
    }
}

@Composable
private fun companyFilterColors() =
    FilterChipDefaults.filterChipColors(
        containerColor =
            GuinchouSurface,

        labelColor =
            GuinchouGray,

        selectedContainerColor =
            GuinchouGreen.copy(
                alpha =
                    0.13f
            ),

        selectedLabelColor =
            GuinchouGreen
    )