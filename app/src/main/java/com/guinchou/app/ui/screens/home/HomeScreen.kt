package com.guinchou.app.ui.screens.home

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.platform.LocalContext
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
            val mapHeight = if (compactHeight) 200.dp else 260.dp

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
                            .padding(
                                horizontal = 20.dp,
                                vertical = 14.dp
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "GUINCHOU",
                                color = GuinchouWhite,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(
                                modifier = Modifier.height(2.dp)
                            )

                            Text(
                                text = "Seu socorro chegou.",
                                color = GuinchouGray,
                                fontSize = 13.sp
                            )

                            val greeting = when {
                                homeState.loading ->
                                    "Carregando seus dados..."

                                homeState.error != null ->
                                    homeState.error

                                else ->
                                    "Olá, ${homeState.home?.name ?: "Cliente"} · " +
                                            "${homeState.home?.completedServices ?: 0} " +
                                            "serviço(s) concluído(s)"
                            }

                            Text(
                                text = greeting,
                                color = GuinchouGray,
                                fontSize = 12.sp
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .border(
                                    width = 1.dp,
                                    color = GuinchouBorder,
                                    shape = CircleShape
                                )
                                .clickable(
                                    onClick = onNotificationClick
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "!",
                                color = GuinchouGreen,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

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
                    }

                    Spacer(
                        modifier = Modifier.height(
                            if (compactHeight) 12.dp else 18.dp
                        )
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .background(
                                color = GuinchouSurface,
                                shape = RoundedCornerShape(20.dp)
                            )
                            .border(
                                width = 1.dp,
                                color = GuinchouBorder,
                                shape = RoundedCornerShape(20.dp)
                            )
                            .padding(
                                if (compactHeight) 16.dp else 20.dp
                            )
                    ) {
                        Text(
                            text = "Precisa de um guincho?",
                            color = GuinchouWhite,
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(
                            modifier = Modifier.height(6.dp)
                        )

                        Text(
                            text = "Solicite atendimento para o seu veículo.",
                            color = GuinchouGray,
                            fontSize = 13.sp
                        )

                        Spacer(
                            modifier = Modifier.height(18.dp)
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    color = GuinchouBackground,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .background(
                                        color = GuinchouGreen,
                                        shape = CircleShape
                                    )
                            )

                            Spacer(
                                modifier = Modifier.size(12.dp)
                            )

                            Column {
                                Text(
                                    text = "Localização atual",
                                    color = GuinchouWhite,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold
                                )

                                Spacer(
                                    modifier = Modifier.height(2.dp)
                                )

                                Text(
                                    text = "Confira sua posição no mapa acima",
                                    color = GuinchouGray,
                                    fontSize = 12.sp
                                )
                            }
                        }

                        Spacer(
                            modifier = Modifier.height(18.dp)
                        )

                        if (homeState.home?.activeRequestId != null) {
                            val status = homeState.home.activeRequestStatus
                                ?: "Em aberto"

                            Text(
                                text = "Atendimento em andamento: $status",
                                color = GuinchouGreen,
                                fontSize = 13.sp,
                                modifier = Modifier.padding(
                                    bottom = 10.dp
                                )
                            )
                        }

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

                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )
                }

                HorizontalDivider(
                    color = GuinchouBorder
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(GuinchouBackground)
                        .navigationBarsPadding()
                        .padding(
                            horizontal = 8.dp,
                            vertical = 8.dp
                        ),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BottomItem(
                        text = "Início",
                        selected = true
                    )

                    BottomItem(
                        text = "Chamados"
                    )

                    BottomItem(
                        text = "Pagamentos"
                    )

                    BottomItem(
                        text = "Perfil",
                        onClick = onProfileClick
                    )
                }
            }
        }
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
    text: String,
    selected: Boolean = false,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(
                horizontal = 10.dp,
                vertical = 6.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .background(
                    color = if (selected) {
                        GuinchouGreen
                    } else {
                        Color.Transparent
                    },
                    shape = CircleShape
                )
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
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}