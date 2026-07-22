@file:OptIn(ExperimentalMaterial3Api::class)

package edu.cit.soriano.pawwatch.mobile.ui.features.animal

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import edu.cit.soriano.pawwatch.mobile.model.Animal
import edu.cit.soriano.pawwatch.mobile.network.RetrofitClient
import edu.cit.soriano.pawwatch.mobile.ui.components.LoadingIndicator
import edu.cit.soriano.pawwatch.mobile.ui.components.PrimaryButton
import edu.cit.soriano.pawwatch.mobile.ui.components.TopBar
import edu.cit.soriano.pawwatch.mobile.ui.theme.PawWatchColors
import kotlinx.coroutines.launch

@Composable
fun AnimalBrowseScreen(
    onAnimalClick: (Long) -> Unit,
    onMyApplicationsClick: () -> Unit,
    onFavoritesClick: () -> Unit,
    onProfileClick: () -> Unit,
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PawWatchColors.Background)
    ) {
        TopBar(showNotifications = true, onProfileClick = onProfileClick, onLogout = onLogout)

        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "🐾 Find Your Companion",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = PawWatchColors.TextDark
                )
            }
            Spacer(modifier = Modifier.height(10.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(
                    onClick = onFavoritesClick,
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = PawWatchColors.Primary)
                ) {
                    Text("❤️ Favorites", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                }
                OutlinedButton(
                    onClick = onMyApplicationsClick,
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = PawWatchColors.Primary)
                ) {
                    Text("My Applications", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            OutlinedTextField(
                value = keyword,
                onValueChange = { keyword = it },
                label = { Text("Search by name or breed") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(10.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("" to "All", "CAT" to "🐱 Cats", "DOG" to "🐶 Dogs").forEach { (value, label) ->
                    FilterChip(
                        selected = species == value,
                        onClick = { species = value },
                        label = { Text(label) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = PawWatchColors.Primary,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                PrimaryButton(
                    text = "Search",
                    onClick = { fetchAnimals() },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f)
                )
                OutlinedButton(
                    onClick = { keyword = ""; species = ""; fetchAnimals() },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.weight(1f)
                ) { Text("Reset") }
            }
        }

        if (loading) {
            LoadingIndicator()
        } else if (animals.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No animals available right now.", color = PawWatchColors.TextGray)
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(animals) { animal ->
                    AnimalCard(animal = animal, onClick = { onAnimalClick(animal.animalId) })
                }
            }
        }
    }
}

// Animal feature slice - handles animal browsing and search