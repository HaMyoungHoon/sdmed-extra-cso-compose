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
fun vectorPlus(data: FVectorData = FVectorData()): ImageVector {
    data.imageVectorViewportWidth = viewportWidth
    data.imageVectorViewportHeight = viewportHeight
    return fVectorBase(data).apply {
        addPath(fill = SolidColor(data.fillColor),
            pathData = PathParser().parsePathString("M440,520L240,520Q223,520 211.5,508.5Q200,497 200,480Q200,463 211.5,451.5Q223,440 240,440L440,440L440,240Q440,223 451.5,211.5Q463,200 480,200Q497,200 508.5,211.5Q520,223 520,240L520,440L720,440Q737,440 748.5,451.5Q760,463 760,480Q760,497 748.5,508.5Q737,520 720,520L520,520L520,720Q520,737 508.5,748.5Q497,760 480,760Q463,760 451.5,748.5Q440,737 440,720L440,520").toNodes())
    }.build()
}
@Composable
fun vectorPlus(data: FVectorData = FVectorData(), size: Dp) {
    fVectorBase(vectorPlus(data), data, size)
}

//@Preview
@Composable
private fun previewPlus(@PreviewParameter(PreviewColorProvider::class) data: Pair<Color, Color>) {
    Surface { vectorPlus(FVectorData().apply {
        tintColor = data.first
        fillColor = data.second
    }, 24.dp) }
}