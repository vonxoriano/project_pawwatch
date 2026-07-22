package edu.cit.soriano.pawwatch.mobile.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import edu.cit.soriano.pawwatch.mobile.ui.theme.PawWatchColors

/**
 * Full-width primary submit button (Login, Register, Profile save/change-
 * password, adoption submit, "Adopt Me", browse Search).
 *
 * Defaults intentionally match Material3's own `Button` defaults for shape,
 * height and text weight/size, since most existing call sites never
 * overrode them. Only [containerColor] defaults to [PawWatchColors].Primary,
 * since a majority of call sites do set that explicitly. Call sites that
 * need Login's bespoke look (bold text, 48dp height, 10dp rounded corners)
 * or Register/Profile-Save's un-orange default color pass those explicitly
 * so every screen keeps rendering exactly as it does today.
 */
@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier.fillMaxWidth(),
    enabled: Boolean = true,
    containerColor: Color = PawWatchColors.Primary,
    shape: Shape = ButtonDefaults.shape,
    buttonHeight: Dp? = null,
    fontWeight: FontWeight = FontWeight.Normal,
    fontSize: TextUnit = TextUnit.Unspecified
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = shape,
        colors = ButtonDefaults.buttonColors(containerColor = containerColor),
        modifier = if (buttonHeight != null) modifier.height(buttonHeight) else modifier
    ) {
        Text(text, fontWeight = fontWeight, fontSize = fontSize)
    }
}
