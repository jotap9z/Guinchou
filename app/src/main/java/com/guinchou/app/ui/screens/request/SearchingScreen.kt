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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import java.text.NumberFormat
import java.util.Locale
import kotlinx.coroutines.delay

/**
 * Tela exibida enquanto procuramos
 * um parceiro disponível.
 *
 * Neste protótipo:
 *
 * após 6 segundos simulamos que
 * um guincheiro aceitou o chamado.
 *
 * Futuramente esse evento virá
 * do backend em tempo real.
 */
@Composable
fun SearchingScreen(

    pickupAddress: String,

    destinationAddress: String,

    vehicleBrand: String,

    vehicleModel: String,

    problemDetail: String,

    servicePrice: Double,

    onTowFound: () -> Unit,

    onCancelClick: () -> Unit
) {

    val currencyFormatter =
        NumberFormat.getCurrencyInstance(
            Locale(
                "pt",
                "BR"
            )
        )

    /*
     * =========================================
     * SIMULAÇÃO TEMPORÁRIA
     * =========================================
     *
     * Aguarda 6 segundos e simula
     * que algum parceiro aceitou.
     *
     * Depois removeremos isso.
     */
    LaunchedEffect(Unit) {

        delay(
            6000
        )

        onTowFound()
    }

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

                Box(
                    modifier = Modifier
                        .size(
                            18.dp
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
                        "Mapa em tempo real",
                    color =
                        GuinchouWhite,
                    fontSize =
                        18.sp,
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
                        "Aqui aparecerão sua localização, destino e guinchos próximos.",
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
                            .weight(1f)
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

                Spacer(
                    modifier =
                        Modifier.height(
                            12.dp
                        )
                )

                Text(
                    text =
                        "Buscando parceiros próximos...",
                    color =
                        GuinchouGray,
                    fontSize =
                        12.sp
                )
            }

            /*
             * =========================================
             * STATUS
             * =========================================
             */
            Row(
                modifier = Modifier
                    .align(
                        Alignment.TopCenter
                    )
                    .fillMaxWidth()
                    .padding(
                        horizontal =
                            16.dp,
                        vertical =
                            14.dp
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
                        horizontal =
                            16.dp,
                        vertical =
                            14.dp
                    ),
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                /*
                 * Área fixa.
                 *
                 * O loading gira dentro dela,
                 * sem deslocar o texto.
                 */
                Box(
                    modifier =
                        Modifier.size(
                            28.dp
                        ),
                    contentAlignment =
                        Alignment.Center
                ) {

                    CircularProgressIndicator(
                        modifier =
                            Modifier.size(
                                22.dp
                            ),
                        color =
                            GuinchouGreen,
                        strokeWidth =
                            3.dp
                    )
                }

                Spacer(
                    modifier =
                        Modifier.size(
                            12.dp
                        )
                )

                Column(
                    modifier =
                        Modifier.weight(
                            1f
                        )
                ) {

                    Text(
                        text =
                            "Procurando um guincho",
                        color =
                            GuinchouWhite,
                        fontSize =
                            15.sp,
                        fontWeight =
                            FontWeight.SemiBold
                    )

                    Spacer(
                        modifier =
                            Modifier.height(
                                2.dp
                            )
                    )

                    Text(
                        text =
                            "Buscando o parceiro mais próximo",
                        color =
                            GuinchouGray,
                        fontSize =
                            12.sp
                    )
                }
            }
        }

        /*
         * =========================================
         * PAINEL INFERIOR
         * =========================================
         */
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    GuinchouBackground
                )
                .padding(
                    horizontal =
                        20.dp,
                    vertical =
                        16.dp
                )
        ) {

            Box(
                modifier = Modifier
                    .align(
                        Alignment.CenterHorizontally
                    )
                    .fillMaxWidth(
                        0.13f
                    )
                    .height(
                        4.dp
                    )
                    .background(
                        color =
                            GuinchouBorder,
                        shape =
                            RoundedCornerShape(
                                50.dp
                            )
                    )
            )

            Spacer(
                modifier =
                    Modifier.height(
                        16.dp
                    )
            )

            Text(
                text =
                    "Seu chamado",
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
                        14.dp
                    )
            )

            SearchInformation(
                label =
                    "Partida",
                value =
                    pickupAddress
            )

            Spacer(
                modifier =
                    Modifier.height(
                        10.dp
                    )
            )

            SearchInformation(
                label =
                    "Destino",
                value =
                    destinationAddress
            )

            Spacer(
                modifier =
                    Modifier.height(
                        10.dp
                    )
            )

            SearchInformation(
                label =
                    "Veículo",
                value =
                    "$vehicleBrand $vehicleModel"
            )

            Spacer(
                modifier =
                    Modifier.height(
                        10.dp
                    )
            )

            SearchInformation(
                label =
                    "Problema",
                value =
                    problemDetail
            )

            Spacer(
                modifier =
                    Modifier.height(
                        10.dp
                    )
            )

            SearchInformation(
                label =
                    "Valor estimado",
                value =
                    currencyFormatter.format(
                        servicePrice
                    )
            )

            Spacer(
                modifier =
                    Modifier.height(
                        18.dp
                    )
            )

            Button(
                onClick =
                    onCancelClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(
                        52.dp
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
                    text =
                        "Cancelar solicitação",
                    fontWeight =
                        FontWeight.Medium
                )
            }
        }
    }
}


@Composable
private fun SearchInformation(
    label: String,
    value: String
) {

    Row(
        modifier =
            Modifier.fillMaxWidth(),
        horizontalArrangement =
            Arrangement.SpaceBetween,
        verticalAlignment =
            Alignment.Top
    ) {

        Text(
            text =
                label,
            color =
                GuinchouGray,
            fontSize =
                12.sp,
            modifier =
                Modifier.weight(
                    0.35f
                )
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
                TextAlign.End,
            modifier =
                Modifier.weight(
                    0.65f
                )
        )
    }
}