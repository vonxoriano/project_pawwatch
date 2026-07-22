package edu.cit.soriano.pawwatch.mobile.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.cit.soriano.pawwatch.mobile.ui.theme.PawWatchColors

/**
 * Shared labeled text field.
 *
 * Two label conventions coexist in the app today: Login renders the label as
 * separate [Text] above the field with custom input colors, while every other
 * screen relies on Material3's built-in floating [label] slot with default
 * colors. [useFloatingLabel] lets each call site keep reproducing its own
 * existing look exactly instead of forcing one convention on every screen.
 */
@Composable
fun LabeledTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    useFloatingLabel: Boolean = true,
    isPassword: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    enabled: Boolean = true,
    // Matches androidx.compose.material3.OutlinedTextField's own default —
    // no call site in this codebase set singleLine explicitly before this
    // extraction, so this preserves existing behavior exactly.
    singleLine: Boolean = false,
    minLines: Int = 1,
    shape: Shape = RoundedCornerShape(10.dp),
    colors: TextFieldColors? = null
) {
    val visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None
    val fieldColors = colors ?: OutlinedTextFieldDefaults.colors()

    if (useFloatingLabel) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            enabled = enabled,
            singleLine = singleLine,
            minLines = minLines,
            visualTransformation = visualTransformation,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            shape = shape,
            colors = fieldColors,
            modifier = modifier.fillMaxWidth()
        )
    } else {
        Column(modifier = modifier.fillMaxWidth()) {
            Text(
                label,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = PawWatchColors.TextDark,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                enabled = enabled,
                singleLine = singleLine,
                minLines = minLines,
                visualTransformation = visualTransformation,
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                shape = shape,
                colors = fieldColors,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

/** Colors matching Login's current custom input styling (unfocused/focused container + border). */
@Composable
fun loginFieldColors(): TextFieldColors = OutlinedTextFieldDefaults.colors(
    unfocusedContainerColor = PawWatchColors.InputBg,
    focusedContainerColor = PawWatchColors.InputBg,
    unfocusedBorderColor = Color.Transparent,
    focusedBorderColor = PawWatchColors.Primary
)
