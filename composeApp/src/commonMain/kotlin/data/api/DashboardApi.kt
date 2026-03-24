package data.api

import data.model.DashboardSummaryResponse

object DashboardApi{
    suspend fun getSummary(period: String = "daily"): DashboardSummaryResponse {
        return ApiClient.get("/dashboard/summary?period=$period")
    }
}