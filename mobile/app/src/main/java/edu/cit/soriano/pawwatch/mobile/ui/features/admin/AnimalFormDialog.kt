package edu.cit.soriano.pawwatch.mobile.ui.features.admin

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import edu.cit.soriano.pawwatch.mobile.model.Animal
import edu.cit.soriano.pawwatch.mobile.model.AnimalRequest
import edu.cit.soriano.pawwatch.mobile.network.uploadAnimalPhoto
import edu.cit.soriano.pawwatch.mobile.ui.components.ChoiceChipRow
import edu.cit.soriano.pawwatch.mobile.ui.components.LabeledTextField
import edu.cit.soriano.pawwatch.mobile.ui.components.LoadingIndicator
import edu.cit.soriano.pawwatch.mobile.ui.theme.PawWatchColors
import kotlinx.coroutines.launch

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

    var uploading by remember { mutableStateOf(false) }
    var uploadError by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (animal != null) "Edit Animal" else "Add New Animal") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
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
                PhotoPickerField(
                    photoUrl = photo,
                    uploading = uploading,
                    error = uploadError,
                    onPhotoUploaded = { url -> photo = url; uploadError = "" },
                    onUploadingChange = { uploading = it },
                    onError = { uploadError = it }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(AnimalRequest(name, species, breed, age.toIntOrNull() ?: 0, gender, description, healthStatus, photo))
                },
                enabled = !uploading,
                colors = ButtonDefaults.buttonColors(containerColor = PawWatchColors.Primary)
            ) { Text(if (animal != null) "Save Changes" else "Add Animal") }
        },
        dismissButton = { OutlinedButton(onClick = onDismiss) { Text("Cancel") } }
    )
}

/** Used only within AnimalFormDialog, so kept private rather than promoted to ui/components. */
@Composable
private fun PhotoPickerField(
    photoUrl: String,
    uploading: Boolean,
    error: String,
    onPhotoUploaded: (String) -> Unit,
    onUploadingChange: (Boolean) -> Unit,
    onError: (String) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            onError("")
            onUploadingChange(true)
            scope.launch {
                val result = uploadAnimalPhoto(context, uri)
                result.onSuccess { url -> onPhotoUploaded(url) }
                    .onFailure { e -> onError(e.message ?: "Photo upload failed.") }
                onUploadingChange(false)
            }
        }
    }

    Column {
        Text("Photo", fontSize = 13.sp)
        OutlinedButton(
            onClick = {
                pickImageLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            },
            enabled = !uploading
        ) {
            Text(if (photoUrl.isBlank()) "Choose Photo" else "Change Photo")
        }

        if (uploading) {
            Column {
                LoadingIndicator(modifier = Modifier.size(40.dp))
                Text("Uploading...", fontSize = 12.sp, color = PawWatchColors.TextGray)
            }
        } else if (photoUrl.isNotBlank()) {
            AsyncImage(
                model = photoUrl,
                contentDescription = "Selected photo",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
        }

        if (error.isNotBlank()) {
            Text("⚠️ $error", fontSize = 12.sp, color = PawWatchColors.DeleteRed)
        }
    }
}
