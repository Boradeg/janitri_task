package com.example.janitri_task.presentation.home_screen

import com.example.janitri_task.domain.model.VitalRecord

sealed class VitalsUiState {
    object Loading : VitalsUiState()
    object Empty : VitalsUiState()
    data class Success(val list: List<VitalRecord>) : VitalsUiState()
    data class Error(val message: String) : VitalsUiState()
}
