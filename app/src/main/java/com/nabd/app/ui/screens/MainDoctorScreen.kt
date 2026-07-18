package com.nabd.app.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeviceHub
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nabd.app.navigation.PatientChoice
import com.nabd.app.ui.theme.NabdPrimary
import com.nabd.app.ui.theme.NabdSecondary
import com.nabd.app.ui.viewmodel.PatientViewModel
import com.nabd.app.ui.viewmodel.ProfileViewModel

@Composable
fun MainDoctorScreen(
    onLogout: () -> Unit,
    onPatientClick: (String, PatientChoice) -> Unit,
    onPairSuccess: () -> Unit,
    onEditProfile: () -> Unit
) {
    var selectedTab by rememberSaveable { mutableIntStateOf(1) } // Default to Patients (Tab 1)
    val profileViewModel: ProfileViewModel = viewModel()
    val patientViewModel: PatientViewModel = viewModel()

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = androidx.compose.ui.graphics.Color.White,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                    label = { Text("Profile") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = NabdPrimary,
                        selectedTextColor = NabdPrimary,
                        unselectedIconColor = NabdSecondary.copy(alpha = 0.6f),
                        unselectedTextColor = NabdSecondary.copy(alpha = 0.6f),
                        indicatorColor = NabdPrimary.copy(alpha = 0.1f)
                    )
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Icon(Icons.Default.Groups, contentDescription = "Patients") },
                    label = { Text("Patients") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = NabdPrimary,
                        selectedTextColor = NabdPrimary,
                        unselectedIconColor = NabdSecondary.copy(alpha = 0.6f),
                        unselectedTextColor = NabdSecondary.copy(alpha = 0.6f),
                        indicatorColor = NabdPrimary.copy(alpha = 0.1f)
                    )
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = { Icon(Icons.Default.DeviceHub, contentDescription = "Pairing") },
                    label = { Text("Pairing") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = NabdPrimary,
                        selectedTextColor = NabdPrimary,
                        unselectedIconColor = NabdSecondary.copy(alpha = 0.6f),
                        unselectedTextColor = NabdSecondary.copy(alpha = 0.6f),
                        indicatorColor = NabdPrimary.copy(alpha = 0.1f)
                    )
                )
            }
        }
    ) { paddingValues ->
        Surface(modifier = Modifier.padding(paddingValues)) {
            when (selectedTab) {
                0 -> ProfileScreen(
                    viewModel = profileViewModel,
                    onLogout = onLogout,
                    onPatientsClick = { selectedTab = 1 },
                    onEditClick = onEditProfile
                )
                1 -> PatientListScreen(
                    viewModel = patientViewModel,
                    onPatientClick = onPatientClick,
                    onAddPatient = { selectedTab = 2 },
                    onBack = { /* Handled by bottom nav */ }
                )
                2 -> {
                    // Get first unlinked patient for demo purposes or provide a generic pairing screen
                    val patientId = patientViewModel.uiState.collectAsState().value.patients.firstOrNull { !it.isConnected }?.id ?: ""
                    DevicePairingScreen(
                        viewModel = patientViewModel,
                        patientId = patientId,
                        onBack = { selectedTab = 1 },
                        onPairSuccess = {
                            selectedTab = 1
                            onPairSuccess()
                        }
                    )
                }
            }
        }
    }
}
