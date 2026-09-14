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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
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
fun CreateAccountScreen(

    onCreateAccountClick: (
        fullName: String,
        cpf: String,
        phone: String,
        email: String,
        password: String
    ) -> Unit = { _, _, _, _, _ -> },

    onLoginClick: () -> Unit = {}
) {

    var fullName by remember {
        mutableStateOf("")
    }

    var cpf by remember {
        mutableStateOf("")
    }

    var phone by remember {
        mutableStateOf("")
    }

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var confirmPassword by remember {
        mutableStateOf("")
    }

    var passwordVisible by remember {
        mutableStateOf(false)
    }

    var confirmPasswordVisible by remember {
        mutableStateOf(false)
    }

    var acceptedTerms by remember {
        mutableStateOf(false)
    }

    var errorMessage by remember {
        mutableStateOf<String?>(null)
    }


    /*
     * =========================================
     * REGRAS DA SENHA
     * =========================================
     */

    val hasMinimumCharacters =
        password.length >= 6

    val hasNumber =
        password.any {
            it.isDigit()
        }

    val hasSpecialCharacter =
        password.any {
            !it.isLetterOrDigit()
        }

    val isPasswordValid =
        hasMinimumCharacters &&
                hasNumber &&
                hasSpecialCharacter


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


        /*
         * =========================================
         * CABEÇALHO
         * =========================================
         */

        Text(
            text = "G",
            color = GuinchouGreen,
            fontSize = 48.sp,
            fontWeight = FontWeight.Black
        )

        Text(
            text = "CRIAR CONTA",
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
                "Cadastre seus dados para solicitar serviços de guincho.",
            color = GuinchouGray,
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )

        Spacer(
            modifier =
                Modifier.height(26.dp)
        )


        /*
         * =========================================
         * NOME
         * =========================================
         */

        OutlinedTextField(

            value = fullName,

            onValueChange = {
                fullName = it
                errorMessage = null
            },

            modifier =
                Modifier.fillMaxWidth(),

            label = {
                RequiredFieldLabel(
                    text = "Nome completo"
                )
            },

            placeholder = {
                Text(
                    text = "Digite seu nome"
                )
            },

            singleLine = true,

            keyboardOptions =
                KeyboardOptions(
                    keyboardType =
                        KeyboardType.Text
                ),

            colors =
                authFieldColors()
        )

        Spacer(
            modifier =
                Modifier.height(14.dp)
        )


        /*
         * =========================================
         * CPF
         * =========================================
         */

        OutlinedTextField(

            value = cpf,

            onValueChange = {
                cpf = formatCpf(it)
                errorMessage = null
            },

            modifier =
                Modifier.fillMaxWidth(),

            label = {
                RequiredFieldLabel(
                    text = "CPF"
                )
            },

            placeholder = {
                Text(
                    text = "000.000.000-00"
                )
            },

            singleLine = true,

            keyboardOptions =
                KeyboardOptions(
                    keyboardType =
                        KeyboardType.Number
                ),

            colors =
                authFieldColors()
        )

        Spacer(
            modifier =
                Modifier.height(14.dp)
        )


        /*
         * =========================================
         * TELEFONE
         * =========================================
         */

        OutlinedTextField(

            value = phone,

            onValueChange = {
                phone = formatPhone(it)
                errorMessage = null
            },

            modifier =
                Modifier.fillMaxWidth(),

            label = {
                RequiredFieldLabel(
                    text = "Telefone"
                )
            },

            placeholder = {
                Text(
                    text = "(61) 99999-9999"
                )
            },

            singleLine = true,

            keyboardOptions =
                KeyboardOptions(
                    keyboardType =
                        KeyboardType.Phone
                ),

            colors =
                authFieldColors()
        )

        Spacer(
            modifier =
                Modifier.height(14.dp)
        )


        /*
         * =========================================
         * E-MAIL
         * =========================================
         */

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
                    text = "exemplo@email.com"
                )
            },

            singleLine = true,

            keyboardOptions =
                KeyboardOptions(
                    keyboardType =
                        KeyboardType.Email
                ),

            colors =
                authFieldColors()
        )

        Spacer(
            modifier =
                Modifier.height(14.dp)
        )


        /*
         * =========================================
         * SENHA
         * =========================================
         */

        OutlinedTextField(

            value = password,

            onValueChange = {
                password = it
                errorMessage = null
            },

            modifier =
                Modifier.fillMaxWidth(),

            label = {
                RequiredFieldLabel(
                    text = "Senha"
                )
            },

            singleLine = true,

            visualTransformation =
                if (passwordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },

            trailingIcon = {

                TextButton(
                    onClick = {

                        passwordVisible =
                            !passwordVisible
                    }
                ) {

                    Text(
                        text =
                            if (passwordVisible) {
                                "Ocultar"
                            } else {
                                "Mostrar"
                            },
                        color =
                            GuinchouGreen,
                        fontSize =
                            12.sp
                    )
                }
            },

            keyboardOptions =
                KeyboardOptions(
                    keyboardType =
                        KeyboardType.Password
                ),

            colors =
                authFieldColors()
        )


        /*
         * =========================================
         * REQUISITOS DA SENHA
         * =========================================
         */

        Spacer(
            modifier =
                Modifier.height(8.dp)
        )

        Column(
            modifier =
                Modifier.fillMaxWidth()
        ) {

            Text(
                text =
                    "Sua senha deve conter:",
                color =
                    GuinchouGray,
                fontSize =
                    12.sp,
                fontWeight =
                    FontWeight.Medium
            )

            Spacer(
                modifier =
                    Modifier.height(5.dp)
            )

            PasswordRequirement(
                text =
                    "Mínimo de 6 caracteres",
                isValid =
                    hasMinimumCharacters
            )

            PasswordRequirement(
                text =
                    "Pelo menos 1 número",
                isValid =
                    hasNumber
            )

            PasswordRequirement(
                text =
                    "Pelo menos 1 caractere especial",
                isValid =
                    hasSpecialCharacter
            )
        }

        Spacer(
            modifier =
                Modifier.height(14.dp)
        )


        /*
         * =========================================
         * CONFIRMAR SENHA
         * =========================================
         */

        OutlinedTextField(

            value = confirmPassword,

            onValueChange = {
                confirmPassword = it
                errorMessage = null
            },

            modifier =
                Modifier.fillMaxWidth(),

            label = {
                RequiredFieldLabel(
                    text = "Confirmar senha"
                )
            },

            singleLine = true,

            visualTransformation =
                if (confirmPasswordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },

            trailingIcon = {

                TextButton(
                    onClick = {

                        confirmPasswordVisible =
                            !confirmPasswordVisible
                    }
                ) {

                    Text(
                        text =
                            if (confirmPasswordVisible) {
                                "Ocultar"
                            } else {
                                "Mostrar"
                            },
                        color =
                            GuinchouGreen,
                        fontSize =
                            12.sp
                    )
                }
            },

            keyboardOptions =
                KeyboardOptions(
                    keyboardType =
                        KeyboardType.Password
                ),

            colors =
                authFieldColors()
        )


        /*
         * =========================================
         * ERRO
         * =========================================
         */

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


        /*
         * =========================================
         * TERMOS
         * =========================================
         */

        Spacer(
            modifier =
                Modifier.height(14.dp)
        )

        Row(
            modifier =
                Modifier.fillMaxWidth(),
            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Checkbox(
                checked =
                    acceptedTerms,
                onCheckedChange = {

                    acceptedTerms = it
                    errorMessage = null
                },
                colors =
                    CheckboxDefaults.colors(
                        checkedColor =
                            GuinchouGreen,
                        uncheckedColor =
                            GuinchouBorder,
                        checkmarkColor =
                            GuinchouBackground
                    )
            )

            Text(
                text =
                    buildAnnotatedString {

                        append(
                            "Aceito os Termos de Uso e a Política de Privacidade "
                        )

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
                    },
                color =
                    GuinchouGray,
                fontSize =
                    13.sp,
                modifier =
                    Modifier.weight(1f)
            )
        }


        /*
         * =========================================
         * BOTÃO CRIAR CONTA
         * =========================================
         */

        Spacer(
            modifier =
                Modifier.height(18.dp)
        )

        Button(

            onClick = {

                focusManager.clearFocus()
                keyboardController?.hide()


                if (
                    fullName.isBlank()
                ) {

                    errorMessage =
                        "Informe seu nome completo."

                    return@Button
                }


                if (
                    fullName
                        .trim()
                        .length < 3
                ) {

                    errorMessage =
                        "Informe um nome válido."

                    return@Button
                }


                if (
                    cpf.filter {
                        it.isDigit()
                    }.length != 11
                ) {

                    errorMessage =
                        "Informe um CPF válido."

                    return@Button
                }


                if (
                    phone.filter {
                        it.isDigit()
                    }.length < 10
                ) {

                    errorMessage =
                        "Informe um telefone válido."

                    return@Button
                }


                if (
                    email.isBlank() ||
                    !email.contains("@")
                ) {

                    errorMessage =
                        "Informe um e-mail válido."

                    return@Button
                }


                if (
                    !hasMinimumCharacters
                ) {

                    errorMessage =
                        "A senha deve possuir no mínimo 6 caracteres."

                    return@Button
                }


                if (
                    !hasNumber
                ) {

                    errorMessage =
                        "A senha deve possuir pelo menos 1 número."

                    return@Button
                }


                if (
                    !hasSpecialCharacter
                ) {

                    errorMessage =
                        "A senha deve possuir pelo menos 1 caractere especial."

                    return@Button
                }


                if (
                    !isPasswordValid
                ) {

                    errorMessage =
                        "A senha não atende aos requisitos de segurança."

                    return@Button
                }


                if (
                    confirmPassword.isBlank()
                ) {

                    errorMessage =
                        "Confirme sua senha."

                    return@Button
                }


                if (
                    password !=
                    confirmPassword
                ) {

                    errorMessage =
                        "As senhas não são iguais."

                    return@Button
                }


                if (
                    !acceptedTerms
                ) {

                    errorMessage =
                        "Você precisa aceitar os Termos de Uso e a Política de Privacidade."

                    return@Button
                }


                onCreateAccountClick(
                    fullName.trim(),
                    cpf.filter {
                        it.isDigit()
                    },
                    phone.filter {
                        it.isDigit()
                    },
                    email.trim(),
                    password
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
                    "Criar conta",
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

        Row(
            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Text(
                text =
                    "Já possui uma conta? ",
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

                        onLoginClick()
                    }
            )
        }
    }
}


/*
 * =============================================
 * LABEL PARA CAMPOS OBRIGATÓRIOS
 * =============================================
 */

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


/*
 * =============================================
 * INDICADOR DOS REQUISITOS DA SENHA
 * =============================================
 */

@Composable
private fun PasswordRequirement(
    text: String,
    isValid: Boolean
) {

    Text(
        text =
            if (isValid) {
                "✓ $text"
            } else {
                "• $text"
            },
        color =
            if (isValid) {
                GuinchouGreen
            } else {
                GuinchouGray
            },
        fontSize =
            12.sp,
        modifier =
            Modifier.padding(
                vertical = 2.dp
            )
    )
}


/*
 * =============================================
 * CORES DOS CAMPOS
 * =============================================
 */

@Composable
private fun authFieldColors() =
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


/*
 * =============================================
 * FORMATAÇÃO DO CPF
 * =============================================
 */

private fun formatCpf(
    value: String
): String {

    val digits =
        value
            .filter {
                it.isDigit()
            }
            .take(11)

    return when {

        digits.length <= 3 ->
            digits

        digits.length <= 6 ->
            "${digits.substring(0, 3)}." +
                    digits.substring(3)

        digits.length <= 9 ->
            "${digits.substring(0, 3)}." +
                    "${digits.substring(3, 6)}." +
                    digits.substring(6)

        else ->
            "${digits.substring(0, 3)}." +
                    "${digits.substring(3, 6)}." +
                    "${digits.substring(6, 9)}-" +
                    digits.substring(9)
    }
}


/*
 * =============================================
 * FORMATAÇÃO DO TELEFONE
 * =============================================
 */

private fun formatPhone(
    value: String
): String {

    val digits =
        value
            .filter {
                it.isDigit()
            }
            .take(11)

    return when {

        digits.length <= 2 ->
            digits

        digits.length <= 6 ->
            "(${digits.substring(0, 2)}) " +
                    digits.substring(2)

        digits.length <= 10 ->
            "(${digits.substring(0, 2)}) " +
                    "${digits.substring(2, 6)}-" +
                    digits.substring(6)

        else ->
            "(${digits.substring(0, 2)}) " +
                    "${digits.substring(2, 7)}-" +
                    digits.substring(7)
    }
}