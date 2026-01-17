package com.area.linkstometers.data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Data class for history entry
 */
data class HistoryEntry(
    val links: Double,
    val meters: Double,
    val feet: Double,
    val timestamp: Long = System.currentTimeMillis()
)

/**
 * Repository for managing conversion history
 */
class HistoryRepository(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("conversion_history", Context.MODE_PRIVATE)
    private val gson = Gson()
    
    companion object {
        private const val KEY_HISTORY = "history_entries"
    }
    
    /**
     * Save conversion results to history
     */
    fun saveToHistory(links: Double, meters: Double, feet: Double) {
        val history = getHistory().toMutableList()
        history.add(0, HistoryEntry(links, meters, feet)) // Add to beginning
        
        // Keep only last 100 entries
        if (history.size > 100) {
            history.removeAt(history.size - 1)
        }
        
        val json = gson.toJson(history)
        prefs.edit().putString(KEY_HISTORY, json).apply()
    }
    
    /**
     * Get all history entries sorted by date (newest first)
     */
    fun getHistory(): List<HistoryEntry> {
        val json = prefs.getString(KEY_HISTORY, null) ?: return emptyList()
        val type = object : TypeToken<List<HistoryEntry>>() {}.type
        return gson.fromJson(json, type)
    }
    
    /**
     * Clear all history
     */
    fun clearHistory() {
        prefs.edit().remove(KEY_HISTORY).apply()
    }
}
