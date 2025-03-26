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

class VectorMenuOpen: FVectorBase() {
    override var viewportWidth = 960F
    override var viewportHeight = 960F
    override fun vector(tintColor: Color, fillColor: Color): ImageVector {
        return ImageVector.Builder(name, defaultWidth, defaultHeight, viewportWidth, viewportHeight).apply {
            addPath(fill = SolidColor(fillColor),
                pathData = PathParser().parsePathString("M120,720L120,640L640,640L640,720L120,720").toNodes())
            addPath(fill = SolidColor(fillColor),
                pathData = PathParser().parsePathString("M784,680L584,480L784,280L840,336L696,480L840,624L784,680").toNodes())
            addPath(fill = SolidColor(fillColor),
                pathData = PathParser().parsePathString("M120,520L120,440L520,440L520,520L120,520").toNodes())
            addPath(fill = SolidColor(fillColor),
                pathData = PathParser().parsePathString("M120,320L120,240L640,240L640,320L120,320").toNodes())
        }.build()
    }

    @Preview
    @Composable
    fun previewVectorMenu(@PreviewParameter(PreviewColorProvider::class) data: Pair<Color, Color>) {
        Surface { screen(data.first, data.second, 24.dp) }
    }
}