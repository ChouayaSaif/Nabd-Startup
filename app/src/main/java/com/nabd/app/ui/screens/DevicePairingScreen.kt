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

import androidx.compose.foundation.background
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import com.nabd.app.ui.components.QrScannerView

@Composable
fun DevicePairingScreen(
    viewModel: PatientViewModel,
    patientId: String,
    onBack: () -> Unit,
    onPairSuccess: () -> Unit // Assumed to navigate to PatientListScreen
) {
    val uiState by viewModel.uiState.collectAsState()
    var isScanning by remember { mutableStateOf(false) }

    if (isScanning) {
        Box(modifier = Modifier.fillMaxSize()) {
            QrScannerView(
                onCodeScanned = { code ->
                    viewModel.onPairingCodeChange(code)
                    isScanning = false
                }
            )
            IconButton(
                onClick = { isScanning = false },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
                    .background(Color.Black.copy(alpha = 0.5f), CircleShape)
            ) {
                Icon(Icons.Default.Close, contentDescription = "Close Scanner", tint = Color.White)
            }
        }
    } else {
        DevicePairingContent(
            uiState = uiState,
            onPairingCodeChange = viewModel::onPairingCodeChange,
            onBack = onBack,
            onScanClick = { isScanning = true },
            onConfirm = {
                // 1. Tell ViewModel to add the patient (Fire and forget, don't wait for it)
                viewModel.createPatientAndPair(
                    name = "John Doe",
                    age = 35,
                    gender = "Male"
                ) { _, _ -> }

                // 2. Unconditionally and INSTANTLY navigate to the Patient List Screen
                onPairSuccess()
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DevicePairingContent(
    uiState: PatientUiState,
    onPairingCodeChange: (String) -> Unit,
    onBack: () -> Unit,
    onScanClick: () -> Unit,
    onConfirm: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollState = rememberScrollState()

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
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Main content wrapped in scrollable column
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // Pairing Illustration
                Surface(
                    modifier = Modifier.size(140.dp),
                    color = NabdSecondary.copy(alpha = 0.05f),
                    shape = RoundedCornerShape(32.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.DeviceHub, contentDescription = null, modifier = Modifier.size(64.dp), tint = NabdSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Connect Device",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold, color = NabdSecondary)
                )
                Text(
                    text = "Enter the 8-digit code found on the back of the PillMate module.",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium.copy(color = NabdTextMuted),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Manual Entry Field
                OutlinedTextField(
                    value = uiState.pairingCode,
                    onValueChange = onPairingCodeChange,
                    label = { Text("Device Code") },
                    placeholder = { Text("e.g. RM-X42B") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Scan QR Button
                OutlinedButton(
                    onClick = onScanClick,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = NabdSecondary)
                ) {
                    Icon(Icons.Default.QrCodeScanner, contentDescription = null)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Scan QR Code", style = MaterialTheme.typography.labelLarge)
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Confirm Button - Instantly Navigates
            Button(
                onClick = onConfirm,
                modifier = Modifier.fillMaxWidth().height(60.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = NabdPrimary)
                // Removed all conditions, always enabled
            ) {
                // Completely removed the CircularProgressIndicator so it never shows loading
                Text(
                    "Confirm Connection",
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold, fontSize = 18.sp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DevicePairingPreview() {
    NABDTheme {
        DevicePairingContent(
            uiState = PatientUiState(pairingCode = "RM-X42B"),
            onPairingCodeChange = {},
            onBack = {},
            onScanClick = {},
            onConfirm = {}
        )
    }
}