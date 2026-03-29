package com.example.mobile_mangli

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import ui.navigation.Screen
import ui.screens.DashboardScreen
import ui.screens.LoginScreen
import ui.screens.PosScreen
import ui.screens.ScannerScreen
import ui.screens.PackageScreen
import ui.screens.ProfileScreen
import ui.viewmodel.AuthViewModel
import ui.viewmodel.DashboardViewModel
import ui.viewmodel.PackageViewModel
import ui.viewmodel.PosViewModel
import ui.viewmodel.ScannerViewModel

@Composable
fun App() {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Login) }

    val authViewModel: AuthViewModel = viewModel()
    val dashboardViewModel: DashboardViewModel = viewModel()
    val scannerViewModel: ScannerViewModel = viewModel()
    val posViewModel: PosViewModel = viewModel()
    val packageViewModel: PackageViewModel = viewModel()
    MaterialTheme {
        when (currentScreen) {
            is Screen.Login -> {
                LoginScreen(
                        viewModel = authViewModel,
                        onLoginSuccess = { currentScreen = Screen.Dashboard }
                )
            }
            is Screen.Dashboard -> {
                DashboardScreen(
                        viewModel = dashboardViewModel,
                        admin = authViewModel.currentAdmin,
                        onNavigate = { screen -> currentScreen = screen }
                )
            }
            is Screen.Scanner -> {
                ScannerScreen(
                        viewModel = scannerViewModel,
                        admin = authViewModel.currentAdmin,
                        onNavigate = { screen -> currentScreen = screen }
                )
            }
            is Screen.Pos -> {
                PosScreen(
                        viewModel = posViewModel,
                        admin = authViewModel.currentAdmin,
                        onNavigate = { screen -> currentScreen = screen }
                )
            }
            is Screen.PackageManagement -> {
                PackageScreen(
                        viewModel = packageViewModel,
                        onBack = { currentScreen = Screen.Dashboard }
                )
            }
            is Screen.Profile -> {
                ProfileScreen(
                    admin = authViewModel.currentAdmin, 
                    onNavigate = {screen ->
                        currentScreen = screen
                    },
                    onLogout = {
                        authViewModel.logout()
                        currentScreen = Screen.Login
                    }
                )
            }
        }
    }
}
