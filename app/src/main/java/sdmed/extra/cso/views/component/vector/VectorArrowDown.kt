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
fun vectorArrowDown(data: FVectorData = FVectorData()): ImageVector {
    data.imageVectorViewportWidth = viewportWidth
    data.imageVectorViewportHeight = viewportHeight
    return fVectorBase(data).apply {
        addPath(fill = SolidColor(data.fillColor),
            pathData = PathParser().parsePathString("M480,599Q472,599 465,596.5Q458,594 452,588L268,404Q257,393 257,376Q257,359 268,348Q279,337 296,337Q313,337 324,348L480,504L636,348Q647,337 664,337Q681,337 692,348Q703,359 703,376Q703,393 692,404L508,588Q502,594 495,596.5Q488,599 480,599").toNodes())
    }.build()
}
@Composable
fun vectorArrowDown(data: FVectorData = FVectorData(), size: Dp) {
    fVectorBase(vectorArrowDown(data), data, size)
}
@Preview
@Composable
private fun previewScreen(@PreviewParameter(PreviewColorProvider::class) data: Pair<Color, Color>) {
    Surface { vectorArrowDown(FVectorData().apply {
        tintColor = data.first
        fillColor = data.second
    }, 24.dp) }
}