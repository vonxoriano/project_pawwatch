package edu.cit.soriano.pawwatch.mobile.model

data class RegisterRequest(
    val fullName: String,
    val email: String,
    val contactNumber: String,
    val password: String
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class AuthResponse(
    val token: String,
    val role: String,
    val fullName: String
)

data class UserProfile(
    val userId: Long,
    val fullName: String,
    val email: String,
    val contactNumber: String,
    val role: String
)

data class UpdateProfileRequest(
    val fullName: String,
    val contactNumber: String
)

data class ChangePasswordRequest(
    val currentPassword: String,
    val newPassword: String,
    val confirmNewPassword: String
)