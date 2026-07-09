package edu.cit.soriano.pawwatch.mobile.ui.features.application

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import edu.cit.soriano.pawwatch.mobile.network.RetrofitClient
import edu.cit.soriano.pawwatch.mobile.ui.components.LoadingIndicator
import edu.cit.soriano.pawwatch.mobile.util.SessionManager
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApplicationsScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val scope = rememberCoroutineScope()
    var applications by remember { mutableStateOf(listOf<edu.cit.soriano.pawwatch.mobile.model.AdoptionApplication>()) }
    var loading by remember { mutableStateOf(true) }
    val token = "Bearer ${sessionManager.getToken()}"

    fun fetchApplications() {
        scope.launch {
            try {
                val response = RetrofitClient.apiService.getMyApplications(token)
                if (response.isSuccessful) applications = response.body() ?: emptyList()
            } catch (e: Exception) {
                Toast.makeText(context, "Failed to load applications", Toast.LENGTH_SHORT).show()
            } finally {
                loading = false
            }
        }
    }

    LaunchedEffect(Unit) { fetchApplications() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Applications") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFFF6B2C),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        when {
            loading -> LoadingIndicator()
            applications.isEmpty() -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) { Text("No applications submitted yet.", color = Color.Gray) }
            else -> LazyColumn(
                modifier = Modifier.padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(applications) { app ->
                    ApplicationCard(
                        application = app,
                        onCancel = {
                            scope.launch {
                                try {
                                    val response = RetrofitClient.apiService
                                        .cancelApplication(token, app.applicationId)
                                    if (response.isSuccessful) {
                                        Toast.makeText(context, "Application cancelled.",
                                            Toast.LENGTH_SHORT).show()
                                        fetchApplications()
                                    }
                                } catch (e: Exception) {
                                    Toast.makeText(context, "Failed to cancel.",
                                        Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}// Application feature slice - handles adoption application tracking