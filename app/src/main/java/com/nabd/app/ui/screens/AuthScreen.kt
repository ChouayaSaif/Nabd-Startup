package com.nabd.app.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nabd.app.ui.theme.*
import com.nabd.app.ui.viewmodel.AuthUiState
import com.nabd.app.ui.viewmodel.AuthViewModel

@Composable
fun AuthScreen(
    viewModel: AuthViewModel,
    onAuthSuccess: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    AuthContent(
        uiState = uiState,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onRoleChange = viewModel::onRoleChange,
        onHospitalNameChange = viewModel::onHospitalNameChange,
        onToggleMode = viewModel::toggleAuthMode,
        onAuthenticate = { viewModel.onAuthenticate(onAuthSuccess) }
    )
}

@Composable
fun AuthContent(
    uiState: AuthUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRoleChange: (Boolean) -> Unit,
    onHospitalNameChange: (String) -> Unit,
    onToggleMode: () -> Unit,
    onAuthenticate: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NabdBackground)
            .verticalScroll(scrollState)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = if (uiState.isSignUpMode) "Create Account" else "Welcome Back",
            style = MaterialTheme.typography.displayLarge.copy(
                color = NabdSecondary,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )
        )

        Text(
            text = if (uiState.isSignUpMode) "Join our medical community" else "Sign in to continue",
            style = MaterialTheme.typography.bodyLarge.copy(color = NabdTextMuted)
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Role Selection Toggle
        RoleSelection(
            isDoctor = uiState.isDoctor,
            onRoleChange = onRoleChange
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Form Fields
        OutlinedTextField(
            value = uiState.email,
            onValueChange = onEmailChange,
            label = { Text("Email Address") },
            placeholder = { Text("example@nabd.com") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = NabdPrimary) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = uiState.password,
            onValueChange = onPasswordChange,
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = NabdPrimary) },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true
        )

        // Dynamic Field for Doctors during Sign Up
        AnimatedVisibility(visible = uiState.isDoctor && uiState.isSignUpMode) {
            Column {
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = uiState.hospitalName,
                    onValueChange = onHospitalNameChange,
                    label = { Text("Hospital/Clinic Name") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    leadingIcon = { Icon(Icons.Default.Home, contentDescription = null, tint = NabdPrimary) },
                    singleLine = true
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Submit Button
        Button(
            onClick = onAuthenticate,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = NabdPrimary)
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text(
                    text = if (uiState.isSignUpMode) "Sign Up" else "Sign In",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Auth Mode Toggle
        TextButton(onClick = onToggleMode) {
            Text(
                text = if (uiState.isSignUpMode) 
                    "Already have an account? Sign In" 
                else 
                    "Don't have an account? Sign Up",
                color = NabdSecondary,
                fontWeight = FontWeight.Medium
            )
        }

        // Error Handling
        if (uiState.errorMessage != null) {
            Text(
                text = uiState.errorMessage!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun RoleSelection(
    isDoctor: Boolean,
    onRoleChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(Color.White, RoundedCornerShape(24.dp))
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RoleButton(
            text = "Patient",
            isSelected = !isDoctor,
            modifier = Modifier.weight(1f),
            onClick = { onRoleChange(false) }
        )
        RoleButton(
            text = "Doctor",
            isSelected = isDoctor,
            modifier = Modifier.weight(1f),
            onClick = { onRoleChange(true) }
        )
    }
}

@Composable
fun RoleButton(
    text: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = modifier.fillMaxHeight(),
        shape = RoundedCornerShape(20.dp),
        color = if (isSelected) NabdPrimary else Color.Transparent,
        contentColor = if (isSelected) Color.White else NabdTextMuted
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = text,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                fontSize = 14.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AuthScreenPreview() {
    NABDTheme {
        AuthContent(
            uiState = AuthUiState(isSignUpMode = false),
            onEmailChange = {},
            onPasswordChange = {},
            onRoleChange = {},
            onHospitalNameChange = {},
            onToggleMode = {},
            onAuthenticate = {}
        )
    }
}
