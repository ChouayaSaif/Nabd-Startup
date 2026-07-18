package com.nabd.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nabd.app.ui.theme.*
import com.nabd.app.ui.viewmodel.ProfileUiState
import com.nabd.app.ui.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onLogout: () -> Unit,
    onPatientsClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    ProfileContent(
        uiState = uiState,
        onLogoutClick = { viewModel.logout(onLogout) },
        onEditClick = { /* TODO */ },
        onPatientsClick = onPatientsClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileContent(
    uiState: ProfileUiState,
    onLogoutClick: () -> Unit,
    onEditClick: () -> Unit,
    onPatientsClick: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Text(
                        "My Profile", 
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = NabdSecondary
                        )
                    ) 
                },
                actions = {
                    IconButton(onClick = onLogoutClick) {
                        Icon(Icons.Default.Logout, contentDescription = "Logout", tint = NabdPrimary)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = NabdBackground
                )
            )
        },
        containerColor = NabdBackground
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                // Header: Profile Picture & Basic Info
                ProfileHeader(uiState.name, uiState.email)
            }

            if (uiState.isDoctor) {
                item {
                    // Doctor specific: Practice Info Card
                    ProfileCard(title = "Practice Information", icon = Icons.Default.Business) {
                        InfoRow(label = "Hospital", value = uiState.hospitalName)
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = NabdBackground)
                        InfoRow(label = "License Number", value = uiState.licenseNumber)
                    }
                }

                item {
                    // Doctor specific: Summary Card
                    ProfileCard(
                        title = "Managed Summary", 
                        icon = Icons.Default.Groups,
                        modifier = Modifier.clickable(onClick = onPatientsClick)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Total Patients Managed", style = MaterialTheme.typography.bodyLarge)
                            Text(
                                uiState.patientsManaged.toString(),
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    color = NabdPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }
            } else {
                item {
                    // Patient specific: Recent Medications
                    ProfileCard(title = "Recent Medications", icon = Icons.Default.Medication) {
                        uiState.medications.forEachIndexed { index, medication ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = NabdPrimary, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(medication, style = MaterialTheme.typography.bodyLarge)
                            }
                            if (index < uiState.medications.size - 1) {
                                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = NabdBackground)
                            }
                        }
                    }
                }
            }

            item {
                // Common: Settings/Edit
                Button(
                    onClick = onEditClick,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NabdSecondary)
                ) {
                    Icon(Icons.Default.Edit, contentDescription = null)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Edit Profile", style = MaterialTheme.typography.labelLarge)
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun ProfileHeader(name: String, email: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(NabdPrimary.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.Person,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = NabdPrimary
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = name,
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold, color = NabdSecondary)
        )
        Text(
            text = email,
            style = MaterialTheme.typography.bodyMedium.copy(color = NabdTextMuted)
        )
    }
}

@Composable
fun ProfileCard(
    title: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null, tint = NabdPrimary, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = NabdSecondary)
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            content()
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Column {
        Text(text = label, style = MaterialTheme.typography.labelSmall.copy(color = NabdTextMuted))
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = value, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium))
    }
}

@Preview(showBackground = true)
@Composable
fun DoctorProfilePreview() {
    NABDTheme {
        ProfileContent(
            uiState = ProfileUiState(
                isDoctor = true,
                name = "Dr. Houssem Chouaya",
                email = "houssem@nabd.com"
            ),
            onLogoutClick = {},
            onEditClick = {},
            onPatientsClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PatientProfilePreview() {
    NABDTheme {
        ProfileContent(
            uiState = ProfileUiState(
                isDoctor = false,
                name = "Mohamed Ben Ali",
                email = "mohamed@nabd.com",
                medications = listOf("Plavix 75mg", "Lisinopril 10mg")
            ),
            onLogoutClick = {},
            onEditClick = {},
            onPatientsClick = {}
        )
    }
}
