package com.guinchou.app.ui.screens.request

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.guinchou.app.ui.theme.GuinchouBackground
import com.guinchou.app.ui.theme.GuinchouBorder
import com.guinchou.app.ui.theme.GuinchouGray
import com.guinchou.app.ui.theme.GuinchouGreen
import com.guinchou.app.ui.theme.GuinchouSurface
import com.guinchou.app.ui.theme.GuinchouWhite

@Composable
fun RequestConfirmationScreen(
    pickupAddress: String,
    destinationAddress: String,
    vehicleDescription: String,
    problemDescription: String,
    photosRequired: Boolean,
    submitting: Boolean,
    errorMessage: String?,
    createdRequestId: String?,
    onConfirmClick: () -> Unit,
    onBackClick: () -> Unit,
    onHomeClick: () -> Unit
) {
    BackHandler(enabled = submitting || createdRequestId != null) {
        // Evita sair durante o envio ou retornar à confirmação após criar.
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GuinchouBackground)
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (createdRequestId != null) {
            Text(
                text = "Chamado registrado",
                color = GuinchouWhite,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Sua solicitação foi salva no Supabase com status CREATED.",
                color = GuinchouGray,
                fontSize = 15.sp
            )

            SummaryItem(
                label = "Identificador do chamado",
                value = createdRequestId
            )

            Text(
                text = "A busca por guincheiro e o pagamento ainda não foram ativados para este chamado.",
                color = GuinchouGray,
                fontSize = 13.sp
            )

            Button(
                onClick = onHomeClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = GuinchouGreen,
                    contentColor = GuinchouBackground
                )
            ) {
                Text(
                    text = "Voltar para a Home",
                    fontWeight = FontWeight.Bold
                )
            }

            return@Column
        }

        Text(
            text = "Confirmar solicitação",
            color = GuinchouWhite,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Confira os dados antes de registrar o chamado.",
            color = GuinchouGray,
            fontSize = 14.sp
        )

        SummaryItem("Origem", pickupAddress)
        SummaryItem("Destino", destinationAddress)
        SummaryItem(
            "Veículo",
            vehicleDescription.ifBlank { "Não informado" }
        )
        SummaryItem(
            "Problema",
            problemDescription.ifBlank { "Não informado" }
        )

        Text(
            text = "Nesta etapa, o chamado será registrado sem cobrança. O preço definitivo ainda não é calculado pelo servidor.",
            color = GuinchouGray,
            fontSize = 13.sp
        )

        if (photosRequired) {
            Text(
                text = "Chamados de acidente ainda não podem ser enviados: as fotos selecionadas precisam ser integradas ao armazenamento antes do registro.",
                color = Color(0xFFFFC107),
                fontSize = 13.sp
            )
        }

        if (errorMessage != null) {
            Text(
                text = errorMessage,
                color = Color(0xFFFF8A80),
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onBackClick,
                enabled = !submitting,
                modifier = Modifier
                    .weight(1f)
                    .height(54.dp)
            ) {
                Text("Voltar")
            }

            Button(
                onClick = onConfirmClick,
                enabled = !submitting && !photosRequired,
                modifier = Modifier
                    .weight(1.6f)
                    .height(54.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = GuinchouGreen,
                    contentColor = GuinchouBackground
                )
            ) {
                if (submitting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = GuinchouBackground,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "Registrar chamado",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun SummaryItem(
    label: String,
    value: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = GuinchouSurface,
                shape = RoundedCornerShape(14.dp)
            )
            .border(
                width = 1.dp,
                color = GuinchouBorder,
                shape = RoundedCornerShape(14.dp)
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = label,
            color = GuinchouGray,
            fontSize = 12.sp
        )

        Text(
            text = value,
            color = GuinchouWhite,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}