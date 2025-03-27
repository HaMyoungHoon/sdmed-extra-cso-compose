package sdmed.extra.cso.views.component.vector

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private const val viewportWidth = 960F
private const val viewportHeight = 960F
fun vectorDoubleRight(data: FVectorData = FVectorData()): ImageVector {
    data.imageVectorViewportWidth = viewportWidth
    data.imageVectorViewportHeight = viewportHeight
    return fVectorBase(data).apply {
        addPath(fill = SolidColor(data.fillColor),
            pathData = PathParser().parsePathString("M383,480L228,324Q217,313 216.5,296.5Q216,280 228,268Q239,257 256,257Q273,257 284,268L468,452Q474,458 476.5,465Q479,472 479,480Q479,488 476.5,495Q474,502 468,508L284,692Q273,703 256.5,703.5Q240,704 228,692Q217,681 217,664Q217,647 228,636L383,480").toNodes())
        addPath(fill = SolidColor(data.fillColor),
            pathData = PathParser().parsePathString("M647,480L492,324Q481,313 480.5,296.5Q480,280 492,268Q503,257 520,257Q537,257 548,268L732,452Q738,458 740.5,465Q743,472 743,480Q743,488 740.5,495Q738,502 732,508L548,692Q537,703 520.5,703.5Q504,704 492,692Q481,681 481,664Q481,647 492,636L647,480").toNodes())
    }.build()
}
@Composable
fun vectorDoubleRight(data: FVectorData = FVectorData(), size: Dp) {
    fVectorBase(vectorDoubleRight(data), data, size)
}

//@Preview
@Composable
private fun previewScreen(@PreviewParameter(PreviewColorProvider::class) data: Pair<Color, Color>) {
    Surface { vectorDoubleRight(FVectorData().apply {
        tintColor = data.first
        fillColor = data.second
    }, 24.dp) }
}