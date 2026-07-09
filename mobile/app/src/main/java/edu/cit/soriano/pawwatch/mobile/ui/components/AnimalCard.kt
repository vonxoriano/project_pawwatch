package edu.cit.soriano.pawwatch.mobile.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.cit.soriano.pawwatch.mobile.model.Animal

@Composable
fun AnimalCard(animal: Animal, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (animal.species == "CAT") "🐱" else "🐶",
                fontSize = 40.sp,
                modifier = Modifier.size(56.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(animal.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(
                    "${animal.breed} · ${animal.age} yrs · ${animal.gender}",
                    fontSize = 13.sp, color = Color.Gray
                )
                Spacer(modifier = Modifier.height(6.dp))
                StatusBadge(status = animal.adoptionStatus)
            }
            Text("→", fontSize = 18.sp, color = Color(0xFFFF6B2C))
        }
    }
}