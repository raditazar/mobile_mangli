package data.api

import data.model.CashPaymentRequest
import data.model.CashPaymentResponse

object PaymentApi{
    suspend fun payCash(orderId: String, amount: Double): CashPaymentResponse{
        return ApiClient.post(
            "/payments/cash/$orderId",
            CashPaymentRequest(amount = amount)
        )
    }
}