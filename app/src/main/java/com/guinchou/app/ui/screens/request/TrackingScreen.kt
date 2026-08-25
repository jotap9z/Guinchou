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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.guinchou.app.model.TowRequestStatus
import com.guinchou.app.ui.theme.GuinchouBackground
import com.guinchou.app.ui.theme.GuinchouBorder
import com.guinchou.app.ui.theme.GuinchouGray
import com.guinchou.app.ui.theme.GuinchouGreen
import com.guinchou.app.ui.theme.GuinchouSurface
import com.guinchou.app.ui.theme.GuinchouWhite

/**
 * Tela de acompanhamento do atendimento.
 *
 * Ela reage ao status real do chamado.
 *
 * Futuramente esses estados serão atualizados
 * pelo backend quando o guincheiro realizar
 * cada ação em seu aplicativo.
 */
@Composable
fun TrackingScreen(

    pickupAddress: String,

    destinationAddress: String,

    driverName: String,

    towTruckDescription: String,

    towTruckPlate: String,

    estimatedArrivalMinutes: Int,

    driverRating: Double,

    requestStatus: TowRequestStatus,

    onCallClick: () -> Unit,

    onMessageClick: () -> Unit,

    /*
     * Temporário.
     *
     * Usado somente enquanto o aplicativo
     * do guincheiro/backend ainda não existem.
     */
    onAdvanceTestClick: () -> Unit,

    onCancelClick: () -> Unit
) {

    /*
     * =========================================
     * TEXTO DEPENDENTE DO STATUS
     * =========================================
     */

    val statusTitle =
        when (
            requestStatus
        ) {

            TowRequestStatus.ACCEPTED ->
                "Chamado aceito"

            TowRequestStatus.DRIVER_ON_THE_WAY ->
                "Guincho a caminho"

            TowRequestStatus.ARRIVED ->
                "Seu guincheiro chegou"

            TowRequestStatus.VEHICLE_LOADED ->
                "Veículo carregado"

            TowRequestStatus.IN_TRANSIT ->
                "A caminho do destino"

            else ->
                "Atendimento em andamento"
        }


    val statusDescription =
        when (
            requestStatus
        ) {

            TowRequestStatus.ACCEPTED ->
                "O parceiro aceitou seu chamado."

            TowRequestStatus.DRIVER_ON_THE_WAY ->
                "O guincheiro está indo até você."

            TowRequestStatus.ARRIVED ->
                "O guincheiro informou que chegou ao local."

            TowRequestStatus.VEHICLE_LOADED ->
                "Seu veículo foi colocado no guincho."

            TowRequestStatus.IN_TRANSIT ->
                "Seu veículo está sendo transportado até o destino."

            else ->
                "Acompanhe as atualizações do atendimento."
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
         * CONTEÚDO
         * =========================================
         */

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(
                    rememberScrollState()
                )
        ) {

            /*
             * =========================================
             * CABEÇALHO
             * =========================================
             */

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 20.dp,
                        vertical = 16.dp
                    )
            ) {

                Text(
                    text = statusTitle,
                    color = GuinchouWhite,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier =
                        Modifier.height(6.dp)
                )

                Text(
                    text = statusDescription,
                    color = GuinchouGray,
                    fontSize = 14.sp
                )
            }


            /*
             * =========================================
             * MAPA
             * =========================================
             */

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(270.dp)
                    .background(
                        GuinchouSurface
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
                            .size(18.dp)
                            .background(
                                color =
                                    GuinchouGreen,
                                shape =
                                    CircleShape
                            )
                    )

                    Spacer(
                        modifier =
                            Modifier.height(10.dp)
                    )

                    Text(
                        text =
                            when (
                                requestStatus
                            ) {

                                TowRequestStatus
                                    .DRIVER_ON_THE_WAY ->
                                    "Guincho se aproximando"

                                TowRequestStatus
                                    .ARRIVED ->
                                    "Guincho no local"

                                TowRequestStatus
                                    .VEHICLE_LOADED ->
                                    "Preparando transporte"

                                TowRequestStatus
                                    .IN_TRANSIT ->
                                    "Veículo em transporte"

                                else ->
                                    "Mapa"
                            },
                        color = GuinchouWhite,
                        fontSize = 16.sp,
                        fontWeight =
                            FontWeight.SemiBold
                    )

                    Spacer(
                        modifier =
                            Modifier.height(5.dp)
                    )

                    Text(
                        text =
                            "A localização em tempo real será exibida aqui.",
                        color =
                            GuinchouGray,
                        fontSize =
                            12.sp,
                        textAlign =
                            TextAlign.Center
                    )
                }
            }


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 20.dp,
                        vertical = 18.dp
                    )
            ) {

                /*
                 * =====================================
                 * STATUS
                 * =====================================
                 */

                StatusCard(
                    status =
                        requestStatus,
                    estimatedArrivalMinutes =
                        estimatedArrivalMinutes
                )


                Spacer(
                    modifier =
                        Modifier.height(18.dp)
                )


                /*
                 * =====================================
                 * GUINCHEIRO
                 * =====================================
                 */

                Text(
                    text = "Seu guincheiro",
                    color = GuinchouWhite,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier =
                        Modifier.height(10.dp)
                )


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
                        .padding(16.dp)
                ) {

                    Row(
                        modifier =
                            Modifier.fillMaxWidth(),
                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        /*
                         * Foto temporária.
                         */
                        Box(
                            modifier = Modifier
                                .size(58.dp)
                                .clip(
                                    CircleShape
                                )
                                .background(
                                    GuinchouBackground
                                )
                                .border(
                                    width = 1.dp,
                                    color =
                                        GuinchouGreen,
                                    shape =
                                        CircleShape
                                ),
                            contentAlignment =
                                Alignment.Center
                        ) {

                            Text(
                                text =
                                    driverName
                                        .firstOrNull()
                                        ?.uppercase()
                                        ?: "G",
                                color =
                                    GuinchouGreen,
                                fontSize =
                                    21.sp,
                                fontWeight =
                                    FontWeight.Bold
                            )
                        }


                        Spacer(
                            modifier =
                                Modifier.size(
                                    14.dp
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
                                    driverName,
                                color =
                                    GuinchouWhite,
                                fontSize =
                                    17.sp,
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
                                    "★ $driverRating",
                                color =
                                    GuinchouGreen,
                                fontSize =
                                    13.sp,
                                fontWeight =
                                    FontWeight.SemiBold
                            )
                        }
                    }


                    Spacer(
                        modifier =
                            Modifier.height(
                                16.dp
                            )
                    )


                    InfoLine(
                        label =
                            "Guincho",
                        value =
                            towTruckDescription
                    )


                    Spacer(
                        modifier =
                            Modifier.height(
                                10.dp
                            )
                    )


                    InfoLine(
                        label =
                            "Placa",
                        value =
                            towTruckPlate
                    )
                }


                Spacer(
                    modifier =
                        Modifier.height(
                            18.dp
                        )
                )


                /*
                 * =====================================
                 * CONTATO
                 * =====================================
                 */

                Text(
                    text =
                        "Fale com o guincheiro",
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
                        "O contato fica disponível enquanto o atendimento estiver ativo.",
                    color =
                        GuinchouGray,
                    fontSize =
                        13.sp
                )

                Spacer(
                    modifier =
                        Modifier.height(
                            12.dp
                        )
                )


                Row(
                    modifier =
                        Modifier.fillMaxWidth(),
                    horizontalArrangement =
                        Arrangement.spacedBy(
                            10.dp
                        )
                ) {

                    Button(
                        onClick =
                            onMessageClick,
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp),
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
                                "Mensagem",
                            fontWeight =
                                FontWeight.Bold
                        )
                    }


                    OutlinedButton(
                        onClick =
                            onCallClick,
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp),
                        shape =
                            RoundedCornerShape(
                                14.dp
                            )
                    ) {

                        Text(
                            text =
                                "Ligar",
                            color =
                                GuinchouWhite,
                            fontWeight =
                                FontWeight.SemiBold
                        )
                    }
                }


                Spacer(
                    modifier =
                        Modifier.height(
                            22.dp
                        )
                )


                /*
                 * =====================================
                 * TRAJETO
                 * =====================================
                 */

                Text(
                    text =
                        "Detalhes do chamado",
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
                            10.dp
                        )
                )


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
                        .padding(16.dp)
                ) {

                    AddressItem(
                        indicator =
                            "A",
                        title =
                            "Local do veículo",
                        address =
                            pickupAddress
                    )


                    if (
                        destinationAddress
                            .isNotBlank()
                    ) {

                        Spacer(
                            modifier =
                                Modifier.height(
                                    16.dp
                                )
                        )

                        AddressItem(
                            indicator =
                                "B",
                            title =
                                "Destino",
                            address =
                                destinationAddress
                        )
                    }
                }


                Spacer(
                    modifier =
                        Modifier.height(
                            20.dp
                        )
                )


                /*
                 * =====================================
                 * PROGRESSO
                 * =====================================
                 */

                Text(
                    text =
                        "Etapas do atendimento",
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
                            12.dp
                        )
                )


                ServiceProgress(
                    requestStatus =
                        requestStatus
                )


                Spacer(
                    modifier =
                        Modifier.height(
                            22.dp
                        )
                )


                /*
                 * =====================================
                 * CONTROLE TEMPORÁRIO DE TESTE
                 * =====================================
                 *
                 * Este botão NÃO existirá
                 * no app final do cliente.
                 *
                 * Futuramente:
                 *
                 * o guincheiro muda o status
                 * no app dele e o backend
                 * atualiza esta tela.
                 */

                Text(
                    text =
                        "Modo de desenvolvimento",
                    color =
                        GuinchouGray,
                    fontSize =
                        12.sp
                )


                Spacer(
                    modifier =
                        Modifier.height(
                            8.dp
                        )
                )


                OutlinedButton(
                    onClick =
                        onAdvanceTestClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(
                            52.dp
                        ),
                    shape =
                        RoundedCornerShape(
                            14.dp
                        )
                ) {

                    Text(
                        text =
                            nextTestButtonText(
                                requestStatus
                            ),
                        color =
                            GuinchouGreen,
                        fontWeight =
                            FontWeight.SemiBold
                    )
                }


                Spacer(
                    modifier =
                        Modifier.height(
                            18.dp
                        )
                )
            }
        }


        /*
         * =========================================
         * CANCELAMENTO
         * =========================================
         */

        if (
            requestStatus !=
            TowRequestStatus.IN_TRANSIT
        ) {

            OutlinedButton(
                onClick =
                    onCancelClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal =
                            20.dp,
                        vertical =
                            12.dp
                    )
                    .height(
                        52.dp
                    ),
                shape =
                    RoundedCornerShape(
                        14.dp
                    )
            ) {

                Text(
                    text =
                        "Cancelar chamado",
                    color =
                        GuinchouWhite
                )
            }
        }
    }
}


/**
 * =============================================
 * STATUS PRINCIPAL
 * =============================================
 */
@Composable
private fun StatusCard(
    status: TowRequestStatus,
    estimatedArrivalMinutes: Int
) {

    val mainText =
        when (
            status
        ) {

            TowRequestStatus.DRIVER_ON_THE_WAY ->
                "$estimatedArrivalMinutes min"

            TowRequestStatus.ARRIVED ->
                "Chegou"

            TowRequestStatus.VEHICLE_LOADED ->
                "Carregado"

            TowRequestStatus.IN_TRANSIT ->
                "Em transporte"

            else ->
                "Em andamento"
        }


    val label =
        when (
            status
        ) {

            TowRequestStatus.DRIVER_ON_THE_WAY ->
                "Previsão de chegada"

            TowRequestStatus.ARRIVED ->
                "Status do guincheiro"

            TowRequestStatus.VEHICLE_LOADED ->
                "Status do veículo"

            TowRequestStatus.IN_TRANSIT ->
                "Status da viagem"

            else ->
                "Status"
        }


    Row(
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
            ),
        verticalAlignment =
            Alignment.CenterVertically,
        horizontalArrangement =
            Arrangement.SpaceBetween
    ) {

        Column {

            Text(
                text =
                    label,
                color =
                    GuinchouGray,
                fontSize =
                    13.sp
            )

            Spacer(
                modifier =
                    Modifier.height(
                        5.dp
                    )
            )

            Text(
                text =
                    mainText,
                color =
                    GuinchouWhite,
                fontSize =
                    25.sp,
                fontWeight =
                    FontWeight.Bold
            )
        }


        Box(
            modifier = Modifier
                .size(
                    46.dp
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
                    19.sp,
                fontWeight =
                    FontWeight.Bold
            )
        }
    }
}


/**
 * =============================================
 * ETAPAS DO ATENDIMENTO
 * =============================================
 */
@Composable
private fun ServiceProgress(
    requestStatus: TowRequestStatus
) {

    val currentLevel =
        when (
            requestStatus
        ) {

            TowRequestStatus.DRIVER_ON_THE_WAY ->
                1

            TowRequestStatus.ARRIVED ->
                2

            TowRequestStatus.VEHICLE_LOADED ->
                3

            TowRequestStatus.IN_TRANSIT ->
                4

            TowRequestStatus.COMPLETED ->
                5

            else ->
                0
        }


    Column {

        ProgressItem(
            title =
                "Guincho a caminho",
            completed =
                currentLevel >= 1
        )

        ProgressItem(
            title =
                "Guincho chegou",
            completed =
                currentLevel >= 2
        )

        ProgressItem(
            title =
                "Veículo carregado",
            completed =
                currentLevel >= 3
        )

        ProgressItem(
            title =
                "Em transporte",
            completed =
                currentLevel >= 4
        )

        ProgressItem(
            title =
                "Atendimento concluído",
            completed =
                currentLevel >= 5
        )
    }
}


@Composable
private fun ProgressItem(
    title: String,
    completed: Boolean
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                vertical =
                    7.dp
            ),
        verticalAlignment =
            Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(
                    22.dp
                )
                .background(
                    color =
                        if (
                            completed
                        ) {
                            GuinchouGreen
                        } else {
                            GuinchouSurface
                        },
                    shape =
                        CircleShape
                )
                .border(
                    width =
                        1.dp,
                    color =
                        if (
                            completed
                        ) {
                            GuinchouGreen
                        } else {
                            GuinchouBorder
                        },
                    shape =
                        CircleShape
                ),
            contentAlignment =
                Alignment.Center
        ) {

            if (
                completed
            ) {

                Text(
                    text = "✓",
                    color =
                        GuinchouBackground,
                    fontSize =
                        11.sp,
                    fontWeight =
                        FontWeight.Bold
                )
            }
        }


        Spacer(
            modifier =
                Modifier.size(
                    12.dp
                )
        )


        Text(
            text =
                title,
            color =
                if (
                    completed
                ) {
                    GuinchouWhite
                } else {
                    GuinchouGray
                },
            fontSize =
                14.sp,
            fontWeight =
                if (
                    completed
                ) {
                    FontWeight.Medium
                } else {
                    FontWeight.Normal
                }
        )
    }
}


/**
 * =============================================
 * TEXTO DO BOTÃO DE TESTE
 * =============================================
 */
private fun nextTestButtonText(
    status: TowRequestStatus
): String {

    return when (
        status
    ) {

        TowRequestStatus.DRIVER_ON_THE_WAY ->
            "Simular chegada do guincheiro"

        TowRequestStatus.ARRIVED ->
            "Simular veículo carregado"

        TowRequestStatus.VEHICLE_LOADED ->
            "Simular início do transporte"

        TowRequestStatus.IN_TRANSIT ->
            "Simular conclusão do serviço"

        else ->
            "Avançar atendimento"
    }
}


/**
 * =============================================
 * INFORMAÇÃO DO GUINCHO
 * =============================================
 */
@Composable
private fun InfoLine(
    label: String,
    value: String
) {

    Row(
        modifier =
            Modifier.fillMaxWidth(),
        horizontalArrangement =
            Arrangement.SpaceBetween,
        verticalAlignment =
            Alignment.CenterVertically
    ) {

        Text(
            text =
                label,
            color =
                GuinchouGray,
            fontSize =
                13.sp
        )

        Text(
            text =
                value,
            color =
                GuinchouWhite,
            fontSize =
                13.sp,
            fontWeight =
                FontWeight.SemiBold,
            textAlign =
                TextAlign.End,
            modifier = Modifier
                .weight(
                    1f
                )
                .padding(
                    start =
                        18.dp
                )
        )
    }
}


/**
 * =============================================
 * ENDEREÇO
 * =============================================
 */
@Composable
private fun AddressItem(
    indicator: String,
    title: String,
    address: String
) {

    Row(
        modifier =
            Modifier.fillMaxWidth(),
        verticalAlignment =
            Alignment.Top
    ) {

        Box(
            modifier = Modifier
                .size(
                    28.dp
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
                text =
                    indicator,
                color =
                    GuinchouBackground,
                fontSize =
                    12.sp,
                fontWeight =
                    FontWeight.Bold
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
                    title,
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
                    address,
                color =
                    GuinchouWhite,
                fontSize =
                    14.sp,
                fontWeight =
                    FontWeight.Medium
            )
        }
    }
}