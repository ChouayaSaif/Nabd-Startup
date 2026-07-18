package com.nabd.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.nabd.app.data.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class ConfigurationUiState(
    val medications: List<Medication> = emptyList(),
    val deviceSettings: DeviceSettings = DeviceSettings(),
    val isLoading: Boolean = false,
    val isSyncing: Boolean = false
)

class ConfigurationViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ConfigurationUiState())
    val uiState: StateFlow<ConfigurationUiState> = _uiState.asStateFlow()

    init {
        // Load initial medications
        _uiState.update { 
            it.copy(
                medications = listOf(
                    Medication(name = "Plavix 75mg", time = "08:00", dosage = "1 Pill", status = MedicationStatus.TAKEN),
                    Medication(name = "Lisinopril 10mg", time = "20:00", dosage = "1 Pill", status = MedicationStatus.PENDING)
                )
            )
        }
    }

    fun addMedication() {
        _uiState.update { state ->
            state.copy(medications = state.medications + Medication(name = "New Medication"))
        }
    }

    fun updateMedication(id: String, updated: Medication) {
        _uiState.update { state ->
            val list = state.medications.map { if (it.id == id) updated else it }
            state.copy(medications = list)
        }
    }

    fun removeMedication(id: String) {
        _uiState.update { state ->
            state.copy(medications = state.medications.filter { it.id != id })
        }
    }

    fun updateDeviceSettings(updated: DeviceSettings) {
        _uiState.update { it.copy(deviceSettings = updated) }
    }

    fun syncToDevice(onComplete: () -> Unit) {
        _uiState.update { it.copy(isSyncing = true) }
        // Mock sync
        android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
            _uiState.update { it.copy(isSyncing = false) }
            onComplete()
        }, 1500)
    }
}
