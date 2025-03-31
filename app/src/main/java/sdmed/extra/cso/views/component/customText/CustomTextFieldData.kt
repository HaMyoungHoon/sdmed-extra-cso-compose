package sdmed.extra.cso.views.component.customText

import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import sdmed.extra.cso.R
import sdmed.extra.cso.views.theme.FThemeUtil

data class CustomTextFieldData(
    var text: String = "",
    var onValueChange: (String) -> Unit = { },
    var modifier: Modifier = Modifier,
    var enable: Boolean = true,
    var readonly: Boolean = false,
    var textColor: Color = Color.Black,
    var textSize: TextUnit = FThemeUtil.textUnit(18F),
    var fontWeight: FontWeight = FontWeight.Normal,
    var fontStyle: FontStyle = FontStyle.Normal,
    var fontFamily: FontFamily = FontFamily(Font(R.font.nanum_gothic)),
    var textAlign: TextAlign = TextAlign.Start,
    var keyboardType: KeyboardType = KeyboardType.Text,
    var onDone: (KeyboardActionScope.() -> Unit)? = null,
    var singleLine: Boolean = false,
    var maxLines: Int = 1,
    var minLines: Int = 1,
    var visualTransformation: VisualTransformation = VisualTransformation.None,
    var decorationBox: @Composable (@Composable () -> Unit) -> Unit = @Composable { innerTextField -> innerTextField() },
    var backgroundTint: Color = Color.Transparent
) {
}