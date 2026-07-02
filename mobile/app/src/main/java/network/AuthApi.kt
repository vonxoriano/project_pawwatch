package edu.cit.soriano.pawwatch.mobile.network

import edu.cit.soriano.pawwatch.mobile.model.AuthResponse
import edu.cit.soriano.pawwatch.mobile.model.LoginRequest
import edu.cit.soriano.pawwatch.mobile.model.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>
}