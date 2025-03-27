package sdmed.extra.cso.views.component.vector

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun fVectorBase(data: FVectorData = FVectorData()): ImageVector.Builder {
    return ImageVector.Builder(data.imageVectorName,
        data.imageVectorDefaultWidth,
        data.imageVectorDefaultHeight,
        data.imageVectorViewportWidth,
        data.imageVectorViewportHeight)
}
@Composable
fun fVectorBase(imageVector: ImageVector, data: FVectorData = FVectorData(), size: Dp = 24.dp) {
    Icon(imageVector, data.iconContentDescription, Modifier.size(size), data.iconTint)
}
open class FVectorBase {
    open var viewportWidth = 24F
    open var viewportHeight = 24F
    open var defaultWidth = 24.dp
    open var defaultHeight = 24.dp
    open var name = ""
    open fun getContentDescription(): String? = null
    open fun getTint(): Color = Color.Unspecified
    open fun vector(tintColor: Color, fillColor: Color): ImageVector {
        return ImageVector.Builder(name, defaultWidth, defaultHeight, viewportWidth, viewportHeight).apply {
            addPath(fill = SolidColor(fillColor),
                pathData = PathParser().parsePathString("").toNodes())
        }.build()
    }
    @Composable
    open fun screen(tintColor: Color, fillColor: Color, size: Dp) {
        val contentDescription = getContentDescription()
        val tint = getTint()
        val imageVector = vector(tintColor, fillColor)
        Icon(imageVector, contentDescription, Modifier.size(size), tint)
    }
}