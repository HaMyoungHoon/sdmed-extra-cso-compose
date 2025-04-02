package sdmed.extra.cso.views.component.vector

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import sdmed.extra.cso.views.component.shape.ShapeRoundedBoxData
import sdmed.extra.cso.views.component.shape.shapeRoundedBox
import sdmed.extra.cso.views.theme.FLightColor


private const val viewportWidth = 960F
private const val viewportHeight = 960F
fun vectorEDINew1(data: FVectorData = FVectorData()): ImageVector {
    data.imageVectorViewportWidth = viewportWidth
    data.imageVectorViewportHeight = viewportHeight
    return fVectorBase(data).apply {
        addPath(fill = SolidColor(data.fillColor),
            pathData = PathParser().parsePathString("M40,840L40,120L600,120L600,840L360,840L360,680L280,680L280,840L40,840").toNodes())
        addPath(fill = SolidColor(data.tintColor),
            pathData = PathParser().parsePathString("M120,760L200,760L200,600L440,600L440,760L520,760L520,200L120,200L120,760").toNodes())
        addPath(fill = SolidColor(data.fillColor),
            pathData = PathParser().parsePathString("M200,520L280,520L280,440L200,440L200,520").toNodes())
        addPath(fill = SolidColor(data.fillColor),
            pathData = PathParser().parsePathString("M200,360L280,360L280,280L200,280L200,360").toNodes())
        addPath(fill = SolidColor(data.fillColor),
            pathData = PathParser().parsePathString("M360,520L440,520L440,440L360,440L360,520").toNodes())
        addPath(fill = SolidColor(data.fillColor),
            pathData = PathParser().parsePathString("M360,360L440,360L440,280L360,280L360,360").toNodes())
        addPath(fill = SolidColor(data.fillColor),
            pathData = PathParser().parsePathString("M780,620L724,564L767,520L640,520L640,440L767,440L724,396L780,340L920,480L780,620").toNodes())
        addPath(fill = SolidColor(data.fillColor),
            pathData = PathParser().parsePathString("M200,760L200,600L440,600L440,760L440,760L440,600L200,600L200,760").toNodes())
    }.build()
}
fun vectorEDINew2(data: FVectorData = FVectorData()): ImageVector {
    data.imageVectorViewportWidth = viewportWidth
    data.imageVectorViewportHeight = viewportHeight
    return fVectorBase(data).apply {
        addPath(fill = SolidColor(data.fillColor),
            pathData = PathParser().parsePathString("M480,480Q480,480 480,480Q480,480 480,480L480,480Q480,480 480,480Q480,480 480,480L480,480Q480,480 480,480Q480,480 480,480L480,480Q480,480 480,480Q480,480 480,480L480,480Q480,480 480,480Q480,480 480,480Q480,480 480,480Q480,480 480,480").toNodes())
        addPath(fill = SolidColor(data.fillColor),
            pathData = PathParser().parsePathString("M200,840Q167,840 143.5,816.5Q120,793 120,760L120,200Q120,167 143.5,143.5Q167,120 200,120L520,120Q520,137 520,157Q520,177 520,200L200,200Q200,200 200,200Q200,200 200,200L200,760Q200,760 200,760Q200,760 200,760L760,760Q760,760 760,760Q760,760 760,760L760,440Q783,440 803,440Q823,440 840,440L840,760Q840,793 816.5,816.5Q793,840 760,840L200,840").toNodes())
        addPath(fill = SolidColor(data.fillColor),
            pathData = PathParser().parsePathString("M240,680L720,680L570,480L450,640L360,520L240,680").toNodes())
        addPath(fill = SolidColor(data.fillColor),
            pathData = PathParser().parsePathString("M680,360L680,280L600,280L600,200L680,200L680,120L760,120L760,200L840,200L840,280L760,280L760,360L680,360").toNodes())
    }.build()
}

@Composable
fun vectorEDINew(modifier: Modifier = Modifier, data: FVectorData = FVectorData(), size: Dp) {
    val childSize = size / 3
    val childOffsetX = - size / 6
    Box(modifier.wrapContentSize()) {
        Icon(vectorEDINew1(FVectorData(data.tintColor, data.fillColor)), data.iconContentDescription,
            Modifier.size(size), Color.Unspecified)
        shapeRoundedBox(ShapeRoundedBoxData().apply {
            backgroundColor = data.tintColor
            roundedSize = 2.dp
            this.modifier = Modifier.size(childSize).offset(x = childOffsetX, y = 1.dp).align(Alignment.TopEnd)
        }) {
            Icon(vectorEDINew2(FVectorData(data.tintColor, data.fillColor)), data.iconContentDescription,
                Modifier.fillMaxSize(), Color.Unspecified)
        }
    }
}

//@Preview
@Composable
private fun previewScreen(@PreviewParameter(PreviewColorProvider::class) data: Pair<Color, Color>) {
    Surface(Modifier) {
        val tintColor = FLightColor.background
        val fillColor = FLightColor.foreground
        val size = 24.dp
        vectorEDINew(Modifier, FVectorData(tintColor, fillColor), size)
    }
}