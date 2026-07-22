package edu.cit.soriano.pawwatch.mobile.network

import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface SupabaseStorageApi {
    @POST("storage/v1/object/{bucket}/{path}")
    suspend fun uploadFile(
        @Path("bucket") bucket: String,
        @Path("path") path: String,
        @Header("apikey") apiKey: String,
        @Header("Authorization") authorization: String,
        @Body file: RequestBody
    ): Response<ResponseBody>
}

// Separate Retrofit instance from RetrofitClient - Supabase Storage lives on
// a different host than the Spring Boot backend. No converter factory needed
// since we only care about the raw success/failure of the upload, not a
// parsed response body.
object SupabaseStorageClient {

    const val SUPABASE_URL = "https://tkuaxlkxfknebwsxdizr.supabase.co"
    const val SUPABASE_ANON_KEY = "sb_publishable_OEREtYD_mGCOUNWcVQzXIg_oSOqkWmq"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BASIC
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    val api: SupabaseStorageApi by lazy {
        Retrofit.Builder()
            .baseUrl("$SUPABASE_URL/")
            .client(client)
            .build()
            .create(SupabaseStorageApi::class.java)
    }
}
