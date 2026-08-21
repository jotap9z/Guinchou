package com.guinchou.app.ui.screens.request

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
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

/**
 * Etapa 3 de 5.
 *
 * Aqui o cliente informa qual veículo
 * precisará ser transportado.
 *
 * Dados principais:
 *
 * - tipo do veículo;
 * - marca;
 * - modelo;
 * - ano;
 * - placa.
 */
@Composable
fun VehicleScreen(

    /*
     * Envia os dados preenchidos
     * para a próxima etapa.
     */
    onContinueClick: (
        vehicleType: String,
        brand: String,
        model: String,
        year: String,
        plate: String
    ) -> Unit,

    /*
     * Volta para a etapa anterior.
     */
    onBackClick: () -> Unit
) {

    /*
     * Tipo selecionado.
     */
    var selectedVehicleType by remember {
        mutableStateOf("")
    }

    /*
     * Marca.
     *
     * Exemplo:
     * Chevrolet
     */
    var brand by remember {
        mutableStateOf("")
    }

    /*
     * Modelo.
     *
     * Exemplo:
     * Onix
     */
    var model by remember {
        mutableStateOf("")
    }

    /*
     * Ano.
     */
    var year by remember {
        mutableStateOf("")
    }

    /*
     * Placa.
     */
    var plate by remember {
        mutableStateOf("")
    }

    /*
     * Mensagem de validação.
     */
    var errorMessage by remember {
        mutableStateOf<String?>(null)
    }

    /*
     * Controla qual campo
     * está selecionado.
     */
    val focusManager: FocusManager =
        LocalFocusManager.current

    /*
     * Controla o teclado
     * virtual do Android.
     */
    val keyboardController =
        LocalSoftwareKeyboardController.current

    /*
     * InteractionSource utilizado
     * para que tocar no fundo
     * não produza ripple.
     */
    val backgroundInteractionSource =
        remember {
            MutableInteractionSource()
        }

    /*
     * Lista de categorias disponíveis.
     */
    val vehicleTypes = listOf(
        "Carro",
        "Moto",
        "SUV / Pickup",
        "Van",
        "Utilitário",
        "Caminhão leve",
        "Caminhão pesado"
    )


    Column(

        modifier = Modifier
            .fillMaxSize()

            /*
             * Fundo principal.
             */
            .background(
                GuinchouBackground
            )

            /*
             * QUALQUER TOQUE EM ÁREA LIVRE:
             *
             * - remove o foco;
             * - fecha o teclado;
             * - mantém todos os dados digitados.
             */
            .clickable(
                interactionSource =
                    backgroundInteractionSource,
                indication = null
            ) {

                focusManager.clearFocus()

                keyboardController?.hide()
            }

            /*
             * Respeita relógio,
             * bateria, notch etc.
             */
            .statusBarsPadding()

            /*
             * Respeita gestos ou
             * barra inferior do Android.
             */
            .navigationBarsPadding()
    ) {

        /*
         * ======================================
         * CONTEÚDO ROLÁVEL
         * ======================================
         */
        Column(

            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(
                    horizontal = 20.dp,
                    vertical = 16.dp
                )
        ) {

            /*
             * Indicador da etapa.
             */
            Text(
                text = "3 de 5",
                color = GuinchouGray,
                fontSize = 13.sp
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            /*
             * Título.
             */
            Text(
                text = "Qual é o veículo?",
                color = GuinchouWhite,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text =
                    "Informe os dados do veículo para encontrarmos o guincho adequado.",
                color = GuinchouGray,
                fontSize = 14.sp
            )

            Spacer(
                modifier = Modifier.height(26.dp)
            )


            /*
             * ======================================
             * TIPO DO VEÍCULO
             * ======================================
             */
            Text(
                text = "Tipo do veículo",
                color = GuinchouWhite,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            /*
             * Criamos uma opção para
             * cada categoria.
             */
            vehicleTypes.forEach { vehicleType ->

                VehicleTypeOption(

                    title = vehicleType,

                    selected =
                        selectedVehicleType ==
                                vehicleType,

                    onClick = {

                        /*
                         * Seleciona o veículo.
                         */
                        selectedVehicleType =
                            vehicleType

                        /*
                         * Limpa possível erro.
                         */
                        errorMessage = null

                        /*
                         * Fecha teclado caso
                         * ainda esteja aberto.
                         */
                        focusManager.clearFocus()

                        keyboardController?.hide()
                    }
                )

                Spacer(
                    modifier =
                        Modifier.height(8.dp)
                )
            }


            Spacer(
                modifier = Modifier.height(18.dp)
            )


            /*
             * ======================================
             * MARCA
             * ======================================
             */
            OutlinedTextField(

                value = brand,

                onValueChange = {

                    brand = it

                    errorMessage = null
                },

                modifier =
                    Modifier.fillMaxWidth(),

                label = {

                    Text(
                        text = "Marca"
                    )
                },

                placeholder = {

                    Text(
                        text = "Ex.: Chevrolet"
                    )
                },

                singleLine = true,

                keyboardOptions =
                    KeyboardOptions(
                        keyboardType =
                            KeyboardType.Text
                    ),

                colors =
                    guinchouTextFieldColors()
            )


            Spacer(
                modifier = Modifier.height(14.dp)
            )


            /*
             * ======================================
             * MODELO
             * ======================================
             */
            OutlinedTextField(

                value = model,

                onValueChange = {

                    model = it

                    errorMessage = null
                },

                modifier =
                    Modifier.fillMaxWidth(),

                label = {

                    Text(
                        text = "Modelo"
                    )
                },

                placeholder = {

                    Text(
                        text = "Ex.: Onix"
                    )
                },

                singleLine = true,

                keyboardOptions =
                    KeyboardOptions(
                        keyboardType =
                            KeyboardType.Text
                    ),

                colors =
                    guinchouTextFieldColors()
            )


            Spacer(
                modifier = Modifier.height(14.dp)
            )


            /*
             * ======================================
             * ANO
             * ======================================
             */
            OutlinedTextField(

                value = year,

                onValueChange = { newValue ->

                    /*
                     * Permitimos apenas números
                     * e no máximo quatro dígitos.
                     */
                    if (
                        newValue.length <= 4 &&
                        newValue.all {
                            it.isDigit()
                        }
                    ) {

                        year = newValue
                    }

                    errorMessage = null
                },

                modifier =
                    Modifier.fillMaxWidth(),

                label = {

                    Text(
                        text = "Ano"
                    )
                },

                placeholder = {

                    Text(
                        text = "Ex.: 2022"
                    )
                },

                singleLine = true,

                keyboardOptions =
                    KeyboardOptions(
                        keyboardType =
                            KeyboardType.Number
                    ),

                colors =
                    guinchouTextFieldColors()
            )


            Spacer(
                modifier = Modifier.height(14.dp)
            )


            /*
             * ======================================
             * PLACA
             * ======================================
             */
            OutlinedTextField(

                value = plate,

                onValueChange = { newValue ->

                    /*
                     * Remove espaços
                     * e deixa a placa maiúscula.
                     */
                    plate =
                        newValue
                            .uppercase()
                            .replace(
                                " ",
                                ""
                            )
                            .take(7)

                    errorMessage = null
                },

                modifier =
                    Modifier.fillMaxWidth(),

                label = {

                    Text(
                        text = "Placa"
                    )
                },

                placeholder = {

                    Text(
                        text = "Ex.: ABC1D23"
                    )
                },

                singleLine = true,

                keyboardOptions =
                    KeyboardOptions(
                        keyboardType =
                            KeyboardType.Text
                    ),

                colors =
                    guinchouTextFieldColors()
            )


            /*
             * ======================================
             * ERRO
             * ======================================
             */
            if (
                errorMessage != null
            ) {

                Spacer(
                    modifier =
                        Modifier.height(10.dp)
                )

                Text(

                    text =
                        errorMessage!!,

                    color =
                        MaterialTheme
                            .colorScheme
                            .error,

                    fontSize = 13.sp
                )
            }


            Spacer(
                modifier = Modifier.height(18.dp)
            )


            /*
             * Informação de apoio.
             */
            Text(

                text =
                    "Essas informações ajudam a selecionar um guincho compatível com o seu veículo.",

                color =
                    GuinchouGray,

                fontSize = 13.sp,

                textAlign =
                    TextAlign.Center,

                modifier =
                    Modifier.fillMaxWidth()
            )


            Spacer(
                modifier = Modifier.height(20.dp)
            )
        }


        /*
         * ======================================
         * BOTÕES INFERIORES
         * ======================================
         */
        Row(

            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 20.dp,
                    vertical = 12.dp
                ),

            horizontalArrangement =
                Arrangement.spacedBy(
                    12.dp
                )
        ) {

            /*
             * VOLTAR
             */
            OutlinedButton(

                onClick = {

                    focusManager.clearFocus()

                    keyboardController?.hide()

                    onBackClick()
                },

                modifier = Modifier
                    .weight(1f)
                    .height(54.dp),

                shape =
                    RoundedCornerShape(
                        14.dp
                    )
            ) {

                Text(
                    text = "Voltar",
                    color = GuinchouWhite
                )
            }


            /*
             * CONTINUAR
             */
            Button(

                onClick = {

                    /*
                     * Fecha teclado.
                     */
                    focusManager.clearFocus()

                    keyboardController?.hide()


                    /*
                     * O tipo é obrigatório.
                     */
                    if (
                        selectedVehicleType.isBlank()
                    ) {

                        errorMessage =
                            "Selecione o tipo do veículo."

                        return@Button
                    }


                    /*
                     * A marca é obrigatória.
                     */
                    if (
                        brand.isBlank()
                    ) {

                        errorMessage =
                            "Informe a marca do veículo."

                        return@Button
                    }


                    /*
                     * O modelo é obrigatório.
                     *
                     * Essa é uma das informações
                     * exigidas pelo fluxo do Guinchou.
                     */
                    if (
                        model.isBlank()
                    ) {

                        errorMessage =
                            "Informe o modelo do veículo."

                        return@Button
                    }


                    /*
                     * Se houver ano preenchido,
                     * fazemos uma validação simples.
                     */
                    if (
                        year.isNotBlank() &&
                        year.length != 4
                    ) {

                        errorMessage =
                            "Informe um ano válido."

                        return@Button
                    }


                    /*
                     * Se houver placa,
                     * esperamos sete caracteres.
                     */
                    if (
                        plate.isNotBlank() &&
                        plate.length != 7
                    ) {

                        errorMessage =
                            "Informe uma placa válida."

                        return@Button
                    }


                    /*
                     * Envia os dados para
                     * a próxima tela.
                     */
                    onContinueClick(

                        selectedVehicleType,

                        brand.trim(),

                        model.trim(),

                        year.trim(),

                        plate.trim()
                    )
                },

                modifier = Modifier
                    .weight(1.4f)
                    .height(54.dp),

                shape =
                    RoundedCornerShape(
                        14.dp
                    ),

                colors =
                    ButtonDefaults
                        .buttonColors(

                            containerColor =
                                GuinchouGreen,

                            contentColor =
                                GuinchouBackground
                        )
            ) {

                Text(

                    text = "Continuar",

                    fontWeight =
                        FontWeight.Bold
                )
            }
        }
    }
}


/**
 * Card utilizado para selecionar
 * o tipo do veículo.
 */
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

                color =

                    if (selected) {

                        GuinchouSurface

                    } else {

                        GuinchouBackground
                    },

                shape =
                    RoundedCornerShape(
                        14.dp
                    )
            )

            .border(

                width =

                    if (selected) {
                        2.dp
                    } else {
                        1.dp
                    },

                color =

                    if (selected) {

                        GuinchouGreen

                    } else {

                        GuinchouBorder
                    },

                shape =
                    RoundedCornerShape(
                        14.dp
                    )
            )

            .clickable {

                onClick()
            }

            .padding(15.dp),

        horizontalArrangement =
            Arrangement.SpaceBetween
    ) {

        Text(

            text = title,

            color =

                if (selected) {

                    GuinchouWhite

                } else {

                    GuinchouGray
                },

            fontSize = 14.sp,

            fontWeight =

                if (selected) {

                    FontWeight.SemiBold

                } else {

                    FontWeight.Normal
                }
        )


        /*
         * Indicador visual.
         */
        Text(

            text =

                if (selected) {
                    "✓"
                } else {
                    ""
                },

            color =
                GuinchouGreen,

            fontWeight =
                FontWeight.Bold
        )
    }
}


/**
 * Centraliza as cores dos campos.
 *
 * Assim não precisamos repetir toda
 * a configuração visual em cada campo.
 */
@Composable
private fun guinchouTextFieldColors() =
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