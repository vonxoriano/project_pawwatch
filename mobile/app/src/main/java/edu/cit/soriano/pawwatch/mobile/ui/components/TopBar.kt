package edu.cit.soriano.pawwatch.mobile.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.cit.soriano.pawwatch.mobile.ui.features.notification.NotificationBell
import edu.cit.soriano.pawwatch.mobile.ui.theme.PawWatchColors

@Composable
fun TopBar(
    userLabel: String? = null,
    showNotifications: Boolean = false,
    onProfileClick: (() -> Unit)? = null,
    onLogout: () -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }

    Surface(color = Color.White, shadowElevation = 2.dp) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "🐾 PawWatch",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = PawWatchColors.Primary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f, fill = false)
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (showNotifications) {
                    NotificationBell()
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Box {
                    IconButton(onClick = { menuExpanded = true }) {
                        Icon(
                            Icons.Default.MoreVert,
                            contentDescription = "More options",
                            tint = PawWatchColors.Primary
                        )
                    }
                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false }
                    ) {
                        userLabel?.let {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        it,
                                        fontWeight = FontWeight.SemiBold,
                                        color = PawWatchColors.TextDark
                                    )
                                },
                                onClick = {},
                                enabled = false
                            )
                        }
                        onProfileClick?.let { onClick ->
                            DropdownMenuItem(
                                text = { Text("Profile") },
                                onClick = {
                                    menuExpanded = false
                                    onClick()
                                }
                            )
                        }
                        DropdownMenuItem(
                            text = { Text("Log Out") },
                            onClick = {
                                menuExpanded = false
                                onLogout()
                            }
                        )
                    }
                }
            }
        }
    }
}