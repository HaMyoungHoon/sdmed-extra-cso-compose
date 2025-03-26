package sdmed.extra.cso.views.component.vector

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import sdmed.extra.cso.views.theme.DarkColor
import sdmed.extra.cso.views.theme.LightColor

class PreviewColorProvider: PreviewParameterProvider<Pair<Color, Color>> {
    override val values = sequenceOf(
        Pair(LightColor.background, LightColor.foreground),
        Pair(LightColor.background, LightColor.primary),
        Pair(LightColor.background, LightColor.gray),
        Pair(DarkColor.background, DarkColor.foreground),
        Pair(DarkColor.background, DarkColor.primary),
        Pair(DarkColor.background, DarkColor.gray)
    )
}