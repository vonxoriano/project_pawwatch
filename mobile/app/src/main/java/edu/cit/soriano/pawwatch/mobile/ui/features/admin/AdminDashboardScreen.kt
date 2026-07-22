@file:OptIn(ExperimentalMaterial3Api::class)

package edu.cit.soriano.pawwatch.mobile.ui.features.admin

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.cit.soriano.pawwatch.mobile.model.*
import edu.cit.soriano.pawwatch.mobile.network.RetrofitClient
import edu.cit.soriano.pawwatch.mobile.ui.components.LoadingIndicator
import edu.cit.soriano.pawwatch.mobile.ui.components.TopBar
import edu.cit.soriano.pawwatch.mobile.ui.theme.PawWatchColors
import edu.cit.soriano.pawwatch.mobile.util.SessionManager
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter

// Admin feature slice - handles animal management and application processing.
// Tab content and card composables live in AnimalTable.kt / ApplicationsTable.kt;
// the add/edit dialog lives in AnimalFormDialog.kt.
@Composable
fun AdminDashboardScreen(onProfileClick: () -> Unit, onLogout: () -> Unit) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val scope = rememberCoroutineScope()
    val token = "Bearer ${sessionManager.getToken()}"

    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("🐾 Animal Listings", "📋 Applications", "📊 Reports")

    var animals by remember { mutableStateOf(listOf<Animal>()) }
    var applications by remember { mutableStateOf(listOf<AdoptionApplication>()) }
    var loading by remember { mutableStateOf(true) }
    var showAddDialog by remember { mutableStateOf(false) }
    var editingAnimal by remember { mutableStateOf<Animal?>(null) }

    var appKeyword by remember { mutableStateOf("") }
    var appStatus by remember { mutableStateOf("") }
    var appDateFromMillis by remember { mutableStateOf<Long?>(null) }
    var appDateToMillis by remember { mutableStateOf<Long?>(null) }

    fun fetchAnimals() {
        scope.launch {
            try {
                val res = RetrofitClient.apiService.getAllAnimals(token)
                if (res.isSuccessful) animals = res.body() ?: emptyList()
            } catch (e: Exception) {
                Toast.makeText(context, "Failed to load animals", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun fetchApplications() {
        scope.launch {
            try {
                val fromStr = appDateFromMillis?.let { millisToLocalDate(it).format(DateTimeFormatter.ISO_LOCAL_DATE) }
                val toStr = appDateToMillis?.let { millisToLocalDate(it).format(DateTimeFormatter.ISO_LOCAL_DATE) }
                val res = RetrofitClient.apiService.getAllApplications(
                    token,
                    status = appStatus.ifBlank { null },
                    keyword = appKeyword.ifBlank { null },
                    dateFrom = fromStr,
                    dateTo = toStr
                )
                if (res.isSuccessful) applications = res.body() ?: emptyList()
            } catch (e: Exception) {
                Toast.makeText(context, "Failed to load applications", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun resetApplicationFilters() {
        appKeyword = ""
        appStatus = ""
        appDateFromMillis = null
        appDateToMillis = null
        fetchApplications()
    }

    LaunchedEffect(Unit) {
        fetchAnimals()
        fetchApplications()
        loading = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PawWatchColors.Background)
    ) {
        TopBar(userLabel = "Admin: Admin", onProfileClick = onProfileClick, onLogout = onLogout)

        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = PawWatchColors.CardWhite,
            contentColor = PawWatchColors.Primary
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(title, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                            if (index == 1) {
                                val pending = applications.count { it.status == "PENDING" }
                                if (pending > 0) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Surface(color = PawWatchColors.Primary, shape = RoundedCornerShape(20.dp)) {
                                        Text(
                                            "$pending",
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                            fontSize = 11.sp,
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                )
            }
        }

        if (loading) {
            LoadingIndicator()
        } else {
            when (selectedTab) {
                0 -> AnimalsTab(
                    animals = animals,
                    onAdd = { showAddDialog = true },
                    onEdit = { editingAnimal = it },
                    onDelete = { animalId ->
                        scope.launch {
                            try {
                                val res = RetrofitClient.apiService.deleteAnimal(token, animalId)
                                if (res.isSuccessful) {
                                    Toast.makeText(context, "Animal removed.", Toast.LENGTH_SHORT).show()
                                    fetchAnimals()
                                }
                            } catch (e: Exception) {
                                Toast.makeText(context, "Failed to delete.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                )
                1 -> ApplicationsTab(
                    applications = applications,
                    keyword = appKeyword,
                    onKeywordChange = { appKeyword = it },
                    status = appStatus,
                    onStatusChange = { appStatus = it },
                    dateFromMillis = appDateFromMillis,
                    onDateFromChange = { appDateFromMillis = it },
                    dateToMillis = appDateToMillis,
                    onDateToChange = { appDateToMillis = it },
                    onFilter = { fetchApplications() },
                    onReset = { resetApplicationFilters() },
                    onProcess = { appId, status, remarks ->
                        scope.launch {
                            try {
                                val res = RetrofitClient.apiService.processApplication(
                                    token, appId, ApplicationStatusRequest(status, remarks)
                                )
                                if (res.isSuccessful) {
                                    Toast.makeText(context, "Application ${status.lowercase()}.", Toast.LENGTH_SHORT).show()
                                    fetchApplications()
                                    fetchAnimals()
                                }
                            } catch (e: Exception) {
                                Toast.makeText(context, "Failed to process.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                )
                2 -> ReportPanel()
            }
        }
    }

    if (showAddDialog || editingAnimal != null) {
        AnimalFormDialog(
            animal = editingAnimal,
            onDismiss = { showAddDialog = false; editingAnimal = null },
            onSave = { request ->
                scope.launch {
                    try {
                        if (editingAnimal != null) {
                            RetrofitClient.apiService.editAnimal(token, editingAnimal!!.animalId, request)
                            Toast.makeText(context, "Animal updated.", Toast.LENGTH_SHORT).show()
                        } else {
                            RetrofitClient.apiService.addAnimal(token, request)
                            Toast.makeText(context, "Animal added.", Toast.LENGTH_SHORT).show()
                        }
                        showAddDialog = false
                        editingAnimal = null
                        fetchAnimals()
                    } catch (e: Exception) {
                        Toast.makeText(context, "Failed to save.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        )
    }
}
