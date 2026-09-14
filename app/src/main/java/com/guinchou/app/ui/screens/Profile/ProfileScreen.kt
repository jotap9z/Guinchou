package com.guinchou.app.ui.screens.profile

import androidx.compose.foundation.background
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.guinchou.app.ui.theme.GuinchouBackground
import com.guinchou.app.ui.theme.GuinchouBorder
import com.guinchou.app.ui.theme.GuinchouGray
import com.guinchou.app.ui.theme.GuinchouGreen
import com.guinchou.app.ui.theme.GuinchouSurface
import com.guinchou.app.ui.theme.GuinchouWhite

private enum class ProfilePage {
    MAIN,
    EDIT_PROFILE,
    VEHICLES,
    HISTORY,
    PAYMENTS,
    NOTIFICATIONS,
    SECURITY,
    HELP
}

private data class ProfileVehicle(
    val name: String,
    val plate: String
)

@Composable
fun ProfileScreen(
    userName: String = "João da Silva",
    userEmail: String = "joao@email.com",
    onBackClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {}
) {

    var currentPage by remember {
        mutableStateOf(ProfilePage.MAIN)
    }

    var currentName by remember {
        mutableStateOf(userName)
    }

    var currentEmail by remember {
        mutableStateOf(userEmail)
    }

    var currentCpf by remember {
        mutableStateOf("12345678900")
    }

    var currentPhone by remember {
        mutableStateOf("61999999999")
    }

    val vehicles = remember {
        mutableStateListOf(
            ProfileVehicle(
                name = "Toyota Corolla",
                plate = "ABC1D23"
            )
        )
    }

    when (currentPage) {

        ProfilePage.MAIN -> {

            ProfileMainPage(
                userName = currentName,
                userEmail = currentEmail,

                onBackClick = onBackClick,

                onEditProfileClick = {
                    currentPage = ProfilePage.EDIT_PROFILE
                },

                onVehiclesClick = {
                    currentPage = ProfilePage.VEHICLES
                },

                onHistoryClick = {
                    currentPage = ProfilePage.HISTORY
                },

                onPaymentsClick = {
                    currentPage = ProfilePage.PAYMENTS
                },

                onNotificationsClick = {
                    currentPage = ProfilePage.NOTIFICATIONS
                },

                onSecurityClick = {
                    currentPage = ProfilePage.SECURITY
                },

                onHelpClick = {
                    currentPage = ProfilePage.HELP
                },

                onLogoutClick = onLogoutClick
            )
        }

        ProfilePage.EDIT_PROFILE -> {

            EditProfilePage(
                initialName = currentName,
                initialCpf = currentCpf,
                initialPhone = currentPhone,
                initialEmail = currentEmail,

                onBackClick = {
                    currentPage = ProfilePage.MAIN
                },

                onSaveClick = {
                        name,
                        cpf,
                        phone,
                        email ->

                    currentName = name
                    currentCpf = cpf
                    currentPhone = phone
                    currentEmail = email

                    currentPage = ProfilePage.MAIN
                }
            )
        }

        ProfilePage.VEHICLES -> {

            VehiclesPage(
                vehicles = vehicles,

                onBackClick = {
                    currentPage = ProfilePage.MAIN
                },

                onAddVehicle = {
                        model,
                        plate ->

                    vehicles.add(
                        ProfileVehicle(
                            name = model,
                            plate = plate
                        )
                    )
                },

                onRemoveVehicle = { vehicle ->
                    vehicles.remove(vehicle)
                }
            )
        }

        ProfilePage.HISTORY -> {

            HistoryPage(
                onBackClick = {
                    currentPage = ProfilePage.MAIN
                }
            )
        }

        ProfilePage.PAYMENTS -> {

            PaymentsPage(
                onBackClick = {
                    currentPage = ProfilePage.MAIN
                }
            )
        }

        ProfilePage.NOTIFICATIONS -> {

            NotificationsPage(
                onBackClick = {
                    currentPage = ProfilePage.MAIN
                }
            )
        }

        ProfilePage.SECURITY -> {

            SecurityPage(
                onBackClick = {
                    currentPage = ProfilePage.MAIN
                }
            )
        }

        ProfilePage.HELP -> {

            HelpPage(
                onBackClick = {
                    currentPage = ProfilePage.MAIN
                }
            )
        }
    }
}

@Composable
private fun ProfileMainPage(
    userName: String,
    userEmail: String,
    onBackClick: () -> Unit,
    onEditProfileClick: () -> Unit,
    onVehiclesClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onPaymentsClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onSecurityClick: () -> Unit,
    onHelpClick: () -> Unit,
    onLogoutClick: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GuinchouBackground)
            .verticalScroll(rememberScrollState())
            .padding(
                horizontal = 20.dp,
                vertical = 24.dp
            )
    ) {

        ProfileHeader(
            title = "Meu perfil",
            onBackClick = onBackClick
        )

        Spacer(
            modifier = Modifier.height(28.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = GuinchouSurface,
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(20.dp),

            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .size(84.dp)
                    .clip(CircleShape)
                    .background(GuinchouGreen),

                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = getInitials(userName),
                    color = GuinchouBackground,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            Text(
                text = userName,
                color = GuinchouWhite,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = userEmail,
                color = GuinchouGray,
                fontSize = 14.sp
            )

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            Text(
                text = "Editar perfil",
                color = GuinchouGreen,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .clickable {
                        onEditProfileClick()
                    }
                    .padding(8.dp)
            )
        }

        Spacer(
            modifier = Modifier.height(26.dp)
        )

        ProfileSectionTitle(
            text = "Minha conta"
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        ProfileMenuItem(
            icon = "🚗",
            title = "Meus veículos",
            description = "Cadastre e gerencie seus veículos",
            onClick = onVehiclesClick
        )

        ProfileMenuItem(
            icon = "🧾",
            title = "Histórico de serviços",
            description = "Consulte seus atendimentos anteriores",
            onClick = onHistoryClick
        )

        ProfileMenuItem(
            icon = "💳",
            title = "Formas de pagamento",
            description = "Gerencie seus métodos de pagamento",
            onClick = onPaymentsClick
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        ProfileSectionTitle(
            text = "Preferências"
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        ProfileMenuItem(
            icon = "🔔",
            title = "Notificações",
            description = "Gerencie seus avisos",
            onClick = onNotificationsClick
        )

        ProfileMenuItem(
            icon = "🔒",
            title = "Segurança",
            description = "Senha e proteção da conta",
            onClick = onSecurityClick
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        ProfileSectionTitle(
            text = "Suporte"
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        ProfileMenuItem(
            icon = "❔",
            title = "Ajuda e suporte",
            description = "Dúvidas e atendimento",
            onClick = onHelpClick
        )

        Spacer(
            modifier = Modifier.height(26.dp)
        )

        Button(
            onClick = onLogoutClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),

            shape = RoundedCornerShape(14.dp),

            colors = ButtonDefaults.buttonColors(
                containerColor = GuinchouSurface,
                contentColor = GuinchouWhite
            )
        ) {

            Text(
                text = "Sair da conta",
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Text(
            text = "Guinchou",
            color = GuinchouGreen,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = "Versão de desenvolvimento",
            color = GuinchouGray,
            fontSize = 11.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun EditProfilePage(
    initialName: String,
    initialCpf: String,
    initialPhone: String,
    initialEmail: String,
    onBackClick: () -> Unit,
    onSaveClick: (
        String,
        String,
        String,
        String
    ) -> Unit
) {

    var name by remember {
        mutableStateOf(initialName)
    }

    var cpf by remember {
        mutableStateOf(formatCpf(initialCpf))
    }

    var phone by remember {
        mutableStateOf(formatPhone(initialPhone))
    }

    var email by remember {
        mutableStateOf(initialEmail)
    }

    var error by remember {
        mutableStateOf<String?>(null)
    }

    var showDeleteAccountDialog by remember {
        mutableStateOf(false)
    }

    PageContainer(
        title = "Editar perfil",
        onBackClick = onBackClick
    ) {

        ProfileTextField(
            value = name,
            label = "Nome completo",
            onValueChange = {
                name = it
                error = null
            }
        )

        Spacer(
            modifier = Modifier.height(14.dp)
        )

        ProfileTextField(
            value = cpf,
            label = "CPF",
            keyboardType = KeyboardType.Number,
            onValueChange = {
                cpf = formatCpf(it)
                error = null
            }
        )

        Spacer(
            modifier = Modifier.height(14.dp)
        )

        ProfileTextField(
            value = phone,
            label = "Telefone",
            keyboardType = KeyboardType.Phone,
            onValueChange = {
                phone = formatPhone(it)
                error = null
            }
        )

        Spacer(
            modifier = Modifier.height(14.dp)
        )

        ProfileTextField(
            value = email,
            label = "E-mail",
            keyboardType = KeyboardType.Email,
            onValueChange = {
                email = it
                error = null
            }
        )

        if (error != null) {

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Text(
                text = error!!,
                color = Color.Red,
                fontSize = 13.sp
            )
        }

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        PrimaryButton(
            text = "Salvar alterações",

            onClick = {

                val cpfDigits =
                    cpf.filter {
                        it.isDigit()
                    }

                val phoneDigits =
                    phone.filter {
                        it.isDigit()
                    }

                when {

                    name.trim().length < 3 -> {

                        error =
                            "Informe um nome válido."
                    }

                    cpfDigits.length != 11 -> {

                        error =
                            "Informe um CPF válido."
                    }

                    phoneDigits.length < 10 -> {

                        error =
                            "Informe um telefone válido."
                    }

                    !isValidEmail(email) -> {

                        error =
                            "Informe um e-mail válido."
                    }

                    else -> {

                        onSaveClick(
                            name.trim(),
                            cpfDigits,
                            phoneDigits,
                            email.trim()
                        )
                    }
                }
            }
        )


        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Button(
            onClick = {
                showDeleteAccountDialog = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF3A1717),
                contentColor = Color(0xFFFF6B6B)
            )
        ) {
            Text(
                text = "Excluir conta",
                fontWeight = FontWeight.Bold
            )
        }

        if (showDeleteAccountDialog) {
            AlertDialog(
                onDismissRequest = {
                    showDeleteAccountDialog = false
                },
                containerColor = GuinchouSurface,
                title = {
                    Text(
                        text = "Excluir conta?",
                        color = GuinchouWhite,
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Text(
                        text = "Essa ação excluirá sua conta permanentemente e não poderá ser desfeita. Tem certeza de que deseja continuar?",
                        color = GuinchouGray
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showDeleteAccountDialog = false

                            /*
                             * FRONT-END:
                             * A exclusão real da conta será conectada
                             * ao backend/Supabase posteriormente.
                             */
                        }
                    ) {
                        Text(
                            text = "Excluir conta",
                            color = Color(0xFFFF5252),
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showDeleteAccountDialog = false
                        }
                    ) {
                        Text(
                            text = "Cancelar",
                            color = GuinchouGreen,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            )
        }
    }
}

@Composable
private fun VehiclesPage(
    vehicles: List<ProfileVehicle>,
    onBackClick: () -> Unit,
    onAddVehicle: (
        model: String,
        plate: String
    ) -> Unit,
    onRemoveVehicle: (
        ProfileVehicle
    ) -> Unit
) {

    var showAddVehicleCard by remember {
        mutableStateOf(false)
    }

    var vehicleModel by remember {
        mutableStateOf("")
    }

    var vehiclePlate by remember {
        mutableStateOf("")
    }

    var errorMessage by remember {
        mutableStateOf<String?>(null)
    }

    PageContainer(
        title = "Meus veículos",
        onBackClick = onBackClick
    ) {

        Text(
            text = "Veículos cadastrados",
            color = GuinchouGray,
            fontSize = 13.sp
        )

        Spacer(
            modifier = Modifier.height(14.dp)
        )

        if (vehicles.isEmpty()) {

            EmptyCard(
                text =
                    "Você ainda não possui veículos cadastrados."
            )

        } else {

            vehicles.forEach { vehicle ->

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = GuinchouSurface,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(16.dp)
                ) {

                    Text(
                        text = vehicle.name,
                        color = GuinchouWhite,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )

                    Spacer(
                        modifier = Modifier.height(4.dp)
                    )

                    Text(
                        text = "Placa: ${vehicle.plate}",
                        color = GuinchouGray,
                        fontSize = 13.sp
                    )

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    Text(
                        text = "Remover veículo",
                        color = Color.Red,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.clickable {
                            onRemoveVehicle(vehicle)
                        }
                    )
                }

                Spacer(
                    modifier = Modifier.height(12.dp)
                )
            }
        }

        if (showAddVehicleCard) {

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = GuinchouSurface,
                        shape = RoundedCornerShape(18.dp)
                    )
                    .padding(18.dp)
            ) {

                Text(
                    text = "Adicionar veículo",
                    color = GuinchouWhite,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(6.dp)
                )

                Text(
                    text =
                        "Informe o modelo e a placa do veículo.",
                    color = GuinchouGray,
                    fontSize = 12.sp
                )

                Spacer(
                    modifier = Modifier.height(18.dp)
                )

                ProfileTextField(
                    value = vehicleModel,
                    label = "Modelo",
                    onValueChange = {

                        vehicleModel = it

                        errorMessage = null
                    }
                )

                Spacer(
                    modifier = Modifier.height(14.dp)
                )

                ProfileTextField(
                    value = vehiclePlate,
                    label = "Placa",
                    onValueChange = {

                        vehiclePlate =
                            formatVehiclePlate(it)

                        errorMessage = null
                    }
                )

                if (errorMessage != null) {

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    Text(
                        text = errorMessage!!,
                        color = Color.Red,
                        fontSize = 13.sp
                    )
                }

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                PrimaryButton(
                    text = "Salvar veículo",

                    onClick = {

                        val model =
                            vehicleModel.trim()

                        val plate =
                            vehiclePlate
                                .trim()
                                .uppercase()

                        when {

                            model.isBlank() -> {

                                errorMessage =
                                    "Informe o modelo do veículo."
                            }

                            model.length < 2 -> {

                                errorMessage =
                                    "Informe um modelo válido."
                            }

                            plate.length != 7 -> {

                                errorMessage =
                                    "Informe uma placa válida com 7 caracteres."
                            }

                            else -> {

                                onAddVehicle(
                                    model,
                                    plate
                                )

                                vehicleModel = ""
                                vehiclePlate = ""
                                errorMessage = null
                                showAddVehicleCard = false
                            }
                        }
                    }
                )

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                Text(
                    text = "Cancelar",
                    color = GuinchouGray,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,

                    modifier = Modifier
                        .align(
                            Alignment.CenterHorizontally
                        )
                        .clickable {

                            vehicleModel = ""
                            vehiclePlate = ""
                            errorMessage = null
                            showAddVehicleCard = false
                        }
                        .padding(10.dp)
                )
            }

        } else {

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            PrimaryButton(
                text = "Adicionar veículo",

                onClick = {

                    showAddVehicleCard = true
                }
            )
        }
    }
}

@Composable
private fun HistoryPage(
    onBackClick: () -> Unit
) {

    PageContainer(
        title = "Histórico de serviços",
        onBackClick = onBackClick
    ) {

        ServiceHistoryCard(
            date = "14/09/2026",
            route = "Asa Norte → Águas Claras",
            value = "R$ 200,00",
            status = "Concluído"
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        ServiceHistoryCard(
            date = "03/09/2026",
            route = "Taguatinga → SIA",
            value = "R$ 150,00",
            status = "Concluído"
        )
    }
}

@Composable
private fun PaymentsPage(
    onBackClick: () -> Unit
) {

    var pixEnabled by remember {
        mutableStateOf(true)
    }

    var creditEnabled by remember {
        mutableStateOf(true)
    }

    var debitEnabled by remember {
        mutableStateOf(false)
    }

    PageContainer(
        title = "Formas de pagamento",
        onBackClick = onBackClick
    ) {

        ToggleCard(
            title = "PIX",
            description = "Pagamento instantâneo",
            checked = pixEnabled,
            onCheckedChange = {
                pixEnabled = it
            }
        )

        ToggleCard(
            title = "Cartão de crédito",
            description = "Pagamento com cartão",
            checked = creditEnabled,
            onCheckedChange = {
                creditEnabled = it
            }
        )

        ToggleCard(
            title = "Cartão de débito",
            description = "Pagamento no débito",
            checked = debitEnabled,
            onCheckedChange = {
                debitEnabled = it
            }
        )
    }
}

@Composable
private fun NotificationsPage(
    onBackClick: () -> Unit
) {

    var serviceNotifications by remember {
        mutableStateOf(true)
    }

    var driverNotifications by remember {
        mutableStateOf(true)
    }

    var promotionalNotifications by remember {
        mutableStateOf(false)
    }

    PageContainer(
        title = "Notificações",
        onBackClick = onBackClick
    ) {

        ToggleCard(
            title = "Status do atendimento",
            description =
                "Receber atualizações sobre o guincho",
            checked = serviceNotifications,
            onCheckedChange = {

                serviceNotifications = it
            }
        )

        ToggleCard(
            title = "Chegada do motorista",
            description =
                "Avisar quando o motorista estiver próximo",
            checked = driverNotifications,
            onCheckedChange = {

                driverNotifications = it
            }
        )

        ToggleCard(
            title = "Novidades e promoções",
            description =
                "Receber novidades do Guinchou",
            checked = promotionalNotifications,
            onCheckedChange = {

                promotionalNotifications = it
            }
        )
    }
}

@Composable
private fun SecurityPage(
    onBackClick: () -> Unit
) {

    var currentPassword by remember {
        mutableStateOf("")
    }

    var newPassword by remember {
        mutableStateOf("")
    }

    var confirmPassword by remember {
        mutableStateOf("")
    }

    var message by remember {
        mutableStateOf<String?>(null)
    }

    PageContainer(
        title = "Segurança",
        onBackClick = onBackClick
    ) {

        Text(
            text = "Alterar senha",
            color = GuinchouWhite,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        ProfileTextField(
            value = currentPassword,
            label = "Senha atual",
            onValueChange = {

                currentPassword = it
                message = null
            }
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        ProfileTextField(
            value = newPassword,
            label = "Nova senha",
            onValueChange = {

                newPassword = it
                message = null
            }
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        ProfileTextField(
            value = confirmPassword,
            label = "Confirmar nova senha",
            onValueChange = {

                confirmPassword = it
                message = null
            }
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        if (message != null) {

            Text(
                text = message!!,
                color = GuinchouGreen,
                fontSize = 13.sp
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )
        }

        PrimaryButton(
            text = "Atualizar senha",

            onClick = {

                when {

                    currentPassword.isBlank() -> {

                        message =
                            "Informe sua senha atual."
                    }

                    newPassword.length < 6 -> {

                        message =
                            "A nova senha precisa ter pelo menos 6 caracteres."
                    }

                    newPassword != confirmPassword -> {

                        message =
                            "As novas senhas não coincidem."
                    }

                    else -> {

                        message =
                            "Senha validada no Front-end."
                    }
                }
            }
        )
    }
}

@Composable
private fun HelpPage(
    onBackClick: () -> Unit
) {

    PageContainer(
        title = "Ajuda e suporte",
        onBackClick = onBackClick
    ) {

        SupportCard(
            title =
                "Como solicitar um guincho?",

            description =
                "Na página inicial, toque em solicitar guincho e siga as etapas informando origem, destino, veículo e problema."
        )

        SupportCard(
            title =
                "Como acompanho meu atendimento?",

            description =
                "Após um motorista aceitar a solicitação, o acompanhamento ficará disponível até a conclusão."
        )

        SupportCard(
            title =
                "Problemas com pagamento",

            description =
                "O suporte de pagamentos será integrado posteriormente."
        )

        SupportCard(
            title =
                "Falar com o suporte",

            description =
                "O canal de atendimento será conectado ao backend em uma etapa futura."
        )
    }
}

@Composable
private fun PageContainer(
    title: String,
    onBackClick: () -> Unit,
    content: @Composable () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GuinchouBackground)
            .verticalScroll(
                rememberScrollState()
            )
            .padding(
                horizontal = 20.dp,
                vertical = 24.dp
            )
    ) {

        ProfileHeader(
            title = title,
            onBackClick = onBackClick
        )

        Spacer(
            modifier = Modifier.height(28.dp)
        )

        content()

        Spacer(
            modifier = Modifier.height(30.dp)
        )
    }
}

@Composable
private fun ProfileHeader(
    title: String,
    onBackClick: () -> Unit
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment =
            Alignment.CenterVertically
    ) {

        Text(
            text = "‹",
            color = GuinchouWhite,
            fontSize = 38.sp,

            modifier = Modifier
                .clickable {

                    onBackClick()
                }
                .padding(
                    end = 12.dp
                )
        )

        Text(
            text = title,
            color = GuinchouWhite,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun ProfileMenuItem(
    icon: String,
    title: String,
    description: String,
    onClick: () -> Unit
) {

    Column {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = GuinchouSurface,
                    shape = RoundedCornerShape(16.dp)
                )
                .clickable {

                    onClick()
                }
                .padding(16.dp),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(
                        color = GuinchouBackground,
                        shape = RoundedCornerShape(12.dp)
                    ),

                contentAlignment =
                    Alignment.Center
            ) {

                Text(
                    text = icon,
                    fontSize = 20.sp
                )
            }

            Spacer(
                modifier = Modifier.size(14.dp)
            )

            Column(
                modifier =
                    Modifier.weight(1f)
            ) {

                Text(
                    text = title,
                    color = GuinchouWhite,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(
                    modifier = Modifier.height(3.dp)
                )

                Text(
                    text = description,
                    color = GuinchouGray,
                    fontSize = 12.sp
                )
            }

            Text(
                text = "›",
                color = GuinchouGreen,
                fontSize = 24.sp
            )
        }

        Spacer(
            modifier = Modifier.height(10.dp)
        )
    }
}

@Composable
private fun ProfileSectionTitle(
    text: String
) {

    Text(
        text = text,
        color = GuinchouGray,
        fontSize = 13.sp,
        fontWeight = FontWeight.SemiBold
    )
}

@Composable
private fun ProfileTextField(
    value: String,
    label: String,
    keyboardType: KeyboardType =
        KeyboardType.Text,
    onValueChange: (String) -> Unit
) {

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,

        modifier =
            Modifier.fillMaxWidth(),

        label = {

            Text(
                text = label
            )
        },

        singleLine = true,

        keyboardOptions =
            KeyboardOptions(
                keyboardType =
                    keyboardType
            ),

        colors =
            OutlinedTextFieldDefaults.colors(

                focusedTextColor =
                    GuinchouWhite,

                unfocusedTextColor =
                    GuinchouWhite,

                focusedBorderColor =
                    GuinchouGreen,

                unfocusedBorderColor =
                    GuinchouBorder,

                focusedLabelColor =
                    GuinchouGreen,

                unfocusedLabelColor =
                    GuinchouGray,

                cursorColor =
                    GuinchouGreen,

                focusedContainerColor =
                    GuinchouSurface,

                unfocusedContainerColor =
                    GuinchouSurface
            )
    )
}

@Composable
private fun PrimaryButton(
    text: String,
    onClick: () -> Unit
) {

    Button(
        onClick = onClick,

        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),

        shape =
            RoundedCornerShape(14.dp),

        colors =
            ButtonDefaults.buttonColors(

                containerColor =
                    GuinchouGreen,

                contentColor =
                    GuinchouBackground
            )
    ) {

        Text(
            text = text,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun ToggleCard(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = GuinchouSurface,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp),

        verticalAlignment =
            Alignment.CenterVertically
    ) {

        Column(
            modifier =
                Modifier.weight(1f)
        ) {

            Text(
                text = title,
                color = GuinchouWhite,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = description,
                color = GuinchouGray,
                fontSize = 12.sp
            )
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,

            colors =
                SwitchDefaults.colors(

                    checkedThumbColor =
                        GuinchouBackground,

                    checkedTrackColor =
                        GuinchouGreen
                )
        )
    }

    Spacer(
        modifier = Modifier.height(12.dp)
    )
}

@Composable
private fun ServiceHistoryCard(
    date: String,
    route: String,
    value: String,
    status: String
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = GuinchouSurface,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
    ) {

        Row(
            modifier =
                Modifier.fillMaxWidth(),

            horizontalArrangement =
                Arrangement.SpaceBetween
        ) {

            Text(
                text = date,
                color = GuinchouGray,
                fontSize = 12.sp
            )

            Text(
                text = status,
                color = GuinchouGreen,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
            )
        }

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        Text(
            text = route,
            color = GuinchouWhite,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(6.dp)
        )

        Text(
            text = value,
            color = GuinchouGreen,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun SupportCard(
    title: String,
    description: String
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = GuinchouSurface,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
    ) {

        Text(
            text = title,
            color = GuinchouWhite,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(6.dp)
        )

        Text(
            text = description,
            color = GuinchouGray,
            fontSize = 13.sp
        )
    }

    Spacer(
        modifier = Modifier.height(12.dp)
    )
}

@Composable
private fun EmptyCard(
    text: String
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = GuinchouSurface,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(24.dp),

        contentAlignment =
            Alignment.Center
    ) {

        Text(
            text = text,
            color = GuinchouGray,
            textAlign = TextAlign.Center
        )
    }
}

private fun getInitials(
    name: String
): String {

    val parts =
        name
            .trim()
            .split(" ")
            .filter {
                it.isNotBlank()
            }

    if (parts.isEmpty()) {
        return "U"
    }

    if (parts.size == 1) {

        return parts
            .first()
            .take(1)
            .uppercase()
    }

    return (
            parts
                .first()
                .take(1) +
                    parts
                        .last()
                        .take(1)
            ).uppercase()
}

private fun formatCpf(
    value: String
): String {

    val digits =
        value
            .filter {
                it.isDigit()
            }
            .take(11)

    return buildString {

        digits.forEachIndexed {
                index,
                char ->

            append(char)

            if (
                index == 2 ||
                index == 5
            ) {

                if (
                    index !=
                    digits.lastIndex
                ) {

                    append(".")
                }
            }

            if (index == 8) {

                if (
                    index !=
                    digits.lastIndex
                ) {

                    append("-")
                }
            }
        }
    }
}

private fun formatPhone(
    value: String
): String {

    val digits =
        value
            .filter {
                it.isDigit()
            }
            .take(11)

    if (digits.isEmpty()) {
        return ""
    }

    return buildString {

        append("(")

        digits.forEachIndexed {
                index,
                char ->

            append(char)

            if (index == 1) {

                append(") ")
            }

            if (
                index == 6 &&
                digits.length > 7
            ) {

                append("-")
            }
        }
    }
}

private fun formatVehiclePlate(
    value: String
): String {

    return value
        .uppercase()
        .filter {
            it.isLetterOrDigit()
        }
        .take(7)
}

private fun isValidEmail(
    email: String
): Boolean {

    val value =
        email.trim()

    if (value.isBlank()) {
        return false
    }

    if (
        !value.contains("@")
    ) {
        return false
    }

    val parts =
        value.split("@")

    if (
        parts.size != 2
    ) {
        return false
    }

    if (
        parts[0].isBlank() ||
        parts[1].isBlank()
    ) {
        return false
    }

    if (
        !parts[1].contains(".")
    ) {
        return false
    }

    return true
}
