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
 * Etapa 1 de 5 da solicitação de guincho.
 *
 * O cliente informa o local exato
 * onde o veículo está.
 */
@Composable
fun PickupScreen(
    onContinueClick: (String) -> Unit,
    onBackClick: () -> Unit
) {

    /*
     * Guarda o endereço digitado.
     */
    var pickupAddress by remember {
        mutableStateOf("")
    }

    /*
     * Guarda possíveis mensagens de erro.
     */
    var errorMessage by remember {
        mutableStateOf<String?>(null)
    }

    /*
     * Controla qual campo está com foco.
     *
     * Quando chamamos clearFocus(),
     * o campo deixa de ficar selecionado.
     */
    val focusManager: FocusManager =
        LocalFocusManager.current

    /*
     * Controla o teclado virtual do Android.
     */
    val keyboardController =
        LocalSoftwareKeyboardController.current

    /*
     * Utilizado para remover o efeito visual
     * de clique do fundo da tela.
     */
    val backgroundInteractionSource =
        remember {
            MutableInteractionSource()
        }

    /*
     * ========================================
     * CONTAINER PRINCIPAL
     * ========================================
     *
     * Ao tocar em uma área livre da tela:
     *
     * 1. remove o foco do campo;
     * 2. fecha o teclado;
     * 3. mantém o texto digitado.
     */
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                GuinchouBackground
            )
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
         * ========================================
         * CONTEÚDO PRINCIPAL
         * ========================================
         *
         * Continua rolável para funcionar
         * corretamente em celulares menores.
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
             * Indicador de progresso.
             */
            Text(
                text = "1 de 5",
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
                text = "Onde está o veículo?",
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
                text = "Informe o local exato onde o veículo está.",
                color = GuinchouGray,
                fontSize = 14.sp
            )

            Spacer(
                modifier = Modifier.height(28.dp)
            )

            /*
             * ========================================
             * CAMPO DE ENDEREÇO
             * ========================================
             */
            OutlinedTextField(
                value = pickupAddress,

                onValueChange = {

                    pickupAddress = it

                    /*
                     * Remove a mensagem de erro
                     * quando o usuário volta a digitar.
                     */
                    errorMessage = null
                },

                modifier = Modifier.fillMaxWidth(),

                label = {
                    Text(
                        text = "Local de partida"
                    )
                },

                placeholder = {
                    Text(
                        text = "Digite o endereço completo"
                    )
                },

                singleLine = false,

                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text
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
             * ========================================
             * ERRO DE VALIDAÇÃO
             * ========================================
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
             * LOCALIZAÇÃO ATUAL
             * ========================================
             *
             * Ainda será conectado ao GPS
             * posteriormente.
             */
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = GuinchouSurface,
                        shape = RoundedCornerShape(
                            16.dp
                        )
                    )
                    .border(
                        width = 1.dp,
                        color = GuinchouBorder,
                        shape = RoundedCornerShape(
                            16.dp
                        )
                    )
                    .padding(16.dp),

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                /*
                 * Marcador temporário.
                 */
                Box(
                    modifier = Modifier
                        .height(16.dp)
                        .fillMaxWidth(0.035f)
                        .background(
                            color = GuinchouGreen,
                            shape = CircleShape
                        )
                )

                Spacer(
                    modifier = Modifier.padding(
                        horizontal = 6.dp
                    )
                )

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text =
                            "Usar minha localização atual",
                        color = GuinchouWhite,
                        fontSize = 14.sp,
                        fontWeight =
                            FontWeight.SemiBold
                    )

                    Spacer(
                        modifier = Modifier.height(3.dp)
                    )

                    Text(
                        text =
                            "O GPS será integrado nesta opção.",
                        color = GuinchouGray,
                        fontSize = 12.sp
                    )
                }
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
                        text = "Mapa",
                        color = GuinchouWhite,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(
                        modifier = Modifier.height(4.dp)
                    )

                    Text(
                        text =
                            "Confirme o local exato do veículo no mapa",
                        color = GuinchouGray,
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center,
                        modifier =
                            Modifier.padding(
                                horizontal = 20.dp
                            )
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            Text(
                text =
                    "Confirme se este é o local exato onde o veículo está.",
                color = GuinchouGray,
                fontSize = 13.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
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
                Arrangement.spacedBy(12.dp)
        ) {

            /*
             * VOLTAR
             */
            OutlinedButton(
                onClick = {

                    /*
                     * Fecha o teclado antes
                     * de voltar.
                     */
                    focusManager.clearFocus()
                    keyboardController?.hide()

                    onBackClick()
                },

                modifier = Modifier
                    .weight(1f)
                    .height(54.dp),

                shape =
                    RoundedCornerShape(14.dp)
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
                     * Fecha o teclado primeiro.
                     */
                    focusManager.clearFocus()
                    keyboardController?.hide()

                    /*
                     * Validação.
                     */
                    if (pickupAddress.isBlank()) {

                        errorMessage =
                            "Informe o local exato de partida."

                        return@Button
                    }

                    /*
                     * Envia o endereço preenchido
                     * para a navegação.
                     */
                    onContinueClick(
                        pickupAddress.trim()
                    )
                },

                modifier = Modifier
                    .weight(1.4f)
                    .height(54.dp),

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
                    text = "Continuar",
                    fontWeight =
                        FontWeight.Bold
                )
            }
        }
    }
}