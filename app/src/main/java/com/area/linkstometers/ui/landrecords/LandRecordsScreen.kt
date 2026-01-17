package com.area.linkstometers.ui.landrecords

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

data class LandRecordWebsite(
    val state: String,
    val url: String
)

val indianLandRecords = listOf(
    LandRecordWebsite("Andhra Pradesh", "https://meebhoomi.ap.gov.in/"),
    LandRecordWebsite("Arunachal Pradesh", "https://arunachalpradesh.gov.in/"),
    LandRecordWebsite("Assam", "https://revenueassam.nic.in/"),
    LandRecordWebsite("Bihar", "http://biharbhumi.bihar.gov.in/"),
    LandRecordWebsite("Chhattisgarh", "https://bhuiyan.cg.nic.in/"),
    LandRecordWebsite("Goa", "https://egov.goa.nic.in/"),
    LandRecordWebsite("Gujarat", "https://anyror.gujarat.gov.in/"),
    LandRecordWebsite("Haryana", "https://jamabandi.nic.in/"),
    LandRecordWebsite("Himachal Pradesh", "https://lrc.hp.nic.in/"),
    LandRecordWebsite("Jharkhand", "https://jharbhoomi.nic.in/"),
    LandRecordWebsite("Karnataka", "https://landrecords.karnataka.gov.in/"),
    LandRecordWebsite("Kerala", "https://erekha.kerala.gov.in/"),
    LandRecordWebsite("Madhya Pradesh", "https://mpbhulekh.gov.in/"),
    LandRecordWebsite("Maharashtra", "https://mahabhulekh.maharashtra.gov.in/"),
    LandRecordWebsite("Manipur", "https://louchapathap.nic.in/"),
    LandRecordWebsite("Meghalaya", "https://megrevenuedm.gov.in/"),
    LandRecordWebsite("Mizoram", "https://mizoram.nic.in/"),
    LandRecordWebsite("Nagaland", "https://landrevenue.nagaland.gov.in/"),
    LandRecordWebsite("Odisha", "https://bhulekh.ori.nic.in/"),
    LandRecordWebsite("Punjab", "https://jamabandi.punjab.gov.in/"),
    LandRecordWebsite("Rajasthan", "https://apnakhata.raj.nic.in/"),
    LandRecordWebsite("Sikkim", "https://landrevenue.sikkim.gov.in/"),
    LandRecordWebsite("Tamil Nadu", "https://eservices.tn.gov.in/"),
    LandRecordWebsite("Telangana", "https://dharani.telangana.gov.in/"),
    LandRecordWebsite("Tripura", "https://jami.tripura.gov.in/"),
    LandRecordWebsite("Uttar Pradesh", "https://upbhulekh.gov.in/"),
    LandRecordWebsite("Uttarakhand", "https://bhulekh.uk.gov.in/"),
    LandRecordWebsite("West Bengal", "https://banglarbhumi.gov.in/"),
    LandRecordWebsite("Andaman & Nicobar", "https://andaman.nic.in/"),
    LandRecordWebsite("Chandigarh", "https://chandigarh.gov.in/"),
    LandRecordWebsite("Dadra & Nagar Haveli", "https://dnh.nic.in/"),
    LandRecordWebsite("Daman & Diu", "https://daman.nic.in/"),
    LandRecordWebsite("Delhi", "https://dlrc.delhi.gov.in/"),
    LandRecordWebsite("Jammu & Kashmir", "https://landrecords.jk.gov.in/"),
    LandRecordWebsite("Ladakh", "https://landrecords.ladakh.gov.in/"),
    LandRecordWebsite("Lakshadweep", "https://lakshadweep.gov.in/"),
    LandRecordWebsite("Puducherry", "https://py.gov.in/")
)

@Composable
fun LandRecordsScreen() {
    val context = LocalContext.current
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        // Header
        Text(
            text = "Indian Land Records",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "State-wise land record portals",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // List of states
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(indianLandRecords) { record ->
                LandRecordCard(
                    state = record.state,
                    url = record.url,
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(record.url))
                        context.startActivity(intent)
                    }
                )
            }
        }
    }
}

@Composable
fun LandRecordCard(
    state: String,
    url: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = state,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = url,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    maxLines = 1
                )
            }
            
            Icon(
                imageVector = Icons.Default.OpenInNew,
                contentDescription = "Open website",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
