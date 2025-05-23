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

fun vectorMenuEDI(data: FVectorData = FVectorData()): ImageVector {
    return fVectorBase(data).apply {
        addPath(fill = SolidColor(data.fillColor),
            pathData = PathParser().parsePathString("M20,7h-5V4c0,-1.1 -0.9,-2 -2,-2h-2C9.9,2 9,2.9 9,4v3H4C2.9,7 2,7.9 2,9v11c0,1.1 0.9,2 2,2h16c1.1,0 2,-0.9 2,-2V9C22,7.9 21.1,7 20,7").toNodes())
        addPath(fill = SolidColor(data.tintColor),
            pathData = PathParser().parsePathString("M11,4h2v5h-2V4").toNodes())
        addPath(fill = SolidColor(data.tintColor),
            pathData = PathParser().parsePathString("M11,16H9v2H7v-2H5v-2h2v-2h2v2h2V16").toNodes())
        addPath(fill = SolidColor(data.tintColor),
            pathData = PathParser().parsePathString("M13,14.5V13h6v1.5H13").toNodes())
        addPath(fill = SolidColor(data.tintColor),
            pathData = PathParser().parsePathString("M13,17.5V16h4v1.5H13").toNodes())
    }.build()
}
@Composable
fun vectorMenuEDI(data: FVectorData = FVectorData(), size: Dp) {
    fVectorBase(vectorMenuEDI(data), data, size)
}

//@Preview
@Composable
private fun previewMenuEDI(@PreviewParameter(PreviewColorProvider::class) data: Pair<Color, Color>) {
    Surface { vectorMenuEDI(FVectorData().apply {
        tintColor = data.first
        fillColor = data.second
    }, 24.dp) }
}