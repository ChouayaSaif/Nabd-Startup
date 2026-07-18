package com.nabd.app.ui.screens

import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nabd.app.ui.theme.*
import com.nabd.app.ui.viewmodel.PatientUiState
import com.nabd.app.ui.viewmodel.PatientViewModel

@Composable
fun DevicePairingScreen(
    viewModel: PatientViewModel,
    patientId: String,
    onBack: () -> Unit,
    onPairSuccess: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    DevicePairingContent(
        uiState = uiState,
        onPairingCodeChange = viewModel::onPairingCodeChange,
        onBack = onBack,
        onConfirm = {
            viewModel.pairDevice(patientId) { success ->
                if (success) onPairSuccess()
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DevicePairingContent(
    uiState: PatientUiState,
    onPairingCodeChange: (String) -> Unit,
    onBack: () -> Unit,
    onConfirm: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Link PillMate", style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold, color = NabdSecondary)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = NabdSecondary)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = NabdBackground)
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = NabdBackground
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // Pairing Illustration Placeholder
            Surface(
                modifier = Modifier.size(160.dp),
                color = NabdSecondary.copy(alpha = 0.05f),
                shape = RoundedCornerShape(32.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.DeviceHub, contentDescription = null, modifier = Modifier.size(64.dp), tint = NabdSecondary)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Connect Device",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold, color = NabdSecondary)
            )
            Text(
                text = "Enter the 8-digit code found on the back of the PillMate module.",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium.copy(color = NabdTextMuted),
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Manual Entry Field
            OutlinedTextField(
                value = uiState.pairingCode,
                onValueChange = onPairingCodeChange,
                label = { Text("Device Code") },
                placeholder = { Text("e.g. PM-X42B") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Scan QR Button (Secondary)
            OutlinedButton(
                onClick = { /* TODO: Open CameraX Scanner */ },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = NabdSecondary)
            ) {
                Icon(Icons.Default.QrCodeScanner, contentDescription = null)
                Spacer(modifier = Modifier.width(12.dp))
                Text("Scan QR Code", style = MaterialTheme.typography.labelLarge)
            }

            Spacer(modifier = Modifier.weight(1f))

            // Confirm Button
            Button(
                onClick = onConfirm,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = NabdPrimary)
            ) {
                Text("Confirm Connection", style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DevicePairingPreview() {
    NABDTheme {
        DevicePairingContent(
            uiState = PatientUiState(),
            onPairingCodeChange = {},
            onBack = {},
            onConfirm = {}
        )
    }
}
