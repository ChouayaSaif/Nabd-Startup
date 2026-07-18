package com.nabd.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.nabd.app.data.model.Patient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class PatientUiState(
    val patients: List<Patient> = emptyList(),
    val searchQuery: String = "",
    val pairingCode: String = "",
    val isLoading: Boolean = false,
    val selectedPatient: Patient? = null,
    val error: String? = null
)

class PatientViewModel : ViewModel() {

    private val auth: FirebaseAuth = Firebase.auth
    private val firestore: FirebaseFirestore = Firebase.firestore
    private val _uiState = MutableStateFlow(PatientUiState())
    val uiState: StateFlow<PatientUiState> = _uiState.asStateFlow()

    init {
        fetchPatients()
    }

    fun fetchPatients() {
        val currentUser = auth.currentUser ?: return
        
        _uiState.update { it.copy(isLoading = true) }
        
        viewModelScope.launch {
            try {
                val result = firestore.collection("patients")
                    .whereEqualTo("caregiverId", currentUser.uid)
                    .get().await()
                
                val patientsList = result.documents.mapNotNull { doc ->
                    Patient(
                        id = doc.id,
                        name = doc.getString("name") ?: "Unknown",
                        age = doc.getLong("age")?.toInt() ?: 0,
                        gender = doc.getString("gender") ?: "Unknown",
                        isConnected = doc.getBoolean("isConnected") ?: false,
                        deviceCode = doc.getString("deviceCode"),
                        lastSync = doc.getString("lastSync")
                    )
                }
                
                if (patientsList.isEmpty()) {
                    // Seed initial data if empty for demo
                    seedMockPatients(currentUser.uid)
                } else {
                    _uiState.update { it.copy(patients = patientsList, isLoading = false) }
                }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        patients = listOf(
                            Patient("1", "Mohamed Ben Ali", 68, "Male", true, "PM-102"),
                            Patient("2", "Fatma Mansour", 72, "Female", false),
                            Patient("3", "Youssef Zidi", 55, "Male", true, "PM-405")
                        ),
                        isLoading = false,
                        error = e.localizedMessage
                    )
                }
            }
        }
    }

    private fun seedMockPatients(caregiverId: String) {
        val mocks = listOf(
            mapOf("name" to "Mohamed Ben Ali", "age" to 68, "gender" to "Male", "isConnected" to false, "caregiverId" to caregiverId),
            mapOf("name" to "Fatma Mansour", "age" to 72, "gender" to "Female", "isConnected" to false, "caregiverId" to caregiverId)
        )
        
        viewModelScope.launch {
            mocks.forEach { data ->
                firestore.collection("patients").add(data).await()
            }
            fetchPatients()
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
        if (code.isBlank() || patientId.isBlank()) return

        _uiState.update { it.copy(isLoading = true) }
        
        viewModelScope.launch {
            try {
                firestore.collection("patients").document(patientId)
                    .set(mapOf(
                        "isConnected" to true,
                        "deviceCode" to code
                    ), SetOptions.merge()).await()
                
                _uiState.update { state ->
                    val updatedPatients = state.patients.map { 
                        if (it.id == patientId) it.copy(isConnected = true, deviceCode = code) else it
                    }
                    state.copy(patients = updatedPatients, pairingCode = "", isLoading = false)
                }
                onResult(true)
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.localizedMessage) }
                onResult(false)
            }
        }
    }

    fun createPatientAndPair(name: String, age: Int, gender: String, onResult: (Boolean, String?) -> Unit) {
        val code = _uiState.value.pairingCode
        val currentUser = auth.currentUser ?: return

        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            try {
                val newPatientData = mapOf(
                    "name" to name,
                    "age" to age,
                    "gender" to gender,
                    "isConnected" to true,
                    "deviceCode" to code,
                    "caregiverId" to currentUser.uid
                )
                val docRef = firestore.collection("patients").add(newPatientData).await()
                
                fetchPatients()
                _uiState.update { it.copy(pairingCode = "", isLoading = false) }
                onResult(true, docRef.id)
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.localizedMessage) }
                onResult(false, null)
            }
        }
    }
}
