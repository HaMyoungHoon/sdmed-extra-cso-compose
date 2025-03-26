package sdmed.extra.cso.views.component.customText

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import sdmed.extra.cso.views.theme.DarkColor
import sdmed.extra.cso.views.theme.LightColor

class CustomTextProvider: PreviewParameterProvider<CustomTextData> {
    val all = 2.dp
    override val values = sequenceOf(
        CustomTextData().apply {
            text = "라이트 텍스트"
            textColor = LightColor.foreground
            backgroundTint = LightColor.background
            modifier = Modifier.padding(all)
        },
        CustomTextData().apply {
            text = "라이트 카드 텍스트"
            textColor = LightColor.cardForeground
            backgroundTint = LightColor.cardBackground
            modifier = Modifier.padding(all)
        },
        CustomTextData().apply {
            text = "라이트 카드 헤드 텍스트"
            textColor = LightColor.cardParagraph
            backgroundTint = LightColor.cardBackground
            modifier = Modifier.padding(all)
        },
        CustomTextData().apply {
            text = "라이트 버튼 텍스트"
            textColor = LightColor.buttonForeground
            backgroundTint = LightColor.buttonBackground
            modifier = Modifier.padding(all)
        },
        CustomTextData().apply {
            text = "다크 텍스트"
            textColor = DarkColor.foreground
            backgroundTint = DarkColor.background
            modifier = Modifier.padding(all)
        },
        CustomTextData().apply {
            text = "다크 카드 텍스트"
            textColor = DarkColor.cardForeground
            backgroundTint = DarkColor.cardBackground
            modifier = Modifier.padding(all)
        },
        CustomTextData().apply {
            text = "다크 카드 헤드 텍스트"
            textColor = DarkColor.cardParagraph
            backgroundTint = DarkColor.cardBackground
            modifier = Modifier.padding(all)
        },
        CustomTextData().apply {
            text = "다크 버튼 텍스트"
            textColor = DarkColor.buttonForeground
            backgroundTint = DarkColor.buttonBackground
            modifier = Modifier.padding(all)
        },
    )

    @Preview
    @Composable
    fun previewCustomTextField() {
        customTextField(CustomTextFieldData().apply {
            text = "preview"
            backgroundTint = LightColor.background
        })
    }
}