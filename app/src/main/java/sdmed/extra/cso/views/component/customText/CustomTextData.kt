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
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import sdmed.extra.cso.R

data class CustomTextData(
    var text: String = "",
    var modifier: Modifier = Modifier.padding(0.dp),
    var textColor: Color = Color.Black,
    var textSize: TextUnit = TextUnit(24F, TextUnitType.Sp),
    var backgroundTint: Color = Color.Transparent,
    var textAlign: TextAlign = TextAlign.Start,
    var textDecoration: TextDecoration = TextDecoration.None,
    var fontFamily: FontFamily = FontFamily(Font(R.font.nanum_gothic)),
    var fontWeight: FontWeight = FontWeight.Normal,
    var fontStyle: FontStyle = FontStyle.Normal) {
}