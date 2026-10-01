package com.guinchou.app.ui.screens.request

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.guinchou.app.viewmodel.CustomerTowRequestSubmissionViewModel
import com.guinchou.app.viewmodel.TowRequestViewModel

@Composable
fun CustomerRequestConfirmationRoute(
    towRequestViewModel: TowRequestViewModel,
    onRegistered: () -> Unit,
    onBackClick: () -> Unit,
    onHomeClick: () -> Unit
) {
    val submissionViewModel: CustomerTowRequestSubmissionViewModel = viewModel()
    val state by submissionViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(state.createdRequestId) {
        if (state.createdRequestId != null) {
            onRegistered()
        }
    }

    RequestConfirmationScreen(
        pickupAddress = towRequestViewModel.pickupAddress,
        destinationAddress = towRequestViewModel.destinationAddress,
        vehicleDescription = listOf(
            towRequestViewModel.vehicleBrand,
            towRequestViewModel.vehicleModel,
            towRequestViewModel.vehiclePlate
        ).filter { it.isNotBlank() }.joinToString(" · "),
        problemDescription = listOf(
            towRequestViewModel.problemDetail.ifBlank {
                towRequestViewModel.problemType
            },
            towRequestViewModel.problemDescription
        ).filter { it.isNotBlank() }.joinToString("\n"),
        photosRequired = towRequestViewModel.problemType == "ACCIDENT",
        submitting = state.submitting,
        errorMessage = state.errorMessage,
        createdRequestId = state.createdRequestId,
        onConfirmClick = {
            submissionViewModel.submit(towRequestViewModel)
        },
        onBackClick = {
            if (!state.submitting && state.createdRequestId == null) {
                onBackClick()
            }
        },
        onHomeClick = {
            if (!state.submitting && state.createdRequestId != null) {
                onHomeClick()
            }
        }
    )
}