package data.api

import data.model.CreateOrderRequest
import data.model.CreateOrderResponse
import data.model.TourPackageListResponse

object OrderApi{
    suspend fun getPackages():
    TourPackageListResponse{
       return ApiClient.get("/tour-packages")
    }

    suspend fun createOrder(request: CreateOrderRequest): CreateOrderResponse{
        return ApiClient.post("/orders", request)
    }
}