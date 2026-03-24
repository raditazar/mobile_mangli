package ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.api.DashboardApi
import data.model.DashboardSummary
import kotlinx.coroutines.launch

class DashboardViewModel : ViewModel() {
    var summary by mutableStateOf<DashboardSummary?>(null)
    private set
    var isLoading by mutableStateOf(false)
    private set
    var errorMessage by mutableStateOf<String?>(null)
    private set

    fun loadSummary(){
        viewModelScope.launch{
            isLoading = true
            errorMessage = null
            try{
                val response = DashboardApi.getSummary("daily")
                summary = response.data
            } catch(e: Exception){
                errorMessage = e.message ?: "Gagal memuat dashboard"
            } finally{
                isLoading = false
            }
        } 
    }
}