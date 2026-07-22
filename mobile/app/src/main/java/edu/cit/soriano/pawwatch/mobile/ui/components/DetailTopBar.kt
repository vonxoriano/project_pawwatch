package edu.cit.soriano.pawwatch.mobile.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import edu.cit.soriano.pawwatch.mobile.ui.theme.PawWatchColors

/**
 * Orange back-navigation top bar used on drill-down screens (animal detail,
 * apply-for-adoption, my-applications). Sibling to [TopBar], which serves the
 * different root/tab case (profile + logout actions, no back arrow).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailTopBar(title: String, onBack: () -> Unit) {
    TopAppBar(
        title = { Text(title) },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, "Back")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = PawWatchColors.Primary,
            titleContentColor = Color.White,
            navigationIconContentColor = Color.White
        )
    )
}
