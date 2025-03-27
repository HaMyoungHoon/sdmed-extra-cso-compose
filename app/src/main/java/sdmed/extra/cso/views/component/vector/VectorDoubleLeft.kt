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
fun vectorDoubleLeft(data: FVectorData = FVectorData()): ImageVector {
    data.imageVectorViewportWidth = viewportWidth
    data.imageVectorViewportHeight = viewportHeight
    return fVectorBase(data).apply {
        addPath(fill = SolidColor(data.fillColor),
            pathData = PathParser().parsePathString("M313,480L468,636Q479,647 479.5,663.5Q480,680 468,692Q457,703 440,703Q423,703 412,692L228,508Q222,502 219.5,495Q217,488 217,480Q217,472 219.5,465Q222,458 228,452L412,268Q423,257 439.5,256.5Q456,256 468,268Q479,279 479,296Q479,313 468,324L313,480").toNodes())
        addPath(fill = SolidColor(data.fillColor),
            pathData = PathParser().parsePathString("M577,480L732,636Q743,647 743.5,663.5Q744,680 732,692Q721,703 704,703Q687,703 676,692L492,508Q486,502 483.5,495Q481,488 481,480Q481,472 483.5,465Q486,458 492,452L676,268Q687,257 703.5,256.5Q720,256 732,268Q743,279 743,296Q743,313 732,324L577,480").toNodes())
    }.build()
}
@Composable
fun vectorDoubleLeft(data: FVectorData = FVectorData(), size: Dp) {
    fVectorBase(vectorDoubleRight(data), data, size)
}

@Preview
@Composable
private fun previewScreen(@PreviewParameter(PreviewColorProvider::class) data: Pair<Color, Color>) {
    Surface { vectorDoubleLeft(FVectorData().apply {
        tintColor = data.first
        fillColor = data.second
    }, 24.dp) }
}