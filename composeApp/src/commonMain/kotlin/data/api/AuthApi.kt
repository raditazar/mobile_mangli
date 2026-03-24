package data.api

import data.model.LoginRequest
import data.model.LoginResponse

object AuthApi{
    suspend fun login(email: String, password: String): LoginResponse{
        return ApiClient.post(
            endpoint = "/auth/login",
            body = LoginRequest(email = email, password = password)
        )
    }
}