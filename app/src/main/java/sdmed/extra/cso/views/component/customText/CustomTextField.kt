package sdmed.extra.cso.views.component.customText

import androidx.compose.foundation.background
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontSynthesis
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import sdmed.extra.cso.R
import sdmed.extra.cso.views.theme.FThemeUtil
import sdmed.extra.cso.views.theme.FLightColor

@Composable
fun customTextField(data: CustomTextFieldData) {
    customTextField(data.text,
        data.onValueChange,
        data.modifier,
        data.enable,
        data.readonly,
        data.textColor,
        data.textSize,
        data.fontWeight,
        data.fontStyle,
        data.fontFamily,
        data.textAlign,
        data.keyboardType,
        data.onDone,
        data.singleLine,
        data.maxLines,
        data.minLines,
        data.visualTransformation,
        data.decorationBox,
        data.backgroundTint)
}
@Composable
fun customTextField(text: String = "",
                    onValueChange: (String) -> Unit = { },
                    modifier: Modifier = Modifier,
                    enable: Boolean = true,
                    readonly: Boolean = false,
                    textColor: Color = FLightColor.foreground,
                    textSize: TextUnit = FThemeUtil.textUnit(18F),
                    fontWeight: FontWeight = FontWeight.Normal,
                    fontStyle: FontStyle = FontStyle.Normal,
                    fontFamily: FontFamily = FontFamily(Font(R.font.nanum_gothic)),
                    textAlign: TextAlign = TextAlign.Start,
                    keyboardType: KeyboardType = KeyboardType.Text,
                    onDone: (KeyboardActionScope.() -> Unit)? = null,
                    singleLine: Boolean = false,
                    maxLines: Int = 1,
                    minLines: Int = 1,
                    visualTransformation: VisualTransformation = VisualTransformation.None,
                    decorationBox: @Composable (@Composable () -> Unit) -> Unit = @Composable { innerTextField -> innerTextField() },
                    backgroundTint: Color = FLightColor.transparent) {
    val focusManager = LocalFocusManager.current
    BasicTextField(text, onValueChange, modifier.background(backgroundTint), enable, readonly,
        LocalTextStyle.current.copy(textColor,
            textSize,
            fontWeight,
            fontStyle,
            FontSynthesis.None,
            fontFamily,
            textAlign = textAlign,
        ),
        KeyboardOptions.Default.copy(imeAction = ImeAction.Done,
            keyboardType = keyboardType
        ),
        KeyboardActions( {
            if (onDone == null) {
                focusManager.clearFocus()
            } else {
                onDone(this)
            }
        }),
        singleLine,
        maxLines,
        minLines,
        visualTransformation,
        decorationBox = decorationBox
    )
}