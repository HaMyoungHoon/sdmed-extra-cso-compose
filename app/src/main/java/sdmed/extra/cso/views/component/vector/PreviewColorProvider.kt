package sdmed.extra.cso.views.component.vector

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import sdmed.extra.cso.views.theme.FDarkColor
import sdmed.extra.cso.views.theme.FLightColor

class PreviewColorProvider: PreviewParameterProvider<Pair<Color, Color>> {
    override val values = sequenceOf(
        Pair(FLightColor.background, FLightColor.foreground),
        Pair(FLightColor.background, FLightColor.primary),
        Pair(FLightColor.background, FLightColor.gray),
        Pair(FDarkColor.background, FDarkColor.foreground),
        Pair(FDarkColor.background, FDarkColor.primary),
        Pair(FDarkColor.background, FDarkColor.gray)
    )
}