
@file:OptIn(ExperimentalMaterial3Api::class)
package edu.cit.soriano.pawwatch.mobile.ui.components


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import edu.cit.soriano.pawwatch.mobile.model.Animal
import edu.cit.soriano.pawwatch.mobile.ui.theme.PawWatchColors

@Composable
fun AnimalCard(animal: Animal, onClick: () -> Unit) {
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