package ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.api.OrderApi
import data.api.PaymentApi
import data.model.CreateOrderRequest
import data.model.OrderItem
import data.model.TourPackage
import kotlin.collections.emptyMap
import kotlinx.coroutines.launch

class PosViewModel : ViewModel() {
    // Step tracking (1 = choose, 2 = form, 3 = finish)
    var currentStep by mutableStateOf(1)
        private set

    // Data State
    var packages by mutableStateOf<List<TourPackage>>(emptyList())
        private set

    // Cart State
    var quantities by mutableStateOf<Map<String, Int>>(emptyMap())
        private set

    // Form State
    var fullName by mutableStateOf("")
        private set
    var phoneNumber by mutableStateOf("")
        private set

    var isLoading by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set
    var successMessage by mutableStateOf<String?>(null)
        private set
    var showConfirmSheet by mutableStateOf(false)
        private set

    fun loadPackages() {
        viewModelScope.launch {
            isLoading = true
            try {
                val response = OrderApi.getPackages()
                packages = response.data.filter { it.isActive }
            } catch (e: Exception) {
                errorMessage = e.message ?: "Gagal memuat paket"
            } finally {
                isLoading = false
            }
        }
    }

    fun incrementQuantity(priceId: String) {
        val current = quantities[priceId] ?: 0
        quantities = quantities + (priceId to current + 1)
    }

    fun decrementQuantity(priceId: String) {
        val current = quantities[priceId] ?: 0
        if (current > 0) {
            quantities = quantities + (priceId to current - 1)
        }
    }

    fun calculateTotal(): Double {
        var total = 0.0
        for (pkg in packages) {
            for (price in pkg.packagePrices ?: emptyList()) {
                val qty = quantities[price.id] ?: 0
                total += price.price * qty
            }
        }
        return total
    }

    fun hasSelection(): Boolean {
        return quantities.values.any { it > 0 }
    }

    fun updateFullName(name: String) {
        fullName = name
    }

    fun updatePhoneNumber(phone: String) {
        phoneNumber = phone
    }

    fun goToStep2() {
        if (hasSelection()) {
            errorMessage = null
            currentStep = 2
        } else {
            errorMessage = "Pilih tiket terlebih dahulu"
        }
    }

    fun goBackToStep1() {
        currentStep = 1
        errorMessage = null
    }

    fun showConfirmation() {
        if (fullName.isBlank() || phoneNumber.isBlank()) {
            errorMessage = "Lengkapi data diri"
        } else {
            errorMessage = null
            showConfirmSheet = true
        }
    }

    fun hideConfirmation() {
        showConfirmSheet = false
    }

    fun processPayment() {
        viewModelScope.launch {
            isLoading = true
            showConfirmSheet = false
            errorMessage = null
            try {
                val items = buildOrderItems()
                val orderRequest =
                        CreateOrderRequest(
                                fullName = fullName,
                                phoneNumber = phoneNumber,
                                email = "$phoneNumber@walk-in.local",
                                visitDate = getTodayDate(),
                                paymentMethod = "cash",
                                items = items
                        )
                val orderResponse = OrderApi.createOrder(orderRequest)
                val total = calculateTotal()
                PaymentApi.payCash(orderResponse.data.id, total)
                successMessage = "Pembayaran berhasil! No. Order: ${orderResponse.data.orderNumber}"
                currentStep = 3
            } catch (e: Exception) {
                errorMessage = e.message ?: "Gagal memproses pembayaran"
            } finally {
                isLoading = false
            }
        }
    }
    fun resetTransaction() {
        currentStep = 1
        quantities = emptyMap()
        fullName = ""
        phoneNumber = ""
        errorMessage = null
        successMessage = null
    }

    private fun buildOrderItems(): List<OrderItem> {
        val items = mutableListOf<OrderItem>()
        for (pkg in packages) {
            for (price in pkg.packagePrices ?: emptyList()) {
                val qty = quantities[price.id] ?: 0
                if (qty > 0) {
                    items.add(
                            OrderItem(
                                    tourPackageId = pkg.id,
                                    packagePriceId = price.id,
                                    quantity = qty
                            )
                    )
                }
            }
        }
        return items
    }

    private fun getTodayDate(): String {
        // val now = kotlinx.datetime.Clock.System.now()
        // return now.toString().substring(0, 10)
        return "2026-03-29"
    }
}
