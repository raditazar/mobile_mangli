package ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.api.ApiClient
import data.api.AuthApi
import data.model.Admin
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    var isLoading by mutableStateOf(false)
    private set
    var errorMessage by mutableStateOf<String?>(null)
    private set
    var currentAdmin by mutableStateOf<Admin?>(null)
    private set
    var isLoggedIn by mutableStateOf(false)
    private set

    // Login
    fun login(email: String, password: String){
        viewModelScope.launch{
            isLoading = true
            errorMessage = null
            try{
                val response = AuthApi.login(email, password)
                ApiClient.token = response.data.token
                currentAdmin = response.data.admin
                isLoggedIn = true
            } catch (e: Exception){
                errorMessage = e.message
            } finally{
                isLoading = false
            }
        }
    }

    // Logout
    fun logout(){
        ApiClient.token = null
        currentAdmin = null
        isLoggedIn = false
    }
}