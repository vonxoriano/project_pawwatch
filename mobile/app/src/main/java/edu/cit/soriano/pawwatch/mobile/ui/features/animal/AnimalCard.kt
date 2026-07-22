@file:OptIn(ExperimentalMaterial3Api::class)

package edu.cit.soriano.pawwatch.mobile.ui.features.animal

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import edu.cit.soriano.pawwatch.mobile.model.Animal
import edu.cit.soriano.pawwatch.mobile.network.RetrofitClient
import edu.cit.soriano.pawwatch.mobile.ui.components.StatusBadge
import edu.cit.soriano.pawwatch.mobile.ui.theme.PawWatchColors
import edu.cit.soriano.pawwatch.mobile.util.SessionManager
import kotlinx.coroutines.launch

@Composable
fun AnimalCard(
    animal: Animal,
    onClick: () -> Unit,
    onFavoriteChange: ((Long, Boolean) -> Unit)? = null
) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val token = "Bearer ${sessionManager.getToken()}"
    val scope = rememberCoroutineScope()

    var isFavorite by remember { mutableStateOf(false) }
    var toggling by remember { mutableStateOf(false) }

    LaunchedEffect(animal.animalId) {
        try {
            val response = RetrofitClient.apiService.checkFavorite(token, animal.animalId)
            if (response.isSuccessful) isFavorite = response.body() ?: false
        } catch (e: Exception) {
            // Not logged in or check failed - default to unfavorited, no error shown
        }
    }

    fun toggleFavorite() {
        if (toggling) return
        toggling = true
        val wasFavorite = isFavorite
        scope.launch {
            try {
                if (wasFavorite) {
                    RetrofitClient.apiService.removeFavorite(token, animal.animalId)
                    isFavorite = false
                } else {
                    RetrofitClient.apiService.addFavorite(token, animal.animalId)
                    isFavorite = true
                }
                onFavoriteChange?.invoke(animal.animalId, !wasFavorite)
            } catch (e: Exception) {
                // Re-sync with backend truth in case of a stale/conflicting state
                try {
                    val response = RetrofitClient.apiService.checkFavorite(token, animal.animalId)
                    if (response.isSuccessful) isFavorite = response.body() ?: false
                } catch (syncErr: Exception) {
                    // Ignore - nothing more we can do here
                }
            } finally {
                toggling = false
            }
        }
    }

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = PawWatchColors.CardWhite),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Column {
            AsyncImage(
                model = animal.photo,
                contentDescription = animal.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            )
            Column(modifier = Modifier.padding(14.dp)) {
                Text(animal.name, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = PawWatchColors.TextDark)
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    "${animal.breed} · ${animal.age} yrs · ${animal.gender}",
                    fontSize = 12.sp,
                    color = PawWatchColors.TextGray
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StatusBadge(status = animal.adoptionStatus)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = { toggleFavorite() },
                            enabled = !toggling,
                            modifier = Modifier.size(28.dp)
                        ) {
                            Text(if (isFavorite) "❤️" else "🤍", fontSize = 16.sp)
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            "View Profile →",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = PawWatchColors.Primary
                        )
                    }
                }
            }
        }
    }
}
