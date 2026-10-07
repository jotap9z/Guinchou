package com.guinchou.app.ui.screens.partner

import android.util.Patterns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.guinchou.app.ui.theme.GuinchouBackground
import com.guinchou.app.ui.theme.GuinchouBorder
import com.guinchou.app.ui.theme.GuinchouError
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

private enum class CompanyRegistrationStep(
    val title: String,
    val subtitle: String
) {
    COMPANY(
        "Dados da empresa",
        "Informe os dados principais da empresa de guinchos."
    ),

    OWNER(
        "Dono da empresa",
        "Agora precisamos dos dados do proprietário ou responsável legal."
    ),

    ADDRESS(
        "Endereço",
        "Informe o endereço principal da empresa."
    ),

    OPERATION(
        "Operação",
        "Defina a região e os tipos de atendimento oferecidos."
    ),

    DOCUMENTS(
        "Documentos",
        "Envie apenas os documentos da empresa e do responsável legal."
    ),

    REVIEW(
        "Revisão",
        "Confira os dados do cadastro antes de enviar para análise."
    )
}

private data class CepAddress(
    val street: String,
    val neighborhood: String,
    val city: String,
    val state: String
)

@Composable
fun CompanyRegistrationScreen(
    onBackClick: () -> Unit = {},
    onRegistrationFinished: () -> Unit = {}
) {
    val scope = rememberCoroutineScope()
    val steps = CompanyRegistrationStep.entries

    var currentStep by remember {
        mutableIntStateOf(0)
    }

    val step = steps[currentStep]

    // EMPRESA

    var cnpj by remember {
        mutableStateOf("")
    }

    var corporateName by remember {
        mutableStateOf("")
    }

    var tradeName by remember {
        mutableStateOf("")
    }

    var companyPhone by remember {
        mutableStateOf("")
    }

    var companyEmail by remember {
        mutableStateOf("")
    }

    // DONO / RESPONSÁVEL

    var ownerName by remember {
        mutableStateOf("")
    }

    var ownerCpf by remember {
        mutableStateOf("")
    }

    var ownerPhone by remember {
        mutableStateOf("")
    }

    var ownerRole by remember {
        mutableStateOf("")
    }

    // ENDEREÇO

    var cep by remember {
        mutableStateOf("")
    }

    var street by remember {
        mutableStateOf("")
    }

    var number by remember {
        mutableStateOf("")
    }

    var complement by remember {
        mutableStateOf("")
    }

    var neighborhood by remember {
        mutableStateOf("")
    }

    var city by remember {
        mutableStateOf("")
    }

    var state by remember {
        mutableStateOf("")
    }

    var isSearchingCep by remember {
        mutableStateOf(false)
    }

    var lastSearchedCep by remember {
        mutableStateOf("")
    }

    // OPERAÇÃO

    var serviceRegion by remember {
        mutableStateOf("")
    }

    var twentyFourHours by remember {
        mutableStateOf(true)
    }

    var mechanicalService by remember {
        mutableStateOf(true)
    }

    var electricalService by remember {
        mutableStateOf(false)
    }

    var accidentService by remember {
        mutableStateOf(true)
    }

    var transportService by remember {
        mutableStateOf(true)
    }

    var heavyVehicleService by remember {
        mutableStateOf(false)
    }

    // DOCUMENTOS

    var companyDocument by remember {
        mutableStateOf<String?>(null)
    }

    var ownerDocument by remember {
        mutableStateOf<String?>(null)
    }

    var ownerAddressProof by remember {
        mutableStateOf<String?>(null)
    }

    var documentTarget by remember {
        mutableStateOf<String?>(null)
    }

    // ERROS

    var cnpjError by remember {
        mutableStateOf<String?>(null)
    }

    var corporateNameError by remember {
        mutableStateOf<String?>(null)
    }

    var tradeNameError by remember {
        mutableStateOf<String?>(null)
    }

    var companyPhoneError by remember {
        mutableStateOf<String?>(null)
    }

    var companyEmailError by remember {
        mutableStateOf<String?>(null)
    }

    var ownerNameError by remember {
        mutableStateOf<String?>(null)
    }

    var ownerCpfError by remember {
        mutableStateOf<String?>(null)
    }

    var ownerPhoneError by remember {
        mutableStateOf<String?>(null)
    }

    var ownerRoleError by remember {
        mutableStateOf<String?>(null)
    }

    var cepError by remember {
        mutableStateOf<String?>(null)
    }

    var streetError by remember {
        mutableStateOf<String?>(null)
    }

    var numberError by remember {
        mutableStateOf<String?>(null)
    }

    var neighborhoodError by remember {
        mutableStateOf<String?>(null)
    }

    var cityError by remember {
        mutableStateOf<String?>(null)
    }

    var stateError by remember {
        mutableStateOf<String?>(null)
    }

    var serviceRegionError by remember {
        mutableStateOf<String?>(null)
    }

    var servicesError by remember {
        mutableStateOf<String?>(null)
    }

    var documentsError by remember {
        mutableStateOf<String?>(null)
    }

    var showSuccessDialog by remember {
        mutableStateOf(false)
    }

    val documentLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.GetContent()
        ) { uri ->

            if (uri != null) {
                when (documentTarget) {
                    "COMPANY" -> {
                        companyDocument = uri.toString()
                    }

                    "OWNER" -> {
                        ownerDocument = uri.toString()
                    }

                    "OWNER_ADDRESS" -> {
                        ownerAddressProof = uri.toString()
                    }
                }
            }

            documentTarget = null
        }

    fun searchCep(
        cepDigits: String
    ) {
        if (
            cepDigits.length != 8 ||
            cepDigits == lastSearchedCep
        ) {
            return
        }

        lastSearchedCep = cepDigits
        cepError = null
        isSearchingCep = true

        scope.launch {

            val result =
                withContext(
                    Dispatchers.IO
                ) {
                    fetchCepAddress(
                        cepDigits
                    )
                }

            isSearchingCep = false

            if (result != null) {

                street =
                    result.street

                neighborhood =
                    result.neighborhood

                city =
                    result.city

                state =
                    result.state

                cepError = null

            } else {

                cepError =
                    "CEP não encontrado. Verifique o número informado."
            }
        }
    }

    fun validateCompany(): Boolean {

        cnpjError =
            when {
                cnpj.isBlank() ->
                    "Informe o CNPJ."

                !isValidCnpj(cnpj) ->
                    "CNPJ inválido."

                else ->
                    null
            }

        corporateNameError =
            if (
                corporateName.trim()
                    .length < 3
            ) {
                "Informe uma razão social válida."
            } else {
                null
            }

        tradeNameError =
            if (
                tradeName.trim()
                    .length < 2
            ) {
                "Informe o nome fantasia."
            } else {
                null
            }

        companyPhoneError =
            if (
                !isValidPhone(
                    companyPhone
                )
            ) {
                "Informe um telefone com DDD válido."
            } else {
                null
            }

        companyEmailError =
            if (
                !isValidEmail(
                    companyEmail
                )
            ) {
                "Informe um e-mail válido."
            } else {
                null
            }

        return listOf(
            cnpjError,
            corporateNameError,
            tradeNameError,
            companyPhoneError,
            companyEmailError
        ).all {
            it == null
        }
    }

    fun validateOwner(): Boolean {

        ownerNameError =
            if (
                ownerName.trim()
                    .length < 3
            ) {
                "Informe o nome completo do responsável."
            } else {
                null
            }

        ownerCpfError =
            when {
                ownerCpf.isBlank() ->
                    "Informe o CPF."

                !isValidCpf(ownerCpf) ->
                    "CPF inválido."

                else ->
                    null
            }

        ownerPhoneError =
            if (
                !isValidPhone(
                    ownerPhone
                )
            ) {
                "Informe um telefone com DDD válido."
            } else {
                null
            }

        ownerRoleError =
            if (
                ownerRole.trim()
                    .length < 2
            ) {
                "Informe o cargo ou função."
            } else {
                null
            }

        return listOf(
            ownerNameError,
            ownerCpfError,
            ownerPhoneError,
            ownerRoleError
        ).all {
            it == null
        }
    }

    fun validateAddress(): Boolean {

        cepError =
            when {
                onlyDigits(cep).length != 8 ->
                    "Informe um CEP válido com 8 números."

                cepError != null ->
                    cepError

                else ->
                    null
            }

        streetError =
            if (street.isBlank()) {
                "Informe o endereço."
            } else {
                null
            }

        numberError =
            if (number.isBlank()) {
                "Informe o número."
            } else {
                null
            }

        neighborhoodError =
            if (neighborhood.isBlank()) {
                "Informe o bairro."
            } else {
                null
            }

        cityError =
            if (city.isBlank()) {
                "Informe a cidade."
            } else {
                null
            }

        stateError =
            if (!isValidUf(state)) {
                "Informe uma UF válida."
            } else {
                null
            }

        return listOf(
            cepError,
            streetError,
            numberError,
            neighborhoodError,
            cityError,
            stateError
        ).all {
            it == null
        }
    }

    fun validateOperation(): Boolean {

        serviceRegionError =
            if (
                serviceRegion.trim()
                    .length < 2
            ) {
                "Informe a região de atendimento."
            } else {
                null
            }

        servicesError =
            if (
                !mechanicalService &&
                !electricalService &&
                !accidentService &&
                !transportService &&
                !heavyVehicleService
            ) {
                "Selecione pelo menos um serviço oferecido."
            } else {
                null
            }

        return serviceRegionError == null &&
                servicesError == null
    }

    fun validateDocuments(): Boolean {

        documentsError =
            if (
                companyDocument == null ||
                ownerDocument == null ||
                ownerAddressProof == null
            ) {
                "Adicione todos os documentos obrigatórios."
            } else {
                null
            }

        return documentsError == null
    }

    fun validateCurrentStep(): Boolean {

        return when (step) {

            CompanyRegistrationStep.COMPANY ->
                validateCompany()

            CompanyRegistrationStep.OWNER ->
                validateOwner()

            CompanyRegistrationStep.ADDRESS ->
                validateAddress()

            CompanyRegistrationStep.OPERATION ->
                validateOperation()

            CompanyRegistrationStep.DOCUMENTS ->
                validateDocuments()

            CompanyRegistrationStep.REVIEW ->
                true
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                GuinchouBackground
            )
    ) {

        Column(
            modifier =
                Modifier.fillMaxSize()
        ) {

            RegistrationHeader(
                currentStep =
                    currentStep + 1,
                totalSteps =
                    steps.size,
                onBackClick = {

                    if (
                        currentStep > 0
                    ) {
                        currentStep--
                    } else {
                        onBackClick()
                    }
                }
            )

            RegistrationProgress(
                currentStep =
                    currentStep,
                totalSteps =
                    steps.size
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(
                        rememberScrollState()
                    )
                    .padding(
                        horizontal =
                            20.dp,
                        vertical =
                            18.dp
                    )
            ) {

                Text(
                    text =
                        step.title,
                    color =
                        GuinchouWhite,
                    fontSize =
                        25.sp,
                    fontWeight =
                        FontWeight.Bold
                )

                Spacer(
                    Modifier.height(
                        6.dp
                    )
                )

                Text(
                    text =
                        step.subtitle,
                    color =
                        GuinchouGray,
                    fontSize =
                        14.sp,
                    lineHeight =
                        20.sp
                )

                Spacer(
                    Modifier.height(
                        24.dp
                    )
                )

                when (step) {

                    CompanyRegistrationStep.COMPANY -> {

                        CompanyTextField(
                            value = cnpj,
                            onValueChange = {
                                cnpj =
                                    formatCnpj(
                                        it
                                    )

                                cnpjError =
                                    null
                            },
                            label =
                                "CNPJ",
                            required =
                                true,
                            placeholder =
                                "00.000.000/0000-00",
                            keyboardType =
                                KeyboardType.Number,
                            error =
                                cnpjError
                        )

                        CompanyTextField(
                            value =
                                corporateName,
                            onValueChange = {
                                corporateName =
                                    it

                                corporateNameError =
                                    null
                            },
                            label =
                                "Razão social",
                            required =
                                true,
                            placeholder =
                                "Nome registrado da empresa",
                            error =
                                corporateNameError
                        )

                        CompanyTextField(
                            value =
                                tradeName,
                            onValueChange = {
                                tradeName =
                                    it

                                tradeNameError =
                                    null
                            },
                            label =
                                "Nome fantasia",
                            required =
                                true,
                            placeholder =
                                "Nome comercial",
                            error =
                                tradeNameError
                        )

                        CompanyTextField(
                            value =
                                companyPhone,
                            onValueChange = {
                                companyPhone =
                                    formatPhone(
                                        it
                                    )

                                companyPhoneError =
                                    null
                            },
                            label =
                                "Telefone",
                            required =
                                true,
                            placeholder =
                                "(61) 99999-9999",
                            keyboardType =
                                KeyboardType.Phone,
                            error =
                                companyPhoneError
                        )

                        CompanyTextField(
                            value =
                                companyEmail,
                            onValueChange = {
                                companyEmail =
                                    it.trim()

                                companyEmailError =
                                    null
                            },
                            label =
                                "E-mail",
                            required =
                                true,
                            placeholder =
                                "empresa@email.com",
                            keyboardType =
                                KeyboardType.Email,
                            error =
                                companyEmailError
                        )
                    }

                    CompanyRegistrationStep.OWNER -> {

                        InfoCard(
                            "Este cadastro identifica o proprietário ou responsável legal. " +
                                    "Motoristas da empresa serão adicionados depois pela área de gestão, inclusive por QR Code."
                        )

                        Spacer(
                            Modifier.height(
                                18.dp
                            )
                        )

                        CompanyTextField(
                            value =
                                ownerName,
                            onValueChange = {
                                ownerName =
                                    it

                                ownerNameError =
                                    null
                            },
                            label =
                                "Nome completo",
                            required =
                                true,
                            placeholder =
                                "Nome do proprietário",
                            error =
                                ownerNameError
                        )

                        CompanyTextField(
                            value =
                                ownerCpf,
                            onValueChange = {
                                ownerCpf =
                                    formatCpf(
                                        it
                                    )

                                ownerCpfError =
                                    null
                            },
                            label =
                                "CPF",
                            required =
                                true,
                            placeholder =
                                "000.000.000-00",
                            keyboardType =
                                KeyboardType.Number,
                            error =
                                ownerCpfError
                        )

                        CompanyTextField(
                            value =
                                ownerPhone,
                            onValueChange = {
                                ownerPhone =
                                    formatPhone(
                                        it
                                    )

                                ownerPhoneError =
                                    null
                            },
                            label =
                                "Telefone",
                            required =
                                true,
                            placeholder =
                                "(61) 99999-9999",
                            keyboardType =
                                KeyboardType.Phone,
                            error =
                                ownerPhoneError
                        )

                        CompanyTextField(
                            value =
                                ownerRole,
                            onValueChange = {
                                ownerRole =
                                    it

                                ownerRoleError =
                                    null
                            },
                            label =
                                "Cargo / função",
                            required =
                                true,
                            placeholder =
                                "Ex.: Proprietário",
                            error =
                                ownerRoleError
                        )
                    }

                    CompanyRegistrationStep.ADDRESS -> {

                        CompanyTextField(
                            value = cep,
                            onValueChange = {

                                cep =
                                    formatCep(
                                        it
                                    )

                                cepError =
                                    null

                                val digits =
                                    onlyDigits(
                                        cep
                                    )

                                if (
                                    digits.length < 8
                                ) {
                                    lastSearchedCep =
                                        ""
                                }

                                if (
                                    digits.length == 8
                                ) {
                                    searchCep(
                                        digits
                                    )
                                }
                            },
                            label =
                                "CEP",
                            required =
                                true,
                            placeholder =
                                "00000-000",
                            keyboardType =
                                KeyboardType.Number,
                            error =
                                cepError
                        )

                        if (
                            isSearchingCep
                        ) {

                            Row(
                                verticalAlignment =
                                    Alignment.CenterVertically
                            ) {

                                CircularProgressIndicator(
                                    modifier =
                                        Modifier.size(
                                            18.dp
                                        ),
                                    strokeWidth =
                                        2.dp,
                                    color =
                                        GuinchouGreen
                                )

                                Spacer(
                                    Modifier.width(
                                        8.dp
                                    )
                                )

                                Text(
                                    text =
                                        "Buscando endereço...",
                                    color =
                                        GuinchouGreen,
                                    fontSize =
                                        12.sp
                                )
                            }

                            Spacer(
                                Modifier.height(
                                    14.dp
                                )
                            )
                        }

                        CompanyTextField(
                            value =
                                street,
                            onValueChange = {
                                street =
                                    it

                                streetError =
                                    null
                            },
                            label =
                                "Endereço",
                            required =
                                true,
                            placeholder =
                                "Rua, avenida...",
                            error =
                                streetError
                        )

                        CompanyTextField(
                            value =
                                number,
                            onValueChange = {
                                number =
                                    it

                                numberError =
                                    null
                            },
                            label =
                                "Número",
                            required =
                                true,
                            placeholder =
                                "123",
                            error =
                                numberError
                        )

                        CompanyTextField(
                            value =
                                complement,
                            onValueChange = {
                                complement =
                                    it
                            },
                            label =
                                "Complemento",
                            required =
                                false,
                            placeholder =
                                "Sala, lote, bloco..."
                        )

                        CompanyTextField(
                            value =
                                neighborhood,
                            onValueChange = {
                                neighborhood =
                                    it

                                neighborhoodError =
                                    null
                            },
                            label =
                                "Bairro",
                            required =
                                true,
                            placeholder =
                                "Bairro",
                            error =
                                neighborhoodError
                        )

                        CompanyTextField(
                            value =
                                city,
                            onValueChange = {
                                city =
                                    it

                                cityError =
                                    null
                            },
                            label =
                                "Cidade",
                            required =
                                true,
                            placeholder =
                                "Cidade",
                            error =
                                cityError
                        )

                        CompanyTextField(
                            value =
                                state,
                            onValueChange = {

                                state =
                                    it.uppercase()
                                        .filter(
                                            Char::isLetter
                                        )
                                        .take(
                                            2
                                        )

                                stateError =
                                    null
                            },
                            label =
                                "UF",
                            required =
                                true,
                            placeholder =
                                "DF",
                            error =
                                stateError
                        )
                    }

                    CompanyRegistrationStep.OPERATION -> {

                        CompanyTextField(
                            value =
                                serviceRegion,
                            onValueChange = {
                                serviceRegion =
                                    it

                                serviceRegionError =
                                    null
                            },
                            label =
                                "Região de atendimento",
                            required =
                                true,
                            placeholder =
                                "Ex.: Distrito Federal",
                            error =
                                serviceRegionError
                        )

                        SettingSwitchCard(
                            title =
                                "Atendimento 24 horas",
                            description =
                                "Permite receber chamados durante todo o dia.",
                            checked =
                                twentyFourHours,
                            onCheckedChange = {
                                twentyFourHours =
                                    it
                            }
                        )

                        Spacer(
                            Modifier.height(
                                22.dp
                            )
                        )

                        RequiredSectionLabel(
                            "Serviços oferecidos"
                        )

                        ServiceCheckCard(
                            "Pane mecânica",
                            mechanicalService
                        ) {
                            mechanicalService =
                                it

                            servicesError =
                                null
                        }

                        ServiceCheckCard(
                            "Pane elétrica",
                            electricalService
                        ) {
                            electricalService =
                                it

                            servicesError =
                                null
                        }

                        ServiceCheckCard(
                            "Acidente / colisão",
                            accidentService
                        ) {
                            accidentService =
                                it

                            servicesError =
                                null
                        }

                        ServiceCheckCard(
                            "Transporte de veículo",
                            transportService
                        ) {
                            transportService =
                                it

                            servicesError =
                                null
                        }

                        ServiceCheckCard(
                            "Veículo pesado",
                            heavyVehicleService
                        ) {
                            heavyVehicleService =
                                it

                            servicesError =
                                null
                        }

                        servicesError?.let {
                            ErrorText(
                                it
                            )
                        }
                    }

                    CompanyRegistrationStep.DOCUMENTS -> {

                        InfoCard(
                            "Nesta etapa entram somente documentos da empresa e do dono/responsável. " +
                                    "Documentos de motoristas e guinchos serão enviados pelos próprios cadastros vinculados à empresa."
                        )

                        Spacer(
                            Modifier.height(
                                18.dp
                            )
                        )

                        DocumentUploadCard(
                            title =
                                "Cartão CNPJ / comprovante da empresa",
                            required =
                                true,
                            description =
                                "Documento oficial da empresa",
                            selected =
                                companyDocument != null,
                            onClick = {

                                documentTarget =
                                    "COMPANY"

                                documentLauncher.launch(
                                    "*/*"
                                )
                            }
                        )

                        DocumentUploadCard(
                            title =
                                "Documento do proprietário",
                            required =
                                true,
                            description =
                                "RG, CNH ou documento oficial",
                            selected =
                                ownerDocument != null,
                            onClick = {

                                documentTarget =
                                    "OWNER"

                                documentLauncher.launch(
                                    "*/*"
                                )
                            }
                        )

                        DocumentUploadCard(
                            title =
                                "Comprovante de endereço do proprietário",
                            required =
                                true,
                            description =
                                "Documento recente",
                            selected =
                                ownerAddressProof != null,
                            onClick = {

                                documentTarget =
                                    "OWNER_ADDRESS"

                                documentLauncher.launch(
                                    "*/*"
                                )
                            }
                        )

                        documentsError?.let {
                            ErrorText(
                                it
                            )
                        }
                    }

                    CompanyRegistrationStep.REVIEW -> {

                        ReviewSection(
                            title =
                                "Empresa",
                            items =
                                listOf(
                                    "CNPJ" to
                                            cnpj,

                                    "Razão social" to
                                            corporateName,

                                    "Nome fantasia" to
                                            tradeName,

                                    "Telefone" to
                                            companyPhone,

                                    "E-mail" to
                                            companyEmail
                                )
                        )

                        ReviewSection(
                            title =
                                "Dono / responsável",
                            items =
                                listOf(
                                    "Nome" to
                                            ownerName,

                                    "CPF" to
                                            ownerCpf,

                                    "Telefone" to
                                            ownerPhone,

                                    "Cargo" to
                                            ownerRole
                                )
                        )

                        ReviewSection(
                            title =
                                "Endereço",
                            items =
                                listOf(
                                    "CEP" to
                                            cep,

                                    "Endereço" to
                                            "$street, $number",

                                    "Complemento" to
                                            complement.ifBlank {
                                                "Não informado"
                                            },

                                    "Bairro" to
                                            neighborhood,

                                    "Cidade / UF" to
                                            "$city - $state"
                                )
                        )

                        val services =
                            buildList {

                                if (
                                    mechanicalService
                                ) {
                                    add(
                                        "Pane mecânica"
                                    )
                                }

                                if (
                                    electricalService
                                ) {
                                    add(
                                        "Pane elétrica"
                                    )
                                }

                                if (
                                    accidentService
                                ) {
                                    add(
                                        "Acidente / colisão"
                                    )
                                }

                                if (
                                    transportService
                                ) {
                                    add(
                                        "Transporte de veículo"
                                    )
                                }

                                if (
                                    heavyVehicleService
                                ) {
                                    add(
                                        "Veículo pesado"
                                    )
                                }
                            }.joinToString(
                                ", "
                            )

                        ReviewSection(
                            title =
                                "Operação",
                            items =
                                listOf(
                                    "Região" to
                                            serviceRegion,

                                    "Atendimento 24h" to
                                            if (
                                                twentyFourHours
                                            ) {
                                                "Sim"
                                            } else {
                                                "Não"
                                            },

                                    "Serviços" to
                                            services
                                )
                        )

                        ReviewSection(
                            title =
                                "Documentos",
                            items =
                                listOf(
                                    "Empresa" to
                                            if (
                                                companyDocument != null
                                            ) {
                                                "Adicionado"
                                            } else {
                                                "Pendente"
                                            },

                                    "Proprietário" to
                                            if (
                                                ownerDocument != null
                                            ) {
                                                "Adicionado"
                                            } else {
                                                "Pendente"
                                            },

                                    "Comprovante de endereço" to
                                            if (
                                                ownerAddressProof != null
                                            ) {
                                                "Adicionado"
                                            } else {
                                                "Pendente"
                                            }
                                )
                        )

                        InfoCard(
                            "Após o envio, a conta empresarial será encaminhada para análise. " +
                                    "Na área da empresa, o proprietário poderá convidar motoristas e cadastrar guinchos por QR Code."
                        )
                    }
                }

                Spacer(
                    Modifier.height(
                        30.dp
                    )
                )
            }

            Surface(
                color =
                    GuinchouBackground,
                shadowElevation =
                    8.dp
            ) {

                Button(
                    onClick = {

                        if (
                            !validateCurrentStep()
                        ) {
                            return@Button
                        }

                        if (
                            currentStep <
                            steps.lastIndex
                        ) {
                            currentStep++
                        } else {
                            showSuccessDialog =
                                true
                        }
                    },
                    enabled =
                        !isSearchingCep,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal =
                                20.dp,
                            vertical =
                                16.dp
                        )
                        .height(
                            54.dp
                        ),
                    shape =
                        RoundedCornerShape(
                            16.dp
                        ),
                    colors =
                        ButtonDefaults
                            .buttonColors(
                                containerColor =
                                    GuinchouGreen,
                                contentColor =
                                    Color.Black,
                                disabledContainerColor =
                                    GuinchouGreen.copy(
                                        alpha =
                                            0.4f
                                    )
                            )
                ) {

                    Text(
                        text =
                            if (
                                step ==
                                CompanyRegistrationStep.REVIEW
                            ) {
                                "Enviar para análise"
                            } else {
                                "Continuar"
                            },
                        fontWeight =
                            FontWeight.Bold
                    )
                }
            }
        }
    }

    if (
        showSuccessDialog
    ) {

        AlertDialog(
            onDismissRequest = {},
            containerColor =
                GuinchouSurface,
            title = {

                Text(
                    text =
                        "Cadastro empresarial enviado",
                    color =
                        GuinchouWhite,
                    fontWeight =
                        FontWeight.Bold
                )
            },
            text = {

                Column {

                    Icon(
                        imageVector =
                            Icons.Default.CheckCircle,
                        contentDescription =
                            null,
                        tint =
                            GuinchouGreen,
                        modifier =
                            Modifier.size(
                                50.dp
                            )
                    )

                    Spacer(
                        Modifier.height(
                            14.dp
                        )
                    )

                    Text(
                        text =
                            "Os dados do proprietário e da empresa foram enviados. " +
                                    "A próxima área será o painel empresarial, onde motoristas e guinchos serão gerenciados separadamente.",
                        color =
                            GuinchouGray,
                        lineHeight =
                            20.sp
                    )
                }
            },
            confirmButton = {

                Button(
                    onClick = {

                        showSuccessDialog =
                            false

                        onRegistrationFinished()
                    },
                    colors =
                        ButtonDefaults
                            .buttonColors(
                                containerColor =
                                    GuinchouGreen,
                                contentColor =
                                    Color.Black
                            )
                ) {

                    Text(
                        text =
                            "Ir para painel",
                        fontWeight =
                            FontWeight.Bold
                    )
                }
            }
        )
    }
}

@Composable
private fun RegistrationHeader(
    currentStep: Int,
    totalSteps: Int,
    onBackClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal =
                    8.dp,
                vertical =
                    12.dp
            ),
        verticalAlignment =
            Alignment.CenterVertically
    ) {

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

        Column {

            Text(
                text =
                    "Cadastro empresarial",
                color =
                    GuinchouWhite,
                fontSize =
                    18.sp,
                fontWeight =
                    FontWeight.Bold
            )

            Text(
                text =
                    "Etapa $currentStep de $totalSteps",
                color =
                    GuinchouGray,
                fontSize =
                    12.sp
            )
        }
    }
}

@Composable
private fun RegistrationProgress(
    currentStep: Int,
    totalSteps: Int
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal =
                    20.dp,
                vertical =
                    8.dp
            ),
        horizontalArrangement =
            Arrangement.spacedBy(
                6.dp
            )
    ) {

        repeat(
            totalSteps
        ) { index ->

            Surface(
                modifier = Modifier
                    .weight(
                        1f
                    )
                    .height(
                        4.dp
                    ),
                shape =
                    RoundedCornerShape(
                        100.dp
                    ),
                color =
                    if (
                        index <=
                        currentStep
                    ) {
                        GuinchouGreen
                    } else {
                        GuinchouBorder
                    }
            ) {}
        }
    }
}

@Composable
private fun FieldLabel(
    text: String,
    required: Boolean
) {

    Text(
        text =
            buildAnnotatedString {

                append(
                    text
                )

                if (
                    required
                ) {

                    append(
                        " "
                    )

                    withStyle(
                        SpanStyle(
                            color =
                                GuinchouError,
                            fontWeight =
                                FontWeight.Bold
                        )
                    ) {
                        append(
                            "*"
                        )
                    }
                }
            },
        color =
            GuinchouWhite,
        fontSize =
            13.sp,
        fontWeight =
            FontWeight.Medium
    )
}

@Composable
private fun CompanyTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    required: Boolean,
    placeholder: String = "",
    keyboardType: KeyboardType =
        KeyboardType.Text,
    error: String? = null
) {

    Column {

        FieldLabel(
            text =
                label,
            required =
                required
        )

        Spacer(
            Modifier.height(
                7.dp
            )
        )

        OutlinedTextField(
            value =
                value,
            onValueChange =
                onValueChange,
            modifier =
                Modifier.fillMaxWidth(),
            singleLine =
                true,
            isError =
                error != null,
            placeholder = {

                Text(
                    text =
                        placeholder,
                    color =
                        GuinchouGray
                )
            },
            keyboardOptions =
                KeyboardOptions(
                    keyboardType =
                        keyboardType
                ),
            colors =
                OutlinedTextFieldDefaults
                    .colors(
                        focusedTextColor =
                            GuinchouWhite,
                        unfocusedTextColor =
                            GuinchouWhite,
                        focusedBorderColor =
                            GuinchouGreen,
                        unfocusedBorderColor =
                            GuinchouBorder,
                        errorBorderColor =
                            GuinchouError,
                        cursorColor =
                            GuinchouGreen,
                        focusedContainerColor =
                            GuinchouSurface,
                        unfocusedContainerColor =
                            GuinchouSurface
                    ),
            shape =
                RoundedCornerShape(
                    14.dp
                )
        )

        error?.let {

            Spacer(
                Modifier.height(
                    5.dp
                )
            )

            ErrorText(
                it
            )
        }

        Spacer(
            Modifier.height(
                16.dp
            )
        )
    }
}

@Composable
private fun RequiredSectionLabel(
    text: String
) {

    FieldLabel(
        text =
            text,
        required =
            true
    )
}

@Composable
private fun SettingSwitchCard(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {

    Surface(
        modifier =
            Modifier.fillMaxWidth(),
        color =
            GuinchouSurface,
        shape =
            RoundedCornerShape(
                16.dp
            ),
        border =
            BorderStroke(
                1.dp,
                GuinchouBorder
            )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    16.dp
                ),
            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Column(
                modifier =
                    Modifier.weight(
                        1f
                    )
            ) {

                Text(
                    text =
                        title,
                    color =
                        GuinchouWhite,
                    fontWeight =
                        FontWeight.SemiBold
                )

                Spacer(
                    Modifier.height(
                        3.dp
                    )
                )

                Text(
                    text =
                        description,
                    color =
                        GuinchouGray,
                    fontSize =
                        12.sp,
                    lineHeight =
                        17.sp
                )
            }

            Switch(
                checked =
                    checked,
                onCheckedChange =
                    onCheckedChange,
                colors =
                    SwitchDefaults
                        .colors(
                            checkedThumbColor =
                                Color.Black,
                            checkedTrackColor =
                                GuinchouGreen
                        )
            )
        }
    }
}

@Composable
private fun ServiceCheckCard(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                bottom =
                    9.dp
            ),
        color =
            if (
                checked
            ) {
                GuinchouGreen.copy(
                    alpha =
                        0.08f
                )
            } else {
                GuinchouSurface
            },
        shape =
            RoundedCornerShape(
                14.dp
            ),
        border =
            BorderStroke(
                1.dp,
                if (
                    checked
                ) {
                    GuinchouGreen.copy(
                        alpha =
                            0.5f
                    )
                } else {
                    GuinchouBorder
                }
            )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal =
                        14.dp,
                    vertical =
                        10.dp
                ),
            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Checkbox(
                checked =
                    checked,
                onCheckedChange =
                    onCheckedChange,
                colors =
                    CheckboxDefaults
                        .colors(
                            checkedColor =
                                GuinchouGreen,
                            checkmarkColor =
                                Color.Black
                        )
            )

            Spacer(
                Modifier.width(
                    8.dp
                )
            )

            Text(
                text =
                    title,
                color =
                    GuinchouWhite,
                fontSize =
                    14.sp
            )
        }
    }
}

@Composable
private fun DocumentUploadCard(
    title: String,
    required: Boolean,
    description: String,
    selected: Boolean,
    onClick: () -> Unit
) {

    Surface(
        onClick =
            onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                bottom =
                    12.dp
            ),
        color =
            if (
                selected
            ) {
                GuinchouGreen.copy(
                    alpha =
                        0.07f
                )
            } else {
                GuinchouSurface
            },
        shape =
            RoundedCornerShape(
                16.dp
            ),
        border =
            BorderStroke(
                1.dp,
                if (
                    selected
                ) {
                    GuinchouGreen.copy(
                        alpha =
                            0.5f
                    )
                } else {
                    GuinchouBorder
                }
            )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    16.dp
                ),
            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Icon(
                imageVector =
                    if (
                        selected
                    ) {
                        Icons.Default.CheckCircle
                    } else {
                        Icons.Default.UploadFile
                    },
                contentDescription =
                    null,
                tint =
                    if (
                        selected
                    ) {
                        GuinchouGreen
                    } else {
                        GuinchouGray
                    },
                modifier =
                    Modifier.size(
                        28.dp
                    )
            )

            Spacer(
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

                FieldLabel(
                    text =
                        title,
                    required =
                        required
                )

                Spacer(
                    Modifier.height(
                        3.dp
                    )
                )

                Text(
                    text =
                        if (
                            selected
                        ) {
                            "Arquivo selecionado"
                        } else {
                            description
                        },
                    color =
                        if (
                            selected
                        ) {
                            GuinchouGreen
                        } else {
                            GuinchouGray
                        },
                    fontSize =
                        12.sp
                )
            }
        }
    }
}

@Composable
private fun ReviewSection(
    title: String,
    items: List<Pair<String, String>>
) {

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                bottom =
                    14.dp
            ),
        color =
            GuinchouSurface,
        shape =
            RoundedCornerShape(
                17.dp
            ),
        border =
            BorderStroke(
                1.dp,
                GuinchouBorder
            )
    ) {

        Column(
            modifier =
                Modifier.padding(
                    16.dp
                )
        ) {

            Text(
                text =
                    title,
                color =
                    GuinchouGreen,
                fontWeight =
                    FontWeight.Bold,
                fontSize =
                    15.sp
            )

            Spacer(
                Modifier.height(
                    12.dp
                )
            )

            items.forEach {
                    (
                        label,
                        value
                    ) ->

                Text(
                    text =
                        label,
                    color =
                        GuinchouGray,
                    fontSize =
                        11.sp
                )

                Text(
                    text =
                        value.ifBlank {
                            "Não informado"
                        },
                    color =
                        GuinchouWhite,
                    fontSize =
                        14.sp,
                    lineHeight =
                        19.sp
                )

                Spacer(
                    Modifier.height(
                        10.dp
                    )
                )
            }
        }
    }
}

@Composable
private fun InfoCard(
    text: String
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
                15.dp
            ),
        border =
            BorderStroke(
                1.dp,
                GuinchouGreen.copy(
                    alpha =
                        0.25f
                )
            )
    ) {

        Row(
            modifier =
                Modifier.padding(
                    15.dp
                ),
            verticalAlignment =
                Alignment.Top
        ) {

            Icon(
                imageVector =
                    Icons.Default.Info,
                contentDescription =
                    null,
                tint =
                    GuinchouGreen,
                modifier =
                    Modifier.size(
                        20.dp
                    )
            )

            Spacer(
                Modifier.width(
                    10.dp
                )
            )

            Text(
                text =
                    text,
                color =
                    GuinchouGray,
                fontSize =
                    12.sp,
                lineHeight =
                    18.sp
            )
        }
    }
}

@Composable
private fun ErrorText(
    text: String
) {

    Text(
        text =
            text,
        color =
            GuinchouError,
        fontSize =
            12.sp
    )
}

private fun onlyDigits(
    value: String
): String {

    return value.filter(
        Char::isDigit
    )
}

private fun formatCpf(
    value: String
): String {

    val digits =
        onlyDigits(
            value
        ).take(
            11
        )

    return buildString {

        digits.forEachIndexed {
                index,
                char ->

            append(
                char
            )

            if (
                (
                        index == 2 ||
                                index == 5
                        ) &&
                index <
                digits.lastIndex
            ) {
                append(
                    '.'
                )
            }

            if (
                index == 8 &&
                index <
                digits.lastIndex
            ) {
                append(
                    '-'
                )
            }
        }
    }
}

private fun formatCnpj(
    value: String
): String {

    val digits =
        onlyDigits(
            value
        ).take(
            14
        )

    return buildString {

        digits.forEachIndexed {
                index,
                char ->

            append(
                char
            )

            when (index) {

                1,
                4 -> {
                    if (
                        index <
                        digits.lastIndex
                    ) {
                        append(
                            '.'
                        )
                    }
                }

                7 -> {
                    if (
                        index <
                        digits.lastIndex
                    ) {
                        append(
                            '/'
                        )
                    }
                }

                11 -> {
                    if (
                        index <
                        digits.lastIndex
                    ) {
                        append(
                            '-'
                        )
                    }
                }
            }
        }
    }
}

private fun formatPhone(
    value: String
): String {

    val digits =
        onlyDigits(
            value
        ).take(
            11
        )

    if (
        digits.isEmpty()
    ) {
        return ""
    }

    return when {

        digits.length <= 2 ->
            "($digits"

        digits.length <= 6 ->
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

    val digits =
        onlyDigits(
            value
        ).take(
            8
        )

    return if (
        digits.length <= 5
    ) {
        digits
    } else {
        "${digits.take(5)}-${digits.drop(5)}"
    }
}

private fun isValidCpf(
    value: String
): Boolean {

    val cpf =
        onlyDigits(
            value
        )

    if (
        cpf.length != 11 ||
        cpf.all {
            it ==
                    cpf.first()
        }
    ) {
        return false
    }

    val numbers =
        cpf.map(
            Char::digitToInt
        )

    var sum =
        (0..8).sumOf {
            numbers[it] *
                    (
                            10 -
                                    it
                            )
        }

    var firstDigit =
        (
                sum *
                        10
                ) % 11

    if (
        firstDigit == 10
    ) {
        firstDigit =
            0
    }

    if (
        firstDigit !=
        numbers[9]
    ) {
        return false
    }

    sum =
        (0..9).sumOf {
            numbers[it] *
                    (
                            11 -
                                    it
                            )
        }

    var secondDigit =
        (
                sum *
                        10
                ) % 11

    if (
        secondDigit == 10
    ) {
        secondDigit =
            0
    }

    return secondDigit ==
            numbers[10]
}

private fun isValidCnpj(
    value: String
): Boolean {

    val cnpj =
        onlyDigits(
            value
        )

    if (
        cnpj.length != 14 ||
        cnpj.all {
            it ==
                    cnpj.first()
        }
    ) {
        return false
    }

    val numbers =
        cnpj.map(
            Char::digitToInt
        )

    val firstWeights =
        intArrayOf(
            5, 4, 3, 2,
            9, 8, 7, 6,
            5, 4, 3, 2
        )

    val firstRemainder =
        firstWeights.indices
            .sumOf {
                numbers[it] *
                        firstWeights[it]
            } % 11

    val firstDigit =
        if (
            firstRemainder < 2
        ) {
            0
        } else {
            11 -
                    firstRemainder
        }

    if (
        firstDigit !=
        numbers[12]
    ) {
        return false
    }

    val secondWeights =
        intArrayOf(
            6, 5, 4, 3, 2,
            9, 8, 7, 6,
            5, 4, 3, 2
        )

    val secondRemainder =
        secondWeights.indices
            .sumOf {
                numbers[it] *
                        secondWeights[it]
            } % 11

    val secondDigit =
        if (
            secondRemainder < 2
        ) {
            0
        } else {
            11 -
                    secondRemainder
        }

    return secondDigit ==
            numbers[13]
}

private fun isValidPhone(
    value: String
): Boolean {

    val digits =
        onlyDigits(
            value
        )

    if (
        digits.length !in
        10..11 ||
        digits.all {
            it ==
                    digits.first()
        }
    ) {
        return false
    }

    val ddd =
        digits.take(
            2
        ).toIntOrNull()
            ?: return false

    return ddd in
            11..99
}

private fun isValidEmail(
    value: String
): Boolean {

    return value.isNotBlank() &&
            Patterns.EMAIL_ADDRESS
                .matcher(
                    value
                )
                .matches()
}

private fun isValidUf(
    value: String
): Boolean {

    return value
        .trim()
        .uppercase() in
            setOf(
                "AC",
                "AL",
                "AP",
                "AM",
                "BA",
                "CE",
                "DF",
                "ES",
                "GO",
                "MA",
                "MT",
                "MS",
                "MG",
                "PA",
                "PB",
                "PR",
                "PE",
                "PI",
                "RJ",
                "RN",
                "RS",
                "RO",
                "RR",
                "SC",
                "SP",
                "SE",
                "TO"
            )
}

private fun fetchCepAddress(
    cep: String
): CepAddress? {

    var connection:
            HttpURLConnection? =
        null

    return try {

        connection =
            URL(
                "https://viacep.com.br/ws/$cep/json/"
            ).openConnection()
                    as HttpURLConnection

        connection.requestMethod =
            "GET"

        connection.connectTimeout =
            7000

        connection.readTimeout =
            7000

        connection.setRequestProperty(
            "Accept",
            "application/json"
        )

        if (
            connection.responseCode !in
            200..299
        ) {
            return null
        }

        val json =
            JSONObject(
                connection
                    .inputStream
                    .bufferedReader()
                    .use {
                        it.readText()
                    }
            )

        if (
            json.optBoolean(
                "erro",
                false
            )
        ) {
            return null
        }

        CepAddress(
            street =
                json.optString(
                    "logradouro",
                    ""
                ),
            neighborhood =
                json.optString(
                    "bairro",
                    ""
                ),
            city =
                json.optString(
                    "localidade",
                    ""
                ),
            state =
                json.optString(
                    "uf",
                    ""
                )
        )

    } catch (
        _: Exception
    ) {

        null

    } finally {

        connection
            ?.disconnect()
    }
}