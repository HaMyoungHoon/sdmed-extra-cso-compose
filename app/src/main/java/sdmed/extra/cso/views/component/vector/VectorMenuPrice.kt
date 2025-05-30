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
fun vectorMenuPrice(data: FVectorData = FVectorData()): ImageVector {
    data.imageVectorViewportWidth = viewportWidth
    data.imageVectorViewportHeight = viewportHeight
    return fVectorBase(data).apply {
        addPath(fill = SolidColor(data.fillColor),
            pathData = PathParser().parsePathString("M80,800L80,160L880,160L880,800L80,800").toNodes())
        addPath(fill = SolidColor(data.tintColor),
            pathData = PathParser().parsePathString("M160,720L800,720L800,240L160,240L160,720").toNodes())
        addPath(fill = SolidColor(data.fillColor),
            pathData = PathParser().parsePathString("M160,720L160,240L160,240L160,720").toNodes())
        addPath(fill = SolidColor(data.fillColor),
            pathData = PathParser().parsePathString("M320,680L400,680L400,640L440,640Q457,640 468.5,628.5Q480,617 480,600L480,480Q480,463 468.5,451.5Q457,440 440,440L320,440L320,400L480,400L480,320L400,320L400,280L320,280L320,320L280,320Q263,320 251.5,331.5Q240,343 240,360L240,480Q240,497 251.5,508.5Q263,520 280,520L400,520L400,560L240,560L240,640L320,640L320,680").toNodes())
        addPath(fill = SolidColor(data.fillColor),
            pathData = PathParser().parsePathString("M640,650L720,570L560,570L640,650").toNodes())
        addPath(fill = SolidColor(data.fillColor),
            pathData = PathParser().parsePathString("M560,400L720,400L640,320L560,400").toNodes())
    }.build()
}
@Composable
fun vectorMenuPrice(data: FVectorData = FVectorData(), size: Dp) {
    fVectorBase(vectorMenuPrice(data), data, size)
}

//@Preview
@Composable
private fun previewMenuPrice(@PreviewParameter(PreviewColorProvider::class) data: Pair<Color, Color>) {
    Surface { vectorMenuPrice(FVectorData(data.first, data.second), 24.dp) }
}