package sdmed.extra.cso.views.component.customText

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import sdmed.extra.cso.views.theme.FDarkColor
import sdmed.extra.cso.views.theme.FLightColor

class CustomTextProvider: PreviewParameterProvider<CustomTextData> {
    val all = 2.dp
    override val values = sequenceOf(
        CustomTextData().apply {
            text = "라이트 텍스트"
            textColor = FLightColor.foreground
            backgroundTint = FLightColor.background
            modifier = Modifier.padding(all)
        },
        CustomTextData().apply {
            text = "라이트 카드 텍스트"
            textColor = FLightColor.cardForeground
            backgroundTint = FLightColor.cardBackground
            modifier = Modifier.padding(all)
        },
        CustomTextData().apply {
            text = "라이트 카드 헤드 텍스트"
            textColor = FLightColor.cardParagraph
            backgroundTint = FLightColor.cardBackground
            modifier = Modifier.padding(all)
        },
        CustomTextData().apply {
            text = "라이트 버튼 텍스트"
            textColor = FLightColor.buttonForeground
            backgroundTint = FLightColor.buttonBackground
            modifier = Modifier.padding(all)
        },
        CustomTextData().apply {
            text = "다크 텍스트"
            textColor = FDarkColor.foreground
            backgroundTint = FDarkColor.background
            modifier = Modifier.padding(all)
        },
        CustomTextData().apply {
            text = "다크 카드 텍스트"
            textColor = FDarkColor.cardForeground
            backgroundTint = FDarkColor.cardBackground
            modifier = Modifier.padding(all)
        },
        CustomTextData().apply {
            text = "다크 카드 헤드 텍스트"
            textColor = FDarkColor.cardParagraph
            backgroundTint = FDarkColor.cardBackground
            modifier = Modifier.padding(all)
        },
        CustomTextData().apply {
            text = "다크 버튼 텍스트"
            textColor = FDarkColor.buttonForeground
            backgroundTint = FDarkColor.buttonBackground
            modifier = Modifier.padding(all)
        },
    )


//    @Preview
    @Composable
    fun previewCustomTextField() {
        customTextField(CustomTextFieldData().apply {
            text = "preview"
            backgroundTint = FLightColor.background
        })
    }
}