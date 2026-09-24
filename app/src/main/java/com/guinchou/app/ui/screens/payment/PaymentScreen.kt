package com.guinchou.app.ui.screens.payment

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.guinchou.app.ui.theme.GuinchouBackground
import com.guinchou.app.ui.theme.GuinchouBorder
import com.guinchou.app.ui.theme.GuinchouGray
import com.guinchou.app.ui.theme.GuinchouGreen
import com.guinchou.app.ui.theme.GuinchouSurface
import com.guinchou.app.ui.theme.GuinchouWhite

private data class CustomerPaymentCard(
    val id: Int,
    val brand: String,
    val lastFour: String,
    val holder: String,
    val expiry: String,
)

private data class PaymentHistoryItem(
    val date: String,
    val description: String,
    val method: String,
    val value: String,
    val status: String
)

@Composable
fun PaymentsScreen(
    onBackClick: () -> Unit = {},
    onHomeClick: () -> Unit = {},
    onCallsClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    var cards by remember {
        mutableStateOf(
            listOf(
                CustomerPaymentCard(
                    id = 1,
                    brand = "Mastercard",
                    lastFour = "4242",
                    holder = "JOÃO DA SILVA",
                    expiry = "12/29"
                )
            )
        )
    }
    var defaultMethod by remember { mutableStateOf("PIX") }
    var showAddCard by remember { mutableStateOf(false) }
    var cardToDelete by remember { mutableStateOf<CustomerPaymentCard?>(null) }
    var feedback by remember { mutableStateOf<String?>(null) }

    val history = remember {
        listOf(
            PaymentHistoryItem(
                date = "14/09/2026",
                description = "Guincho • Asa Norte → Águas Claras",
                method = "PIX",
                value = "R$ 200,00",
                status = "Pago"
            ),
            PaymentHistoryItem(
                date = "03/09/2026",
                description = "Guincho • Taguatinga → SIA",
                method = "Mastercard •••• 4242",
                value = "R$ 150,00",
                status = "Pago"
            )
        )
    }

    if (showAddCard) {
        AddCardDialog(
            onDismiss = { showAddCard = false },
            onSave = { number, holder, expiry ->
                val newCard = CustomerPaymentCard(
                    id = (cards.maxOfOrNull { it.id } ?: 0) + 1,
                    brand = detectCardBrand(number),
                    lastFour = number.takeLast(4),
                    holder = holder.uppercase(),
                    expiry = expiry
                )
                cards = cards + newCard
                defaultMethod = "CARD_${newCard.id}"
                feedback = "Cartão adicionado e definido como forma de pagamento padrão."
                showAddCard = false
            }
        )
    }

    cardToDelete?.let { card ->
        AlertDialog(
            onDismissRequest = { cardToDelete = null },
            containerColor = GuinchouSurface,
            title = {
                Text(
                    text = "Remover cartão?",
                    color = GuinchouWhite,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "${card.brand} final ${card.lastFour} será removido deste dispositivo.",
                    color = GuinchouGray
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        cards = cards.filterNot { it.id == card.id }
                        if (defaultMethod == "CARD_${card.id}") {
                            defaultMethod = "PIX"
                        }
                        feedback = "Cartão removido."
                        cardToDelete = null
                    }
                ) {
                    Text("Remover", color = Color(0xFFFF6B6B))
                }
            },
            dismissButton = {
                TextButton(onClick = { cardToDelete = null }) {
                    Text("Cancelar", color = GuinchouGray)
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GuinchouBackground)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
        ) {
            PaymentsHeader(onBackClick = onBackClick)

            Column(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "Sua carteira",
                    color = GuinchouWhite,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = "Gerencie como você deseja pagar pelos atendimentos.",
                    color = GuinchouGray,
                    fontSize = 13.sp
                )

                feedback?.let {
                    Spacer(Modifier.height(16.dp))
                    PaymentFeedbackCard(
                        text = it,
                        onClose = { feedback = null }
                    )
                }

                Spacer(Modifier.height(20.dp))

                Text(
                    text = "Forma de pagamento padrão",
                    color = GuinchouWhite,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(10.dp))

                PaymentMethodCard(
                    icon = Icons.Default.Wallet,
                    title = "PIX",
                    description = "Pagamento instantâneo",
                    selected = defaultMethod == "PIX",
                    onClick = {
                        defaultMethod = "PIX"
                        feedback = "PIX definido como forma de pagamento padrão."
                    }
                )

                cards.forEach { card ->
                    Spacer(Modifier.height(10.dp))
                    SavedCardItem(
                        card = card,
                        selected = defaultMethod == "CARD_${card.id}",
                        onSelect = {
                            defaultMethod = "CARD_${card.id}"
                            feedback = "${card.brand} final ${card.lastFour} definido como padrão."
                        },
                        onDelete = { cardToDelete = card }
                    )
                }

                Spacer(Modifier.height(12.dp))

                Button(
                    onClick = { showAddCard = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GuinchouGreen,
                        contentColor = GuinchouBackground
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null
                    )
                    Spacer(Modifier.size(8.dp))
                    Text(
                        text = "Adicionar cartão",
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(Modifier.height(26.dp))

                Text(
                    text = "Pagamentos recentes",
                    color = GuinchouWhite,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(10.dp))

                history.forEachIndexed { index, item ->
                    PaymentHistoryCard(item)
                    if (index != history.lastIndex) {
                        Spacer(Modifier.height(10.dp))
                    }
                }

                Spacer(Modifier.height(18.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            GuinchouSurface,
                            RoundedCornerShape(16.dp)
                        )
                        .border(
                            1.dp,
                            GuinchouBorder,
                            RoundedCornerShape(16.dp)
                        )
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ReceiptLong,
                        contentDescription = null,
                        tint = GuinchouGreen
                    )

                    Spacer(Modifier.size(12.dp))

                    Text(
                        text = "Nesta etapa, os pagamentos são demonstrativos. O processamento real será conectado ao gateway pelo backend.",
                        color = GuinchouGray,
                        fontSize = 11.sp,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(Modifier.height(20.dp))
            }
        }

        HorizontalDivider(color = GuinchouBorder)

        PaymentsBottomBar(
            onHomeClick = onHomeClick,
            onCallsClick = onCallsClick,
            onProfileClick = onProfileClick
        )
    }
}

@Composable
private fun PaymentsHeader(
    onBackClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Voltar",
                tint = GuinchouWhite
            )
        }

        Text(
            text = "Pagamentos",
            color = GuinchouWhite,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun PaymentMethodCard(
    icon: ImageVector,
    title: String,
    description: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                GuinchouSurface,
                RoundedCornerShape(16.dp)
            )
            .border(
                width = 1.dp,
                color = if (selected) GuinchouGreen else GuinchouBorder,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onClick)
            .padding(15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .background(
                    GuinchouBackground,
                    RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = GuinchouGreen
            )
        }

        Spacer(Modifier.size(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                color = GuinchouWhite,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Text(
                text = description,
                color = GuinchouGray,
                fontSize = 11.sp
            )
        }

        SelectionMark(selected)
    }
}

@Composable
private fun SavedCardItem(
    card: CustomerPaymentCard,
    selected: Boolean,
    onSelect: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                GuinchouSurface,
                RoundedCornerShape(16.dp)
            )
            .border(
                width = 1.dp,
                color = if (selected) GuinchouGreen else GuinchouBorder,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onSelect)
            .padding(15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .background(
                    GuinchouBackground,
                    RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.CreditCard,
                contentDescription = null,
                tint = GuinchouGreen
            )
        }

        Spacer(Modifier.size(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "${card.brand} •••• ${card.lastFour}",
                color = GuinchouWhite,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Text(
                text = "${card.holder} • ${card.expiry}",
                color = GuinchouGray,
                fontSize = 10.sp
            )
        }

        SelectionMark(selected)

        Spacer(Modifier.size(4.dp))

        IconButton(onClick = onDelete) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Remover cartão",
                tint = GuinchouGray,
                modifier = Modifier.size(19.dp)
            )
        }
    }
}

@Composable
private fun SelectionMark(
    selected: Boolean
) {
    Box(
        modifier = Modifier
            .size(22.dp)
            .border(
                1.dp,
                if (selected) GuinchouGreen else GuinchouBorder,
                CircleShape
            )
            .background(
                if (selected) GuinchouGreen else Color.Transparent,
                CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        if (selected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = GuinchouBackground,
                modifier = Modifier.size(15.dp)
            )
        }
    }
}

@Composable
private fun PaymentHistoryCard(
    item: PaymentHistoryItem
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                GuinchouSurface,
                RoundedCornerShape(16.dp)
            )
            .border(
                1.dp,
                GuinchouBorder,
                RoundedCornerShape(16.dp)
            )
            .padding(15.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = item.date,
                color = GuinchouGray,
                fontSize = 11.sp,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = item.status,
                color = GuinchouGreen,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.height(8.dp))

        Text(
            text = item.description,
            color = GuinchouWhite,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(Modifier.height(5.dp))

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = item.method,
                color = GuinchouGray,
                fontSize = 11.sp,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = item.value,
                color = GuinchouGreen,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun PaymentFeedbackCard(
    text: String,
    onClose: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                GuinchouGreen.copy(alpha = 0.08f),
                RoundedCornerShape(14.dp)
            )
            .border(
                1.dp,
                GuinchouGreen.copy(alpha = 0.30f),
                RoundedCornerShape(14.dp)
            )
            .padding(13.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = null,
            tint = GuinchouGreen
        )

        Spacer(Modifier.size(10.dp))

        Text(
            text = text,
            color = GuinchouWhite,
            fontSize = 11.sp,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = "×",
            color = GuinchouGray,
            fontSize = 20.sp,
            modifier = Modifier
                .clickable(onClick = onClose)
                .padding(4.dp)
        )
    }
}

@Composable
private fun AddCardDialog(
    onDismiss: () -> Unit,
    onSave: (number: String, holder: String, expiry: String) -> Unit
) {
    var number by remember { mutableStateOf("") }
    var holder by remember { mutableStateOf("") }
    var expiry by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    val brand = detectCardBrand(number)
    val numberValid = (number.length in 13..16)
    val holderValid = holder.trim().length >= 3
    val expiryValid = isValidExpiry(expiry)
    val formValid = numberValid && holderValid && expiryValid

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = GuinchouSurface,
        title = {
            Column {
                Text(
                    text = "Adicionar cartão",
                    color = GuinchouWhite,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Spacer(Modifier.height(3.dp))
                Text(
                    text = "Preencha os dados e acompanhe a prévia em tempo real.",
                    color = GuinchouGray,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                CardPreview(
                    brand = brand,
                    number = number,
                    holder = holder,
                    expiry = expiry
                )

                Spacer(Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FormStatusChip(
                        text = "Número",
                        completed = numberValid,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(Modifier.size(6.dp))
                    FormStatusChip(
                        text = "Titular",
                        completed = holderValid,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(Modifier.size(6.dp))
                    FormStatusChip(
                        text = "Validade",
                        completed = expiryValid,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(Modifier.height(14.dp))

                PaymentTextField(
                    value = formatCardNumber(number),
                    label = "Número do cartão",
                    keyboardType = KeyboardType.Number,
                    onValueChange = {
                        number = it.filter(Char::isDigit).take(16)
                        error = null
                    }
                )

                if (number.isNotEmpty()) {
                    Spacer(Modifier.height(5.dp))
                    Text(
                        text = if (numberValid) "$brand identificado" else "Digite de 13 a 16 números",
                        color = if (numberValid) GuinchouGreen else GuinchouGray,
                        fontSize = 10.sp
                    )
                }

                Spacer(Modifier.height(10.dp))

                PaymentTextField(
                    value = holder,
                    label = "Nome impresso no cartão",
                    onValueChange = {
                        holder = it
                            .filter { char -> char.isLetter() || char.isWhitespace() }
                            .take(40)
                            .uppercase()
                        error = null
                    }
                )

                Spacer(Modifier.height(10.dp))

                PaymentTextField(
                    value = expiry,
                    label = "Validade (MM/AA)",
                    keyboardType = KeyboardType.Number,
                    onValueChange = {
                        expiry = formatExpiry(it)
                        error = null
                    }
                )

                error?.let {
                    Spacer(Modifier.height(10.dp))
                    Text(
                        text = it,
                        color = Color(0xFFFF6B6B),
                        fontSize = 11.sp
                    )
                }

                Spacer(Modifier.height(12.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            GuinchouBackground,
                            RoundedCornerShape(12.dp)
                        )
                        .border(
                            1.dp,
                            GuinchouBorder,
                            RoundedCornerShape(12.dp)
                        )
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🔒",
                        fontSize = 14.sp
                    )
                    Spacer(Modifier.size(8.dp))
                    Text(
                        text = "Demonstração do front-end. Não utilize dados de um cartão real nesta versão.",
                        color = GuinchouGray,
                        fontSize = 9.sp,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                enabled = formValid,
                onClick = {
                    if (formValid) {
                        onSave(number, holder.trim(), expiry)
                    } else {
                        error = "Confira os dados do cartão antes de continuar."
                    }
                },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = GuinchouGreen,
                    contentColor = GuinchouBackground,
                    disabledContainerColor = GuinchouBorder,
                    disabledContentColor = GuinchouGray
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.size(17.dp)
                )
                Spacer(Modifier.size(6.dp))
                Text(
                    text = "Adicionar cartão",
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = GuinchouGray)
            }
        }
    )
}

@Composable
private fun CardPreview(
    brand: String,
    number: String,
    holder: String,
    expiry: String
) {
    val previewNumber = if (number.isBlank()) {
        "•••• •••• •••• ••••"
    } else {
        val groups = formatCardNumber(number).split(" ").toMutableList()
        while (groups.size < 4) groups.add("••••")
        groups.mapIndexed { index, group ->
            if (index < groups.lastIndex && group.length < 4) {
                group.padEnd(4, '•')
            } else {
                group
            }
        }.joinToString(" ")
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                GuinchouBackground,
                RoundedCornerShape(18.dp)
            )
            .border(
                1.dp,
                GuinchouGreen.copy(alpha = 0.45f),
                RoundedCornerShape(18.dp)
            )
            .padding(18.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(width = 38.dp, height = 27.dp)
                    .background(
                        GuinchouGreen.copy(alpha = 0.18f),
                        RoundedCornerShape(6.dp)
                    )
                    .border(
                        1.dp,
                        GuinchouGreen.copy(alpha = 0.55f),
                        RoundedCornerShape(6.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "▦",
                    color = GuinchouGreen,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.weight(1f))

            Text(
                text = brand.uppercase(),
                color = GuinchouWhite,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.height(22.dp))

        Text(
            text = previewNumber,
            color = GuinchouWhite,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Bottom
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "TITULAR",
                    color = GuinchouGray,
                    fontSize = 8.sp
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = holder.ifBlank { "NOME DO TITULAR" }.uppercase(),
                    color = GuinchouWhite,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "VALIDADE",
                    color = GuinchouGray,
                    fontSize = 8.sp
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = expiry.ifBlank { "MM/AA" },
                    color = GuinchouWhite,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun FormStatusChip(
    text: String,
    completed: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .background(
                if (completed) {
                    GuinchouGreen.copy(alpha = 0.10f)
                } else {
                    GuinchouBackground
                },
                RoundedCornerShape(10.dp)
            )
            .border(
                1.dp,
                if (completed) {
                    GuinchouGreen.copy(alpha = 0.35f)
                } else {
                    GuinchouBorder
                },
                RoundedCornerShape(10.dp)
            )
            .padding(horizontal = 7.dp, vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        if (completed) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = GuinchouGreen,
                modifier = Modifier.size(12.dp)
            )
            Spacer(Modifier.size(4.dp))
        }

        Text(
            text = text,
            color = if (completed) GuinchouGreen else GuinchouGray,
            fontSize = 9.sp,
            fontWeight = if (completed) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}

@Composable
private fun PaymentTextField(
    value: String,
    label: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(label) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = GuinchouWhite,
            unfocusedTextColor = GuinchouWhite,
            focusedBorderColor = GuinchouGreen,
            unfocusedBorderColor = GuinchouBorder,
            focusedLabelColor = GuinchouGreen,
            unfocusedLabelColor = GuinchouGray,
            cursorColor = GuinchouGreen,
            focusedContainerColor = GuinchouBackground,
            unfocusedContainerColor = GuinchouBackground
        )
    )
}

@Composable
private fun PaymentsBottomBar(
    onHomeClick: () -> Unit,
    onCallsClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(GuinchouBackground)
            .navigationBarsPadding()
            .padding(horizontal = 8.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        PaymentBottomItem(
            icon = Icons.Default.Home,
            text = "Início",
            onClick = onHomeClick
        )
        PaymentBottomItem(
            icon = Icons.Default.Build,
            text = "Chamados",
            onClick = onCallsClick
        )
        PaymentBottomItem(
            icon = Icons.Default.CreditCard,
            text = "Pagamentos",
            selected = true
        )
        PaymentBottomItem(
            icon = Icons.Default.Person,
            text = "Perfil",
            onClick = onProfileClick
        )
    }
}

@Composable
private fun PaymentBottomItem(
    icon: ImageVector,
    text: String,
    selected: Boolean = false,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = if (selected) GuinchouGreen else GuinchouGray,
            modifier = Modifier.size(22.dp)
        )

        Spacer(Modifier.height(4.dp))

        Text(
            text = text,
            color = if (selected) GuinchouGreen else GuinchouGray,
            fontSize = 11.sp,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(3.dp))

        Box(
            modifier = Modifier
                .size(width = 18.dp, height = 2.dp)
                .background(
                    if (selected) GuinchouGreen else Color.Transparent,
                    RoundedCornerShape(50)
                )
        )
    }
}

private fun formatCardNumber(value: String): String =
    value.filter(Char::isDigit)
        .take(16)
        .chunked(4)
        .joinToString(" ")

private fun formatExpiry(value: String): String {
    val digits = value.filter(Char::isDigit).take(4)
    return when {
        digits.length <= 2 -> digits
        else -> "${digits.take(2)}/${digits.drop(2)}"
    }
}

private fun isValidExpiry(value: String): Boolean {
    if (!Regex("""\d{2}/\d{2}""").matches(value)) return false
    val month = value.take(2).toIntOrNull() ?: return false
    return month in 1..12
}

private fun detectCardBrand(number: String): String =
    when {
        number.startsWith("4") -> "Visa"
        number.startsWith("5") -> "Mastercard"
        number.startsWith("34") || number.startsWith("37") -> "American Express"
        else -> "Cartão"
    }

