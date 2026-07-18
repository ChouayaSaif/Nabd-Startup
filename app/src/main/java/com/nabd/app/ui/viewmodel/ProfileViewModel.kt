package com.nabd.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class ProfileUiState(
    val name: String = "Dr. Houssem Chouaya",
    val email: String = "houssem@nabd.com",
    val isDoctor: Boolean = true,
    val hospitalName: String = "Tunis Cardiology Center",
    val licenseNumber: String = "TN-123456-MED",
    val patientsManaged: Int = 142,
    val medications: List<String> = listOf("Plavix 75mg", "Lisinopril 10mg", "Metformin 500mg"),
    val isLoading: Boolean = false
)

class ProfileViewModel : ViewModel() {

    private val auth: FirebaseAuth = Firebase.auth
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        // In a real app, we would fetch the user profile from Firestore here
        val currentUser = auth.currentUser
        if (currentUser != null) {
            _uiState.update { 
                it.copy(
                    email = currentUser.email ?: "",
                    name = currentUser.displayName ?: "User Name"
                )
            }
        }
    }

    fun logout(onLogoutSuccess: () -> Unit) {
        auth.signOut()
        onLogoutSuccess()
    }
}
