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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.guinchou.app.ui.theme.GuinchouBackground
import com.guinchou.app.ui.theme.GuinchouBorder
import com.guinchou.app.ui.theme.GuinchouGray
import com.guinchou.app.ui.theme.GuinchouGreen
import com.guinchou.app.ui.theme.GuinchouSurface
import com.guinchou.app.ui.theme.GuinchouWhite

/**
 * Etapa 4 de 5.
 *
 * O cliente informa se o problema
 * é mecânico ou decorrente de acidente.
 *
 * Mantém o comportamento padrão:
 *
 * tocar fora do campo:
 *
 * - fecha teclado;
 * - remove foco;
 * - mantém tudo preenchido.
 */
@Composable
fun ProblemScreen(

    initialProblemType: String = "",

    initialProblemDetail: String = "",

    initialDescription: String = "",

    onContinueClick: (
        problemType: String,
        problemDetail: String,
        description: String
    ) -> Unit,

    onBackClick: () -> Unit
) {

    /*
     * Categoria principal.
     */
    var selectedProblemType by remember(
        initialProblemType
    ) {

        mutableStateOf(
            initialProblemType
        )
    }

    /*
     * Problema específico.
     */
    var selectedProblemDetail by remember(
        initialProblemDetail
    ) {

        mutableStateOf(
            initialProblemDetail
        )
    }

    /*
     * Observação adicional.
     */
    var description by remember(
        initialDescription
    ) {

        mutableStateOf(
            initialDescription
        )
    }

    /*
     * Erro de validação.
     */
    var errorMessage by remember {
        mutableStateOf<String?>(null)
    }

    val focusManager: FocusManager =
        LocalFocusManager.current

    val keyboardController =
        LocalSoftwareKeyboardController.current

    val backgroundInteractionSource =
        remember {
            MutableInteractionSource()
        }

    /*
     * Problemas mecânicos.
     */
    val mechanicalProblems =
        listOf(
            "Veículo não liga",
            "Pane mecânica",
            "Superaquecimento",
            "Problema elétrico",
            "Problema na transmissão",
            "Pneu / roda",
            "Falta de combustível",
            "Outro problema mecânico"
        )

    /*
     * Problemas relacionados
     * a acidentes.
     */
    val accidentProblems =
        listOf(
            "Colisão",
            "Capotamento",
            "Veículo fora da pista",
            "Rodas danificadas",
            "Suspensão danificada",
            "Veículo preso",
            "Outro acidente"
        )

    /*
     * Determina qual lista mostrar.
     */
    val detailOptions =

        when (
            selectedProblemType
        ) {

            "MECHANICAL" ->
                mechanicalProblems

            "ACCIDENT" ->
                accidentProblems

            else ->
                emptyList()
        }


    Column(

        modifier = Modifier
            .fillMaxSize()

            .background(
                GuinchouBackground
            )

            /*
             * Padrão global dos formulários:
             *
             * tocar fora fecha teclado
             * sem apagar os dados.
             */
            .clickable(
                interactionSource =
                    backgroundInteractionSource,
                indication = null
            ) {

                focusManager.clearFocus()

                keyboardController?.hide()
            }

            .statusBarsPadding()

            .navigationBarsPadding()
    ) {

        /*
         * =====================================
         * CONTEÚDO
         * =====================================
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

            Text(
                text = "4 de 5",
                color = GuinchouGray,
                fontSize = 13.sp
            )

            Spacer(
                modifier =
                    Modifier.height(8.dp)
            )

            Text(
                text = "O que aconteceu?",
                color = GuinchouWhite,
                fontSize = 26.sp,
                fontWeight =
                    FontWeight.Bold
            )

            Spacer(
                modifier =
                    Modifier.height(8.dp)
            )

            Text(
                text =
                    "Selecione o tipo de problema do veículo.",
                color = GuinchouGray,
                fontSize = 14.sp
            )

            Spacer(
                modifier =
                    Modifier.height(26.dp)
            )

            /*
             * =====================================
             * PROBLEMA MECÂNICO
             * =====================================
             */
            ProblemTypeCard(

                title =
                    "Problema mecânico",

                description =
                    "Pane, falha elétrica, superaquecimento e outros problemas do veículo.",

                selected =
                    selectedProblemType ==
                            "MECHANICAL",

                onClick = {

                    selectedProblemType =
                        "MECHANICAL"

                    /*
                     * Ao trocar a categoria,
                     * limpamos o detalhe antigo.
                     */
                    selectedProblemDetail = ""

                    errorMessage = null

                    focusManager.clearFocus()

                    keyboardController?.hide()
                }
            )


            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )


            /*
             * =====================================
             * ACIDENTE
             * =====================================
             */
            ProblemTypeCard(

                title =
                    "Acidente",

                description =
                    "Colisão, capotamento ou danos que impedem o veículo de se locomover.",

                selected =
                    selectedProblemType ==
                            "ACCIDENT",

                onClick = {

                    selectedProblemType =
                        "ACCIDENT"

                    selectedProblemDetail = ""

                    errorMessage = null

                    focusManager.clearFocus()

                    keyboardController?.hide()
                }
            )


            /*
             * Se for acidente,
             * mostramos uma orientação.
             */
            if (
                selectedProblemType ==
                "ACCIDENT"
            ) {

                Spacer(
                    modifier =
                        Modifier.height(14.dp)
                )

                Text(

                    text =
                        "Em situações com vítimas ou risco imediato, acione primeiro os serviços públicos de emergência.",

                    color =
                        GuinchouGray,

                    fontSize = 13.sp
                )
            }


            /*
             * =====================================
             * DETALHAMENTO
             * =====================================
             */
            if (
                selectedProblemType
                    .isNotBlank()
            ) {

                Spacer(
                    modifier =
                        Modifier.height(26.dp)
                )

                Text(
                    text =
                        "Selecione o problema",
                    color =
                        GuinchouWhite,
                    fontSize = 16.sp,
                    fontWeight =
                        FontWeight.SemiBold
                )

                Spacer(
                    modifier =
                        Modifier.height(12.dp)
                )

                detailOptions.forEach {
                        detail ->

                    ProblemDetailOption(

                        title = detail,

                        selected =
                            selectedProblemDetail ==
                                    detail,

                        onClick = {

                            selectedProblemDetail =
                                detail

                            errorMessage = null

                            focusManager.clearFocus()

                            keyboardController
                                ?.hide()
                        }
                    )

                    Spacer(
                        modifier =
                            Modifier.height(8.dp)
                    )
                }
            }


            Spacer(
                modifier =
                    Modifier.height(22.dp)
            )


            /*
             * =====================================
             * OBSERVAÇÃO
             * =====================================
             */
            OutlinedTextField(

                value =
                    description,

                onValueChange = {

                    description = it

                    errorMessage = null
                },

                modifier =
                    Modifier.fillMaxWidth(),

                label = {

                    Text(
                        text =
                            "Observações"
                    )
                },

                placeholder = {

                    Text(
                        text =
                            "Descreva informações importantes sobre a situação"
                    )
                },

                minLines = 3,

                maxLines = 5,

                keyboardOptions =
                    KeyboardOptions(
                        keyboardType =
                            KeyboardType.Text
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


            /*
             * =====================================
             * ERRO
             * =====================================
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
                modifier =
                    Modifier.height(20.dp)
            )
        }


        /*
         * =====================================
         * BOTÕES
         * =====================================
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

            OutlinedButton(

                onClick = {

                    focusManager.clearFocus()

                    keyboardController
                        ?.hide()

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
                    color =
                        GuinchouWhite
                )
            }


            Button(

                onClick = {

                    focusManager.clearFocus()

                    keyboardController
                        ?.hide()


                    /*
                     * Categoria obrigatória.
                     */
                    if (
                        selectedProblemType
                            .isBlank()
                    ) {

                        errorMessage =
                            "Selecione se o problema é mecânico ou acidente."

                        return@Button
                    }


                    /*
                     * Problema específico obrigatório.
                     */
                    if (
                        selectedProblemDetail
                            .isBlank()
                    ) {

                        errorMessage =
                            "Selecione o tipo de problema."

                        return@Button
                    }


                    onContinueClick(

                        selectedProblemType,

                        selectedProblemDetail,

                        description.trim()
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
 * Card da categoria principal.
 */
@Composable
private fun ProblemTypeCard(

    title: String,

    description: String,

    selected: Boolean,

    onClick: () -> Unit
) {

    Column(

        modifier = Modifier
            .fillMaxWidth()

            .background(
                color =
                    GuinchouSurface,
                shape =
                    RoundedCornerShape(
                        16.dp
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
                        16.dp
                    )
            )

            .clickable {
                onClick()
            }

            .padding(16.dp)
    ) {

        Text(
            text = title,
            color =
                GuinchouWhite,
            fontSize = 16.sp,
            fontWeight =
                FontWeight.SemiBold
        )

        Spacer(
            modifier =
                Modifier.height(5.dp)
        )

        Text(
            text = description,
            color =
                GuinchouGray,
            fontSize = 13.sp
        )
    }
}


/**
 * Opção específica do problema.
 */
@Composable
private fun ProblemDetailOption(

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
                        13.dp
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
                        13.dp
                    )
            )

            .clickable {
                onClick()
            }

            .padding(14.dp),

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
            fontSize = 14.sp
        )

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