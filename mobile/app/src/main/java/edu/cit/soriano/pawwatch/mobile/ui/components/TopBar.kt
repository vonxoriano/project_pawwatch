package edu.cit.soriano.pawwatch.mobile.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PawWatchTopBar(onLogout: () -> Unit) {
    Surface(shadowElevation = 4.dp, color = MaterialTheme.colorScheme.surface) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("🐾 PawWatch", fontSize = 20.sp,
                fontWeight = FontWeight.Bold, color = Color(0xFFFF6B2C))
            TextButton(onClick = onLogout) {
                Text("Log Out", color = Color(0xFFFF6B2C))
            }
        }
    }
}