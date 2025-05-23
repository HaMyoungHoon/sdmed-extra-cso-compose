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

private const val alpha = 0.76F
fun vectorPerson(data: FVectorData = FVectorData()): ImageVector {
    return fVectorBase(data).apply {
        addPath(fill = SolidColor(data.fillColor), fillAlpha = alpha,
            pathData = PathParser().parsePathString("M12,12c2.21,0 4,-1.79 4,-4s-1.79,-4 -4,-4 -4,1.79 -4,4 1.79,4 4,4").toNodes())
        addPath(fill = SolidColor(data.fillColor), fillAlpha = alpha,
            pathData = PathParser().parsePathString("M12,14c-2.67,0 -8,1.34 -8,4v2h16v-2c0,-2.66 -5.33,-4 -8,-4").toNodes())
    }.build()
}
@Composable
fun vectorPerson(data: FVectorData = FVectorData(), size: Dp) {
    fVectorBase(vectorPerson(data), data, size)
}

//@Preview
@Composable
private fun previewPerson(@PreviewParameter(PreviewColorProvider::class) data: Pair<Color, Color>) {
    Surface { vectorPerson(FVectorData(data.first, data.second), 24.dp) }
}