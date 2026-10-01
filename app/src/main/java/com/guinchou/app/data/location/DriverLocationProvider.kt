package com.guinchou.app.data.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.SystemClock
import androidx.core.content.ContextCompat
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

object DriverLocationProvider {

    @SuppressLint("MissingPermission")
    suspend fun currentLocation(context: Context): Location {
        val granted = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ).any {
            ContextCompat.checkSelfPermission(
                context,
                it
            ) == PackageManager.PERMISSION_GRANTED
        }

        check(granted) {
            "Permita o acesso à localização para buscar chamados."
        }

        val request = CurrentLocationRequest.Builder()
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .setMaxUpdateAgeMillis(30_000L)
            .setDurationMillis(20_000L)
            .build()

        val location = withTimeoutOrNull(25_000L) {
            suspendCancellableCoroutine<Location> { continuation ->
                val token = CancellationTokenSource()

                continuation.invokeOnCancellation {
                    token.cancel()
                }

                LocationServices
                    .getFusedLocationProviderClient(context)
                    .getCurrentLocation(request, token.token)
                    .addOnSuccessListener { value ->
                        if (continuation.isActive) {
                            if (value == null) {
                                continuation.resumeWithException(
                                    IllegalStateException(
                                        "Ative o GPS e tente buscar novamente."
                                    )
                                )
                            } else {
                                continuation.resume(value)
                            }
                        }
                    }
                    .addOnFailureListener { error ->
                        if (continuation.isActive) {
                            continuation.resumeWithException(error)
                        }
                    }
                    .addOnCanceledListener {
                        if (continuation.isActive) {
                            continuation.resumeWithException(
                                IllegalStateException(
                                    "A consulta ao GPS foi interrompida. Tente novamente."
                                )
                            )
                        }
                    }
            }
        } ?: throw IllegalStateException(
            "O GPS não respondeu. Ative a localização e tente novamente."
        )

        val ageNanos =
            SystemClock.elapsedRealtimeNanos() - location.elapsedRealtimeNanos

        check(ageNanos in 0L..60_000_000_000L) {
            "A localização está desatualizada. Tente novamente com o GPS ligado."
        }

        check(
            location.latitude.isFinite() &&
                    location.latitude in -90.0..90.0
        ) {
            "Latitude inválida."
        }

        check(
            location.longitude.isFinite() &&
                    location.longitude in -180.0..180.0
        ) {
            "Longitude inválida."
        }

        return location
    }
}