package com.guinchou.app.ui.screens.partner

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

/**
 * Primeira tela do módulo de parceiros.
 *
 * O usuário escolhe como deseja trabalhar:
 *
 * - motorista independente;
 * - empresa de guinchos.
 */
@Composable
fun PartnerTypeScreen(

    /*
     * Abre o cadastro
     * de motorista independente.
     */
    onIndependentDriverClick: () -> Unit,

    /*
     * Abre o cadastro
     * empresarial.
     */
    onCompanyClick: () -> Unit,

    /*
     * Volta para o Login.
     */
    onBackClick: () -> Unit
) {

    Column(

        modifier = Modifier
            .fillMaxSize()

            /*
             * Fundo padrão Guinchou.
             */
            .background(
                GuinchouBackground
            )

            /*
             * Protege contra relógio,
             * bateria, notch etc.
             */
            .statusBarsPadding()

            /*
             * Protege contra barra
             * de navegação e gestos.
             */
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

                /*
                 * Mantém a tela utilizável
                 * também em celulares menores.
                 */
                .verticalScroll(
                    rememberScrollState()
                )

                .padding(
                    horizontal = 20.dp,
                    vertical = 18.dp
                )
        ) {

            /*
             * Identificação da área.
             */
            Text(
                text = "GUINCHOU",
                color = GuinchouGreen,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier =
                    Modifier.height(22.dp)
            )


            /*
             * Título principal.
             */
            Text(
                text = "Como você deseja trabalhar?",
                color = GuinchouWhite,
                fontSize = 27.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier =
                    Modifier.height(8.dp)
            )


            /*
             * Texto explicativo.
             */
            Text(
                text =
                    "Escolha o tipo de parceiro para iniciarmos seu cadastro.",
                color = GuinchouGray,
                fontSize = 14.sp
            )

            Spacer(
                modifier =
                    Modifier.height(30.dp)
            )


            /*
             * =========================================
             * MOTORISTA INDEPENDENTE
             * =========================================
             */
            PartnerTypeCard(

                title =
                    "Motorista independente",

                description =
                    "Para quem possui ou opera seu próprio guincho e deseja receber chamados pelo Guinchou.",

                details =
                    "Cadastro pessoal • Guincho • Documentos • Aprovação",

                onClick =
                    onIndependentDriverClick
            )


            Spacer(
                modifier =
                    Modifier.height(16.dp)
            )


            /*
             * =========================================
             * EMPRESA
             * =========================================
             */
            PartnerTypeCard(

                title =
                    "Empresa de guinchos",

                description =
                    "Para empresas que administram uma frota, motoristas e múltiplos guinchos.",

                details =
                    "Frota • Motoristas • Importação em lote • Gestão empresarial",

                onClick =
                    onCompanyClick
            )


            Spacer(
                modifier =
                    Modifier.height(28.dp)
            )


            /*
             * Informação sobre aprovação.
             */
            Column(

                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color =
                            GuinchouSurface,
                        shape =
                            RoundedCornerShape(
                                16.dp
                            )
                    )
                    .border(
                        width = 1.dp,
                        color =
                            GuinchouBorder,
                        shape =
                            RoundedCornerShape(
                                16.dp
                            )
                    )
                    .padding(16.dp)
            ) {

                Text(
                    text =
                        "Cadastro sujeito à aprovação",
                    color =
                        GuinchouWhite,
                    fontSize =
                        14.sp,
                    fontWeight =
                        FontWeight.SemiBold
                )

                Spacer(
                    modifier =
                        Modifier.height(6.dp)
                )

                Text(
                    text =
                        "Antes de receber chamados, os dados, veículos e documentos enviados precisarão passar pela análise do Guinchou.",
                    color =
                        GuinchouGray,
                    fontSize =
                        13.sp
                )
            }


            Spacer(
                modifier =
                    Modifier.height(20.dp)
            )
        }


        /*
         * =========================================
         * VOLTAR
         * =========================================
         */
        OutlinedButton(

            onClick =
                onBackClick,

            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 20.dp,
                    vertical = 12.dp
                )
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
    }
}


/**
 * Card reutilizável para os
 * tipos de parceiro.
 */
@Composable
private fun PartnerTypeCard(

    title: String,

    description: String,

    details: String,

    onClick: () -> Unit
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
                width =
                    1.dp,
                color =
                    GuinchouBorder,
                shape =
                    RoundedCornerShape(
                        18.dp
                    )
            )

            .clickable {

                onClick()
            }

            .padding(
                20.dp
            )
    ) {

        /*
         * Título da opção.
         */
        Row(

            modifier =
                Modifier.fillMaxWidth(),

            horizontalArrangement =
                Arrangement.SpaceBetween
        ) {

            Text(

                text =
                    title,

                color =
                    GuinchouWhite,

                fontSize =
                    18.sp,

                fontWeight =
                    FontWeight.Bold,

                modifier =
                    Modifier.weight(
                        1f
                    )
            )


            /*
             * Seta simples.
             */
            Text(
                text = "›",
                color =
                    GuinchouGreen,
                fontSize =
                    28.sp,
                fontWeight =
                    FontWeight.Bold
            )
        }


        Spacer(
            modifier =
                Modifier.height(
                    8.dp
                )
        )


        /*
         * Explicação.
         */
        Text(

            text =
                description,

            color =
                GuinchouGray,

            fontSize =
                14.sp
        )


        Spacer(
            modifier =
                Modifier.height(
                    14.dp
                )
        )


        /*
         * Resumo das etapas.
         */
        Text(

            text =
                details,

            color =
                GuinchouGreen,

            fontSize =
                12.sp,

            fontWeight =
                FontWeight.Medium
        )
    }
}