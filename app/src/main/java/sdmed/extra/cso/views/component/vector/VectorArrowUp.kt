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
fun vectorArrowUp(data: FVectorData = FVectorData()): ImageVector {
    data.imageVectorViewportWidth = viewportWidth
    data.imageVectorViewportHeight = viewportHeight
    return fVectorBase(data).apply {
        addPath(fill = SolidColor(data.fillColor),
            pathData = PathParser().parsePathString("M480,432L324,588Q313,599 296,599Q279,599 268,588Q257,577 257,560Q257,543 268,532L452,348Q464,336 480,336Q496,336 508,348L692,532Q703,543 703,560Q703,577 692,588Q681,599 664,599Q647,599 636,588L480,432").toNodes())
    }.build()
}
@Composable
fun vectorArrowUp(data: FVectorData = FVectorData(), size: Dp) {
    fVectorBase(vectorArrowUp(data), data, size)
}

//@Preview
@Composable
private fun previewScreen(@PreviewParameter(PreviewColorProvider::class) data: Pair<Color, Color>) {
    Surface { vectorArrowUp(FVectorData().apply {
        tintColor = data.first
        fillColor = data.second
    }, 24.dp) }
}
