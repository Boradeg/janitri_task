package com.example.janitri_task.presentation.home_screen

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.janitri_task.domain.repository.VitalRepository
import com.example.janitri_task.domain.model.VitalRecord
import com.example.janitri_task.presentation.service.TimerService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

import javax.inject.Inject

@HiltViewModel
class TimerViewModel @Inject constructor(
    private val repository: VitalRepository,
    application: Application
) : AndroidViewModel(application) {

    private val _time = mutableStateOf("00:00")
    val time: State<String> = _time

    private val _isServiceRunning = mutableStateOf(false)
    val isServiceRunning: State<Boolean> = _isServiceRunning

    private val _uiEvent = MutableSharedFlow<VitalsUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun setServiceRunning(running: Boolean) {
        _isServiceRunning.value = running
    }

    fun validateAndAddVitals(sys: String, dia: String, weight: String, kicks: String) {
        val error = validateInputs(sys, dia, weight, kicks)
        if (error != null) {
            emitError(error)
            return
        }

        viewModelScope.launch {
            _uiEvent.emit(VitalsUiEvent.Loading)
            val result = repository.insertVital(VitalRecord(sys, dia, weight, kicks))
            result.fold(
                onSuccess = { _uiEvent.emit(VitalsUiEvent.Success) },
                onFailure = { e -> emitError("Failed to save record: ${e.message ?: "Unknown error"}") }
            )
        }
    }

    private fun validateInputs(sys: String, dia: String, weight: String, kicks: String): String? {
        return when {
            sys.isBlank() -> "Please enter Sys BP"
            dia.isBlank() -> "Please enter Dia BP"
            weight.isBlank() -> "Please enter Weight"
            kicks.isBlank() -> "Please enter Baby Kicks"
            else -> null
        }
    }

    private fun emitError(message: String) {
        viewModelScope.launch { _uiEvent.emit(VitalsUiEvent.ShowError(message)) }
    }

    // Room DB observer
    val vitalsUiState: StateFlow<VitalsUiState> =
        repository.getAllVitals()
            .map { list ->
                if (list.isEmpty()) VitalsUiState.Empty
                else VitalsUiState.Success(list)
            }
            .catch { e ->
                emit(VitalsUiState.Error(e.localizedMessage ?: "Unknown error"))
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = VitalsUiState.Loading
            )

    // Timer BroadcastReceiver
    private val timerReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == TimerService.ACTION_TIMER_TICK) {
                val time = intent.getStringExtra(TimerService.EXTRA_TIME) ?: return
                _time.value = time
            }
        }
    }

    init {
        val filter = IntentFilter(TimerService.ACTION_TIMER_TICK)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getApplication<Application>().registerReceiver(
                timerReceiver,
                filter,
                Context.RECEIVER_EXPORTED
            )
        } else {
            ContextCompat.registerReceiver(
                getApplication(),
                timerReceiver,
                filter,
                ContextCompat.RECEIVER_NOT_EXPORTED
            )
        }
    }

    override fun onCleared() {
        getApplication<Application>().unregisterReceiver(timerReceiver)
        super.onCleared()
    }
}



