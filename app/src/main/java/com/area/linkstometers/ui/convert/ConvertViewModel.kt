package com.area.linkstometers.ui.convert

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

/**
 * ViewModel for the Convert screen
 * Handles conversion logic from links to meters and feet
 * Supports multiple entries separated by comma, period, or space
 */
class ConvertViewModel : ViewModel() {
    
    // Conversion constants
    companion object {
        const val LINKS_TO_METERS = 0.201168
        const val LINKS_TO_FEET = 0.660
    }
    
    // UI State
    var inputValue by mutableStateOf("")
        private set
    
    var conversionResults by mutableStateOf<List<ConversionResult>>(emptyList())
        private set
    
    var isError by mutableStateOf(false)
        private set
    
    var errorMessage by mutableStateOf("")
        private set
    
    /**
     * Update input value and perform conversion
     */
    fun onInputChanged(newValue: String) {
        inputValue = newValue
        performConversion()
    }
    
    /**
     * Set initial input value (e.g., from scanned numbers)
     */
    fun setInitialInput(value: String) {
        inputValue = value
        performConversion()
    }
    
    /**
     * Perform the conversion calculation for multiple entries
     */
    private fun performConversion() {
        if (inputValue.isBlank()) {
            // Clear results when input is empty
            conversionResults = emptyList()
            isError = false
            errorMessage = ""
            return
        }
        
        // Split input by comma, period, or space
        val entries = inputValue.split(Regex("[,\\.\\s]+"))
            .filter { it.isNotBlank() }
        
        if (entries.isEmpty()) {
            conversionResults = emptyList()
            isError = false
            errorMessage = ""
            return
        }
        
        val results = mutableListOf<ConversionResult>()
        var hasError = false
        
        for (entry in entries) {
            val linksValue = entry.toDoubleOrNull()
            
            if (linksValue == null) {
                hasError = true
                errorMessage = "Invalid value: $entry"
                break
            } else if (linksValue < 0) {
                hasError = true
                errorMessage = "Negative value not allowed: $entry"
                break
            } else if (linksValue == 0.0) {
                // Skip zero values - they are not useful for conversion
                continue
            } else {
                val meters = linksValue * LINKS_TO_METERS
                val feet = linksValue * LINKS_TO_FEET
                
                results.add(
                    ConversionResult(
                        links = linksValue,
                        meters = meters,
                        feet = feet
                    )
                )
            }
        }
        
        if (hasError) {
            isError = true
            conversionResults = emptyList()
        } else {
            isError = false
            errorMessage = ""
            conversionResults = results
        }
    }
    
    /**
     * Clear all input and results
     */
    fun clearAll() {
        inputValue = ""
        conversionResults = emptyList()
        isError = false
        errorMessage = ""
    }
}

/**
 * Data class to hold conversion results
 */
data class ConversionResult(
    val links: Double,
    val meters: Double,
    val feet: Double
)
