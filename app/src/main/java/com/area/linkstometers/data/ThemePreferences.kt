package com.area.linkstometers.data

import android.content.Context
import android.content.SharedPreferences

/**
 * Theme modes for the app
 */
enum class ThemeMode {
    SYSTEM_DEFAULT,
    LIGHT,
    DARK
}

/**
 * Repository for managing theme preferences
 */
class ThemePreferences(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)
    
    companion object {
        private const val KEY_THEME_MODE = "theme_mode"
    }
    
    /**
     * Get current theme mode
     */
    fun getThemeMode(): ThemeMode {
        val mode = prefs.getString(KEY_THEME_MODE, ThemeMode.SYSTEM_DEFAULT.name)
        return ThemeMode.valueOf(mode ?: ThemeMode.SYSTEM_DEFAULT.name)
    }
    
    /**
     * Set theme mode
     */
    fun setThemeMode(mode: ThemeMode) {
        prefs.edit().putString(KEY_THEME_MODE, mode.name).apply()
    }
}
