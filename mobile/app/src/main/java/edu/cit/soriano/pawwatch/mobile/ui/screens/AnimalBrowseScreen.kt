package edu.cit.soriano.pawwatch.mobile.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.cit.soriano.pawwatch.mobile.model.Animal
import edu.cit.soriano.pawwatch.mobile.network.RetrofitClient
import edu.cit.soriano.pawwatch.mobile.ui.components.AnimalCard
import edu.cit.soriano.pawwatch.mobile.ui.components.LoadingIndicator
import edu.cit.soriano.pawwatch.mobile.ui.components.PawWatchTopBar
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimalBrowseScreen(
    onAnimalClick: (Long) -> Unit,
    onMyApplicationsClick: () -> Unit,
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var animals by remember { mutableStateOf(listOf<Animal>()) }
    var keyword by remember { mutableStateOf("") }
    var species by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }

    fun fetchAnimals() {
        loading = true
        scope.launch {
            try {
                val response = RetrofitClient.apiService.browseAnimals(
                    keyword = keyword.ifBlank { null },
                    species = species.ifBlank { null }
                )
                if (response.isSuccessful) animals = response.body() ?: emptyList()
            } catch (e: Exception) {
                Toast.makeText(context, "Failed to load animals", Toast.LENGTH_SHORT).show()
            } finally {
                loading = false
            }
        }
    }

    LaunchedEffect(Unit) { fetchAnimals() }

    Column(modifier = Modifier.fillMaxSize()) {
        PawWatchTopBar(onLogout = onLogout)

        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Find Your Companion",
                    fontSize = 18.sp, fontWeight = FontWeight.Bold)
                TextButton(onClick = onMyApplicationsClick) {
                    Text("My Applications", color = Color(0xFFFF6B2C))
                }
            }
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = keyword,
                onValueChange = { keyword = it },
                label = { Text("Search by name or breed") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("" to "All", "CAT" to "🐱 Cats", "DOG" to "🐶 Dogs").forEach { (value, label) ->
                    FilterChip(
                        selected = species == value,
                        onClick = { species = value },
                        label = { Text(label) }
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = { fetchAnimals() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF6B2C)),
                    modifier = Modifier.weight(1f)
                ) { Text("Search") }
                OutlinedButton(
                    onClick = { keyword = ""; species = ""; fetchAnimals() },
                    modifier = Modifier.weight(1f)
                ) { Text("Reset") }
            }
        }

        if (loading) {
            LoadingIndicator()
        } else if (animals.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No animals available right now.", color = Color.Gray)
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(animals) { animal ->
                    AnimalCard(animal = animal, onClick = { onAnimalClick(animal.animalId) })
                }
            }
        }
    }
}