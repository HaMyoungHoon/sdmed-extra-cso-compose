package sdmed.extra.cso.views.component.customText

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import sdmed.extra.cso.R
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import sdmed.extra.cso.views.component.shape.ShapeRoundedBox
import sdmed.extra.cso.views.theme.FThemeUtil

class CustomText {
    @Composable
    fun screen(text: String,
               modifier: Modifier = Modifier,
               textColor: Color = FThemeUtil.baseColor().defForeground,
               textSize: TextUnit = TextUnit(16F, TextUnitType.Sp),
               backgroundTint: Color = FThemeUtil.baseColor().transparent,
               textAlign: TextAlign = TextAlign.Start,
               textDecoration: TextDecoration = TextDecoration.None,
               fontFamily: FontFamily = FontFamily(Font(R.font.nanum_gothic)),
               fontWeight: FontWeight = FontWeight.Normal,
               fontStyle: FontStyle = FontStyle.Normal) {
        Box(modifier.background(backgroundTint)) {
            Text(text, modifier,
                style = LocalTextStyle.current.copy(
                    color = textColor,
                    fontSize = textSize,
                    textAlign = textAlign,
                    fontFamily = fontFamily,
                    fontWeight = fontWeight,
                    fontStyle = fontStyle,
                ),
                textDecoration = textDecoration)
        }
    }
    @Composable
    fun screen(customTextData: CustomTextData) {
        Box(customTextData.modifier.background(customTextData.backgroundTint)) {
            Text(customTextData.text, customTextData.modifier,
                style = LocalTextStyle.current.copy(
                    color = customTextData.textColor,
                    fontSize = customTextData.textSize,
                    textAlign = customTextData.textAlign,
                    fontFamily = customTextData.fontFamily,
                    fontWeight = customTextData.fontWeight,
                    fontStyle = customTextData.fontStyle,
                ),
                textDecoration = customTextData.textDecoration)
        }
    }

    @Preview
    @Composable
    fun previewCustomText(@PreviewParameter(CustomTextProvider::class) data: CustomTextData) {
        screen(data)
    }

    @Preview
    @Composable
    fun previewCustomBoxText(@PreviewParameter(CustomTextProvider::class) data: CustomTextData) {
        ShapeRoundedBox().screen(data.backgroundTint, data.textColor) {
            screen(data)
        }
    }
}