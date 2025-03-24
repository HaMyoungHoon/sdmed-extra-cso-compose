package sdmed.extra.cso.views.component.vector

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp

class VectorFile: FVectorBase() {
    override var viewportWidth = 24F
    override var viewportHeight = 24F
    override fun vector(tintColor: Color, fillColor: Color): ImageVector {
        return ImageVector.Builder(name, defaultWidth, defaultHeight, viewportWidth, viewportHeight).apply {
            addPath(fill = SolidColor(fillColor),
                pathData = PathParser().parsePathString("M13.17,2H6C4.9,2 4,2.9 4,4v16c0,1.1 0.9,2 2,2h9v-6c0,-1.1 0.9,-2 2,-2h3V8.83c0,-0.53 -0.21,-1.04 -0.59,-1.41l-4.83,-4.83C14.21,2.21 13.7,2 13.17,2").toNodes())
            addPath(fill = SolidColor(tintColor),
                pathData = PathParser().parsePathString("M13,8V3.5L18.5,9H14C13.45,9 13,8.55 13,8").toNodes())
            addPath(fill = SolidColor(fillColor),
                pathData = PathParser().parsePathString("M22.66,17c0,0.55 -0.45,1 -1,1h-1.24l2.24,2.24c0.39,0.39 0.39,1.02 0,1.41l0,0c-0.39,0.39 -1.02,0.39 -1.41,0L19,19.41l0,1.24c0,0.55 -0.45,1 -1,1c-0.55,0 -1,-0.45 -1,-1V17c0,-0.55 0.45,-1 1,-1h3.66C22.21,16 22.66,16.45 22.66,17").toNodes())
        }.build()
    }

    @Preview
    @Composable
    fun previewFile(@PreviewParameter(PreviewColorProvider::class) data: Pair<Color, Color>) {
        Surface { screen(data.first, data.second, 24.dp) }
    }
}