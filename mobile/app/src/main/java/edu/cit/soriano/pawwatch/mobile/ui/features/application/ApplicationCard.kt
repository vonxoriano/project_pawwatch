package edu.cit.soriano.pawwatch.mobile.ui.features.application

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
import edu.cit.soriano.pawwatch.mobile.model.AdoptionApplication
import edu.cit.soriano.pawwatch.mobile.ui.components.StatusBadge

@Composable
fun ApplicationCard(
    application: AdoptionApplication,
    onCancel: (() -> Unit)? = null
) {
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