package edu.cit.soriano.pawwatch.mobile.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Generic single-select [FilterChip] row. Replaces the Own/Rent housing-type
 * row, the four Yes/No questionnaire rows on
 * [edu.cit.soriano.pawwatch.mobile.ui.features.application.ApplyApplicationScreen],
 * and the species/gender chip rows in the admin add/edit-animal dialog.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> ChoiceChipRow(
    options: List<Pair<String, T>>,
    selected: T?,
    onSelect: (T) -> Unit,
    modifier: Modifier = Modifier,
    spacing: Dp = 8.dp
) {
    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(spacing)) {
        options.forEach { (label, value) ->
            FilterChip(
                selected = selected == value,
                onClick = { onSelect(value) },
                label = { Text(label) }
            )
        }
    }
}
