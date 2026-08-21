package com.guinchou.app.ui.screens.request

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

/**
 * Etapa 5 de 5.
 *
 * Exibe o resumo completo da solicitação
 * e o preço calculado.
 *
 * Essa tela não possui campos de texto,
 * portanto não necessita controle
 * de teclado/foco.
 */
@Composable
fun EstimateScreen(

    pickupAddress: String,

    destinationAddress: String,

    vehicleType: String,

    vehicleBrand: String,

    vehicleModel: String,

    problemDetail: String,

    distanceKm: Double,

    servicePrice: Double,

    onContinueClick: () -> Unit,

    onBackClick: () -> Unit
) {

    /*
     * Formatação brasileira.
     *
     * Exemplo:
     *
     * 200.0
     *
     * vira:
     *
     * R$ 200,00
     */
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
    ) {

        /*
         * ===================================
         * CONTEÚDO ROLÁVEL
         * ===================================
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
             * Etapa.
             */
            Text(
                text = "5 de 5",
                color = GuinchouGray,
                fontSize = 13.sp
            )

            Spacer(
                modifier =
                    Modifier.height(8.dp)
            )

            /*
             * Título.
             */
            Text(
                text = "Confira sua estimativa",
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
                    "Revise os dados antes de continuar.",
                color =
                    GuinchouGray,
                fontSize = 14.sp
            )

            Spacer(
                modifier =
                    Modifier.height(26.dp)
            )


            /*
             * ===================================
             * VALOR PRINCIPAL
             * ===================================
             */
            Column(

                modifier = Modifier
                    .fillMaxWidth()

                    .background(
                        color =
                            GuinchouSurface,
                        shape =
                            RoundedCornerShape(
                                20.dp
                            )
                    )

                    .border(
                        width = 1.dp,
                        color =
                            GuinchouBorder,
                        shape =
                            RoundedCornerShape(
                                20.dp
                            )
                    )

                    .padding(22.dp)
            ) {

                Text(
                    text =
                        "Valor estimado",
                    color =
                        GuinchouGray,
                    fontSize = 13.sp
                )

                Spacer(
                    modifier =
                        Modifier.height(6.dp)
                )

                Text(

                    text =
                        currencyFormatter
                            .format(
                                servicePrice
                            ),

                    color =
                        GuinchouWhite,

                    fontSize = 34.sp,

                    fontWeight =
                        FontWeight.Bold
                )

                Spacer(
                    modifier =
                        Modifier.height(8.dp)
                )

                Text(
                    text =
                        "${formatDistance(distanceKm)} km de percurso",
                    color =
                        GuinchouGreen,
                    fontSize = 14.sp,
                    fontWeight =
                        FontWeight.SemiBold
                )
            }


            Spacer(
                modifier =
                    Modifier.height(20.dp)
            )


            /*
             * ===================================
             * TRAJETO
             * ===================================
             */
            EstimateSection(

                title = "Trajeto"
            ) {

                EstimateInformation(

                    label =
                        "Local de partida",

                    value =
                        pickupAddress
                )

                InformationDivider()

                EstimateInformation(

                    label = "Destino",

                    value =
                        destinationAddress
                )

                InformationDivider()

                EstimateInformation(

                    label =
                        "Distância estimada",

                    value =
                        "${formatDistance(distanceKm)} km"
                )
            }


            Spacer(
                modifier =
                    Modifier.height(16.dp)
            )


            /*
             * ===================================
             * VEÍCULO
             * ===================================
             */
            EstimateSection(

                title = "Veículo"
            ) {

                EstimateInformation(

                    label = "Tipo",

                    value =
                        vehicleType
                )

                InformationDivider()

                EstimateInformation(

                    label =
                        "Marca / modelo",

                    value =
                        "$vehicleBrand $vehicleModel"
                )
            }


            Spacer(
                modifier =
                    Modifier.height(16.dp)
            )


            /*
             * ===================================
             * PROBLEMA
             * ===================================
             */
            EstimateSection(

                title = "Atendimento"
            ) {

                EstimateInformation(

                    label =
                        "Problema informado",

                    value =
                        problemDetail
                )
            }


            Spacer(
                modifier =
                    Modifier.height(20.dp)
            )


            /*
             * Informação ao cliente.
             *
             * A divisão financeira entre
             * plataforma e parceiro NÃO precisa
             * aparecer para o cliente.
             */
            Text(

                text =
                    "O valor poderá ser atualizado caso o trajeto ou as condições do atendimento sejam alterados.",

                color =
                    GuinchouGray,

                fontSize = 12.sp,

                textAlign =
                    TextAlign.Center,

                modifier =
                    Modifier.fillMaxWidth()
            )


            Spacer(
                modifier =
                    Modifier.height(20.dp)
            )
        }


        /*
         * ===================================
         * BOTÕES
         * ===================================
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

                onClick =
                    onBackClick,

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


            /*
             * PAGAMENTO
             */
            Button(

                onClick =
                    onContinueClick,

                modifier = Modifier
                    .weight(1.7f)
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

                    text =
                        "Continuar para pagamento",

                    fontWeight =
                        FontWeight.Bold,

                    fontSize = 13.sp
                )
            }
        }
    }
}


/**
 * Card reutilizável para cada
 * grupo de informações.
 */
@Composable
private fun EstimateSection(

    title: String,

    content:
    @Composable () -> Unit
) {

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
                width = 1.dp,
                color =
                    GuinchouBorder,
                shape =
                    RoundedCornerShape(
                        18.dp
                    )
            )

            .padding(18.dp)
    ) {

        Text(
            text = title,
            color =
                GuinchouWhite,
            fontSize = 16.sp,
            fontWeight =
                FontWeight.Bold
        )

        Spacer(
            modifier =
                Modifier.height(14.dp)
        )

        content()
    }
}


/**
 * Linha de informação.
 */
@Composable
private fun EstimateInformation(

    label: String,

    value: String
) {

    Column(

        modifier =
            Modifier.fillMaxWidth()
    ) {

        Text(
            text = label,
            color =
                GuinchouGray,
            fontSize = 12.sp
        )

        Spacer(
            modifier =
                Modifier.height(3.dp)
        )

        Text(
            text = value,
            color =
                GuinchouWhite,
            fontSize = 14.sp,
            fontWeight =
                FontWeight.Medium
        )
    }
}


/**
 * Separador interno.
 */
@Composable
private fun InformationDivider() {

    Spacer(
        modifier =
            Modifier.height(12.dp)
    )

    HorizontalDivider(
        color =
            GuinchouBorder
    )

    Spacer(
        modifier =
            Modifier.height(12.dp)
    )
}


/**
 * Evita mostrar:
 *
 * 18.0 km
 *
 * quando podemos mostrar:
 *
 * 18 km
 *
 * Mas mantém:
 *
 * 18.5 km
 */
private fun formatDistance(
    distance: Double
): String {

    return if (
        distance % 1.0 == 0.0
    ) {

        distance
            .toInt()
            .toString()

    } else {

        String.format(
            Locale(
                "pt",
                "BR"
            ),
            "%.1f",
            distance
        )
    }
}