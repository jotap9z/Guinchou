package com.guinchou.app.ui.screens.home

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.android.gms.location.LocationServices
import com.guinchou.app.R
import com.guinchou.app.ui.theme.GuinchouBackground
import com.guinchou.app.ui.theme.GuinchouBorder
import com.guinchou.app.ui.theme.GuinchouGray
import com.guinchou.app.ui.theme.GuinchouGreen
import com.guinchou.app.ui.theme.GuinchouSurface
import com.guinchou.app.ui.theme.GuinchouWhite
import com.guinchou.app.viewmodel.CustomerHomeUiState
import org.maplibre.android.MapLibre
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.MapView
import org.maplibre.android.style.layers.CircleLayer
import org.maplibre.android.style.layers.PropertyFactory
import org.maplibre.android.style.sources.GeoJsonSource
import org.maplibre.geojson.Point

@Composable
fun HomeScreen(
    homeState: CustomerHomeUiState,
    onRequestTowClick: () -> Unit,
    onNotificationClick: () -> Unit = {},
    onCallsClick: () -> Unit = {},
    onPaymentsClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GuinchouBackground)
    ) {
        BoxWithConstraints(
            modifier = Modifier.fillMaxSize()
        ) {
            val compactHeight = maxHeight < 700.dp
            val mapHeight = if (compactHeight) 185.dp else 225.dp
            val customerName = homeState.home?.name ?: "Cliente"
            val firstName = customerName.trim().substringBefore(" ").ifBlank { "Cliente" }
            val completedServices = homeState.home?.completedServices ?: 0

            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .verticalScroll(rememberScrollState())
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 18.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.logo_guinchou),
                            contentDescription = "Logo Guinchou",
                            modifier = Modifier
                                .height(40.dp)
                                .weight(1f),
                            contentScale = ContentScale.Fit,
                            alignment = Alignment.CenterStart
                        )

                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .background(
                                    color = GuinchouSurface,
                                    shape = CircleShape
                                )
                                .border(
                                    width = 1.dp,
                                    color = GuinchouBorder,
                                    shape = CircleShape
                                )
                                .clickable(onClick = onNotificationClick),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "!",
                                color = GuinchouGreen,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }

                    Column(
                        modifier = Modifier.padding(horizontal = 18.dp)
                    ) {
                        Text(
                            text = if (homeState.loading) {
                                "Carregando..."
                            } else {
                                "Olá, $firstName"
                            },
                            color = GuinchouWhite,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(Modifier.height(3.dp))

                        Text(
                            text = when {
                                homeState.error != null -> homeState.error
                                else -> "Onde podemos te ajudar hoje?"
                            },
                            color = GuinchouGray,
                            fontSize = 13.sp
                        )
                    }

                    Spacer(
                        modifier = Modifier.height(
                            if (compactHeight) 14.dp else 18.dp
                        )
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .background(
                                color = GuinchouSurface,
                                shape = RoundedCornerShape(22.dp)
                            )
                            .border(
                                width = 1.dp,
                                color = GuinchouBorder,
                                shape = RoundedCornerShape(22.dp)
                            )
                            .padding(18.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .background(
                                        color = GuinchouGreen.copy(alpha = 0.14f),
                                        shape = RoundedCornerShape(14.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "↗",
                                    color = GuinchouGreen,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(Modifier.size(14.dp))

                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = "Precisa de um guincho?",
                                    color = GuinchouWhite,
                                    fontSize = 19.sp,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(Modifier.height(3.dp))

                                Text(
                                    text = "Solicite atendimento de forma rápida e segura.",
                                    color = GuinchouGray,
                                    fontSize = 12.sp
                                )
                            }
                        }

                        if (homeState.home?.activeRequestId != null) {
                            Spacer(Modifier.height(16.dp))

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        color = GuinchouGreen.copy(alpha = 0.08f),
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(9.dp)
                                        .background(
                                            GuinchouGreen,
                                            CircleShape
                                        )
                                )

                                Spacer(Modifier.size(10.dp))

                                Text(
                                    text = "Atendimento em andamento: " +
                                            (homeState.home.activeRequestStatus ?: "Em aberto"),
                                    color = GuinchouGreen,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        Spacer(Modifier.height(18.dp))

                        Button(
                            onClick = onRequestTowClick,
                            enabled = homeState.canRequestTow,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = GuinchouGreen,
                                contentColor = GuinchouBackground
                            )
                        ) {
                            Text(
                                text = "Solicitar um guincho",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        CustomerSummaryCard(
                            modifier = Modifier.weight(1f),
                            value = completedServices.toString(),
                            label = "Serviços",
                            detail = "concluídos"
                        )

                        CustomerSummaryCard(
                            modifier = Modifier.weight(1f),
                            value = if (homeState.home?.activeRequestId != null) "1" else "0",
                            label = "Em andamento",
                            detail = "atendimento"
                        )
                    }

                    Spacer(Modifier.height(18.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 18.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Sua localização",
                            color = GuinchouWhite,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f)
                        )

                        Text(
                            text = "GPS ativo",
                            color = GuinchouGreen,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(Modifier.height(10.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(mapHeight)
                            .padding(horizontal = 16.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(GuinchouSurface)
                            .border(
                                width = 1.dp,
                                color = GuinchouBorder,
                                shape = RoundedCornerShape(20.dp)
                            )
                    ) {
                        CustomerMap(
                            modifier = Modifier.fillMaxSize()
                        )

                        Row(
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(12.dp)
                                .background(
                                    color = GuinchouBackground.copy(alpha = 0.92f),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .padding(horizontal = 11.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(9.dp)
                                    .background(GuinchouGreen, CircleShape)
                            )

                            Spacer(Modifier.size(8.dp))

                            Text(
                                text = "Localização atual",
                                color = GuinchouWhite,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Spacer(Modifier.height(18.dp))

                    Text(
                        text = "Acesso rápido",
                        color = GuinchouWhite,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 18.dp)
                    )

                    Spacer(Modifier.height(10.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        CustomerQuickCard(
                            modifier = Modifier.weight(1f),
                            symbol = "🚗",
                            title = "Veículos",
                            description = "Gerencie seus veículos",
                            onClick = onProfileClick
                        )

                        CustomerQuickCard(
                            modifier = Modifier.weight(1f),
                            symbol = "🧾",
                            title = "Histórico",
                            description = "Veja seus atendimentos",
                            onClick = onProfileClick
                        )
                    }

                    Spacer(Modifier.height(12.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        CustomerQuickCard(
                            modifier = Modifier.weight(1f),
                            symbol = "💳",
                            title = "Pagamentos",
                            description = "Formas de pagamento",
                            onClick = onPaymentsClick
                        )

                        CustomerQuickCard(
                            modifier = Modifier.weight(1f),
                            symbol = "👤",
                            title = "Meu perfil",
                            description = "Conta e configurações",
                            onClick = onProfileClick
                        )
                    }

                    Spacer(Modifier.height(20.dp))
                }

                HorizontalDivider(color = GuinchouBorder)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(GuinchouBackground)
                        .navigationBarsPadding()
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BottomItem(
                        icon = Icons.Default.Home,
                        text = "Início",
                        selected = true
                    )

                    BottomItem(
                        icon = Icons.Default.Build,
                        text = "Chamados",
                        onClick = onCallsClick
                    )

                    BottomItem(
                        icon = Icons.Default.CreditCard,
                        text = "Pagamentos",
                        onClick = onPaymentsClick
                    )

                    BottomItem(
                        icon = Icons.Default.Person,
                        text = "Perfil",
                        onClick = onProfileClick
                    )
                }
            }
        }
    }
}

@Composable
private fun CustomerSummaryCard(
    modifier: Modifier = Modifier,
    value: String,
    label: String,
    detail: String
) {
    Column(
        modifier = modifier
            .background(
                color = GuinchouSurface,
                shape = RoundedCornerShape(18.dp)
            )
            .border(
                width = 1.dp,
                color = GuinchouBorder,
                shape = RoundedCornerShape(18.dp)
            )
            .padding(15.dp)
    ) {
        Text(
            text = value,
            color = GuinchouGreen,
            fontSize = 23.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(3.dp))

        Text(
            text = label,
            color = GuinchouWhite,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold
        )

        Text(
            text = detail,
            color = GuinchouGray,
            fontSize = 10.sp
        )
    }
}

@Composable
private fun CustomerQuickCard(
    modifier: Modifier = Modifier,
    symbol: String,
    title: String,
    description: String,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .background(
                color = GuinchouSurface,
                shape = RoundedCornerShape(18.dp)
            )
            .border(
                width = 1.dp,
                color = GuinchouBorder,
                shape = RoundedCornerShape(18.dp)
            )
            .clickable(onClick = onClick)
            .padding(15.dp)
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .background(
                    color = GuinchouBackground,
                    shape = RoundedCornerShape(11.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = symbol,
                fontSize = 18.sp
            )
        }

        Spacer(Modifier.height(11.dp))

        Text(
            text = title,
            color = GuinchouWhite,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(2.dp))

        Text(
            text = description,
            color = GuinchouGray,
            fontSize = 10.sp,
            maxLines = 2
        )
    }
}

@Composable
private fun CustomerMap(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var location by remember {
        mutableStateOf<LatLng?>(null)
    }

    var locationAllowed by remember {
        mutableStateOf(false)
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        locationAllowed =
            permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                    permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
    }

    LaunchedEffect(Unit) {
        val fineAllowed = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val coarseAllowed = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (fineAllowed || coarseAllowed) {
            locationAllowed = true
        } else {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    LaunchedEffect(locationAllowed) {
        if (locationAllowed) {
            try {
                LocationServices
                    .getFusedLocationProviderClient(context)
                    .lastLocation
                    .addOnSuccessListener { lastLocation ->
                        if (lastLocation != null) {
                            location = LatLng(
                                lastLocation.latitude,
                                lastLocation.longitude
                            )
                        }
                    }
            } catch (_: SecurityException) {
                locationAllowed = false
            }
        }
    }

    val mapView = remember(context) {
        MapLibre.getInstance(context)

        MapView(context).apply {
            onCreate(null)

            getMapAsync { map ->
                map.setStyle(
                    "https://tiles.openfreemap.org/styles/liberty"
                )

                // Posição inicial enquanto o GPS não fornece dados.
                map.cameraPosition = CameraPosition.Builder()
                    .target(LatLng(-15.7801, -47.9292))
                    .zoom(3.0)
                    .build()
            }
        }
    }

    DisposableEffect(mapView, lifecycleOwner) {
        val lifecycle = lifecycleOwner.lifecycle
        var destroyed = false

        if (
            lifecycle.currentState.isAtLeast(
                Lifecycle.State.STARTED
            )
        ) {
            mapView.onStart()
        }

        if (
            lifecycle.currentState.isAtLeast(
                Lifecycle.State.RESUMED
            )
        ) {
            mapView.onResume()
        }

        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START ->
                    mapView.onStart()

                Lifecycle.Event.ON_RESUME ->
                    mapView.onResume()

                Lifecycle.Event.ON_PAUSE ->
                    mapView.onPause()

                Lifecycle.Event.ON_STOP ->
                    mapView.onStop()

                Lifecycle.Event.ON_DESTROY -> {
                    mapView.onDestroy()
                    destroyed = true
                }

                else -> Unit
            }
        }

        lifecycle.addObserver(observer)

        onDispose {
            lifecycle.removeObserver(observer)

            if (!destroyed) {
                if (
                    lifecycle.currentState.isAtLeast(
                        Lifecycle.State.RESUMED
                    )
                ) {
                    mapView.onPause()
                }

                if (
                    lifecycle.currentState.isAtLeast(
                        Lifecycle.State.STARTED
                    )
                ) {
                    mapView.onStop()
                }

                mapView.onDestroy()
            }
        }
    }

    LaunchedEffect(mapView, location) {
        val currentLocation =
            location ?: return@LaunchedEffect

        mapView.getMapAsync { map ->
            map.getStyle { style ->
                val point = Point.fromLngLat(
                    currentLocation.longitude,
                    currentLocation.latitude
                )

                val sourceId =
                    "customer-location-source"

                val layerId =
                    "customer-location-layer"

                val existingSource =
                    style.getSource(sourceId) as? GeoJsonSource

                if (existingSource == null) {
                    style.addSource(
                        GeoJsonSource(
                            sourceId,
                            point
                        )
                    )

                    style.addLayer(
                        CircleLayer(
                            layerId,
                            sourceId
                        ).withProperties(
                            PropertyFactory.circleColor(
                                android.graphics.Color.rgb(
                                    28,
                                    180,
                                    95
                                )
                            ),
                            PropertyFactory.circleRadius(9f),
                            PropertyFactory.circleStrokeColor(
                                android.graphics.Color.WHITE
                            ),
                            PropertyFactory.circleStrokeWidth(3f)
                        )
                    )
                } else {
                    existingSource.setGeoJson(point)
                }

                map.cameraPosition =
                    CameraPosition.Builder()
                        .target(currentLocation)
                        .zoom(15.0)
                        .build()
            }
        }
    }

    Box(
        modifier = modifier
    ) {
        AndroidView(
            factory = { mapView },
            modifier = Modifier.fillMaxSize()
        )

        if (!locationAllowed || location == null) {
            Text(
                text = if (!locationAllowed) {
                    "Permita o acesso à localização para ver sua posição"
                } else {
                    "Aguardando localização do aparelho"
                },
                color = GuinchouWhite,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(12.dp)
                    .background(
                        GuinchouBackground,
                        RoundedCornerShape(8.dp)
                    )
                    .padding(8.dp)
            )
        }

        Text(
            text = "© OpenFreeMap · © OpenStreetMap",
            color = Color.White,
            fontSize = 10.sp,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(8.dp)
                .background(
                    GuinchouBackground,
                    RoundedCornerShape(6.dp)
                )
                .padding(
                    horizontal = 6.dp,
                    vertical = 3.dp
                )
        )
    }
}

@Composable
private fun BottomItem(
    icon: ImageVector,
    text: String,
    selected: Boolean = false,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(
                horizontal = 10.dp,
                vertical = 5.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = if (selected) {
                GuinchouGreen
            } else {
                GuinchouGray
            },
            modifier = Modifier.size(22.dp)
        )

        Spacer(
            modifier = Modifier.height(4.dp)
        )

        Text(
            text = text,
            color = if (selected) {
                GuinchouGreen
            } else {
                GuinchouGray
            },
            fontSize = 11.sp,
            fontWeight = if (selected) {
                FontWeight.SemiBold
            } else {
                FontWeight.Normal
            },
            textAlign = TextAlign.Center,
            maxLines = 1
        )

        Spacer(
            modifier = Modifier.height(3.dp)
        )

        Box(
            modifier = Modifier
                .size(
                    width = 18.dp,
                    height = 2.dp
                )
                .background(
                    color = if (selected) {
                        GuinchouGreen
                    } else {
                        Color.Transparent
                    },
                    shape = RoundedCornerShape(50)
                )
        )
    }
}
