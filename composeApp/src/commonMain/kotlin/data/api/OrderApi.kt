package data.api

import data.model.CreateOrderRequest
import data.model.CreateOrderResponse
import data.model.TourPackageListResponse
import data.model.CreatePackageRequest
import data.model.GenericResponse
import data.model.CreatePriceRequest

object OrderApi{
    suspend fun getPackages():
    TourPackageListResponse{
       return ApiClient.get("/tour-packages")
    }

    suspend fun createOrder(request: CreateOrderRequest): CreateOrderResponse{
        return ApiClient.post("/orders", request)
    }

    suspend fun createPackage(request: CreatePackageRequest): GenericResponse{
        return ApiClient.post("/tour-packages", request)
    }

    suspend fun updatePackage(id: String, request: CreatePackageRequest): GenericResponse{
        return ApiClient.put("/tour-packages/$id", request)
    }

    suspend fun deletePackage(id: String): GenericResponse{
        return ApiClient.delete("/tour-packages/$id")
    }

    suspend fun createPrice(request: CreatePriceRequest): GenericResponse{
        return ApiClient.post("/package-prices", request)
    }

    suspend fun updatePrice(id: String, request: CreatePriceRequest): GenericResponse{
        return ApiClient.put("/package-prices/$id", request)
    }

    suspend fun deletePrice(id: String): GenericResponse{
        return ApiClient.delete("/package-prices/$id")
    }
}