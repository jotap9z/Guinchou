package com.guinchou.app.ui.screens.request

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.guinchou.app.ui.theme.GuinchouBackground
import com.guinchou.app.ui.theme.GuinchouBorder
import com.guinchou.app.ui.theme.GuinchouGray
import com.guinchou.app.ui.theme.GuinchouGreen
import com.guinchou.app.ui.theme.GuinchouSurface
import com.guinchou.app.ui.theme.GuinchouWhite
import java.util.Locale

/**
 * Etapa 1 de 5.
 *
 * Localização do veículo através de:
 *
 * - endereço manual;
 * - localização atual do aparelho.
 */
@Composable
fun PickupScreen(

    onContinueClick: (
        address: String,
        latitude: Double?,
        longitude: Double?,
        source: String
    ) -> Unit,

    onBackClick: () -> Unit
) {

    val context =
        LocalContext.current

    val focusManager: FocusManager =
        LocalFocusManager.current

    val keyboardController =
        LocalSoftwareKeyboardController.current


    /*
     * Cliente de localização Google.
     */
    val fusedLocationClient =
        remember {

            LocationServices
                .getFusedLocationProviderClient(
                    context
                )
        }


    /*
     * =========================================
     * ESTADO
     * =========================================
     */

    var pickupAddress by remember {
        mutableStateOf("")
    }

    var latitude by remember {
        mutableStateOf<Double?>(null)
    }

    var longitude by remember {
        mutableStateOf<Double?>(null)
    }

    var locationSource by remember {
        mutableStateOf("MANUAL")
    }

    var isLoadingLocation by remember {
        mutableStateOf(false)
    }

    var errorMessage by remember {
        mutableStateOf<String?>(null)
    }


    val backgroundInteractionSource =
        remember {
            MutableInteractionSource()
        }


    /*
     * =========================================
     * OBTER LOCALIZAÇÃO
     * =========================================
     */

    @SuppressLint("MissingPermission")
    fun requestCurrentLocation() {

        isLoadingLocation =
            true

        errorMessage =
            null


        val cancellationTokenSource =
            CancellationTokenSource()


        fusedLocationClient
            .getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                cancellationTokenSource.token
            )
            .addOnSuccessListener {
                    location ->


                /*
                 * Nenhuma localização disponível.
                 */
                if (
                    location == null
                ) {

                    isLoadingLocation =
                        false

                    errorMessage =
                        "Não foi possível obter sua localização. Verifique se o GPS está ativado."

                    return@addOnSuccessListener
                }


                /*
                 * Guarda coordenadas.
                 */
                latitude =
                    location.latitude

                longitude =
                    location.longitude

                locationSource =
                    "GPS"


                /*
                 * =====================================
                 * CONVERTER COORDENADAS EM ENDEREÇO
                 * =====================================
                 */
                try {

                    val geocoder =
                        Geocoder(
                            context,
                            Locale.forLanguageTag("pt-BR")
                        )


                    /*
                     * Android 13+
                     */
                    if (
                        Build.VERSION.SDK_INT >=
                        Build.VERSION_CODES.TIRAMISU
                    ) {

                        geocoder.getFromLocation(
                            location.latitude,
                            location.longitude,
                            1
                        ) {
                                addresses ->

                            val address =
                                addresses
                                    .firstOrNull()


                            pickupAddress =

                                address
                                    ?.getAddressLine(
                                        0
                                    )
                                    ?: buildCoordinateText(
                                        location.latitude,
                                        location.longitude
                                    )


                            isLoadingLocation =
                                false
                        }

                    } else {

                        /*
                         * Android 12 ou inferior.
                         */
                        @Suppress("DEPRECATION")
                        val addresses =
                            geocoder.getFromLocation(
                                location.latitude,
                                location.longitude,
                                1
                            )


                        val address =
                            addresses
                                ?.firstOrNull()


                        pickupAddress =

                            address
                                ?.getAddressLine(
                                    0
                                )
                                ?: buildCoordinateText(
                                    location.latitude,
                                    location.longitude
                                )


                        isLoadingLocation =
                            false
                    }

                } catch (
                    _: Exception
                ) {

                    /*
                     * Mesmo sem endereço,
                     * mantemos as coordenadas.
                     */
                    pickupAddress =
                        buildCoordinateText(
                            location.latitude,
                            location.longitude
                        )

                    isLoadingLocation =
                        false
                }
            }

            .addOnFailureListener {

                isLoadingLocation =
                    false

                errorMessage =
                    "Não foi possível acessar sua localização."
            }
    }


    /*
     * =========================================
     * SOLICITAÇÃO DE PERMISSÃO
     * =========================================
     */

    val permissionLauncher =
        rememberLauncherForActivityResult(

            contract =
                ActivityResultContracts
                    .RequestMultiplePermissions()

        ) {
                permissions ->


            val fineGranted =
                permissions[
                    Manifest.permission
                        .ACCESS_FINE_LOCATION
                ] == true


            val coarseGranted =
                permissions[
                    Manifest.permission
                        .ACCESS_COARSE_LOCATION
                ] == true


            if (
                fineGranted ||
                coarseGranted
            ) {

                requestCurrentLocation()

            } else {

                errorMessage =
                    "Permita o acesso à localização para utilizar o GPS."
            }
        }


    /*
     * =========================================
     * USAR LOCALIZAÇÃO ATUAL
     * =========================================
     */

    fun useCurrentLocation() {

        focusManager
            .clearFocus()

        keyboardController
            ?.hide()


        val finePermission =
            ContextCompat
                .checkSelfPermission(
                    context,
                    Manifest.permission
                        .ACCESS_FINE_LOCATION
                )


        val coarsePermission =
            ContextCompat
                .checkSelfPermission(
                    context,
                    Manifest.permission
                        .ACCESS_COARSE_LOCATION
                )


        if (
            finePermission ==
            PackageManager.PERMISSION_GRANTED ||
            coarsePermission ==
            PackageManager.PERMISSION_GRANTED
        ) {

            requestCurrentLocation()

        } else {

            permissionLauncher.launch(

                arrayOf(

                    Manifest.permission
                        .ACCESS_FINE_LOCATION,

                    Manifest.permission
                        .ACCESS_COARSE_LOCATION
                )
            )
        }
    }


    /*
     * =========================================
     * INTERFACE
     * =========================================
     */

    Column(

        modifier = Modifier
            .fillMaxSize()
            .background(
                GuinchouBackground
            )

            /*
             * Tocar fora do campo:
             *
             * - fecha teclado;
             * - mantém informação.
             */
            .clickable(
                interactionSource =
                    backgroundInteractionSource,
                indication = null
            ) {

                focusManager
                    .clearFocus()

                keyboardController
                    ?.hide()
            }

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
                .padding(
                    horizontal = 20.dp,
                    vertical = 16.dp
                )
        ) {


            Text(
                text = "1 de 5",
                color = GuinchouGray,
                fontSize = 13.sp
            )


            Spacer(
                modifier =
                    Modifier.height(
                        8.dp
                    )
            )


            Text(
                text =
                    "Onde está o veículo?",
                color =
                    GuinchouWhite,
                fontSize =
                    26.sp,
                fontWeight =
                    FontWeight.Bold
            )


            Spacer(
                modifier =
                    Modifier.height(
                        8.dp
                    )
            )


            Text(
                text =
                    "Informe o endereço ou utilize sua localização atual.",
                color =
                    GuinchouGray,
                fontSize =
                    14.sp
            )


            Spacer(
                modifier =
                    Modifier.height(
                        26.dp
                    )
            )


            /*
             * =========================================
             * ENDEREÇO
             * =========================================
             */

            OutlinedTextField(

                value =
                    pickupAddress,

                onValueChange = {

                    pickupAddress =
                        it

                    /*
                     * Se alterar manualmente,
                     * as coordenadas antigas
                     * não podem continuar válidas.
                     */
                    latitude =
                        null

                    longitude =
                        null

                    locationSource =
                        "MANUAL"

                    errorMessage =
                        null
                },

                modifier =
                    Modifier.fillMaxWidth(),

                label = {

                    Text(
                        text =
                            "Localização do veículo"
                    )
                },

                placeholder = {

                    Text(
                        text =
                            "Digite rua, número ou endereço"
                    )
                },

                singleLine =
                    false,

                keyboardOptions =
                    KeyboardOptions(
                        keyboardType =
                            KeyboardType.Text
                    ),

                colors =
                    guinchouPickupTextFieldColors()
            )


            Spacer(
                modifier =
                    Modifier.height(
                        16.dp
                    )
            )


            /*
             * =========================================
             * DIVISOR
             * =========================================
             */

            Text(
                text = "ou",
                color = GuinchouGray,
                fontSize = 13.sp,
                textAlign = TextAlign.Center,
                modifier =
                    Modifier.fillMaxWidth()
            )


            Spacer(
                modifier =
                    Modifier.height(
                        16.dp
                    )
            )


            /*
             * =========================================
             * GPS
             * =========================================
             */

            OutlinedButton(

                onClick = {

                    if (
                        !isLoadingLocation
                    ) {

                        useCurrentLocation()
                    }
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .height(
                        56.dp
                    ),

                shape =
                    RoundedCornerShape(
                        14.dp
                    )
            ) {


                if (
                    isLoadingLocation
                ) {

                    /*
                     * Área fixa para que o
                     * indicador gire sem alterar
                     * o layout.
                     */
                    Box(

                        modifier =
                            Modifier.size(
                                26.dp
                            ),

                        contentAlignment =
                            Alignment.Center
                    ) {

                        CircularProgressIndicator(

                            modifier =
                                Modifier.size(
                                    20.dp
                                ),

                            color =
                                GuinchouGreen,

                            strokeWidth =
                                2.5.dp
                        )
                    }


                    Spacer(
                        modifier =
                            Modifier.size(
                                10.dp
                            )
                    )


                    Text(
                        text =
                            "Obtendo localização...",
                        color =
                            GuinchouWhite
                    )

                } else {

                    Text(
                        text =
                            "◎  Usar minha localização atual",
                        color =
                            GuinchouWhite,
                        fontWeight =
                            FontWeight.Medium
                    )
                }
            }


            /*
             * =========================================
             * LOCALIZAÇÃO CONFIRMADA
             * =========================================
             */

            if (
                locationSource ==
                "GPS" &&
                latitude != null &&
                longitude != null &&
                pickupAddress.isNotBlank()
            ) {

                Spacer(
                    modifier =
                        Modifier.height(
                            14.dp
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
                                    14.dp
                                )
                        )
                        .border(
                            width =
                                1.dp,
                            color =
                                GuinchouGreen,
                            shape =
                                RoundedCornerShape(
                                    14.dp
                                )
                        )
                        .padding(
                            14.dp
                        )
                ) {

                    Row(
                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        Box(
                            modifier = Modifier
                                .size(
                                    9.dp
                                )
                                .background(
                                    color =
                                        GuinchouGreen,
                                    shape =
                                        CircleShape
                                )
                        )


                        Spacer(
                            modifier =
                                Modifier.size(
                                    8.dp
                                )
                        )


                        Text(
                            text =
                                "Localização encontrada",
                            color =
                                GuinchouGreen,
                            fontSize =
                                13.sp,
                            fontWeight =
                                FontWeight.Bold
                        )
                    }


                    Spacer(
                        modifier =
                            Modifier.height(
                                7.dp
                            )
                    )


                    Text(
                        text =
                            pickupAddress,
                        color =
                            GuinchouWhite,
                        fontSize =
                            14.sp
                    )
                }
            }


            /*
             * =========================================
             * ERROS
             * =========================================
             */

            if (
                errorMessage != null
            ) {

                Spacer(
                    modifier =
                        Modifier.height(
                            12.dp
                        )
                )


                Text(
                    text =
                        errorMessage!!,
                    color =
                        MaterialTheme
                            .colorScheme
                            .error,
                    fontSize =
                        13.sp
                )
            }


            Spacer(
                modifier =
                    Modifier.height(
                        24.dp
                    )
            )


            /*
             * =========================================
             * MAPA TEMPORÁRIO
             * =========================================
             */

            Box(

                modifier = Modifier
                    .fillMaxWidth()
                    .height(
                        210.dp
                    )
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
                    ),

                contentAlignment =
                    Alignment.Center
            ) {

                Column(

                    horizontalAlignment =
                        Alignment.CenterHorizontally,

                    modifier =
                        Modifier.padding(
                            20.dp
                        )
                ) {

                    Box(
                        modifier = Modifier
                            .size(
                                17.dp
                            )
                            .background(
                                color =
                                    GuinchouGreen,
                                shape =
                                    CircleShape
                            )
                    )


                    Spacer(
                        modifier =
                            Modifier.height(
                                10.dp
                            )
                    )


                    Text(

                        text =

                            if (
                                locationSource ==
                                "GPS"
                            ) {

                                "Sua localização"

                            } else {

                                "Mapa"
                            },

                        color =
                            GuinchouWhite,

                        fontSize =
                            16.sp,

                        fontWeight =
                            FontWeight.Bold
                    )


                    Spacer(
                        modifier =
                            Modifier.height(
                                5.dp
                            )
                    )


                    Text(
                        text =
                            "A visualização real será adicionada com a integração do Google Maps.",
                        color =
                            GuinchouGray,
                        fontSize =
                            12.sp,
                        textAlign =
                            TextAlign.Center
                    )
                }
            }


            Spacer(
                modifier =
                    Modifier.height(
                        20.dp
                    )
            )
        }


        /*
         * =========================================
         * BOTÕES
         * =========================================
         */

        Row(

            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal =
                        20.dp,
                    vertical =
                        12.dp
                ),

            horizontalArrangement =
                Arrangement.spacedBy(
                    12.dp
                )
        ) {


            /*
             * VOLTAR
             */
            OutlinedButton(

                onClick = {

                    focusManager
                        .clearFocus()

                    keyboardController
                        ?.hide()

                    onBackClick()
                },

                modifier = Modifier
                    .weight(
                        1f
                    )
                    .height(
                        54.dp
                    ),

                shape =
                    RoundedCornerShape(
                        14.dp
                    )
            ) {

                Text(
                    text =
                        "Voltar",
                    color =
                        GuinchouWhite
                )
            }


            /*
             * CONTINUAR
             */
            Button(

                onClick = {

                    focusManager
                        .clearFocus()

                    keyboardController
                        ?.hide()


                    if (
                        pickupAddress
                            .isBlank()
                    ) {

                        errorMessage =
                            "Informe onde o veículo está."

                        return@Button
                    }


                    onContinueClick(

                        pickupAddress.trim(),

                        latitude,

                        longitude,

                        locationSource
                    )
                },

                modifier = Modifier
                    .weight(
                        1.4f
                    )
                    .height(
                        54.dp
                    ),

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
                        "Continuar",
                    fontWeight =
                        FontWeight.Bold
                )
            }
        }
    }
}


/**
 * Cores padronizadas dos campos.
 */
@Composable
private fun guinchouPickupTextFieldColors() =
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


/**
 * Formata coordenadas caso não seja
 * possível encontrar um endereço textual.
 */
private fun buildCoordinateText(
    latitude: Double,
    longitude: Double
): String {

    return String.format(
        Locale.US,
        "%.6f, %.6f",
        latitude,
        longitude
    )
}