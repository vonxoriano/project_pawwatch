@file:OptIn(ExperimentalMaterial3Api::class)

package edu.cit.soriano.pawwatch.mobile.ui.features.admin

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import edu.cit.soriano.pawwatch.mobile.ui.components.StatusBadge
import edu.cit.soriano.pawwatch.mobile.ui.theme.PawWatchColors
import edu.cit.soriano.pawwatch.mobile.util.SessionManager
import kotlinx.coroutines.launch


// Admin feature slice - handles animal management and application processing
@Composable
fun AdminDashboardScreen(onLogout: () -> Unit) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val scope = rememberCoroutineScope()
    val token = "Bearer ${sessionManager.getToken()}"

    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("🐾 Animal Listings", "📋 Applications")

    var animals by remember { mutableStateOf(listOf<Animal>()) }
    var applications by remember { mutableStateOf(listOf<AdoptionApplication>()) }
    var loading by remember { mutableStateOf(true) }
    var showAddDialog by remember { mutableStateOf(false) }
    var editingAnimal by remember { mutableStateOf<Animal?>(null) }

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
                val res = RetrofitClient.apiService.getAllApplications(token)
                if (res.isSuccessful) applications = res.body() ?: emptyList()
            } catch (e: Exception) {
                Toast.makeText(context, "Failed to load applications", Toast.LENGTH_SHORT).show()
            }
        }
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
        TopBar(userLabel = "Admin: Admin", onLogout = onLogout)

        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color.White,
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

@Composable
fun AnimalsTab(
    animals: List<Animal>,
    onAdd: () -> Unit,
    onEdit: (Animal) -> Unit,
    onDelete: (Long) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Animal Listings", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = PawWatchColors.TextDark)
            Button(
                onClick = onAdd,
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PawWatchColors.Primary)
            ) { Text("+ Add Animal") }
        }
        Spacer(modifier = Modifier.height(12.dp))

        if (animals.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No animals listed yet.", color = PawWatchColors.TextGray)
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(animals) { animal ->
                    AdminAnimalCard(
                        animal = animal,
                        onEdit = { onEdit(animal) },
                        onDelete = { onDelete(animal.animalId) }
                    )
                }
            }
        }
    }
}

@Composable
fun AdminAnimalCard(animal: Animal, onEdit: () -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(animal.name, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = PawWatchColors.TextDark)
                StatusBadge(status = animal.adoptionStatus)
            }
            Text(
                "${animal.species} · ${animal.breed} · ${animal.age} yrs · ${animal.gender}",
                fontSize = 13.sp, color = PawWatchColors.TextGray
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(
                    onClick = onEdit,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = PawWatchColors.EditBlue)
                ) { Text("Edit") }
                OutlinedButton(
                    onClick = onDelete,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = PawWatchColors.DeleteRed)
                ) { Text("Delete") }
            }
        }
    }
}

@Composable
fun ApplicationsTab(
    applications: List<AdoptionApplication>,
    onProcess: (Long, String, String) -> Unit
) {
    val remarks = remember { mutableStateMapOf<Long, String>() }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Adoption Applications", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = PawWatchColors.TextDark)
        Spacer(modifier = Modifier.height(12.dp))

        if (applications.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No applications yet.", color = PawWatchColors.TextGray)
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(applications) { app ->
                    AdminApplicationCard(
                        application = app,
                        remarks = remarks[app.applicationId] ?: "",
                        onRemarksChange = { remarks[app.applicationId] = it },
                        onApprove = { onProcess(app.applicationId, "APPROVED", remarks[app.applicationId] ?: "") },
                        onReject = { onProcess(app.applicationId, "REJECTED", remarks[app.applicationId] ?: "") }
                    )
                }
            }
        }
    }
}

@Composable
fun AdminApplicationCard(
    application: AdoptionApplication,
    remarks: String,
    onRemarksChange: (String) -> Unit,
    onApprove: () -> Unit,
    onReject: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(application.animal.name, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = PawWatchColors.TextDark)
                    Text("by ${application.user?.fullName ?: "Unknown"}", fontSize = 12.sp, color = PawWatchColors.TextGray)
                }
                StatusBadge(status = application.status)
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text("${application.animal.species} · ${application.animal.breed}", fontSize = 13.sp, color = PawWatchColors.TextGray)

            if (application.status == "PENDING") {
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = remarks,
                    onValueChange = onRemarksChange,
                    label = { Text("Add remarks...") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(
                        onClick = onApprove,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = PawWatchColors.EditBlue)
                    ) { Text("Approve") }
                    OutlinedButton(
                        onClick = onReject,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = PawWatchColors.DeleteRed)
                    ) { Text("Reject") }
                }
            } else {
                application.remarks?.let {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("Remarks: $it", fontSize = 13.sp, color = PawWatchColors.TextGray)
                }
            }
        }
    }
}

@Composable
fun AnimalFormDialog(
    animal: Animal?,
    onDismiss: () -> Unit,
    onSave: (AnimalRequest) -> Unit
) {
    var name by remember { mutableStateOf(animal?.name ?: "") }
    var species by remember { mutableStateOf(animal?.species ?: "CAT") }
    var breed by remember { mutableStateOf(animal?.breed ?: "") }
    var age by remember { mutableStateOf(animal?.age?.toString() ?: "") }
    var gender by remember { mutableStateOf(animal?.gender ?: "MALE") }
    var description by remember { mutableStateOf(animal?.description ?: "") }
    var healthStatus by remember { mutableStateOf(animal?.healthStatus ?: "") }
    var photo by remember { mutableStateOf(animal?.photo ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (animal != null) "Edit Animal" else "Add New Animal") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") }, modifier = Modifier.fillMaxWidth())
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("CAT", "DOG").forEach { s ->
                        FilterChip(selected = species == s, onClick = { species = s }, label = { Text(s) })
                    }
                }
                OutlinedTextField(value = breed, onValueChange = { breed = it }, label = { Text("Breed") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = age, onValueChange = { age = it }, label = { Text("Age") }, modifier = Modifier.fillMaxWidth())
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("MALE", "FEMALE").forEach { g ->
                        FilterChip(selected = gender == g, onClick = { gender = g }, label = { Text(g) })
                    }
                }
                OutlinedTextField(value = healthStatus, onValueChange = { healthStatus = it }, label = { Text("Health Status") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Description") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = photo, onValueChange = { photo = it }, label = { Text("Photo URL") }, modifier = Modifier.fillMaxWidth())
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(AnimalRequest(name, species, breed, age.toIntOrNull() ?: 0, gender, description, healthStatus, photo))
                },
                colors = ButtonDefaults.buttonColors(containerColor = PawWatchColors.Primary)
            ) { Text(if (animal != null) "Save Changes" else "Add Animal") }
        },
        dismissButton = { OutlinedButton(onClick = onDismiss) { Text("Cancel") } }
    )
}