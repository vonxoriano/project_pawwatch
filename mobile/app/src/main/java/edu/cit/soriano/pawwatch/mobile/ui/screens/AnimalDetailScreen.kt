package edu.cit.soriano.pawwatch.mobile.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.cit.soriano.pawwatch.mobile.model.ApplicationRequest
import edu.cit.soriano.pawwatch.mobile.network.RetrofitClient
import edu.cit.soriano.pawwatch.mobile.ui.components.LoadingIndicator
import edu.cit.soriano.pawwatch.mobile.ui.components.StatusBadge
import edu.cit.soriano.pawwatch.mobile.util.SessionManager
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimalDetailScreen(
    animalId: Long,
    onBack: () -> Unit,
    onApplicationSubmitted: () -> Unit
) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val scope = rememberCoroutineScope()
    var animal by remember { mutableStateOf<edu.cit.soriano.pawwatch.mobile.model.Animal?>(null) }
    var loading by remember { mutableStateOf(true) }
    var applying by remember { mutableStateOf(false) }
    val role = sessionManager.getRole()
    val token = "Bearer ${sessionManager.getToken()}"

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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(animal?.name ?: "Animal Profile") },
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
            animal == null -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) { Text("Animal not found.", color = Color.Gray) }
            else -> Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text(if (animal!!.species == "CAT") "🐱" else "🐶", fontSize = 80.sp)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(animal!!.name, fontSize = 26.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    listOf(
                        "🐾 ${animal!!.species}",
                        "🏷️ ${animal!!.breed}",
                        "🎂 ${animal!!.age} yrs",
                        "⚥ ${animal!!.gender}"
                    ).forEach { tag ->
                        Surface(color = Color(0xFFF5F5F5), shape = RoundedCornerShape(20.dp)) {
                            Text(tag,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                fontSize = 12.sp, color = Color.DarkGray)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                animal!!.healthStatus?.let {
                    Text("❤️ Health: $it", fontSize = 14.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(8.dp))
                }
                animal!!.description?.let {
                    Text(it, fontSize = 14.sp, color = Color.DarkGray, lineHeight = 22.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                }

                StatusBadge(status = animal!!.adoptionStatus)

                if (role == "ADOPTER" && animal!!.adoptionStatus == "AVAILABLE") {
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = {
                            applying = true
                            scope.launch {
                                try {
                                    val response = RetrofitClient.apiService.submitApplication(
                                        token = token,
                                        request = ApplicationRequest(animal!!.animalId)
                                    )
                                    if (response.isSuccessful) {
                                        Toast.makeText(context,
                                            "Application submitted successfully!",
                                            Toast.LENGTH_LONG).show()
                                        onApplicationSubmitted()
                                    } else {
                                        Toast.makeText(context,
                                            "Failed to submit application.",
                                            Toast.LENGTH_SHORT).show()
                                    }
                                } catch (e: Exception) {
                                    Toast.makeText(context, "Error: ${e.message}",
                                        Toast.LENGTH_SHORT).show()
                                } finally {
                                    applying = false
                                }
                            }
                        },
                        enabled = !applying,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF6B2C))
                    ) {
                        Text(if (applying) "Submitting..." else "🐾 Adopt Me", fontSize = 16.sp)
                    }
                }
            }
        }
    }
}