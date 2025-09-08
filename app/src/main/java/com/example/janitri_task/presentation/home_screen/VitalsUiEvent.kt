package com.example.janitri_task.presentation.home_screen


sealed class VitalsUiEvent {
    object Loading : VitalsUiEvent()
    object Success : VitalsUiEvent()
    data class ShowError(val message: String) : VitalsUiEvent()
}
