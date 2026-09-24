package com.guinchou.app.ui.screens.request

import android.location.Address
import android.location.Geocoder
import android.os.Build
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.guinchou.app.ui.theme.GuinchouBackground
import com.guinchou.app.ui.theme.GuinchouBorder
import com.guinchou.app.ui.theme.GuinchouGray
import com.guinchou.app.ui.theme.GuinchouGreen
import com.guinchou.app.ui.theme.GuinchouSurface
import com.guinchou.app.ui.theme.GuinchouWhite
import java.util.Locale
import kotlin.coroutines.resume
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext

private data class DestinationSuggestion(
    val title: String,
    val subtitle: String,
    val fullAddress: String,
    val type: DestinationSuggestionType
)

private enum class DestinationSuggestionType {
    HOME,
    RECENT
}

@Composable
fun DestinationScreen(
    onContinueClick: (
        address: String,
        latitude: Double,
        longitude: Double
    ) -> Unit,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    var destinationAddress by rememberSaveable { mutableStateOf("") }
    var isSearching by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val savedDestinations = remember {
        listOf(
            DestinationSuggestion(
                title = "Casa",
                subtitle = "Asa Norte, Brasília - DF",
                fullAddress = "Asa Norte, Brasília - DF",
                type = DestinationSuggestionType.HOME
            ),
            DestinationSuggestion(
                title = "SIA",
                subtitle = "Setor de Indústria e Abastecimento, Brasília - DF",
                fullAddress = "SIA, Brasília - DF",
                type = DestinationSuggestionType.RECENT
            ),
            DestinationSuggestion(
                title = "Águas Claras",
                subtitle = "Águas Claras, Brasília - DF",
                fullAddress = "Águas Claras, Brasília - DF",
                type = DestinationSuggestionType.RECENT
            )
        )
    }

    val visibleSuggestions = remember(destinationAddress) {
        val query = destinationAddress.trim()
        if (query.isBlank()) {
            savedDestinations
        } else {
            savedDestinations.filter {
                it.title.contains(query, ignoreCase = true) ||
                        it.subtitle.contains(query, ignoreCase = true) ||
                        it.fullAddress.contains(query, ignoreCase = true)
            }
        }
    }

    fun selectDestination(address: String) {
        destinationAddress = address
        errorMessage = null
        focusManager.clearFocus()
        keyboardController?.hide()
    }

    fun searchDestination() {
        val address = destinationAddress.trim()

        if (address.length < 4) {
            errorMessage = "Digite um destino mais completo para continuar."
            return
        }

        focusManager.clearFocus()
        keyboardController?.hide()
        isSearching = true
        errorMessage = null

        scope.launch {
            try {
                val location = geocodeDestination(
                    address = address,
                    geocoder = Geocoder(
                        context.applicationContext,
                        Locale("pt", "BR")
                    )
                )

                if (location == null) {
                    errorMessage =
                        "Não encontramos esse destino. Informe rua, número, bairro e cidade."
                } else {
                    onContinueClick(
                        address,
                        location.latitude,
                        location.longitude
                    )
                }
            } catch (_: Exception) {
                errorMessage =
                    "Falha ao localizar o destino. Verifique a conexão e tente novamente."
            } finally {
                isSearching = false
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GuinchouBackground)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        DestinationHeader(
            onBackClick = onBackClick,
            enabled = !isSearching
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            StepIndicator()

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Para onde vamos levar o veículo?",
                color = GuinchouWhite,
                fontSize = 27.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(7.dp))

            Text(
                text = "Pesquise o endereço de destino ou escolha um local utilizado recentemente.",
                color = GuinchouGray,
                fontSize = 13.sp
            )

            Spacer(modifier = Modifier.height(22.dp))

            OutlinedTextField(
                value = destinationAddress,
                onValueChange = {
                    destinationAddress = it.take(120)
                    errorMessage = null
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isSearching,
                singleLine = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = GuinchouGreen
                    )
                },
                trailingIcon = {
                    if (destinationAddress.isNotBlank() && !isSearching) {
                        IconButton(
                            onClick = {
                                destinationAddress = ""
                                errorMessage = null
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Limpar destino",
                                tint = GuinchouGray
                            )
                        }
                    }
                },
                label = {
                    Text("Destino")
                },
                placeholder = {
                    Text("Rua, número, bairro ou local")
                },
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = GuinchouWhite,
                    unfocusedTextColor = GuinchouWhite,
                    focusedBorderColor = GuinchouGreen,
                    unfocusedBorderColor = GuinchouBorder,
                    focusedLabelColor = GuinchouGreen,
                    unfocusedLabelColor = GuinchouGray,
                    cursorColor = GuinchouGreen,
                    focusedContainerColor = GuinchouSurface,
                    unfocusedContainerColor = GuinchouSurface,
                    disabledContainerColor = GuinchouSurface
                )
            )

            errorMessage?.let { message ->
                Spacer(modifier = Modifier.height(9.dp))
                Text(
                    text = message,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(22.dp))

            if (destinationAddress.isBlank()) {
                Text(
                    text = "Locais rápidos",
                    color = GuinchouWhite,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(10.dp))
            } else {
                Text(
                    text = "Sugestões",
                    color = GuinchouWhite,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(10.dp))
            }

            if (visibleSuggestions.isEmpty() && destinationAddress.isNotBlank()) {
                ManualAddressHint()
            } else {
                visibleSuggestions.forEachIndexed { index, suggestion ->
                    DestinationSuggestionCard(
                        suggestion = suggestion,
                        onClick = {
                            selectDestination(suggestion.fullAddress)
                        }
                    )

                    if (index != visibleSuggestions.lastIndex) {
                        Spacer(modifier = Modifier.height(9.dp))
                    }
                }
            }

            if (destinationAddress.isNotBlank()) {
                Spacer(modifier = Modifier.height(12.dp))

                SearchTypedAddressCard(
                    address = destinationAddress,
                    onClick = {
                        searchDestination()
                    }
                )
            }

            Spacer(modifier = Modifier.height(22.dp))

            DestinationTipCard()

            Spacer(modifier = Modifier.height(18.dp))
        }

        HorizontalDivider(color = GuinchouBorder)

        Button(
            onClick = { searchDestination() },
            enabled = !isSearching && destinationAddress.trim().length >= 4,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .height(54.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = GuinchouGreen,
                contentColor = GuinchouBackground,
                disabledContainerColor = GuinchouBorder,
                disabledContentColor = GuinchouGray
            )
        ) {
            if (isSearching) {
                CircularProgressIndicator(
                    modifier = Modifier.size(23.dp),
                    color = GuinchouBackground,
                    strokeWidth = 3.dp
                )

                Spacer(modifier = Modifier.size(9.dp))

                Text(
                    text = "Localizando...",
                    fontWeight = FontWeight.Bold
                )
            } else {
                Text(
                    text = "Confirmar destino",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun DestinationHeader(
    onBackClick: () -> Unit,
    enabled: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBackClick,
            enabled = enabled
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Voltar",
                tint = if (enabled) GuinchouWhite else GuinchouGray
            )
        }

        Text(
            text = "Destino",
            color = GuinchouWhite,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun StepIndicator() {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "ETAPA 2 DE 5",
                color = GuinchouGreen,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Destino",
                color = GuinchouGray,
                fontSize = 11.sp
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            repeat(5) { index ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(3.dp)
                        .background(
                            color = if (index <= 1) {
                                GuinchouGreen
                            } else {
                                GuinchouBorder
                            },
                            shape = RoundedCornerShape(50)
                        )
                )
            }
        }
    }
}

@Composable
private fun DestinationSuggestionCard(
    suggestion: DestinationSuggestion,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                GuinchouSurface,
                RoundedCornerShape(15.dp)
            )
            .border(
                1.dp,
                GuinchouBorder,
                RoundedCornerShape(15.dp)
            )
            .clickable(onClick = onClick)
            .padding(14.dp),
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
                imageVector = if (suggestion.type == DestinationSuggestionType.HOME) {
                    Icons.Default.Home
                } else {
                    Icons.Default.History
                },
                contentDescription = null,
                tint = GuinchouGreen,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.size(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = suggestion.title,
                color = GuinchouWhite,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = suggestion.subtitle,
                color = GuinchouGray,
                fontSize = 10.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun SearchTypedAddressCard(
    address: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                GuinchouGreen.copy(alpha = 0.07f),
                RoundedCornerShape(15.dp)
            )
            .border(
                1.dp,
                GuinchouGreen.copy(alpha = 0.28f),
                RoundedCornerShape(15.dp)
            )
            .clickable(onClick = onClick)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .background(
                    GuinchouGreen.copy(alpha = 0.12f),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                tint = GuinchouGreen,
                modifier = Modifier.size(21.dp)
            )
        }

        Spacer(modifier = Modifier.size(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "Buscar este endereço",
                color = GuinchouGreen,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = address,
                color = GuinchouWhite,
                fontSize = 11.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun ManualAddressHint() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                GuinchouSurface,
                RoundedCornerShape(15.dp)
            )
            .border(
                1.dp,
                GuinchouBorder,
                RoundedCornerShape(15.dp)
            )
            .padding(14.dp)
    ) {
        Text(
            text = "Não encontrou nas sugestões?",
            color = GuinchouWhite,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Continue digitando o endereço completo e use a opção “Buscar este endereço”.",
            color = GuinchouGray,
            fontSize = 10.sp
        )
    }
}

@Composable
private fun DestinationTipCard() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                GuinchouSurface,
                RoundedCornerShape(15.dp)
            )
            .border(
                1.dp,
                GuinchouBorder,
                RoundedCornerShape(15.dp)
            )
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = null,
            tint = GuinchouGreen,
            modifier = Modifier.size(20.dp)
        )

        Spacer(modifier = Modifier.size(11.dp))

        Text(
            text = "Para aumentar a precisão, informe rua, número, bairro e cidade antes de confirmar.",
            color = GuinchouGray,
            fontSize = 10.sp,
            modifier = Modifier.weight(1f)
        )
    }
}

private data class DestinationLocation(
    val latitude: Double,
    val longitude: Double
)

private suspend fun geocodeDestination(
    address: String,
    geocoder: Geocoder
): DestinationLocation? {
    if (!Geocoder.isPresent()) return null

    val result: Address? =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            suspendCancellableCoroutine { continuation ->
                geocoder.getFromLocationName(
                    address,
                    1,
                    object : Geocoder.GeocodeListener {
                        override fun onGeocode(addresses: MutableList<Address>) {
                            if (continuation.isActive) {
                                continuation.resume(addresses.firstOrNull())
                            }
                        }

                        override fun onError(errorMessage: String?) {
                            if (continuation.isActive) {
                                continuation.resume(null)
                            }
                        }
                    }
                )
            }
        } else {
            @Suppress("DEPRECATION")
            withContext(Dispatchers.IO) {
                geocoder.getFromLocationName(address, 1)
                    ?.firstOrNull()
            }
        }

    if (result?.hasLatitude() != true ||
        result.hasLongitude() != true
    ) {
        return null
    }

    return DestinationLocation(
        latitude = result.latitude,
        longitude = result.longitude
    )
}
