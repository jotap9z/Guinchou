package com.guinchou.app.ui.screens.payment

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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
    onBackClick: () -> Unit,
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
            Locale.forLanguageTag("pt-BR")
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

        PaymentHeader(
            onBackClick = onBackClick
        )

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
                text = "Como deseja pagar?",
                color = GuinchouWhite,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = "Escolha a forma de pagamento para confirmar sua solicitação.",
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
                iconType = "PIX",
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
                iconType = "CARD",
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
                iconType = "CARD",
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
                    "Seus dados de pagamento serão protegidos durante o processamento da solicitação.",
                color = GuinchouGray,
                fontSize = 12.sp
            )
        }

        HorizontalDivider(
            color = GuinchouBorder
        )

        Button(
            onClick = {
                if (selectedPaymentMethod.isNotBlank()) {
                    onConfirmPaymentClick(
                        selectedPaymentMethod
                    )
                }
            },
            enabled = selectedPaymentMethod.isNotBlank(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 20.dp,
                    vertical = 12.dp
                )
                .height(54.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = GuinchouGreen,
                contentColor = GuinchouBackground,
                disabledContainerColor = GuinchouBorder,
                disabledContentColor = GuinchouGray
            )
        ) {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )

            Spacer(
                modifier = Modifier.size(8.dp)
            )

            Text(
                text = "Confirmar pagamento",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
            )
        }
    }
}


@Composable
private fun PaymentHeader(
    onBackClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 8.dp,
                vertical = 7.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBackClick
        ) {
            Icon(
                imageVector =
                    Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Voltar",
                tint = GuinchouWhite
            )
        }

        Text(
            text = "Pagamento",
            color = GuinchouWhite,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}


/**
 * Opção reutilizável de pagamento.
 */
@Composable
private fun PaymentOption(
    title: String,
    description: String,
    iconType: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color =
                    if (selected) {
                        GuinchouGreen.copy(
                            alpha = 0.07f
                        )
                    } else {
                        GuinchouSurface
                    },
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                width = if (selected) 2.dp else 1.dp,
                color =
                    if (selected) {
                        GuinchouGreen
                    } else {
                        GuinchouBorder
                    },
                shape = RoundedCornerShape(16.dp)
            )
            .clickable {
                onClick()
            }
            .padding(15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(46.dp)
                .background(
                    color =
                        if (selected) {
                            GuinchouGreen.copy(
                                alpha = 0.12f
                            )
                        } else {
                            GuinchouBackground
                        },
                    shape = RoundedCornerShape(13.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector =
                    if (iconType == "PIX") {
                        Icons.Default.AccountBalance
                    } else {
                        Icons.Default.CreditCard
                    },
                contentDescription = null,
                tint =
                    if (selected) {
                        GuinchouGreen
                    } else {
                        GuinchouGray
                    },
                modifier = Modifier.size(23.dp)
            )
        }

        Spacer(
            modifier = Modifier.size(12.dp)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                color = GuinchouWhite,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
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

        if (selected) {
            Spacer(
                modifier = Modifier.size(10.dp)
            )

            Box(
                modifier = Modifier
                    .size(27.dp)
                    .background(
                        GuinchouGreen,
                        RoundedCornerShape(50)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Selecionado",
                    tint = GuinchouBackground,
                    modifier = Modifier.size(17.dp)
                )
            }
        }
    }
}

