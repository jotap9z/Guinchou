package com.guinchou.app.ui.screens.partner.company

import android.content.Intent
import android.graphics.Bitmap
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import com.guinchou.app.ui.theme.GuinchouBackground
import com.guinchou.app.ui.theme.GuinchouBorder
import com.guinchou.app.ui.theme.GuinchouGray
import com.guinchou.app.ui.theme.GuinchouGreen
import com.guinchou.app.ui.theme.GuinchouSurface
import com.guinchou.app.ui.theme.GuinchouWhite
import java.util.UUID

private enum class CompanyInviteKind {
    DRIVER,
    TOW_TRUCK
}

@Composable
fun DriverInviteQrScreen(
    companyName: String =
        "Empresa de Guinchos",
    onBackClick: () -> Unit = {}
) {

    CompanyInviteQrScreen(
        kind =
            CompanyInviteKind.DRIVER,
        companyName =
            companyName,
        onBackClick =
            onBackClick
    )
}

@Composable
fun TowTruckInviteQrScreen(
    companyName: String =
        "Empresa de Guinchos",
    onBackClick: () -> Unit = {}
) {

    CompanyInviteQrScreen(
        kind =
            CompanyInviteKind.TOW_TRUCK,
        companyName =
            companyName,
        onBackClick =
            onBackClick
    )
}

@OptIn(
    ExperimentalMaterial3Api::class
)
@Composable
private fun CompanyInviteQrScreen(
    kind: CompanyInviteKind,
    companyName: String,
    onBackClick: () -> Unit
) {

    val context =
        LocalContext.current

    val clipboard =
        LocalClipboardManager.current

    var inviteToken by remember {
        mutableStateOf(
            UUID.randomUUID()
                .toString()
                .replace(
                    "-",
                    ""
                )
                .take(
                    20
                )
        )
    }

    val inviteUrl =
        remember(
            inviteToken,
            kind
        ) {

            when (kind) {

                CompanyInviteKind.DRIVER ->
                    "https://guinchou.app/convite/motorista/$inviteToken"

                CompanyInviteKind.TOW_TRUCK ->
                    "https://guinchou.app/convite/guincho/$inviteToken"
            }
        }

    val qrBitmap =
        remember(
            inviteUrl
        ) {
            generateQrBitmap(
                inviteUrl,
                900
            )
        }

    val title =
        if (
            kind ==
            CompanyInviteKind.DRIVER
        ) {
            "Convidar motorista"
        } else {
            "Cadastrar guincho"
        }

    val description =
        if (
            kind ==
            CompanyInviteKind.DRIVER
        ) {

            "Envie este QR Code ao funcionário. " +
                    "Ele preencherá os próprios dados e documentos e o cadastro ficará vinculado à sua empresa."

        } else {

            "Envie este QR Code à pessoa responsável pelo veículo. " +
                    "Ela preencherá os dados e documentos do guincho para vinculá-lo à frota."
        }

    Scaffold(
        containerColor =
            GuinchouBackground,
        topBar = {

            TopAppBar(
                colors =
                    TopAppBarDefaults
                        .topAppBarColors(
                            containerColor =
                                GuinchouBackground
                        ),
                navigationIcon = {

                    IconButton(
                        onClick =
                            onBackClick
                    ) {

                        Icon(
                            imageVector =
                                Icons.AutoMirrored
                                    .Filled
                                    .ArrowBack,
                            contentDescription =
                                "Voltar",
                            tint =
                                GuinchouWhite
                        )
                    }
                },
                title = {

                    Text(
                        text =
                            title,
                        color =
                            GuinchouWhite,
                        fontWeight =
                            FontWeight.Bold
                    )
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    padding
                )
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(
                    20.dp
                ),
            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            Surface(
                color =
                    GuinchouGreen.copy(
                        alpha =
                            0.08f
                    ),
                shape =
                    RoundedCornerShape(
                        16.dp
                    ),
                border =
                    BorderStroke(
                        1.dp,
                        GuinchouGreen.copy(
                            alpha =
                                0.28f
                        )
                    )
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            15.dp
                        ),
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Icon(
                        imageVector =
                            if (
                                kind ==
                                CompanyInviteKind.DRIVER
                            ) {
                                Icons.Default.PersonAdd
                            } else {
                                Icons.Default.QrCode2
                            },
                        contentDescription =
                            null,
                        tint =
                            GuinchouGreen
                    )

                    Spacer(
                        Modifier.width(
                            10.dp
                        )
                    )

                    Column {

                        Text(
                            text =
                                companyName,
                            color =
                                GuinchouWhite,
                            fontWeight =
                                FontWeight.SemiBold
                        )

                        Text(
                            text =
                                "Convite empresarial",
                            color =
                                GuinchouGray,
                            fontSize =
                                12.sp
                        )
                    }
                }
            }

            Spacer(
                Modifier.height(
                    24.dp
                )
            )

            Text(
                text =
                    title,
                color =
                    GuinchouWhite,
                fontSize =
                    24.sp,
                fontWeight =
                    FontWeight.Bold
            )

            Spacer(
                Modifier.height(
                    8.dp
                )
            )

            Text(
                text =
                    description,
                color =
                    GuinchouGray,
                fontSize =
                    14.sp,
                lineHeight =
                    20.sp
            )

            Spacer(
                Modifier.height(
                    24.dp
                )
            )

            Surface(
                color =
                    Color.White,
                shape =
                    RoundedCornerShape(
                        22.dp
                    ),
                modifier =
                    Modifier.fillMaxWidth()
            ) {

                if (
                    qrBitmap != null
                ) {

                    Image(
                        bitmap =
                            qrBitmap.asImageBitmap(),
                        contentDescription =
                            "QR Code do convite",
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(
                                1f
                            )
                            .padding(
                                24.dp
                            )
                    )

                } else {

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(
                                1f
                            ),
                        contentAlignment =
                            Alignment.Center
                    ) {

                        Text(
                            text =
                                "Não foi possível gerar o QR Code",
                            color =
                                Color.Black
                        )
                    }
                }
            }

            Spacer(
                Modifier.height(
                    18.dp
                )
            )

            Text(
                text =
                    "Cada QR Code possui um identificador de convite próprio. " +
                            "Nesta versão ele é demonstrativo; quando o backend for conectado, o token será criado e validado pelo servidor.",
                color =
                    GuinchouGray,
                fontSize =
                    12.sp,
                lineHeight =
                    18.sp
            )

            Spacer(
                Modifier.height(
                    20.dp
                )
            )

            Button(
                onClick = {

                    val shareIntent =
                        Intent(
                            Intent.ACTION_SEND
                        ).apply {

                            type =
                                "text/plain"

                            putExtra(
                                Intent.EXTRA_TEXT,
                                "Convite Guinchou - $companyName\n$inviteUrl"
                            )
                        }

                    context.startActivity(
                        Intent.createChooser(
                            shareIntent,
                            "Compartilhar convite"
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(
                        52.dp
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
                                Color.Black
                        )
            ) {

                Icon(
                    imageVector =
                        Icons.Default.Share,
                    contentDescription =
                        null
                )

                Spacer(
                    Modifier.width(
                        8.dp
                    )
                )

                Text(
                    text =
                        "Compartilhar convite",
                    fontWeight =
                        FontWeight.Bold
                )
            }

            Spacer(
                Modifier.height(
                    10.dp
                )
            )

            OutlinedButton(
                onClick = {

                    clipboard.setText(
                        AnnotatedString(
                            inviteUrl
                        )
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(
                        50.dp
                    ),
                shape =
                    RoundedCornerShape(
                        14.dp
                    ),
                border =
                    BorderStroke(
                        1.dp,
                        GuinchouBorder
                    )
            ) {

                Icon(
                    imageVector =
                        Icons.Default.ContentCopy,
                    contentDescription =
                        null,
                    tint =
                        GuinchouGreen
                )

                Spacer(
                    Modifier.width(
                        8.dp
                    )
                )

                Text(
                    text =
                        "Copiar link",
                    color =
                        GuinchouWhite
                )
            }

            Spacer(
                Modifier.height(
                    10.dp
                )
            )

            TextButton(
                onClick = {

                    inviteToken =
                        UUID.randomUUID()
                            .toString()
                            .replace(
                                "-",
                                ""
                            )
                            .take(
                                20
                            )
                }
            ) {

                Text(
                    text =
                        "Gerar novo convite",
                    color =
                        GuinchouGreen,
                    fontWeight =
                        FontWeight.SemiBold
                )
            }
        }
    }
}

private fun generateQrBitmap(
    content: String,
    size: Int
): Bitmap? {

    return try {

        val hints =
            mapOf(
                EncodeHintType.ERROR_CORRECTION to
                        ErrorCorrectionLevel.M,

                EncodeHintType.MARGIN to
                        1
            )

        val matrix =
            QRCodeWriter()
                .encode(
                    content,
                    BarcodeFormat.QR_CODE,
                    size,
                    size,
                    hints
                )

        val bitmap =
            Bitmap.createBitmap(
                size,
                size,
                Bitmap.Config.RGB_565
            )

        for (
        x in
        0 until size
        ) {

            for (
            y in
            0 until size
            ) {

                bitmap.setPixel(
                    x,
                    y,
                    if (
                        matrix[x, y]
                    ) {
                        android.graphics.Color.BLACK
                    } else {
                        android.graphics.Color.WHITE
                    }
                )
            }
        }

        bitmap

    } catch (
        _: Exception
    ) {

        null
    }
}