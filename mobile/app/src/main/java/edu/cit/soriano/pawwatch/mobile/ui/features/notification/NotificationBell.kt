@file:OptIn(ExperimentalMaterial3Api::class)

package edu.cit.soriano.pawwatch.mobile.ui.features.notification

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.cit.soriano.pawwatch.mobile.model.Notification
import edu.cit.soriano.pawwatch.mobile.network.RetrofitClient
import edu.cit.soriano.pawwatch.mobile.ui.theme.PawWatchColors
import edu.cit.soriano.pawwatch.mobile.util.SessionManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private const val POLL_INTERVAL_MS = 45_000L

private fun formatRelativeTime(dateSent: String): String {
    return try {
        val date = LocalDateTime.parse(dateSent)
        val seconds = Duration.between(date, LocalDateTime.now()).seconds
        when {
            seconds < 60 -> "just now"
            seconds < 3600 -> {
                val minutes = seconds / 60
                "$minutes minute${if (minutes == 1L) "" else "s"} ago"
            }
            seconds < 86_400 -> {
                val hours = seconds / 3600
                "$hours hour${if (hours == 1L) "" else "s"} ago"
            }
            seconds < 604_800 -> {
                val days = seconds / 86_400
                "$days day${if (days == 1L) "" else "s"} ago"
            }
            else -> date.format(DateTimeFormatter.ofPattern("MMM d, yyyy"))
        }
    } catch (e: Exception) {
        dateSent
    }
}

@Composable
fun NotificationBell() {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val token = "Bearer ${sessionManager.getToken()}"
    val scope = rememberCoroutineScope()

    var notifications by remember { mutableStateOf(listOf<Notification>()) }
    var open by remember { mutableStateOf(false) }

    suspend fun fetchNotifications() {
        try {
            val response = RetrofitClient.apiService.getMyNotifications(token)
            if (response.isSuccessful) notifications = response.body() ?: emptyList()
        } catch (e: Exception) {
            // Silently fail on poll errors - not critical enough for a toast
        }
    }

    LaunchedEffect(Unit) {
        while (true) {
            fetchNotifications()
            delay(POLL_INTERVAL_MS)
        }
    }

    val unreadCount = notifications.count { it.status == "UNREAD" }

    Box {
        Box {
            IconButton(onClick = { open = !open }, modifier = Modifier.size(40.dp)) {
                Text("🔔", fontSize = 20.sp)
            }
            if (unreadCount > 0) {
                Surface(
                    color = PawWatchColors.DeleteRed,
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = (-2).dp, y = 2.dp)
                ) {
                    Text(
                        if (unreadCount > 9) "9+" else "$unreadCount",
                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp),
                        fontSize = 10.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        DropdownMenu(expanded = open, onDismissRequest = { open = false }) {
            Text(
                "Notifications",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = PawWatchColors.TextDark,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            if (notifications.isEmpty()) {
                Text(
                    "No notifications yet.",
                    fontSize = 13.sp,
                    color = PawWatchColors.TextGray,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                )
            } else {
                notifications.forEach { notification ->
                    val isUnread = notification.status == "UNREAD"
                    DropdownMenuItem(
                        text = {
                            Column(modifier = Modifier.widthIn(max = 260.dp)) {
                                Text(
                                    notification.message,
                                    fontSize = 13.sp,
                                    fontWeight = if (isUnread) FontWeight.Bold else FontWeight.Normal,
                                    color = PawWatchColors.TextDark
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    formatRelativeTime(notification.dateSent),
                                    fontSize = 11.sp,
                                    color = PawWatchColors.TextGray
                                )
                            }
                        },
                        onClick = {
                            if (isUnread) {
                                scope.launch {
                                    try {
                                        RetrofitClient.apiService.markNotificationAsRead(token, notification.notificationId)
                                        notifications = notifications.map {
                                            if (it.notificationId == notification.notificationId) it.copy(status = "READ") else it
                                        }
                                    } catch (e: Exception) {
                                        // Ignore - not critical
                                    }
                                }
                            }
                        },
                        modifier = Modifier.background(
                            if (isUnread) PawWatchColors.Background else Color.Transparent
                        )
                    )
                }
            }
        }
    }
}
