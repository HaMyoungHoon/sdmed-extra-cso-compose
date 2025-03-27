package sdmed.extra.cso.views.component.vector

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import sdmed.extra.cso.views.theme.FLightColor

data class FVectorData(
    var tintColor: Color = FLightColor.background,
    var fillColor: Color = FLightColor.primary,
    var imageVectorViewportWidth: Float = 24F,
    var imageVectorViewportHeight: Float = 24F,
    var imageVectorDefaultWidth: Dp = 24.dp,
    var imageVectorDefaultHeight: Dp = 24.dp,
    var imageVectorName: String = "",
    var iconContentDescription: String? = null,
    var iconTint: Color = Color.Unspecified
) {
    constructor(tintColor: Color, fillColor: Color): this() {
        this.tintColor = tintColor
        this.fillColor = fillColor
    }
}