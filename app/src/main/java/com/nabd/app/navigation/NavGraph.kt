package com.nabd.app.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.nabd.app.ui.screens.SplashScreen

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Auth : Screen("auth") // Milestone 2 Placeholder
}

@Composable
fun NabdNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onGetStarted = {
                    navController.navigate(Screen.Auth.route)
                }
            )
        }

        composable(Screen.Auth.route) {
            // Milestone 2 Placeholder
            AuthPlaceholderScreen()
        }
    }
}

@Composable
fun AuthPlaceholderScreen() {
    // Simple placeholder for Milestone 2
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Authentication Screen Coming in Milestone 2")
    }
}