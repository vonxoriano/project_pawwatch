package edu.cit.soriano.pawwatch.mobile.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StatusBadge(status: String) {
    val bgColor = when (status) {
        "AVAILABLE", "APPROVED" -> Color(0xFFE8F5E9)
        "PENDING" -> Color(0xFFFFF8E1)
        else -> Color(0xFFF5F5F5)
    }
    val textColor = when (status) {
        "AVAILABLE", "APPROVED" -> Color(0xFF2E7D32)
        "PENDING" -> Color(0xFFF57F17)
        else -> Color.Gray
    }
    Surface(color = bgColor, shape = RoundedCornerShape(20.dp)) {
        Text(
            status,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}