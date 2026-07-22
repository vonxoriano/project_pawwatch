package edu.cit.soriano.pawwatch.mobile.ui.features.animal

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import edu.cit.soriano.pawwatch.mobile.model.Favorite
import edu.cit.soriano.pawwatch.mobile.network.RetrofitClient
import edu.cit.soriano.pawwatch.mobile.ui.components.DetailTopBar
import edu.cit.soriano.pawwatch.mobile.ui.components.LoadingIndicator
import edu.cit.soriano.pawwatch.mobile.ui.theme.PawWatchColors
import edu.cit.soriano.pawwatch.mobile.util.SessionManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    onAnimalClick: (Long) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val token = "Bearer ${sessionManager.getToken()}"

    var favorites by remember { mutableStateOf(listOf<Favorite>()) }
    var loading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        try {
            val response = RetrofitClient.apiService.getMyFavorites(token)
            if (response.isSuccessful) favorites = response.body() ?: emptyList()
        } catch (e: Exception) {
            Toast.makeText(context, "Failed to load favorites", Toast.LENGTH_SHORT).show()
        } finally {
            loading = false
        }
    }

    Scaffold(
        topBar = { DetailTopBar(title = "Favorites", onBack = onBack) }
    ) { padding ->
        when {
            loading -> LoadingIndicator()
            favorites.isEmpty() -> Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "💔 No favorites yet. Browse animals and tap the heart to save them here!",
                    color = PawWatchColors.TextGray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 32.dp)
                )
            }
            else -> LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
            ) {
                items(favorites, key = { it.favoriteId }) { favorite ->
                    AnimalCard(
                        animal = favorite.animal,
                        onClick = { onAnimalClick(favorite.animal.animalId) },
                        onFavoriteChange = { animalId, isFavorite ->
                            if (!isFavorite) {
                                favorites = favorites.filterNot { it.animal.animalId == animalId }
                            }
                        }
                    )
                }
            }
        }
    }
}
