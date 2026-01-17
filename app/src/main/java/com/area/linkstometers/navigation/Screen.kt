package com.area.linkstometers.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Navigation destinations for the app
 */
sealed class Screen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Convert : Screen(
        route = "convert",
        title = "Convert",
        icon = Icons.Default.Calculate
    )
    
    object LandRecords : Screen(
        route = "land_records",
        title = "Records",
        icon = Icons.Default.Description
    )
    
    object History : Screen(
        route = "history",
        title = "History",
        icon = Icons.Default.History
    )
    
    object Scan : Screen(
        route = "scan",
        title = "Scan",
        icon = Icons.Default.CameraAlt
    )
    
    object Settings : Screen(
        route = "settings",
        title = "Settings",
        icon = Icons.Default.Settings
    )
}

/**
 * List of all bottom navigation items
 */
val bottomNavItems = listOf(
    Screen.LandRecords,
    Screen.History,
    Screen.Convert,
    Screen.Scan,
    Screen.Settings
)
