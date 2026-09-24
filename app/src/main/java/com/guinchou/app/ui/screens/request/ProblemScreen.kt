package com.guinchou.app.ui.screens.request

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import com.guinchou.app.ui.theme.GuinchouBackground
import com.guinchou.app.ui.theme.GuinchouBorder
import com.guinchou.app.ui.theme.GuinchouGray
import com.guinchou.app.ui.theme.GuinchouGreen
import com.guinchou.app.ui.theme.GuinchouSurface
import com.guinchou.app.ui.theme.GuinchouWhite
import java.io.File

@Composable
fun ProblemScreen(
    initialProblemType: String,
    initialProblemDetail: String,
    initialDescription: String,
    initialPhotoOneUri: String?,
    initialPhotoTwoUri: String?,
    onPhotoOneChanged: (String?) -> Unit,
    onPhotoTwoChanged: (String?) -> Unit,
    onContinueClick: (
        problemType: String,
        problemDetail: String,
        description: String
    ) -> Unit,
    onBackClick: () -> Unit
) {

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    var problemType by remember {
        mutableStateOf(initialProblemType)
    }

    var problemDetail by remember {
        mutableStateOf(initialProblemDetail)
    }

    var description by remember {
        mutableStateOf(initialDescription)
    }

    var customProblem by remember {
        mutableStateOf("")
    }

    var photoOneUri by remember {
        mutableStateOf(initialPhotoOneUri)
    }

    var photoTwoUri by remember {
        mutableStateOf(initialPhotoTwoUri)
    }

    var pendingCameraOneUri by remember {
        mutableStateOf<Uri?>(null)
    }

    var pendingCameraTwoUri by remember {
        mutableStateOf<Uri?>(null)
    }

    var errorMessage by remember {
        mutableStateOf<String?>(null)
    }

    val backgroundInteractionSource = remember {
        MutableInteractionSource()
    }

    val mechanicalOptions = listOf(
        "Veículo não liga",
        "Bateria descarregada",
        "Superaquecimento",
        "Pneu ou roda",
        "Falha elétrica",
        "Problema no motor",
        "Outro problema mecânico"
    )

    val accidentOptions = listOf(
        "Colisão frontal",
        "Colisão traseira",
        "Colisão lateral",
        "Capotamento",
        "Veículo fora da pista",
        "Veículo impossibilitado de rodar",
        "Outro tipo de acidente"
    )

    val galleryOneLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri ->

            if (uri != null) {
                photoOneUri = uri.toString()
                onPhotoOneChanged(uri.toString())
                errorMessage = null
            }
        }

    val galleryTwoLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri ->

            if (uri != null) {
                photoTwoUri = uri.toString()
                onPhotoTwoChanged(uri.toString())
                errorMessage = null
            }
        }

    val cameraOneLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.TakePicture()
        ) { success ->

            if (success) {
                pendingCameraOneUri?.let { uri ->
                    photoOneUri = uri.toString()
                    onPhotoOneChanged(uri.toString())
                    errorMessage = null
                }
            }
        }

    val cameraTwoLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.TakePicture()
        ) { success ->

            if (success) {
                pendingCameraTwoUri?.let { uri ->
                    photoTwoUri = uri.toString()
                    onPhotoTwoChanged(uri.toString())
                    errorMessage = null
                }
            }
        }

    fun takePhotoOne() {
        val uri = createTemporaryImageUri(context)
        pendingCameraOneUri = uri
        cameraOneLauncher.launch(uri)
    }

    fun takePhotoTwo() {
        val uri = createTemporaryImageUri(context)
        pendingCameraTwoUri = uri
        cameraTwoLauncher.launch(uri)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(GuinchouBackground)
            .clickable(
                interactionSource = backgroundInteractionSource,
                indication = null
            ) {
                focusManager.clearFocus()
                keyboardController?.hide()
            }
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {

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

            ProblemStepIndicator()

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            Text(
                text = "O que aconteceu?",
                color = GuinchouWhite,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = "Selecione a situação que melhor representa o problema do veículo.",
                color = GuinchouGray,
                fontSize = 14.sp
            )

            Spacer(
                modifier = Modifier.height(26.dp)
            )

            ProblemTypeCard(
                title = "Pane mecânica",
                description = "Falha mecânica, elétrica ou problema que impeça o veículo de continuar.",
                selected = problemType == "MECHANICAL",
                onClick = {

                    problemType = "MECHANICAL"
                    problemDetail = ""
                    customProblem = ""

                    photoOneUri = null
                    photoTwoUri = null

                    onPhotoOneChanged(null)
                    onPhotoTwoChanged(null)

                    errorMessage = null
                }
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            ProblemTypeCard(
                title = "Acidente ou colisão",
                description = "Batida, colisão, capotamento ou dano que impeça o veículo de rodar.",
                selected = problemType == "ACCIDENT",
                onClick = {

                    problemType = "ACCIDENT"
                    problemDetail = ""
                    customProblem = ""
                    errorMessage = null
                }
            )

            if (problemType.isNotBlank()) {

                Spacer(
                    modifier = Modifier.height(24.dp)
                )

                Text(
                    text = if (problemType == "ACCIDENT") {
                        "Selecione o tipo de acidente"
                    } else {
                        "Selecione o problema"
                    },
                    color = GuinchouWhite,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                val options =
                    if (problemType == "ACCIDENT") {
                        accidentOptions
                    } else {
                        mechanicalOptions
                    }

                options.forEach { option ->

                    ProblemOptionCard(
                        title = option,
                        selected = problemDetail == option,
                        onClick = {

                            problemDetail = option

                            if (
                                option != "Outro problema mecânico" &&
                                option != "Outro tipo de acidente"
                            ) {
                                customProblem = ""
                            }

                            errorMessage = null
                        }
                    )

                    Spacer(
                        modifier = Modifier.height(10.dp)
                    )
                }

                val isOtherSelected =
                    problemDetail == "Outro problema mecânico" ||
                            problemDetail == "Outro tipo de acidente"

                if (isOtherSelected) {

                    Spacer(
                        modifier = Modifier.height(4.dp)
                    )

                    OutlinedTextField(
                        value = customProblem,
                        onValueChange = {

                            customProblem = it
                            errorMessage = null
                        },
                        modifier = Modifier.fillMaxWidth(),
                        label = {
                            Text(
                                text = "Descreva o problema"
                            )
                        },
                        placeholder = {
                            Text(
                                text = "Informe o que aconteceu"
                            )
                        },
                        minLines = 2,
                        colors = problemTextFieldColors()
                    )
                }

            }

            if (problemType == "ACCIDENT") {

                Spacer(
                    modifier = Modifier.height(26.dp)
                )

                Text(
                    text = "Fotos do veículo",
                    color = GuinchouWhite,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(6.dp)
                )

                Text(
                    text = "Adicione duas fotos para que o guincheiro consiga avaliar melhor a situação.",
                    color = GuinchouGray,
                    fontSize = 13.sp
                )

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                AccidentPhotoCard(
                    title = "Foto 1",
                    subtitle = "Visão geral do veículo",
                    uriString = photoOneUri,
                    onCameraClick = {
                        takePhotoOne()
                    },
                    onGalleryClick = {
                        galleryOneLauncher.launch("image/*")
                    },
                    onRemoveClick = {

                        photoOneUri = null
                        onPhotoOneChanged(null)
                    }
                )

                Spacer(
                    modifier = Modifier.height(14.dp)
                )

                AccidentPhotoCard(
                    title = "Foto 2",
                    subtitle = "Detalhe da área danificada",
                    uriString = photoTwoUri,
                    onCameraClick = {
                        takePhotoTwo()
                    },
                    onGalleryClick = {
                        galleryTwoLauncher.launch("image/*")
                    },
                    onRemoveClick = {

                        photoTwoUri = null
                        onPhotoTwoChanged(null)
                    }
                )

                Spacer(
                    modifier = Modifier.height(14.dp)
                )

                val photoCount =
                    listOf(
                        photoOneUri,
                        photoTwoUri
                    ).count {
                        it != null
                    }

                Text(
                    text =
                        if (photoCount == 2) {
                            "2 de 2 fotos adicionadas ✓"
                        } else {
                            "$photoCount de 2 fotos adicionadas"
                        },
                    color =
                        if (photoCount == 2) {
                            GuinchouGreen
                        } else {
                            GuinchouGray
                        },
                    fontSize = 13.sp,
                    fontWeight =
                        if (photoCount == 2) {
                            FontWeight.SemiBold
                        } else {
                            FontWeight.Normal
                        }
                )
            }

            // Observações ficam ao final, depois das fotos quando houver acidente.
            if (problemType.isNotBlank()) {

                Spacer(
                    modifier = Modifier.height(26.dp)
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = {
                        description = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text(
                            text = "Observações adicionais"
                        )
                    },
                    placeholder = {
                        Text(
                            text = "Ex.: veículo em local de difícil acesso, roda travada, vazamento etc."
                        )
                    },
                    minLines = 3,
                    maxLines = 6,
                    colors = problemTextFieldColors()
                )
            }

            if (errorMessage != null) {

                Spacer(
                    modifier = Modifier.height(14.dp)
                )

                Text(
                    text = errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 13.sp
                )
            }

            Spacer(
                modifier = Modifier.height(24.dp)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 20.dp,
                    vertical = 12.dp
                ),
            horizontalArrangement =
                Arrangement.spacedBy(12.dp)
        ) {

            OutlinedButton(
                onClick = {

                    focusManager.clearFocus()
                    keyboardController?.hide()

                    onBackClick()
                },
                modifier = Modifier
                    .weight(1f)
                    .height(54.dp),
                shape = RoundedCornerShape(14.dp)
            ) {

                Text(
                    text = "Voltar",
                    color = GuinchouWhite
                )
            }

            Button(
                onClick = {

                    focusManager.clearFocus()
                    keyboardController?.hide()

                    if (problemType.isBlank()) {

                        errorMessage =
                            "Selecione se o problema é pane mecânica ou acidente."

                        return@Button
                    }

                    if (problemDetail.isBlank()) {

                        errorMessage =
                            "Selecione uma das opções disponíveis."

                        return@Button
                    }

                    val isOtherSelected =
                        problemDetail == "Outro problema mecânico" ||
                                problemDetail == "Outro tipo de acidente"

                    if (
                        isOtherSelected &&
                        customProblem.isBlank()
                    ) {

                        errorMessage =
                            "Descreva o problema para continuar."

                        return@Button
                    }

                    if (
                        problemType == "ACCIDENT" &&
                        (
                                photoOneUri == null ||
                                        photoTwoUri == null
                                )
                    ) {

                        errorMessage =
                            "Adicione as duas fotos do veículo para continuar."

                        return@Button
                    }

                    val finalProblemDetail =
                        if (isOtherSelected) {
                            customProblem.trim()
                        } else {
                            problemDetail
                        }

                    onContinueClick(
                        problemType,
                        finalProblemDetail,
                        description.trim()
                    )
                },
                modifier = Modifier
                    .weight(1.4f)
                    .height(54.dp),
                shape = RoundedCornerShape(14.dp),
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor =
                            GuinchouGreen,
                        contentColor =
                            GuinchouBackground
                    )
            ) {

                Text(
                    text = "Continuar",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun ProblemStepIndicator() {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "ETAPA 4 DE 5",
                color = GuinchouGreen,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Problema",
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
                            color = if (index <= 3) {
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
private fun ProblemTypeCard(
    title: String,
    description: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val isAccident =
        title.contains(
            "Acidente",
            ignoreCase = true
        )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color =
                    if (selected) {
                        GuinchouGreen.copy(
                            alpha = 0.07f
                        )
                    } else {
                        GuinchouSurface
                    },
                shape =
                    RoundedCornerShape(16.dp)
            )
            .border(
                width =
                    if (selected) 2.dp else 1.dp,
                color =
                    if (selected) {
                        GuinchouGreen
                    } else {
                        GuinchouBorder
                    },
                shape =
                    RoundedCornerShape(16.dp)
            )
            .clickable {
                onClick()
            }
            .padding(15.dp),
        verticalAlignment =
            Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(46.dp)
                .background(
                    color =
                        if (selected) {
                            GuinchouGreen.copy(
                                alpha = 0.12f
                            )
                        } else {
                            GuinchouBackground
                        },
                    shape =
                        RoundedCornerShape(13.dp)
                ),
            contentAlignment =
                Alignment.Center
        ) {
            Icon(
                imageVector =
                    if (isAccident) {
                        Icons.Default.Warning
                    } else {
                        Icons.Default.Build
                    },
                contentDescription = null,
                tint =
                    if (selected) {
                        GuinchouGreen
                    } else {
                        GuinchouGray
                    },
                modifier =
                    Modifier.size(23.dp)
            )
        }

        Spacer(
            modifier = Modifier.size(12.dp)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                color = GuinchouWhite,
                fontSize = 15.sp,
                fontWeight =
                    FontWeight.SemiBold
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = description,
                color = GuinchouGray,
                fontSize = 11.sp
            )
        }

        if (selected) {
            Spacer(
                modifier = Modifier.size(10.dp)
            )

            Box(
                modifier = Modifier
                    .size(27.dp)
                    .background(
                        GuinchouGreen,
                        CircleShape
                    ),
                contentAlignment =
                    Alignment.Center
            ) {
                Icon(
                    imageVector =
                        Icons.Default.Check,
                    contentDescription =
                        "Selecionado",
                    tint =
                        GuinchouBackground,
                    modifier =
                        Modifier.size(17.dp)
                )
            }
        }
    }
}


@Composable
private fun ProblemOptionCard(
    title: String,
    selected: Boolean,
    onClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color =
                    if (selected) {
                        GuinchouSurface
                    } else {
                        GuinchouBackground
                    },
                shape =
                    RoundedCornerShape(14.dp)
            )
            .border(
                width =
                    if (selected) {
                        2.dp
                    } else {
                        1.dp
                    },
                color =
                    if (selected) {
                        GuinchouGreen
                    } else {
                        GuinchouBorder
                    },
                shape =
                    RoundedCornerShape(14.dp)
            )
            .clickable {
                onClick()
            }
            .padding(
                horizontal = 16.dp,
                vertical = 15.dp
            ),
        verticalAlignment =
            Alignment.CenterVertically,
        horizontalArrangement =
            Arrangement.SpaceBetween
    ) {

        Text(
            text = title,
            color = GuinchouWhite,
            fontSize = 14.sp,
            fontWeight =
                if (selected) {
                    FontWeight.SemiBold
                } else {
                    FontWeight.Normal
                },
            modifier = Modifier.weight(1f)
        )

        if (selected) {

            Spacer(
                modifier = Modifier.size(10.dp)
            )

            Box(
                modifier = Modifier
                    .size(18.dp)
                    .background(
                        color = GuinchouGreen,
                        shape = CircleShape
                    ),
                contentAlignment =
                    Alignment.Center
            ) {

                Text(
                    text = "✓",
                    color = GuinchouBackground,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun AccidentPhotoCard(
    title: String,
    subtitle: String,
    uriString: String?,
    onCameraClick: () -> Unit,
    onGalleryClick: () -> Unit,
    onRemoveClick: () -> Unit
) {

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = GuinchouSurface,
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                width = 1.dp,
                color =
                    if (uriString != null) {
                        GuinchouGreen
                    } else {
                        GuinchouBorder
                    },
                shape = RoundedCornerShape(16.dp)
            )
            .padding(14.dp)
    ) {

        Text(
            text = title,
            color = GuinchouWhite,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(
            modifier = Modifier.height(3.dp)
        )

        Text(
            text = subtitle,
            color = GuinchouGray,
            fontSize = 12.sp
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        if (uriString != null) {

            val imageBitmap =
                rememberImageBitmapFromUri(
                    context = context,
                    uriString = uriString
                )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(190.dp)
                    .background(
                        color = GuinchouBackground,
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment =
                    Alignment.Center
            ) {

                if (imageBitmap != null) {

                    Image(
                        bitmap = imageBitmap,
                        contentDescription = title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                } else {

                    Text(
                        text = "Foto adicionada ✓",
                        color = GuinchouGreen,
                        fontSize = 13.sp
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            OutlinedButton(
                onClick = onRemoveClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {

                Text(
                    text = "Remover foto",
                    color = GuinchouWhite
                )
            }

        } else {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(
                        color = GuinchouBackground,
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment =
                    Alignment.Center
            ) {

                Column(
                    horizontalAlignment =
                        Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "+",
                        color = GuinchouGreen,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Nenhuma foto adicionada",
                        color = GuinchouGray,
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.spacedBy(10.dp)
            ) {

                Button(
                    onClick = onCameraClick,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor = GuinchouGreen,
                            contentColor = GuinchouBackground
                        )
                ) {

                    Text(
                        text = "Tirar foto",
                        fontWeight = FontWeight.SemiBold
                    )
                }

                OutlinedButton(
                    onClick = onGalleryClick,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {

                    Text(
                        text = "Galeria",
                        color = GuinchouWhite
                    )
                }
            }
        }
    }
}

@Composable
private fun problemTextFieldColors() =
    OutlinedTextFieldDefaults.colors(
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

private fun createTemporaryImageUri(
    context: Context
): Uri {

    val imagesDirectory =
        File(
            context.cacheDir,
            "images"
        )

    if (!imagesDirectory.exists()) {
        imagesDirectory.mkdirs()
    }

    val imageFile =
        File.createTempFile(
            "guinchou_",
            ".jpg",
            imagesDirectory
        )

    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        imageFile
    )
}

@Composable
private fun rememberImageBitmapFromUri(
    context: Context,
    uriString: String
): ImageBitmap? {

    return remember(uriString) {

        try {

            val uri = Uri.parse(uriString)

            context
                .contentResolver
                .openInputStream(uri)
                ?.use { inputStream ->

                    BitmapFactory
                        .decodeStream(inputStream)
                        ?.asImageBitmap()
                }

        } catch (
            exception: Exception
        ) {

            null
        }
    }
}
