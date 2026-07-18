package com.nabd.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class AuthUiState(
    val email: String = "",
    val password: String = "",
    val isDoctor: Boolean = false,
    val hospitalName: String = "",
    val isSignUpMode: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = Firebase.auth
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun onEmailChange(newValue: String) {
        _uiState.update { it.copy(email = newValue, errorMessage = null) }
    }

    fun onPasswordChange(newValue: String) {
        _uiState.update { it.copy(password = newValue, errorMessage = null) }
    }

    fun onRoleChange(isDoctor: Boolean) {
        _uiState.update { it.copy(isDoctor = isDoctor) }
    }

    fun onHospitalNameChange(newValue: String) {
        _uiState.update { it.copy(hospitalName = newValue) }
    }

    fun toggleAuthMode() {
        _uiState.update { it.copy(isSignUpMode = !it.isSignUpMode, errorMessage = null) }
    }

    fun onAuthenticate(onSuccess: () -> Unit) {
        val currentState = _uiState.value
        
        // Basic Validation
        if (currentState.email.isBlank() || currentState.password.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Please fill in all fields") }
            return
        }

        if (currentState.isDoctor && currentState.isSignUpMode && currentState.hospitalName.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Please enter your hospital/clinic name") }
            return
        }

        _uiState.update { it.copy(isLoading = true, errorMessage = null) }

        viewModelScope.launch {
            try {
                if (currentState.isSignUpMode) {
                    // Sign Up
                    auth.createUserWithEmailAndPassword(currentState.email, currentState.password).await()
                    // TODO: Store role and hospitalName in Firestore/Database here
                } else {
                    // Sign In
                    auth.signInWithEmailAndPassword(currentState.email, currentState.password).await()
                }
                _uiState.update { it.copy(isLoading = false) }
                onSuccess()
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isLoading = false, 
                        errorMessage = e.localizedMessage ?: "Authentication failed"
                    ) 
                }
            }
        }
    }
    
    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
