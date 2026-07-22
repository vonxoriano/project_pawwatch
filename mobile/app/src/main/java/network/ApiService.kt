package edu.cit.soriano.pawwatch.mobile.network

import edu.cit.soriano.pawwatch.mobile.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    // Auth
    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    // Profile
    @GET("api/users/me")
    suspend fun getProfile(
        @Header("Authorization") token: String
    ): Response<UserProfile>

    @PUT("api/users/me")
    suspend fun updateProfile(
        @Header("Authorization") token: String,
        @Body request: UpdateProfileRequest
    ): Response<UserProfile>

    @PUT("api/users/me/password")
    suspend fun changePassword(
        @Header("Authorization") token: String,
        @Body request: ChangePasswordRequest
    ): Response<String>

    // Animals - Adopter
    @GET("api/animals/browse")
    suspend fun browseAnimals(
        @Query("keyword") keyword: String? = null,
        @Query("species") species: String? = null
    ): Response<List<Animal>>

    @GET("api/animals/{id}")
    suspend fun getAnimalById(@Path("id") id: Long): Response<Animal>

    // Animals - Admin
    @GET("api/animals/admin/all")
    suspend fun getAllAnimals(
        @Header("Authorization") token: String
    ): Response<List<Animal>>

    @POST("api/animals/admin/add")
    suspend fun addAnimal(
        @Header("Authorization") token: String,
        @Body request: AnimalRequest
    ): Response<Animal>

    @PUT("api/animals/admin/edit/{id}")
    suspend fun editAnimal(
        @Header("Authorization") token: String,
        @Path("id") id: Long,
        @Body request: AnimalRequest
    ): Response<Animal>

    @DELETE("api/animals/admin/delete/{id}")
    suspend fun deleteAnimal(
        @Header("Authorization") token: String,
        @Path("id") id: Long
    ): Response<String>

    // Applications - Adopter
    @POST("api/applications/submit")
    suspend fun submitApplication(
        @Header("Authorization") token: String,
        @Body request: ApplicationRequest
    ): Response<AdoptionApplication>

    @GET("api/applications/my")
    suspend fun getMyApplications(
        @Header("Authorization") token: String
    ): Response<List<AdoptionApplication>>

    @DELETE("api/applications/cancel/{id}")
    suspend fun cancelApplication(
        @Header("Authorization") token: String,
        @Path("id") id: Long
    ): Response<String>

    // Applications - Admin
    @GET("api/applications/admin/all")
    suspend fun getAllApplications(
        @Header("Authorization") token: String
    ): Response<List<AdoptionApplication>>

    @PATCH("api/applications/admin/process/{id}")
    suspend fun processApplication(
        @Header("Authorization") token: String,
        @Path("id") id: Long,
        @Body request: ApplicationStatusRequest
    ): Response<AdoptionApplication>

    // Favorites - Adopter
    @POST("api/favorites/add/{animalId}")
    suspend fun addFavorite(
        @Header("Authorization") token: String,
        @Path("animalId") animalId: Long
    ): Response<Favorite>

    @DELETE("api/favorites/remove/{animalId}")
    suspend fun removeFavorite(
        @Header("Authorization") token: String,
        @Path("animalId") animalId: Long
    ): Response<String>

    @GET("api/favorites/my")
    suspend fun getMyFavorites(
        @Header("Authorization") token: String
    ): Response<List<Favorite>>

    @GET("api/favorites/check/{animalId}")
    suspend fun checkFavorite(
        @Header("Authorization") token: String,
        @Path("animalId") animalId: Long
    ): Response<Boolean>
}