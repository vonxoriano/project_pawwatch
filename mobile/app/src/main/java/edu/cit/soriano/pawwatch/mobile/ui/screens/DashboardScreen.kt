package edu.cit.soriano.pawwatch.mobile.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import edu.cit.soriano.pawwatch.mobile.util.SessionManager

@Composable
fun DashboardScreen(
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Welcome, ${sessionManager.getFullName()}",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text("Role: ${sessionManager.getRole()}")

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = {
            sessionManager.clearSession()
            onLogout()
        }) {
            Text("Log Out")
        }
    }
}