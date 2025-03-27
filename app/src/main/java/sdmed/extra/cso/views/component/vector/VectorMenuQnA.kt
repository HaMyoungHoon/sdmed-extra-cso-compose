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
fun vectorMenuQnA(data: FVectorData = FVectorData()): ImageVector {
    data.imageVectorViewportWidth = viewportWidth
    data.imageVectorViewportHeight = viewportHeight
    return fVectorBase(data).apply {
        addPath(fill = SolidColor(data.fillColor),
            pathData = PathParser().parsePathString("M480,920L360,800L200,800Q167,800 143.5,776.5Q120,753 120,720L120,160Q120,127 143.5,103.5Q167,80 200,80L760,80Q793,80 816.5,103.5Q840,127 840,160L840,720Q840,753 816.5,776.5Q793,800 760,800L600,800L480,920").toNodes())
        addPath(fill = SolidColor(data.tintColor),
            pathData = PathParser().parsePathString("M200,720L392,720L480,808L568,720L760,720Q760,720 760,720Q760,720 760,720L760,160Q760,160 760,160Q760,160 760,160L200,160Q200,160 200,160Q200,160 200,160L200,720Q200,720 200,720Q200,720 200,720").toNodes())
        addPath(fill = SolidColor(data.tintColor),
            pathData = PathParser().parsePathString("M480,440Q480,440 480,440Q480,440 480,440L480,440Q480,440 480,440Q480,440 480,440L480,440Q480,440 480,440Q480,440 480,440L480,440Q480,440 480,440Q480,440 480,440L480,440L480,440L480,440").toNodes())
        addPath(fill = SolidColor(data.fillColor),
            pathData = PathParser().parsePathString("M476,680Q497,680 511.5,665.5Q526,651 526,630Q526,609 511.5,594.5Q497,580 476,580Q455,580 440.5,594.5Q426,609 426,630Q426,651 440.5,665.5Q455,680 476,680").toNodes())
        addPath(fill = SolidColor(data.fillColor),
            pathData = PathParser().parsePathString("M440,526L514,526Q514,509 515.5,497Q517,485 522,474Q527,463 534.5,453.5Q542,444 556,430Q591,395 605.5,371.5Q620,348 620,318Q620,265 584,232.5Q548,200 487,200Q432,200 393.5,227Q355,254 340,302L406,328Q413,301 434,284.5Q455,268 483,268Q510,268 528,282.5Q546,297 546,321Q546,338 535,357Q524,376 498,399Q481,413 470.5,426.5Q460,440 453,455Q446,470 443,486.5Q440,503 440,526").toNodes())
    }.build()
}
@Composable
fun vectorMenuQnA(data: FVectorData = FVectorData(), size: Dp) {
    fVectorBase(vectorMenuQnA(data), data, size)
}

//@Preview
@Composable
private fun previewMenuQnA(@PreviewParameter(PreviewColorProvider::class) data: Pair<Color, Color>) {
    Surface { vectorMenuQnA(FVectorData(data.first, data.second), 24.dp) }
}
