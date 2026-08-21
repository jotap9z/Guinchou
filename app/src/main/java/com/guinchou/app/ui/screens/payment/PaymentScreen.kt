package com.guinchou.app.ui.screens.payment

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.guinchou.app.ui.theme.GuinchouBackground
import com.guinchou.app.ui.theme.GuinchouBorder
import com.guinchou.app.ui.theme.GuinchouGray
import com.guinchou.app.ui.theme.GuinchouGreen
import com.guinchou.app.ui.theme.GuinchouSurface
import com.guinchou.app.ui.theme.GuinchouWhite
import java.text.NumberFormat
import java.util.Locale

/**
 * Tela de escolha da forma de pagamento.
 *
 * Neste momento o pagamento ainda é apenas visual.
 *
 * Posteriormente será integrado com um gateway real.
 */
@Composable
fun PaymentScreen(
    servicePrice: Double,
    onConfirmPaymentClick: (String) -> Unit,
    onBackClick: () -> Unit
) {

    /*
     * Forma de pagamento selecionada.
     */
    var selectedPaymentMethod by remember {
        mutableStateOf("")
    }

    /*
     * Formatação em Real.
     */
    val currencyFormatter =
        NumberFormat.getCurrencyInstance(
            Locale("pt", "BR")
        )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                GuinchouBackground
            )
            .statusBarsPadding()
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

            Text(
                text = "Pagamento",
                color = GuinchouWhite,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = "Escolha como deseja pagar pelo atendimento.",
                color = GuinchouGray,
                fontSize = 14.sp
            )

            Spacer(
                modifier = Modifier.height(26.dp)
            )

            /*
             * Valor total.
             */
            Column(
                modifier = Modifier
                    .fillMaxWidth()
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
                    )
                    .padding(20.dp)
            ) {

                Text(
                    text = "Valor do atendimento",
                    color = GuinchouGray,
                    fontSize = 13.sp
                )

                Spacer(
                    modifier = Modifier.height(6.dp)
                )

                Text(
                    text = currencyFormatter.format(
                        servicePrice
                    ),
                    color = GuinchouWhite,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(
                modifier = Modifier.height(26.dp)
            )

            Text(
                text = "Forma de pagamento",
                color = GuinchouWhite,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            PaymentOption(
                title = "PIX",
                description = "Pagamento instantâneo",
                selected =
                    selectedPaymentMethod == "PIX",
                onClick = {

                    selectedPaymentMethod = "PIX"
                }
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            PaymentOption(
                title = "Cartão de crédito",
                description = "Visa, Mastercard e outros",
                selected =
                    selectedPaymentMethod == "CREDIT_CARD",
                onClick = {

                    selectedPaymentMethod =
                        "CREDIT_CARD"
                }
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            PaymentOption(
                title = "Cartão de débito",
                description = "Pagamento pelo cartão",
                selected =
                    selectedPaymentMethod == "DEBIT_CARD",
                onClick = {

                    selectedPaymentMethod =
                        "DEBIT_CARD"
                }
            )

            Spacer(
                modifier = Modifier.height(26.dp)
            )

            Text(
                text =
                    "O pagamento real será processado de forma segura pelo gateway de pagamento quando integrarmos o backend.",
                color = GuinchouGray,
                fontSize = 12.sp
            )
        }

        /*
         * Botões inferiores.
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
                onClick = onBackClick,
                modifier = Modifier
                    .weight(1f)
                    .height(54.dp),
                shape = RoundedCornerShape(
                    14.dp
                )
            ) {

                Text(
                    text = "Voltar",
                    color = GuinchouWhite
                )
            }

            Button(
                onClick = {

                    /*
                     * Só avança se houver
                     * forma de pagamento selecionada.
                     */
                    if (
                        selectedPaymentMethod
                            .isNotBlank()
                    ) {

                        onConfirmPaymentClick(
                            selectedPaymentMethod
                        )
                    }
                },
                modifier = Modifier
                    .weight(1.7f)
                    .height(54.dp),
                shape = RoundedCornerShape(
                    14.dp
                ),
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor =
                            GuinchouGreen,
                        contentColor =
                            GuinchouBackground
                    )
            ) {

                Text(
                    text = "Confirmar pagamento",
                    fontWeight =
                        FontWeight.Bold,
                    fontSize = 13.sp
                )
            }
        }
    }
}


/**
 * Opção reutilizável de pagamento.
 */
@Composable
private fun PaymentOption(
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
                    if (selected) {
                        GuinchouSurface
                    } else {
                        GuinchouBackground
                    },
                shape = RoundedCornerShape(
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
                shape = RoundedCornerShape(
                    14.dp
                )
            )
            .clickable {
                onClick()
            }
            .padding(16.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.SpaceBetween
        ) {

            Text(
                text = title,
                color = GuinchouWhite,
                fontSize = 15.sp,
                fontWeight =
                    FontWeight.SemiBold
            )

            if (selected) {

                Text(
                    text = "✓",
                    color = GuinchouGreen,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(
            modifier = Modifier.height(4.dp)
        )

        Text(
            text = description,
            color = GuinchouGray,
            fontSize = 12.sp
        )
    }
}