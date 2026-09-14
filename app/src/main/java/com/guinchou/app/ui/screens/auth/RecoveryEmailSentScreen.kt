package com.guinchou.app.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.guinchou.app.ui.theme.GuinchouBackground
import com.guinchou.app.ui.theme.GuinchouGray
import com.guinchou.app.ui.theme.GuinchouGreen
import com.guinchou.app.ui.theme.GuinchouWhite

@Composable
fun RecoveryEmailSentScreen(
    email: String = "",
    onBackToLoginClick: () -> Unit = {},
    onResendClick: () -> Unit = {}
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                GuinchouBackground
            )
            .padding(
                horizontal = 24.dp,
                vertical = 28.dp
            ),
        horizontalAlignment =
            Alignment.CenterHorizontally,
        verticalArrangement =
            Arrangement.Center
    ) {

        Text(
            text = "✓",
            color = GuinchouGreen,
            fontSize = 64.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier =
                Modifier.height(18.dp)
        )

        Text(
            text =
                "E-MAIL ENVIADO",
            color =
                GuinchouWhite,
            fontSize =
                26.sp,
            fontWeight =
                FontWeight.Bold
        )

        Spacer(
            modifier =
                Modifier.height(12.dp)
        )

        Text(
            text =
                "Enviamos as instruções para redefinir sua senha.",
            color =
                GuinchouGray,
            fontSize =
                15.sp,
            textAlign =
                TextAlign.Center
        )

        if (
            email.isNotBlank()
        ) {

            Spacer(
                modifier =
                    Modifier.height(10.dp)
            )

            Text(
                text = email,
                color =
                    GuinchouGreen,
                fontSize =
                    14.sp,
                fontWeight =
                    FontWeight.SemiBold,
                textAlign =
                    TextAlign.Center
            )
        }

        Spacer(
            modifier =
                Modifier.height(16.dp)
        )

        Text(
            text =
                "Verifique também a caixa de spam ou lixo eletrônico caso não encontre a mensagem.",
            color =
                GuinchouGray,
            fontSize =
                13.sp,
            textAlign =
                TextAlign.Center
        )

        Spacer(
            modifier =
                Modifier.height(30.dp)
        )

        Button(
            onClick = {
                onBackToLoginClick()
            },
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(56.dp),
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
                    "Voltar para o login",
                fontWeight =
                    FontWeight.Bold,
                fontSize =
                    15.sp
            )
        }

        Spacer(
            modifier =
                Modifier.height(22.dp)
        )

        Text(
            text =
                "Não recebeu o e-mail? Reenviar",
            color =
                GuinchouGreen,
            fontSize =
                14.sp,
            fontWeight =
                FontWeight.Bold,
            modifier =
                Modifier.clickable {
                    onResendClick()
                }
        )
    }
}