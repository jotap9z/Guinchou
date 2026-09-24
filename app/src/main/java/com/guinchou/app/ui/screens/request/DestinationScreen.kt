package com.guinchou.app.ui.screens.request

import android.location.Address
import android.location.Geocoder
import android.os.Build
import androidx.compose.foundation.background
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.guinchou.app.ui.theme.GuinchouBackground
import com.guinchou.app.ui.theme.GuinchouBorder
import com.guinchou.app.ui.theme.GuinchouGray
import com.guinchou.app.ui.theme.GuinchouGreen
import com.guinchou.app.ui.theme.GuinchouSurface
import com.guinchou.app.ui.theme.GuinchouWhite
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.util.Locale
import kotlin.coroutines.resume

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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GuinchouBackground)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Text(
                text = "2 de 5",
                color = GuinchouGray,
                fontSize = 13.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Para onde o veículo será levado?",
                color = GuinchouWhite,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Informe o endereço completo para localizar o destino.",
                color = GuinchouGray,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(28.dp))

            OutlinedTextField(
                value = destinationAddress,
                onValueChange = {
                    destinationAddress = it
                    errorMessage = null
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isSearching,
                label = { Text("Destino") },
                placeholder = {
                    Text("Rua, número, bairro, cidade e estado")
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = GuinchouWhite,
                    unfocusedTextColor = GuinchouWhite,
                    focusedBorderColor = GuinchouGreen,
                    unfocusedBorderColor = GuinchouBorder,
                    focusedLabelColor = GuinchouGreen,
                    unfocusedLabelColor = GuinchouGray,
                    cursorColor = GuinchouGreen,
                    focusedContainerColor = GuinchouSurface,
                    unfocusedContainerColor = GuinchouSurface
                )
            )

            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = errorMessage.orEmpty(),
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Confira o endereço antes de continuar. Se ele não for encontrado, informe também o número e a cidade.",
                color = GuinchouGray,
                fontSize = 13.sp
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onBackClick,
                enabled = !isSearching,
                modifier = Modifier
                    .weight(1f)
                    .height(54.dp),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text("Voltar", color = GuinchouWhite)
            }

            Button(
                onClick = {
                    val address = destinationAddress.trim()

                    if (address.isBlank()) {
                        errorMessage = "Informe o destino do veículo."
                    } else {
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
                                        "Não foi possível localizar esse endereço. Informe rua, número, cidade e estado."
                                } else {
                                    onContinueClick(
                                        address,
                                        location.latitude,
                                        location.longitude
                                    )
                                }
                            } catch (exception: Exception) {
                                errorMessage =
                                    "Falha ao localizar o destino. Verifique a conexão e tente novamente."
                            } finally {
                                isSearching = false
                            }
                        }
                    }
                },
                enabled = !isSearching,
                modifier = Modifier
                    .weight(1.4f)
                    .height(54.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = GuinchouGreen,
                    contentColor = GuinchouBackground
                )
            ) {
                if (isSearching) {
                    CircularProgressIndicator(
                        color = GuinchouBackground
                    )
                } else {
                    Text(
                        text = "Continuar",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
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