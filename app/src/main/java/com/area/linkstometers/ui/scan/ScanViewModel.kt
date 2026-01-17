package com.area.linkstometers.ui.scan

import android.graphics.Bitmap
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.area.linkstometers.data.GeminiApiService
import kotlinx.coroutines.launch

/**
 * ViewModel for Scan screen
 */
class ScanViewModel : ViewModel() {
    
    private val apiService = GeminiApiService()
    
    var capturedImage by mutableStateOf<Bitmap?>(null)
        private set
    
    var isLoading by mutableStateOf(false)
        private set
    
    var extractedNumbers by mutableStateOf<List<Double>>(emptyList())
        private set
    
    var errorMessage by mutableStateOf<String?>(null)
        private set
    
    fun onImageCaptured(bitmap: Bitmap) {
        capturedImage = bitmap
        extractedNumbers = emptyList()
        errorMessage = null
    }
    
    fun analyzeImage() {
        val image = capturedImage ?: return
        
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            
            apiService.extractNumbersFromImage(image)
                .onSuccess { numbers ->
                    extractedNumbers = numbers
                    if (numbers.isEmpty()) {
                        errorMessage = "No numbers found in the image"
                    }
                }
                .onFailure { error ->
                    errorMessage = error.message ?: "Failed to analyze image"
                }
            
            isLoading = false
        }
    }
    
    fun clearImage() {
        capturedImage = null
        extractedNumbers = emptyList()
        errorMessage = null
    }
    
    fun getNumbersAsString(): String {
        return extractedNumbers.joinToString(", ")
    }
}
