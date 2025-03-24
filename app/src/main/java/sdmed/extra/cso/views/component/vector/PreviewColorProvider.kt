package sdmed.extra.cso.views.component.vector

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import sdmed.extra.cso.views.theme.DarkColor
import sdmed.extra.cso.views.theme.LightColor

class PreviewColorProvider: PreviewParameterProvider<Pair<Color, Color>> {
    override val values = sequenceOf(
        Pair(LightColor.defBackground, LightColor.defForeground),
        Pair(LightColor.defBackground, LightColor.primary),
        Pair(LightColor.defBackground, LightColor.gray),
        Pair(DarkColor.defBackground, DarkColor.defForeground),
        Pair(DarkColor.defBackground, DarkColor.primary),
        Pair(DarkColor.defBackground, DarkColor.gray)
    )
}