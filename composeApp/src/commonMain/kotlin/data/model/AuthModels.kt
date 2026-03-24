package data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// Login Request
@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

// Admin Response
@Serializable
data class Admin(
    val id: String,
    val name: String,
    val email: String,
    val role: String,
    @SerialName("is_active")
    val isActive: Boolean,
    @SerialName("last_login_at")
    val lastLoginAt: String? = null,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String
)

// Login Response
@Serializable
data class LoginResponse(
    val success: Boolean,
    val data: AuthData
)

// Auth Data
@Serializable
data class AuthData(
    val admin: Admin,
    val token: String
)