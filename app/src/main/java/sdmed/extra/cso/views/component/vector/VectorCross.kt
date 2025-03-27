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

fun vectorCross(data: FVectorData = FVectorData()): ImageVector {
    return fVectorBase(data).apply {
        addPath(fill = SolidColor(data.fillColor),
            pathData = PathParser().parsePathString("M18.3,5.71c-0.39,-0.39 -1.02,-0.39 -1.41,0L12,10.59 7.11,5.7c-0.39,-0.39 -1.02,-0.39 -1.41,0 -0.39,0.39 -0.39,1.02 0,1.41L10.59,12 5.7,16.89c-0.39,0.39 -0.39,1.02 0,1.41 0.39,0.39 1.02,0.39 1.41,0L12,13.41l4.89,4.89c0.39,0.39 1.02,0.39 1.41,0 0.39,-0.39 0.39,-1.02 0,-1.41L13.41,12l4.89,-4.89c0.38,-0.38 0.38,-1.02 0,-1.4").toNodes())
    }.build()
}
@Composable
fun vectorCross(data: FVectorData = FVectorData(), size: Dp) {
    fVectorBase(vectorCross(data), data, size)
}
@Preview
@Composable
private fun previewScreen(@PreviewParameter(PreviewColorProvider::class) data: Pair<Color, Color>) {
    Surface { vectorCross(FVectorData().apply {
        tintColor = data.first
        fillColor = data.second
    }, 24.dp) }
}