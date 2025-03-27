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

fun vectorDelete(data: FVectorData = FVectorData()): ImageVector {
    return fVectorBase(data).apply {
        addPath(fill = SolidColor(data.fillColor),
            pathData = PathParser().parsePathString("M6,19c0,1.1 0.9,2 2,2h8c1.1,0 2,-0.9 2,-2V7H6V19").toNodes())
        addPath(fill = SolidColor(data.tintColor),
            pathData = PathParser().parsePathString("M9.17,12.59c-0.39,-0.39 -0.39,-1.02 0,-1.41c0.39,-0.39 1.02,-0.39 1.41,0L12,12.59l1.41,-1.41c0.39,-0.39 1.02,-0.39 1.41,0s0.39,1.02 0,1.41L13.41,14l1.41,1.41c0.39,0.39 0.39,1.02 0,1.41s-1.02,0.39 -1.41,0L12,15.41l-1.41,1.41c-0.39,0.39 -1.02,0.39 -1.41,0c-0.39,-0.39 -0.39,-1.02 0,-1.41L10.59,14L9.17,12.59").toNodes())
        addPath(fill = SolidColor(data.fillColor),
            pathData = PathParser().parsePathString("M18,4h-2.5l-0.71,-0.71C14.61,3.11 14.35,3 14.09,3H9.91c-0.26,0 -0.52,0.11 -0.7,0.29L8.5,4H6C5.45,4 5,4.45 5,5s0.45,1 1,1h12c0.55,0 1,-0.45 1,-1S18.55,4 18,4").toNodes())
    }.build()
}
@Composable
fun vectorDelete(data: FVectorData = FVectorData(), size: Dp) {
    fVectorBase(vectorDelete(data), data, size)
}
@Preview
@Composable
fun previewDelete(@PreviewParameter(PreviewColorProvider::class) data: Pair<Color, Color>) {
    Surface { vectorDelete(FVectorData(data.first, data.second), 24.dp) }
}