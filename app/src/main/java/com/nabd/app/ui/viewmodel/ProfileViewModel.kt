package com.nabd.app.ui.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class ProfileUiState(
    val name: String = "",
    val email: String = "",
    val isDoctor: Boolean = true,
    val hospitalName: String = "",
    val licenseNumber: String = "",
    val profileImageUrl: String? = null,
    val patientsManaged: Int = 0,
    val medications: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val isUpdating: Boolean = false,
    val error: String? = null
)

class ProfileViewModel : ViewModel() {

    private val auth: FirebaseAuth = Firebase.auth
    private val firestore: FirebaseFirestore = Firebase.firestore
    private val storage: FirebaseStorage = Firebase.storage
    
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        fetchProfile()
    }

    fun fetchProfile() {
        val currentUser = auth.currentUser ?: return
        
        _uiState.update { it.copy(isLoading = true, email = currentUser.email ?: "") }
        
        viewModelScope.launch {
            try {
                val document = firestore.collection("users").document(currentUser.uid).get().await()
                if (document.exists()) {
                    _uiState.update { 
                        it.copy(
                            name = document.getString("name") ?: currentUser.displayName ?: "",
                            hospitalName = document.getString("hospitalName") ?: "",
                            licenseNumber = document.getString("licenseNumber") ?: "",
                            profileImageUrl = document.getString("profileImageUrl"),
                            isDoctor = document.getBoolean("isDoctor") ?: true,
                            patientsManaged = document.getLong("patientsManaged")?.toInt() ?: 0,
                            isLoading = false
                        )
                    }
                } else {
                    _uiState.update { 
                        it.copy(
                            name = currentUser.displayName ?: "",
                            isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = "Failed to load profile: ${e.localizedMessage}") }
            }
        }
    }

    fun updateProfile(name: String, hospital: String, license: String, onComplete: () -> Unit) {
        val currentUser = auth.currentUser ?: return
        
        _uiState.update { it.copy(isUpdating = true, error = null) }
        
        val updates = mapOf(
            "name" to name,
            "hospitalName" to hospital,
            "licenseNumber" to license
        )
        
        viewModelScope.launch {
            try {
                // Use set with merge instead of update to ensure document exists
                firestore.collection("users").document(currentUser.uid)
                    .set(updates, SetOptions.merge()).await()
                
                _uiState.update { 
                    it.copy(
                        name = name,
                        hospitalName = hospital,
                        licenseNumber = license,
                        isUpdating = false
                    )
                }
                onComplete()
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.update { it.copy(isUpdating = false, error = "Update failed: ${e.localizedMessage}") }
            }
        }
    }

    fun uploadProfilePicture(uri: Uri) {
        val currentUser = auth.currentUser ?: return
        
        _uiState.update { it.copy(isUpdating = true, error = null) }
        
        val ref = storage.reference.child("profile_pictures/${currentUser.uid}.jpg")
        
        viewModelScope.launch {
            try {
                // Perform upload
                val uploadTask = ref.putFile(uri).await()
                
                // Get URL - only if upload was successful
                val downloadUrl = ref.downloadUrl.await().toString()
                
                // Update Firestore with new URL using set(merge)
                firestore.collection("users").document(currentUser.uid)
                    .set(mapOf("profileImageUrl" to downloadUrl), SetOptions.merge()).await()
                
                _uiState.update { 
                    it.copy(
                        profileImageUrl = downloadUrl,
                        isUpdating = false
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.update { it.copy(isUpdating = false, error = "Upload failed: ${e.localizedMessage}") }
            }
        }
    }

    fun logout(onLogoutSuccess: () -> Unit) {
        auth.signOut()
        onLogoutSuccess()
    }
    
    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
