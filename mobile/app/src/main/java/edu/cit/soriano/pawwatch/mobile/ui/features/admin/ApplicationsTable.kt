package edu.cit.soriano.pawwatch.mobile.ui.features.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.cit.soriano.pawwatch.mobile.model.AdoptionApplication
import edu.cit.soriano.pawwatch.mobile.ui.components.StatusBadge
import edu.cit.soriano.pawwatch.mobile.ui.theme.PawWatchColors

@Composable
fun ApplicationsTab(
    applications: List<AdoptionApplication>,
    onProcess: (Long, String, String) -> Unit
) {
    val remarks = remember { mutableStateMapOf<Long, String>() }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Adoption Applications", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = PawWatchColors.TextDark)
        Spacer(modifier = Modifier.height(12.dp))

        if (applications.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No applications yet.", color = PawWatchColors.TextGray)
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(applications) { app ->
                    AdminApplicationCard(
                        application = app,
                        remarks = remarks[app.applicationId] ?: "",
                        onRemarksChange = { remarks[app.applicationId] = it },
                        onApprove = { onProcess(app.applicationId, "APPROVED", remarks[app.applicationId] ?: "") },
                        onReject = { onProcess(app.applicationId, "REJECTED", remarks[app.applicationId] ?: "") }
                    )
                }
            }
        }
    }
}

@Composable
fun AdminApplicationCard(
    application: AdoptionApplication,
    remarks: String,
    onRemarksChange: (String) -> Unit,
    onApprove: () -> Unit,
    onReject: () -> Unit
) {
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
                Column {
                    Text(application.animal.name, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = PawWatchColors.TextDark)
                    Text("by ${application.user?.fullName ?: "Unknown"}", fontSize = 12.sp, color = PawWatchColors.TextGray)
                }
                StatusBadge(status = application.status)
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text("${application.animal.species} · ${application.animal.breed}", fontSize = 13.sp, color = PawWatchColors.TextGray)

            if (application.status == "PENDING") {
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = remarks,
                    onValueChange = onRemarksChange,
                    label = { Text("Add remarks...") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(
                        onClick = onApprove,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = PawWatchColors.EditBlue)
                    ) { Text("Approve") }
                    OutlinedButton(
                        onClick = onReject,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = PawWatchColors.DeleteRed)
                    ) { Text("Reject") }
                }
            } else {
                application.remarks?.let {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("Remarks: $it", fontSize = 13.sp, color = PawWatchColors.TextGray)
                }
            }
        }
    }
}
