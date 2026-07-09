package edu.cit.soriano.pawwatch.mobile.ui.components

import androidx.compose.foundation.BorderStroke
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
import edu.cit.soriano.pawwatch.mobile.ui.theme.PawWatchColors

@Composable
fun TopBar(userLabel: String? = null, onLogout: () -> Unit) {
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
                color = PawWatchColors.Primary
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                userLabel?.let {
                    Text(it, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = PawWatchColors.TextDark)
                    Spacer(modifier = Modifier.width(12.dp))
                }
                OutlinedButton(
                    onClick = onLogout,
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = PawWatchColors.Primary),
                    border = BorderStroke(1.dp, PawWatchColors.Primary),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    Text("Log Out", fontSize = 13.sp)
                }
            }
        }
    }
}