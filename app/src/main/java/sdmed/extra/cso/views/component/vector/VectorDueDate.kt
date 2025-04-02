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
fun vectorDueDate(data: FVectorData = FVectorData()): ImageVector {
    data.imageVectorViewportWidth = viewportWidth
    data.imageVectorViewportHeight = viewportHeight
    return fVectorBase(data).apply {
        addPath(fill = SolidColor(data.fillColor),
            pathData = PathParser().parsePathString("M200,880Q167,880 143.5,856.5Q120,833 120,800L120,240Q120,207 143.5,183.5Q167,160 200,160L240,160L240,80L320,80L320,160L640,160L640,80L720,80L720,160L760,160Q793,160 816.5,183.5Q840,207 840,240L840,800Q840,833 816.5,856.5Q793,880 760,880L200,880").toNodes())
        addPath(fill = SolidColor(data.fillColor),
            pathData = PathParser().parsePathString("M200,320L200,240Q200,240 200,240Q200,240 200,240L200,240Q200,240 200,240Q200,240 200,240L200,320").toNodes())
        addPath(fill = SolidColor(data.tintColor),
            pathData = PathParser().parsePathString("M200,800L760,800Q760,800 760,800Q760,800 760,800L760,400L200,400L200,800Q200,800 200,800Q200,800 200,800").toNodes())
        addPath(fill = SolidColor(data.fillColor),
            pathData = PathParser().parsePathString("M438,734L296,592L354,534L438,618L606,450L664,508L438,734").toNodes())
        addPath(fill = SolidColor(data.tintColor),
            pathData = PathParser().parsePathString("M200,320L760,320L760,240Q760,240 760,240Q760,240 760,240L200,240Q200,240 200,240Q200,240 200,240L200,320").toNodes())
    }.build()
}
@Composable
fun vectorDueDate(data: FVectorData = FVectorData(), size: Dp) {
    fVectorBase(vectorDueDate(data), data, size)
}

//@Preview
@Composable
private fun previewScreen(@PreviewParameter(PreviewColorProvider::class) data: Pair<Color, Color>) {
    Surface { vectorDueDate(FVectorData().apply {
        tintColor = data.first
        fillColor = data.second
    }, 24.dp) }
}