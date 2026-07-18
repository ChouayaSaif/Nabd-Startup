package com.nabd.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
                .padding(horizontal = 32.dp, vertical = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Pushes the content down so it sits comfortably in the visual center
            Spacer(modifier = Modifier.weight(1f))

// 1. Symbol Logo (The Red Heart Pulse)
            Image(
                painter = painterResource(id = R.drawable.symbol),
                contentDescription = "Nabd Heart Pulse Symbol",
                modifier = Modifier
                    .width(330.dp)           // Use width instead of size so it doesn't force a 330dp height
                    .wrapContentHeight(),    // Wraps the height tightly around the image
                contentScale = ContentScale.FillWidth
            )

// 2. Logo/Brand Name Typography (Nabd + نبض)
            Image(
                painter = painterResource(id = R.drawable.words),
                contentDescription = "Nabd Typography Logo",
                modifier = Modifier
                    .offset(y = (-20).dp)    // Use offset to forcibly pull the words up closer
                    .height(150.dp)
                    .fillMaxWidth(1f),
                contentScale = ContentScale.Fit
            )

// Adjust this spacer to account for the -20dp offset above
            Spacer(modifier = Modifier.height(4.dp))

            // 3. Updated Tagline
            Text(
                text = "Pulse & Care.\nTrusted Medical Management.",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = NabdTextMuted,
                    lineHeight = 22.sp,
                    letterSpacing = 0.5.sp
                )
            )

            // Pushes the button and footer to the bottom
            Spacer(modifier = Modifier.weight(1f))

            // 4. Subtle Loading Indicator (matching the image)
            CircularProgressIndicator(
                modifier = Modifier
                    .size(16.dp),
                color = NabdTextMuted.copy(alpha = 0.4f),
                strokeWidth = 2.dp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 5. CTA Button (Pill Shaped)
            Button(
                onClick = onGetStarted,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .shadow(elevation = 6.dp, shape = CircleShape),
                colors = ButtonDefaults.buttonColors(
                    containerColor = NabdPrimary
                ),
                shape = CircleShape
            ) {
                Text(
                    text = "Get Started",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 6. Footer Text
            Text(
                text = "Connecting your pulse to our care.",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = NabdSecondary.copy(alpha = 0.8f),
                    fontWeight = FontWeight.Normal
                )
            )
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    com.nabd.app.ui.theme.NABDTheme {
        SplashScreen(onGetStarted = {})
    }
}