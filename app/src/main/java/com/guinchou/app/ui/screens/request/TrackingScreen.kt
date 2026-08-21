package com.guinchou.app.ui.screens.request

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
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
 * Tela de acompanhamento após
 * um parceiro aceitar a corrida.
 *
 * Mais adiante:
 *
 * - posição real do guincho;
 * - ETA;
 * - rota;
 * - ligação;
 * - chat;
 * - atualizações em tempo real.
 */
@Composable
fun TrackingScreen(

    pickupAddress: String,

    driverName: String,

    towTruckDescription: String,

    towTruckPlate: String,

    estimatedArrivalMinutes: Int,

    onCallClick: () -> Unit,

    onMessageClick: () -> Unit,

    onCancelClick: () -> Unit
) {

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
         * =========================================
         * MAPA
         * =========================================
         */
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(
                    GuinchouSurface
                )
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        20.dp
                    ),
                horizontalAlignment =
                    Alignment.CenterHorizontally,
                verticalArrangement =
                    Arrangement.Center
            ) {

                /*
                 * Guincho temporário.
                 */
                Box(
                    modifier = Modifier
                        .size(
                            22.dp
                        )
                        .background(
                            color =
                                GuinchouGreen,
                            shape =
                                CircleShape
                        )
                )

                Spacer(
                    modifier =
                        Modifier.height(
                            12.dp
                        )
                )

                Text(
                    text =
                        "Guincho a caminho",
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
                        "A posição do parceiro aparecerá aqui em tempo real.",
                    color =
                        GuinchouGray,
                    fontSize =
                        13.sp,
                    textAlign =
                        TextAlign.Center
                )

                Spacer(
                    modifier =
                        Modifier.height(
                            28.dp
                        )
                )

                /*
                 * Rota simulada:
                 *
                 * guincho -------- cliente
                 */
                Row(
                    modifier =
                        Modifier.fillMaxWidth(),
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Box(
                        modifier = Modifier
                            .size(
                                14.dp
                            )
                            .background(
                                color =
                                    GuinchouGreen,
                                shape =
                                    CircleShape
                            )
                    )

                    HorizontalDivider(
                        modifier = Modifier
                            .weight(
                                1f
                            )
                            .padding(
                                horizontal =
                                    8.dp
                            ),
                        color =
                            GuinchouBorder
                    )

                    Box(
                        modifier = Modifier
                            .size(
                                14.dp
                            )
                            .background(
                                color =
                                    GuinchouWhite,
                                shape =
                                    CircleShape
                            )
                    )
                }
            }

            /*
             * ETA.
             */
            Column(
                modifier = Modifier
                    .align(
                        Alignment.TopCenter
                    )
                    .fillMaxWidth()
                    .padding(
                        16.dp
                    )
                    .background(
                        color =
                            GuinchouBackground,
                        shape =
                            RoundedCornerShape(
                                16.dp
                            )
                    )
                    .border(
                        width =
                            1.dp,
                        color =
                            GuinchouBorder,
                        shape =
                            RoundedCornerShape(
                                16.dp
                            )
                    )
                    .padding(
                        14.dp
                    )
            ) {

                Text(
                    text =
                        "Guincho encontrado",
                    color =
                        GuinchouGreen,
                    fontSize =
                        13.sp,
                    fontWeight =
                        FontWeight.Bold
                )

                Spacer(
                    modifier =
                        Modifier.height(
                            4.dp
                        )
                )

                Text(
                    text =
                        "Chegada estimada em $estimatedArrivalMinutes min",
                    color =
                        GuinchouWhite,
                    fontSize =
                        17.sp,
                    fontWeight =
                        FontWeight.SemiBold
                )
            }
        }

        /*
         * =========================================
         * PARCEIRO
         * =========================================
         */
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    GuinchouBackground
                )
                .padding(
                    20.dp
                )
        ) {

            Text(
                text =
                    "Seu guincheiro",
                color =
                    GuinchouGray,
                fontSize =
                    12.sp
            )

            Spacer(
                modifier =
                    Modifier.height(
                        6.dp
                    )
            )

            Text(
                text =
                    driverName,
                color =
                    GuinchouWhite,
                fontSize =
                    21.sp,
                fontWeight =
                    FontWeight.Bold
            )

            Spacer(
                modifier =
                    Modifier.height(
                        14.dp
                    )
            )

            DriverInformation(
                label =
                    "Guincho",
                value =
                    towTruckDescription
            )

            Spacer(
                modifier =
                    Modifier.height(
                        8.dp
                    )
            )

            DriverInformation(
                label =
                    "Placa",
                value =
                    towTruckPlate
            )

            Spacer(
                modifier =
                    Modifier.height(
                        8.dp
                    )
            )

            DriverInformation(
                label =
                    "Destino inicial",
                value =
                    pickupAddress
            )

            Spacer(
                modifier =
                    Modifier.height(
                        18.dp
                    )
            )

            /*
             * CONTATO
             */
            Row(
                modifier =
                    Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.spacedBy(
                        12.dp
                    )
            ) {

                Button(
                    onClick =
                        onCallClick,
                    modifier = Modifier
                        .weight(
                            1f
                        )
                        .height(
                            50.dp
                        ),
                    shape =
                        RoundedCornerShape(
                            14.dp
                        ),
                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor =
                                GuinchouSurface,
                            contentColor =
                                GuinchouWhite
                        )
                ) {

                    Text(
                        text = "Ligar"
                    )
                }

                Button(
                    onClick =
                        onMessageClick,
                    modifier = Modifier
                        .weight(
                            1f
                        )
                        .height(
                            50.dp
                        ),
                    shape =
                        RoundedCornerShape(
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
                        text =
                            "Mensagem",
                        fontWeight =
                            FontWeight.Bold
                    )
                }
            }

            Spacer(
                modifier =
                    Modifier.height(
                        12.dp
                    )
            )

            Button(
                onClick =
                    onCancelClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(
                        50.dp
                    ),
                shape =
                    RoundedCornerShape(
                        14.dp
                    ),
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor =
                            GuinchouBackground,
                        contentColor =
                            GuinchouGray
                    )
            ) {

                Text(
                    text =
                        "Cancelar chamado"
                )
            }
        }
    }
}


@Composable
private fun DriverInformation(
    label: String,
    value: String
) {

    Row(
        modifier =
            Modifier.fillMaxWidth(),
        horizontalArrangement =
            Arrangement.SpaceBetween
    ) {

        Text(
            text =
                label,
            color =
                GuinchouGray,
            fontSize =
                12.sp
        )

        Text(
            text =
                value,
            color =
                GuinchouWhite,
            fontSize =
                13.sp,
            fontWeight =
                FontWeight.Medium,
            textAlign =
                TextAlign.End
        )
    }
}