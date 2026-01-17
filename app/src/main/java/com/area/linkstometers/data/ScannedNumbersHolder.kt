package com.area.linkstometers.data

/**
 * Singleton to temporarily hold scanned numbers for passing between screens
 */
object ScannedNumbersHolder {
    var scannedNumbers: String? = null
        private set
    
    fun setNumbers(numbers: String) {
        scannedNumbers = numbers
    }
    
    fun consumeNumbers(): String? {
        val numbers = scannedNumbers
        scannedNumbers = null
        return numbers
    }
}
