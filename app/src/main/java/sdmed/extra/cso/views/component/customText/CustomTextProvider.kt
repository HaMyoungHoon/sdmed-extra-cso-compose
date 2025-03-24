package sdmed.extra.cso.views.component.customText

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import sdmed.extra.cso.views.theme.DarkColor
import sdmed.extra.cso.views.theme.LightColor

class CustomTextProvider: PreviewParameterProvider<CustomTextData> {
    val all = 2.dp
    override val values = sequenceOf(
        CustomTextData().apply {
            text = "라이트 텍스트"
            textColor = LightColor.defForeground
            backgroundTint = LightColor.defBackground
            modifier = Modifier.padding(all)
        },
        CustomTextData().apply {
            text = "라이트 카드 텍스트"
            textColor = LightColor.defCardForeground
            backgroundTint = LightColor.defCardBackground
            modifier = Modifier.padding(all)
        },
        CustomTextData().apply {
            text = "라이트 카드 헤드 텍스트"
            textColor = LightColor.defCardParagraph
            backgroundTint = LightColor.defCardBackground
            modifier = Modifier.padding(all)
        },
        CustomTextData().apply {
            text = "라이트 버튼 텍스트"
            textColor = LightColor.defButtonForeground
            backgroundTint = LightColor.defButtonBackground
            modifier = Modifier.padding(all)
        },
        CustomTextData().apply {
            text = "다크 텍스트"
            textColor = DarkColor.defForeground
            backgroundTint = DarkColor.defBackground
            modifier = Modifier.padding(all)
        },
        CustomTextData().apply {
            text = "다크 카드 텍스트"
            textColor = DarkColor.defCardForeground
            backgroundTint = DarkColor.defCardBackground
            modifier = Modifier.padding(all)
        },
        CustomTextData().apply {
            text = "다크 카드 헤드 텍스트"
            textColor = DarkColor.defCardParagraph
            backgroundTint = DarkColor.defCardBackground
            modifier = Modifier.padding(all)
        },
        CustomTextData().apply {
            text = "다크 버튼 텍스트"
            textColor = DarkColor.defButtonForeground
            backgroundTint = DarkColor.defButtonBackground
            modifier = Modifier.padding(all)
        },
    )
}