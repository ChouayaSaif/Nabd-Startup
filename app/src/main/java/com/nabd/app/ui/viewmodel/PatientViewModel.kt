package com.nabd.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.nabd.app.data.model.Patient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

data class PatientUiState(
    val patients: List<Patient> = emptyList(),
    val searchQuery: String = "",
    val pairingCode: String = "",
    val isLoading: Boolean = false,
    val selectedPatient: Patient? = null
)

class PatientViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(PatientUiState())
    val uiState: StateFlow<PatientUiState> = _uiState.asStateFlow()

    init {
        // Mock initial data
        _uiState.update { 
            it.copy(
                patients = listOf(
                    Patient(UUID.randomUUID().toString(), "Mohamed Ben Ali", 68, "Male", true, "PM-102"),
                    Patient(UUID.randomUUID().toString(), "Fatma Mansour", 72, "Female", false),
                    Patient(UUID.randomUUID().toString(), "Youssef Zidi", 55, "Male", true, "PM-405")
                )
            )
        }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun onPairingCodeChange(code: String) {
        _uiState.update { it.copy(pairingCode = code) }
    }

    fun selectPatient(patient: Patient) {
        _uiState.update { it.copy(selectedPatient = patient) }
    }

    fun pairDevice(patientId: String, onResult: (Boolean) -> Unit) {
        val code = _uiState.value.pairingCode
        if (code.isBlank()) return

        _uiState.update { state ->
            val updatedPatients = state.patients.map { 
                if (it.id == patientId) it.copy(isConnected = true, deviceCode = code) else it
            }
            state.copy(patients = updatedPatients, pairingCode = "")
        }
        onResult(true)
    }
}
