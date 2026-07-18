package com.nabd.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nabd.app.R
import com.nabd.app.ui.theme.NabdBackground
import com.nabd.app.ui.theme.NabdPrimary
import com.nabd.app.ui.theme.NabdSecondary
import com.nabd.app.ui.theme.NabdTextMuted

@Composable
fun SplashScreen(onGetStarted: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(NabdBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Symbol Logo
            Image(
                painter = painterResource(id = R.drawable.symbol),
                contentDescription = "Nabd Symbol",
                modifier = Modifier
                    .size(160.dp)
                    .padding(bottom = 24.dp),
                contentScale = ContentScale.Fit
            )

            // Logo/Brand Name
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Nabd Logo",
                modifier = Modifier
                    .height(60.dp)
                    .fillMaxWidth(0.6f),
                contentScale = ContentScale.Fit
            )
            
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Your Health, Our Pulse",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = NabdTextMuted,
                    letterSpacing = 1.sp
                )
            )

            Spacer(modifier = Modifier.height(80.dp))

            // CTA Button
            Button(
                onClick = onGetStarted,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .shadow(elevation = 8.dp, shape = RoundedCornerShape(16.dp)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = NabdPrimary
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "Get Started",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        }

        // Optional: Branding hint at the bottom
        Text(
            text = "Designed for Medical Excellence",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp),
            style = MaterialTheme.typography.labelSmall.copy(
                color = NabdSecondary.copy(alpha = 0.5f)
            )
        )
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    com.nabd.app.ui.theme.NABDTheme {
        SplashScreen(onGetStarted = {})
    }
}
