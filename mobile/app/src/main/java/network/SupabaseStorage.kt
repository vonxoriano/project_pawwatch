package edu.cit.soriano.pawwatch.mobile.network

import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

private const val BUCKET = "animal-photos"

/**
 * Uploads an image picked via the system Photo Picker to the same Supabase
 * Storage bucket web's PhotoUploadField.js uses, and returns its public URL.
 * Mirrors PhotoUploadField.js: flat bucket path named "<timestamp>.<ext>",
 * public URL constructed the same way the JS SDK's getPublicUrl() does.
 */
suspend fun uploadAnimalPhoto(context: Context, uri: Uri): Result<String> {
    return try {
        val mimeType = context.contentResolver.getType(uri) ?: "image/jpeg"
        val extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType) ?: "jpg"
        val fileName = "${System.currentTimeMillis()}.$extension"

        val bytes = context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
            ?: return Result.failure(Exception("Unable to read the selected image."))

        val body = bytes.toRequestBody(mimeType.toMediaTypeOrNull())
        val response = SupabaseStorageClient.api.uploadFile(
            bucket = BUCKET,
            path = fileName,
            apiKey = SupabaseStorageClient.SUPABASE_ANON_KEY,
            authorization = "Bearer ${SupabaseStorageClient.SUPABASE_ANON_KEY}",
            file = body
        )

        if (response.isSuccessful) {
            val publicUrl = "${SupabaseStorageClient.SUPABASE_URL}/storage/v1/object/public/$BUCKET/$fileName"
            Result.success(publicUrl)
        } else {
            val message = response.errorBody()?.string()?.takeIf { it.isNotBlank() } ?: "Photo upload failed."
            Result.failure(Exception(message))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}
