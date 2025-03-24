package sdmed.extra.cso.views.component.shape

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import sdmed.extra.cso.views.component.vector.PreviewColorProvider

class ShapeRoundedBox: FShapeBase() {
    @Composable
    fun screen(backgroundColor: Color,
               borderColor: Color = Color.Transparent,
               modifier: Modifier = Modifier,
               roundedSize: Dp = 5.dp,
               borderSize: Dp = 0.dp,
               padding: Dp = 0.dp, content: @Composable BoxScope.() -> Unit) {
        val shape = RoundedCornerShape(roundedSize)
        val border = BorderStroke(borderSize, borderColor)
        val modifier = modifier.background(backgroundColor).border(border, shape).padding(padding)
        Box(modifier) {
            content()
        }
    }

    @Preview
    @Composable
    fun previewScreen(@PreviewParameter(PreviewColorProvider::class) data: Pair<Color, Color>) {
        screen(data.second) {
        }
    }
}