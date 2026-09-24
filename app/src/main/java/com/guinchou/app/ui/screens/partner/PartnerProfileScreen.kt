package com.guinchou.app.ui.screens.partner

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.guinchou.app.ui.theme.*

private val ProfileCard = Color(0xFF0E1A2A)
private val ProfileCardSoft = Color(0xFF112033)
private val ProfileMuted = Color(0xFF8998AD)
private val ProfileBorder = Color(0xFF1B2B3F)
private val ProfileWarning = Color(0xFFFFB547)
private val ProfileDanger = Color(0xFFFF6262)

private enum class PartnerProfileSection {
    MAIN,
    PERSONAL_DATA,
    PROFESSIONAL_DATA,
    TOW_TRUCK,
    DOCUMENTS,
    SETTINGS,
    SUPPORT
}

@Composable
fun PartnerProfileScreen(
    driverName: String = "João",
    driverEmail: String = "joao@guinchou.com.br",
    driverPhone: String = "(61) 99999-9999",
    driverRating: Double = 4.9,
    onNotificationsClick: () -> Unit = {},
    onPersonalDataClick: () -> Unit = {},
    onProfessionalDataClick: () -> Unit = {},
    onTowTruckClick: () -> Unit = {},
    onDocumentsClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onSupportClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    onHomeClick: () -> Unit = {},
    onCallsClick: () -> Unit = {},
    onEarningsClick: () -> Unit = {}
) {
    var currentSection by remember {
        mutableStateOf(PartnerProfileSection.MAIN)
    }

    when (currentSection) {
        PartnerProfileSection.PERSONAL_DATA -> {
            PersonalDataSection(
                driverName = driverName,
                driverEmail = driverEmail,
                driverPhone = driverPhone,
                onBackClick = {
                    currentSection = PartnerProfileSection.MAIN
                },
                onEditClick = onPersonalDataClick
            )
            return
        }

        PartnerProfileSection.PROFESSIONAL_DATA -> {
            ProfessionalDataSection(
                onBackClick = {
                    currentSection = PartnerProfileSection.MAIN
                },
                onEditClick = onProfessionalDataClick
            )
            return
        }

        PartnerProfileSection.TOW_TRUCK -> {
            TowTruckSection(
                onBackClick = {
                    currentSection = PartnerProfileSection.MAIN
                },
                onEditClick = onTowTruckClick
            )
            return
        }

        PartnerProfileSection.DOCUMENTS -> {
            DocumentsSection(
                onBackClick = {
                    currentSection = PartnerProfileSection.MAIN
                },
                onSendDocumentClick = onDocumentsClick
            )
            return
        }

        PartnerProfileSection.SETTINGS -> {
            SettingsSection(
                onBackClick = {
                    currentSection = PartnerProfileSection.MAIN
                },
                onSettingsActionClick = onSettingsClick
            )
            return
        }

        PartnerProfileSection.SUPPORT -> {
            SupportSection(
                onBackClick = {
                    currentSection = PartnerProfileSection.MAIN
                },
                onSupportClick = onSupportClick
            )
            return
        }

        PartnerProfileSection.MAIN -> Unit
    }

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

            Spacer(Modifier.height(12.dp))

            PartnerProfileHeader(
                onNotificationsClick = onNotificationsClick
            )

            Spacer(Modifier.height(24.dp))

            PartnerIdentityCard(
                driverName = driverName,
                driverEmail = driverEmail,
                driverPhone = driverPhone,
                driverRating = driverRating
            )

            Spacer(Modifier.height(18.dp))

            AccountStatusCard()

            Spacer(Modifier.height(25.dp))

            ProfileSectionTitle(
                title = "Minha conta",
                subtitle = "Gerencie suas informações e seus dados profissionais."
            )

            Spacer(Modifier.height(12.dp))

            ProfileMenuCard(
                icon = Icons.Default.Person,
                title = "Dados pessoais",
                subtitle = "Nome, telefone, e-mail e informações da conta",
                onClick = {
                    currentSection = PartnerProfileSection.PERSONAL_DATA
                }
            )

            Spacer(Modifier.height(10.dp))

            ProfileMenuCard(
                icon = Icons.Default.Badge,
                title = "Dados profissionais",
                subtitle = "CNH e informações do motorista",
                onClick = {
                    currentSection = PartnerProfileSection.PROFESSIONAL_DATA
                }
            )

            Spacer(Modifier.height(10.dp))

            ProfileMenuCard(
                icon = Icons.Default.LocalShipping,
                title = "Meu guincho",
                subtitle = "Consulte os dados do veículo vinculado",
                onClick = {
                    currentSection = PartnerProfileSection.TOW_TRUCK
                }
            )

            Spacer(Modifier.height(10.dp))

            ProfileMenuCard(
                icon = Icons.Default.Description,
                title = "Documentos",
                subtitle = "Acompanhe documentos enviados e vencimentos",
                badge = "1 pendente",
                warning = true,
                onClick = {
                    currentSection = PartnerProfileSection.DOCUMENTS
                }
            )

            Spacer(Modifier.height(25.dp))

            ProfileSectionTitle(
                title = "Preferências",
                subtitle = "Configurações e atendimento da plataforma."
            )

            Spacer(Modifier.height(12.dp))

            ProfileMenuCard(
                icon = Icons.Default.Settings,
                title = "Configurações",
                subtitle = "Notificações, segurança e preferências",
                onClick = {
                    currentSection = PartnerProfileSection.SETTINGS
                }
            )

            Spacer(Modifier.height(10.dp))

            ProfileMenuCard(
                icon = Icons.Default.HelpOutline,
                title = "Ajuda e suporte",
                subtitle = "Central de ajuda e atendimento Guinchou",
                onClick = {
                    currentSection = PartnerProfileSection.SUPPORT
                }
            )

            Spacer(Modifier.height(25.dp))

            PartnerStatsCard()

            Spacer(Modifier.height(20.dp))

            LogoutButton(
                onClick = onLogoutClick
            )

            Spacer(Modifier.height(14.dp))

            Text(
                text = "Guinchou • Área do Parceiro",
                color = ProfileMuted.copy(alpha = .65f),
                fontSize = 9.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(Modifier.height(25.dp))
        }

        PartnerProfileBottomBar(
            onHomeClick = onHomeClick,
            onCallsClick = onCallsClick,
            onEarningsClick = onEarningsClick
        )
    }
}

@Composable
private fun PartnerProfileHeader(
    onNotificationsClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = "Perfil",
                color = GuinchouWhite,
                fontSize = 19.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Text(
                text = "Sua conta de parceiro",
                color = ProfileMuted,
                fontSize = 9.sp
            )
        }

        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(13.dp))
                .background(ProfileCard)
                .border(
                    width = 1.dp,
                    color = ProfileBorder,
                    shape = RoundedCornerShape(13.dp)
                )
                .clickable(onClick = onNotificationsClick),
            contentAlignment = Alignment.Center
        ) {

            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Notificações",
                tint = GuinchouWhite,
                modifier = Modifier.size(20.dp)
            )

            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(
                        x = (-5).dp,
                        y = 5.dp
                    )
                    .size(8.dp)
                    .background(
                        GuinchouGreen,
                        CircleShape
                    )
                    .border(
                        width = 2.dp,
                        color = ProfileCard,
                        shape = CircleShape
                    )
            )
        }
    }
}

@Composable
private fun PartnerIdentityCard(
    driverName: String,
    driverEmail: String,
    driverPhone: String,
    driverRating: Double
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(23.dp))
            .background(
                Brush.horizontalGradient(
                    listOf(
                        GuinchouGreen.copy(alpha = .14f),
                        ProfileCardSoft,
                        ProfileCard
                    )
                )
            )
            .border(
                width = 1.dp,
                color = GuinchouGreen.copy(alpha = .28f),
                shape = RoundedCornerShape(23.dp)
            )
            .padding(18.dp)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(70.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            listOf(
                                GuinchouGreen,
                                GuinchouGreen.copy(alpha = .65f)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = driverName
                        .trim()
                        .take(1)
                        .uppercase(),
                    color = Color(0xFF07110A),
                    fontSize = 27.sp,
                    fontWeight = FontWeight.Black
                )
            }

            Spacer(Modifier.width(15.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = driverName,
                    color = GuinchouWhite,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.ExtraBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = ProfileWarning,
                        modifier = Modifier.size(14.dp)
                    )

                    Spacer(Modifier.width(4.dp))

                    Text(
                        text = "$driverRating",
                        color = GuinchouWhite,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.width(8.dp))

                    Text(
                        text = "• Guincheiro parceiro",
                        color = ProfileMuted,
                        fontSize = 9.sp
                    )
                }
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        GuinchouGreen.copy(alpha = .12f)
                    )
                    .padding(
                        horizontal = 9.dp,
                        vertical = 6.dp
                    )
            ) {

                Text(
                    text = "ATIVO",
                    color = GuinchouGreen,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }

        Spacer(Modifier.height(17.dp))

        HorizontalDivider(
            color = ProfileBorder
        )

        Spacer(Modifier.height(14.dp))

        ProfileContactRow(
            icon = Icons.Default.Email,
            text = driverEmail
        )

        Spacer(Modifier.height(9.dp))

        ProfileContactRow(
            icon = Icons.Default.Phone,
            text = driverPhone
        )
    }
}

@Composable
private fun ProfileContactRow(
    icon: ImageVector,
    text: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = ProfileMuted,
            modifier = Modifier.size(15.dp)
        )

        Spacer(Modifier.width(9.dp))

        Text(
            text = text,
            color = GuinchouWhite.copy(alpha = .80f),
            fontSize = 10.sp
        )
    }
}

@Composable
private fun AccountStatusCard() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(17.dp))
            .background(ProfileCard)
            .border(
                width = 1.dp,
                color = ProfileBorder,
                shape = RoundedCornerShape(17.dp)
            )
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(13.dp))
                .background(
                    GuinchouGreen.copy(alpha = .10f)
                ),
            contentAlignment = Alignment.Center
        ) {

            Icon(
                imageVector = Icons.Default.VerifiedUser,
                contentDescription = null,
                tint = GuinchouGreen,
                modifier = Modifier.size(21.dp)
            )
        }

        Spacer(Modifier.width(11.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = "Conta verificada",
                color = GuinchouWhite,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(2.dp))

            Text(
                text = "Seu cadastro está aprovado para receber chamados.",
                color = ProfileMuted,
                fontSize = 9.sp
            )
        }

        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = GuinchouGreen,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
private fun ProfileSectionTitle(
    title: String,
    subtitle: String
) {
    Column {

        Text(
            text = title,
            color = GuinchouWhite,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(3.dp))

        Text(
            text = subtitle,
            color = ProfileMuted,
            fontSize = 9.sp
        )
    }
}

@Composable
private fun ProfileMenuCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    badge: String? = null,
    warning: Boolean = false,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(17.dp))
            .background(ProfileCard)
            .border(
                width = 1.dp,
                color = if (warning) {
                    ProfileWarning.copy(alpha = .24f)
                } else {
                    ProfileBorder
                },
                shape = RoundedCornerShape(17.dp)
            )
            .clickable(onClick = onClick)
            .padding(13.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(13.dp))
                .background(
                    if (warning) {
                        ProfileWarning.copy(alpha = .10f)
                    } else {
                        GuinchouGreen.copy(alpha = .09f)
                    }
                ),
            contentAlignment = Alignment.Center
        ) {

            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (warning) {
                    ProfileWarning
                } else {
                    GuinchouGreen
                },
                modifier = Modifier.size(21.dp)
            )
        }

        Spacer(Modifier.width(11.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = title,
                color = GuinchouWhite,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(3.dp))

            Text(
                text = subtitle,
                color = ProfileMuted,
                fontSize = 8.sp,
                maxLines = 2,
                lineHeight = 12.sp
            )
        }

        if (badge != null) {

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        ProfileWarning.copy(alpha = .11f)
                    )
                    .padding(
                        horizontal = 7.dp,
                        vertical = 5.dp
                    )
            ) {

                Text(
                    text = badge,
                    color = ProfileWarning,
                    fontSize = 7.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.width(6.dp))
        }

        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = ProfileMuted,
            modifier = Modifier.size(19.dp)
        )
    }
}

@Composable
private fun PartnerStatsCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(ProfileCard)
            .border(
                width = 1.dp,
                color = ProfileBorder,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(16.dp)
    ) {

        Text(
            text = "Sua jornada na Guinchou",
            color = GuinchouWhite,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(4.dp))

        Text(
            text = "Resumo demonstrativo da sua conta de parceiro.",
            color = ProfileMuted,
            fontSize = 9.sp
        )

        Spacer(Modifier.height(17.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(9.dp)
        ) {

            ProfileStatItem(
                modifier = Modifier.weight(1f),
                value = "47",
                label = "Atendimentos"
            )

            ProfileStatItem(
                modifier = Modifier.weight(1f),
                value = "4,9",
                label = "Avaliação"
            )

            ProfileStatItem(
                modifier = Modifier.weight(1f),
                value = "96%",
                label = "Conclusão"
            )
        }
    }
}

@Composable
private fun ProfileStatItem(
    modifier: Modifier,
    value: String,
    label: String
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(
                GuinchouGreen.copy(alpha = .06f)
            )
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = value,
            color = GuinchouGreen,
            fontSize = 15.sp,
            fontWeight = FontWeight.ExtraBold
        )

        Spacer(Modifier.height(3.dp))

        Text(
            text = label,
            color = ProfileMuted,
            fontSize = 8.sp
        )
    }
}

@Composable
private fun LogoutButton(
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(
                ProfileDanger.copy(alpha = .06f)
            )
            .border(
                width = 1.dp,
                color = ProfileDanger.copy(alpha = .25f),
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onClick)
            .padding(vertical = 14.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            imageVector = Icons.Default.Logout,
            contentDescription = null,
            tint = ProfileDanger,
            modifier = Modifier.size(18.dp)
        )

        Spacer(Modifier.width(8.dp))

        Text(
            text = "Sair da conta",
            color = ProfileDanger,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun PartnerProfileBottomBar(
    onHomeClick: () -> Unit,
    onCallsClick: () -> Unit,
    onEarningsClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF08121E))
            .border(
                width = 1.dp,
                color = ProfileBorder.copy(alpha = .85f)
            )
            .padding(
                horizontal = 9.dp,
                vertical = 8.dp
            ),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {

        PartnerProfileBottomItem(
            icon = Icons.Default.Home,
            label = "Início",
            selected = false,
            onClick = onHomeClick
        )

        PartnerProfileBottomItem(
            icon = Icons.AutoMirrored.Filled.FormatListBulleted,
            label = "Chamados",
            selected = false,
            onClick = onCallsClick
        )

        PartnerProfileBottomItem(
            icon = Icons.Default.AccountBalanceWallet,
            label = "Ganhos",
            selected = false,
            onClick = onEarningsClick
        )

        PartnerProfileBottomItem(
            icon = Icons.Default.Person,
            label = "Perfil",
            selected = true,
            onClick = {}
        )
    }
}

@Composable
private fun PartnerProfileBottomItem(
    icon: ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(11.dp))
            .clickable(onClick = onClick)
            .padding(
                horizontal = 12.dp,
                vertical = 4.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (selected) {
                GuinchouGreen
            } else {
                GuinchouWhite.copy(alpha = .88f)
            },
            modifier = Modifier.size(21.dp)
        )

        Spacer(Modifier.height(3.dp))

        Text(
            text = label,
            color = if (selected) {
                GuinchouGreen
            } else {
                GuinchouWhite.copy(alpha = .70f)
            },
            fontSize = 9.sp,
            fontWeight = if (selected) {
                FontWeight.Bold
            } else {
                FontWeight.Medium
            }
        )
    }
}

/*
 * =========================================================
 * ESTRUTURA DAS TELAS INTERNAS
 * =========================================================
 */

@Composable
private fun ProfileInternalScreen(
    title: String,
    subtitle: String,
    onBackClick: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
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
                .padding(
                    horizontal = 18.dp,
                    vertical = 12.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(13.dp))
                    .background(ProfileCard)
                    .border(
                        width = 1.dp,
                        color = ProfileBorder,
                        shape = RoundedCornerShape(13.dp)
                    )
                    .clickable(onClick = onBackClick),
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Voltar",
                    tint = GuinchouWhite,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(Modifier.width(13.dp))

            Column {

                Text(
                    text = title,
                    color = GuinchouWhite,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold
                )

                Text(
                    text = subtitle,
                    color = ProfileMuted,
                    fontSize = 9.sp
                )
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(
                    horizontal = 18.dp,
                    vertical = 10.dp
                ),
            content = content
        )
    }
}

@Composable
private fun ProfileInformationCard(
    icon: ImageVector,
    title: String,
    value: String,
    status: String? = null,
    warning: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(ProfileCard)
            .border(
                width = 1.dp,
                color = if (warning) {
                    ProfileWarning.copy(alpha = .28f)
                } else {
                    ProfileBorder
                },
                shape = RoundedCornerShape(16.dp)
            )
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(13.dp))
                .background(
                    if (warning) {
                        ProfileWarning.copy(alpha = .10f)
                    } else {
                        GuinchouGreen.copy(alpha = .08f)
                    }
                ),
            contentAlignment = Alignment.Center
        ) {

            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (warning) {
                    ProfileWarning
                } else {
                    GuinchouGreen
                },
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = title,
                color = ProfileMuted,
                fontSize = 8.sp
            )

            Spacer(Modifier.height(3.dp))

            Text(
                text = value,
                color = GuinchouWhite,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        if (status != null) {

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        if (warning) {
                            ProfileWarning.copy(alpha = .12f)
                        } else {
                            GuinchouGreen.copy(alpha = .10f)
                        }
                    )
                    .padding(
                        horizontal = 8.dp,
                        vertical = 5.dp
                    )
            ) {

                Text(
                    text = status,
                    color = if (warning) {
                        ProfileWarning
                    } else {
                        GuinchouGreen
                    },
                    fontSize = 7.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun ProfileActionButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(GuinchouGreen)
            .clickable(onClick = onClick),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color(0xFF07110A),
            modifier = Modifier.size(18.dp)
        )

        Spacer(Modifier.width(8.dp))

        Text(
            text = text,
            color = Color(0xFF07110A),
            fontSize = 11.sp,
            fontWeight = FontWeight.ExtraBold
        )
    }
}

/*
 * =========================================================
 * DADOS PESSOAIS
 * =========================================================
 */

@Composable
private fun PersonalDataSection(
    driverName: String,
    driverEmail: String,
    driverPhone: String,
    onBackClick: () -> Unit,
    onEditClick: () -> Unit
) {
    var name by remember { mutableStateOf(driverName) }
    var email by remember { mutableStateOf(driverEmail) }
    var phone by remember { mutableStateOf(driverPhone) }
    var birthDate by remember { mutableStateOf("15/05/1990") }
    var address by remember { mutableStateOf("Brasília - Distrito Federal") }
    var editing by remember { mutableStateOf(false) }
    var saved by remember { mutableStateOf(false) }

    if (editing) {
        ProfileInternalScreen("Editar dados pessoais", "Atualize suas informações", { editing = false }) {
            ProfileEditField("Nome completo", name, { name = it }, Icons.Default.Person)
            ProfileEditField("E-mail", email, { email = it }, Icons.Default.Email, KeyboardType.Email)
            ProfileEditField("Telefone", phone, { phone = it }, Icons.Default.Phone, KeyboardType.Phone)
            ProfileEditField("Data de nascimento", birthDate, { birthDate = it }, Icons.Default.Cake)
            ProfileEditField("Endereço", address, { address = it }, Icons.Default.LocationOn)
            Spacer(Modifier.height(8.dp))
            ProfileActionButton("Salvar alterações", Icons.Default.Save) {
                if (name.isNotBlank() && email.isNotBlank() && phone.isNotBlank()) {
                    saved = true
                    editing = false
                }
            }
            Spacer(Modifier.height(10.dp))
            ProfileSecondaryButton("Cancelar") { editing = false }
            Spacer(Modifier.height(30.dp))
        }
        return
    }

    ProfileInternalScreen("Dados pessoais", "Suas informações de cadastro", onBackClick) {
        if (saved) {
            ProfileSuccessCard("Dados pessoais atualizados com sucesso.")
            Spacer(Modifier.height(14.dp))
        }
        ProfileSectionTitle("Informações pessoais", "Confira os dados vinculados à sua conta.")
        Spacer(Modifier.height(14.dp))
        ProfileInformationCard(Icons.Default.Person, "Nome completo", name)
        Spacer(Modifier.height(10.dp))
        ProfileInformationCard(Icons.Default.Badge, "CPF", "***.***.***-**", "VERIFICADO")
        Spacer(Modifier.height(10.dp))
        ProfileInformationCard(Icons.Default.Cake, "Data de nascimento", birthDate)
        Spacer(Modifier.height(10.dp))
        ProfileInformationCard(Icons.Default.Email, "E-mail", email, "VERIFICADO")
        Spacer(Modifier.height(10.dp))
        ProfileInformationCard(Icons.Default.Phone, "Telefone", phone, "VERIFICADO")
        Spacer(Modifier.height(24.dp))
        ProfileSectionTitle("Endereço", "Endereço principal cadastrado.")
        Spacer(Modifier.height(14.dp))
        ProfileInformationCard(Icons.Default.LocationOn, "Endereço", address)
        Spacer(Modifier.height(22.dp))
        ProfileActionButton("Editar dados pessoais", Icons.Default.Edit) { editing = true }
        Spacer(Modifier.height(30.dp))
    }
}

/*
 * =========================================================
 * DADOS PROFISSIONAIS
 * =========================================================
 */

@Composable
private fun ProfessionalDataSection(
    onBackClick: () -> Unit,
    onEditClick: () -> Unit
) {
    var cnh by remember { mutableStateOf("***********") }
    var category by remember { mutableStateOf("C") }
    var validity by remember { mutableStateOf("18/08/2029") }
    var type by remember { mutableStateOf("Motorista independente") }
    var editing by remember { mutableStateOf(false) }
    var saved by remember { mutableStateOf(false) }

    if (editing) {
        ProfileInternalScreen("Editar dados profissionais", "Atualize os dados do motorista", { editing = false }) {
            ProfileEditField("Número da CNH", cnh, { cnh = it }, Icons.Default.Badge, KeyboardType.Number)
            ProfileEditField("Categoria", category, { category = it.uppercase() }, Icons.Default.DirectionsCar)
            ProfileEditField("Validade da CNH", validity, { validity = it }, Icons.Default.CalendarMonth)
            ProfileEditField("Tipo de cadastro", type, { type = it }, Icons.Default.Work)
            Spacer(Modifier.height(8.dp))
            ProfileActionButton("Salvar dados profissionais", Icons.Default.Save) {
                if (cnh.isNotBlank() && category.isNotBlank() && validity.isNotBlank()) {
                    saved = true
                    editing = false
                }
            }
            Spacer(Modifier.height(10.dp))
            ProfileSecondaryButton("Cancelar") { editing = false }
            Spacer(Modifier.height(30.dp))
        }
        return
    }

    ProfileInternalScreen("Dados profissionais", "Informações do motorista parceiro", onBackClick) {
        if (saved) {
            ProfileSuccessCard("Dados profissionais atualizados com sucesso.")
            Spacer(Modifier.height(14.dp))
        }
        ProfileSectionTitle("Carteira de habilitação", "Dados utilizados para validação do parceiro.")
        Spacer(Modifier.height(14.dp))
        ProfileInformationCard(Icons.Default.Badge, "Número da CNH", cnh, "APROVADA")
        Spacer(Modifier.height(10.dp))
        ProfileInformationCard(Icons.Default.DirectionsCar, "Categoria", category)
        Spacer(Modifier.height(10.dp))
        ProfileInformationCard(Icons.Default.CalendarMonth, "Validade da CNH", validity, "VÁLIDA")
        Spacer(Modifier.height(24.dp))
        ProfileSectionTitle("Situação profissional", "Status atual na plataforma.")
        Spacer(Modifier.height(14.dp))
        ProfileInformationCard(Icons.Default.VerifiedUser, "Cadastro profissional", type, "APROVADO")
        Spacer(Modifier.height(10.dp))
        ProfileInformationCard(Icons.Default.Work, "Situação", "Liberado para receber chamados", "ATIVO")
        Spacer(Modifier.height(22.dp))
        ProfileActionButton("Atualizar dados profissionais", Icons.Default.Edit) { editing = true }
        Spacer(Modifier.height(30.dp))
    }
}

/*
 * =========================================================
 * MEU GUINCHO
 * =========================================================
 */

@Composable
private fun TowTruckSection(
    onBackClick: () -> Unit,
    onEditClick: () -> Unit
) {
    var plate by remember { mutableStateOf("ABC1D23") }
    var model by remember { mutableStateOf("Mercedes-Benz Accelo") }
    var year by remember { mutableStateOf("2021") }
    var type by remember { mutableStateOf("Plataforma") }
    var capacity by remember { mutableStateOf("Até 4.000 kg") }
    var editing by remember { mutableStateOf(false) }
    var saved by remember { mutableStateOf(false) }

    if (editing) {
        ProfileInternalScreen("Editar meu guincho", "Atualize os dados do veículo", { editing = false }) {
            ProfileEditField("Placa", plate, { plate = it.uppercase() }, Icons.Default.Pin)
            ProfileEditField("Marca / Modelo", model, { model = it }, Icons.Default.LocalShipping)
            ProfileEditField("Ano", year, { year = it }, Icons.Default.CalendarMonth, KeyboardType.Number)
            ProfileSelectField(
                label = "Tipo",
                value = type,
                options = listOf(
                    "Plataforma",
                    "Asa Delta",
                    "Lança",
                    "Pesado",
                    "Reboque"
                ),
                icon = Icons.Default.Build,
                onValueChange = { type = it }
            )
            Spacer(Modifier.height(12.dp))
            ProfileEditField("Capacidade", capacity, { capacity = it }, Icons.Default.Scale)
            Spacer(Modifier.height(8.dp))
            ProfileActionButton("Salvar informações", Icons.Default.Save) {
                if (plate.isNotBlank() && model.isNotBlank()) {
                    saved = true
                    editing = false
                }
            }
            Spacer(Modifier.height(10.dp))
            ProfileSecondaryButton("Cancelar") { editing = false }
            Spacer(Modifier.height(30.dp))
        }
        return
    }

    ProfileInternalScreen("Meu guincho", "Veículo vinculado à sua conta", onBackClick) {
        if (saved) {
            ProfileSuccessCard("Informações do guincho atualizadas com sucesso.")
            Spacer(Modifier.height(14.dp))
        }
        ProfileInformationCard(Icons.Default.Pin, "Placa", plate, "VERIFICADA")
        Spacer(Modifier.height(10.dp))
        ProfileInformationCard(Icons.Default.LocalShipping, "Marca / Modelo", model)
        Spacer(Modifier.height(10.dp))
        ProfileInformationCard(Icons.Default.CalendarMonth, "Ano", year)
        Spacer(Modifier.height(10.dp))
        ProfileInformationCard(Icons.Default.Build, "Tipo", type)
        Spacer(Modifier.height(10.dp))
        ProfileInformationCard(Icons.Default.Scale, "Capacidade", capacity)
        Spacer(Modifier.height(22.dp))
        ProfileActionButton("Editar informações do guincho", Icons.Default.Edit) { editing = true }
        Spacer(Modifier.height(30.dp))
    }
}

/*
 * =========================================================
 * DOCUMENTOS
 * =========================================================
 */

@Composable
private fun DocumentsSection(
    onBackClick: () -> Unit,
    onSendDocumentClick: () -> Unit
) {
    var selectedFile by remember { mutableStateOf<String?>(null) }
    var showTypeDialog by remember { mutableStateOf(false) }
    var selectedType by remember { mutableStateOf("Comprovante de endereço") }

    val fileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            selectedFile = uri.lastPathSegment?.substringAfterLast("/") ?: "Documento selecionado"
        }
    }

    if (showTypeDialog) {
        AlertDialog(
            onDismissRequest = { showTypeDialog = false },
            containerColor = ProfileCard,
            title = { Text("Selecione o documento", color = GuinchouWhite) },
            text = {
                Column {
                    listOf(
                        "Comprovante de endereço",
                        "Carteira Nacional de Habilitação",
                        "Documento do guincho",
                        "Seguro do veículo"
                    ).forEach { item ->
                        Row(
                            Modifier.fillMaxWidth().clickable {
                                selectedType = item
                                showTypeDialog = false
                                fileLauncher.launch("*/*")
                            }.padding(vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedType == item,
                                onClick = null,
                                colors = RadioButtonDefaults.colors(selectedColor = GuinchouGreen)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(item, color = GuinchouWhite, fontSize = 11.sp)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showTypeDialog = false }) {
                    Text("Cancelar", color = GuinchouGreen)
                }
            }
        )
    }

    ProfileInternalScreen("Documentos", "Validação e situação dos documentos", onBackClick) {
        ProfileSectionTitle(
            "Documentos do parceiro",
            "Mantenha seus documentos atualizados para continuar recebendo chamados."
        )
        Spacer(Modifier.height(15.dp))
        DocumentStatusCard("Carteira Nacional de Habilitação", "CNH • Categoria C", "APROVADO", true)
        Spacer(Modifier.height(10.dp))
        DocumentStatusCard("Documento do guincho", "CRLV • ABC1D23", "APROVADO", true)
        Spacer(Modifier.height(10.dp))
        if (selectedFile == null) {
            DocumentStatusCard("Comprovante de endereço", "Necessário atualizar o documento", "PENDENTE", false)
        } else {
            DocumentStatusCard(selectedType, selectedFile ?: "Arquivo selecionado", "EM ANÁLISE", false)
        }
        Spacer(Modifier.height(10.dp))
        DocumentStatusCard("Seguro do veículo", "Validade: 15/11/2026", "APROVADO", true)

        if (selectedFile != null) {
            Spacer(Modifier.height(18.dp))
            ProfileSuccessCard("Arquivo selecionado. O envio definitivo será conectado ao backend.")
        }

        Spacer(Modifier.height(22.dp))
        ProfileActionButton("Enviar novo documento", Icons.Default.UploadFile) {
            showTypeDialog = true
        }
        Spacer(Modifier.height(30.dp))
    }
}

@Composable
private fun DocumentStatusCard(
    title: String,
    description: String,
    status: String,
    approved: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(17.dp))
            .background(ProfileCard)
            .border(
                width = 1.dp,
                color = if (approved) {
                    ProfileBorder
                } else {
                    ProfileWarning.copy(alpha = .28f)
                },
                shape = RoundedCornerShape(17.dp)
            )
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(13.dp))
                .background(
                    if (approved) {
                        GuinchouGreen.copy(alpha = .09f)
                    } else {
                        ProfileWarning.copy(alpha = .10f)
                    }
                ),
            contentAlignment = Alignment.Center
        ) {

            Icon(
                imageVector = Icons.Default.Description,
                contentDescription = null,
                tint = if (approved) {
                    GuinchouGreen
                } else {
                    ProfileWarning
                },
                modifier = Modifier.size(21.dp)
            )
        }

        Spacer(Modifier.width(11.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = title,
                color = GuinchouWhite,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(3.dp))

            Text(
                text = description,
                color = ProfileMuted,
                fontSize = 8.sp
            )
        }

        Text(
            text = status,
            color = if (approved) {
                GuinchouGreen
            } else {
                ProfileWarning
            },
            fontSize = 7.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

/*
 * =========================================================
 * CONFIGURAÇÕES
 * =========================================================
 */

@Composable
private fun SettingsSection(
    onBackClick: () -> Unit,
    onSettingsActionClick: () -> Unit
) {
    var callNotifications by remember { mutableStateOf(true) }
    var earningsNotifications by remember { mutableStateOf(true) }
    var documentNotifications by remember { mutableStateOf(true) }
    var dialog by remember { mutableStateOf<String?>(null) }
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var language by remember { mutableStateOf("Português (Brasil)") }
    var message by remember { mutableStateOf<String?>(null) }

    if (dialog == "password") {
        AlertDialog(
            onDismissRequest = { dialog = null },
            containerColor = ProfileCard,
            title = { Text("Alterar senha", color = GuinchouWhite) },
            text = {
                Column {
                    ProfilePasswordField("Senha atual", currentPassword) { currentPassword = it }
                    Spacer(Modifier.height(10.dp))
                    ProfilePasswordField("Nova senha", newPassword) { newPassword = it }
                    Spacer(Modifier.height(10.dp))
                    ProfilePasswordField("Confirmar nova senha", confirmPassword) { confirmPassword = it }
                    if (newPassword.isNotEmpty() && newPassword != confirmPassword) {
                        Spacer(Modifier.height(7.dp))
                        Text("As senhas não coincidem.", color = ProfileDanger, fontSize = 9.sp)
                    }
                }
            },
            confirmButton = {
                TextButton(
                    enabled = currentPassword.isNotBlank() && newPassword.length >= 6 && newPassword == confirmPassword,
                    onClick = {
                        dialog = null
                        currentPassword = ""
                        newPassword = ""
                        confirmPassword = ""
                        message = "Validação concluída. A troca real da senha será conectada ao Auth."
                    }
                ) { Text("Salvar", color = GuinchouGreen) }
            },
            dismissButton = {
                TextButton(onClick = { dialog = null }) { Text("Cancelar", color = ProfileMuted) }
            }
        )
    }

    if (dialog == "security") {
        AlertDialog(
            onDismissRequest = { dialog = null },
            containerColor = ProfileCard,
            title = { Text("Segurança da conta", color = GuinchouWhite) },
            text = {
                Column {
                    Text("E-mail verificado", color = GuinchouWhite, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(6.dp))
                    Text("Telefone verificado", color = GuinchouWhite, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(6.dp))
                    Text("Sessão atual ativa neste dispositivo.", color = ProfileMuted, fontSize = 10.sp)
                }
            },
            confirmButton = {
                TextButton(onClick = { dialog = null }) { Text("Fechar", color = GuinchouGreen) }
            }
        )
    }

    if (dialog == "language") {
        AlertDialog(
            onDismissRequest = { dialog = null },
            containerColor = ProfileCard,
            title = { Text("Idioma", color = GuinchouWhite) },
            text = {
                Column {
                    listOf("Português (Brasil)", "English", "Español").forEach { item ->
                        Row(
                            Modifier.fillMaxWidth().clickable {
                                language = item
                                dialog = null
                                message = if (item == "Português (Brasil)")
                                    "Idioma definido como Português (Brasil)."
                                else
                                    "$item selecionado. A tradução completa depende da internacionalização do app."
                            }.padding(vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = language == item,
                                onClick = null,
                                colors = RadioButtonDefaults.colors(selectedColor = GuinchouGreen)
                            )
                            Text(item, color = GuinchouWhite)
                        }
                    }
                }
            },
            confirmButton = {}
        )
    }

    ProfileInternalScreen("Configurações", "Preferências da sua conta", onBackClick) {
        message?.let {
            ProfileSuccessCard(it)
            Spacer(Modifier.height(14.dp))
        }

        ProfileSectionTitle("Notificações", "Escolha quais alertas deseja receber.")
        Spacer(Modifier.height(14.dp))
        ProfileSwitchCard(Icons.Default.NotificationsActive, "Novos chamados",
            "Receber alerta quando houver um novo chamado próximo.", callNotifications) { callNotifications = it }
        Spacer(Modifier.height(10.dp))
        ProfileSwitchCard(Icons.Default.AccountBalanceWallet, "Ganhos",
            "Receber atualizações sobre pagamentos e ganhos.", earningsNotifications) { earningsNotifications = it }
        Spacer(Modifier.height(10.dp))
        ProfileSwitchCard(Icons.Default.Description, "Documentos",
            "Alertas de documentos pendentes ou próximos do vencimento.", documentNotifications) { documentNotifications = it }

        Spacer(Modifier.height(25.dp))
        ProfileSectionTitle("Conta e segurança", "Configurações relacionadas ao acesso.")
        Spacer(Modifier.height(14.dp))
        SettingsActionCard(Icons.Default.Lock, "Alterar senha", "Atualize a senha da sua conta.") { dialog = "password" }
        Spacer(Modifier.height(10.dp))
        SettingsActionCard(Icons.Default.Security, "Segurança da conta", "Gerencie opções de proteção e acesso.") { dialog = "security" }
        Spacer(Modifier.height(10.dp))
        SettingsActionCard(Icons.Default.Language, "Idioma", language, if (language == "Português (Brasil)") "PT-BR" else null) {
            dialog = "language"
        }
        Spacer(Modifier.height(30.dp))
    }
}

@Composable
private fun ProfileSwitchCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(17.dp))
            .background(ProfileCard)
            .border(
                width = 1.dp,
                color = ProfileBorder,
                shape = RoundedCornerShape(17.dp)
            )
            .padding(13.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(13.dp))
                .background(
                    GuinchouGreen.copy(alpha = .08f)
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

        Spacer(Modifier.width(11.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = title,
                color = GuinchouWhite,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(3.dp))

            Text(
                text = subtitle,
                color = ProfileMuted,
                fontSize = 8.sp,
                lineHeight = 12.sp
            )
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color(0xFF07110A),
                checkedTrackColor = GuinchouGreen,
                uncheckedThumbColor = ProfileMuted,
                uncheckedTrackColor = ProfileCardSoft
            )
        )
    }
}

@Composable
private fun SettingsActionCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    trailingText: String? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(17.dp))
            .background(ProfileCard)
            .border(
                width = 1.dp,
                color = ProfileBorder,
                shape = RoundedCornerShape(17.dp)
            )
            .clickable(onClick = onClick)
            .padding(13.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(13.dp))
                .background(
                    GuinchouGreen.copy(alpha = .08f)
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

        Spacer(Modifier.width(11.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = title,
                color = GuinchouWhite,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(3.dp))

            Text(
                text = subtitle,
                color = ProfileMuted,
                fontSize = 8.sp
            )
        }

        if (trailingText != null) {

            Text(
                text = trailingText,
                color = GuinchouGreen,
                fontSize = 8.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.width(5.dp))
        }

        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = ProfileMuted,
            modifier = Modifier.size(18.dp)
        )
    }
}

/*
 * =========================================================
 * AJUDA E SUPORTE
 * =========================================================
 */

@Composable
private fun SupportSection(
    onBackClick: () -> Unit,
    onSupportClick: () -> Unit
) {
    var helpTopic by remember { mutableStateOf<String?>(null) }
    var contactDialog by remember { mutableStateOf(false) }
    var subject by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var sent by remember { mutableStateOf(false) }

    helpTopic?.let { topic ->
        AlertDialog(
            onDismissRequest = { helpTopic = null },
            containerColor = ProfileCard,
            title = { Text(topic, color = GuinchouWhite) },
            text = { Text(profileHelpText(topic), color = ProfileMuted, fontSize = 11.sp, lineHeight = 17.sp) },
            confirmButton = {
                TextButton(onClick = { helpTopic = null }) { Text("Entendi", color = GuinchouGreen) }
            }
        )
    }

    if (contactDialog) {
        AlertDialog(
            onDismissRequest = { contactDialog = false },
            containerColor = ProfileCard,
            title = { Text("Falar com o suporte", color = GuinchouWhite) },
            text = {
                Column {
                    ProfileSelectField(
                        label = "Assunto",
                        value = subject,
                        options = listOf(
                            "Problema com chamado",
                            "Cliente não localizado",
                            "Problema durante o atendimento",
                            "Cancelamento de chamado",
                            "Problema com pagamento ou repasse",
                            "Documento pendente ou recusado",
                            "Problema com minha conta",
                            "Problema com o aplicativo",
                            "Outro assunto"
                        ),
                        icon = Icons.Default.SupportAgent,
                        placeholder = "Selecione o motivo",
                        onValueChange = { subject = it }
                    )
                    Spacer(Modifier.height(10.dp))
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Descreva o problema") },
                        minLines = 4,
                        modifier = Modifier.fillMaxWidth(),
                        colors = profileFieldColors()
                    )
                }
            },
            confirmButton = {
                TextButton(
                    enabled = subject.isNotBlank() && description.isNotBlank(),
                    onClick = {
                        sent = true
                        contactDialog = false
                        subject = ""
                        description = ""
                    }
                ) { Text("Enviar", color = GuinchouGreen) }
            },
            dismissButton = {
                TextButton(onClick = { contactDialog = false }) { Text("Cancelar", color = ProfileMuted) }
            }
        )
    }

    ProfileInternalScreen("Ajuda e suporte", "Como podemos ajudar?", onBackClick) {
        if (sent) {
            ProfileSuccessCard("Solicitação registrada localmente. O envio real será conectado ao backend.")
            Spacer(Modifier.height(14.dp))
        }

        ProfileSectionTitle("Encontre uma solução", "Selecione o assunto relacionado à sua dúvida.")
        Spacer(Modifier.height(14.dp))

        SupportOptionCard(Icons.Default.LocalShipping, "Problemas com chamados",
            "Aceite, cancelamento ou andamento de serviços.") { helpTopic = "Problemas com chamados" }
        Spacer(Modifier.height(10.dp))
        SupportOptionCard(Icons.Default.AccountBalanceWallet, "Ganhos e pagamentos",
            "Valores, repasses e histórico financeiro.") { helpTopic = "Ganhos e pagamentos" }
        Spacer(Modifier.height(10.dp))
        SupportOptionCard(Icons.Default.Description, "Documentos e cadastro",
            "Envio, aprovação e atualização de documentos.") { helpTopic = "Documentos e cadastro" }
        Spacer(Modifier.height(10.dp))
        SupportOptionCard(Icons.Default.Person, "Minha conta",
            "Acesso, dados pessoais e configurações.") { helpTopic = "Minha conta" }

        Spacer(Modifier.height(25.dp))
        ProfileSectionTitle("Ainda precisa de ajuda?", "Entre em contato com nossa equipe.")
        Spacer(Modifier.height(14.dp))
        ProfileActionButton("Falar com o suporte", Icons.Default.Chat) { contactDialog = true }
        Spacer(Modifier.height(30.dp))
    }
}

@Composable
private fun SupportOptionCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(17.dp))
            .background(ProfileCard)
            .border(
                width = 1.dp,
                color = ProfileBorder,
                shape = RoundedCornerShape(17.dp)
            )
            .clickable(onClick = onClick)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(13.dp))
                .background(
                    GuinchouGreen.copy(alpha = .08f)
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

        Spacer(Modifier.width(11.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = title,
                color = GuinchouWhite,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(3.dp))

            Text(
                text = subtitle,
                color = ProfileMuted,
                fontSize = 8.sp
            )
        }

        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = ProfileMuted,
            modifier = Modifier.size(19.dp)
        )
    }
}

/*
 * =========================================================
 * COMPONENTES FUNCIONAIS DO PERFIL
 * =========================================================
 */

@Composable
private fun ProfileEditField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    icon: ImageVector,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(label) },
        leadingIcon = { Icon(icon, null, tint = GuinchouGreen) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        colors = profileFieldColors(),
        shape = RoundedCornerShape(15.dp)
    )
    Spacer(Modifier.height(12.dp))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileSelectField(
    label: String,
    value: String,
    options: List<String>,
    icon: ImageVector,
    placeholder: String = "Selecione uma opção",
    onValueChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            label = { Text(label) },
            placeholder = { Text(placeholder) },
            leadingIcon = {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = GuinchouGreen
                )
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            colors = profileFieldColors(),
            shape = RoundedCornerShape(15.dp)
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(ProfileCard)
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = option,
                            color = GuinchouWhite,
                            fontSize = 11.sp
                        )
                    },
                    onClick = {
                        onValueChange(option)
                        expanded = false
                    },
                    leadingIcon = {
                        if (value == option) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = GuinchouGreen,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    },
                    colors = MenuDefaults.itemColors(
                        textColor = GuinchouWhite,
                        leadingIconColor = GuinchouGreen
                    )
                )
            }
        }
    }
}

@Composable
private fun ProfileSimpleField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        colors = profileFieldColors(),
        shape = RoundedCornerShape(14.dp)
    )
}

@Composable
private fun ProfilePasswordField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        visualTransformation = PasswordVisualTransformation(),
        colors = profileFieldColors(),
        shape = RoundedCornerShape(14.dp)
    )
}

@Composable
private fun profileFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedTextColor = GuinchouWhite,
    unfocusedTextColor = GuinchouWhite,
    focusedBorderColor = GuinchouGreen,
    unfocusedBorderColor = ProfileBorder,
    focusedLabelColor = GuinchouGreen,
    unfocusedLabelColor = ProfileMuted,
    cursorColor = GuinchouGreen,
    focusedContainerColor = ProfileCard,
    unfocusedContainerColor = ProfileCard
)

@Composable
private fun ProfileSecondaryButton(
    text: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, ProfileBorder, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(text, color = GuinchouWhite, fontSize = 11.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun ProfileSuccessCard(message: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(15.dp))
            .background(GuinchouGreen.copy(alpha = .08f))
            .border(1.dp, GuinchouGreen.copy(alpha = .25f), RoundedCornerShape(15.dp))
            .padding(13.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.CheckCircle, null, tint = GuinchouGreen, modifier = Modifier.size(19.dp))
        Spacer(Modifier.width(9.dp))
        Text(message, color = GuinchouWhite, fontSize = 9.sp, lineHeight = 13.sp)
    }
}

private fun profileHelpText(topic: String): String = when (topic) {
    "Problemas com chamados" ->
        "Confira sua conexão e o status do atendimento. Em um chamado ativo, verifique as etapas antes de cancelar ou finalizar o serviço."
    "Ganhos e pagamentos" ->
        "Use a área Ganhos para acompanhar serviços concluídos, valores e histórico financeiro. A integração de repasses será conectada ao backend."
    "Documentos e cadastro" ->
        "Documentos pendentes podem limitar a conta. Na seção Documentos você pode selecionar um novo arquivo e acompanhar o status apresentado no aplicativo."
    "Minha conta" ->
        "No Perfil você pode revisar dados pessoais, dados profissionais, informações do guincho, documentos e preferências da conta."
    else -> "Selecione uma categoria para consultar as orientações."
}

