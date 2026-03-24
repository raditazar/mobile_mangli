package com.example.mobile_mangli



import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import ui.screens.LoginScreen

@Composable
fun App() {
    MaterialTheme {
        LoginScreen(
            onLoginSuccess = {
                // Logika saat tombol masuk ditekan
                println("Tombol masuk ditekan!")
            }
        )
    }
}