package com.nabd.app.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.nabd.app.ui.screens.*
import com.nabd.app.ui.viewmodel.*

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Auth : Screen("auth")
    object Profile : Screen("profile")
    object PatientList : Screen("patient_list")
    object DevicePairing : Screen("device_pairing/{patientId}") {
        fun createRoute(patientId: String) = "device_pairing/$patientId"
    }
    object PatientConfig : Screen("patient_config/{patientId}") {
        fun createRoute(patientId: String) = "patient_config/$patientId"
    }
    object PatientAnalytics : Screen("patient_analytics/{patientId}") {
        fun createRoute(patientId: String) = "patient_analytics/$patientId"
    }
}

@Composable
fun NabdNavGraph(navController: NavHostController) {
    val patientViewModel: PatientViewModel = viewModel()

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
                    navController.navigate(Screen.Profile.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Profile.route) {
            val profileViewModel: ProfileViewModel = viewModel()
            ProfileScreen(
                viewModel = profileViewModel,
                onLogout = {
                    navController.navigate(Screen.Auth.route) {
                        popUpTo(Screen.Profile.route) { inclusive = true }
                    }
                },
                onPatientsClick = {
                    navController.navigate(Screen.PatientList.route)
                }
            )
        }

        composable(Screen.PatientList.route) {
            PatientListScreen(
                viewModel = patientViewModel,
                onPatientClick = { patientId ->
                    navController.navigate(Screen.PatientConfig.createRoute(patientId))
                },
                onAddPatient = {
                    val patientId = patientViewModel.uiState.value.patients.firstOrNull { !it.isConnected }?.id ?: ""
                    navController.navigate(Screen.DevicePairing.createRoute(patientId))
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Screen.DevicePairing.route,
            arguments = listOf(navArgument("patientId") { type = NavType.StringType })
        ) { backStackEntry ->
            val patientId = backStackEntry.arguments?.getString("patientId") ?: ""
            DevicePairingScreen(
                viewModel = patientViewModel,
                patientId = patientId,
                onBack = { navController.popBackStack() },
                onPairSuccess = {
                    navController.popBackStack()
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
