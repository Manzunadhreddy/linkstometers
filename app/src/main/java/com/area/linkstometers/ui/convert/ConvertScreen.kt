package com.area.linkstometers.ui.convert

import android.content.Context
import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import java.io.File
import java.io.FileWriter

@Composable
fun ConvertScreen(
    viewModel: ConvertViewModel = viewModel(),
    onNavigateToHistory: () -> Unit = {}
) {
    val context = LocalContext.current
    
    // Check for scanned numbers and auto-populate
    // Using a side effect that runs every time the screen is displayed
    DisposableEffect(Unit) {
        val scannedNumbers = com.area.linkstometers.data.ScannedNumbersHolder.consumeNumbers()
        if (scannedNumbers != null && scannedNumbers.isNotEmpty()) {
            viewModel.setInitialInput(scannedNumbers)
        }
        onDispose { }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title at the very top
        Text(
            text = "Links Converter",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = "For Surveyors & Land Measurement Professionals",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Input Field
        OutlinedTextField(
            value = viewModel.inputValue,
            onValueChange = { viewModel.onInputChanged(it) },
            label = { Text("Enter Links (separate with , . or space)") },
            placeholder = { Text("e.g., 10, 20, 30") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            isError = viewModel.isError,
            supportingText = {
                if (viewModel.isError) {
                    Text(
                        text = viewModel.errorMessage,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            trailingIcon = {
                if (viewModel.inputValue.isNotEmpty()) {
                    IconButton(onClick = { viewModel.clearAll() }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear"
                        )
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            textStyle = MaterialTheme.typography.bodyLarge,
            singleLine = true
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // History and Export buttons row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // History button
            OutlinedButton(
                onClick = onNavigateToHistory,
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = "History",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "History", 
                    style = MaterialTheme.typography.labelMedium,
                    maxLines = 1,
                    softWrap = false
                )
            }
            
            // Save to History button
            Button(
                onClick = { 
                    val repository = com.area.linkstometers.data.HistoryRepository(context)
                    viewModel.conversionResults.forEach { result ->
                        repository.saveToHistory(result.links, result.meters, result.feet)
                    }
                },
                modifier = Modifier.weight(1f),
                enabled = viewModel.conversionResults.isNotEmpty(),
                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Save,
                    contentDescription = "Save",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Save", 
                    style = MaterialTheme.typography.labelMedium,
                    maxLines = 1,
                    softWrap = false
                )
            }
            
            // PDF Export button
            OutlinedButton(
                onClick = { 
                    exportToPdf(context, viewModel.conversionResults)
                },
                modifier = Modifier.weight(1f),
                enabled = viewModel.conversionResults.isNotEmpty(),
                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.PictureAsPdf,
                    contentDescription = "Export PDF",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "PDF", 
                    style = MaterialTheme.typography.labelMedium,
                    maxLines = 1,
                    softWrap = false
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Formula Display Card - moved below input
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = "Conversion Formulas",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "1 link = 0.201168 meters",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Text(
                    text = "1 link = 0.660 feet",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Results Section - Ultra Compact with responsive layout
        AnimatedVisibility(
            visible = viewModel.conversionResults.isNotEmpty() && !viewModel.isError,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                viewModel.conversionResults.forEach { result ->
                    ResponsiveResultCard(
                        links = result.links,
                        meters = result.meters,
                        feet = result.feet
                    )
                }
            }
        }
    }
}

@Composable
fun ResponsiveResultCard(
    links: Double,
    meters: Double,
    feet: Double
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp)
        ) {
            // First row: Links value
            Text(
                text = "${String.format("%.2f", links)} links =",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(6.dp))
            
            // Second row: Meters and Feet (will wrap if needed)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${String.format("%.4f", meters)} m",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.weight(1f, fill = false)
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                Text(
                    text = "${String.format("%.2f", feet)} ft",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.weight(1f, fill = false)
                )
            }
        }
    }
}

/**
 * Export conversion results to PDF and share
 */
// Replaced by PdfExportUtils
private fun exportToPdf(context: Context, results: List<ConversionResult>) {
    com.area.linkstometers.utils.PdfExportUtils.exportConversionResults(context, results)
}
