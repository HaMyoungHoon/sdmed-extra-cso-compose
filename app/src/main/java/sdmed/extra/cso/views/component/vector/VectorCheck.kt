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
fun vectorCheck(data: FVectorData = FVectorData()): ImageVector {
    data.imageVectorViewportWidth = viewportWidth
    data.imageVectorViewportHeight = viewportHeight
    return fVectorBase(data).apply {
        addPath(fill = SolidColor(data.fillColor),
            pathData = PathParser().parsePathString("M424,552L338,466Q327,455 310,455Q293,455 282,466Q271,477 271,494Q271,511 282,522L396,636Q408,648 424,648Q440,648 452,636L678,410Q689,399 689,382Q689,365 678,354Q667,343 650,343Q633,343 622,354L424,552").toNodes())
    }.build()
}
@Composable
fun vectorCheck(data: FVectorData = FVectorData(), size: Dp) {
    fVectorBase(vectorCheck(data), data, size)
}

@Preview
@Composable
private fun previewScreen(@PreviewParameter(PreviewColorProvider::class) data: Pair<Color, Color>) {
    Surface { vectorCheck(FVectorData().apply {
        tintColor = data.first
        fillColor = data.second
    }, 24.dp) }
}