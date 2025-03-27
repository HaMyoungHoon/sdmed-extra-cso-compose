package sdmed.extra.cso.views.component.customText

import androidx.compose.foundation.background
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.LocalTextStyle
import sdmed.extra.cso.R
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontSynthesis
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.TextUnit
import sdmed.extra.cso.views.component.shape.shapeRoundedBox
import sdmed.extra.cso.views.theme.FThemeUtil
import sdmed.extra.cso.views.theme.FLightColor

@Composable
fun customText(data: CustomTextData) {
    customText(data.text, data.modifier, data.textColor, data.textSize, data.backgroundTint, data.textAlign, data.fontFamily, data.fontWeight, data.fontStyle, data.maxLines, data.overflow)
}
@Composable
fun customText(text: String,
               modifier: Modifier = Modifier,
               textColor: Color = FLightColor.foreground,
               textSize: TextUnit = FThemeUtil.textUnit(24F),
               backgroundTint: Color = FLightColor.transparent,
               textAlign: TextAlign = TextAlign.Start,
               fontFamily: FontFamily = FontFamily(Font(R.font.nanum_gothic)),
               fontWeight: FontWeight = FontWeight.Normal,
               fontStyle: FontStyle = FontStyle.Normal,
               maxLines: Int = 1,
               overflow: TextOverflow = TextOverflow.Visible) {
    BasicText(text, modifier.background(backgroundTint),
        LocalTextStyle.current.copy(textColor,
            textSize,
            fontWeight,
            fontStyle,
            FontSynthesis.None,
            fontFamily,
            textAlign = textAlign,
        ),
        null,
        overflow,
        true,
        maxLines)
}


@Preview
@Composable
fun previewCustomText(@PreviewParameter(CustomTextProvider::class) data: CustomTextData) {
    customText(data)
}

@Preview
@Composable
fun previewCustomBoxText(@PreviewParameter(CustomTextProvider::class) data: CustomTextData) {
    shapeRoundedBox(data.backgroundTint, data.textColor) {
        customText(data)
    }
}