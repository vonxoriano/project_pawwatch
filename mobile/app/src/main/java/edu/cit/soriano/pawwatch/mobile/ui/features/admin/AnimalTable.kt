package edu.cit.soriano.pawwatch.mobile.ui.features.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.cit.soriano.pawwatch.mobile.model.Animal
import edu.cit.soriano.pawwatch.mobile.ui.components.StatusBadge
import edu.cit.soriano.pawwatch.mobile.ui.theme.PawWatchColors

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
        colors = CardDefaults.cardColors(containerColor = PawWatchColors.CardWhite),
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
