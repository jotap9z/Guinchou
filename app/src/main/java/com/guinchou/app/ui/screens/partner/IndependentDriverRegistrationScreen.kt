package com.guinchou.app.ui.screens.partner

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.guinchou.app.ui.theme.GuinchouBackground
import com.guinchou.app.ui.theme.GuinchouBorder
import com.guinchou.app.ui.theme.GuinchouGray
import com.guinchou.app.ui.theme.GuinchouGreen
import com.guinchou.app.ui.theme.GuinchouSurface
import com.guinchou.app.ui.theme.GuinchouWhite
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

private const val REGISTRATION_STEPS = 6
private const val REGISTRATION_FEE = 10.0


private data class CepAddressResult(
    val street: String,
    val neighborhood: String,
    val city: String,
    val state: String
)

private data class DriverRegistrationData(
    val fullName: String = "",
    val cpf: String = "",
    val phone: String = "",
    val email: String = "",
    val birthDate: String = "",
    val cnhNumber: String = "",
    val cnhCategory: String = "",
    val cnhExpiration: String = "",
    val cep: String = "",
    val address: String = "",
    val addressNumber: String = "",
    val complement: String = "",
    val city: String = "",
    val state: String = "",
    val towTruckType: String = "",
    val brand: String = "",
    val model: String = "",
    val year: String = "",
    val plate: String = "",
    val color: String = "",
    val renavam: String = "",
    val vehicleDocumentExpiration: String = "",
    val cnhDocumentUri: Uri? = null,
    val vehicleDocumentUri: Uri? = null,
    val addressProofUri: Uri? = null,
    val paymentMethod: String = ""
)

@Composable
fun IndependentDriverRegistrationScreen(
    onBackClick: () -> Unit = {},
    onRegistrationFinished: () -> Unit = {}
) {
    var currentStep by remember { mutableIntStateOf(1) }
    var registration by remember { mutableStateOf(DriverRegistrationData()) }
    var showExitDialog by remember { mutableStateOf(false) }

    fun goBack() {
        if (currentStep > 1) {
            currentStep--
        } else {
            showExitDialog = true
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GuinchouBackground)
    ) {
        when (currentStep) {
            1 -> PersonalDataStep(
                registration = registration,
                onRegistrationChange = { registration = it },
                onBackClick = ::goBack,
                onContinueClick = { currentStep = 2 }
            )

            2 -> TowTruckDataStep(
                registration = registration,
                onRegistrationChange = { registration = it },
                onBackClick = ::goBack,
                onContinueClick = { currentStep = 3 }
            )

            3 -> DocumentsStep(
                registration = registration,
                onRegistrationChange = { registration = it },
                onBackClick = ::goBack,
                onContinueClick = { currentStep = 4 }
            )

            4 -> ReviewStep(
                registration = registration,
                onBackClick = ::goBack,
                onEditPersonalClick = { currentStep = 1 },
                onEditVehicleClick = { currentStep = 2 },
                onEditDocumentsClick = { currentStep = 3 },
                onContinueClick = { currentStep = 5 }
            )

            5 -> RegistrationPaymentStep(
                registration = registration,
                onRegistrationChange = { registration = it },
                onBackClick = ::goBack,
                onPaymentConfirmed = { currentStep = 6 }
            )

            6 -> RegistrationSubmittedStep(
                registration = registration,
                onFinishClick = onRegistrationFinished
            )
        }
    }

    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            containerColor = GuinchouSurface,
            title = {
                Text(
                    text = "Sair do cadastro?",
                    color = GuinchouWhite,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "Os dados preenchidos ainda não foram salvos no servidor. Deseja voltar para a seleção de parceiros?",
                    color = GuinchouGray
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showExitDialog = false
                        onBackClick()
                    }
                ) {
                    Text(
                        text = "Sair",
                        color = Color(0xFFFF6B6B),
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showExitDialog = false }
                ) {
                    Text(
                        text = "Continuar cadastro",
                        color = GuinchouGreen,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PersonalDataStep(
    registration: DriverRegistrationData,
    onRegistrationChange: (DriverRegistrationData) -> Unit,
    onBackClick: () -> Unit,
    onContinueClick: () -> Unit
) {
    var fullName by remember(registration.fullName) { mutableStateOf(registration.fullName) }
    var cpf by remember(registration.cpf) { mutableStateOf(formatCpf(registration.cpf)) }
    var phone by remember(registration.phone) { mutableStateOf(formatPhone(registration.phone)) }
    var email by remember(registration.email) { mutableStateOf(registration.email) }
    var birthDate by remember(registration.birthDate) { mutableStateOf(registration.birthDate) }
    var cnhNumber by remember(registration.cnhNumber) { mutableStateOf(registration.cnhNumber) }
    var cnhCategory by remember(registration.cnhCategory) { mutableStateOf(registration.cnhCategory) }
    var cnhExpiration by remember(registration.cnhExpiration) { mutableStateOf(registration.cnhExpiration) }
    var cep by remember(registration.cep) { mutableStateOf(formatCep(registration.cep)) }
    var address by remember(registration.address) { mutableStateOf(registration.address) }
    var addressNumber by remember(registration.addressNumber) { mutableStateOf(registration.addressNumber) }
    var complement by remember(registration.complement) { mutableStateOf(registration.complement) }
    var city by remember(registration.city) { mutableStateOf(registration.city) }
    var state by remember(registration.state) { mutableStateOf(registration.state) }
    var categoryExpanded by remember { mutableStateOf(false) }
    var acceptedInformation by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    var isSearchingCep by remember {
        mutableStateOf(false)
    }

    var cepLookupMessage by remember {
        mutableStateOf<String?>(null)
    }

    var cepLookupSucceeded by remember {
        mutableStateOf(false)
    }

    var lastSearchedCep by remember {
        mutableStateOf("")
    }

    val coroutineScope = rememberCoroutineScope()

    val cnhCategories = listOf("A", "B", "AB", "C", "D", "E", "AC", "AD", "AE")

    StepContainer(
        step = 1,
        title = "Cadastro do motorista",
        subtitle = "Motorista independente",
        stepName = "Dados pessoais",
        onBackClick = onBackClick
    ) {
        SectionTitle(
            title = "Dados pessoais",
            description = "Preencha seus dados para iniciar o cadastro."
        )

        Spacer(modifier = Modifier.height(16.dp))

        DriverTextField(
            value = fullName,
            label = "Nome completo",
            required = true,
            onValueChange = {
                fullName = it
                errorMessage = null
            }
        )

        FormSpacer()

        DriverTextField(
            value = cpf,
            label = "CPF",
            required = true,
            keyboardType = KeyboardType.Number,
            onValueChange = {
                cpf = formatCpf(it)
                errorMessage = null
            }
        )

        FormSpacer()

        DriverTextField(
            value = phone,
            label = "Telefone",
            required = true,
            keyboardType = KeyboardType.Phone,
            onValueChange = {
                phone = formatPhone(it)
                errorMessage = null
            }
        )

        FormSpacer()

        DriverTextField(
            value = email,
            label = "E-mail",
            required = true,
            keyboardType = KeyboardType.Email,
            onValueChange = {
                email = it
                errorMessage = null
            }
        )

        FormSpacer()

        DriverTextField(
            value = birthDate,
            label = "Data de nascimento",
            required = true,
            placeholder = "DD/MM/AAAA",
            keyboardType = KeyboardType.Number,
            onValueChange = {
                birthDate = formatDate(it)
                errorMessage = null
            }
        )

        Spacer(modifier = Modifier.height(28.dp))

        SectionTitle(
            title = "Carteira de habilitação",
            description = "Informe os dados da sua CNH."
        )

        Spacer(modifier = Modifier.height(16.dp))

        DriverTextField(
            value = cnhNumber,
            label = "Número da CNH",
            required = true,
            keyboardType = KeyboardType.Number,
            onValueChange = {
                cnhNumber = it.filter(Char::isDigit).take(11)
                errorMessage = null
            }
        )

        FormSpacer()

        ExposedDropdownMenuBox(
            expanded = categoryExpanded,
            onExpandedChange = { categoryExpanded = !categoryExpanded }
        ) {
            OutlinedTextField(
                value = cnhCategory,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                label = { RequiredLabel("Categoria da CNH") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = categoryExpanded
                    )
                },
                colors = driverTextFieldColors()
            )

            ExposedDropdownMenu(
                expanded = categoryExpanded,
                onDismissRequest = { categoryExpanded = false },
                containerColor = GuinchouSurface
            ) {
                cnhCategories.forEach { category ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = category,
                                color = GuinchouWhite
                            )
                        },
                        onClick = {
                            cnhCategory = category
                            categoryExpanded = false
                            errorMessage = null
                        }
                    )
                }
            }
        }

        FormSpacer()

        DriverTextField(
            value = cnhExpiration,
            label = "Validade da CNH",
            required = true,
            placeholder = "DD/MM/AAAA",
            keyboardType = KeyboardType.Number,
            onValueChange = {
                cnhExpiration = formatDate(it)
                errorMessage = null
            }
        )

        Spacer(modifier = Modifier.height(28.dp))

        SectionTitle(
            title = "Endereço",
            description = "Informe seu endereço residencial."
        )

        Spacer(modifier = Modifier.height(16.dp))

        DriverTextField(
            value = cep,
            label = "CEP",
            required = true,
            keyboardType = KeyboardType.Number,
            onValueChange = { newValue ->

                val formattedCep =
                    formatCep(newValue)

                val cepDigits =
                    formattedCep
                        .filter(Char::isDigit)

                cep = formattedCep
                errorMessage = null

                /*
                 * Ao completar os 8 dígitos,
                 * o app consulta automaticamente o ViaCEP.
                 */
                if (
                    cepDigits.length == 8 &&
                    cepDigits != lastSearchedCep
                ) {

                    lastSearchedCep = cepDigits
                    isSearchingCep = true
                    cepLookupMessage =
                        "Buscando endereço..."
                    cepLookupSucceeded = false

                    coroutineScope.launch {

                        val requestedCep =
                            cepDigits

                        val result =
                            fetchAddressByCep(
                                requestedCep
                            )

                        /*
                         * Só aplica a resposta se o usuário
                         * ainda estiver com o mesmo CEP.
                         */
                        if (
                            cep
                                .filter(Char::isDigit) ==
                            requestedCep
                        ) {

                            isSearchingCep = false

                            result.onSuccess {
                                    cepAddress ->

                                if (
                                    cepAddress.street
                                        .isNotBlank()
                                ) {

                                    address =
                                        cepAddress.street
                                }

                                city =
                                    cepAddress.city

                                state =
                                    cepAddress.state

                                cepLookupSucceeded = true

                                cepLookupMessage =
                                    if (
                                        cepAddress.street
                                            .isBlank()
                                    ) {
                                        "CEP encontrado. Preencha o endereço e o número."
                                    } else {
                                        "Endereço encontrado e preenchido automaticamente."
                                    }
                            }

                            result.onFailure {

                                cepLookupSucceeded = false

                                cepLookupMessage =
                                    "Não foi possível localizar este CEP. Você pode preencher o endereço manualmente."
                            }
                        }
                    }

                } else if (
                    cepDigits.length < 8
                ) {

                    lastSearchedCep = ""
                    isSearchingCep = false
                    cepLookupMessage = null
                    cepLookupSucceeded = false
                }
            }
        )

        if (
            isSearchingCep ||
            cepLookupMessage != null
        ) {

            Spacer(
                modifier =
                    Modifier.height(7.dp)
            )

            Text(
                text =
                    cepLookupMessage
                        ?: "Buscando endereço...",
                color =
                    if (
                        cepLookupSucceeded
                    ) {
                        GuinchouGreen
                    } else {
                        GuinchouGray
                    },
                fontSize = 12.sp,
                lineHeight = 17.sp
            )
        }

        FormSpacer()

        DriverTextField(
            value = address,
            label = "Endereço",
            required = true,
            onValueChange = {
                address = it
                errorMessage = null
            }
        )

        FormSpacer()

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(modifier = Modifier.weight(0.38f)) {
                DriverTextField(
                    value = addressNumber,
                    label = "Número",
                    required = true,
                    onValueChange = {
                        addressNumber = it.take(10)
                        errorMessage = null
                    }
                )
            }

            Box(modifier = Modifier.weight(0.62f)) {
                DriverTextField(
                    value = complement,
                    label = "Complemento",
                    required = false,
                    onValueChange = {
                        complement = it
                        errorMessage = null
                    }
                )
            }
        }

        FormSpacer()

        DriverTextField(
            value = city,
            label = "Cidade",
            required = true,
            onValueChange = {
                city = it
                errorMessage = null
            }
        )

        FormSpacer()

        DriverTextField(
            value = state,
            label = "Estado",
            required = true,
            placeholder = "Ex.: DF",
            onValueChange = {
                state = it.uppercase().filter(Char::isLetter).take(2)
                errorMessage = null
            }
        )

        Spacer(modifier = Modifier.height(22.dp))

        ConfirmationCard(
            checked = acceptedInformation,
            onCheckedChange = {
                acceptedInformation = it
                errorMessage = null
            },
            text = "Declaro que as informações fornecidas são verdadeiras."
        )

        ErrorMessage(errorMessage)

        Spacer(modifier = Modifier.height(24.dp))

        PrimaryButton(
            text = "Continuar",
            onClick = {
                val cpfDigits = cpf.filter(Char::isDigit)
                val phoneDigits = phone.filter(Char::isDigit)
                val cepDigits = cep.filter(Char::isDigit)

                errorMessage = when {
                    fullName.trim().length < 3 ->
                        "Informe seu nome completo."

                    cpfDigits.length != 11 ->
                        "Informe um CPF válido com 11 dígitos."

                    phoneDigits.length < 10 ->
                        "Informe um telefone válido."

                    !isValidEmail(email) ->
                        "Informe um e-mail válido."

                    birthDate.length != 10 ->
                        "Informe sua data de nascimento."

                    cnhNumber.length != 11 ->
                        "Informe um número de CNH válido."

                    cnhCategory.isBlank() ->
                        "Selecione a categoria da CNH."

                    cnhExpiration.length != 10 ->
                        "Informe a validade da CNH."

                    cepDigits.length != 8 ->
                        "Informe um CEP válido."

                    address.trim().length < 3 ->
                        "Informe seu endereço."

                    addressNumber.isBlank() ->
                        "Informe o número do endereço."

                    city.trim().length < 2 ->
                        "Informe sua cidade."

                    state.length != 2 ->
                        "Informe a sigla do estado."

                    !acceptedInformation ->
                        "Confirme que as informações são verdadeiras."

                    else -> null
                }

                if (errorMessage == null) {
                    onRegistrationChange(
                        registration.copy(
                            fullName = fullName.trim(),
                            cpf = cpfDigits,
                            phone = phoneDigits,
                            email = email.trim(),
                            birthDate = birthDate,
                            cnhNumber = cnhNumber,
                            cnhCategory = cnhCategory,
                            cnhExpiration = cnhExpiration,
                            cep = cepDigits,
                            address = address.trim(),
                            addressNumber = addressNumber.trim(),
                            complement = complement.trim(),
                            city = city.trim(),
                            state = state
                        )
                    )
                    onContinueClick()
                }
            }
        )

        StepFooter("Próxima etapa: dados do guincho")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TowTruckDataStep(
    registration: DriverRegistrationData,
    onRegistrationChange: (DriverRegistrationData) -> Unit,
    onBackClick: () -> Unit,
    onContinueClick: () -> Unit
) {
    var towTruckType by remember(registration.towTruckType) {
        mutableStateOf(registration.towTruckType)
    }
    var brand by remember(registration.brand) { mutableStateOf(registration.brand) }
    var model by remember(registration.model) { mutableStateOf(registration.model) }
    var year by remember(registration.year) { mutableStateOf(registration.year) }
    var plate by remember(registration.plate) { mutableStateOf(registration.plate) }
    var color by remember(registration.color) { mutableStateOf(registration.color) }
    var renavam by remember(registration.renavam) { mutableStateOf(registration.renavam) }
    var vehicleDocumentExpiration by remember(registration.vehicleDocumentExpiration) {
        mutableStateOf(registration.vehicleDocumentExpiration)
    }
    var typeExpanded by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val towTruckTypes = listOf(
        "Plataforma",
        "Lança / asa-delta",
        "Pesado",
        "Moto",
        "Outro"
    )

    StepContainer(
        step = 2,
        title = "Dados do guincho",
        subtitle = "Veículo de atendimento",
        stepName = "Guincho",
        onBackClick = onBackClick
    ) {
        SectionTitle(
            title = "Informações do guincho",
            description = "Cadastre o veículo que será utilizado nos atendimentos."
        )

        Spacer(modifier = Modifier.height(16.dp))

        ExposedDropdownMenuBox(
            expanded = typeExpanded,
            onExpandedChange = { typeExpanded = !typeExpanded }
        ) {
            OutlinedTextField(
                value = towTruckType,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                label = { RequiredLabel("Tipo de guincho") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = typeExpanded
                    )
                },
                colors = driverTextFieldColors()
            )

            ExposedDropdownMenu(
                expanded = typeExpanded,
                onDismissRequest = { typeExpanded = false },
                containerColor = GuinchouSurface
            ) {
                towTruckTypes.forEach { type ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = type,
                                color = GuinchouWhite
                            )
                        },
                        onClick = {
                            towTruckType = type
                            typeExpanded = false
                            errorMessage = null
                        }
                    )
                }
            }
        }

        FormSpacer()

        DriverTextField(
            value = brand,
            label = "Marca",
            required = true,
            placeholder = "Ex.: Mercedes-Benz",
            onValueChange = {
                brand = it
                errorMessage = null
            }
        )

        FormSpacer()

        DriverTextField(
            value = model,
            label = "Modelo",
            required = true,
            placeholder = "Ex.: Accelo 815",
            onValueChange = {
                model = it
                errorMessage = null
            }
        )

        FormSpacer()

        DriverTextField(
            value = year,
            label = "Ano",
            required = true,
            keyboardType = KeyboardType.Number,
            onValueChange = {
                year = it.filter(Char::isDigit).take(4)
                errorMessage = null
            }
        )

        FormSpacer()

        DriverTextField(
            value = plate,
            label = "Placa",
            required = true,
            placeholder = "ABC1D23",
            onValueChange = {
                plate = formatVehiclePlate(it)
                errorMessage = null
            }
        )

        FormSpacer()

        DriverTextField(
            value = color,
            label = "Cor",
            required = true,
            onValueChange = {
                color = it
                errorMessage = null
            }
        )

        FormSpacer()

        DriverTextField(
            value = renavam,
            label = "RENAVAM",
            required = true,
            keyboardType = KeyboardType.Number,
            onValueChange = {
                renavam = it.filter(Char::isDigit).take(11)
                errorMessage = null
            }
        )

        FormSpacer()

        DriverTextField(
            value = vehicleDocumentExpiration,
            label = "Validade / licenciamento",
            required = true,
            placeholder = "DD/MM/AAAA",
            keyboardType = KeyboardType.Number,
            onValueChange = {
                vehicleDocumentExpiration = formatDate(it)
                errorMessage = null
            }
        )

        Spacer(modifier = Modifier.height(22.dp))

        InfoCard(
            title = "Importante",
            text = "O guincho cadastrado passará por análise antes da liberação para receber chamados."
        )

        ErrorMessage(errorMessage)

        Spacer(modifier = Modifier.height(24.dp))

        PrimaryButton(
            text = "Continuar",
            onClick = {
                errorMessage = when {
                    towTruckType.isBlank() ->
                        "Selecione o tipo de guincho."

                    brand.trim().length < 2 ->
                        "Informe a marca do guincho."

                    model.trim().length < 2 ->
                        "Informe o modelo do guincho."

                    year.length != 4 ->
                        "Informe o ano com 4 dígitos."

                    plate.length != 7 ->
                        "Informe uma placa válida com 7 caracteres."

                    color.trim().length < 2 ->
                        "Informe a cor do veículo."

                    renavam.length != 11 ->
                        "Informe o RENAVAM com 11 dígitos."

                    vehicleDocumentExpiration.length != 10 ->
                        "Informe a validade ou data do licenciamento."

                    else -> null
                }

                if (errorMessage == null) {
                    onRegistrationChange(
                        registration.copy(
                            towTruckType = towTruckType,
                            brand = brand.trim(),
                            model = model.trim(),
                            year = year,
                            plate = plate,
                            color = color.trim(),
                            renavam = renavam,
                            vehicleDocumentExpiration = vehicleDocumentExpiration
                        )
                    )
                    onContinueClick()
                }
            }
        )

        StepFooter("Próxima etapa: documentos")
    }
}

@Composable
private fun DocumentsStep(
    registration: DriverRegistrationData,
    onRegistrationChange: (DriverRegistrationData) -> Unit,
    onBackClick: () -> Unit,
    onContinueClick: () -> Unit
) {
    var cnhDocumentUri by remember(registration.cnhDocumentUri) {
        mutableStateOf(registration.cnhDocumentUri)
    }
    var vehicleDocumentUri by remember(registration.vehicleDocumentUri) {
        mutableStateOf(registration.vehicleDocumentUri)
    }
    var addressProofUri by remember(registration.addressProofUri) {
        mutableStateOf(registration.addressProofUri)
    }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val cnhLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            cnhDocumentUri = uri
            errorMessage = null
        }
    }

    val vehicleDocumentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            vehicleDocumentUri = uri
            errorMessage = null
        }
    }

    val addressProofLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            addressProofUri = uri
        }
    }

    StepContainer(
        step = 3,
        title = "Documentos",
        subtitle = "Validação do parceiro",
        stepName = "Documentos",
        onBackClick = onBackClick
    ) {
        SectionTitle(
            title = "Envio de documentos",
            description = "Selecione os arquivos que serão analisados pela equipe Guinchou."
        )

        Spacer(modifier = Modifier.height(18.dp))

        DocumentUploadCard(
            title = "CNH",
            description = "Documento de habilitação do motorista",
            required = true,
            uri = cnhDocumentUri,
            onSelectClick = {
                cnhLauncher.launch("image/*")
            },
            onRemoveClick = {
                cnhDocumentUri = null
            }
        )

        Spacer(modifier = Modifier.height(14.dp))

        DocumentUploadCard(
            title = "Documento do guincho",
            description = "CRLV ou documento equivalente do veículo",
            required = true,
            uri = vehicleDocumentUri,
            onSelectClick = {
                vehicleDocumentLauncher.launch("image/*")
            },
            onRemoveClick = {
                vehicleDocumentUri = null
            }
        )

        Spacer(modifier = Modifier.height(14.dp))

        DocumentUploadCard(
            title = "Comprovante de endereço",
            description = "Opcional nesta etapa do Front-end",
            required = false,
            uri = addressProofUri,
            onSelectClick = {
                addressProofLauncher.launch("image/*")
            },
            onRemoveClick = {
                addressProofUri = null
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        InfoCard(
            title = "Validade dos documentos",
            text = "Após a integração com o backend, o Guinchou poderá avisar quando CNH e documentos do veículo estiverem próximos do vencimento."
        )

        ErrorMessage(errorMessage)

        Spacer(modifier = Modifier.height(24.dp))

        PrimaryButton(
            text = "Continuar",
            onClick = {
                errorMessage = when {
                    cnhDocumentUri == null ->
                        "Envie uma imagem da CNH."

                    vehicleDocumentUri == null ->
                        "Envie o documento do guincho."

                    else -> null
                }

                if (errorMessage == null) {
                    onRegistrationChange(
                        registration.copy(
                            cnhDocumentUri = cnhDocumentUri,
                            vehicleDocumentUri = vehicleDocumentUri,
                            addressProofUri = addressProofUri
                        )
                    )
                    onContinueClick()
                }
            }
        )

        StepFooter("Próxima etapa: revisar cadastro")
    }
}

@Composable
private fun ReviewStep(
    registration: DriverRegistrationData,
    onBackClick: () -> Unit,
    onEditPersonalClick: () -> Unit,
    onEditVehicleClick: () -> Unit,
    onEditDocumentsClick: () -> Unit,
    onContinueClick: () -> Unit
) {
    var acceptedReview by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    StepContainer(
        step = 4,
        title = "Revisão do cadastro",
        subtitle = "Confira antes de continuar",
        stepName = "Revisão",
        onBackClick = onBackClick
    ) {
        ReviewSectionCard(
            icon = Icons.Default.Person,
            title = "Motorista",
            onEditClick = onEditPersonalClick
        ) {
            ReviewLine("Nome", registration.fullName)
            ReviewLine("CPF", formatCpf(registration.cpf))
            ReviewLine("Telefone", formatPhone(registration.phone))
            ReviewLine("E-mail", registration.email)
            ReviewLine("CNH", registration.cnhNumber)
            ReviewLine("Categoria", registration.cnhCategory)
            ReviewLine("Validade", registration.cnhExpiration)
        }

        Spacer(modifier = Modifier.height(16.dp))

        ReviewSectionCard(
            icon = Icons.Default.DirectionsCar,
            title = "Guincho",
            onEditClick = onEditVehicleClick
        ) {
            ReviewLine("Tipo", registration.towTruckType)
            ReviewLine("Veículo", "${registration.brand} ${registration.model}")
            ReviewLine("Ano", registration.year)
            ReviewLine("Placa", registration.plate)
            ReviewLine("Cor", registration.color)
            ReviewLine("RENAVAM", registration.renavam)
        }

        Spacer(modifier = Modifier.height(16.dp))

        ReviewSectionCard(
            icon = Icons.Default.Description,
            title = "Documentos",
            onEditClick = onEditDocumentsClick
        ) {
            ReviewLine(
                "CNH",
                if (registration.cnhDocumentUri != null) "Selecionada" else "Não enviada"
            )
            ReviewLine(
                "Documento do guincho",
                if (registration.vehicleDocumentUri != null) "Selecionado" else "Não enviado"
            )
            ReviewLine(
                "Comprovante de endereço",
                if (registration.addressProofUri != null) "Selecionado" else "Não enviado"
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        ConfirmationCard(
            checked = acceptedReview,
            onCheckedChange = {
                acceptedReview = it
                errorMessage = null
            },
            text = "Revisei os dados acima e confirmo que estão corretos."
        )

        ErrorMessage(errorMessage)

        Spacer(modifier = Modifier.height(24.dp))

        PrimaryButton(
            text = "Ir para pagamento",
            onClick = {
                if (!acceptedReview) {
                    errorMessage = "Confirme a revisão dos dados para continuar."
                    return@PrimaryButton
                }

                onContinueClick()
            }
        )

        StepFooter("Próxima etapa: taxa de cadastro")
    }
}

@Composable
private fun RegistrationPaymentStep(
    registration: DriverRegistrationData,
    onRegistrationChange: (DriverRegistrationData) -> Unit,
    onBackClick: () -> Unit,
    onPaymentConfirmed: () -> Unit
) {
    var paymentMethod by remember(registration.paymentMethod) {
        mutableStateOf(registration.paymentMethod)
    }
    var paymentAccepted by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    StepContainer(
        step = 5,
        title = "Taxa de cadastro",
        subtitle = "Ativação do parceiro",
        stepName = "Pagamento",
        onBackClick = onBackClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = GuinchouSurface,
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(20.dp)
        ) {
            Text(
                text = "Taxa única de cadastro",
                color = GuinchouGray,
                fontSize = 13.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "R$ 10,00",
                color = GuinchouGreen,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "A taxa corresponde ao cadastro deste guincho na plataforma.",
                color = GuinchouWhite,
                fontSize = 13.sp
            )
        }

        Spacer(modifier = Modifier.height(22.dp))

        SectionTitle(
            title = "Forma de pagamento",
            description = "Escolha uma opção para simular o pagamento no Front-end."
        )

        Spacer(modifier = Modifier.height(14.dp))

        PaymentOption(
            title = "Pix",
            description = "Pagamento instantâneo",
            selected = paymentMethod == "PIX",
            onClick = {
                paymentMethod = "PIX"
                errorMessage = null
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        PaymentOption(
            title = "Cartão",
            description = "Crédito ou débito",
            selected = paymentMethod == "CARD",
            onClick = {
                paymentMethod = "CARD"
                errorMessage = null
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        ConfirmationCard(
            checked = paymentAccepted,
            onCheckedChange = {
                paymentAccepted = it
                errorMessage = null
            },
            text = "Estou ciente da taxa de cadastro de R$ 10,00."
        )

        ErrorMessage(errorMessage)

        Spacer(modifier = Modifier.height(24.dp))

        PrimaryButton(
            text = "Confirmar pagamento",
            onClick = {
                errorMessage = when {
                    paymentMethod.isBlank() ->
                        "Selecione uma forma de pagamento."

                    !paymentAccepted ->
                        "Confirme que está ciente da taxa de cadastro."

                    else -> null
                }

                if (errorMessage == null) {
                    onRegistrationChange(
                        registration.copy(
                            paymentMethod = paymentMethod
                        )
                    )

                    /*
                     * Front-end:
                     * o pagamento é considerado aprovado ao tocar no botão.
                     *
                     * Na integração real, este ponto deve aguardar a
                     * confirmação do gateway de pagamento.
                     */
                    onPaymentConfirmed()
                }
            }
        )

        StepFooter("Próxima etapa: análise do cadastro")
    }
}

@Composable
private fun RegistrationSubmittedStep(
    registration: DriverRegistrationData,
    onFinishClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GuinchouBackground)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        RegistrationProgressCard(
            currentStep = 6,
            stepName = "Análise"
        )

        Spacer(modifier = Modifier.height(48.dp))

        Box(
            modifier = Modifier
                .size(92.dp)
                .background(
                    color = GuinchouGreen.copy(alpha = 0.14f),
                    shape = CircleShape
                )
                .border(
                    width = 2.dp,
                    color = GuinchouGreen,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = GuinchouGreen,
                modifier = Modifier.size(46.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Cadastro enviado!",
            color = GuinchouWhite,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Recebemos o cadastro de ${registration.fullName}. Agora os dados e documentos ficarão aguardando análise.",
            color = GuinchouGray,
            fontSize = 14.sp,
            lineHeight = 21.sp
        )

        Spacer(modifier = Modifier.height(28.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = GuinchouSurface,
                    shape = RoundedCornerShape(18.dp)
                )
                .padding(18.dp)
        ) {
            StatusRow(
                title = "Dados pessoais",
                status = "Enviado"
            )

            StatusDivider()

            StatusRow(
                title = "Guincho",
                status = registration.plate.ifBlank { "Enviado" }
            )

            StatusDivider()

            StatusRow(
                title = "Documentos",
                status = "Aguardando análise"
            )

            StatusDivider()

            StatusRow(
                title = "Taxa de cadastro",
                status = "R$ 10,00"
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        InfoCard(
            title = "Próximo passo",
            text = "Quando o backend for integrado, esta tela será atualizada automaticamente após a aprovação ou caso algum documento precise ser reenviado."
        )

        Spacer(modifier = Modifier.height(28.dp))

        PrimaryButton(
            text = "Concluir",
            onClick = onFinishClick
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun StepContainer(
    step: Int,
    title: String,
    subtitle: String,
    stepName: String,
    onBackClick: () -> Unit,
    content: @Composable androidx.compose.foundation.layout.ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GuinchouBackground)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "‹",
                color = GuinchouWhite,
                fontSize = 38.sp,
                modifier = Modifier
                    .clickable { onBackClick() }
                    .padding(end = 12.dp)
            )

            Column {
                Text(
                    text = title,
                    color = GuinchouWhite,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = subtitle,
                    color = GuinchouGreen,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        RegistrationProgressCard(
            currentStep = step,
            stepName = stepName
        )

        Spacer(modifier = Modifier.height(26.dp))

        content()

        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
private fun RegistrationProgressCard(
    currentStep: Int,
    stepName: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = GuinchouSurface,
                shape = RoundedCornerShape(18.dp)
            )
            .padding(18.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Etapa $currentStep de $REGISTRATION_STEPS",
                color = GuinchouGreen,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = stepName,
                color = GuinchouGray,
                fontSize = 12.sp
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            repeat(REGISTRATION_STEPS) { index ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(5.dp)
                        .background(
                            color = if (index < currentStep) {
                                GuinchouGreen
                            } else {
                                GuinchouBorder
                            },
                            shape = RoundedCornerShape(50)
                        )
                )
            }
        }
    }
}

@Composable
private fun SectionTitle(
    title: String,
    description: String
) {
    Text(
        text = title,
        color = GuinchouWhite,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold
    )

    Spacer(modifier = Modifier.height(4.dp))

    Text(
        text = description,
        color = GuinchouGray,
        fontSize = 13.sp
    )
}

@Composable
private fun DriverTextField(
    value: String,
    label: String,
    required: Boolean,
    placeholder: String = "",
    keyboardType: KeyboardType = KeyboardType.Text,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        label = {
            if (required) {
                RequiredLabel(label)
            } else {
                Text(text = label)
            }
        },
        placeholder = {
            if (placeholder.isNotBlank()) {
                Text(
                    text = placeholder,
                    color = GuinchouGray
                )
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        ),
        colors = driverTextFieldColors()
    )
}

@Composable
private fun RequiredLabel(
    text: String
) {
    Text(
        text = buildAnnotatedString {
            append(text)

            withStyle(
                style = SpanStyle(
                    color = Color(0xFFFF5C5C)
                )
            ) {
                append(" *")
            }
        }
    )
}

@Composable
private fun driverTextFieldColors() =
    OutlinedTextFieldDefaults.colors(
        focusedTextColor = GuinchouWhite,
        unfocusedTextColor = GuinchouWhite,
        focusedBorderColor = GuinchouGreen,
        unfocusedBorderColor = GuinchouBorder,
        focusedLabelColor = GuinchouGreen,
        unfocusedLabelColor = GuinchouGray,
        cursorColor = GuinchouGreen,
        focusedContainerColor = GuinchouSurface,
        unfocusedContainerColor = GuinchouSurface
    )

@Composable
private fun DocumentUploadCard(
    title: String,
    description: String,
    required: Boolean,
    uri: Uri?,
    onSelectClick: () -> Unit,
    onRemoveClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = GuinchouSurface,
                shape = RoundedCornerShape(18.dp)
            )
            .border(
                width = 1.dp,
                color = if (uri != null) GuinchouGreen else GuinchouBorder,
                shape = RoundedCornerShape(18.dp)
            )
            .padding(18.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        color = GuinchouGreen.copy(alpha = 0.10f),
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (uri != null) {
                        Icons.Default.Check
                    } else {
                        Icons.Default.UploadFile
                    },
                    contentDescription = null,
                    tint = GuinchouGreen
                )
            }

            Spacer(modifier = Modifier.size(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = buildAnnotatedString {
                        append(title)

                        if (required) {
                            withStyle(
                                SpanStyle(
                                    color = Color(0xFFFF5C5C)
                                )
                            ) {
                                append(" *")
                            }
                        }
                    },
                    color = GuinchouWhite,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = if (uri != null) {
                        "Arquivo selecionado"
                    } else {
                        description
                    },
                    color = if (uri != null) {
                        GuinchouGreen
                    } else {
                        GuinchouGray
                    },
                    fontSize = 12.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (uri == null) {
            OutlinedButton(
                onClick = onSelectClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = GuinchouGreen
                )
            ) {
                Text(
                    text = "Selecionar arquivo",
                    fontWeight = FontWeight.Bold
                )
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onSelectClick,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = GuinchouGreen
                    )
                ) {
                    Text("Trocar")
                }

                OutlinedButton(
                    onClick = onRemoveClick,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFFFF6B6B)
                    )
                ) {
                    Text("Remover")
                }
            }
        }
    }
}

@Composable
private fun ReviewSectionCard(
    icon: ImageVector,
    title: String,
    onEditClick: () -> Unit,
    content: @Composable androidx.compose.foundation.layout.ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = GuinchouSurface,
                shape = RoundedCornerShape(18.dp)
            )
            .padding(18.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = GuinchouGreen
            )

            Spacer(modifier = Modifier.size(10.dp))

            Text(
                text = title,
                color = GuinchouWhite,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )

            Text(
                text = "Editar",
                color = GuinchouGreen,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable {
                    onEditClick()
                }
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        content()
    }
}

@Composable
private fun ReviewLine(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            color = GuinchouGray,
            fontSize = 13.sp,
            modifier = Modifier.weight(0.42f)
        )

        Text(
            text = value.ifBlank { "-" },
            color = GuinchouWhite,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.weight(0.58f)
        )
    }
}

@Composable
private fun PaymentOption(
    title: String,
    description: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = GuinchouSurface,
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                width = 1.dp,
                color = if (selected) GuinchouGreen else GuinchouBorder,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = GuinchouGreen,
                unselectedColor = GuinchouGray
            )
        )

        Spacer(modifier = Modifier.size(8.dp))

        Column {
            Text(
                text = title,
                color = GuinchouWhite,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = description,
                color = GuinchouGray,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun ConfirmationCard(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    text: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = GuinchouSurface,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = GuinchouGreen,
                uncheckedColor = GuinchouGray,
                checkmarkColor = GuinchouBackground
            )
        )

        Text(
            text = text,
            color = GuinchouWhite,
            fontSize = 13.sp,
            lineHeight = 18.sp,
            modifier = Modifier
                .padding(top = 10.dp)
                .weight(1f)
        )
    }
}

@Composable
private fun InfoCard(
    title: String,
    text: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = GuinchouGreen.copy(alpha = 0.07f),
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                width = 1.dp,
                color = GuinchouGreen.copy(alpha = 0.25f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
    ) {
        Text(
            text = title,
            color = GuinchouGreen,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(5.dp))

        Text(
            text = text,
            color = GuinchouWhite,
            fontSize = 12.sp,
            lineHeight = 18.sp
        )
    }
}

@Composable
private fun ErrorMessage(
    errorMessage: String?
) {
    if (errorMessage != null) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = errorMessage,
            color = Color(0xFFFF5C5C),
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
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
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = GuinchouGreen,
            contentColor = GuinchouBackground
        )
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
        )
    }
}

@Composable
private fun StepFooter(
    text: String
) {
    Spacer(modifier = Modifier.height(10.dp))

    Text(
        text = text,
        color = GuinchouGray,
        fontSize = 12.sp,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun FormSpacer() {
    Spacer(modifier = Modifier.height(14.dp))
}

@Composable
private fun StatusRow(
    title: String,
    status: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            color = GuinchouGray,
            fontSize = 13.sp
        )

        Text(
            text = status,
            color = GuinchouWhite,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun StatusDivider() {
    Spacer(modifier = Modifier.height(12.dp))

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(GuinchouBorder)
    )

    Spacer(modifier = Modifier.height(12.dp))
}

private suspend fun fetchAddressByCep(
    cep: String
): Result<CepAddressResult> {

    return withContext(
        Dispatchers.IO
    ) {

        runCatching {

            val cleanCep =
                cep
                    .filter(Char::isDigit)
                    .take(8)

            require(
                cleanCep.length == 8
            ) {
                "CEP inválido."
            }

            val url =
                URL(
                    "https://viacep.com.br/ws/$cleanCep/json/"
                )

            val connection =
                url.openConnection()
                        as HttpURLConnection

            try {

                connection.requestMethod =
                    "GET"

                connection.connectTimeout =
                    6000

                connection.readTimeout =
                    6000

                connection.setRequestProperty(
                    "Accept",
                    "application/json"
                )

                val responseCode =
                    connection.responseCode

                if (
                    responseCode !in 200..299
                ) {
                    error(
                        "Falha ao consultar CEP."
                    )
                }

                val responseText =
                    connection
                        .inputStream
                        .bufferedReader()
                        .use {
                            it.readText()
                        }

                val json =
                    JSONObject(
                        responseText
                    )

                if (
                    json.optBoolean(
                        "erro",
                        false
                    )
                ) {
                    error(
                        "CEP não encontrado."
                    )
                }

                CepAddressResult(
                    street =
                        json.optString(
                            "logradouro"
                        ),
                    neighborhood =
                        json.optString(
                            "bairro"
                        ),
                    city =
                        json.optString(
                            "localidade"
                        ),
                    state =
                        json.optString(
                            "uf"
                        )
                            .uppercase()
                )
            } finally {

                connection.disconnect()
            }
        }
    }
}


private fun formatCpf(
    value: String
): String {
    val digits = value
        .filter(Char::isDigit)
        .take(11)

    return buildString {
        digits.forEachIndexed { index, char ->
            append(char)

            if ((index == 2 || index == 5) && index != digits.lastIndex) {
                append(".")
            }

            if (index == 8 && index != digits.lastIndex) {
                append("-")
            }
        }
    }
}

private fun formatPhone(
    value: String
): String {
    val digits = value
        .filter(Char::isDigit)
        .take(11)

    if (digits.isEmpty()) {
        return ""
    }

    return when {
        digits.length <= 2 ->
            "(${digits}"

        digits.length <= 7 ->
            "(${digits.take(2)}) ${digits.drop(2)}"

        digits.length <= 10 ->
            "(${digits.take(2)}) ${digits.substring(2, 6)}-${digits.drop(6)}"

        else ->
            "(${digits.take(2)}) ${digits.substring(2, 7)}-${digits.drop(7)}"
    }
}

private fun formatCep(
    value: String
): String {
    val digits = value
        .filter(Char::isDigit)
        .take(8)

    return if (digits.length <= 5) {
        digits
    } else {
        "${digits.take(5)}-${digits.drop(5)}"
    }
}

private fun formatDate(
    value: String
): String {
    val digits = value
        .filter(Char::isDigit)
        .take(8)

    return when {
        digits.length <= 2 ->
            digits

        digits.length <= 4 ->
            "${digits.take(2)}/${digits.drop(2)}"

        else ->
            "${digits.take(2)}/${digits.substring(2, 4)}/${digits.drop(4)}"
    }
}

private fun formatVehiclePlate(
    value: String
): String {
    return value
        .uppercase()
        .filter(Char::isLetterOrDigit)
        .take(7)
}

private fun isValidEmail(
    email: String
): Boolean {
    val value = email.trim()

    if (value.isBlank()) {
        return false
    }

    val parts = value.split("@")

    return parts.size == 2 &&
            parts[0].isNotBlank() &&
            parts[1].contains(".") &&
            !parts[1].startsWith(".") &&
            !parts[1].endsWith(".")
}
