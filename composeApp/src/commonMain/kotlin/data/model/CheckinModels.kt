package data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// /orders/number/:orderNumber
@Serializable
data class OrderLookUpResponse(
    val success: Boolean,
    val data: OrderDetail
)

// Order Detail
@Serializable
data class OrderDetail(
    val id: String,
    @SerialName("order_number")
    val orderNumber: String,
    @SerialName("full_name")
    val fullName: String,
    @SerialName("phone_number")
    val phoneNumber: String,
    val email: String,
    @SerialName("visit_date")
    val visitDate: String,
    val status: String,
    @SerialName("total_amount")
    val totalAmount: Double,
    @SerialName("payment_method")
    val paymentMethod: String
)

// POST /visitor-checkins
@Serializable
data class CheckinRequest(
    @SerialName("order_id")
    val orderId: String,
    @SerialName("number_of_visitors")
    val numberOfVisitors: Int,
    val notes: String? = null
)

// POST /visitor-checkins/scan
@Serializable
data class ScanRequest(
    @SerialName("qr_data")
    val qrData: String
)

// Checkin Response
@Serializable
data class CheckinResponse(
    val success: Boolean,
    val data: CheckinData? = null,
    val message: String? = null
)

// Checkin Data
@Serializable
data class CheckinData(
    val id: String,
    @SerialName("order_id")
    val orderId: String,
    @SerialName("number_of_visitors")
    val numberOfVisitors: Int,
    @SerialName("checked_in_at")
    val checkedInAt: String
)

