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
fun vectorMenuHome(data: FVectorData = FVectorData()): ImageVector {
    data.imageVectorViewportWidth = viewportWidth
    data.imageVectorViewportHeight = viewportHeight
    return fVectorBase(data).apply {
        addPath(fill = SolidColor(data.fillColor),
            pathData = PathParser().parsePathString("M680,880Q597,880 538.5,821.5Q480,763 480,680Q480,597 538.5,538.5Q597,480 680,480Q763,480 821.5,538.5Q880,597 880,680Q880,763 821.5,821.5Q763,880 680,880").toNodes())
        addPath(fill = SolidColor(data.tintColor),
            pathData = PathParser().parsePathString("M747,775L775,747L700,672L700,560L660,560L660,688L747,775").toNodes())
        addPath(fill = SolidColor(data.fillColor),
            pathData = PathParser().parsePathString("M200,840Q167,840 143.5,816.5Q120,793 120,760L120,200Q120,167 143.5,143.5Q167,120 200,120L367,120Q378,85 410,62.5Q442,40 480,40Q520,40 551.5,62.5Q583,85 594,120L760,120Q793,120 816.5,143.5Q840,167 840,200L840,450Q822,437 802,428Q782,419 760,412L760,200Q760,200 760,200Q760,200 760,200L680,200L680,320L280,320L280,200L200,200Q200,200 200,200Q200,200 200,200L200,760Q200,760 200,760Q200,760 200,760L412,760Q419,782 428,802Q437,822 450,840L200,840").toNodes())
        addPath(fill = SolidColor(data.tintColor),
            pathData = PathParser().parsePathString("M480,200Q497,200 508.5,188.5Q520,177 520,160Q520,143 508.5,131.5Q497,120 480,120Q463,120 451.5,131.5Q440,143 440,160Q440,177 451.5,188.5Q463,200 480,200").toNodes())
    }.build()
}
@Composable
fun vectorMenuHome(data: FVectorData = FVectorData(), size: Dp) {
    fVectorBase(vectorMenuHome(data), data, size)
}

@Preview
@Composable
fun previewMenuHome(@PreviewParameter(PreviewColorProvider::class) data: Pair<Color, Color>) {
    Surface { vectorMenuHome(FVectorData(data.first, data.second), 24.dp) }
}