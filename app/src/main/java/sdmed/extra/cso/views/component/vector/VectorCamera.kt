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

fun vectorCamera(data: FVectorData = FVectorData()): ImageVector {
    return fVectorBase(data).apply {
        addPath(fill = SolidColor(data.fillColor),
            pathData = PathParser().parsePathString("M12,12m-3,0a3,3 0,1 1,6 0a3,3 0,1 1,-6 0").toNodes())
        addPath(fill = SolidColor(data.fillColor),
            pathData = PathParser().parsePathString("M20,4h-3.17l-1.24,-1.35c-0.37,-0.41 -0.91,-0.65 -1.47,-0.65L9.88,2c-0.56,0 -1.1,0.24 -1.48,0.65L7.17,4L4,4c-1.1,0 -2,0.9 -2,2v12c0,1.1 0.9,2 2,2h16c1.1,0 2,-0.9 2,-2L22,6c0,-1.1 -0.9,-2 -2,-2").toNodes())
        addPath(fill = SolidColor(data.tintColor),
            pathData = PathParser().parsePathString("M12,17c-2.76,0 -5,-2.24 -5,-5s2.24,-5 5,-5 5,2.24 5,5 -2.24,5 -5,5").toNodes())
    }.build()
}
@Composable
fun vectorCamera(data: FVectorData = FVectorData(), size: Dp) {
    fVectorBase(vectorCamera(data), data, size)
}

//@Preview
@Composable
private fun previewScreen(@PreviewParameter(PreviewColorProvider::class) data: Pair<Color, Color>) {
    Surface { vectorCamera(FVectorData().apply {
        tintColor = data.first
        fillColor = data.second
    }, 24.dp) }
}