package com.nabd.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nabd.app.data.model.*
import com.nabd.app.ui.theme.*
import com.nabd.app.ui.viewmodel.ConfigurationUiState
import com.nabd.app.ui.viewmodel.ConfigurationViewModel

@Composable
fun ConfigurationScreen(
    viewModel: ConfigurationViewModel,
    patientId: String,
    onBack: () -> Unit,
    onViewAnalytics: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    ConfigurationContent(
        uiState = uiState,
        onBack = onBack,
        onViewAnalytics = { onViewAnalytics(patientId) },
        onAddMedication = viewModel::addMedication,
        onUpdateMedication = viewModel::updateMedication,
        onRemoveMedication = viewModel::removeMedication,
        onUpdateSettings = viewModel::updateDeviceSettings,
        onSync = {
            viewModel.syncToDevice {
                // Show Success Snackbar
            }
        },
        snackbarHostState = snackbarHostState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfigurationContent(
    uiState: ConfigurationUiState,
    onBack: () -> Unit,
    onViewAnalytics: () -> Unit,
    onAddMedication: () -> Unit,
    onUpdateMedication: (String, Medication) -> Unit,
    onRemoveMedication: (String) -> Unit,
    onUpdateSettings: (DeviceSettings) -> Unit,
    onSync: () -> Unit,
    snackbarHostState: SnackbarHostState
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Configure Device", style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold, color = NabdSecondary)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = NabdSecondary)
                    }
                },
                actions = {
                    IconButton(onClick = onViewAnalytics) {
                        Icon(Icons.Default.AutoGraph, contentDescription = "AI Insights", tint = NabdPrimary)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = NabdBackground)
            )
        },
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.White,
                shadowElevation = 8.dp
            ) {
                Button(
                    onClick = onSync,
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NabdPrimary),
                    enabled = !uiState.isSyncing
                ) {
                    if (uiState.isSyncing) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    } else {
                        Icon(Icons.Default.Sync, contentDescription = null)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Save and Sync Configuration", style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold))
                    }
                }
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = NabdBackground
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 32.dp)
        ) {
            item {
                SectionHeader(title = "Medication Schedule", icon = Icons.Default.Medication)
            }

            items(uiState.medications, key = { it.id }) { medication ->
                MedicationConfigCard(
                    medication = medication,
                    onUpdate = { onUpdateMedication(medication.id, it) },
                    onDelete = { onRemoveMedication(medication.id) }
                )
            }

            item {
                OutlinedButton(
                    onClick = onAddMedication,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = NabdPrimary)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Add Medication", fontWeight = FontWeight.Bold)
                }
            }

            item {
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color.LightGray.copy(alpha = 0.3f))
                SectionHeader(title = "Advanced Device Settings", icon = Icons.Default.Settings)
            }

            item {
                AdvancedSettingsCard(
                    settings = uiState.deviceSettings,
                    onUpdate = onUpdateSettings
                )
            }
        }
    }
}

@Composable
fun MedicationConfigCard(
    medication: Medication,
    onUpdate: (Medication) -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = medication.name,
                    onValueChange = { onUpdate(medication.copy(name = it)) },
                    label = { Text("Drug Name") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.DeleteOutline, contentDescription = "Delete", tint = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                // Image Upload Placeholder
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(NabdBackground, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(onClick = { /* Image Picker Placeholder */ }) {
                        Icon(Icons.Default.AddAPhoto, contentDescription = "Upload", tint = NabdSecondary)
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { /* TODO: Show M3 TimePickerDialog */ },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.AccessTime, contentDescription = null, tint = NabdPrimary, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Time: ${medication.time}", style = MaterialTheme.typography.bodyMedium)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Scale, contentDescription = null, tint = NabdPrimary, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Dosage: ${medication.dosage}", style = MaterialTheme.typography.bodyMedium)
                    }
                }

                // Status Badge
                Surface(
                    color = if (medication.status == MedicationStatus.TAKEN) Color(0xFF4CAF50).copy(alpha = 0.1f) else Color(0xFFFF9800).copy(alpha = 0.1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = medication.status.name,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = if (medication.status == MedicationStatus.TAKEN) Color(0xFF4CAF50) else Color(0xFFFF9800),
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun AdvancedSettingsCard(
    settings: DeviceSettings,
    onUpdate: (DeviceSettings) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = NabdSecondary), // Navy Blue Card
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            // Notification Prefs
            Text("Notification Alert Pattern", color = Color.White.copy(alpha = 0.7f), style = MaterialTheme.typography.labelSmall)
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(NotificationPreference.NOTIFICATION_ONLY, NotificationPreference.VIBRATION_ONLY, NotificationPreference.BOTH).forEach { pref ->
                    val isSelected = settings.notificationPref == pref
                    FilterChip(
                        selected = isSelected,
                        onClick = { onUpdate(settings.copy(notificationPref = pref)) },
                        label = { Text(pref.name.replace("_", " "), fontSize = 10.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = NabdPrimary,
                            selectedLabelColor = Color.White,
                            containerColor = Color.White.copy(alpha = 0.1f),
                            labelColor = Color.White.copy(alpha = 0.7f)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Reminder Interval
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text("Reminder Interval", color = Color.White)
                Text("${settings.reminderInterval} mins", color = NabdPrimary, fontWeight = FontWeight.Bold)
            }
            Slider(
                value = settings.reminderInterval.toFloat(),
                onValueChange = { onUpdate(settings.copy(reminderInterval = it.toInt())) },
                valueRange = 5f..60f,
                steps = 11,
                colors = SliderDefaults.colors(thumbColor = NabdPrimary, activeTrackColor = NabdPrimary)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Toggles
            SettingToggle(
                label = "Critical Missed Alerts",
                checked = settings.criticalAlertsEnabled,
                onCheckedChange = { onUpdate(settings.copy(criticalAlertsEnabled = it)) }
            )
            SettingToggle(
                label = "Enable PillMate Device",
                checked = settings.deviceEnabled,
                onCheckedChange = { onUpdate(settings.copy(deviceEnabled = it)) }
            )
        }
    }
}

@Composable
fun SettingToggle(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = Color.White, style = MaterialTheme.typography.bodyLarge)
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(checkedThumbColor = NabdPrimary, checkedTrackColor = NabdPrimary.copy(alpha = 0.3f))
        )
    }
}

@Composable
fun SectionHeader(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = NabdSecondary, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Text(title, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = NabdSecondary))
    }
}

@Preview(showBackground = true)
@Composable
fun ConfigurationPreview() {
    NABDTheme {
        ConfigurationContent(
            uiState = ConfigurationUiState(
                medications = listOf(
                    Medication(name = "Plavix 75mg", time = "08:00", dosage = "1 Pill", status = MedicationStatus.TAKEN),
                    Medication(name = "Lisinopril 10mg", time = "20:00", dosage = "1 Pill", status = MedicationStatus.PENDING)
                )
            ),
            onBack = {},
            onViewAnalytics = {},
            onAddMedication = {},
            onUpdateMedication = { _, _ -> },
            onRemoveMedication = { _ -> },
            onUpdateSettings = {},
            onSync = {},
            snackbarHostState = SnackbarHostState()
        )
    }
}
