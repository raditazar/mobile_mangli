package data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// Dashboard Response
@Serializable
data class DashboardSummaryResponse(
    val success: Boolean,
    val data: DashboardSummary
)

@Serializable
data class DashboardSummary(
    @SerialName("total_revenue")
    val totalRevenue: Double,
    @SerialName("total_orders")
    val totalOrders: Int,
    @SerialName("total_visitors")
    val totalVisitors: Int,
    @SerialName("top_packages")
    val topPackages: List<TopPackage> = emptyList(),
    @SerialName("daily_visitors")
    val dailyVisitors: List<DailyVisitor> = emptyList()
)

@Serializable
data class TopPackage(
    @SerialName("tour_package_id")
    val tourPackageId: String,
    val name: String,
    @SerialName("total_sold")
    val totalSold: Int
)

@Serializable
data class DailyVisitor(
    val date: String,
    val visitors: Int
)