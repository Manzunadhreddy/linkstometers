package com.area.linkstometers.ui.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.area.linkstometers.data.ThemeMode

/**
 * ViewModel for Settings screen
 */
class SettingsViewModel : ViewModel() {
    var themeMode by mutableStateOf(ThemeMode.SYSTEM_DEFAULT)
        private set
    
    fun updateThemeMode(mode: ThemeMode) {
        themeMode = mode
    }
}
