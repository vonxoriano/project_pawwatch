package edu.cit.soriano.pawwatch.mobile.ui.features.application

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.cit.soriano.pawwatch.mobile.model.AdoptionApplication
import edu.cit.soriano.pawwatch.mobile.ui.components.StatusBadge

private fun yesNo(value: Boolean?): String = when (value) {
    true -> "Yes"
    false -> "No"
    null -> "—"
}

@Composable
fun ApplicationCard(
    application: AdoptionApplication,
    onCancel: (() -> Unit)? = null
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(application.animal.name,
                    fontWeight = FontWeight.Bold, fontSize = 16.sp)
                StatusBadge(status = application.status)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "${application.animal.species} · ${application.animal.breed}",
                fontSize = 13.sp, color = Color.Gray
            )
            application.remarks?.let {
                Spacer(modifier = Modifier.height(4.dp))
                Text("Remarks: $it", fontSize = 13.sp, color = Color.DarkGray)
            }

            Spacer(modifier = Modifier.height(8.dp))
            TextButton(onClick = { expanded = !expanded }) {
                Text(if (expanded) "Hide Questionnaire" else "View Questionnaire", fontSize = 13.sp)
            }

            if (expanded) {
                Column(modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)) {
                    Text("Housing type: ${application.housingType ?: "—"}", fontSize = 12.sp, color = Color.DarkGray)
                    Text("Landlord permission: ${yesNo(application.hasLandlordPermission)}", fontSize = 12.sp, color = Color.DarkGray)
                    Text("Has yard: ${yesNo(application.hasYard)}", fontSize = 12.sp, color = Color.DarkGray)
                    Text("Household members: ${application.householdMembers ?: "—"}", fontSize = 12.sp, color = Color.DarkGray)
                    Text("Young children at home: ${yesNo(application.hasYoungChildren)}", fontSize = 12.sp, color = Color.DarkGray)
                    Text("Has other pets: ${yesNo(application.hasOtherPets)}", fontSize = 12.sp, color = Color.DarkGray)
                    Text("Hours away daily: ${application.hoursAwayDaily ?: "—"}", fontSize = 12.sp, color = Color.DarkGray)
                    Text("Agrees to return policy: ${yesNo(application.agreesToReturnPolicy)}", fontSize = 12.sp, color = Color.DarkGray)
                    Text("Pet experience: ${application.petExperience ?: "—"}", fontSize = 12.sp, color = Color.DarkGray)
                    Text("Reason for adopting: ${application.reasonForAdopting ?: "—"}", fontSize = 12.sp, color = Color.DarkGray)
                }
            }

            if (application.status == "PENDING" && onCancel != null) {
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedButton(
                    onClick = onCancel,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFFE53935)
                    )
                ) {
                    Text("Cancel Application")
                }
            }
        }
    }
}