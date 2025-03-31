package sdmed.extra.cso.views.component.customText

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import sdmed.extra.cso.R
import sdmed.extra.cso.views.theme.FThemeUtil

data class CustomTextData(
    var text: String = "",
    var modifier: Modifier = Modifier.padding(0.dp),
    var textColor: Color = Color.Black,
    var textSize: TextUnit = FThemeUtil.textUnit(18F),
    var backgroundTint: Color = Color.Transparent,
    var textAlign: TextAlign = TextAlign.Start,
    var fontFamily: FontFamily = FontFamily(Font(R.font.nanum_gothic)),
    var fontWeight: FontWeight = FontWeight.Normal,
    var fontStyle: FontStyle = FontStyle.Normal,
    var maxLines: Int = 1,
    var overflow: TextOverflow = TextOverflow.Visible) {
}