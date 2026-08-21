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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.Alignment
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
 * Etapa 2 de 5 da solicitação de guincho.
 *
 * O cliente informa para onde o veículo
 * deverá ser transportado.
 */
@Composable
fun DestinationScreen(
    onContinueClick: (String) -> Unit,
    onBackClick: () -> Unit
) {

    /*
     * Endereço digitado pelo usuário.
     */
    var destinationAddress by remember {
        mutableStateOf("")
    }

    /*
     * Mensagem de validação.
     */
    var errorMessage by remember {
        mutableStateOf<String?>(null)
    }

    /*
     * Controla o foco atual da interface.
     */
    val focusManager: FocusManager =
        LocalFocusManager.current

    /*
     * Controla o teclado virtual.
     */
    val keyboardController =
        LocalSoftwareKeyboardController.current

    /*
     * Remove o efeito visual de clique
     * no fundo da tela.
     */
    val backgroundInteractionSource =
        remember {
            MutableInteractionSource()
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                GuinchouBackground
            )

            /*
             * Ao tocar fora de um campo:
             *
             * - remove o foco;
             * - fecha o teclado;
             * - mantém o texto preenchido.
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
             * Protege a área superior.
             */
            .statusBarsPadding()

            /*
             * Protege a área inferior.
             */
            .navigationBarsPadding()
    ) {

        /*
         * Conteúdo rolável.
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
                text = "2 de 5",
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
                text = "Para onde o veículo será levado?",
                color = GuinchouWhite,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            /*
             * Descrição.
             */
            Text(
                text = "Informe o endereço exato do destino.",
                color = GuinchouGray,
                fontSize = 14.sp
            )

            Spacer(
                modifier = Modifier.height(28.dp)
            )

            /*
             * ========================================
             * CAMPO DE DESTINO
             * ========================================
             */
            OutlinedTextField(
                value = destinationAddress,

                onValueChange = {

                    destinationAddress = it

                    /*
                     * Remove erro quando
                     * o usuário começa a corrigir.
                     */
                    errorMessage = null
                },

                modifier =
                    Modifier.fillMaxWidth(),

                label = {
                    Text(
                        text = "Destino"
                    )
                },

                placeholder = {
                    Text(
                        text = "Digite o endereço completo"
                    )
                },

                singleLine = false,

                keyboardOptions =
                    KeyboardOptions(
                        keyboardType =
                            KeyboardType.Text
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

            /*
             * Mensagem de erro.
             */
            if (errorMessage != null) {

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text = errorMessage!!,
                    color =
                        MaterialTheme
                            .colorScheme
                            .error,
                    fontSize = 13.sp
                )
            }

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            /*
             * ========================================
             * MAPA TEMPORÁRIO
             * ========================================
             */
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .background(
                        color = GuinchouSurface,
                        shape = RoundedCornerShape(
                            18.dp
                        )
                    )
                    .border(
                        width = 1.dp,
                        color = GuinchouBorder,
                        shape = RoundedCornerShape(
                            18.dp
                        )
                    ),

                contentAlignment =
                    Alignment.Center
            ) {

                Column(
                    horizontalAlignment =
                        Alignment.CenterHorizontally
                ) {

                    /*
                     * Marcador visual.
                     */
                    Box(
                        modifier = Modifier
                            .height(18.dp)
                            .fillMaxWidth(0.05f)
                            .background(
                                color = GuinchouGreen,
                                shape = CircleShape
                            )
                    )

                    Spacer(
                        modifier = Modifier.height(10.dp)
                    )

                    Text(
                        text = "Destino",
                        color = GuinchouWhite,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(
                        modifier = Modifier.height(4.dp)
                    )

                    Text(
                        text =
                            "O destino será exibido no mapa",
                        color = GuinchouGray,
                        fontSize = 13.sp,
                        textAlign =
                            TextAlign.Center
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            /*
             * ========================================
             * DESTINOS RECENTES
             * ========================================
             */
            Text(
                text = "Destinos recentes",
                color = GuinchouWhite,
                fontSize = 16.sp,
                fontWeight =
                    FontWeight.SemiBold
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            /*
             * Opção temporária 1.
             */
            DestinationOption(
                title = "Minha casa",
                subtitle = "Endereço salvo",
                onClick = {

                    destinationAddress =
                        "Minha casa"

                    /*
                     * Remove foco caso o teclado
                     * ainda esteja aberto.
                     */
                    focusManager.clearFocus()
                    keyboardController?.hide()
                }
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            /*
             * Opção temporária 2.
             */
            DestinationOption(
                title = "Oficina parceira",
                subtitle = "Destino frequente",
                onClick = {

                    destinationAddress =
                        "Oficina parceira"

                    focusManager.clearFocus()
                    keyboardController?.hide()
                }
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )
        }

        /*
         * ========================================
         * BOTÕES INFERIORES
         * ========================================
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

                    /*
                     * Fecha teclado antes
                     * de navegar.
                     */
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
                     * Fecha teclado primeiro.
                     */
                    focusManager.clearFocus()
                    keyboardController?.hide()

                    /*
                     * Valida destino.
                     */
                    if (
                        destinationAddress.isBlank()
                    ) {

                        errorMessage =
                            "Informe o destino do veículo."

                        return@Button
                    }

                    /*
                     * Envia destino para
                     * a próxima etapa.
                     */
                    onContinueClick(
                        destinationAddress.trim()
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
 * Card reutilizável para destinos recentes.
 */
@Composable
private fun DestinationOption(
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = GuinchouSurface,
                shape =
                    RoundedCornerShape(
                        14.dp
                    )
            )
            .border(
                width = 1.dp,
                color = GuinchouBorder,
                shape =
                    RoundedCornerShape(
                        14.dp
                    )
            )
            .clickable {

                onClick()
            }
            .padding(14.dp),

        verticalAlignment =
            Alignment.CenterVertically
    ) {

        /*
         * Marcador visual.
         */
        Box(
            modifier = Modifier
                .height(12.dp)
                .fillMaxWidth(0.035f)
                .background(
                    color = GuinchouGreen,
                    shape = CircleShape
                )
        )

        Spacer(
            modifier =
                Modifier.padding(
                    horizontal = 6.dp
                )
        )

        Column {

            Text(
                text = title,
                color = GuinchouWhite,
                fontSize = 14.sp,
                fontWeight =
                    FontWeight.SemiBold
            )

            Text(
                text = subtitle,
                color = GuinchouGray,
                fontSize = 12.sp
            )
        }
    }
}