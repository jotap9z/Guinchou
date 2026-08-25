package com.guinchou.app.ui.screens.request

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
 * Tela exibida ao término do atendimento.
 *
 * Futuramente a avaliação será persistida
 * no backend e vinculada ao motorista,
 * veículo e corrida.
 */
@Composable
fun CompletedScreen(

    driverName: String,

    pickupAddress: String,

    destinationAddress: String,

    servicePrice: Double,

    onFinishClick: (
        rating: Int
    ) -> Unit
) {

    var rating by remember {
        mutableIntStateOf(0)
    }


    val currencyFormatter =
        NumberFormat
            .getCurrencyInstance(
                Locale(
                    "pt",
                    "BR"
                )
            )


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                GuinchouBackground
            )
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(
                horizontal =
                    20.dp,
                vertical =
                    20.dp
            )
    ) {

        /*
         * =========================================
         * CONTEÚDO
         * =========================================
         */

        Column(
            modifier = Modifier
                .weight(
                    1f
                )
                .fillMaxWidth(),
            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            Spacer(
                modifier =
                    Modifier.height(
                        30.dp
                    )
            )


            /*
             * CHECK
             */
            Box(
                modifier = Modifier
                    .size(
                        72.dp
                    )
                    .background(
                        color =
                            GuinchouGreen,
                        shape =
                            CircleShape
                    ),
                contentAlignment =
                    Alignment.Center
            ) {

                Text(
                    text = "✓",
                    color =
                        GuinchouBackground,
                    fontSize =
                        34.sp,
                    fontWeight =
                        FontWeight.Bold
                )
            }


            Spacer(
                modifier =
                    Modifier.height(
                        20.dp
                    )
            )


            Text(
                text =
                    "Atendimento concluído",
                color =
                    GuinchouWhite,
                fontSize =
                    27.sp,
                fontWeight =
                    FontWeight.Bold
            )


            Spacer(
                modifier =
                    Modifier.height(
                        8.dp
                    )
            )


            Text(
                text =
                    "Seu veículo chegou ao destino.",
                color =
                    GuinchouGray,
                fontSize =
                    14.sp
            )


            Spacer(
                modifier =
                    Modifier.height(
                        28.dp
                    )
            )


            /*
             * =========================================
             * RESUMO
             * =========================================
             */

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color =
                            GuinchouSurface,
                        shape =
                            RoundedCornerShape(
                                18.dp
                            )
                    )
                    .border(
                        width =
                            1.dp,
                        color =
                            GuinchouBorder,
                        shape =
                            RoundedCornerShape(
                                18.dp
                            )
                    )
                    .padding(
                        18.dp
                    )
            ) {

                SummaryLine(
                    label =
                        "Guincheiro",
                    value =
                        driverName
                )

                Spacer(
                    modifier =
                        Modifier.height(
                            12.dp
                        )
                )

                SummaryLine(
                    label =
                        "Partida",
                    value =
                        pickupAddress
                )

                Spacer(
                    modifier =
                        Modifier.height(
                            12.dp
                        )
                )

                SummaryLine(
                    label =
                        "Destino",
                    value =
                        destinationAddress
                )

                Spacer(
                    modifier =
                        Modifier.height(
                            12.dp
                        )
                )

                SummaryLine(
                    label =
                        "Valor",
                    value =
                        currencyFormatter
                            .format(
                                servicePrice
                            )
                )
            }


            Spacer(
                modifier =
                    Modifier.height(
                        28.dp
                    )
            )


            /*
             * =========================================
             * AVALIAÇÃO
             * =========================================
             */

            Text(
                text =
                    "Como foi o atendimento?",
                color =
                    GuinchouWhite,
                fontSize =
                    19.sp,
                fontWeight =
                    FontWeight.Bold
            )


            Spacer(
                modifier =
                    Modifier.height(
                        6.dp
                    )
            )


            Text(
                text =
                    "Sua avaliação ajuda a manter a qualidade dos parceiros.",
                color =
                    GuinchouGray,
                fontSize =
                    13.sp
            )


            Spacer(
                modifier =
                    Modifier.height(
                        18.dp
                    )
            )


            Row {

                for (
                star in 1..5
                ) {

                    Text(
                        text =
                            if (
                                star <= rating
                            ) {
                                "★"
                            } else {
                                "☆"
                            },
                        color =
                            if (
                                star <= rating
                            ) {
                                GuinchouGreen
                            } else {
                                GuinchouGray
                            },
                        fontSize =
                            36.sp,
                        modifier =
                            Modifier
                                .clickable {
                                    rating =
                                        star
                                }
                                .padding(
                                    horizontal =
                                        5.dp
                                )
                    )
                }
            }
        }


        /*
         * =========================================
         * FINALIZAR
         * =========================================
         */

        Button(
            onClick = {

                onFinishClick(
                    rating
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(
                    54.dp
                ),
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
                text =
                    if (
                        rating > 0
                    ) {
                        "Enviar avaliação"
                    } else {
                        "Concluir"
                    },
                fontWeight =
                    FontWeight.Bold
            )
        }
    }
}


@Composable
private fun SummaryLine(
    label: String,
    value: String
) {

    Column(
        modifier =
            Modifier.fillMaxWidth()
    ) {

        Text(
            text =
                label,
            color =
                GuinchouGray,
            fontSize =
                12.sp
        )

        Spacer(
            modifier =
                Modifier.height(
                    3.dp
                )
        )

        Text(
            text =
                value,
            color =
                GuinchouWhite,
            fontSize =
                14.sp,
            fontWeight =
                FontWeight.Medium
        )
    }
}