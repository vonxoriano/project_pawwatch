@file:OptIn(ExperimentalMaterial3Api::class)

package edu.cit.soriano.pawwatch.mobile.ui.features.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.cit.soriano.pawwatch.mobile.model.ReportSummary
import edu.cit.soriano.pawwatch.mobile.network.RetrofitClient
import edu.cit.soriano.pawwatch.mobile.ui.components.LoadingIndicator
import edu.cit.soriano.pawwatch.mobile.ui.components.PrimaryButton
import edu.cit.soriano.pawwatch.mobile.ui.theme.PawWatchColors
import edu.cit.soriano.pawwatch.mobile.util.SessionManager
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

// DatePickerState.selectedDateMillis is UTC midnight of the picked date, not
// local midnight - converting with the device's zone instead of UTC would
// shift the date back a day for anyone west of UTC.
internal fun millisToLocalDate(millis: Long): LocalDate =
    Instant.ofEpochMilli(millis).atZone(ZoneOffset.UTC).toLocalDate()

internal val displayFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy")

@Composable
fun ReportPanel() {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val token = "Bearer ${sessionManager.getToken()}"
    val scope = rememberCoroutineScope()

    var startDateMillis by remember { mutableStateOf<Long?>(null) }
    var endDateMillis by remember { mutableStateOf<Long?>(null) }
    var showStartPicker by remember { mutableStateOf(false) }
    var showEndPicker by remember { mutableStateOf(false) }

    var report by remember { mutableStateOf<ReportSummary?>(null) }
    var error by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }

    fun generateReport() {
        error = ""
        loading = true
        scope.launch {
            try {
                val startStr = startDateMillis?.let { millisToLocalDate(it).format(DateTimeFormatter.ISO_LOCAL_DATE) }
                val endStr = endDateMillis?.let { millisToLocalDate(it).format(DateTimeFormatter.ISO_LOCAL_DATE) }
                val response = RetrofitClient.apiService.getReport(token, startStr, endStr)
                if (response.isSuccessful) {
                    report = response.body()
                } else {
                    error = response.errorBody()?.string()?.trim('"')?.takeIf { it.isNotBlank() }
                        ?: "Failed to generate report."
                    report = null
                }
            } catch (e: Exception) {
                error = "Failed to generate report."
                report = null
            } finally {
                loading = false
            }
        }
    }

    fun resetReport() {
        startDateMillis = null
        endDateMillis = null
        report = null
        error = ""
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Adoption Report", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = PawWatchColors.TextDark)
        Spacer(modifier = Modifier.height(12.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(
                onClick = { showStartPicker = true },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    startDateMillis?.let { millisToLocalDate(it).format(displayFormatter) } ?: "Start date",
                    fontSize = 13.sp
                )
            }
            OutlinedButton(
                onClick = { showEndPicker = true },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    endDateMillis?.let { millisToLocalDate(it).format(displayFormatter) } ?: "End date",
                    fontSize = 13.sp
                )
            }
        }
        Spacer(modifier = Modifier.height(10.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            PrimaryButton(
                text = "Generate Report",
                onClick = { generateReport() },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.weight(1f)
            )
            OutlinedButton(
                onClick = { resetReport() },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.weight(1f)
            ) { Text("Reset") }
        }
        Spacer(modifier = Modifier.height(20.dp))

        if (error.isNotBlank()) {
            Text("⚠️ $error", color = PawWatchColors.DeleteRed, fontSize = 13.sp)
            Spacer(modifier = Modifier.height(12.dp))
        }

        when {
            loading -> LoadingIndicator()

            report != null && report!!.totalApplications == 0L -> Box(
                modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "📊 No applications were submitted within the selected date range.",
                    color = PawWatchColors.TextGray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
            }

            report != null -> {
                val r = report!!
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        ReportStatCard(
                            label = "Total Applications",
                            value = r.totalApplications,
                            bgColor = PawWatchColors.InputBg,
                            textColor = PawWatchColors.Primary,
                            modifier = Modifier.weight(1f)
                        )
                        ReportStatCard(
                            label = "Approved",
                            value = r.approvedCount,
                            bgColor = PawWatchColors.StatusAvailableBg,
                            textColor = PawWatchColors.StatusAvailableText,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        ReportStatCard(
                            label = "Rejected",
                            value = r.rejectedCount,
                            bgColor = PawWatchColors.StatusRejectedBg,
                            textColor = PawWatchColors.StatusRejectedText,
                            modifier = Modifier.weight(1f)
                        )
                        ReportStatCard(
                            label = "Pending",
                            value = r.pendingCount,
                            bgColor = PawWatchColors.StatusPendingBg,
                            textColor = PawWatchColors.StatusPendingText,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            error.isBlank() -> Box(
                modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "📊 Select a date range and tap \"Generate Report\" to view adoption activity.",
                    color = PawWatchColors.TextGray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
            }
        }
    }

    if (showStartPicker) {
        val state = rememberDatePickerState(initialSelectedDateMillis = startDateMillis)
        DatePickerDialog(
            onDismissRequest = { showStartPicker = false },
            confirmButton = {
                TextButton(onClick = {
                    startDateMillis = state.selectedDateMillis
                    showStartPicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showStartPicker = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = state)
        }
    }

    if (showEndPicker) {
        val state = rememberDatePickerState(initialSelectedDateMillis = endDateMillis)
        DatePickerDialog(
            onDismissRequest = { showEndPicker = false },
            confirmButton = {
                TextButton(onClick = {
                    endDateMillis = state.selectedDateMillis
                    showEndPicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showEndPicker = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = state)
        }
    }
}

@Composable
private fun ReportStatCard(
    label: String,
    value: Long,
    bgColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("$value", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = textColor)
            Spacer(modifier = Modifier.height(4.dp))
            Text(label, fontSize = 12.sp, color = textColor, textAlign = TextAlign.Center)
        }
    }
}
