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
fun vectorLock(data: FVectorData = FVectorData()): ImageVector {
    return fVectorBase(data).apply {
        addPath(fill = SolidColor(data.fillColor), fillAlpha = alpha,
            pathData = PathParser().parsePathString("M18,8h-1L17,6c0,-2.76 -2.24,-5 -5,-5S7,3.24 7,6v2L6,8c-1.1,0 -2,0.9 -2,2v10c0,1.1 0.9,2 2,2h12c1.1,0 2,-0.9 2,-2L20,10c0,-1.1 -0.9,-2 -2,-2").toNodes())
        addPath(fill = SolidColor(data.tintColor),
            pathData = PathParser().parsePathString("M12,17c-1.1,0 -2,-0.9 -2,-2s0.9,-2 2,-2 2,0.9 2,2 -0.9,2 -2,2").toNodes())
        addPath(fill = SolidColor(data.tintColor),
            pathData = PathParser().parsePathString("M15.1,8L8.9,8L8.9,6c0,-1.71 1.39,-3.1 3.1,-3.1 1.71,0 3.1,1.39 3.1,3.1v2").toNodes())
    }.build()
}
@Composable
fun vectorLock(data: FVectorData = FVectorData(), size: Dp) {
    fVectorBase(vectorLock(data), data, size)
}

//@Preview
@Composable
private fun previewLock(@PreviewParameter(PreviewColorProvider::class) data: Pair<Color, Color>) {
    Surface { vectorLock(FVectorData().apply {
        tintColor = data.first
        fillColor = data.second
    }, 24.dp) }
}