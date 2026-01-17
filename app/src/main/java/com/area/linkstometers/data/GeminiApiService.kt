package com.area.linkstometers.data

import android.graphics.Bitmap
import android.util.Base64
import com.google.gson.annotations.SerializedName
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import java.io.ByteArrayOutputStream
import java.util.concurrent.TimeUnit

/**
 * Gemini API request/response models
 */
data class GeminiRequest(
    val contents: List<Content>
)

data class Content(
    val parts: List<Part>
)

data class Part(
    val text: String? = null,
    @SerializedName("inline_data")
    val inlineData: InlineData? = null
)

data class InlineData(
    @SerializedName("mime_type")
    val mimeType: String,
    val data: String
)

data class GeminiResponse(
    val candidates: List<Candidate>?
)

data class Candidate(
    val content: Content?
)

/**
 * Retrofit API interface
 */
interface GeminiApi {
    @POST("/")
    suspend fun analyzeImage(@Body request: GeminiRequest): GeminiResponse
}

/**
 * Service for communicating with Cloudflare Workers Gemini API
 */
class GeminiApiService {
    
    companion object {
        private const val BASE_URL = "https://googlegemini.manzunadhreddy.workers.dev/"
        private const val TIMEOUT_SECONDS = 60L
    }
    
    private val api: GeminiApi
    
    init {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .build()
        
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        
        api = retrofit.create(GeminiApi::class.java)
    }
    
    /**
     * Extract numbers from an image using Gemini API
     */
    suspend fun extractNumbersFromImage(bitmap: Bitmap): Result<List<Double>> {
        return try {
            // Convert bitmap to base64
            val base64Image = bitmapToBase64(bitmap)
            
            // Create request
            val request = GeminiRequest(
                contents = listOf(
                    Content(
                        parts = listOf(
                            Part(
                                text = "Extract all numbers from this image. Return only the numbers separated by commas, nothing else. If there are no numbers, return 'NONE'."
                            ),
                            Part(
                                inlineData = InlineData(
                                    mimeType = "image/jpeg",
                                    data = base64Image
                                )
                            )
                        )
                    )
                )
            )
            
            // Call API
            val response = api.analyzeImage(request)
            
            // Extract text from response
            val text = response.candidates?.firstOrNull()
                ?.content?.parts?.firstOrNull()
                ?.text ?: ""
            
            // Parse numbers from text
            val numbers = parseNumbers(text)
            
            Result.success(numbers)
            
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Convert bitmap to base64 string
     */
    private fun bitmapToBase64(bitmap: Bitmap): String {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
        val byteArray = outputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.NO_WRAP)
    }
    
    /**
     * Parse numbers from API response text
     */
    private fun parseNumbers(text: String): List<Double> {
        if (text.trim().equals("NONE", ignoreCase = true)) {
            return emptyList()
        }
        
        // Extract numbers using regex
        val numberPattern = Regex("""(\d+\.?\d*)""")
        return numberPattern.findAll(text)
            .mapNotNull { it.value.toDoubleOrNull() }
            .filter { it > 0.0 } // Exclude zero values (0, 0.0, 0.00, etc.)
            .toList()
    }
}
