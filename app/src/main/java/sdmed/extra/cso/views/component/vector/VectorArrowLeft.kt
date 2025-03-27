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

fun vectorArrowLeft(data: FVectorData = FVectorData()): ImageVector {
    return fVectorBase(data).apply {
        addPath(fill = SolidColor(data.fillColor),
            pathData = PathParser().parsePathString("M16.62,2.99c-0.49,-0.49 -1.28,-0.49 -1.77,0L6.54,11.3c-0.39,0.39 -0.39,1.02 0,1.41l8.31,8.31c0.49,0.49 1.28,0.49 1.77,0s0.49,-1.28 0,-1.77L9.38,12l7.25,-7.25c0.48,-0.48 0.48,-1.28 -0.01,-1.76").toNodes())
    }.build()
}
@Composable
fun vectorArrowLeft(data: FVectorData = FVectorData(), size: Dp) {
    fVectorBase(vectorArrowLeft(data), data, size)
}

//@Preview
@Composable
private fun previewScreen(@PreviewParameter(PreviewColorProvider::class) data: Pair<Color, Color>) {
    Surface { vectorArrowLeft(FVectorData().apply {
        tintColor = data.first
        fillColor = data.second
    }, 24.dp) }
}
