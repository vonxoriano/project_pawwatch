package edu.cit.soriano.pawwatch.mobile.ui.features.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.cit.soriano.pawwatch.mobile.model.Animal
import edu.cit.soriano.pawwatch.mobile.model.AnimalRequest
import edu.cit.soriano.pawwatch.mobile.ui.components.ChoiceChipRow
import edu.cit.soriano.pawwatch.mobile.ui.components.LabeledTextField
import edu.cit.soriano.pawwatch.mobile.ui.theme.PawWatchColors

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
                LabeledTextField(label = "Name", value = name, onValueChange = { name = it })
                ChoiceChipRow(
                    options = listOf("CAT" to "CAT", "DOG" to "DOG"),
                    selected = species,
                    onSelect = { species = it }
                )
                LabeledTextField(label = "Breed", value = breed, onValueChange = { breed = it })
                LabeledTextField(label = "Age", value = age, onValueChange = { age = it })
                ChoiceChipRow(
                    options = listOf("MALE" to "MALE", "FEMALE" to "FEMALE"),
                    selected = gender,
                    onSelect = { gender = it }
                )
                LabeledTextField(label = "Health Status", value = healthStatus, onValueChange = { healthStatus = it })
                LabeledTextField(label = "Description", value = description, onValueChange = { description = it })
                LabeledTextField(label = "Photo URL", value = photo, onValueChange = { photo = it })
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
