package com.guinchou.app.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.guinchou.app.ui.theme.GuinchouBackground
import com.guinchou.app.ui.theme.GuinchouBorder
import com.guinchou.app.ui.theme.GuinchouGray
import com.guinchou.app.ui.theme.GuinchouGreen
import com.guinchou.app.ui.theme.GuinchouSurface
import com.guinchou.app.ui.theme.GuinchouWhite

@Composable
fun ForgotPasswordScreen(

    onSendRecoveryClick: (
        email: String
    ) -> Unit = {},

    onBackToLoginClick: () -> Unit = {}
) {

    var email by remember {
        mutableStateOf("")
    }

    var errorMessage by remember {
        mutableStateOf<String?>(null)
    }

    val focusManager: FocusManager =
        LocalFocusManager.current

    val keyboardController =
        LocalSoftwareKeyboardController.current

    val backgroundInteractionSource =
        remember {
            MutableInteractionSource()
        }

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
            .verticalScroll(
                rememberScrollState()
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
            text = "G",
            color = GuinchouGreen,
            fontSize = 48.sp,
            fontWeight = FontWeight.Black
        )

        Text(
            text = "RECUPERAR SENHA",
            color = GuinchouWhite,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier =
                Modifier.height(8.dp)
        )

        Text(
            text =
                "Informe o e-mail cadastrado na sua conta. Enviaremos as instruções para redefinir sua senha.",
            color =
                GuinchouGray,
            fontSize =
                14.sp,
            textAlign =
                TextAlign.Center
        )

        Spacer(
            modifier =
                Modifier.height(28.dp)
        )

        OutlinedTextField(

            value = email,

            onValueChange = {

                email = it

                errorMessage = null
            },

            modifier =
                Modifier.fillMaxWidth(),

            label = {

                RequiredFieldLabel(
                    text = "E-mail"
                )
            },

            placeholder = {

                Text(
                    text =
                        "exemplo@email.com"
                )
            },

            singleLine = true,

            keyboardOptions =
                KeyboardOptions(
                    keyboardType =
                        KeyboardType.Email
                ),

            colors =
                recoveryFieldColors()
        )

        if (
            errorMessage != null
        ) {

            Spacer(
                modifier =
                    Modifier.height(10.dp)
            )

            Text(
                text =
                    errorMessage!!,
                color =
                    MaterialTheme
                        .colorScheme
                        .error,
                fontSize =
                    13.sp,
                modifier =
                    Modifier.fillMaxWidth()
            )
        }

        Spacer(
            modifier =
                Modifier.height(20.dp)
        )

        Button(

            onClick = {

                focusManager.clearFocus()

                keyboardController?.hide()

                if (
                    email.isBlank()
                ) {

                    errorMessage =
                        "Informe seu e-mail."

                    return@Button
                }

                if (
                    !isValidEmail(
                        email
                    )
                ) {

                    errorMessage =
                        "Informe um e-mail válido."

                    return@Button
                }

                onSendRecoveryClick(
                    email.trim()
                )
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
                    "Enviar instruções",
                fontWeight =
                    FontWeight.Bold,
                fontSize =
                    15.sp
            )
        }

        Spacer(
            modifier =
                Modifier.height(24.dp)
        )

        Row(
            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Text(
                text =
                    "Lembrou sua senha? ",
                color =
                    GuinchouGray,
                fontSize =
                    14.sp
            )

            Text(
                text =
                    "Entrar",
                color =
                    GuinchouGreen,
                fontSize =
                    14.sp,
                fontWeight =
                    FontWeight.Bold,
                modifier =
                    Modifier.clickable {

                        focusManager.clearFocus()

                        keyboardController?.hide()

                        onBackToLoginClick()
                    }
            )
        }
    }
}


@Composable
private fun RequiredFieldLabel(
    text: String
) {

    Text(
        text =
            buildAnnotatedString {

                append(text)

                append(" ")

                withStyle(
                    style =
                        SpanStyle(
                            color =
                                Color.Red,
                            fontWeight =
                                FontWeight.Bold
                        )
                ) {

                    append("*")
                }
            }
    )
}


@Composable
private fun recoveryFieldColors() =
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


private fun isValidEmail(
    email: String
): Boolean {

    val value =
        email.trim()

    if (
        value.isBlank()
    ) {
        return false
    }

    if (
        !value.contains("@")
    ) {
        return false
    }

    val parts =
        value.split("@")

    if (
        parts.size != 2
    ) {
        return false
    }

    if (
        parts[0].isBlank() ||
        parts[1].isBlank()
    ) {
        return false
    }

    if (
        !parts[1].contains(".")
    ) {
        return false
    }

    return true
}