package data.api

import data.model.CheckinRequest
import data.model.CheckinResponse
import data.model.OrderLookUpResponse
import data.model.ScanRequest

object CheckinApi{
    // GET /orders/number/:orderNumber
    suspend fun lookupOrder(orderNumber: String): OrderLookUpResponse{
        return ApiClient.get("/orders/number/$orderNumber")
    }

    // 
    suspend fun checkin(request: CheckinRequest): CheckinResponse{
        return ApiClient.post("/visitor-checkins", request)
    }

    suspend fun scan(request: ScanRequest): CheckinResponse{
        return ApiClient.post("/visitor-checkins/scan", request)
    }
}