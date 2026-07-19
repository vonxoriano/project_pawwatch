package edu.cit.soriano.pawwatch.mobile.ui.features.application

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.cit.soriano.pawwatch.mobile.model.ApplicationRequest
import edu.cit.soriano.pawwatch.mobile.network.RetrofitClient
import edu.cit.soriano.pawwatch.mobile.ui.components.LoadingIndicator
import edu.cit.soriano.pawwatch.mobile.util.SessionManager
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApplyApplicationScreen(
    animalId: Long,
    onBack: () -> Unit,
    onSubmitted: () -> Unit
) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val scope = rememberCoroutineScope()
    val token = "Bearer ${sessionManager.getToken()}"

    var animal by remember { mutableStateOf<edu.cit.soriano.pawwatch.mobile.model.Animal?>(null) }
    var loading by remember { mutableStateOf(true) }
    var submitting by remember { mutableStateOf(false) }

    var housingType by remember { mutableStateOf("OWN") }
    var hasLandlordPermission by remember { mutableStateOf<Boolean?>(null) }
    var hasYard by remember { mutableStateOf<Boolean?>(null) }
    var householdMembers by remember { mutableStateOf("") }
    var hasYoungChildren by remember { mutableStateOf<Boolean?>(null) }
    var hasOtherPets by remember { mutableStateOf<Boolean?>(null) }
    var petExperience by remember { mutableStateOf("") }
    var hoursAwayDaily by remember { mutableStateOf("") }
    var reasonForAdopting by remember { mutableStateOf("") }
    var agreesToReturnPolicy by remember { mutableStateOf(false) }
    var remarks by remember { mutableStateOf("") }

    LaunchedEffect(animalId) {
        try {
            val response = RetrofitClient.apiService.getAnimalById(animalId)
            if (response.isSuccessful) animal = response.body()
        } catch (e: Exception) {
            Toast.makeText(context, "Failed to load animal", Toast.LENGTH_SHORT).show()
        } finally {
            loading = false
        }
    }

    fun validate(): String? {
        if (housingType == "RENT" && hasLandlordPermission == null) return "Please indicate if you have landlord permission."
        if (hasYard == null) return "Please indicate if you have a yard."
        if (householdMembers.isBlank()) return "Please enter number of household members."
        if (hasYoungChildren == null) return "Please indicate if you have young children."
        if (hasOtherPets == null) return "Please indicate if you have other pets."
        if (petExperience.isBlank()) return "Please describe your pet experience."
        if (hoursAwayDaily.isBlank()) return "Please enter hours away daily."
        if (reasonForAdopting.isBlank()) return "Please provide your reason for adopting."
        if (!agreesToReturnPolicy) return "You must agree to the return policy to proceed."
        return null
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Adoption Application") },
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
        if (loading) {
            LoadingIndicator()
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Text(
                "Please answer honestly so our shelter staff can find the best match for ${animal?.name ?: "this pet"}.",
                fontSize = 13.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text("Do you own or rent your home?", fontWeight = FontWeight.Medium, fontSize = 14.sp)
            Row(modifier = Modifier.fillMaxWidth()) {
                FilterChip(
                    selected = housingType == "OWN",
                    onClick = { housingType = "OWN" },
                    label = { Text("Own") },
                    modifier = Modifier.padding(end = 8.dp)
                )
                FilterChip(
                    selected = housingType == "RENT",
                    onClick = { housingType = "RENT" },
                    label = { Text("Rent") }
                )
            }
            Spacer(modifier = Modifier.height(12.dp))

            if (housingType == "RENT") {
                Text("Do you have landlord permission to keep pets?", fontWeight = FontWeight.Medium, fontSize = 14.sp)
                YesNoRow(value = hasLandlordPermission, onChange = { hasLandlordPermission = it })
                Spacer(modifier = Modifier.height(12.dp))
            }

            Text("Do you have a yard?", fontWeight = FontWeight.Medium, fontSize = 14.sp)
            YesNoRow(value = hasYard, onChange = { hasYard = it })
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = householdMembers,
                onValueChange = { householdMembers = it },
                label = { Text("How many members are in your household?") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))

            Text("Do you have young children at home?", fontWeight = FontWeight.Medium, fontSize = 14.sp)
            YesNoRow(value = hasYoungChildren, onChange = { hasYoungChildren = it })
            Spacer(modifier = Modifier.height(12.dp))

            Text("Do you currently have other pets?", fontWeight = FontWeight.Medium, fontSize = 14.sp)
            YesNoRow(value = hasOtherPets, onChange = { hasOtherPets = it })
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = petExperience,
                onValueChange = { petExperience = it },
                label = { Text("Describe your experience with pets") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = hoursAwayDaily,
                onValueChange = { hoursAwayDaily = it },
                label = { Text("Hours per day the pet will be left alone") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = reasonForAdopting,
                onValueChange = { reasonForAdopting = it },
                label = { Text("Why do you want to adopt?") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = remarks,
                onValueChange = { remarks = it },
                label = { Text("Additional remarks (optional)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                Checkbox(checked = agreesToReturnPolicy, onCheckedChange = { agreesToReturnPolicy = it })
                Text("I agree to the shelter's return policy if the adoption doesn't work out.", fontSize = 13.sp)
            }
            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    val validationError = validate()
                    if (validationError != null) {
                        Toast.makeText(context, validationError, Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    submitting = true
                    scope.launch {
                        try {
                            val response = RetrofitClient.apiService.submitApplication(
                                token = token,
                                request = ApplicationRequest(
                                    animalId = animalId,
                                    housingType = housingType,
                                    hasLandlordPermission = if (housingType == "RENT") hasLandlordPermission else null,
                                    hasYard = hasYard!!,
                                    householdMembers = householdMembers.toIntOrNull() ?: 0,
                                    hasYoungChildren = hasYoungChildren!!,
                                    hasOtherPets = hasOtherPets!!,
                                    petExperience = petExperience,
                                    hoursAwayDaily = hoursAwayDaily.toIntOrNull() ?: 0,
                                    reasonForAdopting = reasonForAdopting,
                                    agreesToReturnPolicy = agreesToReturnPolicy,
                                    remarks = remarks
                                )
                            )
                            if (response.isSuccessful) {
                                Toast.makeText(context, "Application submitted successfully!", Toast.LENGTH_LONG).show()
                                onSubmitted()
                            } else {
                                Toast.makeText(context, "Failed to submit application.", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                        } finally {
                            submitting = false
                        }
                    }
                },
                enabled = !submitting,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF6B2C))
            ) {
                Text(if (submitting) "Submitting..." else "Submit Application", fontSize = 16.sp)
            }
        }
    }
}

@Composable
private fun YesNoRow(value: Boolean?, onChange: (Boolean) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth()) {
        FilterChip(
            selected = value == true,
            onClick = { onChange(true) },
            label = { Text("Yes") },
            modifier = Modifier.padding(end = 8.dp)
        )
        FilterChip(
            selected = value == false,
            onClick = { onChange(false) },
            label = { Text("No") }
        )
    }
}
