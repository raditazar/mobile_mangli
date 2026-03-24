package com.example.mobile_mangli

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import ui.navigation.Screen
import ui.screens.LoginScreen
import ui.viewmodel.AuthViewModel
import ui.screens.DashboardScreen
import ui.viewmodel.DashboardViewModel

@Composable
fun App() {
    var currentScreen by remember {
        mutableStateOf<Screen>(Screen.Login)
    }

    val authViewModel: AuthViewModel = viewModel()
    val dashboardViewModel: DashboardViewModel = viewModel()
    MaterialTheme {
        when (currentScreen){
            is Screen.Login -> {
                LoginScreen(
                    viewModel = authViewModel,
                    onLoginSuccess = {
                        currentScreen = Screen.Dashboard
                    }
                )
            }
            is Screen.Dashboard -> {
                DashboardScreen(
                    viewModel = dashboardViewModel,
                    admin = authViewModel.currentAdmin,
                    onNavigate = { screen ->
                        currentScreen = screen
                    }
                )
            }
            is Screen.Scanner -> {

            }
            is Screen.Pos -> {

            }
            is Screen.Profile -> {

            }
        }
    }
}