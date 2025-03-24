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

class VectorArrowRight: FVectorBase() {
    override var viewportWidth = 24F
    override var viewportHeight = 24F
    override fun vector(tintColor: Color, fillColor: Color): ImageVector {
        return ImageVector.Builder(name, defaultWidth, defaultHeight, viewportWidth, viewportHeight).apply {
            addPath(fill = SolidColor(fillColor),
                pathData = PathParser().parsePathString("M7.38,21.01c0.49,0.49 1.28,0.49 1.77,0l8.31,-8.31c0.39,-0.39 0.39,-1.02 0,-1.41L9.15,2.98c-0.49,-0.49 -1.28,-0.49 -1.77,0s-0.49,1.28 0,1.77L14.62,12l-7.25,7.25c-0.48,0.48 -0.48,1.28 0.01,1.76").toNodes())
        }.build()
    }

    @Preview
    @Composable
    fun previewScreen(@PreviewParameter(PreviewColorProvider::class) data: Pair<Color, Color>) {
        Surface { screen(data.first, data.second, 24.dp) }
    }
}