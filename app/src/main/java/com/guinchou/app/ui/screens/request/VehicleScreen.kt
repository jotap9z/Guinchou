package com.guinchou.app.ui.screens.request

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.guinchou.app.ui.theme.GuinchouBackground
import com.guinchou.app.ui.theme.GuinchouBorder
import com.guinchou.app.ui.theme.GuinchouGray
import com.guinchou.app.ui.theme.GuinchouGreen
import com.guinchou.app.ui.theme.GuinchouSurface
import com.guinchou.app.ui.theme.GuinchouWhite

private data class QuickVehicle(
    val type: String,
    val brand: String,
    val model: String,
    val year: String,
    val plate: String
)

@Composable
fun VehicleScreen(
    onContinueClick: (
        vehicleType: String,
        brand: String,
        model: String,
        year: String,
        plate: String
    ) -> Unit,
    onBackClick: () -> Unit
) {
    var selectedVehicleType by remember { mutableStateOf("") }
    var brand by remember { mutableStateOf("") }
    var model by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var plate by remember { mutableStateOf("") }
    var selectedQuickVehicle by remember { mutableStateOf<String?>(null) }
    var showManualForm by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val focusManager: FocusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val backgroundInteractionSource = remember {
        MutableInteractionSource()
    }

    /*
     * Mock temporário.
     * Depois esta lista poderá vir dos veículos cadastrados
     * no perfil/Supabase sem alterar o fluxo desta tela.
     */
    val quickVehicles = remember {
        listOf(
            QuickVehicle(
                type = "Carro",
                brand = "Chevrolet",
                model = "Onix",
                year = "2022",
                plate = "ABC1D23"
            ),
            QuickVehicle(
                type = "SUV / Pickup",
                brand = "Jeep",
                model = "Renegade",
                year = "2021",
                plate = "DEF4G56"
            )
        )
    }

    val vehicleTypes = remember {
        listOf(
            "Carro",
            "Moto",
            "SUV / Pickup",
            "Van",
            "Utilitário",
            "Caminhão leve",
            "Caminhão pesado"
        )
    }

    fun selectQuickVehicle(vehicle: QuickVehicle) {
        selectedQuickVehicle = vehicle.plate
        selectedVehicleType = vehicle.type
        brand = vehicle.brand
        model = vehicle.model
        year = vehicle.year
        plate = vehicle.plate
        showManualForm = false
        errorMessage = null
        focusManager.clearFocus()
        keyboardController?.hide()
    }

    fun startManualVehicle() {
        selectedQuickVehicle = null
        selectedVehicleType = ""
        brand = ""
        model = ""
        year = ""
        plate = ""
        showManualForm = true
        errorMessage = null
    }

    fun validateAndContinue() {
        focusManager.clearFocus()
        keyboardController?.hide()

        if (selectedVehicleType.isBlank()) {
            errorMessage = "Selecione o tipo do veículo."
            return
        }

        if (brand.isBlank()) {
            errorMessage = "Informe a marca do veículo."
            return
        }

        if (model.isBlank()) {
            errorMessage = "Informe o modelo do veículo."
            return
        }

        if (year.isNotBlank() && year.length != 4) {
            errorMessage = "Informe um ano válido."
            return
        }

        if (plate.isNotBlank() && plate.length != 7) {
            errorMessage = "Informe uma placa válida."
            return
        }

        onContinueClick(
            selectedVehicleType,
            brand.trim(),
            model.trim(),
            year.trim(),
            plate.trim()
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GuinchouBackground)
            .clickable(
                interactionSource = backgroundInteractionSource,
                indication = null
            ) {
                focusManager.clearFocus()
                keyboardController?.hide()
            }
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        VehicleHeader(onBackClick = onBackClick)

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            VehicleStepIndicator()

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Qual veículo precisa do guincho?",
                color = GuinchouWhite,
                fontSize = 27.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(7.dp))

            Text(
                text = "Selecione um veículo cadastrado ou informe os dados de outro veículo.",
                color = GuinchouGray,
                fontSize = 13.sp
            )

            Spacer(modifier = Modifier.height(22.dp))

            Text(
                text = "Meus veículos",
                color = GuinchouWhite,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(10.dp))

            quickVehicles.forEachIndexed { index, vehicle ->
                SavedVehicleCard(
                    vehicle = vehicle,
                    selected = selectedQuickVehicle == vehicle.plate,
                    onClick = {
                        selectQuickVehicle(vehicle)
                    }
                )

                if (index != quickVehicles.lastIndex) {
                    Spacer(modifier = Modifier.height(9.dp))
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            OtherVehicleCard(
                selected = showManualForm,
                onClick = {
                    startManualVehicle()
                }
            )

            if (showManualForm) {
                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Dados do veículo",
                    color = GuinchouWhite,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Essas informações ajudam a encontrar um guincho compatível.",
                    color = GuinchouGray,
                    fontSize = 11.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Tipo do veículo",
                    color = GuinchouWhite,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(10.dp))

                vehicleTypes.forEachIndexed { index, vehicleType ->
                    VehicleTypeOption(
                        title = vehicleType,
                        selected = selectedVehicleType == vehicleType,
                        onClick = {
                            selectedVehicleType = vehicleType
                            errorMessage = null
                            focusManager.clearFocus()
                            keyboardController?.hide()
                        }
                    )

                    if (index != vehicleTypes.lastIndex) {
                        Spacer(modifier = Modifier.height(7.dp))
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = brand,
                    onValueChange = {
                        brand = it.take(40)
                        errorMessage = null
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Marca") },
                    placeholder = { Text("Ex.: Chevrolet") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text
                    ),
                    shape = RoundedCornerShape(15.dp),
                    colors = guinchouTextFieldColors()
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = model,
                    onValueChange = {
                        model = it.take(40)
                        errorMessage = null
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Modelo") },
                    placeholder = { Text("Ex.: Onix") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text
                    ),
                    shape = RoundedCornerShape(15.dp),
                    colors = guinchouTextFieldColors()
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = year,
                        onValueChange = { newValue ->
                            if (
                                newValue.length <= 4 &&
                                newValue.all { it.isDigit() }
                            ) {
                                year = newValue
                            }
                            errorMessage = null
                        },
                        modifier = Modifier.weight(1f),
                        label = { Text("Ano") },
                        placeholder = { Text("2022") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        shape = RoundedCornerShape(15.dp),
                        colors = guinchouTextFieldColors()
                    )

                    OutlinedTextField(
                        value = plate,
                        onValueChange = { newValue ->
                            plate = newValue
                                .uppercase()
                                .filter { it.isLetterOrDigit() }
                                .take(7)
                            errorMessage = null
                        },
                        modifier = Modifier.weight(1.25f),
                        label = { Text("Placa") },
                        placeholder = { Text("ABC1D23") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text
                        ),
                        shape = RoundedCornerShape(15.dp),
                        colors = guinchouTextFieldColors()
                    )
                }
            }

            errorMessage?.let { message ->
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = message,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 12.sp
                )
            }

            if (selectedQuickVehicle != null) {
                Spacer(modifier = Modifier.height(18.dp))

                SelectedVehicleSummary(
                    vehicleType = selectedVehicleType,
                    brand = brand,
                    model = model,
                    year = year,
                    plate = plate
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }

        HorizontalDivider(color = GuinchouBorder)

        Button(
            onClick = { validateAndContinue() },
            enabled = selectedQuickVehicle != null ||
                    (
                            showManualForm &&
                                    selectedVehicleType.isNotBlank() &&
                                    brand.isNotBlank() &&
                                    model.isNotBlank()
                            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .height(54.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = GuinchouGreen,
                contentColor = GuinchouBackground,
                disabledContainerColor = GuinchouBorder,
                disabledContentColor = GuinchouGray
            )
        ) {
            Text(
                text = "Continuar",
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun VehicleHeader(
    onBackClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Voltar",
                tint = GuinchouWhite
            )
        }

        Text(
            text = "Veículo",
            color = GuinchouWhite,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun VehicleStepIndicator() {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "ETAPA 3 DE 5",
                color = GuinchouGreen,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Veículo",
                color = GuinchouGray,
                fontSize = 11.sp
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            repeat(5) { index ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(3.dp)
                        .background(
                            color = if (index <= 2) {
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
private fun SavedVehicleCard(
    vehicle: QuickVehicle,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = if (selected) {
                    GuinchouGreen.copy(alpha = 0.07f)
                } else {
                    GuinchouSurface
                },
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                width = if (selected) 2.dp else 1.dp,
                color = if (selected) GuinchouGreen else GuinchouBorder,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onClick)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        VehicleIconBox(
            type = vehicle.type,
            selected = selected
        )

        Spacer(modifier = Modifier.size(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "${vehicle.brand} ${vehicle.model}",
                color = GuinchouWhite,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = "${vehicle.type} • ${vehicle.year} • ${vehicle.plate}",
                color = GuinchouGray,
                fontSize = 10.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        if (selected) {
            Box(
                modifier = Modifier
                    .size(26.dp)
                    .background(
                        GuinchouGreen,
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Selecionado",
                    tint = GuinchouBackground,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
private fun OtherVehicleCard(
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = if (selected) {
                    GuinchouGreen.copy(alpha = 0.07f)
                } else {
                    GuinchouSurface
                },
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                width = if (selected) 2.dp else 1.dp,
                color = if (selected) GuinchouGreen else GuinchouBorder,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onClick)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .background(
                    GuinchouBackground,
                    RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = GuinchouGreen,
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(modifier = Modifier.size(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "Outro veículo",
                color = GuinchouWhite,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = "Informar os dados manualmente",
                color = GuinchouGray,
                fontSize = 10.sp
            )
        }

        if (selected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Selecionado",
                tint = GuinchouGreen,
                modifier = Modifier.size(21.dp)
            )
        }
    }
}

@Composable
private fun VehicleIconBox(
    type: String,
    selected: Boolean
) {
    val icon: ImageVector = when {
        type.contains("Moto", ignoreCase = true) ->
            Icons.Default.TwoWheeler

        type.contains("Caminhão", ignoreCase = true) ||
                type.contains("Utilitário", ignoreCase = true) ||
                type.contains("Van", ignoreCase = true) ->
            Icons.Default.LocalShipping

        else ->
            Icons.Default.DirectionsCar
    }

    Box(
        modifier = Modifier
            .size(44.dp)
            .background(
                if (selected) {
                    GuinchouGreen.copy(alpha = 0.12f)
                } else {
                    GuinchouBackground
                },
                RoundedCornerShape(12.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (selected) GuinchouGreen else GuinchouGray,
            modifier = Modifier.size(23.dp)
        )
    }
}

@Composable
private fun VehicleTypeOption(
    title: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = if (selected) {
                    GuinchouGreen.copy(alpha = 0.07f)
                } else {
                    GuinchouSurface
                },
                shape = RoundedCornerShape(14.dp)
            )
            .border(
                width = if (selected) 2.dp else 1.dp,
                color = if (selected) GuinchouGreen else GuinchouBorder,
                shape = RoundedCornerShape(14.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 13.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            color = if (selected) GuinchouWhite else GuinchouGray,
            fontSize = 13.sp,
            fontWeight = if (selected) {
                FontWeight.SemiBold
            } else {
                FontWeight.Normal
            }
        )

        if (selected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = GuinchouGreen,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
private fun SelectedVehicleSummary(
    vehicleType: String,
    brand: String,
    model: String,
    year: String,
    plate: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                GuinchouGreen.copy(alpha = 0.06f),
                RoundedCornerShape(15.dp)
            )
            .border(
                1.dp,
                GuinchouGreen.copy(alpha = 0.25f),
                RoundedCornerShape(15.dp)
            )
            .padding(14.dp)
    ) {
        Text(
            text = "Veículo selecionado",
            color = GuinchouGreen,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(5.dp))

        Text(
            text = "$brand $model",
            color = GuinchouWhite,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(3.dp))

        Text(
            text = listOf(vehicleType, year, plate)
                .filter { it.isNotBlank() }
                .joinToString(" • "),
            color = GuinchouGray,
            fontSize = 10.sp
        )
    }
}

@Composable
private fun guinchouTextFieldColors() =
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
