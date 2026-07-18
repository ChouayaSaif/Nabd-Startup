package com.nabd.app.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoGraph
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nabd.app.ui.theme.NabdPrimary
import com.nabd.app.ui.theme.NabdSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientOptionsSheet(
    patientName: String,
    onDismiss: () -> Unit,
    onConfigure: () -> Unit,
    onViewAnalytics: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = patientName,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = NabdSecondary
                )
            )
            Text(
                text = "Select an action to manage patient care",
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray),
                modifier = Modifier.padding(bottom = 32.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OptionCard(
                    title = "Device Configuration",
                    icon = Icons.Default.Settings,
                    color = NabdSecondary,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        onDismiss()
                        onConfigure()
                    }
                )
                OptionCard(
                    title = "AI Analytics & Trends",
                    icon = Icons.Default.AutoGraph,
                    color = NabdPrimary,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        onDismiss()
                        onViewAnalytics()
                    }
                )
            }
        }
    }
}

@Composable
fun OptionCard(
    title: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(140.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.05f)),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(color.copy(alpha = 0.2f)))
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                icon, 
                contentDescription = null, 
                tint = color, 
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = color,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                ),
                lineHeight = 16.sp
            )
        }
    }
}
