package com.nabd.app.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.nabd.app.ui.screens.*
import com.nabd.app.ui.viewmodel.*

enum class PatientChoice {
    CONFIGURE, ANALYTICS
}

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Auth : Screen("auth")
    object MainDoctor : Screen("main_doctor")
    object PatientConfig : Screen("patient_config/{patientId}") {
        fun createRoute(patientId: String) = "patient_config/$patientId"
    }
    object PatientAnalytics : Screen("patient_analytics/{patientId}") {
        fun createRoute(patientId: String) = "patient_analytics/$patientId"
    }
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
            val authViewModel: AuthViewModel = viewModel()
            AuthScreen(
                viewModel = authViewModel,
                onAuthSuccess = {
                    navController.navigate(Screen.MainDoctor.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.MainDoctor.route) {
            MainDoctorScreen(
                onLogout = {
                    navController.navigate(Screen.Auth.route) {
                        popUpTo(Screen.MainDoctor.route) { inclusive = true }
                    }
                },
                onPatientClick = { patientId, choice ->
                    when (choice) {
                        PatientChoice.CONFIGURE -> navController.navigate(Screen.PatientConfig.createRoute(patientId))
                        PatientChoice.ANALYTICS -> navController.navigate(Screen.PatientAnalytics.createRoute(patientId))
                    }
                },
                onPairSuccess = {
                    // Stay on pairing or move to list
                }
            )
        }

        composable(
            route = Screen.PatientConfig.route,
            arguments = listOf(navArgument("patientId") { type = NavType.StringType })
        ) { backStackEntry ->
            val patientId = backStackEntry.arguments?.getString("patientId") ?: ""
            val configViewModel: ConfigurationViewModel = viewModel()
            ConfigurationScreen(
                viewModel = configViewModel,
                patientId = patientId,
                onBack = { navController.popBackStack() },
                onViewAnalytics = { id ->
                    navController.navigate(Screen.PatientAnalytics.createRoute(id))
                }
            )
        }

        composable(
            route = Screen.PatientAnalytics.route,
            arguments = listOf(navArgument("patientId") { type = NavType.StringType })
        ) { backStackEntry ->
            val patientId = backStackEntry.arguments?.getString("patientId") ?: ""
            val analyticsViewModel: AnalyticsViewModel = viewModel()
            AnalyticsScreen(
                viewModel = analyticsViewModel,
                patientId = patientId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
