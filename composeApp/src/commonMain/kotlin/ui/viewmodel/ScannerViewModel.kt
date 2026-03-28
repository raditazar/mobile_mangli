package ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.api.CheckinApi
import data.model.CheckinRequest
import data.model.OrderDetail
import data.model.ScanRequest
import kotlinx.coroutines.launch

class ScannerViewModel : ViewModel() {
    // state for manual input
    var orderCode by mutableStateOf("")
        private set
    var orderDetail by mutableStateOf<OrderDetail?>(null)
        private set
    var visitorCount by mutableStateOf(1)
        private set

    // general state
    var isLoading by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set
    var successMessage by mutableStateOf<String?>(null)
        private set

    fun updateOrderCode(code: String) {
        orderCode = code
    }

    fun updateVisitorCount(count: Int) {
        if (count in 1..100) {
            visitorCount = count
        }
    }

    fun lookupOrder() {
        if (orderCode.isBlank()) {
            errorMessage = "Masukkan kode booking"
            return
        }
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            orderDetail = null
            try {
                val response = CheckinApi.lookupOrder(orderCode)
                orderDetail = response.data
            } catch (e: Exception) {
                errorMessage = e.message ?: "Pesanan tidak ditemukan"
            } finally {
                isLoading = false
            }
        }
    }

    fun checkinManual() {
        val order = orderDetail ?: return
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val request = CheckinRequest(orderId = order.id, numberOfVisitors = visitorCount)
                CheckinApi.checkin(request)
                successMessage = "Check-in berhasil! ${visitorCount} pengunjung."
                orderDetail = null
                orderCode = ""
                visitorCount = 1
            } catch (e: Exception) {
                errorMessage = e.message ?: "Check-in gagal"
            } finally {
                isLoading = false
            }
        }
    }

    fun checkinScan(qrData: String) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            successMessage = null
            try {
                val request = ScanRequest(qrData = qrData)
                CheckinApi.scan(request)
                successMessage = "Check-in berhasil!"
            } catch (e: Exception) {
                errorMessage = e.message ?: "Check-in gagal"
            } finally {
                isLoading = false
            }
        }
    }

    fun clearMessage() {
        errorMessage = null
        successMessage = null
    }
}
