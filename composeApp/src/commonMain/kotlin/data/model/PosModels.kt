package data.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

// Tour Package Response
@Serializable
data class TourPackageListResponse(
    val success: Boolean,
    val data: List<TourPackage>
)

// Tour Package
@Serializable
data class TourPackage(
    val id: String,
    val name: String,
    val slug: String,
    val description: String,
    @SerialName("duration_days")
    val durationDays: Int,
    @SerialName("max_participants")
    val maxParticipants: Int,
    val location: String,
    @SerialName("is_active")
    val isActive: Boolean,
    @SerialName("package_prices")
    val packagePrices: List<PackagePrice>? = null
)

// Package Price
@Serializable
data class PackagePrice(
    val id: String,
    @SerialName("tour_package_id")
    val tourPackageId: String,
    val name: String,
    val price: Double,
    @SerialName("discount_price")
    val discountPrice: Double? = null,
    @SerialName("is_active")
    val isActive: Boolean
)

@Serializable
data class CreateOrderRequest(
    @SerialName("full_name")
    val fullName: String,
    @SerialName("phone_number")
    val phoneNumber: String,
    val email: String,
    @SerialName("visit_date")
    val visitDate: String,
    @SerialName("payment_method")
    val paymentMethod: String = "cash",
    val items: List<OrderItem>
)

@Serializable
data class OrderItem(
    @SerialName("tour_package_id")
    val tourPackageId: String,
    @SerialName("package_price_id")
    val packagePriceId: String,
    val quantity: Int
)

@Serializable
data class CreateOrderResponse(
    val success: Boolean,
    val data: OrderCreated
)

@Serializable
data class OrderCreated(
    val id: String,
    @SerialName("order_number")
    val orderNumber: String,
    @SerialName("total_amount")
    val totalAmount: Double,
    val status: String
)

@Serializable
data class CashPaymentRequest(
    val amount: Double
)

@Serializable
data class CashPaymentResponse(
    val success: Boolean,
    val message: String? = null
)