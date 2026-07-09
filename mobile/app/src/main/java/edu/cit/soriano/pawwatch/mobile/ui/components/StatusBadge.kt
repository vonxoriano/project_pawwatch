package edu.cit.soriano.pawwatch.mobile.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.cit.soriano.pawwatch.mobile.ui.theme.PawWatchColors

@Composable
fun StatusBadge(status: String) {
    val (bg, text) = when (status.uppercase()) {
        "AVAILABLE", "APPROVED" -> PawWatchColors.StatusAvailableBg to PawWatchColors.StatusAvailableText
        "PENDING" -> PawWatchColors.StatusPendingBg to PawWatchColors.StatusPendingText
        else -> PawWatchColors.StatusAdoptedBg to PawWatchColors.StatusAdoptedText
    }
    Surface(color = bg, shape = RoundedCornerShape(20.dp)) {
        Text(
            text = status.uppercase(),
            color = text,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}