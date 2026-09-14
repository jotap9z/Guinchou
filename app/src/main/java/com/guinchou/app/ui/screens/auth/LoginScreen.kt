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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
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
 * Tela principal de autenticação do Guinchou.
 *
 * O usuário poderá entrar utilizando:
 *
 * - e-mail + senha;
 * - telefone + senha;
 * - Google.
 *
 * Ao tocar fora dos campos:
 *
 * - o teclado fecha;
 * - o campo perde o foco;
 * - os dados continuam preenchidos.
 */
@Composable
fun LoginScreen(

    onLoginClick: (
        identifier: String,
        password: String,
    ) -> Unit = { _, _ -> },

    onGoogleClick: () -> Unit = {},

    onCreateAccountClick: () -> Unit = {},

    onForgotPasswordClick: () -> Unit = {},

    onPartnerClick: () -> Unit = {},

    isLoading: Boolean = false,

    externalErrorMessage: String? = null,
) {

    /*
     * E-mail ou telefone.
     */
    var identifier by remember {
        mutableStateOf("")
    }

    /*
     * Senha.
     */
    var password by remember {
        mutableStateOf("")
    }

    /*
     * Controla visualização da senha.
     */
    var passwordVisible by remember {
        mutableStateOf(value = false)
    }

    /*
     * Mensagem de validação.
     */
    var errorMessage by remember {
        mutableStateOf<String?>(null)
    }

    /*
     * Controla o foco dos campos.
     */
    val focusManager: FocusManager =
        LocalFocusManager.current

    /*
     * Controla o teclado do Android.
     */
    val keyboardController =
        LocalSoftwareKeyboardController.current

    /*
     * Evita efeito visual ao tocar
     * no fundo da tela.
     */
    val backgroundInteractionSource =
        remember {
            MutableInteractionSource()
        }

    Column(

        modifier = Modifier
            .fillMaxSize()
            .background(
                GuinchouBackground,
            )

            /*
             * Ao tocar fora dos campos:
             *
             * remove o foco
             * e fecha o teclado.
             */
            .clickable(
                interactionSource =
                    backgroundInteractionSource,
                indication = null,
            ) {

                focusManager.clearFocus()

                keyboardController?.hide()
            }

            /*
             * Permite rolagem em celulares
             * menores quando o teclado estiver aberto.
             */
            .verticalScroll(
                rememberScrollState(),
            )

            .padding(
                horizontal = 24.dp,
                vertical = 28.dp,
            ),

        horizontalAlignment =
            Alignment.CenterHorizontally,

        verticalArrangement =
            Arrangement.Center,
    ) {

        /*
         * ===================================
         * IDENTIDADE
         * ===================================
         */

        Text(
            text = "G",
            color = GuinchouGreen,
            fontSize = 52.sp,
            fontWeight = FontWeight.Black,
        )

        Text(
            text = "GUINCHOU",
            color = GuinchouWhite,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
        )

        Spacer(
            modifier =
                Modifier.height(30.dp),
        )

        Text(
            text = "Bem-vindo de volta",
            color = GuinchouWhite,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
        )

        Spacer(
            modifier =
                Modifier.height(6.dp),
        )

        Text(
            text =
                "Entre para solicitar seu guincho.",
            color = GuinchouGray,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
        )

        Spacer(
            modifier =
                Modifier.height(28.dp)
        )


        /*
         * ===================================
         * E-MAIL OU TELEFONE
         * ===================================
         */

        OutlinedTextField(

            value = identifier,

            onValueChange = {

                identifier = it

                errorMessage = null
            },

            modifier =
                Modifier.fillMaxWidth(),

            label = {

                Text(
                    text = "E-mail ou telefone"
                )
            },

            placeholder = {

                Text(
                    text =
                        "exemplo@email.com ou telefone"
                )
            },

            singleLine = true,

            keyboardOptions =
                KeyboardOptions(
                    keyboardType =
                        KeyboardType.Text
                ),

            colors =
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
        )

        Spacer(
            modifier =
                Modifier.height(14.dp)
        )


        /*
         * ===================================
         * SENHA
         * ===================================
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

                Text(
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

            /*
             * Mostrar / ocultar senha.
             */
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

                        fontSize = 12.sp
                    )
                }
            },

            keyboardOptions =
                KeyboardOptions(
                    keyboardType =
                        KeyboardType.Password
                ),

            colors =
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
        )


        /*
         * ===================================
         * ERRO
         * ===================================
         */

        val displayError = externalErrorMessage ?: errorMessage

        if (displayError != null) {

            Spacer(
                modifier =
                    Modifier.height(8.dp)
            )

            Text(

                text =
                    displayError,

                color =
                    MaterialTheme
                        .colorScheme
                        .error,

                fontSize = 13.sp,

                modifier =
                    Modifier.fillMaxWidth()
            )
        }


        Spacer(
            modifier =
                Modifier.height(8.dp)
        )


        /*
         * ===================================
         * ESQUECI MINHA SENHA
         * ===================================
         */

        Text(

            text =
                "Esqueci minha senha",

            color =
                GuinchouGreen,

            fontSize = 14.sp,

            fontWeight =
                FontWeight.Medium,

            modifier =
                Modifier
                    .align(
                        Alignment.End
                    )
                    .clickable {

                        /*
                         * Fecha teclado antes
                         * da navegação.
                         */
                        focusManager.clearFocus()

                        keyboardController?.hide()

                        onForgotPasswordClick()
                    }
        )


        Spacer(
            modifier =
                Modifier.height(20.dp)
        )


        /*
         * ===================================
         * ENTRAR
         * ===================================
         */

        Button(

            onClick = {

                /*
                 * Fecha teclado primeiro.
                 */
                focusManager.clearFocus()

                keyboardController?.hide()


                /*
                 * Validação do identificador.
                 */
                if (
                    identifier.isBlank()
                ) {

                    errorMessage =
                        "Informe seu e-mail ou telefone."

                    return@Button
                }


                /*
                 * Validação da senha.
                 */
                if (
                    password.isBlank()
                ) {

                    errorMessage =
                        "Informe sua senha."

                    return@Button
                }


                /*
                 * Regra temporária.
                 */
                if (
                    password.length < 6
                ) {

                    errorMessage =
                        "A senha deve possuir pelo menos 6 caracteres."

                    return@Button
                }


                /*
                 * Realiza o login.
                 */
                onLoginClick(

                    identifier.trim(),

                    password
                )
            },

            enabled = !isLoading,

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
                text = if (isLoading) "Carregando..." else "Entrar",
                fontWeight =
                    FontWeight.Bold,
                fontSize = 15.sp
            )
        }


        Spacer(
            modifier =
                Modifier.height(22.dp)
        )


        /*
         * ===================================
         * DIVISOR
         * ===================================
         */

        Row(

            modifier =
                Modifier.fillMaxWidth(),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            HorizontalDivider(

                modifier =
                    Modifier.weight(1f),

                color =
                    GuinchouBorder
            )

            Text(

                text = "ou",

                color =
                    GuinchouGray,

                modifier =
                    Modifier.padding(
                        horizontal = 14.dp
                    )
            )

            HorizontalDivider(

                modifier =
                    Modifier.weight(1f),

                color =
                    GuinchouBorder
            )
        }


        Spacer(
            modifier =
                Modifier.height(22.dp)
        )


        /*
         * ===================================
         * LOGIN GOOGLE
         * ===================================
         */

        OutlinedButton(

            onClick = {

                focusManager.clearFocus()

                keyboardController?.hide()

                onGoogleClick()
            },

            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(54.dp),

            shape =
                RoundedCornerShape(
                    14.dp
                )
        ) {

            Text(

                text =
                    "Continuar com Google",

                color =
                    GuinchouWhite,

                fontWeight =
                    FontWeight.Medium
            )
        }


        Spacer(
            modifier =
                Modifier.height(24.dp)
        )


        /*
         * ===================================
         * CRIAR CONTA
         * ===================================
         */

        Row(

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Text(

                text =
                    "Ainda não tem conta? ",

                color =
                    GuinchouGray,

                fontSize = 14.sp
            )

            Text(

                text =
                    "Criar conta",

                color =
                    GuinchouGreen,

                fontSize = 14.sp,

                fontWeight =
                    FontWeight.Bold,

                modifier =
                    Modifier.clickable {

                        focusManager.clearFocus()

                        keyboardController?.hide()

                        onCreateAccountClick()
                    }
            )
        }


        Spacer(
            modifier =
                Modifier.height(20.dp)
        )


        /*
         * ===================================
         * PARCEIROS
         * ===================================
         */

        Text(

            text =
                "Sou motorista ou empresa parceira",

            color =
                GuinchouGreen,

            fontSize = 14.sp,

            fontWeight =
                FontWeight.Medium,

            modifier =
                Modifier.clickable {

                    focusManager.clearFocus()

                    keyboardController?.hide()

                    onPartnerClick()
                }
        )
    }
}