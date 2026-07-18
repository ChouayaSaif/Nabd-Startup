package com.nabd.app.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nabd.app.data.model.Patient
import com.nabd.app.navigation.PatientChoice
import com.nabd.app.ui.components.PatientOptionsSheet
import com.nabd.app.ui.theme.*
import com.nabd.app.ui.viewmodel.PatientUiState
import com.nabd.app.ui.viewmodel.PatientViewModel

@Composable
fun PatientListScreen(
    viewModel: PatientViewModel,
    onPatientClick: (String, PatientChoice) -> Unit,
    onAddPatient: () -> Unit,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedPatientForHub by remember { mutableStateOf<Patient?>(null) }

    PatientListContent(
        uiState = uiState,
        onSearchQueryChange = viewModel::onSearchQueryChange,
        onPatientClick = { patientId ->
            selectedPatientForHub = uiState.patients.find { it.id == patientId }
        },
        onAddPatient = onAddPatient,
        onBack = onBack
    )

    val currentPatient = selectedPatientForHub
    if (currentPatient != null) {
        PatientOptionsSheet(
            patientName = currentPatient.name,
            onDismiss = { selectedPatientForHub = null },
            onConfigure = { onPatientClick(currentPatient.id, PatientChoice.CONFIGURE) },
            onViewAnalytics = { onPatientClick(currentPatient.id, PatientChoice.ANALYTICS) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientListContent(
    uiState: PatientUiState,
    onSearchQueryChange: (String) -> Unit,
    onPatientClick: (String) -> Unit,
    onAddPatient: () -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("My Patients", style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold, color = NabdSecondary)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = NabdSecondary)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = NabdBackground)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddPatient,
                containerColor = NabdPrimary,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Patient")
            }
        },
        containerColor = NabdBackground
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
        ) {
            // Search Bar
            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = onSearchQueryChange,
                placeholder = { Text("Search patients...") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = NabdTextMuted) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                val filteredPatients = uiState.patients.filter { 
                    it.name.contains(uiState.searchQuery, ignoreCase = true) 
                }
                
                items(filteredPatients) { patient ->
                    PatientCard(patient = patient, onClick = { onPatientClick(patient.id) })
                }
            }
        }
    }
}

@Composable
fun PatientCard(patient: Patient, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = NabdSecondary), // Deep navy background
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = patient.name,
                    style = MaterialTheme.typography.titleLarge.copy(color = Color.White, fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "${patient.age}Y | ${patient.gender}",
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.White.copy(alpha = 0.7f))
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Connection Status Chip
                Surface(
                    color = if (patient.isConnected) Color(0xFF4CAF50) else Color(0xFFFF9800),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = if (patient.isConnected) "Linked: ${patient.deviceCode}" else "Unlinked",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall.copy(color = Color.White, fontWeight = FontWeight.Bold)
                    )
                }
            }
            
            Icon(
                Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.5f),
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PatientListPreview() {
    NABDTheme {
        PatientListContent(
            uiState = PatientUiState(
                patients = listOf(
                    Patient("1", "Mohamed Ben Ali", 68, "Male", true, "PM-102"),
                    Patient("2", "Fatma Mansour", 72, "Female", false)
                )
            ),
            onSearchQueryChange = {},
            onPatientClick = {},
            onAddPatient = {},
            onBack = {}
        )
    }
}
